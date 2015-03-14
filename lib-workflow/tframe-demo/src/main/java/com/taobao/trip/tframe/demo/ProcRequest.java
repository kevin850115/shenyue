package com.taobao.trip.tframe.demo;

import com.taobao.trip.tframe.demo.status.DemoStatusType;
import com.taobao.trip.tframe.demo.status.ProcBookStatusContext;
import com.taobao.trip.tframe.demo.status.ProcPayStatusContext;
import com.taobao.trip.tframe.demo.status.ProcStatusContext;
import com.taobao.trip.tframe.process.IStatusType;
import com.taobao.trip.tframe.process.Request;

public class ProcRequest<Param> extends Request<Param,ProcDO>{

	
	@Override
	public IStatusType[] getStatusTypes() {
		return DemoStatusType.values();
	}

	@Override
	public Long getUniqId() {
		ProcDO procDO = getDataModel();
		long id = procDO.getId();
		if ( id == 0l){
			return null;
		}
		return id;
	}

	@Override
	protected void initCtx() {
		ProcDO procDO = getDataModel();
		if ( procDO != null ){
			addContext(new ProcPayStatusContext(procDO));
			addContext(new ProcStatusContext(procDO));
			addContext(new ProcBookStatusContext(procDO));
		}
	}


}
