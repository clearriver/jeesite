/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.biz.entity.BizMediaServer;
import com.jeesite.modules.biz.service.BizMediaServerService;

/**
 * 流媒体服务器管理Controller
 * @author 长江
 * @version 2019-03-03
 */
@Controller
@RequestMapping(value = "${adminPath}/biz/bizMediaServer")
public class BizMediaServerController extends BaseController {

	@Autowired
	private BizMediaServerService bizMediaServerService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public BizMediaServer get(String office, String domainName, String serverIp, String serverPort, String serverType, boolean isNewRecord) {
		return bizMediaServerService.get(new Class<?>[]{String.class, String.class, String.class, String.class, String.class},
				new Object[]{office, domainName, serverIp, serverPort,serverType}, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("biz:bizMediaServer:view")
	@RequestMapping(value = {"list", ""})
	public String list(BizMediaServer bizMediaServer, Model model) {
		model.addAttribute("bizMediaServer", bizMediaServer);
		return "modules/biz/bizMediaServerList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("biz:bizMediaServer:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<BizMediaServer> listData(BizMediaServer bizMediaServer, HttpServletRequest request, HttpServletResponse response) {
		bizMediaServer.setPage(new Page<>(request, response));
		Page<BizMediaServer> page = bizMediaServerService.findPage(bizMediaServer);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("biz:bizMediaServer:view")
	@RequestMapping(value = "form")
	public String form(BizMediaServer bizMediaServer, Model model) {
		BizMediaServer r=bizMediaServerService.get(bizMediaServer);
		if(r!=null) {
			r.setIsNewRecord(false);
			bizMediaServer=r;
		}
		model.addAttribute("bizMediaServer", bizMediaServer);
		return "modules/biz/bizMediaServerForm";
	}

	/**
	 * 保存流媒体服务器管理
	 */
	@RequiresPermissions("biz:bizMediaServer:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated BizMediaServer bizMediaServer){
		BizMediaServer r=null;
		if(StringUtils.isBlank(bizMediaServer.getId())) {
			List<BizMediaServer> list=bizMediaServerService.findList(bizMediaServer);
			r=list==null||list.size()==0?null:list.get(0);
		}else {
			String id=bizMediaServer.getId();
			r=bizMediaServerService.get(id);
			bizMediaServer.setId(null);
			List<BizMediaServer> list=bizMediaServerService.findList(bizMediaServer);
			BizMediaServer e=list==null||list.size()==0?null:list.get(0);
			if(e!=null&&!id.equals(e.getId())) {
				return renderResult(Global.FALSE, text("保存服务器地址失败！该地区已经存在该类型的服务器"));
			}
		}
		if(r!=null) {
			r.setIsNewRecord(false);
			r.setOffice(bizMediaServer.getOffice());
			r.setDomainName(bizMediaServer.getDomainName());
			r.setServerIp(bizMediaServer.getServerIp());
			r.setServerPort(bizMediaServer.getServerPort());
			r.setServerType(bizMediaServer.getServerType());
			bizMediaServer=r;
		}
		bizMediaServerService.save(bizMediaServer);
		return renderResult(Global.TRUE, text("保存服务器地址成功！"));
	}
	
	/**
	 * 停用流媒体服务器管理
	 */
	@RequiresPermissions("biz:bizMediaServer:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(BizMediaServer bizMediaServer) {
		bizMediaServer.setStatus(BizMediaServer.STATUS_DISABLE);
		bizMediaServerService.updateStatus(bizMediaServer);
		return renderResult(Global.TRUE, text("停用流媒体服务器管理成功"));
	}
	
	/**
	 * 启用流媒体服务器管理
	 */
	@RequiresPermissions("biz:bizMediaServer:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(BizMediaServer bizMediaServer) {
		bizMediaServer.setStatus(BizMediaServer.STATUS_NORMAL);
		bizMediaServerService.updateStatus(bizMediaServer);
		return renderResult(Global.TRUE, text("启用流媒体服务器管理成功"));
	}
	
	/**
	 * 删除流媒体服务器管理
	 */
	@RequiresPermissions("biz:bizMediaServer:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(BizMediaServer bizMediaServer) {
		bizMediaServerService.delete(bizMediaServer);
		return renderResult(Global.TRUE, text("删除流媒体服务器管理成功！"));
	}
	
}