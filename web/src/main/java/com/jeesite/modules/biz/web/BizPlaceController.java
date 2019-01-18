/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
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
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.utils.excel.annotation.ExcelField.Type;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.biz.entity.BizPlace;
import com.jeesite.modules.biz.service.BizPlaceService;
import com.jeesite.modules.sys.entity.Config;
import com.jeesite.modules.sys.entity.EmpUser;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.ConfigService;
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
	private BizPlaceService bizPlaceService;
	@Autowired
	private ConfigService configService;
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public BizPlace get(String placeCode, boolean isNewRecord) {
		return bizPlaceService.get(placeCode, isNewRecord);
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
		bizPlace.setPage(new Page<>(request, response));
		Page<BizPlace> page = bizPlaceService.findPage(bizPlace);
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
		try(ExcelExport ee = new ExcelExport("智能视频监管系统各场所单位相关信息字段表", EmpUser.class, Type.IMPORT)){
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
		String fileName = "场所数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("智能视频监管系统各场所单位相关信息字段表", BizPlace.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}

}