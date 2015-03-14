package com.taobao.trip.tframe.home.module.screen;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.taobao.trip.tframe.common.WebBase;
import com.taobao.trip.tframe.demo.ao.DemoAO;
import com.taobao.trip.tframe.demo.ao.DemoCreateParam;

/**
 * 时光机接口，某航段某时间范围机票订单低价曲线
 * 
 */
public class Main extends WebBase {
	
	@Autowired
	DemoAO demoAO;
	

	public void execute(Context context) {
		DemoCreateParam param = new DemoCreateParam();
		param.setBuyer("秦冲");
		param.setSeller("李震");
		context.put("cb",demoAO.createOrder(param));

	}


}
