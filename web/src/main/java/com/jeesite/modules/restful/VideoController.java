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

import com.jeesite.modules.Constants;
import com.jeesite.modules.biz.entity.BizAlarm;
import com.jeesite.modules.biz.entity.BizPlace;
import com.jeesite.modules.biz.service.BizAlarmService;
import com.jeesite.modules.biz.service.BizPlaceService;
import com.jeesite.modules.restful.dto.Result;
import com.jeesite.modules.sys.entity.Area;
import com.jeesite.modules.sys.service.AreaService;
import com.jeesite.modules.util.MD5Util;

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
	private BizPlaceService bizPlaceService;
	@Autowired
	private BizAlarmService bizAlarmService;
	@Autowired
	private AreaService areaService;
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
			@ApiParam(value = "报警的类型状态1：未成年 2: 图像丢失 3：烟雾 4：火焰", required = true,defaultValue="1") @RequestParam(value = "typeStatus")String typeStatus) {
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
			BizPlace bp=bizPlaceService.get(place);
			if(bp!=null) {
				BizAlarm bizAlarm=new BizAlarm();
				bizAlarm.setAlarmCode(place+"_"+bp.getBizAlarmList().size());
				bizAlarm.setPlaceCode(place);
				bizAlarm.setSign(sign);
				bizAlarm.setAlarmTime(dt);
				bizAlarm.setVideoUrl(videoUrl);
				bizAlarm.setOosUrl(lookImg);
				bizAlarm.setAlarmType(typeStatus);
				try {
//					bizPlaceService.save(bp);
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
		return new ResponseEntity<Result>(r, HttpStatus.OK);
	}
	
	@RequestMapping(value = {"/place"},method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Result> savePlace(@RequestBody BizPlace bizPlace) {
		Result r=new Result();
		try {
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
			@ApiParam(value = "视频场所编号", required = true) @RequestParam(value = "rtsp")String rtspUrl) {
		Result r=new Result();
		try {
			if(StringUtils.isNotBlank(place)) {
				BizPlace bizPlace=bizPlaceService.get(place);
				if(bizPlace!=null) {
					bizPlace.setRtspUrl(rtspUrl);
					bizPlaceService.save(bizPlace);
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
					bizPlace.setRtspUrl(null);
					bizPlaceService.save(bizPlace);
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
		if(StringUtils.isNotBlank(place)) {
			BizPlace bp = null;
			try {
				bp = bizPlaceService.get(place);
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
			@ApiParam(value = "时间戳", required = true) @RequestParam(value = "noncestr")String noncestr,
			@ApiParam(value = "场所类型", required = true) @RequestParam("tradeType")String tradeType,
			@ApiParam(value = "地区编码") @RequestParam(value ="areaCode", required = false)String areaCode,
			@ApiParam(value = "场所名称") @RequestParam(value ="placeName", required = false)String placeName) {
		Result r=new Result();
		BizPlace bp=new BizPlace();
		bp.setTradeType(tradeType);
		bp.setPlaceName(placeName);
		Area area=areaService.get(areaCode);
		if(area==null) {
			r.setSuccess(false);
			r.setErrCode(Result.ERR_CODE);
			r.setMsg("地区编码不存在");
		}else {
			bp.setArea(area);
		}
		if(r.isSuccess()) {
			try {
				List<BizPlace> eu = bizPlaceService.findList(bp);
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
	 * 2.	获取历史报警记录
	 * */
	@RequestMapping(value = {"/alarms"},method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Result> getAlarms(@ApiParam(value = "签名", required = true) @RequestParam(value = "sign")String sign,
			@ApiParam(value = "时间戳", required = true) @RequestParam(value = "noncestr")String noncestr,
			@ApiParam(value = "场所类型", required = true) @RequestParam("tradeType")String tradeType,
			@ApiParam(value = "报警类型", required = true) @RequestParam("alarmType")String alarmType,
			@ApiParam(value = "地区编码") @RequestParam(value ="areaCode", required = false)String areaCode,
			@ApiParam(value = "报警日期(yyyy-MM-dd)") @RequestParam(value ="alarmTime" , required = false)String alarmTime,
			@ApiParam(value = "场所名称") @RequestParam(value ="placeName", required = false)String placeName) {
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
				String andsql=MessageFormat.format("and p.trade_type=''{0}'' and a.alarm_type=''{1}'' {2} {3} {4}",
						tradeType,alarmType,
						StringUtils.isBlank(areaCode)?"":"and p.area_code='"+areaCode+"'",
						StringUtils.isBlank(placeName)?"":"and p.place_name='"+placeName+"'",
						StringUtils.isBlank(alarmTime)?"":"and date_format(a.alarm_time, '%Y-%m-%d')='"+alarmTime+"'");
				//TODO : 添加按alarmTime 查询条件 ;签名;时间戳" 
				HashMap<String,Object> bp=new HashMap<String,Object>();
				bp.put("andsql",andsql);
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
			@ApiParam(value = "时间戳", required = true) @RequestParam(value = "noncestr")String noncestr,
			@ApiParam(value = "场所类型", required = true) @RequestParam("tradeType")String tradeType,
			@ApiParam(value = "地区编码", required = false) @RequestParam(value="areaCode", required = false)String areaCode,
			@ApiParam(value = "处置方式") @RequestParam(value="dealWay", required = false)String dealWay) {
		Result r=new Result();
		if(r.isSuccess()) {
			try {
				String andsql=MessageFormat.format("and p.trade_type=''{0}'' {1} {2}", tradeType,
						StringUtils.isBlank(areaCode)?"":"and p.area_code='"+areaCode+"'",
						StringUtils.isBlank(dealWay)?"":"and p.deal_way='"+dealWay+"'");
				//TODO :  签名; 时间戳
				HashMap<String,Object> bp=new HashMap<String,Object>();
				bp.put("andsql",andsql);
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
			@ApiParam(value = "时间戳", required = true) @RequestParam(value = "noncestr")String noncestr,
			@ApiParam(value = "用户编号", required = true) @RequestParam("userCode")String userCode,
			@ApiParam(value = "处置方式") @RequestParam(value ="result", required = false)String dealWay) {
		Result r=new Result();
		if(r.isSuccess()) {
			try {
				String andsql=MessageFormat.format(" {0}", 
				StringUtils.isBlank(dealWay)?"":"and p.deal_way='"+dealWay+"'");
				//TODO :  签名; 时间戳；用户编号
				HashMap<String,Object> bp=new HashMap<String,Object>();
				bp.put("andsql",andsql);
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

}