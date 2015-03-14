package com.taobao.trip.tframe.err;

import com.taobao.trip.tframe.process.ProcessResultDO;


/**
 *
 * @description 主要用于记录流程引擎的错误
 * @version 1.0
 * @author zhanghei
 * @update 2012-2-14 下午01:53:58
 */
public enum ProcessErrorEnum {

    NOTIFY_SEND_NULL("004","notify发送返回空指针"),
    PROCESS_NOT_EXIST("005","订单流程不存在"),
    ALREADY_HANDLED("006","此流程已经执行过"),
    FEATURE_NOT_EXIST("007","订单特性定义不存在"),
    BEAN_COPY_ERROR("008","Bean拷贝错误"),
    UNKNOWN_ERROR("009","未知错误"),
    HSF_CALL_FAILED("010","HSF调用失败"),
    HANDLER_NOT_SUPPORT("011","此流程不支持"),
    HANDLER_EXE_NULL("013","exe返回null"),
    SAVE2DB_IN_HANDLER_EXCEPTION("012","数据库保存错误"),
    UNKNOWN_EXCEPTION("016","未知异常"),
    PROCESS_EXCEPTION("017","流程引擎异常"),
    CONTEXT_EXCEPTION("018","数据模型异常"),
    NULL("015","空指针"),
    ORDER_NULL("020","订单数据为空"),
    NO_IMPLEMENT("014","该动作尚未实现");
    public void fillResult(ProcessResultDO rs){
    	 rs.setErrCode(code);
         rs.setErrMsg(msg);
    }
    public boolean isEqual(ProcessResultDO rs ){
        return code.equals(rs.getErrCode());
    }

    ProcessErrorEnum(String c,String m){
    	code = c;
    	msg = m;
    }
    private String code;
    private String msg;
	public void setCode(String code) {
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getMsg() {
		return msg;
	}
    

}
