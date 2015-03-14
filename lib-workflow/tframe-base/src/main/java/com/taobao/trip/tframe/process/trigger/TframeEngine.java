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
 * 流程触发器，用来触发流程。
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
    	//step0.埋点变量
    	TripMonitorRT totalRT = TripMonitor.rt("Handler处理时间", handlerName.toString(),"");
    	TripMonitorFailRate totalFailRate = TripMonitor.failRate("Handler失败率", handlerName.toString(), "");
    	TripMonitorFailRate totalExceptionRate = TripMonitor.failRate("Handler异常率", handlerName.toString(),"");
    	long start = System.currentTimeMillis();
    	totalExceptionRate.incrTotal();
    	totalFailRate.incrTotal();
    	//step1.执行实际程序
    	try{
    		UniqRequest.getId();
			ctx.doInit();
			rs =  _executeWithSaveOnce(ctx, handlerName, type);
    		return (T)rs;
    	}catch(Throwable t){
    		log.error("执行Handler:"+handlerName+"异常:",t);
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
     * 判断当前数据是否支持此Handler
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
     * 获取当前Ctx支持的Handler
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
     * 一次性执行多个ContextModel。
     * 本函数和分别调用两次单个ContextModel的区别在于
     * 本函数的saveb执行一次，即两个事务合并为一个事务执行
     * @return
     */
    /*
    public ProcessResultDO exec(List<Request<?,?>> ctxs, IHandlerName handlerName) {
    	ProcessResultDO rs = newResult(type);
    	//step0.埋点变量
    	TripMonitorRT totalRT = TripMonitor.rt("Handler处理时间", handlerName.toString(),"");
    	TripMonitorFailRate totalFailRate = TripMonitor.failRate("Handler失败率", handlerName.toString(), "");
    	TripMonitorFailRate totalExceptionRate = TripMonitor.failRate("Handler异常率", handlerName.toString(),"");
    	long start = System.currentTimeMillis();
    	totalExceptionRate.incrTotal();
    	totalFailRate.incrTotal();
    	//step1.执行实际程序
    	try{
    		UniqRequest.getId();
    		for ( Request<?,?> ctx:ctxs ){
    			ctx.doInit();
    		}
    		rs =  _executeWithSaveOnce(ctxs, handlerName);
    		return rs;
    	}catch(Throwable t){
    		log.error("执行Handler:"+handlerName+"异常:",t);
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
    	//step0.寻找PM
    	TframeProcessManager pm = getProcessSelector().getProcessManager(ctx);
    	if ( pm == null ){
	    	ProcessResultDO rs = newResult(type);
            ProcessErrorEnum.PROCESS_NOT_EXIST.fillResult(rs);
            return (T)rs;
    	}
    	//step1.执行保存之前的业务逻辑
		//step1.1.处理业务逻辑
		ProcessResultDO ctxRs = _executeBeforeSave(ctx, handlerName, type);
		if ( ctxRs.isFailure() ){//任何一个Ctx执行失败，则返回失败
			return (T)ctxRs;
		}
		if ( ctxRs.isHandlerEndWithOutSave() ){
			return (T)ctxRs;
		}
		IHandler hl = pm.getHandler(handlerName);
        //step3.持久化进行保存
    	long tic = System.currentTimeMillis();
    	try{
	        //step3.1.执行保存方法
    		hl.save(ctx.getDataModel(),ctxRs);
    	}catch(Throwable t){
    		log.error("Save执行发生异常",t);
    		ProcessErrorEnum.SAVE2DB_IN_HANDLER_EXCEPTION.fillResult(ctxRs);
	    	TripMonitorQps totalSaveException = TripMonitor.qps("HandlerSave发生异常总次数", hl.getName().toString(),"");
	    	totalSaveException.record();
    		return (T)ctxRs;
    	}finally{
	        //埋点
	    	TripMonitorRT totalSaveRT = TripMonitor.rt("HandlerSave处理时间", hl.getName().toString(),"");
	        totalSaveRT.recordTimeUsed(System.currentTimeMillis()-tic);
    	}
    	return (T)ctxRs;
    }
    /*
	private ProcessResultDO _executeWithSaveOnce(List<Request<?,?>> ctxs, IHandlerName handlerName) throws ProcessException {
    	//step0.寻找PM
    	ProcessResultDO rs = newResult(type);
    	for ( Request<?,?> ctx:ctxs ){
        	TframeProcessManager pm = getProcessSelector().getProcessManager(ctx);
        	if ( pm == null ){
	            ProcessErrorEnum.PROCESS_NOT_EXIST.fillResult(rs);
	            return rs;
        	}
        	ctx.setCurrentPM(pm);
    	}
    	//step1.执行保存之前的业务逻辑
        Map<IHandler<?,?,?>,List<Request<?,?>>> needSaveCtxs = new HashMap<IHandler<?,?,?>,List<Request<?,?>>>();
        Map<IHandler<?,?,?>,List<ProcessResultDO>> needSaveResults = new HashMap<IHandler<?,?,?>,List<ProcessResultDO>>();
    	for ( Request<?,?> ctx:ctxs ){
    		//step1.1.处理业务逻辑
    		ProcessResultDO ctxRs = _executeBeforeSave(ctx, handlerName);
    		if ( ctxRs.isFailure() ){//任何一个Ctx执行失败，则返回失败
    			return ctxRs;
    		}
    		if ( ctxRs.isHandlerEndWithOutSave() ){
    			continue;
    		}
    		//step1.5.记录需要进行保存的数据，以Handler分组
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
    	//step2.如果需要保存的数据为空，则跳过
    	if ( needSaveCtxs.isEmpty() ){
            return rs;
        }
        //step3.持久化进行保存
        for ( IHandler<?,?,?> hl:needSaveCtxs.keySet() ){
        	long tic = System.currentTimeMillis();
        	try{
		        //step3.1.执行保存方法
        		hl.save(needSaveCtxs.get(hl),needSaveResults.get(hl));
        	}catch(Throwable t){
        		log.error("Save执行发生异常",t);
        		ProcessErrorEnum.SAVE2DB_IN_HANDLER_EXCEPTION.fillResult(rs);
		    	TripMonitorQps totalSaveException = TripMonitor.qps("Handler", hl.getName().toString(), "Save发生异常总次数");
		    	totalSaveException.record();
        		return rs;
        	}finally{
		        //埋点
		    	TripMonitorRT totalSaveRT = TripMonitor.rt("Handler", hl.getName().toString(), "Save处理总时间(ms)/总次数");
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
        // step1.判断是否支持该handler
        // 三种结果：1.支持;2.不支持，因为已处理过;3.不支持
        // 后两种情况本次处理失败
        ProcessResultDO supportRs = _isSupport(req, handlerName);
        if ( supportRs.isFailure() ){
        	rs.setErrCode(supportRs.getErrCode());
        	rs.setErrMsg(supportRs.getErrMsg());
        	return (T)rs;
        }
        //step2.获取Handler实例
        IHandler<?,?,?> handler = pm.getHandler(handlerName);
        //step3.执行动作
        rs = handler.execute(req);
        if (rs == null) {//step3.1.返回空指针
            log.error(handlerName + " execute Failed(NULL)!,tracerId:" + req.getUniqId());
            ProcessResultDO retRs = newResult(type);
            ProcessErrorEnum.HANDLER_EXE_NULL.fillResult(retRs);
            return (T)retRs;
        }
        if (rs.isHandlerEndWithOutSave() ){//step3.2.不用保存
            log.info(handlerName + " execute Success without save!,tracerId:" + req.getUniqId());
            return (T)rs;
        }
        if (!rs.isSuccess()) {//step3.4.如果执行失败，直接返回
            log.error(handlerName + " execute Failed!,tracerId:" + req.getUniqId());
            return (T)rs;
        }
        //step4.驱动状态机转变状态
        for (ProcessManager statusPm: pm.getManagers()){
        	IStatusType st = statusPm.getStatusType();
        	IStatusContext context = req.getContext(st);
            if (context == null) {
                continue;
            }
            //step4.1.根据当前节点状态获取当前节点
            //上面判断保证了currentNode一定非null
            Node currentNode = statusPm.getNode(context.getCurrentStatus());
            //step4.2.获取当前节点的下一个节点
            Node nextNode = currentNode.getNextNode(handlerName);
            //step4.3.只有当真实发生迁移才会更新状态；【比较内存地址即可】
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
