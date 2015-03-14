package com.taobao.trip.tframe.process.ctx;

import com.taobao.trip.tframe.process.IStatusType;
import com.taobao.trip.tframe.process.IStatusValue;

public class ProcessInitContext extends AbstractIntegerContext {

	IStatusType st;
	public ProcessInitContext(IStatusType st) {
		super(IStatusValue.PROCESS_INITIAL.getStatusValue());
		this.st = st;
	}

	@Override
	public IStatusType getStatusType() {
		return st;
	}

}
