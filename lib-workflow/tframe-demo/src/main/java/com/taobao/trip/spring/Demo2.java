package com.taobao.trip.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

public class Demo2 implements FactoryBean {
	
	@Autowired
	private Demo demo;

	@Override
	public Object getObject() throws Exception {
		return null;
	}

	@Override
	public Class getObjectType() {
		// TODO Auto-generated method stub
		return Demo.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setDemo(Demo demo) {
		this.demo = demo;
	}

	public Demo getDemo() {
		return demo;
	}

}
