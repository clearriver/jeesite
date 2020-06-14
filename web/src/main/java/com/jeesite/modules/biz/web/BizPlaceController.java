/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.web;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.utils.excel.annotation.ExcelField.Type;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.biz.entity.BizCyber;
import com.jeesite.modules.biz.entity.BizPlace;
import com.jeesite.modules.biz.service.BizPlaceService;
import com.jeesite.modules.biz.service.CyberService;
import com.jeesite.modules.sys.entity.Area;
import com.jeesite.modules.sys.entity.Config;
import com.jeesite.modules.sys.entity.EmpUser;
import com.jeesite.modules.sys.entity.Employee;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.service.ConfigService;
import com.jeesite.modules.sys.service.EmpUserService;
import com.jeesite.modules.sys.service.OfficeService;
import com.jeesite.modules.sys.utils.AreaUtils;
import com.jeesite.modules.sys.utils.ConfigUtils;
import com.jeesite.modules.sys.utils.UserUtils;

/**
 * 场所表Controller
 * @author 长江
 * @version 2019-01-12
 */
@Controller("BizPlaceController-v1")
@RequestMapping(value = "${adminPath}/biz/bizPlace")
public class BizPlaceController extends BaseController {
    @Autowired
    private CyberService cyberService;
	@Autowired
	private BizPlaceService bizPlaceService;
	@Autowired
	private ConfigService configService;
    @Autowired
    private EmpUserService empUserService;
    @Autowired
    private OfficeService officeService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public BizPlace get(String placeCode, boolean isNewRecord) {
	    BizPlace bp= bizPlaceService.get(placeCode, isNewRecord);
	    if(bp!=null&&bp.getPlaceCode()!=null) {
          BizCyber cyber=cyberService.getCybers().get(bp.getPlaceCode());
          bp.setCyberInstall(cyber!=null&&cyber.isInstall());
          bp.setCyberOnline(cyber!=null&&cyber.isOnline());
	    }
		return bp;
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("biz:bizPlace:view")
	@RequestMapping(value = {"list", ""})
	public String list(BizPlace bizPlace, Model model) {
		model.addAttribute("bizPlace", bizPlace);
		return "modules/biz/bizPlaceList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("biz:bizPlace:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<BizPlace> listData(BizPlace bizPlace, HttpServletRequest request, HttpServletResponse response) {
	    Page<BizPlace> page = null;
	    EmpUser empUser=empUserService.get(UserUtils.getUser().getUserCode());
	    if(empUser==null) {
	      bizPlace.setPage(new Page<>(request, response));
	      page = bizPlaceService.findPage(bizPlace);
	    }else {
	      page = new Page<>(request, response);
	      
	      Employee employee = empUser.getEmployee();
          String officeCode=employee.getOffice().getOfficeCode();
          officeCode=officeCode==null||officeCode.length()<=6?officeCode:officeCode.substring(0,6);
          Office where = new Office();
          where.getSqlMap().getWhere().andBracket("office_code", QueryType.EQ, officeCode, 1)
                .or("parent_codes", QueryType.LIKE,officeCode, 2).endBracket();
          List<Office> offices=officeService.findList(where);
          List<String> officeCodes=offices.stream().map(e->e.getOfficeCode()==null||e.getOfficeCode().length()<=6?e.getOfficeCode():e.getOfficeCode().substring(0,6)).collect(Collectors.toList());
          String city=bizPlace.getCity()==null?null:bizPlace.getCity().getAreaCode();
          String area=bizPlace.getArea()==null?null:bizPlace.getArea().getAreaCode();
          if(StringUtils.isNotBlank(city)) {
            final String tcity=city.substring(0,4);
            officeCodes=officeCodes.stream().filter(e->e.startsWith(tcity)).collect(Collectors.toList());
          }
          if(StringUtils.isNotBlank(area)) {
            officeCodes=officeCodes.stream().filter(e->e.startsWith(area)).collect(Collectors.toList());
          }
          if(CollectionUtils.isEmpty(officeCodes)) {
            page.setCount(0);
            page.setList(new ArrayList<>());
          }else {
            String codes=StringUtils.join(officeCodes, "','");
            String andsql=MessageFormat.format("{0} {1} {2} {3}",
                StringUtils.isBlank(bizPlace.getPlaceName())?"":"and p.place_name like '%"+bizPlace.getPlaceName()+"%'",
                StringUtils.isBlank(bizPlace.getTradeType())?"":"and p.trade_type = '"+bizPlace.getTradeType()+"'",
                StringUtils.isBlank(codes)?"":"and (p.city in('"+codes+"') or p.area in('"+codes+"'))",
                StringUtils.isBlank(bizPlace.getBusinessStatus())?"":"and p.business_status = '"+bizPlace.getBusinessStatus()+"'"
            );
            
            HashMap<String,Object> param=new HashMap<String,Object>();
            param.put("andsql",andsql);
  //        select * from table limit (pageNo-1)*pageSize, pageSize;
            param.put("maxnum", "limit "+((page.getPageNo()-1)*page.getPageSize())+","+page.getPageSize());
            
            List<Map<String, Object>> count=bizPlaceService.queryCount(param);
            List<Map<String, Object>> list=bizPlaceService.queryList(param);
            ArrayList<BizPlace> l=new ArrayList<BizPlace>();
            list.forEach(e->{
              BizPlace bp=new BizPlace();
              bp.setPlaceCode(String.valueOf(e.get("placeCode")));
              bp.setPlaceName(String.valueOf(e.get("placeName")));
              bp.setTradeType(String.valueOf(e.get("tradeType")));
              bp.setCity(AreaUtils.getArea(String.valueOf(e.get("city"))));
              bp.setArea(AreaUtils.getArea(String.valueOf(e.get("area"))));
              bp.setStreet(String.valueOf(e.get("street")));
              bp.setGeoCoordinates(String.valueOf(e.get("geoCoordinates")));
              bp.setRepresentative(String.valueOf(e.get("representative")));
              bp.setPhone(String.valueOf(e.get("phone")));
              bp.setBusinessStatus(String.valueOf(e.get("businessStatus")));
              l.add(bp);
            });
            page.setCount(Long.valueOf(count.get(0).get("count").toString()));
            page.setList(l);
          }
	    }
		Map<String,BizCyber> cyberMap=cyberService.getCybers();
	    page.getList().forEach(bp->{
	      BizCyber cyber=cyberMap.get(bp.getPlaceCode());
	      bp.setCyberInstall(cyber!=null&&cyber.isInstall());
	      bp.setCyberOnline(cyber!=null&&cyber.isOnline());
	    });
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("biz:bizPlace:view")
	@RequestMapping(value = "form")
	public String form(BizPlace bizPlace, Model model) {
		model.addAttribute("bizPlace", bizPlace);
		Config config=ConfigUtils.getConfig("sys.baidu.ak");
		model.addAttribute("baidu_ak",config==null?"":config.getConfigValue());
		return "modules/biz/bizPlaceForm";
	}

	/**
	 * 保存场所表
	 */
	@RequiresPermissions("biz:bizPlace:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated BizPlace bizPlace) {
		bizPlaceService.save(bizPlace);
		return renderResult(Global.TRUE, text("保存场所表成功！"));
	}
	
	/**
	 * 停用场所表
	 */
	@RequiresPermissions("biz:bizPlace:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(BizPlace bizPlace) {
		bizPlace.setStatus(BizPlace.STATUS_DISABLE);
		bizPlaceService.updateStatus(bizPlace);
		return renderResult(Global.TRUE, text("停用场所表成功"));
	}
	
	/**
	 * 启用场所表
	 */
	@RequiresPermissions("biz:bizPlace:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(BizPlace bizPlace) {
		bizPlace.setStatus(BizPlace.STATUS_NORMAL);
		bizPlaceService.updateStatus(bizPlace);
		return renderResult(Global.TRUE, text("启用场所表成功"));
	}
	
	/**
	 * 删除场所表
	 */
	@RequiresPermissions("biz:bizPlace:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(BizPlace bizPlace) {
		bizPlaceService.delete(bizPlace);
		return renderResult(Global.TRUE, text("删除场所表成功！"));
	}

	/**
	 * 下载导入场所数据模板
	 */
	@RequiresPermissions("biz:bizPlace:view")
	@RequestMapping(value = "importTemplate")
	public void importTemplate(HttpServletResponse response) {
		BizPlace bizPlace = new BizPlace();
		List<BizPlace> list = ListUtils.newArrayList(bizPlace);
		String fileName = "场所数据模板.xlsx";
		try(ExcelExport ee = new ExcelExport("智能视频监管系统各场所单位相关信息字段表", BizPlace.class, Type.IMPORT)){
			ee.setDataList(list).write(response, fileName);
		}
	}
	/**
	 * 导入场所数据
	 */
	@ResponseBody
	@RequiresPermissions("biz:bizPlace:edit")
	@PostMapping(value = "importData")
	public String importData(MultipartFile file, String updateSupport) {
		try {
			boolean isUpdateSupport = Global.YES.equals(updateSupport);
			String message = bizPlaceService.importData(file, isUpdateSupport);
			return renderResult(Global.TRUE, "posfull:"+message);
		} catch (Exception ex) {
			return renderResult(Global.FALSE, "posfull:"+ex.getMessage());
		}
	}
	/**
	 * 导出场所数据
	 */
	@RequiresPermissions("biz:bizPlace:view")
	@RequestMapping(value = "exportData")
	public void exportData(BizPlace bizPlace, Boolean isAll, String ctrlPermi, HttpServletResponse response) {
//		bizPlace.getCity().setIsQueryChildren(true);
//		bizPlace.getArea().setIsQueryChildren(true);
//		if (!(isAll != null && isAll)){
//			bizPlaceService.addDataScopeFilter(bizPlace, ctrlPermi);
//		}
		List<BizPlace> list = bizPlaceService.findList(bizPlace);
	    Map<String,BizCyber> cyberMap=cyberService.getCybers();
	    list.forEach(bp->{
	      BizCyber cyber=cyberMap.get(bp.getPlaceCode());
	      bp.setCyberInstall(cyber!=null&&cyber.isInstall());
	      bp.setCyberOnline(cyber!=null&&cyber.isOnline());
	    });
		String fileName = "场所数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("智能视频监管系统各场所单位相关信息字段表", BizPlace.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

}