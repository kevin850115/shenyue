package com.taobao.trip.tframe.demo.handler;

import org.springframework.stereotype.Component;

import com.taobao.trip.tframe.demo.constant.DemoHandlerName;
import com.taobao.trip.tframe.process.IHandlerName;

@Component
public class DemoHandler11 extends  NothingHandler{

	@Override
	public IHandlerName getName() {
		return DemoHandlerName.H11;
	}

	

}