package com.taobao.trip.qinchong;

import java.util.concurrent.TimeUnit;

public class DemoCImpl implements DemoC {

	@Override
	public String getA(Integer sec) {
		if ( sec == null ){
			return "[null]";
		}
		try {
			TimeUnit.SECONDS.sleep(sec);
		} catch (InterruptedException e) {
			return "[Interrupt];";
		}
		return String.valueOf(sec);
	}

}
