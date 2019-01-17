package com.jeesite.modules.restful.dto;

public class Result {
	public static String SUCCESS="成功";
	public static String FAIL="失败";
	public static int ERR_CODE=10000;
	public static int SUCCESS_CODE=20000;
	private boolean success=true;// True ：成功，false 表示失败
	private int errCode=SUCCESS_CODE;// 10000 ：标识失败，20000标识成功
	private String msg=SUCCESS;// 信息描述
	private Object data;// 成功的数据集合

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
