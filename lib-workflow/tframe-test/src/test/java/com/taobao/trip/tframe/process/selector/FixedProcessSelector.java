package com.taobao.trip.tframe.process.selector;

import com.taobao.trip.tframe.process.Request;
import com.taobao.trip.tframe.process.TframeProcessManager;
public class FixedProcessSelector implements IProcessSelector {
	TframeProcessManager pm;
	public FixedProcessSelector(TframeProcessManager pm){
		this.pm = pm;
	}
	@Override
	public TframeProcessManager getProcessManager(Request<?,?> ctx) {
		return pm;
	}

}
