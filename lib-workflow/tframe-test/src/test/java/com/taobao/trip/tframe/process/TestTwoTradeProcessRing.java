package com.taobao.trip.tframe.process;



public class TestTwoTradeProcessRing extends TframeProcessManager{

	@Override
	protected void initHandler() throws ProcessException {
		register(new TwoAct1to2()).from(BizStatus.N1).to(BizStatus.N2);
		register(new TwoAct1to2()).from(PayStatus.P1).to(PayStatus.P2);
		register(new TwoAct2to3()).from(PayStatus.P2).to(PayStatus.P3);
		register(new TwoAct2to3()).from(BizStatus.N2).to(BizStatus.N1);
		
	}

}
