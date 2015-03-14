package com.taobao.trip.tframe.process;

import java.io.Serializable;


public class ProcessResultDO implements Serializable{

    private static final long serialVersionUID = 76948397355501864L;
	protected boolean isHandlerEndWithOutSave =false;
    private boolean success = true; // Ö´ÐÐÊÇ·ñ³É¹¦
    private String errCode;// ´íÎó´úÂë
    private String errMsg;// ´íÎóÏêÏ¸ÃèÊö

	public ProcessResultDO() {
		this.setSuccess(true);
	}

	public ProcessResultDO(boolean b) {
		this.setSuccess(b);
	}
	public void setHandlerEndWithOutSave(boolean isHandlerEndWithOutSave) {
		this.isHandlerEndWithOutSave = isHandlerEndWithOutSave;
	}
	public boolean isHandlerEndWithOutSave() {
		return isHandlerEndWithOutSave;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
		this.success = false;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
		this.success = false;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public boolean isFailure() {
		return !success;
	}
	@Override
	public String toString() {
		return "ProcessResultDO [errCode=" + errCode + ", errMsg=" + errMsg +
				", success=" + success + "]";
	}
}