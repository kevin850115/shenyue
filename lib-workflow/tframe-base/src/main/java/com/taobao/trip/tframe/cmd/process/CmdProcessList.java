package com.taobao.trip.tframe.cmd.process;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.taobao.trip.tframe.cmd.AbstractCmd;
import com.taobao.trip.tframe.process.TframeProcessManager;
import com.taobao.trip.tframe.system.TframeSystem;

public class CmdProcessList extends AbstractCmd{

	@Override
	public String doCmd(Map<String, Object> paras) {
		JSONObject obj = new JSONObject();
		Map<String, TframeProcessManager> pms = TframeSystem.processdefs;
		for (Map.Entry<String, TframeProcessManager> ent:pms.entrySet() ){
			JSONObject pmObj = new JSONObject();
			TframeProcessManager pm = ent.getValue();
			pmObj.put("class", pm.getClass().getName());
			pmObj.put("desc",pm.getProcessDesc());
			pmObj.put("statusTypes",pm.getManagers().size());
			obj.put(ent.getKey(), pmObj);
		}
		return obj.toJSONString();
	}

}
