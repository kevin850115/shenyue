package com.taobao.trip.tframe.process;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;


/**
 * �����е�ÿ���ڵ㶨��
 * @author leconte
 *
 */
public class Node {

	protected IStatusValue sv;
	/* �ýṹ�̺���˼:
	 * 1. ��Action�ɹ�ִ�к�״̬Ǩ�Ƶ�Node
	 */
	protected Map<IHandlerName,Node> transfers = new HashMap<IHandlerName,Node>();
	/*
	 * �ܵ��ﵱǰ�ڵ�Ľڵ㼯�ϣ���������ǰ�ڵ�
	 */
	protected Set<Node> transferFrom = new HashSet<Node>();
	/*
	 * ��ǰ�ڵ��ܵ���Ľڵ㼯�ϣ���������ǰ�ڵ�
	 */
	protected Set<Node> transferTo = new HashSet<Node>();

	public Node(IStatusValue sv){
		this.sv = sv;
	}

	public JSONObject toJson(){
		JSONObject obj = new JSONObject();
		obj.put("code", sv.getStatusValue());
		obj.put("desc", sv.getStatusDesc());
		return obj;
	}
	private void addTransferTo(Node n){
		//��ǰ�ڵ�͵�ǰ�ڵ��ǰ�� -> �ڵ�n�ͽڵ�n�ĺ���
		this.transferTo.add(n);
		this.transferTo.addAll(n.getTransferTo());
		for ( Node node:this.getTransferFrom() ){
			node.transferTo.add(n);
			node.transferTo.addAll(n.getTransferTo());
		}
		//�ڵ�n�ͽڵ�n�ĺ��� --> ��ǰ�ڵ�͵�ǰ�ڵ��ǰ��
		n.getTransferFrom().add(this);
		n.getTransferFrom().addAll(this.getTransferFrom());
		for ( Node node:n.getTransferTo() ){
			node.getTransferFrom().add(this);
			node.getTransferFrom().addAll(this.getTransferFrom());
		}
	}
	/*from this to n*/
	public void addNodeTransfer(IHandlerName a,Node n){
		transfers.put(a,n);
		if ( !this.equals(n) ) {
			this.addTransferTo(n);
		}
	}
	/*
	 * ��ǰ�ڵ��Ƿ�ɴ�n
	 */
	public boolean canReach(Node n){
		return this.equals(n) || transferTo.contains(n);
	}
	public Set<Node> getTransferFrom() {
		return transferFrom;
	}
	public Set<Node> getTransferTo() {
		return transferTo;
	}
	public Node getNextNode(IHandlerName act){
		if ( transfers.containsKey(act) )
			return transfers.get(act);
		else return this;
	}
	public void printTransferFrom(){
		if ( isStartNode() ) return;
		StringBuffer buf = new StringBuffer();
		buf.append(sv).append("  | " );
		for ( Node n:transferFrom){
			buf.append( n.getSv().getStatusValue() ).append(",");
		}
		buf.append("--->").append(sv.getStatusValue());
		System.out.println(buf);
	}
    public IStatusValue getSv() {
		return sv;
	}
	public void setSv(IStatusValue sv) {
		this.sv = sv;
	}
	public void printTransferTo(){
		if ( isEndNode() ) return;
		StringBuffer buf = new StringBuffer();
		buf.append(sv).append("  | " );
		buf.append(sv.getStatusValue()).append("--->");
		for ( Node n:transferTo){
			buf.append( n.getSv().getStatusValue() ).append(",");
		}
		System.out.println(buf);
	}
	public Map<IHandlerName, Node> getTransfers() {
		return transfers;
	}
	public boolean isStartNode(){
		return this.getTransferFrom().isEmpty() || 
			(this.getTransferFrom().size() == 1 
			&& this.getTransferFrom().iterator().next().equals(this) );
	}
	public boolean isEndNode(){
		return this.getTransferTo().isEmpty() || 
			(this.getTransferTo().size() == 1 
			&& this.getTransferTo().iterator().next().equals(this) );
	}
    @Override
    public String toString() {
        return "[status=" + sv + "]";
    }
}
