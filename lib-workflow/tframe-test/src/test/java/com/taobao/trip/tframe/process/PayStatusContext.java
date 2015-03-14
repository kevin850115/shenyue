package com.taobao.trip.tframe.process;

import com.taobao.trip.tframe.process.ctx.AbstractIntegerContext;


/*
 * 支付上下文
 */
public class PayStatusContext extends AbstractIntegerContext {

	public PayStatusContext(Integer t) {
		super(t);
	}

	@Override
	public IStatusType getStatusType() {
		return TestStatusTypeEnum.PAY;
	}

}
