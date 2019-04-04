/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.entity;

import java.util.Date;

import javax.validation.Valid;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import com.jeesite.common.utils.excel.annotation.ExcelField.Align;
import com.jeesite.common.utils.excel.fieldtype.AreaType;

/**
 * 员工岗位Entity
 * @author ThinkGem
 * @version 2017-03-25
 */
@Table(name="${_prefix}biz_alarm", alias="a", columns={
		@Column(name="alarm_code", attrName="alarmCode", label="编码", isPK=true),
		@Column(name="place_code",  attrName="placeCode",  label="许可证号或编号"),
		@Column(name="alarm_type", attrName="alarmType", label="报警类型"),
		@Column(name="alarm_time", attrName="alarmTime", label="报警时间"),
		@Column(name="deal_way", attrName="dealWay", label="处置方式"),
		@Column(name="oos_url", attrName="oosUrl", label="报警视频及图像OSS存储URL地址"),
		@Column(name="video_url", attrName="videoUrl", label="报警视频及图像OSS存储URL地址"),
		@Column(name="sign", attrName="sign", label="签名"),
		@Column(name="deal_result", attrName="dealResult", label="处理结果"),
//		@Column(name="create_time", attrName="createTime", label="创建时间"),
//		@Column(name="update_time", attrName="updateTime", label="修改时间")
	}, orderBy="a.alarm_time DESC"
)
@Valid
@ExcelFields({
	@ExcelField(title="编码", attrName="alarmCode", align=Align.CENTER, sort=10),
	@ExcelField(title="许可证号或编号", attrName="placeCode", align=Align.CENTER, sort=20),
	@ExcelField(title="报警类型", attrName="alarmType", align=Align.CENTER, sort=10,dictType="sys_biz_alarm_type"),
	@ExcelField(title="报警时间", attrName="alarmTime", align = Align.CENTER, sort=30),
	@ExcelField(title="处置方式", attrName="dealWay", align=Align.CENTER, sort=40, dictType="sys_biz_deal_way"),
	@ExcelField(title="报警视频及图像OSS存储URL地址", attrName="oosUrl", align=Align.CENTER, sort=50),
	@ExcelField(title="报警视频及图像OSS存储URL地址", attrName="videoUrl", align=Align.CENTER, sort=60),
	@ExcelField(title="处理结果", attrName="dealResult", align=Align.CENTER, sort=80),
})
public class BizAlarm extends DataEntity<BizAlarm> {
	
	private static final long serialVersionUID = 1L;
	private String placeCode;		// 许可证号或编号
	private String alarmCode;	// 编码
	private String alarmType;		// 报警类型
	private Date alarmTime;		// 报警时间
	private String dealWay;		// 处置方式
	private String oosUrl;		// 报警视频及图像OSS存储URL地址
	private String sign;		// 签名
	private String videoUrl;//异常视频的url
	private String dealResult;//处理结果
	public BizAlarm() {
		this(null, null);
	}
	public BizAlarm(String alarmCode) {
		this(null, alarmCode);
	}
	public BizAlarm(String placeCode, String alarmCode){
		this.placeCode = placeCode;
		this.alarmCode = alarmCode;
		this.isNewRecord=true;
	}

	public String getPlaceCode() {
		return placeCode;
	}

	public void setPlaceCode(String placeCode) {
		this.placeCode = placeCode;
	}

	public String getAlarmCode() {
		return alarmCode;
	}

	public void setAlarmCode(String alarmCode) {
		this.alarmCode = alarmCode;
	}

	@Length(min=0, max=100, message="报警类型长度不能超过 100 个字符")
	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(Date alarmTime) {
		this.alarmTime = alarmTime;
	}
	
	@Length(min=0, max=100, message="处置方式长度不能超过 100 个字符")
	public String getDealWay() {
		return dealWay;
	}

	public void setDealWay(String dealWay) {
		this.dealWay = dealWay;
	}
	
	@Length(min=0, max=300, message="报警视频及图像OSS存储URL地址长度不能超过 300 个字符")
	public String getOosUrl() {
		return oosUrl;
	}

	public void setOosUrl(String oosUrl) {
		this.oosUrl = oosUrl;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getDealResult() {
		return dealResult;
	}
	public void setDealResult(String dealResult) {
		this.dealResult = dealResult;
	}
}