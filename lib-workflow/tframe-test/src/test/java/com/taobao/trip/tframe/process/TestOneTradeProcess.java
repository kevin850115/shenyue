package com.taobao.trip.tframe.process;

import com.taobao.common.dao.persistence.exception.DAOException;





class Act1to2 extends NothingHandler{ @Override public IHandlerName getName() { return TestHandlerName.Act1to2; } }
class ActStay1and2 extends NothingHandler{ @Override public IHandlerName getName() { return TestHandlerName.ActStay1and2; } }
class Act2to3 extends NothingHandler{ @Override public IHandlerName getName() { return TestHandlerName.Act2to3; } }
class Act3to5 extends NothingHandler{ @Override public IHandlerName getName() { return TestHandlerName.Act3to5; } }
class ActAt4and5 extends NothingHandler{ @Override public IHandlerName getName() { return TestHandlerName.ActAt4and5; } }
class ActAlways extends  NothingHandler{ @Override public IHandlerName getName() { return TestHandlerName.ActAlways; } }
class ActStay1and2WithoutSave extends NothingHandler{ 
	@Override
	public ProcessResultDO execute(Request context) {
		ProcessResultDO rs = new ProcessResultDO();
		rs.setHandlerEndWithOutSave(true);
		return rs;
	}
	@Override public IHandlerName getName() { 
		return TestHandlerName.ActStay1and2WithoutSave; 
	} 
}
class ActStay1and2ExecException extends NothingHandler{ 
	@Override
	public ProcessResultDO execute(Request context) {
		ProcessResultDO rs = new ProcessResultDO();
		rs.setHandlerEndWithOutSave(true);
		throw new NullPointerException("NN");
	}
	@Override public IHandlerName getName() { 
		return TestHandlerName.ActStay1and2ExecException; 
	} 
}
class ActStay1and2SaveException extends NothingHandler{ 

	@Override
	public void save(TestModel dataModel, ProcessResultDO result)
			throws Exception {
		throw new DAOException("存储出错");
	}
	@Override public IHandlerName getName() { 
		return TestHandlerName.ActStay1and2SaveException; 
	} 
}
class ActStay1and2ExecNullException extends NothingHandler{ 
	@Override
	public ProcessResultDO execute(Request context) {
		return null;
	}
	@Override public IHandlerName getName() { 
		return TestHandlerName.ActStay1and2ExecNullException; 
	} 
}

/*
 * 一个流程定义过程.
 */
public class TestOneTradeProcess extends TframeProcessManager{
	public void initHandler() throws ProcessException{

		//step1.注册Action
		register(new Act1to2()).from(BizStatus.N1).to(BizStatus.N2);
		//重复注册，OK
		register(new Act1to2()).from(BizStatus.N1).to(BizStatus.N2);
		register(new Act2to3()).from(BizStatus.N2).to(BizStatus.N3);
		register(new Act3to5()).from(BizStatus.N3).to(BizStatus.N5);
		register(new ActAt4and5()).at(BizStatus.N4);
		register(new ActAt4and5()).at(BizStatus.N5);
		register(new ActStay1and2()).at(BizStatus.N1);
		register(new ActStay1and2()).at(BizStatus.N2);
		
		register(new ActStay1and2ExecException()).at(BizStatus.N1);
		register(new ActStay1and2WithoutSave()).at(BizStatus.N1);
		register(new ActStay1and2SaveException()).at(BizStatus.N1);
		register(new ActStay1and2ExecNullException()).at(BizStatus.N1);
		
		register(new ActAlways()).all(TestStatusTypeEnum.BIZ);
		register(new MyResultHandler()).all(TestStatusTypeEnum.BIZ);
	}

}
