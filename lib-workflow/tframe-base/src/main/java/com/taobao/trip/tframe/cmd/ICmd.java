package com.taobao.trip.tframe.cmd;

import java.util.Map;

public interface ICmd {
	static String ERROR="{\"error\":\"����ʧ��:%s\"}";
	String doCmd(Map<String,Object> paras);
}
