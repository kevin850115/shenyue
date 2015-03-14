package com.taobao.trip.tframe.process;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;


/*
 * 多个状态的流程描述，包含状态机的定义
 * 
 * 内部是对ProcesManager的包装，显著特点是可以支持多状态节点迁移管理
 */
public abstract class TframeProcessManager {
	
    protected static Logger log = LoggerFactory.getLogger(TframeProcessManager.class);
    /* 多个Node管理 */
    private Set<ProcessManager> managers = new HashSet<ProcessManager>();
    /* Action名称到Action的映射:包含所有的Action */
    private Map<IHandlerName, IHandler<?,?,?>> actionNameMap = new HashMap<IHandlerName, IHandler<?,?,?>>();
    
    /** 临时状态保存，用以记录状态注册(lazy-register**/
    /** <Handler,<MgrName,<From,To>>**/
    private Map<IHandlerName,Map<IStatusType,Map<IStatusValue,IStatusValue>>> tempStatusMap = new HashMap<IHandlerName,Map<IStatusType,Map<IStatusValue,IStatusValue>>>();
    private Set<IStatusType> tempStatusTypes= new HashSet<IStatusType>();
    
    public static final int HANDLER_HANDLED=1;
    public static final int HANDLER_NOT_HANDLED=0;
    public static final int HANDLER_CANNOT_DECIDE=-1;
    
    /* Process描述 */
    public String getProcessDesc(){
    	return this.getClass().getName();
    }
    abstract protected void initHandler() throws ProcessException;

    @PostConstruct
    final public void initManager() throws ProcessException {
        initHandler();
        finishInit();
        if (!this.isValid()) {
            throw new ProcessException(this.getClass().getName() + " is inValid,Init Failed!");
        }
        printAllHandlers();
    }
    
    /*
    public static void printAllProcessManagers(){
		System.out.println("--total:"+AllProcessDef.size());
    	for ( Map.Entry<String, ComposedProcessManager> ent:AllProcessDef.entrySet() ){
    		System.out.println(ent.getKey());
    	}
    }
    */

    public IHandler<?,?,?> getHandler(IHandlerName actionName) {
        return actionNameMap.get(actionName);
    }

    // 判断当前状态支持的Action列表
    public Collection<IHandlerName> getSupportedHandlers(Request<?,?> now) {
        Collection<IHandlerName> handlerNames = null;
        for (ProcessManager pm: getManagers()){
        	IStatusContext context = now.getContext(pm.getStatusType());
            if (context == null) {
                continue;
            }
            IStatusValue status = context.getCurrentStatus();
            
            Collection<IHandlerName> names = pm.getSupportedHandler(status);
            if (names == null) {
                return new HashSet<IHandlerName>();
            }
            if (handlerNames == null) {
                handlerNames = names;
            } else {
                handlerNames = CollectionUtils.intersection(handlerNames, names);
            }
        }
        return handlerNames!=null?handlerNames:new HashSet<IHandlerName>();
    }

    /*
     * 获取支持该动作的所有状态
     */
    public Map<IStatusType, Set<IStatusValue>> getSupportedStatus(IHandlerName actName) {
        Map<IStatusType, Set<IStatusValue>> mm = new HashMap<IStatusType, Set<IStatusValue>>();
        for (ProcessManager pm: getManagers()){
            Set<Node> supportedStatus = pm.getSupportedStatus(actName);
            if ( supportedStatus == null ){
            	continue;
            }
            Set<IStatusValue> svs = new HashSet<IStatusValue>();
            for ( Node n:supportedStatus ){
            	svs.add(n.getSv());
            }
            mm.put(pm.getStatusType(), svs);
        }
        return mm;
    }

