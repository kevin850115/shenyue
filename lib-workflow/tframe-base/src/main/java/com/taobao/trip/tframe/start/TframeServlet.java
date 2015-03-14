package com.taobao.trip.tframe.start;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.taobao.trip.tframe.cmd.CmdReg;
import com.taobao.trip.tframe.cmd.ICmd;
import com.taobao.trip.tframe.config.TframeSystemConfig;
import com.taobao.trip.tframe.handler.TframeAbstractHandler;
import com.taobao.trip.tframe.process.TframeProcessManager;
import com.taobao.trip.tframe.process.selector.IProcessSelector;
import com.taobao.trip.tframe.system.TframeSystem;

/**
 * Tframe�������
 * @author leconte
 *
 */
public final class TframeServlet extends HttpServlet{
	private Logger log = LoggerFactory.getLogger(TframeServlet.class);
	static TframeSystem system;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("application/json; charset=utf-8");
		PrintWriter out = resp.getWriter();
		String cmd = req.getParameter("cmd");
		String action = req.getParameter("action");
		ICmd icmd = CmdReg.getCmd(cmd, action);
		if (icmd == null || cmd == null || action == null ){
			out.println("CmdNotFound!"+cmd+":"+action);
			return;
		}
		Map<String,Object> paras = new HashMap<String,Object>();
		paras.put("_sc_", getServletContext());
		Enumeration parameterNames = req.getParameterNames();
		while ( parameterNames.hasMoreElements() ){
			String k = (String) parameterNames.nextElement();
			paras.put(k,req.getParameter(k));
		}
		String output = icmd.doCmd(paras);
		log.error(output);
		out.println(output);
	}

	private static final long serialVersionUID = 8746786590084882996L;
	
	public void init(){
		log.info("TframeServlet��ʼ��ʼ��");
		try {
			//1.��spring������Ѱ������Tframe��ص�bean�����浽TframeSystemData
			long tic = System.currentTimeMillis();
			findTframeBeans();
			log.info("Ѱ������bean��ʱ:"+(System.currentTimeMillis()-tic)+"ms");
		} catch (Exception e) {
			log.error("TframeServlet��ʼ��ʧ��",e);
			throw new RuntimeException(e);
		}finally{
			log.info("TframeServlet��ʼ������");
		}
	}
	private void findTframeBeans() {
		ServletContext sc = getServletContext();
		WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
		String[] beanNames = applicationContext.getBeanDefinitionNames();
		for (String beanName : beanNames)  {
			try  {
				Object bean = applicationContext.getBean(beanName);
				if(bean!=null) {
					log.debug("�ҵ�bean:"+beanName+",class:"+bean.getClass().getName());
				}
				collectUsefullBean(beanName,bean);
			}catch (Exception e){
				log.error("��ȡbean: "+beanName+"ʱ�����쳣",e);
			}
		}
		log.info(TframeSystem.getSystemSummary());
	}
	private void collectUsefullBean(String name,Object bean) {
		if ( bean instanceof TframeAbstractHandler){
			TframeSystem.handlers.put(name, (TframeAbstractHandler) bean);
		}else if ( bean instanceof TframeProcessManager){
			TframeSystem.processdefs.put(name, (TframeProcessManager) bean);
		}else if ( bean instanceof IProcessSelector){
			TframeSystem.selector = (IProcessSelector) bean;
		}else if ( bean instanceof TframeSystemConfig ){
			TframeSystem.systemConfig = (TframeSystemConfig) bean;
		}else if ( bean instanceof TframeSystem ){
			system = (TframeSystem) bean;
		}
	}
}
