package com.taobao.trip.tframe.process.trigger;

import java.util.Collection;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.ateye.monitor.TripMonitor;
import com.taobao.ateye.monitor.TripMonitorFailRate;
import com.taobao.ateye.monitor.TripMonitorQps;
import com.taobao.ateye.monitor.TripMonitorRT;
import com.taobao.trip.tframe.err.ProcessErrorEnum;
import com.taobao.trip.tframe.monitor.UniqRequest;
import com.taobao.trip.tframe.process.ContextException;
import com.taobao.trip.tframe.process.IHandler;
import com.taobao.trip.tframe.process.IHandlerName;
import com.taobao.trip.tframe.process.IStatusContext;
import com.taobao.trip.tframe.process.IStatusType;
import com.taobao.trip.tframe.process.Node;
import com.taobao.trip.tframe.process.ProcessException;
import com.taobao.trip.tframe.process.ProcessManager;
import com.taobao.trip.tframe.process.ProcessResultDO;
import com.taobao.trip.tframe.process.Request;
import com.taobao.trip.tframe.process.TframeProcessManager;
import com.taobao.trip.tframe.process.selector.IProcessSelector;

/**
 * ���̴������������������̡�
 * @author leconte
 *
 */
@Component
public class TframeEngine{
	private IProcessSelector processSelector;

	protected Logger log = LoggerFactory.getLogger(this.getClass());

    public ProcessResultDO trigger(Request<?,?> ctx, IHandlerName handlerName) {
    	return trigger(ctx,handlerName,ProcessResultDO.class);
    }
    public <T extends ProcessResultDO> T  trigger(Request<?,?> ctx, IHandlerName handlerName, Class<T> type) {
    	ProcessResultDO rs = newResult(type);
    	//step0.������
    	TripMonitorRT totalRT = TripMonitor.rt("Handler����ʱ��", handlerName.toString(),"");
    	TripMonitorFailRate totalFailRate = TripMonitor.failRate("Handlerʧ����", handlerName.toString(), "");
    	TripMonitorFailRate totalExceptionRate = TripMonitor.failRate("Handler�쳣��", handlerName.toString(),"");
    	long start = System.currentTimeMillis();
    	totalExceptionRate.incrTotal();
    	totalFailRate.incrTotal();
    	//step1.ִ��ʵ�ʳ���
    	try{
    		UniqRequest.getId();
			ctx.doInit();
			rs =  _executeWithSaveOnce(ctx, handlerName, type);
    		return (T)rs;
    	}catch(Throwable t){
    		log.error("ִ��Handler:"+handlerName+"�쳣:",t);
    		totalExceptionRate.incrFail();
    		if ( t instanceof ContextException ){
    			ProcessErrorEnum.CONTEXT_EXCEPTION.fillResult(rs);
    		}else if ( t instanceof ProcessException ){
    			ProcessErrorEnum.PROCESS_EXCEPTION.fillResult(rs);
    		}else{
	    		ProcessErrorEnum.UNKNOWN_EXCEPTION.fillResult(rs);
    		}
    		return (T)rs;
    	}finally{
    		totalRT.record(start);
    		log.info(buildLog(ctx,handlerName,rs));
    		UniqRequest.clearId();
    	}
    }
    /**
     * �жϵ�ǰ�����Ƿ�֧�ִ�Handler
     * @return
     */
    public ProcessResultDO isSupport(Request<?,?> ctx, IHandlerName handlerName){
    	ProcessResultDO rs = new ProcessResultDO();
    	TframeProcessManager pm = getProcessSelector().getProcessManager(ctx);
    	if ( pm == null ){
    		ProcessErrorEnum.PROCESS_NOT_EXIST.fillResult(rs);
    		return rs;
    	}
    	try{
    		ctx.doInit();
    		return _isSupport(ctx, handlerName);
    	}catch(ContextException e){
        	ProcessErrorEnum.CONTEXT_EXCEPTION.fillResult(rs);
        	return rs;
    	} catch(ProcessException e){
        	ProcessErrorEnum.PROCESS_EXCEPTION.fillResult(rs);
        	return rs;
    	}
    }
    private ProcessResultDO newResult(Class<? extends ProcessResultDO> classType){
    	ProcessResultDO ret = null;
		try {
			ret = classType.newInstance();		
		} catch (Exception ex) {
			throw new RuntimeException("executeProcess newInstance result error", ex.getCause());
		}
		return ret;
    }
		
