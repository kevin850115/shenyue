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
 * ���״̬����������������״̬���Ķ���
 * 
 * �ڲ��Ƕ�ProcesManager�İ�װ�������ص��ǿ���֧�ֶ�״̬�ڵ�Ǩ�ƹ���
 */
public abstract class TframeProcessManager {
	
    protected static Logger log = LoggerFactory.getLogger(TframeProcessManager.class);
    /* ���Node���� */
    private Set<ProcessManager> managers = new HashSet<ProcessManager>();
    /* Action���Ƶ�Action��ӳ��:�������е�Action */
    private Map<IHandlerName, IHandler<?,?,?>> actionNameMap = new HashMap<IHandlerName, IHandler<?,?,?>>();
    
    /** ��ʱ״̬���棬���Լ�¼״̬ע��(lazy-register**/
    /** <Handler,<MgrName,<From,To>>**/
    private Map<IHandlerName,Map<IStatusType,Map<IStatusValue,IStatusValue>>> tempStatusMap = new HashMap<IHandlerName,Map<IStatusType,Map<IStatusValue,IStatusValue>>>();
    private Set<IStatusType> tempStatusTypes= new HashSet<IStatusType>();
    
    public static final int HANDLER_HANDLED=1;
    public static final int HANDLER_NOT_HANDLED=0;
    public static final int HANDLER_CANNOT_DECIDE=-1;
    
    /* Process���� */
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

    // �жϵ�ǰ״̬֧�ֵ�Action�б�
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
     * ��ȡ֧�ָö���������״̬
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
        // step1.�ж��Ƿ�֧�ָ�action
        if (!actionNameMap.containsKey(actName)) {
            log.warn(actName + " not exist!");
            return HANDLER_NOT_HANDLED;
        }
        //step2.�жϸ�Handler�Ƿ�����״̬ת��
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
        // step3.���ݵ�ǰ����״̬�жϣ��ö����Ƿ��Ѿ�ִ�й�
        boolean isAlreadyHandled = true;
        for (ProcessManager pm: getManagers()){
            // step3.��ȡ����״̬
        	IStatusContext context = now.getContext(pm.getStatusType());
            if (context == null) {
                continue;
            }
            //step3.1.�жϸ�handler�Ƿ�����״̬Ǩ��
            if (!pm.isActionCauseStatusChange(actName)){
                continue;
            }
            //step3.2.���ݵ�ǰ״̬�ж��Ƿ�ö���֮ǰ�Ѿ��������
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
            // step2.��ȡ����״̬
        	IStatusContext context = now.getContext(pm.getStatusType());
            if (context == null ){ //contextȱʧ��
            	if (pm.isActionAlwaysSupport(actName)) {
            		//��Զ֧�֣������
	                continue;
            	}else{
	                throw new ContextException("ContextModel��������ȱ�������Context:"+pm.getStatusType()+",�޷�֧��Handler:"+actName);
            	}
            }
            // step2.2.����Managers���жϵ�ǰaction�ڵ�ǰ����״̬���Ƿ����
            // ��ʽ���ù�״̬ת����
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
    		throw new ProcessException("״̬����:"+mgrName+"�Ѿ�ע���"+act.getName()+","+from+"=>"+kv.get(from)+",������ע�ᵽ"+to);
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
     * ע��һ��Handler,��Handler�ɷ�����״̬(from)����handler����ɹ���״̬Ǩ�Ƶ�(to)
     */
    private void realRegisterHandler(IHandlerName act, ProcessManager pm, IStatusValue from, IStatusValue to) throws ProcessException {
        pm.addNodeTransfer(act, from, to);
    }

    /*
     * ע��һ��Handler,��Handler�ɷ�����״̬(at)
     */
    private void realRegisterHandler(IHandlerName act, ProcessManager pm, IStatusValue at) throws ProcessException {
        pm.actionHappenAtNode(act, at);
    }
    
    /*
     * ע��һ��Handler,��Handler�ɷ������κ�״̬
     */
    private void realRegisterHandler(IHandlerName act, ProcessManager pm) throws ProcessException {
        pm.actionHappenAlways(act);
    }
    
    /**
     * ����handlerȫ����ȫ������� �������nû��ע��ĳhandler m,����Ϊhandler m��ִ�����뿼��n��״̬
     * 
     * @throws ProcessException
     */
    public final void finishInit() throws ProcessException {
    	//step0.��ʼ��״̬
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
     * �ж�״̬���Ƿ���Ч���ж����ݣ� 
     * 1.�޻� 
     * 2.��handler��handler��
     * 3.��״̬����֧�ֵ�Action���ϱ�����ͬ��(��ֹע������е���©)
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
    			throw new RuntimeException("From״̬Ϊ�գ�ע��ʧ�ܣ��������!");
    		}
    		return this;
    	}
    	public void to(IStatusValue sv) throws ProcessException{
    		_to= sv;
    		if ( _to == null){
    			throw new RuntimeException("To״̬Ϊ�գ�ע��ʧ�ܣ��������!");
    		}
    		if (!_from.equals(IStatusValue.PROCESS_INITIAL) 
				&&!_from.getStatusType().equals(_to.getStatusType()) ){
    			throw new RuntimeException("From��To��״̬���Ͳ�ͬ������ע�ᣬ�������");
    		}
    		_pm.registerHandler(_hl, _to.getStatusType(), _from,_to);
    		_from = null;
    		_to = null;
    		registeOver = true;
    	}
    	public void at(IStatusValue sv){
    		if ( sv == null){
    			throw new RuntimeException("At״̬Ϊ�գ�ע��ʧ�ܣ��������!");
    		}
    		_pm.registerHandler(_hl, sv.getStatusType(), sv);
    		registeOver = true;
    	}
    	public void all(IStatusType st){
    		if ( st == null){
    			throw new RuntimeException("At״̬Ϊ�գ�ע��ʧ�ܣ��������!");
    		}
    		_pm.registerHandler(_hl, st);
    		registeOver = true;
    	}
    	
    }
}
