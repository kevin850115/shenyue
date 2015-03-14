package com.taobao.trip.tframe.process;


public abstract class NothingHandler implements IHandler<TestParam,ProcessResultDO,TestModel>{

	@Override
	public ProcessResultDO execute(Request<TestParam, TestModel> request) {
		return new ProcessResultDO();
	}


	@Override
	public void save(TestModel dataModel, ProcessResultDO result)
			throws Exception {
		
	}
	
}
