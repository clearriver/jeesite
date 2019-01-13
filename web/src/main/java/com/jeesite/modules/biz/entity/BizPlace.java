/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.entity;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.sys.entity.Area;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.entity.Office;

/**
 * 场所表Entity
 * @author 长江
 * @version 2019-01-12
 */
@Table(name="${_prefix}biz_place", alias="a", columns={
		@Column(name="place_code", attrName="placeCode", label="许可证号或编号", isPK=true),
		@Column(name="place_name", attrName="placeName", label="主体名称", queryType=QueryType.LIKE),
		@Column(name="trade_type", attrName="tradeType", label="行业类型"),
		@Column(name="city", attrName="city.areaCode", label="所属市"),
		@Column(name="area", attrName="area.areaCode", label="所属县", comment="所属县（区）"),
		@Column(name="street", attrName="street", label="详细地址"),
		@Column(name="geo_coordinates", attrName="geoCoordinates", label="地理坐标"),
		@Column(name="representative", attrName="representative", label="法定代表人", comment="法定代表人（主要负责人）"),
		@Column(name="phone", attrName="phone", label="移动电话"),
		@Column(name="business_status", attrName="businessStatus", label="营业状态"),
		@Column(name="rtsp_url", attrName="rtspUrl", label="实时视频流RTSP地址"),
		@Column(name="alarm_type", attrName="alarmType", label="报警类型"),
		@Column(name="alarm_time", attrName="alarmTime", label="报警时间"),
		@Column(name="deal_way", attrName="dealWay", label="处置方式"),
		@Column(name="oos_url", attrName="oosUrl", label="报警视频及图像OSS存储URL地址"),
		@Column(name="sign", attrName="sign", label="签名")
	}, joinTable={
			@JoinTable(type=Type.LEFT_JOIN, entity=Area.class, alias="o", 
					on="o.area_code = a.city ",attrName="city",
					columns={@Column(includeEntity=Area.class)}),
			@JoinTable(type=Type.LEFT_JOIN, entity=Area.class, alias="s", 
			on=" s.area_code = a.area ",attrName="area",
			columns={@Column(includeEntity=Area.class)}),
	}, orderBy="a.place_code DESC"
)
public class BizPlace extends DataEntity<BizPlace> {
	
	private static final long serialVersionUID = 1L;
	private String placeCode;		// 许可证号或编号
	private String placeName;		// 主体名称
	private String tradeType;		// 行业类型
	private Area city;		// 所属市
	private Area area;		// 所属县（区）
	private String street;		// 详细地址
	private String geoCoordinates;		// 地理坐标
	private String representative;		// 法定代表人（主要负责人）
	private String phone;		// 移动电话
	private String businessStatus;		// 营业状态
	private String rtspUrl;		// 实时视频流RTSP地址
	private String alarmType;		// 报警类型
	private Date alarmTime;		// 报警时间
	private String dealWay;		// 处置方式
	private String oosUrl;		// 报警视频及图像OSS存储URL地址
	private String sign;		// 签名
	
	public BizPlace() {
		this(null);
	}

	public BizPlace(String id){
		super(id);
	}
	
	public String getPlaceCode() {
		return placeCode;
	}

	public void setPlaceCode(String placeCode) {
		this.placeCode = placeCode;
	}
	
	@NotBlank(message="主体名称不能为空")
	@Length(min=0, max=200, message="主体名称长度不能超过 200 个字符")
	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	
	@Length(min=0, max=100, message="行业类型长度不能超过 100 个字符")
	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	
	public Area getCity() {
		return city;
	}

	public void setCity(Area city) {
		this.city = city;
	}
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
	@Length(min=0, max=300, message="详细地址长度不能超过 300 个字符")
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
	
	@Length(min=0, max=100, message="地理坐标长度不能超过 100 个字符")
	public String getGeoCoordinates() {
		return geoCoordinates;
	}

	public void setGeoCoordinates(String geoCoordinates) {
		this.geoCoordinates = geoCoordinates;
	}
	
	@Length(min=0, max=200, message="法定代表人长度不能超过 200 个字符")
	public String getRepresentative() {
		return representative;
	}

	public void setRepresentative(String representative) {
		this.representative = representative;
	}
	
	@Length(min=0, max=100, message="移动电话长度不能超过 100 个字符")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Length(min=0, max=1, message="营业状态长度不能超过 1 个字符")
	public String getBusinessStatus() {
		return businessStatus;
	}

	public void setBusinessStatus(String businessStatus) {
		this.businessStatus = businessStatus;
	}
	
	@Length(min=0, max=100, message="实时视频流RTSP地址长度不能超过 100 个字符")
	public String getRtspUrl() {
		return rtspUrl;
	}

	public void setRtspUrl(String rtspUrl) {
		this.rtspUrl = rtspUrl;
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
}