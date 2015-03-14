package com.taobao.trip.tframe.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.citrus.service.requestcontext.parser.ParameterParser;
import com.alibaba.citrus.service.uribroker.URIBrokerService;
import com.alibaba.citrus.turbine.TurbineRunData;

public class WebBase {

    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;
    @Autowired
    protected HttpSession session;
	@Autowired
	URIBrokerService uriBrokerService;    

    static public ApplicationContext getCtx(HttpSession session) {
        WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
        return ctx;
    }

    protected Map<String, String> getParameterMap(TurbineRunData rundata) {
        ParameterParser paramsParser = rundata.getParameters();
        final Map<String, String> paraMap = new HashMap<String, String>();
        Iterator<String> itr = paramsParser.keySet().iterator();
        while (itr.hasNext()) {
            String param = itr.next();
            paraMap.put(param, paramsParser.getString(param));
        }
        return paraMap;
    }

    protected void responseDirect(String msg) throws IOException {
        PrintWriter out = response.getWriter();
        out.println(msg);
        out.flush();
    }


}