    public int isAlreadyHandled(Request<?,?> now, IHandlerName actName) throws ProcessException {
        // step1.判断是否支持该action
        if (!actionNameMap.containsKey(actName)) {
            log.warn(actName + " not exist!");
            return HANDLER_NOT_HANDLED;
        }
        //step2.判断该Handler是否引起状态转换
        boolean isHandlerCauseStatusChange = false;
        for (ProcessManager pm: getManagers()){
            if (pm.isActionCauseStatusChange(actName)){
                isHandlerCauseStatusChange= true;
                break;
            }
        }
        if ( !isHandlerCauseStatusChange ){
            return HANDLER_CANNOT_DECIDE;
        }
        // step3.根据当前所处状态判断，该动作是否已经执行过
        boolean isAlreadyHandled = true;
        for (ProcessManager pm: getManagers()){
            // step3.获取传入状态
        	IStatusContext context = now.getContext(pm.getStatusType());
            if (context == null) {
                continue;
            }
            //step3.1.判断该handler是否引起状态迁移
            if (!pm.isActionCauseStatusChange(actName)){
                continue;
            }
            //step3.2.根据当前状态判断是否该动作之前已经被处理过
            if (!pm.isActionAlreadyHandled(context, actName)) {
                isAlreadyHandled = false;
                break;
            }
            
        }
        if (isAlreadyHandled) {
            log.info(String.format("(%s)", actName) + " already handled!" + (now));
        }
        return isAlreadyHandled?HANDLER_HANDLED:HANDLER_NOT_HANDLED;
    }
    public boolean isSupport(Request<?,?> now, IHandlerName actName) throws ProcessException {
        for (ProcessManager pm: getManagers()){
            // step2.获取传入状态
        	IStatusContext context = now.getContext(pm.getStatusType());
            if (context == null ){ //context缺失，
            	if (pm.isActionAlwaysSupport(actName)) {
            		//永远支持，则忽略
	                continue;
            	}else{
	                throw new ContextException("ContextModel构建有误，缺乏必须的Context:"+pm.getStatusType()+",无法支持Handler:"+actName);
            	}
            }
            // step2.2.遍历Managers，判断当前action在当前传入状态下是否可行
            // 显式设置过状态转换的
            if (!pm.isActionSupported(context, actName)) {
                return false;
            }
        }
        return true;
    }
    
    private void initMapIfNotExist(IHandler<?,?,?> act,IStatusType mgrName){
    	if ( !tempStatusMap.containsKey(act.getName())){
    		tempStatusMap.put(act.getName(), new HashMap<IStatusType, Map<IStatusValue,IStatusValue>>());
    	}
    	if ( !tempStatusMap.get(act.getName()).containsKey(mgrName)){
    		tempStatusMap.get(act.getName()).put(mgrName, new HashMap<IStatusValue, IStatusValue>());
    	}
    }
    protected void registerHandler(IHandler<?,?,?> act, IStatusType mgrName, IStatusValue from, IStatusValue to) throws ProcessException {
    	if ( from.equals(to) ){
    		registerHandler(act, mgrName,from);
    		return;
    	}
    	initMapIfNotExist(act, mgrName);
    	Map<IStatusValue, IStatusValue> kv = tempStatusMap.get(act.getName()).get(mgrName);
    	if ( kv.containsKey(from) && !kv.get(from).equals(to) ){
    		throw new ProcessException("状态类型:"+mgrName+"已经注册过"+act.getName()+","+from+"=>"+kv.get(from)+",不能再注册到"+to);
    	}
    	kv.put(from, to);
    	tempStatusTypes.add(mgrName);
        actionNameMap.put(act.getName(), act);
    }
    protected void registerHandler(IHandler<?,?,?> act, IStatusType mgrName, IStatusValue at) {
    	initMapIfNotExist(act, mgrName);
    	tempStatusMap.get(act.getName()).get(mgrName).put(at, at);
    	tempStatusTypes.add(mgrName);
        actionNameMap.put(act.getName(), act);
    }
    protected void registerHandler(IHandler<?,?,?> act, IStatusType mgrName){
    	initMapIfNotExist(act, mgrName);
    	tempStatusTypes.add(mgrName);
        actionNameMap.put(act.getName(), act);
    }
    /*
     * 注册一个Handler,该Handler可发生在状态(from)，且handler处理成功后状态迁移到(to)
     */
    private void realRegisterHandler(IHandlerName act, ProcessManager pm, IStatusValue from, IStatusValue to) throws ProcessException {
        pm.addNodeTransfer(act, from, to);
    }

    /*
     * 注册一个Handler,该Handler可发生在状态(at)
     */
    private void realRegisterHandler(IHandlerName act, ProcessManager pm, IStatusValue at) throws ProcessException {
        pm.actionHappenAtNode(act, at);
    }
    
    /*
     * 注册一个Handler,该Handler可发生在任何状态
     */
    private void realRegisterHandler(IHandlerName act, ProcessManager pm) throws ProcessException {
        pm.actionHappenAlways(act);
    }
    
