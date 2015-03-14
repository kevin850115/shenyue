package com.taobao.trip.tframe.common;

import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class GroovyBase {

    private static ApplicationContext ctx;

    static public void setCtx(HttpSession session) {
         ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
    }
    static public ApplicationContext getCtx(){
    	return ctx;
    }
    


}
