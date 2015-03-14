package com.taobao.trip.tframe.process;



public class TestOneTradeProcessHasInvalidAction extends TframeProcessManager{
	@Override
	protected void initHandler() throws ProcessException {
		//step1.注册Action，指向了不同的结束节点，注册失败
		register(new Act1to2()).from(BizStatus.N1).to(BizStatus.N2);
		register(new Act1to2()).from(BizStatus.N1).to(BizStatus.N3);
	}


}


