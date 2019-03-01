package com.jeesite.modules.restful;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jeesite.modules.restful.dto.Result;
import com.jeesite.modules.sys.entity.Area;
import com.jeesite.modules.sys.entity.EmpUser;
import com.jeesite.modules.sys.entity.Office;
import com.jeesite.modules.sys.service.AreaService;
import com.jeesite.modules.sys.service.EmpUserService;
import com.jeesite.modules.sys.service.OfficeService;

import io.swagger.annotations.ApiParam;

@RestController("AccountController-v1")
@RequestMapping(value = "/api")
public class AccountController {
	@Autowired
	private EmpUserService empUserService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private AreaService areaService;
	/**
	 * 1.	获取当前用户账号基本配置信息
	 * */
	@RequestMapping(value = {"/account"},method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
				if(eu!=null) {;
					HashMap<String,Object> eur=new HashMap<String,Object>();
					eur.put("userName", eu.getUserName());
					eur.put("userCode",eu.getLoginCode());
					eur.put("email", eu.getEmail());
					eur.put("mobile", eu.getMobile());
					eur.put("phone", eu.getPhone());
					eur.put("sex", eu.getSex());
					eur.put("avatar", eu.getAvatar());
					eur.put("office",eu.getEmployee()==null?null:
						(eu.getEmployee().getOffice()==null?null:eu.getEmployee().getOffice().getOfficeCode()));
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
	@RequestMapping(value = {"/office/{pcode}"},method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Result> treeOffice(@ApiParam(value = "机构编码", required = true) @PathVariable("pcode")String pcode) {
		Office where = new Office();
		where.setStatus(Office.STATUS_NORMAL);
		where.setParentCodes(pcode);
		List<Office> list = officeService.findList(where);
		ArrayList<HashMap<String,Object>> r=new ArrayList<HashMap<String,Object>>();
		if(!list.isEmpty()) {
			list.forEach(new Consumer<Office>(){
				@Override
				public void accept(Office t) {
					if(t.getParentCodes().endsWith(pcode+",")){
						HashMap<String,Object> e=new HashMap<String,Object>();
						e.put("value",t.getOfficeCode());
						e.put("label",t.getOfficeName());
						r.add(e);
					}
				}
			});
			tree(list,r);
		}
		Result res=new Result();
		res.setData(r);
		return new ResponseEntity<Result>(res, HttpStatus.OK);
	}
	private void tree(List<Office> list,ArrayList<HashMap<String,Object>> r) {
		if(!r.isEmpty()) {
			r.forEach(new Consumer<HashMap<String,Object>>(){
				@Override
				public void accept(HashMap<String,Object> t){
					String pcode=t.get("value").toString();
					ArrayList<HashMap<String,Object>> sr=new ArrayList<HashMap<String,Object>>();
					list.forEach(new Consumer<Office>(){
						@Override
						public void accept(Office t) {
							if(t.getParentCodes().endsWith(pcode+",")){
								HashMap<String,Object> e=new HashMap<String,Object>();
								e.put("value",t.getOfficeCode());
								e.put("label",t.getOfficeName());
								sr.add(e);
							}
						}
					});
					t.put("children", sr);
					if(!sr.isEmpty()) {
						tree(list,sr);
					}
				}
			});
		}
	}
	@RequestMapping(value = {"/area/{pcode}"},method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Result> treeArea(@ApiParam(value = "行政区划编码", required = true) @PathVariable("pcode")String pcode) {
		Area where = new Area();
		where.setStatus(Office.STATUS_NORMAL);
		where.setParentCodes(pcode);
		List<Area> list = areaService.findList(where);
		ArrayList<HashMap<String,Object>> r=new ArrayList<HashMap<String,Object>>();
		if(!list.isEmpty()) {
			list.forEach(new Consumer<Area>(){
				@Override
				public void accept(Area t) {
					if(t.getParentCodes().endsWith(pcode+",")){
						HashMap<String,Object> e=new HashMap<String,Object>();
						e.put("value",t.getAreaCode());
						e.put("label",t.getAreaName());
						r.add(e);
					}
				}
			});
			treeSort(list,r);
		}
		Result res=new Result();
		res.setData(r);
		return new ResponseEntity<Result>(res, HttpStatus.OK);
	}
	private void treeSort(List<Area> list,ArrayList<HashMap<String,Object>> r) {
		if(!r.isEmpty()) {
			r.forEach(new Consumer<HashMap<String,Object>>(){
				@Override
				public void accept(HashMap<String,Object> t){
					String pcode=t.get("value").toString();
					ArrayList<HashMap<String,Object>> sr=new ArrayList<HashMap<String,Object>>();
					list.forEach(new Consumer<Area>(){
						@Override
						public void accept(Area t) {
							if(t.getParentCodes().endsWith(pcode+",")){
								HashMap<String,Object> e=new HashMap<String,Object>();
								e.put("value",t.getAreaCode());
								e.put("label",t.getAreaName());
								sr.add(e);
							}
						}
					});
					t.put("children", sr);
					if(!sr.isEmpty()) {
						treeSort(list,sr);
					}
				}
			});
		}
	}
}
