package com.taobao.trip.tframe.cmd;

import java.util.Map;

public interface ICmd {
	static String ERROR="{\"error\":\"²Ù×÷Ê§°Ü:%s\"}";
	String doCmd(Map<String,Object> paras);
}
