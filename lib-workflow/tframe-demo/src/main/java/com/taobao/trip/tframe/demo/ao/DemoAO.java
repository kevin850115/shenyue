package com.taobao.trip.tframe.demo.ao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.trip.tframe.demo.ProcDO;
import com.taobao.trip.tframe.demo.ProcRequest;
import com.taobao.trip.tframe.demo.constant.DemoHandlerName;
import com.taobao.trip.tframe.process.ProcessResultDO;
import com.taobao.trip.tframe.process.trigger.TframeEngine;

@Component
public class DemoAO {

	@Autowired
	TframeEngine engine;
	public ProcessResultDO createOrder(DemoCreateParam param){
		ProcessResultDO rs = new ProcessResultDO();
		ProcDO order = new ProcDO();
		order.setType(1);
		ProcRequest<DemoCreateParam> request = new ProcRequest<DemoCreateParam>();
		request.setDataModel(order);
		request.setRequestParam(param);
		ProcessResultDO prs = engine.trigger(request, DemoHandlerName.H1);
		if ( prs.isFailure() ){
			return rs;
		}
		//rs.setModel("param", ctx.getInputParam(DemoCreateParam.class));
		//rs.setModel("data2", ctx.getData("2", Integer.class));
		return rs;
		
	}
}
