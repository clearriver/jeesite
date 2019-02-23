/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.biz.entity;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelField.Align;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import com.jeesite.common.utils.excel.fieldtype.AreaType;
import com.jeesite.modules.sys.entity.Area;

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
		@Column(name="rtsp_url", attrName="rtspUrl", label="实时视频流RTSP地址")
	}, joinTable={
			@JoinTable(type=Type.LEFT_JOIN, entity=Area.class, alias="o", 
					on="o.area_code = a.city ",attrName="city",
					columns={@Column(includeEntity=Area.class)}),
			@JoinTable(type=Type.LEFT_JOIN, entity=Area.class, alias="s", 
			on=" s.area_code = a.area ",attrName="area",
			columns={@Column(includeEntity=Area.class)}),
	}, orderBy="a.place_code DESC"
)

@Valid
@ExcelFields({
	@ExcelField(title="行业类型", attrName="tradeType", align=Align.CENTER, sort=10,dictType="sys_biz_trade_type"),
	@ExcelField(title="许可证号或编号", attrName="placeCode", align=Align.CENTER, sort=20),
	@ExcelField(title="主体名称", attrName="placeName", align = Align.CENTER, sort=30),
	@ExcelField(title="所属市", attrName="city.areaName", align=Align.CENTER, sort=40, fieldType=AreaType.class),
	@ExcelField(title="所属县（区）", attrName="area.areaName", align=Align.CENTER, sort=50, fieldType=AreaType.class),
	@ExcelField(title="详细地址", attrName="street", align=Align.CENTER, sort=60),
//	@ExcelField(title="地理坐标", attrName="geoCoordinates", align=Align.CENTER, sort=70,type=ExcelField.Type.EXPORT),
	@ExcelField(title="法定代表人（主要负责人）", attrName="representative", align=Align.CENTER, sort=80),
	@ExcelField(title="移动电话", attrName="phone", align=Align.CENTER, sort=90),
	@ExcelField(title="营业状态", attrName="businessStatus", align=Align.CENTER, sort=100,dictType="sys_biz_status", type=ExcelField.Type.ALL),
})
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

	private List<BizAlarm> bizAlarmList = ListUtils.newArrayList(); // 关联报警信息
	public List<BizAlarm> getBizAlarmList() {
		return bizAlarmList;
	}

	public void setBizAlarmList(List<BizAlarm> bizAlarmList) {
		this.bizAlarmList = bizAlarmList;
	}

	public String[] getBizAlarms() {
		List<String> list = ListUtils.extractToList(bizAlarmList, "alarmCode");
		return list.toArray(new String[list.size()]);
	}

	public void setBizAlarms(String[] bizAlarms) {
		for (String val : bizAlarms){
			if (StringUtils.isNotBlank(val)){
				BizAlarm e = new BizAlarm();
				e.setAlarmCode(val);
				this.bizAlarmList.add(e);
			}
		}
	}
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
}