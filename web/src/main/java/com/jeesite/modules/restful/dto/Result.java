package com.jeesite.modules.restful.dto;

public class Result {
	private boolean success=true;// True ：成功，false 表示失败
	private int errCode=20000;// 10000 ：标识失败，20000标识成功
	private String msg="成功";// 信息描述
	private int count;// 成功的数量
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
