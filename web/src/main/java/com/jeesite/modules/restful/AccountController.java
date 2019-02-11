package com.jeesite.modules.restful;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeesite.modules.restful.dto.Result;
import com.jeesite.modules.sys.entity.EmpUser;
import com.jeesite.modules.sys.service.EmpUserService;

import io.swagger.annotations.ApiParam;

@RestController("AccountController-v1")
@RequestMapping(value = "/api/account")
public class AccountController {
	@Autowired
	private EmpUserService empUserService;
	/**
	 * 1.	获取当前用户账号基本配置信息
	 * */
	@RequestMapping(value = {"/"},method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Result> getUser(@ApiParam(value = "签名", required = true) @RequestParam(value = "sign")String sign,
			@ApiParam(value = "时间戳", required = true) @RequestParam(value = "noncestr")String noncestr,
			@ApiParam(value = "用户编号", required = true) @RequestParam("userCode")String userCode) {
		Result r=new Result();
		if(StringUtils.isNotBlank(userCode)) {
			EmpUser eu = null;
			try {
				EmpUser user=new EmpUser();
				user.setLoginCode(userCode);
				List<EmpUser> list = empUserService.findList(user);
				eu=(list==null||list.size()==0)?null:list.get(0);
				if(eu!=null) {
					HashMap<String,Object> eur=new HashMap<String,Object>();
					eur.put("userName", eu.getUserName());
					eur.put("userCode",eu.getLoginCode());
					eur.put("email", eu.getEmail());
					eur.put("mobile", eu.getMobile());
					eur.put("phone", eu.getPhone());
					eur.put("sex", eu.getSex());
					eur.put("avatar", eu.getAvatar());
					r.setData(eur);
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
}
