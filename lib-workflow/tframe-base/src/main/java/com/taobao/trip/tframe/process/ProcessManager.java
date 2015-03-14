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
 * ����״̬����������
 * @author leconte
 *
 */
public class ProcessManager {

	private IStatusType statusType;
	
    protected static final Log log = LogFactory.getLog(TframeProcessManager.class);
    /*
     * ��¼���д��ڵĽڵ� key: ״ֵ̬ value: ״̬�ڵ�
     */
    private Map<IStatusValue, Node> nodesMap = new HashMap<IStatusValue, Node>();
    /*
     * ���ж����ܹ������Ľڵ� key:���� value:�ڵ㼯��
     */
    private Map<IHandlerName, Set<Node>> actionConds = new HashMap<IHandlerName, Set<Node>>();
    /*
     * �ܷ��������нڵ�Ķ����б�
     */
    private Set<IHandlerName> actionAlways = new HashSet<IHandlerName>();
    /*
     * ����������������״̬�������������״̬Ǩ�Ƶ������ key:���� value:�ڵ�
     */
    private Map<IHandlerName, Node> actionTransferEnds = new HashMap<IHandlerName, Node>();


    // �ж�from->to��״̬Ǩ���Ƿ���ܳɹ�
    /*
     * public boolean isFromCanReachTo(Integer from,Integer to){ Node f= nodesMap.get(from); Node t = nodesMap.get(to);
     * if ( f== null || t == null ) return false; return f.canReach(t); }
     */
    // �жϵ�ǰ״̬֧�ֵ�Action�б�

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
    		//step0.�Ƿ���������״̬
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

    // ��ȡ��ִ�е�ǰAction�����״̬
    public Set<Node> getSupportedStatus(IHandlerName act) {
        return actionConds.get(act);
    }
    
    public ProcessManager(IStatusType st) {
        addNode(IStatusValue.PROCESS_INITIAL);// ��ʼ�ڵ����Դ���
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
     * �ж�״̬Ǩ���Ƿ��л� ����Action1����1->2��Action2����2->1 ԭ�������׻��Ʋ�֧�ֻ���״̬��
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
        if (f != t) {// Important:��������Щ������״̬Ǩ�Ƶ�Action
            actionTransferEnds.put(act, to);
        }
    }

    // action���Է���������Node�£����Ҳ�����״̬��Ǩ
    public void actionHappenAlways(IHandlerName act) throws ProcessException {
        this.actionHappenAtNode(act, nodesMap.keySet());
        this.actionAlways.add(act);
    }

    // action���ҽ�������ָ��״̬��ʱ��ſ���ִ��
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

    /* �ж��Ƿ�֧�ָö��� */
    public boolean isActionSupported(IHandlerName actName) {
        return actionConds.containsKey(actName);
    }
    
    /*
     * ��������״̬�ڵ�A,B
     * �ж��Ƿ��A�Ƿ���Ե���B
     */
    public boolean isAcanReachB(IStatusValue a,IStatusValue b){
        Node nodeA = nodesMap.get(a);
        Node nodeB = nodesMap.get(b);
        if ( nodeA == null || nodeB == null ){
            return false;
        }
        return nodeA.canReach(nodeB);
    }

    /* �ж��ڵ�ǰ״̬�£��Ƿ�֧�ָö��� */
    public boolean isActionSupported(IStatusContext now, IHandlerName handler) throws ProcessException {
        // �жϵ�ǰmanager�Ƿ�֧�ִ�Action
        if (!actionConds.containsKey(handler)) {
            return false;
        }
        // ��ȡ��ǰstatus
        IStatusValue s = now.getCurrentStatus();
        if (s == null) {// ��ʼ��״̬
            throw new ProcessException("Context.Status is null!");
        }
        Node currentNode = nodesMap.get(s);
        if (currentNode == null) {
            throw new ProcessException("Node is null!,status:"+s.toString());
        }
        // ��ǰNode�ܷ�֧�ִ�Action
        if (!actionConds.get(handler).contains(currentNode)) {
            log.warn("current status {"+currentNode.toString()+" } not support handler:" + handler);
            return false;
        }
        return true;
    }

    /*
     * �жϸ�Handler�Ƿ�����״̬Ǩ��
     */
    public boolean isActionCauseStatusChange(IHandlerName handler) throws ProcessException {
        return actionTransferEnds.containsKey(handler);
    }
    /*
     * �ж�Handler�Ƿ�ɷ������κ�������
     */
    public boolean isActionAlwaysSupport(IHandlerName handler) throws ProcessException {
    	return actionAlways.contains(handler);
    }

    /*
     * �жϸö����Ƿ��Ѿ���֮ǰִ�й���: ����ı�׼�ǣ� 1.�ö������� 2.���Ҹö���ִ����Ϻ��״̬<=��ǰ״̬
     */
    public boolean isActionAlreadyHandled(IStatusContext now, IHandlerName handler) throws ProcessException {
        // �жϵ�ǰmanager�Ƿ�֧�ִ�Action
        if (!actionConds.containsKey(handler)) {
            return false;
        }
        // ��ȡ��ǰstatus
        IStatusValue s = now.getCurrentStatus();
        if (s == null) {// ��ʼ��״̬
            throw new ProcessException("Context.Status is null!");
        }
        Node currentNode = nodesMap.get(s);
        if (currentNode == null) {
            throw new ProcessException("Node is null!");
        }
        // ��ȡ��ǰaction�����������״̬
        Node expectedTo = actionTransferEnds.get(handler);
        if (expectedTo == null) {// ��ǰaction��û������״̬Ǩ�ƣ�����޷��жϸö����Ƿ�����ִ�й�
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
        //step1.����ÿ���ڵ�
        for (Entry<IStatusValue, Node> node : nodesMap.entrySet()) {
            graphNode(out, node.getValue());
        }
        //step2.�����ڵ���ת��
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
            //step1.���ת�Ƶ��Ľڵ�Ϊ�������¼
            if (trans.getValue().equals(n)){
                actions.add(act);
                continue;
	            //out.append(String.format("%d -> %d [label=\"%s\",fontsize=8,arrowType=\"none\"];\n", n.getStatus().getCode(), trans.getValue()
	            //        .getStatus().getCode(), act));
            }else{
            //step2.��¼�ڵ�ת��
	            out.append(String.format("%d -> %d [label=\"%s\",fontsize=8];\n", n.getSv().getStatusValue(), trans.getValue().
                    getSv().getStatusValue(), act.getUniqName()+"["+act.getDesc()+"]"));
            }
        }
    }
}
