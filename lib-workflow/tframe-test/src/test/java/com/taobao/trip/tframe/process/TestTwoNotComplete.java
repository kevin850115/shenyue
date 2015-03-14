package com.taobao.trip.tframe.process;



public class TestTwoNotComplete extends TframeProcessManager{

	@Override
	protected void initHandler() throws ProcessException {
		register(new TwoAct1to2()).from(BizStatus.N1).to(BizStatus.N2);
		register(new TwoAct1to2()).from(PayStatus.P1);//没有TO
		register(new TwoAct1to2()).from(PayStatus.P2).to(PayStatus.P3);//没有TO
		
	}

}
