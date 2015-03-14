package com.taobao.trip.tframe.process.selector;

import com.taobao.trip.tframe.process.Request;
import com.taobao.trip.tframe.process.TframeProcessManager;

/*
 * 流程选择器，根据模型特性选择对应的订单处理流程
 */
public interface IProcessSelector {
	
	TframeProcessManager getProcessManager(Request<?,?> req);
	
}
