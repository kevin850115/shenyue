package com.taobao.trip.tframe.demo.handler;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.taobao.common.dao.persistence.exception.DAOException;
import com.taobao.trip.tframe.demo.ProcDO;
import com.taobao.trip.tframe.demo.constant.DemoHandlerName;
import com.taobao.trip.tframe.process.IHandlerName;
import com.taobao.trip.tframe.process.ProcessResultDO;

@Component
public class DemoHandler28 extends NothingHandler{
	
	@Override
	public IHandlerName getName() {
		return DemoHandlerName.H28;
	}

	
	@Override
	public void save2db(ProcDO dataModel, ProcessResultDO result)
	throws DAOException {
		log.info(this.getName()+" save2db");
		try {
			TimeUnit.SECONDS.sleep(60);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
