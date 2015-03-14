package com.local.kevin.web.servlet;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;

import java.util.HashMap;
import java.util.Map;

public class kevinDispatcherServlet extends DispatcherServlet{

	private static final long serialVersionUID = -7838823159683487783L;
	public static ApplicationContext ctx;
	public static Map<String, Object> handlerMap = new HashMap<String,Object>();

	protected void initStrategies(ApplicationContext context) {
		super.initStrategies(context);
		ctx = context;
		Map<String, HandlerMapping> matchingBeans =
			BeanFactoryUtils.beansOfTypeIncludingAncestors(ctx, HandlerMapping.class, true, false);
		DefaultAnnotationHandlerMapping next = (DefaultAnnotationHandlerMapping) matchingBeans.values().iterator().next();
		handlerMap = next.getHandlerMap();
	}


}
