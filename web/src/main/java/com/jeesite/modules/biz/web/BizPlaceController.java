/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.web;

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
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.biz.entity.BizPlace;
import com.jeesite.modules.biz.service.BizPlaceService;
import com.jeesite.modules.sys.entity.Config;
import com.jeesite.modules.sys.service.ConfigService;
import com.jeesite.modules.sys.utils.ConfigUtils;

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
	
}