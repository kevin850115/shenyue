package com.taobao.trip.tframe.spring;

import org.springframework.stereotype.Component;

import com.taobao.trip.tframe.process.Request;
import com.taobao.trip.tframe.process.TframeProcessManager;
import com.taobao.trip.tframe.process.selector.IProcessSelector;
@Component
public class FakePMSelector implements IProcessSelector {


	@Override
	public TframeProcessManager getProcessManager(Request ctx) {
		return null;
	}

}
