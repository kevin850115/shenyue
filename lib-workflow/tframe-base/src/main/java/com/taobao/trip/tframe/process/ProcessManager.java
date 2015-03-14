package com.taobao.trip.tframe.process;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 单个状态的流程描述
 * @author leconte
 *
 */
public class ProcessManager {

	private IStatusType statusType;
	
    protected static final Log log = LogFactory.getLog(TframeProcessManager.class);
    /*
     * 记录所有存在的节点 key: 状态值 value: 状态节点
     */
    private Map<IStatusValue, Node> nodesMap = new HashMap<IStatusValue, Node>();
    /*
     * 所有动作能够发生的节点 key:动作 value:节点集合
     */
    private Map<IHandlerName, Set<Node>> actionConds = new HashMap<IHandlerName, Set<Node>>();
    /*
     * 能发生在所有节点的动作列表
     */
    private Set<IHandlerName> actionAlways = new HashSet<IHandlerName>();
    /*
     * 动作发生后所处的状态【仅保存会引起状态迁移的情况】 key:动作 value:节点
     */
    private Map<IHandlerName, Node> actionTransferEnds = new HashMap<IHandlerName, Node>();


    // 判断from->to的状态迁移是否可能成功
    /*
     * public boolean isFromCanReachTo(Integer from,Integer to){ Node f= nodesMap.get(from); Node t = nodesMap.get(to);
     * if ( f== null || t == null ) return false; return f.canReach(t); }
     */
    // 判断当前状态支持的Action列表

    public Collection<IHandlerName> getSupportedHandler(IStatusValue status) {
        Node n = nodesMap.get(status);
        if (n == null)
            return null;
        return n.getTransfers().keySet();
    }
    
    public JSONObject toJson(){
    	JSONObject obj = new JSONObject();
    	for ( Map.Entry<IHandlerName, Set<Node>> ent:actionConds.entrySet() ){
    		JSONObject handler  = new JSONObject();
    		Set<Node> nodes = ent.getValue();
    		//step0.是否发生在所有状态
    		if ( actionAlways.contains(ent.getKey())){
    			handler.put("always", true);
    		}else{
	    		//step1.Happen at
	    		JSONArray happens = new JSONArray();
	    		for ( Node node:nodes){
	    			happens.add(node.toJson());
	    		}
	    		handler.put("at",happens);
	    		//step2.Transfer To
				Node toNode = actionTransferEnds.get(ent.getKey());
				if ( toNode != null ){
		    		handler.put("to",toNode.toJson());
				}
    		}
    		obj.put(ent.getKey(), handler);
    	}
    	return obj;
    }

    // 获取能执行当前Action所需的状态
    public Set<Node> getSupportedStatus(IHandlerName act) {
        return actionConds.get(act);
    }
    
    public ProcessManager(IStatusType st) {
        addNode(IStatusValue.PROCESS_INITIAL);// 初始节点是自带的
        statusType = st;
        for ( IStatusValue n:st.getValues() ){
            this.addNode(n);
        }
    }

    public void addNode(IStatusValue sv) {
    	if ( !nodesMap.containsKey(sv) ){
	        nodesMap.put(sv,new Node(sv));
    	}
    }

    public void printAllActions() {
        StringBuffer buf = new StringBuffer();
        for (Map.Entry<IHandlerName, Set<Node>> ent : actionConds.entrySet()) {
            buf.append(ent.getKey()).append(" Happens When:");
            for (Node n : ent.getValue()) {
                buf.append(n.getSv().getStatusValue()).append(",");
            }
            buf.append("\n");
        }
        System.out.println(buf);
        for (Map.Entry<IStatusValue, Node> ent : nodesMap.entrySet()) {
            ent.getValue().printTransferFrom();
            ent.getValue().printTransferTo();
        }
    }
    
    /*
     * 判断状态迁移是否有环 即：Action1引起1->2，Action2引起2->1 原则上这套机制不支持环形状态。
     */
    public boolean hasRoundRing() {
        for (Map.Entry<IStatusValue, Node> ent : nodesMap.entrySet()) {
            Node node = ent.getValue();
            Set<Node> transferTo = node.getTransferTo();
            for (Node to : transferTo) {
                if (to == node)
                    continue;
                if (to.getTransferTo().contains(node))
                    return true;
            }
        }
        return false;
    }

    public Node getNode(IStatusValue sv) {
        return nodesMap.get(sv);
    }

    public void addNodeTransfer(IHandlerName act, IStatusValue f, IStatusValue t) throws ProcessException {
        Node from = nodesMap.get(f);
        Node to = nodesMap.get(t);
        if (from == null || to == null)
            throw new ProcessException("Node is null,f:" + f + ",t:" + t);
        from.addNodeTransfer(act, to);
        if (!actionConds.containsKey(act)) {
            actionConds.put(act, new HashSet<Node>());
        }
        actionConds.get(act).add(from);
        if (actionTransferEnds.get(act) != null && !actionTransferEnds.get(act).equals(to)) {
            throw new ProcessException("Same Action cann't point to different Node:" + act);
        }
        if (f != t) {// Important:仅保存那些会引起状态迁移的Action
            actionTransferEnds.put(act, to);
        }
    }

    // action可以发生在任意Node下，并且不引起状态变迁
    public void actionHappenAlways(IHandlerName act) throws ProcessException {
        this.actionHappenAtNode(act, nodesMap.keySet());
        this.actionAlways.add(act);
    }

