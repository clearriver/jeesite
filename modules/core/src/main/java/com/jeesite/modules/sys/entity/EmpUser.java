/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.sys.entity;

import javax.validation.Valid;

import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelField.Align;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import com.jeesite.common.utils.excel.fieldtype.CompanyType;
import com.jeesite.common.utils.excel.fieldtype.OfficeType;
import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.entity.Extend;
/**
 * 员工用户管理Entity
 * @author ThinkGem
 * @version 2017-03-25
 */
@Table(name="${_prefix}sys_user", alias="a", columns={
//		@Column(includeEntity=User.class),
		@Column(includeEntity=com.jeesite.common.entity.BaseEntity.class),
		@Column(includeEntity=DataEntity.class),
		@Column(name="user_code", attrName="userCode", label="用户编码", isPK=true),
		@Column(name="login_code", attrName="loginCode", label="登录账号", queryType=com.jeesite.common.mybatis.mapper.query.QueryType.LIKE), 
		@Column(name="user_name", attrName="userName", label="用户昵称", queryType=com.jeesite.common.mybatis.mapper.query.QueryType.LIKE), 
		@Column(name="password", attrName="password", label="登录密码"), 
		@Column(name="email", attrName="email", label="电子邮箱", queryType=com.jeesite.common.mybatis.mapper.query.QueryType.LIKE),
		@Column(name="mobile", attrName="mobile", label="手机号码", queryType=com.jeesite.common.mybatis.mapper.query.QueryType.LIKE), 
		@Column(name="phone", attrName="phone", label="办公电话", queryType=com.jeesite.common.mybatis.mapper.query.QueryType.LIKE), 
		@Column(name="sex", attrName="sex", label="用户性别"), 
		@Column(name="avatar", attrName="avatar", label="头像路径"), 
		@Column(name="sign", attrName="sign", label="个性签名"), 
		@Column(name="wx_openid", attrName="wxOpenid", label="绑定的微信号"),
		@Column(name="mobile_imei", attrName="mobileImei", label="绑定的手机串号"), 
		@Column(name="user_type", attrName="userType", label="用户类型", isUpdate=false, comment="用户类型"), 
		@Column(name="ref_code", attrName="refCode", label="用户类型引用编号", isUpdate=false),
		@Column(name="ref_name", attrName="refName", label="用户类型引用姓名", queryType=com.jeesite.common.mybatis.mapper.query.QueryType.LIKE), 
		@Column(name="mgr_type", attrName="mgrType", label="管理员类型", isUpdate=true, comment="管理员类型（0非管理员 1系统管理员  2二级管理员）"), 
		@Column(name="pwd_security_level", attrName="pwdSecurityLevel", label="密码安全级别", isUpdate=false, comment="密码安全级别（0初始 1很弱 2弱 3安全 4很安全）"),
		@Column(name="pwd_update_date", attrName="pwdUpdateDate", label="密码最后更新时间", isUpdate=false), 
		@Column(name="pwd_update_record", attrName="pwdUpdateRecord", label="密码修改记录", isUpdate=false), 
		@Column(name="pwd_question", attrName="pwdQuestion", label="密保问题", isUpdate=false), 
		@Column(name="pwd_question_answer", attrName="pwdQuestionAnswer", label="密保问题答案", isUpdate=false), 
		@Column(name="pwd_question_2", attrName="pwdQuestion2", label="密保问题2", isUpdate=false),
		@Column(name="pwd_question_answer_2", attrName="pwdQuestionAnswer2", label="密保问题答案2", isUpdate=false), 
		@Column(name="pwd_question_3", attrName="pwdQuestion3", label="密保问题3", isUpdate=false), 
		@Column(name="pwd_question_answer_3", attrName="pwdQuestionAnswer3", label="密保问题答案3", isUpdate=false), 
		@Column(name="pwd_quest_update_date", attrName="pwdQuestUpdateDate", label="密码问题修改时间", isUpdate=false), 
		@Column(name="last_login_ip", attrName="lastLoginIp", label="最后登陆IP", isUpdate=false), 
		@Column(name="last_login_date", attrName="lastLoginDate", label="最后登陆时间", isUpdate=false),
		@Column(name="freeze_date", attrName="freezeDate", label="冻结时间", isUpdate=false), 
		@Column(name="freeze_cause", attrName="freezeCause", label="冻结原因", isUpdate=false), 
		@Column(name="user_weight", attrName="userWeight", label="用户权重", comment="用户权重（降序）"),
		@Column(includeEntity=Extend.class, attrName="extend")
	}, joinTable={
		@JoinTable(type=Type.JOIN, entity=Employee.class, alias="e",
			on="e.emp_code=a.ref_code AND a.user_type=#{USER_TYPE_EMPLOYEE}",
			attrName="employee", columns={
					@Column(includeEntity=Employee.class)
			}),
		@JoinTable(type=Type.LEFT_JOIN, entity=Office.class, alias="o", 
			on="o.office_code=e.office_code", attrName="employee.office",
			columns={
					@Column(name="office_code", label="机构编码", isPK=true),
					@Column(name="parent_codes",label="所有父级编码", queryType=QueryType.LIKE),
					@Column(name="view_code", 	label="机构代码"),
					@Column(name="office_name", label="机构名称", isQuery=false),
					@Column(name="full_name", 	label="机构全称"),
					@Column(name="office_type", label="机构类型"),
					@Column(name="leader", 		label="负责人"),
					@Column(name="phone", 		label="电话"),
					@Column(name="address", 	label="联系地址"),
					@Column(name="zip_code", 	label="邮政编码"),
					@Column(name="email", 		label="邮箱"),
			}),
		@JoinTable(type=Type.LEFT_JOIN, entity=Company.class, alias="c", 
			on="c.company_code=e.company_code", attrName="employee.company",
			columns={
					@Column(name="company_code", label="公司编码", isPK=true),
					@Column(name="parent_codes",label="所有父级编码", queryType=QueryType.LIKE),
					@Column(name="view_code", 	label="公司代码"),
					@Column(name="company_name", label="公司名称", isQuery=false),
					@Column(name="full_name", 	label="公司全称"),
					@Column(name="area_code", attrName="area.areaCode", label="区域编码"),
			}),
		@JoinTable(type=Type.LEFT_JOIN, entity=Area.class, alias="ar",
			on="ar.area_code = c.area_code", attrName="employee.company.area",
			columns={
					@Column(name="area_code", label="区域代码", isPK=true),
					@Column(name="area_name", label="区域名称", isQuery=false),
					@Column(name="area_type", label="区域类型"),
		}),
	},
	extWhereKeys="dsfOffice, dsfCompany",
	orderBy="a.user_weight DESC, a.update_date DESC"
)
public class EmpUser extends User {
	
