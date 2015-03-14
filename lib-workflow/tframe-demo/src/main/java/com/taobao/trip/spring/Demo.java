package com.taobao.trip.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class Demo implements BeanFactoryAware,BeanNameAware,ApplicationContextAware{

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println("XX:"+beanFactory);
		
	}

	@Override
	public void setBeanName(String name) {
		System.out.println(name);
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		System.out.println("ac:"+applicationContext);

	}

	

}
