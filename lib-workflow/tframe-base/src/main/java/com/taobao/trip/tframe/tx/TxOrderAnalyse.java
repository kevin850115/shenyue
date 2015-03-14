package com.taobao.trip.tframe.tx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.trip.tframe.annotation.TxOrder;

public class TxOrderAnalyse {
	static private Logger log = LoggerFactory.getLogger(TxOrderAnalyse.class);
    private static ThreadLocal<Boolean> analyzeOn = new ThreadLocal<Boolean>();
    private static ThreadLocal<Integer> lastTxNumber = new ThreadLocal<Integer>();
    private static ThreadLocal<Boolean> isOutOfOrder = new ThreadLocal<Boolean>();
	
	static public void setAnalyzeOn(){
		analyzeOn.set(Boolean.TRUE);
		lastTxNumber.set(-100);
		isOutOfOrder.set(Boolean.FALSE);
	}
	static public void setAnalyzeOff(){
		analyzeOn.set(Boolean.FALSE);
		lastTxNumber.set(-100);
		isOutOfOrder.set(Boolean.FALSE);
	}
	static public boolean isOutOfOrder(){
		return isOutOfOrder.get()!=null && isOutOfOrder.get();
	}
	static String getDesc(Object cl,String method){
		return "["+cl.getClass().getName()+":"+method+"]";
	}
	static public void txIsCalled(Object cl,String method){
		String desc = getDesc(cl,method);
		if ( analyzeOn.get() == null || analyzeOn.get().equals(Boolean.FALSE) ){
			if ( log.isDebugEnabled() ){
				log.debug("不在事务里，忽略"+desc);
			}
			return;
		}
        TxOrder txOrder = cl.getClass().getAnnotation(TxOrder.class);
        if ( txOrder == null ){
			log.error("在事务里，但没有TxOrder标签，需要注意！！"+desc);
			return;
        }	
        int number = txOrder.number();
        if ( number < 0 || lastTxNumber.get() ==null){
        	lastTxNumber.set(number);
			log.info("次序:"+number+"."+desc);
        	return;
        }else{
        	if ( number < lastTxNumber.get() ){
				log.error("次序为:"+number+"，小于"+lastTxNumber.get()+"，错误。"+desc);
				isOutOfOrder.set(Boolean.TRUE);
        	}else{
	        	lastTxNumber.set(number);
				log.info("次序:"+number+"."+desc);
        	}
        }
	}

}