    /**
     * ��ȡ��ǰCtx֧�ֵ�Handler
     * @return
     */
    public Collection<IHandlerName> getSupportHandlers(Request<?,?> ctx){
    	Collection<IHandlerName> ret = new HashSet<IHandlerName>();
    	TframeProcessManager pm = getProcessSelector().getProcessManager(ctx);
    	if ( pm == null ){
    		return ret;
    	}
		ctx.doInit();
		

		Collection<IHandlerName> supportedHandlers = pm.getSupportedHandlers(ctx);
		return supportedHandlers;
    }
    /**
     * һ����ִ�ж��ContextModel��
     * �������ͷֱ�������ε���ContextModel����������
     * ��������savebִ��һ�Σ�����������ϲ�Ϊһ������ִ��
     * @return
     */
    /*
    public ProcessResultDO exec(List<Request<?,?>> ctxs, IHandlerName handlerName) {
    	ProcessResultDO rs = newResult(type);
    	//step0.������
    	TripMonitorRT totalRT = TripMonitor.rt("Handler����ʱ��", handlerName.toString(),"");
    	TripMonitorFailRate totalFailRate = TripMonitor.failRate("Handlerʧ����", handlerName.toString(), "");
    	TripMonitorFailRate totalExceptionRate = TripMonitor.failRate("Handler�쳣��", handlerName.toString(),"");
    	long start = System.currentTimeMillis();
    	totalExceptionRate.incrTotal();
    	totalFailRate.incrTotal();
    	//step1.ִ��ʵ�ʳ���
    	try{
    		UniqRequest.getId();
    		for ( Request<?,?> ctx:ctxs ){
    			ctx.doInit();
    		}
    		rs =  _executeWithSaveOnce(ctxs, handlerName);
    		return rs;
    	}catch(Throwable t){
    		log.error("ִ��Handler:"+handlerName+"�쳣:",t);
    		totalExceptionRate.incrFail();
    		if ( t instanceof ContextException ){
    			ProcessErrorEnum.CONTEXT_EXCEPTION.fillResult(rs);
    		}else if ( t instanceof ProcessException ){
    			ProcessErrorEnum.PROCESS_EXCEPTION.fillResult(rs);
    		}else{
	    		ProcessErrorEnum.UNKNOWN_EXCEPTION.fillResult(rs);
    		}
    		return rs;
    	}finally{
    		totalRT.record(start);
    		for ( Request<?,?> ctx:ctxs){
	    		log.info(buildLog(ctx,handlerName,rs));
    		}
    		UniqRequest.clearId();
    	}
    }
    */
    private String buildLog(Request<?,?> ctx, IHandlerName handlerName,ProcessResultDO rs) {
    	StringBuilder sb = new StringBuilder();
    	sb.append(ctx.getUniqId()==null?"[Null]":ctx.getUniqId()).append("|");
    	sb.append(handlerName).append("|");
    	if ( rs.isHandlerEndWithOutSave() ){
    		sb.append("[NoSave]");
    	}else if ( rs.isSuccess() ){
    		sb.append("[Succ]");
    	}else if ( rs.isFailure() ){
    		sb.append("["+rs+"]");
    	}else {
    		sb.append("[N/A]");
    	}
    	sb.append("|");
    	for ( IStatusType key :ctx.getStatusTypes() ){
    		IStatusContext value = ctx.getContext(key);
    		if ( value.isChanged() ){
    			sb.append(key).append(":").append(value.getOldStatus()).append("->").append(value.getCurrentStatus()).append(",");
    		}else{
    			sb.append(key).append(":").append(value.getCurrentStatus()).append(",");
    		}
    	}
    	sb.append("|");
    	if ( getProcessSelector().getProcessManager(ctx) != null ){
    		sb.append(getProcessSelector().getProcessManager(ctx).getClass().getSimpleName());
	    	sb.append("|");
    	}
    	return sb.toString();
	}
	@SuppressWarnings("unchecked")
	private <T extends ProcessResultDO> T _executeWithSaveOnce(Request<?,?> ctx, IHandlerName handlerName, Class<T> type) throws ProcessException {
    	//step0.Ѱ��PM
    	TframeProcessManager pm = getProcessSelector().getProcessManager(ctx);
    	if ( pm == null ){
	    	ProcessResultDO rs = newResult(type);
            ProcessErrorEnum.PROCESS_NOT_EXIST.fillResult(rs);
            return (T)rs;
    	}
    	//step1.ִ�б���֮ǰ��ҵ���߼�
		//step1.1.����ҵ���߼�
		ProcessResultDO ctxRs = _executeBeforeSave(ctx, handlerName, type);
		if ( ctxRs.isFailure() ){//�κ�һ��Ctxִ��ʧ�ܣ��򷵻�ʧ��
			return (T)ctxRs;
		}
		if ( ctxRs.isHandlerEndWithOutSave() ){
			return (T)ctxRs;
		}
		IHandler hl = pm.getHandler(handlerName);
        //step3.�־û����б���
    	long tic = System.currentTimeMillis();
    	try{
	        //step3.1.ִ�б��淽��
    		hl.save(ctx.getDataModel(),ctxRs);
    	}catch(Throwable t){
    		log.error("Saveִ�з����쳣",t);
    		ProcessErrorEnum.SAVE2DB_IN_HANDLER_EXCEPTION.fillResult(ctxRs);
	    	TripMonitorQps totalSaveException = TripMonitor.qps("HandlerSave�����쳣�ܴ���", hl.getName().toString(),"");
	    	totalSaveException.record();
    		return (T)ctxRs;
    	}finally{
	        //���
	    	TripMonitorRT totalSaveRT = TripMonitor.rt("HandlerSave����ʱ��", hl.getName().toString(),"");
	        totalSaveRT.recordTimeUsed(System.currentTimeMillis()-tic);
    	}
    	return (T)ctxRs;
    }
    /*
	private ProcessResultDO _executeWithSaveOnce(List<Request<?,?>> ctxs, IHandlerName handlerName) throws ProcessException {
    	//step0.Ѱ��PM
    	ProcessResultDO rs = newResult(type);
    	for ( Request<?,?> ctx:ctxs ){
        	TframeProcessManager pm = getProcessSelector().getProcessManager(ctx);
        	if ( pm == null ){
	            ProcessErrorEnum.PROCESS_NOT_EXIST.fillResult(rs);
	            return rs;
        	}
        	ctx.setCurrentPM(pm);
    	}
    	//step1.ִ�б���֮ǰ��ҵ���߼�
        Map<IHandler<?,?,?>,List<Request<?,?>>> needSaveCtxs = new HashMap<IHandler<?,?,?>,List<Request<?,?>>>();
        Map<IHandler<?,?,?>,List<ProcessResultDO>> needSaveResults = new HashMap<IHandler<?,?,?>,List<ProcessResultDO>>();
    	for ( Request<?,?> ctx:ctxs ){
    		//step1.1.����ҵ���߼�
    		ProcessResultDO ctxRs = _executeBeforeSave(ctx, handlerName);
    		if ( ctxRs.isFailure() ){//�κ�һ��Ctxִ��ʧ�ܣ��򷵻�ʧ��
    			return ctxRs;
    		}
    		if ( ctxRs.isHandlerEndWithOutSave() ){
    			continue;
    		}
    		//step1.5.��¼��Ҫ���б�������ݣ���Handler����
    		if ( !needSaveCtxs.containsKey(ctx.getCurrentHandler()) ){
	            needSaveCtxs.put(ctx.getCurrentHandler(), new ArrayList<Request<?,?>>());
	        }
    		if ( !needSaveResults.containsKey(ctx.getCurrentHandler()) ){
    			needSaveResults.put(ctx.getCurrentHandler(), new ArrayList<ProcessResultDO>());
	        }
            needSaveCtxs.get(ctx.getCurrentHandler()).add(ctx);
            List<ProcessResultDO> list = needSaveResults.get(ctx.getCurrentHandler());
            list.add(ctxRs);
    	}
    	//step2.�����Ҫ���������Ϊ�գ�������
    	if ( needSaveCtxs.isEmpty() ){
            return rs;
        }
        //step3.�־û����б���
        for ( IHandler<?,?,?> hl:needSaveCtxs.keySet() ){
        	long tic = System.currentTimeMillis();
        	try{
		        //step3.1.ִ�б��淽��
        		hl.save(needSaveCtxs.get(hl),needSaveResults.get(hl));
        	}catch(Throwable t){
        		log.error("Saveִ�з����쳣",t);
        		ProcessErrorEnum.SAVE2DB_IN_HANDLER_EXCEPTION.fillResult(rs);
		    	TripMonitorQps totalSaveException = TripMonitor.qps("Handler", hl.getName().toString(), "Save�����쳣�ܴ���");
		    	totalSaveException.record();
        		return rs;
        	}finally{
		        //���
		    	TripMonitorRT totalSaveRT = TripMonitor.rt("Handler", hl.getName().toString(), "Save������ʱ��(ms)/�ܴ���");
		        totalSaveRT.recordTimeUsed(System.currentTimeMillis()-tic);
        	}
        }
    	return rs;
    }
	*/

