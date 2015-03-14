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
 * 用来收集系统中的运行实例
 * @author leconte
 */
@Component
public final class TframeSystem {

	public static Map<String,TframeAbstractHandler> handlers = new HashMap<String,TframeAbstractHandler>();//所有Handler实例
	public static Map<String,TframeProcessManager> processdefs = new HashMap<String,TframeProcessManager>();//所有交易流程定义
	public static IProcessSelector selector ;//交易流程选择器
	public static TframeSystemConfig systemConfig;//系统配置
	
	@PostConstruct
	public void init(){
	}
	
	public static String getSystemSummary(){
		StringBuilder sb = new StringBuilder();
		sb.append("Handler总数:"+handlers.size()).append("|");
		sb.append("ProcessDef总数:"+processdefs.size()).append("|");
		return sb.toString();
	}
	
}
