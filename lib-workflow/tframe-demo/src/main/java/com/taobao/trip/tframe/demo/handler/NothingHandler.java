package com.taobao.trip.tframe.demo.handler;

import com.taobao.common.dao.persistence.exception.DAOException;
import com.taobao.trip.tframe.demo.ProcDO;
import com.taobao.trip.tframe.demo.param.DefaultParam;
import com.taobao.trip.tframe.process.ProcessResultDO;
import com.taobao.trip.tframe.process.Request;

public abstract class NothingHandler extends DemoDefaultParamHandler{
	
	@Override
	public void save2db(ProcDO dataModel, ProcessResultDO result)
			throws DAOException {
		log.info(this.getName()+" save2db");
		
	}

	@Override
	public ProcessResultDO execute(Request<DefaultParam, ProcDO> request) {
		log.info(this.getName()+" exec");
		return new ProcessResultDO();
	}

}