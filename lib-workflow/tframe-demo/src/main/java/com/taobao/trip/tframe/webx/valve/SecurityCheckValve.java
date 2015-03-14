package com.taobao.trip.tframe.webx.valve;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.service.pipeline.PipelineContext;
import com.alibaba.citrus.service.pipeline.support.AbstractValve;
import com.alibaba.citrus.service.uribroker.URIBrokerService;
import com.alibaba.common.convert.Convert;

/**
 * ?????????????????????????????????
 * 
 */
public class
        SecurityCheckValve extends AbstractValve {

    private static Logger log = LoggerFactory.getLogger(SecurityCheckValve.class);

    private static final String REDIRECT_URL = "redirectURL";
    private static final String TAOBAO_LOGIN_URL = "taobaoLoginURL";
    private static final String LOGIN = "login";
    /**
     * ????????? URL ?????Set<String>
     */

    private Set<String> unProtectedURLs = new HashSet<String>();
    private Set<String> returnJsonURLs = new HashSet<String>();
    
    

    public Set<String> getReturnJsonURLs() {
        return returnJsonURLs;
    }

    public void setReturnJsonURLs(Set<String> returnJsonURLs) {
        this.returnJsonURLs = returnJsonURLs;
    }

    public boolean isURLUnProtected(String target) {
        // URL set ????
        for ( String url:unProtectedURLs ){
            if ( url.endsWith(target) ){
                return true;
            }
        }
        return false;

    }
    
    public boolean isReturnJsonURLs(String target) {
        boolean flag = returnJsonURLs.contains(target);
        return flag;
    }

    @Autowired
    private HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;
    @Autowired
    private URIBrokerService uriBrokerService;

    private boolean isLogin() {
        HttpSession session = request.getSession();
        return Convert.asBoolean(session.getAttribute(LOGIN), false);
    }

    private boolean isTargetProtected(final String target) {
        return !isURLUnProtected(target);
    }
    
    public String getFullURL() {
        StringBuffer url = request.getRequestURL();
        if (request.getQueryString() != null) {
            url.append('?');
            url.append(request.getQueryString());
        }
        return url.toString();
    }

    public void invoke(PipelineContext pipelineContext) throws Exception {
        /*TODO
        TurbineRunData rundata = getTurbineRunData(request);
        if (isTargetProtected(rundata.getTarget())) {// ????????
            if (isLogin()) {
                HttpSession session = request.getSession();
                if (!SessionAuthenticator.hasLoggedIn(session)) { // ?????????????config session
                    configSession(session);
                }
            } else { // (¦Ä?????????)
                // ??????URL(??query????)
                final String redirectURL = getFullURL();
                // ???????????? , ??????????redirect ?? target URL
                URIBroker loginUrl = uriBrokerService.getURIBroker(TAOBAO_LOGIN_URL);
                if (isReturnJsonURLs(rundata.getTarget())) {
                    Map<String, String> data = new HashMap<String, String>();
                    data.put("loginURL", loginUrl.render()+"?redirectURL=");
                    String json = toJson(AtoConstant.IS_FAIL_SESSIONERROR, data, AtOrderErrorEnum.BUYER_NOT_EXIST
                            .getErrorMsgForClient());
                    this.sendResultInfo(json);
                    return;
                } else {
                    String target = loginUrl.addQueryData(REDIRECT_URL, redirectURL).render();
                    rundata.setRedirectLocation(target);
                }
            }
        }
        */
        pipelineContext.invokeNext();
    }
    
    private void sendResultInfo(String message) {
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.write(message);
            pw.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            pw.close();
        }
    }

    public Set<String> getUnProtectedURLs() {
        return unProtectedURLs;
    }

    public void setUnProtectedURLs(Set<String> unProtectedURLs) {
        this.unProtectedURLs = unProtectedURLs;
    }
    
}
