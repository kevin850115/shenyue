package com.taobao.trip.tframe.process;



public class TestOneTradeProcessHasInvalidAction extends TframeProcessManager{
	@Override
	protected void initHandler() throws ProcessException {
		//step1.ע��Action��ָ���˲�ͬ�Ľ����ڵ㣬ע��ʧ��
		register(new Act1to2()).from(BizStatus.N1).to(BizStatus.N2);
		register(new Act1to2()).from(BizStatus.N1).to(BizStatus.N3);
	}


}


