package com.taobao.trip.tframe.demo.status;

import com.taobao.trip.tframe.demo.ProcDO;
import com.taobao.trip.tframe.process.IStatusType;
import com.taobao.trip.tframe.process.ctx.AbstractStatusContext;

public class ProcPayStatusContext extends AbstractStatusContext<ProcDO>{

	public ProcPayStatusContext(ProcDO t) {
		super(t);
	}

	@Override
	public IStatusType getStatusType() {
		return DemoStatusType.P;
	}

	@Override
	protected Integer getCurrentStatusFromBindingObj(ProcDO obj) {
		return obj.getPayStatus();
	}

	@Override
	protected void setCurrentStatus2BindingObj(ProcDO obj, Integer s) {
		obj.setPayStatus(s);
	}
	
}
