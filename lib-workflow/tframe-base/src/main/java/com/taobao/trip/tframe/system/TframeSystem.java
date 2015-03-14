package com.taobao.trip.tframe.system;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.taobao.trip.tframe.config.TframeSystemConfig;
import com.taobao.trip.tframe.handler.TframeAbstractHandler;
import com.taobao.trip.tframe.process.TframeProcessManager;
import com.taobao.trip.tframe.process.selector.IProcessSelector;

/**
 * �����ռ�ϵͳ�е�����ʵ��
 * @author leconte
 */
@Component
public final class TframeSystem {

	public static Map<String,TframeAbstractHandler> handlers = new HashMap<String,TframeAbstractHandler>();//����Handlerʵ��
	public static Map<String,TframeProcessManager> processdefs = new HashMap<String,TframeProcessManager>();//���н������̶���
	public static IProcessSelector selector ;//��������ѡ����
	public static TframeSystemConfig systemConfig;//ϵͳ����
	
	@PostConstruct
	public void init(){
	}
	
	public static String getSystemSummary(){
		StringBuilder sb = new StringBuilder();
		sb.append("Handler����:"+handlers.size()).append("|");
		sb.append("ProcessDef����:"+processdefs.size()).append("|");
		return sb.toString();
	}
	
}