    // action当且仅当处于指定状态的时候才可以执行
    public void actionHappenAtNode(IHandlerName act, Set<IStatusValue> ss) throws ProcessException {
        for (IStatusValue s : ss) {
            Node n = nodesMap.get(s);
            if (n == null)
                throw new ProcessException("Node is null!");

        }
        for (IStatusValue s : ss) {
            addNodeTransfer(act, s, s);
        }
    }

    public void actionHappenAtNode(IHandlerName act, IStatusValue s) throws ProcessException {
        Node n = nodesMap.get(s);
        if (n == null)
            throw new ProcessException("Node is null!");
        addNodeTransfer(act, s, s);
    }

    /* 判断是否支持该动作 */
    public boolean isActionSupported(IHandlerName actName) {
        return actionConds.containsKey(actName);
    }
    
    /*
     * 给定两个状态节点A,B
     * 判断是否从A是否可以到达B
     */
    public boolean isAcanReachB(IStatusValue a,IStatusValue b){
        Node nodeA = nodesMap.get(a);
        Node nodeB = nodesMap.get(b);
        if ( nodeA == null || nodeB == null ){
            return false;
        }
        return nodeA.canReach(nodeB);
    }

    /* 判断在当前状态下，是否支持该动作 */
    public boolean isActionSupported(IStatusContext now, IHandlerName handler) throws ProcessException {
        // 判断当前manager是否支持此Action
        if (!actionConds.containsKey(handler)) {
            return false;
        }
        // 获取当前status
        IStatusValue s = now.getCurrentStatus();
        if (s == null) {// 起始无状态
            throw new ProcessException("Context.Status is null!");
        }
        Node currentNode = nodesMap.get(s);
        if (currentNode == null) {
            throw new ProcessException("Node is null!,status:"+s.toString());
        }
        // 当前Node能否支持此Action
        if (!actionConds.get(handler).contains(currentNode)) {
            log.warn("current status {"+currentNode.toString()+" } not support handler:" + handler);
            return false;
        }
        return true;
    }

    /*
     * 判断该Handler是否引起状态迁移
     */
    public boolean isActionCauseStatusChange(IHandlerName handler) throws ProcessException {
        return actionTransferEnds.containsKey(handler);
    }
    /*
     * 判断Handler是否可发生在任何条件下
     */
    public boolean isActionAlwaysSupport(IHandlerName handler) throws ProcessException {
    	return actionAlways.contains(handler);
    }

    /*
     * 判断该动作是否已经在之前执行过了: 具体的标准是： 1.该动作存在 2.并且该动作执行完毕后的状态<=当前状态
     */
    public boolean isActionAlreadyHandled(IStatusContext now, IHandlerName handler) throws ProcessException {
        // 判断当前manager是否支持此Action
        if (!actionConds.containsKey(handler)) {
            return false;
        }
        // 获取当前status
        IStatusValue s = now.getCurrentStatus();
        if (s == null) {// 起始无状态
            throw new ProcessException("Context.Status is null!");
        }
        Node currentNode = nodesMap.get(s);
        if (currentNode == null) {
            throw new ProcessException("Node is null!");
        }
        // 获取当前action发生后的期望状态
        Node expectedTo = actionTransferEnds.get(handler);
        if (expectedTo == null) {// 当前action并没有引起状态迁移，因此无法判断该动作是否真正执行过
            return false;
        }
        return expectedTo.canReach(currentNode);
    }

    public void setActionAlways(Set<IHandlerName> actionAlways) {
        this.actionAlways = actionAlways;
    }

    public Set<IHandlerName> getActionAlways() {
        return actionAlways;
    }

	public IStatusType getStatusType() {
		return statusType;
	}

	public String toGraph() {
		StringBuilder out = new StringBuilder();
        out.append("digraph " + statusType.getDesc() + "{");
        out.append("rankdir=\"LR\";");
        //step1.定义每个节点
        for (Entry<IStatusValue, Node> node : nodesMap.entrySet()) {
            graphNode(out, node.getValue());
        }
        //step2.划出节点间的转移
        for (Entry<IStatusValue, Node> node : nodesMap.entrySet()) {
            graphNodeRelation(out, node.getValue());
        }
        out.append("}");
        return out.toString();
	}
	private void graphNode(StringBuilder out, Node n) {
		IStatusValue sv = n.getSv();
        if (n.isEndNode() && n.isStartNode())
            return;
        if (n.isEndNode() || n.isStartNode()) {
            out.append(String
                    .format("%d [label=\"%s(%d)\",shape=box];\n", sv.getStatusValue(), sv.getStatusDesc(),sv.getStatusValue()));
        } else {
            out.append(String.format("%d [label=\"%s(%d)\",shape=egg,color=\"red\",ratio=\"fill\"];\n", sv.getStatusValue(), sv.getStatusDesc(),sv.getStatusValue()));
        }
    }
	private void graphNodeRelation(StringBuilder out, Node n) {
        Map<IHandlerName, Node> transfers = n.getTransfers();
        List<IHandlerName> actions = new ArrayList<IHandlerName>();
        for (Entry<IHandlerName, Node> trans : transfers.entrySet()) {
        	IHandlerName act = trans.getKey();
            //step1.如果转移到的节点为自身，则记录
            if (trans.getValue().equals(n)){
                actions.add(act);
                continue;
	            //out.append(String.format("%d -> %d [label=\"%s\",fontsize=8,arrowType=\"none\"];\n", n.getStatus().getCode(), trans.getValue()
	            //        .getStatus().getCode(), act));
            }else{
            //step2.记录节点转移
	            out.append(String.format("%d -> %d [label=\"%s\",fontsize=8];\n", n.getSv().getStatusValue(), trans.getValue().
                    getSv().getStatusValue(), act.getUniqName()+"["+act.getDesc()+"]"));
            }
        }
    }
}
