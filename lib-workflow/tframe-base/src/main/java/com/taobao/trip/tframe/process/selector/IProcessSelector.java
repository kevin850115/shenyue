package com.taobao.trip.tframe.process.selector;

import com.taobao.trip.tframe.process.Request;
import com.taobao.trip.tframe.process.TframeProcessManager;

/*
 * ����ѡ����������ģ������ѡ���Ӧ�Ķ�����������
 */
public interface IProcessSelector {
	
	TframeProcessManager getProcessManager(Request<?,?> req);
	
}