    /**
     * 根据handler全集补全各处理机 若处理机n没有注册某handler m,则认为handler m的执行无须考虑n的状态
     * 
     * @throws ProcessException
     */
    public final void finishInit() throws ProcessException {
    	//step0.初始化状态
    	Map<IStatusType,ProcessManager> pmMap = new HashMap<IStatusType,ProcessManager>();
    	for ( IStatusType st:tempStatusTypes){
            ProcessManager pm = new ProcessManager(st);
            this.getManagers().add(pm);
            pmMap.put(st,pm);
        }
    	//step1.lazy-register
    	for ( Map.Entry<IHandlerName, Map<IStatusType,Map<IStatusValue,IStatusValue>>> ent:tempStatusMap.entrySet() ){
    		IHandlerName handlerName = ent.getKey();
    		for ( Map.Entry<IStatusType, Map<IStatusValue,IStatusValue>> ent2:ent.getValue().entrySet() ){
    			IStatusType mgr = ent2.getKey();
    			if ( ent2.getValue().isEmpty() ){
    				realRegisterHandler(handlerName, pmMap.get(mgr));
    				break;
    			}
    			for ( Map.Entry<IStatusValue, IStatusValue> ent3:ent2.getValue().entrySet() ){
    				if (ent3.getKey().getStatusValue().equals(ent3.getValue().getStatusValue())){
    					realRegisterHandler(handlerName, pmMap.get(mgr), ent3.getValue());
    				}else{
    					realRegisterHandler(handlerName, pmMap.get(mgr), ent3.getKey(),ent3.getValue());
    				}
    			}
    		}
    	}
    	//step2.FillWhole
        for (IHandlerName actName : actionNameMap.keySet()) {
            for (ProcessManager pm: getManagers()){
                if (!pm.isActionSupported(actName)) {
                    pm.actionHappenAlways(actionNameMap.get(actName).getName());
                }
            }
        }
    }

    /*
     * 判断状态机是否有效，判断依据： 
     * 1.无环 
     * 2.有handler的handler，
     * 3.各状态机所支持的Action集合必须相同。(防止注册过程中的遗漏)
     */
    public boolean isValid() {
        for (ProcessManager pm: getManagers()){
            if (pm.hasRoundRing()) {
                log.error(pm.getStatusType() + " has ring,Invalid!");
                return false;
            }
            for (IHandlerName actName : actionNameMap.keySet()) {
                if (!pm.isActionSupported(actName)) {
                    log.error(pm.getStatusType() + " " + actName + " Not support!");
                    return false;
                }
            }
        }
        return true;
    }
    
    public void printAllHandlers(){
        for (ProcessManager pm: getManagers()){
		    pm.printAllActions();
        }
    }
    public String toGraph(){
         Map<String,String> descMap = new HashMap<String,String>();
         for (ProcessManager manager: managers) {
             descMap.put(manager.getStatusType().getDesc(), manager.toGraph());
         }
         return JSON.toJSONString(descMap);
    }
    
    public JSONObject toJson(){
    	JSONObject obj = new JSONObject();
        for (ProcessManager pm: getManagers()){
        	obj.put(pm.getStatusType(),pm.toJson());
        }
        return obj;
    }
    public Register register(IHandler<?,?,?> handler){
    	return new Register(handler,this);
    }
    public void unregister(IHandler<?,?,?> handler){
    	tempStatusMap.remove(handler.getName());
    	actionNameMap.remove(handler.getName());
    }
	public Set<ProcessManager> getManagers() {
		return managers;
	}
	protected class Register{
    	IHandler<?,?,?> _hl;
    	TframeProcessManager _pm;
    	IStatusValue _from;
    	IStatusValue _to;
    	boolean registeOver = false;
    	public Register(IHandler<?,?,?> handler,TframeProcessManager pm){
    		_hl = handler;
    		_pm = pm;
    	}
    	public Register from(IStatusValue sv){
    		_from = sv;
    		if ( _from == null){
    			throw new RuntimeException("From状态为空，注册失败，请检查代码!");
    		}
    		return this;
    	}
    	public void to(IStatusValue sv) throws ProcessException{
    		_to= sv;
    		if ( _to == null){
    			throw new RuntimeException("To状态为空，注册失败，请检查代码!");
    		}
    		if (!_from.equals(IStatusValue.PROCESS_INITIAL) 
				&&!_from.getStatusType().equals(_to.getStatusType()) ){
    			throw new RuntimeException("From和To的状态类型不同，不能注册，请检查代码");
    		}
    		_pm.registerHandler(_hl, _to.getStatusType(), _from,_to);
    		_from = null;
    		_to = null;
    		registeOver = true;
    	}
    	public void at(IStatusValue sv){
    		if ( sv == null){
    			throw new RuntimeException("At状态为空，注册失败，请检查代码!");
    		}
    		_pm.registerHandler(_hl, sv.getStatusType(), sv);
    		registeOver = true;
    	}
    	public void all(IStatusType st){
    		if ( st == null){
    			throw new RuntimeException("At状态为空，注册失败，请检查代码!");
    		}
    		_pm.registerHandler(_hl, st);
    		registeOver = true;
    	}
    	
    }
}
