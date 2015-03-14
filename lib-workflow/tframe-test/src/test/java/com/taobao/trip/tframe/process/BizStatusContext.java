package com.taobao.trip.tframe.process;

import com.taobao.trip.tframe.process.ctx.AbstractIntegerContext;

/*
 * в╢л╛иообнд
 */
public class BizStatusContext extends AbstractIntegerContext {

	public BizStatusContext(Integer t) {
		super(t);
	}

	@Override
	public IStatusType getStatusType() {
		return TestStatusTypeEnum.BIZ;
	}


}
