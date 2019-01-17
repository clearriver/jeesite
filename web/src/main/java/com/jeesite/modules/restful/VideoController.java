/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.restful;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
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
import com.jeesite.modules.biz.entity.BizPlace;
import com.jeesite.modules.biz.service.BizPlaceService;
import com.jeesite.modules.restful.dto.Result;
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
	/**
	 *  sign	签名
	 *	noncestr	时间戳
	 *	palceNo	视频场所编号
	 *	url	异常视频的url
	 *	lookImg	报警抓图(（逗号），分隔拼接)
	 *	typeStatus	报警的类型状态1：未成年 2: 图像丢失 3：烟雾 4：火焰
	 * */
	@RequestMapping(value = {"/video"},method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Result> updateVideo(
			@ApiParam(value = "签名", required = true) @RequestParam(value = "sign")String sign,
			@ApiParam(value = "时间戳( yyyy-MM-dd HH:mm:ss)", required = true) @RequestParam(value = "noncestr")String noncestr,
			@ApiParam(value = "视频场所编号", required = true) @RequestParam(value = "place")String place,
			@ApiParam(value = "异常视频的url", required = true) @RequestParam(value = "url")String url,
			@ApiParam(value = "报警抓图(（逗号），分隔拼接)", required = true) @RequestParam(value = "lookImg")String lookImg,
			@ApiParam(value = "报警的类型状态1：未成年 2: 图像丢失 3：烟雾 4：火焰", required = true,defaultValue="1") @RequestParam(value = "typeStatus")String typeStatus) {
		Result r=new Result();
		Date dt=null;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
				bp.setSign(sign);
				bp.setAlarmTime(dt);
				bp.setRtspUrl(url);
				bp.setOosUrl(lookImg);
				bp.setAlarmType(typeStatus);
				try {
					bizPlaceService.save(bp);
				} catch (Exception e) {
					r.setSuccess(false);
					r.setErrCode(Result.ERR_CODE);
					r.setMsg("保存失败");
					e.printStackTrace();
				}
			}else {
				r.setSuccess(false);
				r.setErrCode(Result.ERR_CODE);
				r.setMsg("视频场所编号不存在");
			}
			r.setData(bp);
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