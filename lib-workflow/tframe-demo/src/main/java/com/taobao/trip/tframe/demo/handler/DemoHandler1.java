package com.taobao.trip.tframe.demo.handler;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taobao.common.dao.persistence.exception.DAOException;
import com.taobao.trip.tframe.demo.ProcDO;
import com.taobao.trip.tframe.demo.ao.DemoCreateParam;
import com.taobao.trip.tframe.demo.constant.DemoHandlerName;
import com.taobao.trip.tframe.process.IHandlerName;
import com.taobao.trip.tframe.process.ProcessResultDO;
import com.taobao.trip.tframe.process.Request;

@Component
public class DemoHandler1 extends DemoAbstractHandler<DemoCreateParam>{
	
	@Override
	public IHandlerName getName() {
		return DemoHandlerName.H1;
	}

	@Override
	public void save2db(ProcDO dataModel, ProcessResultDO result)
			throws DAOException {
		log.info(this.getName()+" save2db");
		
	}

	@Override
	public ProcessResultDO execute(Request<DemoCreateParam, ProcDO> request) {
		log.info(this.getName()+" exec");
		DemoCreateParam param = request.getRequestParam();
		if ( param != null ){
			log.info("param is:"+JSON.toJSONString(param));
		}
		return new ProcessResultDO();
	}

}
