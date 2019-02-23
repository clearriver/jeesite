package com.jeesite.modules.biz.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
/**
 * 实时视频流RTSP地址Entity
 * @author ThinkGem
 * @version 2017-03-25
 */
@Table(name="${_prefix}biz_rtsp_url", alias="url", columns={
		@Column(name="rtsp_url", attrName="rtspUrl", label="实时视频流RTSP地址", isPK=true),
		@Column(name="place_code",  attrName="placeCode",  label="许可证号或编号"),
		@Column(name="online", attrName="online", label="是否上线")
	}, orderBy=""
)
public class BizRtspUrl extends DataEntity<BizRtspUrl>{
	private static final long serialVersionUID = -1872843704429593049L;
	private String placeCode;		// 许可证号或编号
	private String rtspUrl;		// 实时视频流RTSP地址
	private String online;
	public BizRtspUrl() {
		this(null, null);
	}

	public BizRtspUrl(String placeCode, String rtspUrl){
		this.placeCode = placeCode;
		this.rtspUrl = rtspUrl;
		this.isNewRecord=true;
	}

	public BizRtspUrl(String placeCode, String rtspUrl, String online){
		this.placeCode = placeCode;
		this.rtspUrl = rtspUrl;
		this.online = online;
		this.isNewRecord=true;
	}
	
	public String getPlaceCode() {
		return placeCode;
	}

	public void setPlaceCode(String placeCode) {
		this.placeCode = placeCode;
	}

	public String getRtspUrl() {
		return rtspUrl;
	}

	public void setRtspUrl(String rtspUrl) {
		this.rtspUrl = rtspUrl;
	}

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}
}
