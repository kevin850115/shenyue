package com.taobao.trip.tframe.process.ctx;

import com.taobao.trip.tframe.process.IStatusContext;
import com.taobao.trip.tframe.process.IStatusValue;


/**
 * 单个状态运行时上下文信息
 * @author leconte
 *
 */
public abstract class AbstractStatusContext<T> implements IStatusContext {
	protected T bindingObj;
	protected IStatusValue oldStatus;
	abstract protected Integer getCurrentStatusFromBindingObj(T obj);
	abstract protected void setCurrentStatus2BindingObj(T obj,Integer s);
	public AbstractStatusContext(T t){
		bindingObj = t;
	}
	final public IStatusValue getCurrentStatus(){
		Integer statusValue = getCurrentStatusFromBindingObj(bindingObj);
		return getStatusType().getValue(statusValue);
	}
	final public IStatusValue getOldStatus(){
		return oldStatus;
	}
	final public void changeStatusTo(IStatusValue s) {
		oldStatus = getCurrentStatus();
		setCurrentStatus(s);
	}
	protected void setCurrentStatus(IStatusValue s){
		if ( bindingObj != null ){
			setCurrentStatus2BindingObj(bindingObj,s.getStatusValue());
		}
	}
	final public boolean isChanged(){
		IStatusValue status = getCurrentStatus();
		IStatusValue oldStatus = getOldStatus();
		if ( oldStatus == null || status == null){
		    return false;
		}
	    return !oldStatus.getStatusValue().equals(status.getStatusValue());
	}
    public String toString() {
        return "[current=" + getCurrentStatus() + ", old=" + getOldStatus()
	        + ", isChanged()=" + isChanged() + "]";
    }
	
}
