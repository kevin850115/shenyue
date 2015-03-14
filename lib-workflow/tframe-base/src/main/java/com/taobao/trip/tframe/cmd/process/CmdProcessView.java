package com.taobao.trip.tframe.cmd.process;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.taobao.trip.tframe.cmd.AbstractCmd;
import com.taobao.trip.tframe.process.IStatusType;
import com.taobao.trip.tframe.process.IStatusValue;
import com.taobao.trip.tframe.process.ProcessManager;
import com.taobao.trip.tframe.process.TframeProcessManager;
import com.taobao.trip.tframe.system.TframeSystem;

public class CmdProcessView extends AbstractCmd{

	/**
	 * {
	 * 	"XX.XXX.XXXMNager":{
	 * 		"type":"交易状态",
	 * 		"class":"xxxxxxx.StatusType",
	 * 		"values":{"5":"已支付","6":"成功"}
	 * 		"graph":"...."
	 * 		},
	 * 	"YYY.YYY.YYY":{
	 *       ...........
	 *       }
	 * }
	 * 			
	 */
	@Override
	public String doCmd(Map<String, Object> paras) {
		JSONObject obj = new JSONObject();
		Map<String, TframeProcessManager> pms = TframeSystem.processdefs;
		String process = (String) paras.get("process");
		if ( process == null ){
			return error("process参数为空");
		}
		TframeProcessManager pm = pms.get(process);
		if ( pm == null ){
			return error("process不存在");
		}
		Set<ProcessManager> managers = pm.getManagers();
		for ( ProcessManager manager:managers ){
			JSONObject mObj = new JSONObject();
			IStatusType st = manager.getStatusType();
			mObj.put("type", st.getDesc());
			mObj.put("class", st.getClass().getName());
			mObj.put("manager", manager.getClass().getName());
			JSONObject vObj = new JSONObject();
			for ( IStatusValue sv:st.getValues() ){
				vObj.put(sv.getStatusValue().toString(),sv.getStatusDesc());
			}
			mObj.put("values", vObj);
			mObj.put("graph", manager.toGraph());
			obj.put(st.getDesc(), mObj);
		}
		return obj.toJSONString();
	}

}
