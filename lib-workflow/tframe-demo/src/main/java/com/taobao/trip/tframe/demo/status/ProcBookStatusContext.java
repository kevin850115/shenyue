package com.taobao.trip.tframe.demo.status;

import com.taobao.trip.tframe.demo.ProcDO;
import com.taobao.trip.tframe.process.IStatusType;
import com.taobao.trip.tframe.process.ctx.AbstractStatusContext;

public class ProcBookStatusContext extends AbstractStatusContext<ProcDO>{

	public ProcBookStatusContext(ProcDO t) {
		super(t);
	}

	@Override
	public IStatusType getStatusType() {
		return DemoStatusType.T;
	}

	@Override
	protected Integer getCurrentStatusFromBindingObj(ProcDO obj) {
		return obj.getBookStatus();
	}

	@Override
	protected void setCurrentStatus2BindingObj(ProcDO obj, Integer s) {
		obj.setBookStatus(s);
	}
}
