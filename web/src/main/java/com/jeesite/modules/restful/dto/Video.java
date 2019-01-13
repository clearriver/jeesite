package com.jeesite.modules.restful.dto;

import java.util.Date;
public class Video{
	private String palceUuid;
	private Date createTime;
	private int typeStatus;
	private String delStatus;
	private String delTime;
	private String remark;
	private String lookImg;
	private String uuid;
	private String url;

	public String getPalceUuid() {
		return palceUuid;
	}

	public void setPalceUuid(String palceUuid) {
		this.palceUuid = palceUuid;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getTypeStatus() {
		return typeStatus;
	}

	public void setTypeStatus(int typeStatus) {
		this.typeStatus = typeStatus;
	}

	public String getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}

	public String getDelTime() {
		return delTime;
	}

	public void setDelTime(String delTime) {
		this.delTime = delTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLookImg() {
		return lookImg;
	}

	public void setLookImg(String lookImg) {
		this.lookImg = lookImg;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
