package com.taobao.trip.tframe.demo.status;

import com.taobao.trip.tframe.demo.ProcDO;
import com.taobao.trip.tframe.process.IStatusType;
import com.taobao.trip.tframe.process.ctx.AbstractStatusContext;

public class ProcStatusContext extends AbstractStatusContext<ProcDO>{

	public ProcStatusContext(ProcDO t) {
		super(t);
	}

	@Override
	public IStatusType getStatusType() {
		return DemoStatusType.B;
	}

	@Override
	protected Integer getCurrentStatusFromBindingObj(ProcDO obj) {
		return obj.getStatus();
	}

	@Override
	protected void setCurrentStatus2BindingObj(ProcDO obj, Integer s) {
		obj.setStatus(s);
	}
}
