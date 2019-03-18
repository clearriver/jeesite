/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.restful;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.modules.Constants;
import com.jeesite.modules.biz.entity.BizAlarm;
import com.jeesite.modules.biz.entity.BizMediaServer;
import com.jeesite.modules.biz.entity.BizPlace;
import com.jeesite.modules.biz.entity.BizRtspUrl;
import com.jeesite.modules.biz.service.BizAlarmService;
import com.jeesite.modules.biz.service.BizMediaServerService;
import com.jeesite.modules.biz.service.BizPlaceService;
import com.jeesite.modules.biz.service.BizRtspUrlService;
import com.jeesite.modules.restful.dto.Result;
import com.jeesite.modules.sys.entity.DictData;
import com.jeesite.modules.sys.service.AreaService;
import com.jeesite.modules.sys.utils.ConfigUtils;
import com.jeesite.modules.sys.utils.DictUtils;
import com.jeesite.modules.util.MD5Util;
import com.jeesite.modules.util.StringTool;

import io.swagger.annotations.ApiParam;

/**
 * VideoController
 * @author 长江
 * @version 2019-01-12
 */
@RestController("VideoController-v1")
@RequestMapping(value = "/api/restful")
public class VideoController{
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private BizPlaceService bizPlaceService;
	@Autowired
	private BizAlarmService bizAlarmService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private BizRtspUrlService bizRtspUrlService;
	@Autowired
	private BizMediaServerService bizMediaServerService;
	@Autowired
	private Gson gson;
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sdfd=new SimpleDateFormat("yyyy-MM-dd");
	/**
	 *  sign	签名
	 *	noncestr	时间戳
	 *	palceNo	视频场所编号
	 *	url	异常视频的url
	 *	lookImg	报警抓图(（逗号），分隔拼接)
	 *	typeStatus	报警的类型状态1：未成年 2: 图像丢失 3：烟雾 4：火焰
	 * */
	@RequestMapping(value = {"/video"},method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Result> saveVideo(
			@ApiParam(value = "签名", required = true) @RequestParam(value = "sign")String sign,
			@ApiParam(value = "时间戳( yyyy-MM-dd HH:mm:ss)", required = true) @RequestParam(value = "noncestr")String noncestr,
			@ApiParam(value = "视频场所编号", required = true) @RequestParam(value = "place")String place,
			@ApiParam(value = "异常视频的url", required = true) @RequestParam(value = "url")String videoUrl,
			@ApiParam(value = "报警抓图(（逗号），分隔拼接)", required = true) @RequestParam(value = "lookImg")String lookImg,
			@ApiParam(value = "报警的类型状态1：未成年 2: 图像丢失 3：烟雾 4：火焰", required = true,defaultValue="1") @RequestParam(value = "typeStatus")String typeStatus,
			@ApiParam(value = "报警编码") @RequestParam(value = "code", required = false)String alarmCode) {
		Result r=new Result();
		Date dt=null;
		try {
			dt=sdf.parse(noncestr);
		} catch (ParseException e) {
			r.setSuccess(false);
			r.setErrCode(Result.ERR_CODE);
			r.setMsg("日期格式不对");
		}
		if(r.isSuccess()) {
			BizAlarm bizAlarm=null;
			if(StringUtils.isNotBlank(alarmCode)) {
				bizAlarm=bizAlarmService.get(alarmCode);
			}
			if(bizAlarm==null){
				bizAlarm=new BizAlarm();
			}else {
				bizAlarm.setIsNewRecord(false);
			}
			synchronized(place){
				BizPlace bp=bizPlaceService.getBizPlace(place);
				if(bp!=null) {
					if(bizAlarm.getIsNewRecord()) {
						String[] alarmCodes=bp.getBizAlarms();
						long max=0;
						for(String c:alarmCodes) {
							String temp=c.split("_")[1];
							max=max<Long.parseLong(temp)?Long.parseLong(temp):max;
						}
						bizAlarm.setAlarmCode(place+"_"+(max+1));
						
						bizAlarm.setCreateDate(new Date());
						bizAlarm.setAlarmTime(dt);
						bizAlarm.setDealWay("1");
					}
					bizAlarm.setSign(sign);
					bizAlarm.setPlaceCode(place);
					bizAlarm.setVideoUrl(StringTool.addEle(bizAlarm.getVideoUrl(), videoUrl));
					bizAlarm.setOosUrl(StringTool.addEle(bizAlarm.getOosUrl(),lookImg));
					bizAlarm.setAlarmType(typeStatus);
					bizAlarm.setUpdateDate(new Date());
					try {
						bizAlarmService.save(bizAlarm);
						r.setData(bizAlarm);
					} catch (Exception e) {
						r.setSuccess(false);
						r.setErrCode(Result.ERR_CODE);
						r.setMsg("保存失败");
						r.setData(null);
						e.printStackTrace();
					}
				}else {
					r.setData(null);
					r.setSuccess(false);
					r.setErrCode(Result.ERR_CODE);
					r.setMsg("视频场所编号不存在");
				}
			}
		}
		return new ResponseEntity<Result>(r, HttpStatus.OK);
	}
	
	@RequestMapping(value = {"/place"},method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Result> savePlace(@RequestBody BizPlace bizPlace) {
		Result r=new Result();
		try {
			bizPlace.setCreateDate(new Date());
			bizPlace.setUpdateDate(new Date());
			bizPlace.setCreateBy("API");
			bizPlaceService.save(bizPlace);
		} catch (Exception e) {
			r.setSuccess(false);
			r.setData(bizPlace);
			r.setMsg(Result.FAIL);
			r.setErrCode(Result.ERR_CODE);
			
			e.printStackTrace();
		}
		r.setData(bizPlace);
		return new ResponseEntity<Result>(r, HttpStatus.OK);
	}
	@RequestMapping(value = {"/rtsp"},method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Result> saveRtsp(
			@ApiParam(value = "视频场所编号", required = true) @RequestParam(value = "place")String place,
			@ApiParam(value = "视频场RTSP", required = true) @RequestParam(value = "rtsp")String rtspUrl,
			@ApiParam(value = "是否上线(add,del)") @RequestParam(value = "type", required = false)String type) {
		Result r=new Result();
		try {
			if(StringUtils.isNotBlank(place)) {
				BizPlace bizPlace=bizPlaceService.get(place);
				if(bizPlace!=null) {
					if(StringUtils.isNotBlank(rtspUrl)) {
						String oldRtspUrls=StringUtils.join(bizPlace.getBizRtspUrls(),",");
						type=StringUtils.isBlank(type)?"add":type;

						BizRtspUrl bizRtspUrl=new BizRtspUrl(bizPlace.getPlaceCode(),rtspUrl,type);
						if(oldRtspUrls.contains(rtspUrl)) {
							bizRtspUrl.setIsNewRecord(false);
						}
						bizRtspUrl.setUpdateDate(new Date());
						bizRtspUrl.setUpdateBy("API");
						bizRtspUrlService.save(bizRtspUrl);
						
						if(!"add".equals(type)) {
							BizAlarm bizAlarm=new BizAlarm();

							String[] alarmCodes=bizPlace.getBizAlarms();
							long max=0;
							for(String c:alarmCodes) {
								String temp=c.split("_")[1];
								max=max<Long.parseLong(temp)?Long.parseLong(temp):max;
							}
							bizAlarm.setAlarmCode(place+"_"+(max+1));
							
							bizAlarm.setPlaceCode(place);
							bizAlarm.setAlarmTime(new Date());
							bizAlarm.setVideoUrl(rtspUrl);
//							bizAlarm.setSign(sign);
//							bizAlarm.setOosUrl(lookImg);
//							bizAlarm.setAlarmType("1");
							bizAlarm.setDealWay("1");
							bizAlarm.setDealResult("掉线");
							bizAlarm.setCreateDate(new Date());
							bizAlarm.setUpdateDate(new Date());
							bizAlarm.setCreateBy("API");
							bizAlarm.setUpdateBy("API");
							bizAlarmService.save(bizAlarm);
						}
					}
				}else{
					r.setSuccess(false);
					r.setMsg("视频场所编号不存在.");
					r.setErrCode(Result.ERR_CODE);
				}
			}else{
				r.setSuccess(false);
				r.setMsg(Result.FAIL);
				r.setErrCode(Result.ERR_CODE);
			}
		} catch (Exception e) {
			r.setSuccess(false);
			r.setMsg(Result.FAIL);
			r.setErrCode(Result.ERR_CODE);
			e.printStackTrace();
		}
		return new ResponseEntity<Result>(r, HttpStatus.OK);
	}
	@RequestMapping(value = {"/rtsp"},method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Result> deleteRtsp(
			@ApiParam(value = "视频场所编号", required = true) @RequestParam(value = "place")String place) {
		Result r=new Result();
		try {
			if(StringUtils.isNotBlank(place)) {
				BizPlace bizPlace=bizPlaceService.get(place);
				if(bizPlace!=null) {
					List<BizRtspUrl> bizRtspUrlList=bizPlace.getBizRtspUrlList();
					bizRtspUrlList.forEach(new Consumer<BizRtspUrl>() {
						@Override
						public void accept(BizRtspUrl t) {
							bizRtspUrlService.delete(t);						
						}
					});
				}else{
					r.setSuccess(false);
					r.setMsg("视频场所编号不存在.");
					r.setErrCode(Result.ERR_CODE);
				}
			}else{
				r.setSuccess(false);
				r.setMsg(Result.FAIL);
				r.setErrCode(Result.ERR_CODE);
			}
		} catch (Exception e) {
			r.setSuccess(false);
			r.setMsg(Result.FAIL);
			r.setErrCode(Result.ERR_CODE);
			e.printStackTrace();
		}
		return new ResponseEntity<Result>(r, HttpStatus.OK);
	}
	@RequestMapping(value = {"/{place}"},method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Result> get(@ApiParam(value = "许可证号或编号", required = true) @PathVariable("place")String place) {
		Result r=new Result();
		if(StringUtils.isNotBlank(place)){
			BizPlace bp = null;
			try {
				bp = bizPlaceService.get(place);
				if(StringUtils.isBlank(bp.getGeoCoordinates())) {
					String add=bp.getCity().getAreaName()+bp.getArea().getAreaName()+bp.getStreet();
					bp.setGeoCoordinates(getGPS(add));
					bp.setIsNewRecord(false);
					bizPlaceService.save(bp);
				}
				List<BizRtspUrl> list=bp.getBizRtspUrlList();
				for(int i=list.size()-1;i>=0;i--) {
					BizRtspUrl ba=list.get(i);
					if(!"add".equalsIgnoreCase(ba.getOnline())) {
						list.remove(ba);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			r.setData(bp);
		}
		if(r.getData()==null) {
			r.setSuccess(false);
			r.setErrCode(Result.ERR_CODE);
			r.setMsg(Result.FAIL);
		}
		return new ResponseEntity<Result>(r, HttpStatus.OK);
	}
	/**
	 * 3.	获取视频分布点
	 * */
	@RequestMapping(value = {"/places"},method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Result> getPlaces(@ApiParam(value = "签名", required = true) @RequestParam(value = "sign")String sign,
			@ApiParam(value = "时间戳( yyyy-MM-dd HH:mm:ss)", required = true) @RequestParam(value = "noncestr")String noncestr,
			@ApiParam(value = "场所类型", required = true) @RequestParam("tradeType")String tradeType,
			@ApiParam(value = "地区编码") @RequestParam(value ="areaCode", required = false)String areaCode,
			@ApiParam(value = "场所名称或编号") @RequestParam(value ="placeName", required = false)String placeName,
			@ApiParam(value = "最大条数") @RequestParam(value ="maxNum", required = false)String maxNum) {
		Result r=new Result();
		try {
			String andsql=MessageFormat.format("{0} {1} {2} ",
					StringUtils.isBlank(tradeType)||"0".equals(tradeType)?"":"and p.trade_type='"+tradeType+"'",
					StringUtils.isBlank(areaCode)?"":"and p.area like '"+removeZero(areaCode)+"%'",
					StringUtils.isBlank(placeName)?"":"and (p.place_code like '%"+placeName+"%' or p.place_name like '%"+placeName+"%' )");
			//TODO : 查询条件 ;签名;时间戳" 
			HashMap<String,Object> param=new HashMap<String,Object>();
			param.put("andsql",andsql);
			if(StringUtils.isNotBlank(maxNum)) {
				try {
					Integer max=Integer.parseInt(maxNum);
					if(max>0) {
						param.put("maxnum", "limit "+max);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			List<Map<String, Object>> eu = bizPlaceService.queryMap(param);
			r.setData(eu);
			ArrayList<String> pclist=new ArrayList<String>();
			eu.forEach(new Consumer<Map<String, Object>>() {
				@Override
				public void accept(Map<String, Object> bp) {
					pclist.add("'"+bp.get("placeCode").toString()+"'");
					if(!bp.containsKey("geoCoordinates")||bp.get("geoCoordinates")==null||StringUtils.isBlank(bp.get("geoCoordinates").toString())) {
						String add=areaService.get(bp.get("city").toString()).getAreaName() +areaService.get(bp.get("area").toString()).getAreaName()+bp.get("street");
						BizPlace bizPlace = bizPlaceService.get(bp.get("placeCode").toString());
						bizPlace.setGeoCoordinates(getGPS(add));
						bizPlace.setIsNewRecord(false);
						bizPlaceService.save(bizPlace);
					}
				}
			});
			if(!eu.isEmpty()) {
				andsql=MessageFormat.format("and r.place_code in ({0}) ",StringUtils.join(pclist, ","));
				param=new HashMap<String,Object>();
				param.put("andsql",andsql);
				List<BizRtspUrl> bizRtspUrlList=bizPlaceService.queryRtspUrl(param);
				if(bizRtspUrlList!=null&&!bizRtspUrlList.isEmpty()) {
					eu.forEach(new Consumer<Map<String, Object>>() {
						@Override
						public void accept(Map<String, Object> bp) {
							String placeCode=bp.get("placeCode").toString();
							ArrayList<BizRtspUrl> brlist=new ArrayList<BizRtspUrl>();
							bizRtspUrlList.forEach(new Consumer<BizRtspUrl>() {
								@Override
								public void accept(BizRtspUrl t) {
									if(placeCode.equals(t.getPlaceCode())) {
										brlist.add(t);
									}
								}
							});
							List<String> lists = ListUtils.extractToList(brlist, "rtspUrl");
							bp.put("bizRtspUrlList", brlist);
							bp.put("bizRtspUrls", lists);
						}
					});
				}
			}
			
		} catch (Exception e) {
			r.setSuccess(false);
			r.setErrCode(Result.ERR_CODE);
			r.setMsg("查询失败");
			e.printStackTrace();
		}
		return new ResponseEntity<Result>(r, HttpStatus.OK);
	}
	private static String removeZero(String code) {
		if(code.endsWith("0")) {
			code=removeZero(code.substring(0, code.length()-1));
		}
		return code;
	}
	/**
	 * 2.	获取历史报警记录
	 * */
	@RequestMapping(value = {"/alarms"},method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Result> getAlarms(@ApiParam(value = "签名", required = true) @RequestParam(value = "sign")String sign,
			@ApiParam(value = "时间戳( yyyy-MM-dd HH:mm:ss)", required = true) @RequestParam(value = "noncestr")String noncestr,
			@ApiParam(value = "场所类型", required = true) @RequestParam("tradeType")String tradeType,
			@ApiParam(value = "报警类型", required = true) @RequestParam("alarmType")String alarmType,
			@ApiParam(value = "地区编码") @RequestParam(value ="areaCode", required = false)String areaCode,
			@ApiParam(value = "报警日期(yyyy-MM-dd)") @RequestParam(value ="alarmTime" , required = false)String alarmTime,
			@ApiParam(value = "场所名称") @RequestParam(value ="placeName", required = false)String placeName,
			@ApiParam(value = "最大条数") @RequestParam(value="maxNum", required = false)String maxNum) {
		Result r=new Result();
		Date dt=null;
		if(StringUtils.isNotBlank(alarmTime)) {
			try {
				dt=sdfd.parse(alarmTime);
			} catch (Exception e) {
				r.setSuccess(false);
				r.setErrCode(Result.ERR_CODE);
				r.setMsg("日期格式不对");
			}
		}
		if(r.isSuccess()) {
			try {
				String andsql=MessageFormat.format("{0} {1} {2} {3} {4}",
						StringUtils.isBlank(tradeType)||"0".equals(tradeType)?"":"and p.trade_type='"+tradeType+"'",
						StringUtils.isBlank(alarmType)||"0".equals(alarmType)?"":"and a.alarm_type='"+alarmType+"'",
						StringUtils.isBlank(areaCode)?"":"and p.area like '"+removeZero(areaCode)+"%'",
						StringUtils.isBlank(placeName)?"":"and p.place_name like '%"+placeName+"%'",
						StringUtils.isBlank(alarmTime)?"":"and date_format(a.alarm_time, '%Y-%m-%d')='"+alarmTime+"'");
				//TODO : 添加按alarmTime 查询条件 ;签名;时间戳" 
				HashMap<String,Object> bp=new HashMap<String,Object>();
				bp.put("andsql",andsql);
				if(StringUtils.isNotBlank(maxNum)) {
					bp.put("maxnum"," limit "+maxNum);
				}
				List<Map<String,Object>> eu = bizAlarmService.queryMap(bp);
				r.setData(eu);
			} catch (Exception e) {
				r.setSuccess(false);
				r.setErrCode(Result.ERR_CODE);
				r.setMsg("查询失败");
				e.printStackTrace();
			}
		}
		return new ResponseEntity<Result>(r, HttpStatus.OK);
	}
	/**
	 * 4、未处理报警记录
	 * */
	@RequestMapping(value = {"/alarmsno"},method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Result> getAlarmsNodeal(@ApiParam(value = "签名", required = true) @RequestParam(value = "sign")String sign,
			@ApiParam(value = "时间戳( yyyy-MM-dd HH:mm:ss)", required = true) @RequestParam(value = "noncestr")String noncestr,
			@ApiParam(value = "场所类型", required = true) @RequestParam("tradeType")String tradeType,
			@ApiParam(value = "地区编码") @RequestParam(value="areaCode", required = false)String areaCode,
			@ApiParam(value = "起始时间(yyyy-MM-dd HH:mm:ss)") @RequestParam(value="beginTime", required = false)String beginTime,
			@ApiParam(value = "处置方式") @RequestParam(value="dealWay", required = false)String dealWay,
			@ApiParam(value = "最大条数") @RequestParam(value="maxNum", required = false)String maxNum) {
		Result r=new Result();
		Date dt=null;
		if(StringUtils.isNotBlank(beginTime)) {
			try {
				dt=sdf.parse(beginTime);
			} catch (Exception e) {
				r.setSuccess(false);
				r.setErrCode(Result.ERR_CODE);
				r.setMsg("日期时间格式不对");
			}
		}
		if(r.isSuccess()) {
			try {
				String andsql=MessageFormat.format("{0} {1} {2} {3} ", 
						StringUtils.isBlank(tradeType)||"0".equals(tradeType)?"":"and p.trade_type='"+tradeType+"'",
						StringUtils.isBlank(areaCode)?"":"and p.area like '"+removeZero(areaCode)+"%'",
						StringUtils.isBlank(dealWay)||"0".equals(dealWay)?"":"and a.deal_way='"+dealWay+"'",
						StringUtils.isBlank(beginTime)?"":"and a.alarm_time>=STR_TO_DATE('"+beginTime+"','%Y-%m-%d %H:%i:%s')");
				//TODO :  签名; 时间戳
				HashMap<String,Object> bp=new HashMap<String,Object>();
				bp.put("andsql",andsql);
				if(StringUtils.isNotBlank(maxNum)) {
					bp.put("maxnum"," limit "+maxNum);
				}
				List<Map<String,Object>> eu = bizAlarmService.queryMap(bp);
				r.setData(eu);
			} catch (Exception e) {
				r.setSuccess(false);
				r.setErrCode(Result.ERR_CODE);
				r.setMsg("查询失败");
				e.printStackTrace();
			}
		}
		return new ResponseEntity<Result>(r, HttpStatus.OK);
	}
	/**
	 * 5、	异常处理报送
	 * */
	@RequestMapping(value = {"/alarmse"},method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Result> getAlarmsException(@ApiParam(value = "签名", required = true) @RequestParam(value = "sign")String sign,
			@ApiParam(value = "时间戳( yyyy-MM-dd HH:mm:ss)", required = true) @RequestParam(value = "noncestr")String noncestr,
			@ApiParam(value = "用户编号", required = true) @RequestParam("userCode")String userCode,
			@ApiParam(value = "报警记录编号") @RequestParam(value ="alarmCode", required = false)String alarmCode,
			@ApiParam(value = "处置方式") @RequestParam(value ="dealWay", required = false)String dealWay,
			@ApiParam(value = "最大条数") @RequestParam(value="maxNum", required = false)String maxNum) {
		Result r=new Result();
		if(r.isSuccess()) {
			try {
				String andsql=MessageFormat.format(" {0} {1} ", 
				StringUtils.isBlank(dealWay)||"0".equals(dealWay)?"":"and a.deal_way='"+dealWay+"'",
				StringUtils.isBlank(dealWay)?"":"and a.alarm_code='"+alarmCode+"'");
				//TODO :  签名; 时间戳；用户编号
				HashMap<String,Object> bp=new HashMap<String,Object>();
				bp.put("andsql",andsql);
				if(StringUtils.isNotBlank(maxNum)) {
					bp.put("maxnum"," limit "+maxNum);
				}
				List<Map<String,Object>> eu = bizAlarmService.queryMap(bp);
				r.setData(eu);
			} catch (Exception e) {
				r.setSuccess(false);
				r.setErrCode(Result.ERR_CODE);
				r.setMsg("查询失败");
				e.printStackTrace();
			}
		}
		return new ResponseEntity<Result>(r, HttpStatus.OK);
	}
	@RequestMapping(value = {"/server/{place}"},method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Result> getServerByPlace(@ApiParam(value = "许可证号或编号", required = true) @PathVariable("place")String place,
			@ApiParam(value = "服务器类型(1.多媒体服务器,2.分析服务器)") @RequestParam(value ="type", required = false)String type) {
		Result r=new Result();
		if(StringUtils.isNotBlank(place)){
			BizPlace bp = null;
			try {
				bp = bizPlaceService.get(place);
				if(bp!=null) {
					String office=bp.getArea().getAreaCode();
					String andsql=MessageFormat.format("{0} {1} ",
							StringUtils.isBlank(office)?"":"and a.office = '"+office+"'",
							StringUtils.isBlank(type)?"":"and a.server_type='"+type+"'");
					HashMap<String,Object> param=new HashMap<String,Object>();
					param.put("andsql",andsql);
					List<BizMediaServer> ms = bizMediaServerService.queryBizMediaServer(param);
					List<Map<String,Object>> list=JsonMapper.fromJson(JsonMapper.getInstance().toJson(ms),List.class);
					list.forEach(new Consumer<Map<String,Object>>() {
						@Override
						public void accept(Map<String,Object> t) {
							Map<String,Object> om=(Map<String,Object>)t.get("office");
							t.remove("isNewRecord");
							ArrayList<String> fieldNames=new ArrayList<String>();
							om.keySet().forEach(new Consumer<String>() {
								@Override
								public void accept(String key) {
									if(!key.equals("officeCode")&&!key.equals("officeName")) {
										fieldNames.add(key);
									}
								}
							});
							fieldNames.forEach(new Consumer<String>() {
								@Override
								public void accept(String key) {
									om.remove(key);
								}
							});
						}
					});
					r.setData(list);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(r.getData()==null) {
			r.setSuccess(false);
			r.setErrCode(Result.ERR_CODE);
			r.setMsg(Result.FAIL);
		}
		return new ResponseEntity<Result>(r, HttpStatus.OK);
	}
	/**6
	 * type: 1.误报, 2.下发检查查
	 * */
	@RequestMapping(value = {"/alarm/{alarmCode}"},method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Result> processAlarm(@PathVariable("alarmCode")String alarmCode,
			@ApiParam(value = "签名", required = true) @RequestParam(value = "sign")String sign,
			@ApiParam(value = "时间戳( yyyy-MM-dd HH:mm:ss)", required = true) @RequestParam(value = "noncestr")String noncestr,
			@ApiParam(value = "处理结果(4:现场检查,3:分配检查,2:确认误报,1:未处置)") @RequestParam(value ="type", required = true)String type) {
		Result r=new Result();
		HashMap<String,Boolean> m=new HashMap<String,Boolean>();
		m.put("exists",false);
		List<DictData> dicts=DictUtils.getDictList("sys_biz_deal_way");
		dicts.forEach(new Consumer<DictData>() {
			@Override
			public void accept(DictData t) {
				if(t.getDictCode().equals(type)) {
					m.put("exists",true);
				}
			}
		});
		if(m.get("exists")) {
			BizAlarm ba=bizAlarmService.get(alarmCode);
			if(ba!=null) {
				ba.setDealWay(type);
				ba.setUpdateDate(new Date());
				ba.setIsNewRecord(false);
				bizAlarmService.save(ba);
				r.setData(ba);
			}			
		}
		if(r.getData()==null) {
			r.setSuccess(false);
			r.setErrCode(Result.ERR_CODE);
			r.setMsg(Result.FAIL);
		}
		return new ResponseEntity<Result>(r, HttpStatus.OK);
	}

	/**
	 * 7.	按地区和报警类型,行业类型,处理结果  统计 报警数量,
	 * */
	@RequestMapping(value = {"/alarms/stat"},method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Result> getAlarmsStat(@ApiParam(value = "签名", required = true) @RequestParam(value = "sign")String sign,
			@ApiParam(value = "时间戳( yyyy-MM-dd HH:mm:ss)", required = true) @RequestParam(value = "noncestr")String noncestr,
			@ApiParam(value = "行业类型") @RequestParam(value ="tradeType", required = false)String tradeType,
			@ApiParam(value = "报警类型") @RequestParam(value ="alarmType", required = false)String alarmType,
			@ApiParam(value = "地区编码") @RequestParam(value ="areaCode", required = false)String areaCode,
			@ApiParam(value = "报警日期(yyyy-MM-dd)") @RequestParam(value ="alarmTime" , required = false)String alarmTime,
			@ApiParam(value = "场所名称") @RequestParam(value ="placeName", required = false)String placeName,
			@ApiParam(value = "处理结果(4:现场检查,3:分配检查,2:确认误报,1:未处置)") @RequestParam(value ="type", required = false)String dealWay) {
		Result r=new Result();
		Date dt=null;
		if(StringUtils.isNotBlank(alarmTime)) {
			try {
				dt=sdfd.parse(alarmTime);
			} catch (Exception e) {
				r.setSuccess(false);
				r.setErrCode(Result.ERR_CODE);
				r.setMsg("日期格式不对");
			}
		}
		if(r.isSuccess()) {
			try {
				String andsql=MessageFormat.format("{0} {1} {2} {3} {4} {5}",
						StringUtils.isBlank(tradeType)||"0".equals(tradeType)?"":"and p.trade_type='"+tradeType+"'",
						StringUtils.isBlank(alarmType)||"0".equals(alarmType)?"":"and a.alarm_type='"+alarmType+"'",
						StringUtils.isBlank(areaCode)?"":"and p.area like '"+removeZero(areaCode)+"%'",
						StringUtils.isBlank(placeName)?"":"and p.place_name like '%"+placeName+"%'",
						StringUtils.isBlank(alarmTime)?"":"and date_format(a.alarm_time, '%Y-%m-%d')='"+alarmTime+"'",
						StringUtils.isBlank(dealWay)||"0".equals(dealWay)?"":"and a.deal_way='"+dealWay+"'");
				//TODO : 添加按alarmTime 查询条件 ;签名;时间戳" 
				HashMap<String,Object> param=new HashMap<String,Object>();
				param.put("andsql",andsql);
				Long eu= bizAlarmService.queryCount(param);
				r.setData(new HashMap<String,Long>(){{put("count",eu);}});
			} catch (Exception e) {
				r.setSuccess(false);
				r.setErrCode(Result.ERR_CODE);
				r.setMsg("查询失败");
				e.printStackTrace();
			}
		}
		return new ResponseEntity<Result>(r, HttpStatus.OK);
	}

	/**
	 * 8、根据场所类型分组,查询报警记录
	 * */
	@RequestMapping(value = {"/alarms/groupstat"},method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Result> getAlarmsByPlaceGroup(@ApiParam(value = "签名", required = true) @RequestParam(value = "sign")String sign,
			@ApiParam(value = "时间戳( yyyy-MM-dd HH:mm:ss)", required = true) @RequestParam(value = "noncestr")String noncestr,
			@ApiParam(value = "地区编码") @RequestParam(value="areaCode", required = false)String areaCode,
			@ApiParam(value = "报警日期(yyyy-MM-dd HH:mm:ss)(如:2019-01-01 00:00:00)") @RequestParam(value="alarmTime", required = false)String alarmTime,
			@ApiParam(value = "处置方式") @RequestParam(value="type", required = false)String dealWay,
			@ApiParam(value = "每组最大条数") @RequestParam(value="maxNum", required = false)String maxNum) {
		Result r=new Result();
		Date dt=null;
		if(StringUtils.isNotBlank(alarmTime)) {
			try {
				dt=sdf.parse(alarmTime);
			} catch (Exception e) {
				r.setSuccess(false);
				r.setErrCode(Result.ERR_CODE);
				r.setMsg("日期时间格式不对");
			}
		}
		if(r.isSuccess()) {
			try {
				//TODO :  签名; 时间戳
				String andsqla=MessageFormat.format("{0} {1} {2} ", 
						StringUtils.isBlank(areaCode)?"":"and p.area like '"+removeZero(areaCode)+"%'",
						StringUtils.isBlank(dealWay)||"0".equals(dealWay)?"":"and a.deal_way='"+dealWay+"'",
						StringUtils.isBlank(alarmTime)?"":"and a.alarm_time>=STR_TO_DATE('"+alarmTime+"','%Y-%m-%d %H:%i:%s')");
				
				ArrayList<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
				List<DictData> dicts=DictUtils.getDictList("sys_biz_trade_type");
				/*dicts.forEach(new Consumer<DictData>() {
					@Override
					public void accept(DictData t) {
						HashMap<String,Object> dd=new HashMap<String,Object>();
						dd.put("type",t.getDictValue());
						dd.put("label",t.getDictLabel());
						HashMap<String,Object> param=new HashMap<String,Object>();
						param.put("andsqla",andsql+"and p.trade_type='"+t.getDictValue()+"'");
						Long count= bizAlarmService.queryCount(param);
						dd.put("num",count);
						if(StringUtils.isNotBlank(maxNum)&&!"0".equals(maxNum)) {
							param.put("maxnum"," limit " + maxNum);
						}
						dd.put("data",bizAlarmService.queryMap(param));
						list.add(dd);
					}
				});*/
				String andsqlaa=MessageFormat.format("{0} {1} {2} ", 
						StringUtils.isBlank(areaCode)?"":"and pp.area like '"+removeZero(areaCode)+"%'",
						StringUtils.isBlank(dealWay)||"0".equals(dealWay)?"":"and aa.deal_way='"+dealWay+"'",
						StringUtils.isBlank(alarmTime)?"":"and aa.alarm_time>=STR_TO_DATE('"+alarmTime+"','%Y-%m-%d %H:%i:%s')");
				HashMap<String,Object> param=new HashMap<String,Object>();
				if(StringUtils.isNotBlank(maxNum)&&!"0".equals(maxNum)) {
					param.put("maxnum",maxNum);
				}
				param.put("andsqla",andsqla);
				param.put("andsqlaa",andsqlaa);
				List<Map<String, Object>> dataGroup=bizAlarmService.queryByTradeTypeGroup(param);
				List<Map<String, Object>> countGroup=bizAlarmService.countByTradeTypeGroup(param);
				dicts.forEach(new Consumer<DictData>() {
					@Override
					public void accept(DictData t) {
						HashMap<String,Object> dd=new HashMap<String,Object>();
						dd.put("type",t.getDictValue());
						dd.put("label",t.getDictLabel());
						countGroup.forEach(new Consumer<Map<String, Object>>(){
							@Override
							public void accept(Map<String, Object> c) {
								if(t.getDictValue().equals(c.get("tradeType"))) {
									dd.put("num",c.get("num"));
								}
							}
						});
						if(!dd.containsKey("num")) {
							dd.put("num",0);
						}
						ArrayList<Map<String, Object>> datalist=new ArrayList<Map<String, Object>>();
						dataGroup.forEach(new Consumer<Map<String, Object>>(){
							@Override
							public void accept(Map<String, Object> d) {
								if(t.getDictValue().equals(d.get("tradeType"))) {
									datalist.add(d);
								}
							}
						});
						dd.put("data", datalist);
						list.add(dd);
					}
				});
				r.setData(list);
			} catch (Exception e) {
				r.setSuccess(false);
				r.setErrCode(Result.ERR_CODE);
				r.setMsg("查询失败");
				e.printStackTrace();
			}
		}
		return new ResponseEntity<Result>(r, HttpStatus.OK);
	}
	/**
     * 签名方法
	*
     * @param paramMap
     * @return
     */
    public String checkAuthentication(Map<String, String[]> paramMap) {
        Collection<String> keyset = paramMap.keySet();
        List<String> list = new ArrayList<String>(keyset);
        Collections.sort(list);
        String paramEncodeStr = "";
        for (int i = 0; i < list.size(); i++) {
            String paramKey = list.get(i);
            if (!Constants.SIGN_KEY.equals(paramKey)) {
                String paramVal = paramMap.get(list.get(i))[0];
                paramEncodeStr += list.get(i) + "=" + paramVal + "&";
            }
        }
        paramEncodeStr = paramEncodeStr + "key=" + Constants.AUTH_KEY;
        return MD5Util.MD5Encode(paramEncodeStr);
    }
    public String getGPS(String s) {
    	String result=null;
    	try {
			String baidu_ak=ConfigUtils.getConfig("sys.baidu.ak").getConfigValue();
			String url="http://api.map.baidu.com/geocoder/v2/?output=json&ak="+baidu_ak+"&address=宁夏回族自治区"+s;
			Map r=gson.fromJson(restTemplate.getForObject(url, String.class),Map.class);
			//result.location.lng,r.result.location.lat
			result=((Map)((Map)r.get("result")).get("location")).get("lng")+","+((Map)((Map)r.get("result")).get("location")).get("lat");
		}catch (Exception e) {
			e.printStackTrace();
		}
    	return result;
    }
    
}