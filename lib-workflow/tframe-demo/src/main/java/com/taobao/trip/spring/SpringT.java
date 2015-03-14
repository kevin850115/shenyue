package com.taobao.trip.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringT {

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:ac.xml");
		System.out.println(ctx.toString());
		//System.out.println(JSON.toJSONString(ctx,true));
		Object bean = ctx.getBean("demo2");
		System.out.println(bean.getClass());
	}
}
