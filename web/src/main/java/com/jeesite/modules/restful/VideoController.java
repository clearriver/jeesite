/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.restful;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeesite.modules.Constants;
import com.jeesite.modules.restful.dto.Result;
import com.jeesite.modules.restful.dto.Video;
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
	/**
	 *  sign	签名
	 *	noncestr	时间戳
	 *	palceNo	视频场所编号
	 *	url	异常视频的url
	 *	lookImg	报警抓图(（逗号），分隔拼接)
	 *	typeStatus	报警的类型状态1：未成年 2: 图像丢失 3：烟雾 4：火焰
	 * */
	@RequestMapping(value = {"insetIntelVideo"},method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Result> list(
			@ApiParam(value = "签名", required = true) @RequestParam(value = "sign")String sign,
			@ApiParam(value = "时间戳", required = true) @RequestParam(value = "noncestr")String noncestr,
			@ApiParam(value = "视频场所编号", required = true) @RequestParam(value = "palceNo")String palceNo,
			@ApiParam(value = "异常视频的url", required = true) @RequestParam(value = "url")String url,
			@ApiParam(value = "报警抓图(（逗号），分隔拼接)", required = true) @RequestParam(value = "lookImg")String lookImg,
			@ApiParam(value = "报警的类型状态1：未成年 2: 图像丢失 3：烟雾 4：火焰", required = true) @RequestParam(value = "typeStatus")Integer typeStatus) {
		Video video=new Video();
		video.setRemark("modules/biz/bizPlaceList");
		Result r=new Result();
		r.setData(video);
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