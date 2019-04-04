/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.biz.entity.BizAlarm;
import com.jeesite.modules.biz.service.BizAlarmService;

/**
 * 报警信息管理Controller
 * @author 长江
 * @version 2019-03-03
 */
@Controller
@RequestMapping(value = "${adminPath}/biz/bizAlarm")
public class BizAlarmController extends BaseController {

	@Autowired
	private BizAlarmService bizAlarmService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public BizAlarm get(String office, String domainName, String serverIp, String serverPort, String serverType, boolean isNewRecord) {
		return bizAlarmService.get(new Class<?>[]{String.class, String.class, String.class, String.class, String.class},
				new Object[]{office, domainName, serverIp, serverPort,serverType}, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("biz:bizAlarm:view")
	@RequestMapping(value = {"list", ""})
	public String list(BizAlarm bizAlarm, Model model) {
		model.addAttribute("bizAlarm", bizAlarm);
		return "modules/biz/bizAlarmList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("biz:bizAlarm:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<BizAlarm> listData(BizAlarm bizAlarm, HttpServletRequest request, HttpServletResponse response) {
		bizAlarm.setPage(new Page<>(request, response));
		Page<BizAlarm> page = bizAlarmService.findPage(bizAlarm);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("biz:bizAlarm:view")
	@RequestMapping(value = "form")
	public String form(BizAlarm bizAlarm, Model model) {
		BizAlarm r=bizAlarmService.get(bizAlarm);
		if(r!=null) {
			r.setIsNewRecord(false);
			bizAlarm=r;
		}
		model.addAttribute("bizAlarm", bizAlarm);
		return "modules/biz/bizAlarmForm";
	}

	/**
	 * 保存报警信息管理
	 */
	@RequiresPermissions("biz:bizAlarm:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated BizAlarm bizAlarm){
//		BizAlarm r=null;
//		if(StringUtils.isBlank(bizAlarm.getId())) {
//			List<BizAlarm> list=bizAlarmService.findList(bizAlarm);
//			r=list==null||list.size()==0?null:list.get(0);
//		}else {
//			String id=bizAlarm.getId();
//			r=bizAlarmService.get(id);
//			bizAlarm.setId(null);
//			List<BizAlarm> list=bizAlarmService.findList(bizAlarm);
//			BizAlarm e=list==null||list.size()==0?null:list.get(0);
//			if(e!=null&&!id.equals(e.getId())) {
//			r.setServerType(bizAlarm.getServerType());
//			bizAlarm=r;
//		}
//		bizAlarmService.save(bizAlarm);
		return renderResult(Global.TRUE, text("保存报警信息成功！"));
	}
	
	/**
	 * 停用报警信息管理
	 */
	@RequiresPermissions("biz:bizAlarm:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(BizAlarm bizAlarm) {
		bizAlarm.setStatus(BizAlarm.STATUS_DISABLE);
		bizAlarmService.updateStatus(bizAlarm);
		return renderResult(Global.TRUE, text("停用报警信息管理成功"));
	}
	
	/**
	 * 启用报警信息管理
	 */
	@RequiresPermissions("biz:bizAlarm:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(BizAlarm bizAlarm) {
		bizAlarm.setStatus(BizAlarm.STATUS_NORMAL);
		bizAlarmService.updateStatus(bizAlarm);
		return renderResult(Global.TRUE, text("启用报警信息管理成功"));
	}
	
	/**
	 * 删除报警信息管理
	 */
	@RequiresPermissions("biz:bizAlarm:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(BizAlarm bizAlarm) {
		bizAlarmService.delete(bizAlarm);
		return renderResult(Global.TRUE, text("删除报警信息成功！"));
	}
	/**
	 * 导出场所数据
	 */
	@RequiresPermissions("biz:bizAlarm:view")
	@RequestMapping(value = "exportData")
	public void exportData(BizAlarm bizAlarm, Boolean isAll, String ctrlPermi, HttpServletResponse response) {
		List<BizAlarm> list = bizAlarmService.findList(bizAlarm);
		String fileName = "报警数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("智能视频监管系统报警数据", BizAlarm.class)){
			ee.setDataList(list).write(response, fileName);
		}
	}
}