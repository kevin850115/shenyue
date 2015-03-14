package com.taobao.trip.tframe.process;




class TwoAct1to2 extends NothingHandler{ @Override public IHandlerName getName() { return TestHandlerName.TwoAct1to2; } }
class TwoAct1to2Sub1 extends NothingHandler{ @Override public IHandlerName getName() { return TestHandlerName.TwoAct1to2Sub1; } }
class TwoAct1to2Sub2 extends NothingHandler{ @Override public IHandlerName getName() { return TestHandlerName.TwoAct1to2Sub2; } }
class TwoActStay1and2 extends NothingHandler{ @Override public IHandlerName getName() { return TestHandlerName.TwoActStay1and2; } }
class TwoAct2to3 extends NothingHandler{ @Override public IHandlerName getName() { return TestHandlerName.TwoAct2to3; } }
class TwoActBefore2 extends NothingHandler{ @Override public IHandlerName getName() { return TestHandlerName.TwoActBefore2; } }
class TwoActAfter2 extends NothingHandler{ @Override public IHandlerName getName() { return TestHandlerName.TwoActAfter2; } }
class TwoAct3to5 extends NothingHandler{ @Override public IHandlerName getName() { return TestHandlerName.TwoAct3to5; } }
class TwoActAt4and5 extends NothingHandler{ @Override public IHandlerName getName() { return TestHandlerName.TwoActAt4and5; } }
class TwoActAlways extends NothingHandler{ @Override public IHandlerName getName() { return TestHandlerName.TwoActAlways; } }
public class TestTwoTradeProcess extends TframeProcessManager{
	@Override
	protected void initHandler() throws ProcessException {
		//step1.注册Action
		register(new TwoAct1to2()).from(BizStatus.N1).to(BizStatus.N2);
		register(new TwoAct1to2()).from(PayStatus.P1).to(PayStatus.P2);
		register(new TwoAct2to3()).from(PayStatus.P2).to(PayStatus.P3);
		TwoAct3to5 act3to5 = new TwoAct3to5();
		register(act3to5).from(BizStatus.N3).to(BizStatus.N5);
		register(act3to5).from(PayStatus.P3).to(PayStatus.P5);
		register(act3to5).from(PayStatus.P3).to(PayStatus.P5);//重复没关系
		unregister(act3to5);
	}

	
}
