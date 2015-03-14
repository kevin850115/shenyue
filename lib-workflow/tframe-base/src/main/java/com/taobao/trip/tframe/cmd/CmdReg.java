package com.taobao.trip.tframe.cmd;

import java.util.HashMap;
import java.util.Map;

import com.taobao.trip.tframe.cmd.process.CmdProcessList;
import com.taobao.trip.tframe.cmd.process.CmdProcessView;
import com.taobao.trip.tframe.cmd.system.CmdSystemInfo;

public class CmdReg {
	private static Map<String,Map<String,ICmd>> cmds = new HashMap<String,Map<String,ICmd>>();
	static {
		//system
		{
			Map<String, ICmd> ct = newCmdType("system");
			ct.put("info",new CmdSystemInfo());
		}
		{
			Map<String, ICmd> ct = newCmdType("process");
			ct.put("list",new CmdProcessList());
			ct.put("view",new CmdProcessView());
		}
	}
	private static Map<String,ICmd> newCmdType(String type){
		Map<String, ICmd> mm = new HashMap<String,ICmd>();
		cmds.put(type, mm );
		return mm;
	}
	public static ICmd getCmd(String type,String action){
		Map<String, ICmd> mm = cmds.get(type);
		if ( mm == null ){
			return null;
		}
		return mm.get(action);
	}
}