	@SuppressWarnings("unchecked")
	private <T extends ProcessResultDO> T _executeBeforeSave(Request req,IHandlerName handlerName,Class<T> type) throws ProcessException {
		TframeProcessManager pm = getProcessSelector().getProcessManager(req);
		ProcessResultDO rs = newResult(type);
        // step1.�ж��Ƿ�֧�ָ�handler
        // ���ֽ����1.֧��;2.��֧�֣���Ϊ�Ѵ����;3.��֧��
        // ������������δ���ʧ��
        ProcessResultDO supportRs = _isSupport(req, handlerName);
        if ( supportRs.isFailure() ){
        	rs.setErrCode(supportRs.getErrCode());
        	rs.setErrMsg(supportRs.getErrMsg());
        	return (T)rs;
        }
        //step2.��ȡHandlerʵ��
        IHandler<?,?,?> handler = pm.getHandler(handlerName);
        //step3.ִ�ж���
        rs = handler.execute(req);
        if (rs == null) {//step3.1.���ؿ�ָ��
            log.error(handlerName + " execute Failed(NULL)!,tracerId:" + req.getUniqId());
            ProcessResultDO retRs = newResult(type);
            ProcessErrorEnum.HANDLER_EXE_NULL.fillResult(retRs);
            return (T)retRs;
        }
        if (rs.isHandlerEndWithOutSave() ){//step3.2.���ñ���
            log.info(handlerName + " execute Success without save!,tracerId:" + req.getUniqId());
            return (T)rs;
        }
        if (!rs.isSuccess()) {//step3.4.���ִ��ʧ�ܣ�ֱ�ӷ���
            log.error(handlerName + " execute Failed!,tracerId:" + req.getUniqId());
            return (T)rs;
        }
        //step4.����״̬��ת��״̬
        for (ProcessManager statusPm: pm.getManagers()){
        	IStatusType st = statusPm.getStatusType();
        	IStatusContext context = req.getContext(st);
            if (context == null) {
                continue;
            }
            //step4.1.���ݵ�ǰ�ڵ�״̬��ȡ��ǰ�ڵ�
            //�����жϱ�֤��currentNodeһ����null
            Node currentNode = statusPm.getNode(context.getCurrentStatus());
            //step4.2.��ȡ��ǰ�ڵ����һ���ڵ�
            Node nextNode = currentNode.getNextNode(handlerName);
            //step4.3.ֻ�е���ʵ����Ǩ�ƲŻ����״̬�����Ƚ��ڴ��ַ���ɡ�
            if (nextNode != currentNode) {
            	context.changeStatusTo(nextNode.getSv());
            }
        }
        return (T)rs;
    }
	private ProcessResultDO _isSupport(Request<?,?> now, IHandlerName handlerName) throws ProcessException {
		TframeProcessManager pm = getProcessSelector().getProcessManager(now);
        ProcessResultDO rs = new ProcessResultDO();
		if ( !pm.isSupport(now,handlerName) ){
            int handlerStatus = pm.isAlreadyHandled(now, handlerName);
	        if ( handlerStatus == TframeProcessManager.HANDLER_HANDLED){
	            ProcessErrorEnum.ALREADY_HANDLED.fillResult(rs);
	            return rs;
	        }else{
	        	ProcessErrorEnum.HANDLER_NOT_SUPPORT.fillResult(rs);
	            return rs;
	        }
		}
        return rs;
	}
	public void setProcessSelector(IProcessSelector processSelector) {
		this.processSelector = processSelector;
	}
	public IProcessSelector getProcessSelector() {
		return processSelector;
	}
	
}
