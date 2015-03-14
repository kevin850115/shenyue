package com.taobao.trip.tframe.err;

import com.taobao.trip.tframe.process.ProcessResultDO;


/**
 *
 * @description ��Ҫ���ڼ�¼��������Ĵ���
 * @version 1.0
 * @author zhanghei
 * @update 2012-2-14 ����01:53:58
 */
public enum ProcessErrorEnum {

    NOTIFY_SEND_NULL("004","notify���ͷ��ؿ�ָ��"),
    PROCESS_NOT_EXIST("005","�������̲�����"),
    ALREADY_HANDLED("006","�������Ѿ�ִ�й�"),
    FEATURE_NOT_EXIST("007","�������Զ��岻����"),
    BEAN_COPY_ERROR("008","Bean��������"),
    UNKNOWN_ERROR("009","δ֪����"),
    HSF_CALL_FAILED("010","HSF����ʧ��"),
    HANDLER_NOT_SUPPORT("011","�����̲�֧��"),
    HANDLER_EXE_NULL("013","exe����null"),
    SAVE2DB_IN_HANDLER_EXCEPTION("012","���ݿⱣ�����"),
    UNKNOWN_EXCEPTION("016","δ֪�쳣"),
    PROCESS_EXCEPTION("017","���������쳣"),
    CONTEXT_EXCEPTION("018","����ģ���쳣"),
    NULL("015","��ָ��"),
    ORDER_NULL("020","��������Ϊ��"),
    NO_IMPLEMENT("014","�ö�����δʵ��");
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
