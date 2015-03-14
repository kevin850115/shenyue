package com.taobao.trip.tframe.home.module.screen;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.taobao.trip.tframe.common.WebBase;
import com.taobao.trip.tframe.demo.ao.DemoAO;
import com.taobao.trip.tframe.demo.ao.DemoCreateParam;

/**
 * ʱ����ӿڣ�ĳ����ĳʱ�䷶Χ��Ʊ�����ͼ�����
 * 
 */
public class Main extends WebBase {
	
	@Autowired
	DemoAO demoAO;
	

	public void execute(Context context) {
		DemoCreateParam param = new DemoCreateParam();
		param.setBuyer("�س�");
		param.setSeller("����");
		context.put("cb",demoAO.createOrder(param));

	}


}
