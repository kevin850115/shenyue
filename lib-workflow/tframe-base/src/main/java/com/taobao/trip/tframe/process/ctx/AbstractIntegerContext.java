package com.taobao.trip.tframe.process.ctx;

import com.taobao.trip.tframe.process.IStatusValue;

public abstract class AbstractIntegerContext extends AbstractStatusContext<Integer>{

	public AbstractIntegerContext(Integer t) {
		super(t);
	}

	@Override
	protected Integer getCurrentStatusFromBindingObj(Integer obj) {
		return obj;
	}

	protected void setCurrentStatus(IStatusValue s){
		bindingObj = s.getStatusValue();
	}

	@Override
	protected void setCurrentStatus2BindingObj(Integer obj, Integer s) {
	}
	
}
