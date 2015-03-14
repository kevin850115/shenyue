package com.taobao.trip.tframe.cmd.system;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.taobao.trip.tframe.cmd.AbstractCmd;
import com.taobao.trip.tframe.system.TframeSystem;

public class CmdSystemInfo extends AbstractCmd{

	@Override
	public String doCmd(Map<String, Object> paras) {
		JSONObject obj = new JSONObject();
		obj.put("handlers", TframeSystem.handlers.size());
		obj.put("processdefs", TframeSystem.processdefs.size());
		obj.put("selector", TframeSystem.selector.getClass().getName());
		obj.put("systemConfig", TframeSystem.systemConfig);
		return obj.toJSONString();
	}

}
