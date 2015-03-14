package com.taobao.trip.tframe.process;




abstract class FailAction extends NothingHandler{
	public ProcessResultDO execute(Request context){
		return new ProcessResultDO(false);
	}
}
class FailAct1to2 extends FailAction{ 	@Override public IHandlerName getName() {  return TestHandlerName.FailAct1to2;} }
public class TestOneTradeProcessHasFailedAction extends TframeProcessManager{

	@Override
	protected void initHandler() throws ProcessException {
		//step1.注册Action
		register(new FailAct1to2()).from(BizStatus.N1).to(BizStatus.N2);
		//各种重复注册，没关系
		register(new FailAct1to2()).from(BizStatus.N1).to(BizStatus.N2);
		register(new FailAct1to2()).from(BizStatus.N1).to(BizStatus.N2);
		register(new FailAct1to2()).from(BizStatus.N1).to(BizStatus.N2);
		register(new FailAct1to2()).from(BizStatus.N1).to(BizStatus.N2);
		register(new FailAct1to2()).from(BizStatus.N1).to(BizStatus.N2);
		register(new FailAct1to2()).from(BizStatus.N1).to(BizStatus.N2);
		register(new FailAct1to2()).from(BizStatus.N1).to(BizStatus.N2);
		
	}

}

