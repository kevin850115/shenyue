package com.taobao.trip.tframe.spring;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.trip.tframe.config.TframeSystemConfig;

public class TestAnnoBean extends TestSpring {
	@Autowired
	TframeSystemConfig tframeSystemConfig;

	@Test
	public void ok(){
		System.out.println(tframeSystemConfig.getAppName());
	}
}
