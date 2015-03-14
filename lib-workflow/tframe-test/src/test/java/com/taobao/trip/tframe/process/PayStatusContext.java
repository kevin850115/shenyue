package com.taobao.trip.tframe.process;

import com.taobao.trip.tframe.process.ctx.AbstractIntegerContext;


/*
 * ֧��������
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