	private static final long serialVersionUID = 1L;

	public EmpUser() {
		this(null);
	}

	public EmpUser(String id){
		super(id);
	}
	
	@Valid
	@ExcelFields({
		@ExcelField(title="归属机构", attrName="employee.office", align=Align.CENTER, sort=10, fieldType=OfficeType.class),
		@ExcelField(title="归属部门", attrName="employee.company", align = Align.CENTER, sort=20, fieldType=CompanyType.class),
//		@ExcelField(title="登录账号", attrName="loginCode", align=Align.CENTER, sort=30),
//		@ExcelField(title="用户昵称", attrName="userName", align=Align.CENTER, sort=40),
		@ExcelField(title="电子邮箱", attrName="email", align=Align.LEFT, sort=50),
		@ExcelField(title="手机号码", attrName="mobile", align=Align.CENTER, sort=60),
		@ExcelField(title="办公电话", attrName="phone", align=Align.CENTER, sort=70),
		@ExcelField(title="执法证号", attrName="employee.empCode", align=Align.CENTER, sort=80),
		@ExcelField(title="员工姓名", attrName="employee.empName", align=Align.CENTER, sort=95),
		@ExcelField(title="拥有角色编号", attrName="userRoleString", align=Align.LEFT, sort=800, type=ExcelField.Type.IMPORT),
		@ExcelField(title="最后登录日期", attrName="lastLoginDate", align=Align.CENTER, sort=900, type=ExcelField.Type.EXPORT, dataFormat="yyyy-MM-dd HH:mm"),
	})
	public Employee getEmployee(){
		Employee employee = (Employee)super.getRefObj();
		if (employee == null){
			employee = new Employee();
			super.setRefObj(employee);
		}
		return employee;
	}
	
	public void setEmployee(Employee employee){
		super.setRefObj(employee);
	}
	
}