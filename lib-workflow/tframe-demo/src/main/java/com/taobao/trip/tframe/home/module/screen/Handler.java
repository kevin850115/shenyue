package com.taobao.trip.tframe.home.module.screen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.service.requestcontext.parser.ParameterParser;
import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.citrus.turbine.util.TurbineUtil;
import com.taobao.trip.tframe.common.WebBase;
import com.taobao.trip.tframe.demo.ProcDO;
import com.taobao.trip.tframe.demo.ProcRequest;
import com.taobao.trip.tframe.demo.constant.DemoHandlerName;
import com.taobao.trip.tframe.demo.status.DemoStatusType;
import com.taobao.trip.tframe.handler.TframeAbstractHandler;
import com.taobao.trip.tframe.process.IHandlerName;
import com.taobao.trip.tframe.process.ProcessResultDO;
import com.taobao.trip.tframe.process.Request;
import com.taobao.trip.tframe.process.trigger.TframeEngine;
import com.taobao.trip.tframe.system.TframeSystem;

/**
 * ʱ����ӿڣ�ĳ����ĳʱ�䷶Χ��Ʊ�����ͼ�����
 * 
 */
public class Handler extends WebBase {

	@Autowired
	TframeEngine tframeEngine;
	public void execute(Context context) {
		TurbineRunData rundata = TurbineUtil.getTurbineRunData(request);
		ParameterParser parameters = rundata.getParameters();
		Map<String,Integer> inputValue=new HashMap<String,Integer>();
		//0.��ȡ���
		Map<String, String> parameterMap = getParameterMap(rundata);
		for ( Map.Entry<String, String> ent:parameterMap.entrySet()){
			if ( ent.getKey().startsWith("D_")){
				inputValue.put(ent.getKey().substring(2), Integer.valueOf(ent.getValue()));
			}
		}
		//1.״ֵ̬Map
		context.put("allValues",DemoStatusType.values());
		context.put("selected",inputValue);
		//1.5.��ȡϵͳ����Handler������Ļ�ϡ�
		Map<IHandlerName,String> allHandlers = new HashMap<IHandlerName,String>();
		Map<String, TframeAbstractHandler> handlers = TframeSystem.handlers;
		for (Map.Entry<String, TframeAbstractHandler> ent:handlers.entrySet() ){
			allHandlers.put(ent.getValue().getName(), ent.getKey());
		}
		context.put("allHandlers",allHandlers);
		//2.����Request
		if ( !inputValue.isEmpty() ){
			String action = parameters.getString("cmd","");
			String handler = parameters.getString("handler");
			int type = parameters.getInt("type",0);
			int number = parameters.getInt("number",1);
			ProcDO oneProc = null;
			ProcRequest now = null;
			context.put("type", type);
			context.put("number", number);

			List<Request> ctxs = new ArrayList<Request>();
			List<ProcDO> procs = new ArrayList<ProcDO>();
			for ( int i=0;i<number;++i ){
				ProcDO procDO = new ProcDO();
				if ( inputValue.containsKey("T") ){
					procDO.setBookStatus(inputValue.get("T"));
				}
				if ( inputValue.containsKey("B") ){
					procDO.setStatus(inputValue.get("B"));
				}
				if ( inputValue.containsKey("P") ){
					procDO.setPayStatus(inputValue.get("P"));
				}
				//4.�����Trig�����ִ��Handler
				procDO.setType(type);
				ProcRequest ctx = new ProcRequest();
				ctxs.add(ctx);
				ctx.setDataModel(procDO);
				oneProc = procDO;
				now = ctx;
			}
			if ( action.equals("trig") ){
				ProcessResultDO rs = tframeEngine.trigger(ctxs.get(0), DemoHandlerName.valueOf(handler) );
				context.put("rs", rs);
				inputValue.put("T", oneProc.getBookStatus());
				inputValue.put("B", oneProc.getStatus());
				inputValue.put("P", oneProc.getPayStatus());
			}
			//3.��ȡ֧�ֵ�Handler�б�
			Collection<IHandlerName> supportHandlers = tframeEngine.getSupportHandlers(now);
			context.put("handlers",supportHandlers);
		
		}
		
		
	}


}
