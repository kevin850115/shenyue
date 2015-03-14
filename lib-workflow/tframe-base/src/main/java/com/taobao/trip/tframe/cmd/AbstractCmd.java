package com.taobao.trip.tframe.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCmd implements ICmd {
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	protected String error(String msg){
		return String.format(ERROR, msg);
	}

}
