/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.entity;


import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.modules.sys.entity.Office;

/**
 * 流媒体服务器管理Entity
 * @author 长江
 * @version 2019-03-03
 */
@Table(name="${_prefix}biz_media_server", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="office", attrName="office.officeCode", label="地区",isUpdate=true),
		@Column(name="domain_name", attrName="domainName", label="域名", isUpdate=true),
		@Column(name="server_ip", attrName="serverIp", label="IP", isUpdate=true),
		@Column(name="server_port", attrName="serverPort", label="端口", isUpdate=true),
		@Column(name="server_type", attrName="serverType", label="服务器类型", isUpdate=true)
	}, joinTable={
			@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, alias="o", 
					on="o.office_code = a.office ",attrName="office",
					columns={@Column(includeEntity=Office.class)}),
	}, orderBy="a.office DESC, a.domain_name DESC, a.server_ip DESC, a.server_port DESC"
)
public class BizMediaServer extends DataEntity<BizMediaServer> {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private Office office;		// 地区
	private String domainName;		// 域名
	private String serverIp;		// IP
	private String serverPort;		// 端口
	private String serverType; // 1.Media server , 2.RTSP server
	public BizMediaServer() {
		this(null,null,null,null,null);
	}
	public BizMediaServer(String id) {
		this.id=id;
		this.isNewRecord=true;
	}
	public BizMediaServer(String office, String domainName, String serverIp, String serverPort, String serverType){
		this.office = new Office();
		this.office.setOfficeCode(office);
		this.domainName = domainName;
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.serverType=serverType;
		this.isNewRecord=true;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	
	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	
	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	
}