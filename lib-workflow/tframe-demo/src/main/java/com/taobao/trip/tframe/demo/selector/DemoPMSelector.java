package com.taobao.trip.tframe.demo.selector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.trip.tframe.demo.ProcDO;
import com.taobao.trip.tframe.demo.processdef.DemoProcessDef1;
import com.taobao.trip.tframe.demo.processdef.DemoProcessDef2;
import com.taobao.trip.tframe.process.Request;
import com.taobao.trip.tframe.process.TframeProcessManager;
import com.taobao.trip.tframe.process.selector.IProcessSelector;
@Component
public class DemoPMSelector implements IProcessSelector {
	
	@Autowired
	DemoProcessDef1 demoProcessDef1;
	@Autowired
	DemoProcessDef2 demoProcessDef2;

	@Override
	public TframeProcessManager getProcessManager(Request<?, ?> ctx) {
		//1.³óÂªµã£¬
		Object dataModel = ctx.getDataModel();
		if ( dataModel instanceof ProcDO ){
			ProcDO procDO = (ProcDO) dataModel;
			if ( procDO.getType() == 1 ){
				return demoProcessDef1;
			}else if ( procDO.getType() == 2){
				return demoProcessDef2;
			}
		}
		return null;
	}

}
