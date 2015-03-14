package com.taobao.trip.tframe.process;

import com.taobao.trip.tframe.process.result.MyResultDO;


public class MyResultHandler implements IHandler<TestParam,MyResultDO,TestModel>{

	@Override
	public MyResultDO execute(Request<TestParam, TestModel> request) {
		MyResultDO mr = new MyResultDO();
		mr.setRr("MyRRRRRRRRRRRRRR");
		return mr;
	}

	@Override
	public IHandlerName getName() {
		return TestHandlerName.MY;
	}

	@Override
	public void save(TestModel dataModel, MyResultDO result) throws Exception {
		
	}

	
}
