package com.taobao.trip.tframe.process;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;


/**
 * 流程中的每个节点定义
 * @author leconte
 *
 */
public class Node {

	protected IStatusValue sv;
	/* 该结构蕴涵意思:
	 * 1. 当Action成功执行后，状态迁移到Node
	 */
	protected Map<IHandlerName,Node> transfers = new HashMap<IHandlerName,Node>();
	/*
	 * 能到达当前节点的节点集合，不包含当前节点
	 */
	protected Set<Node> transferFrom = new HashSet<Node>();
	/*
	 * 当前节点能到达的节点集合，不包含当前节点
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
		//当前节点和当前节点的前驱 -> 节点n和节点n的后驱
		this.transferTo.add(n);
		this.transferTo.addAll(n.getTransferTo());
		for ( Node node:this.getTransferFrom() ){
			node.transferTo.add(n);
			node.transferTo.addAll(n.getTransferTo());
		}
		//节点n和节点n的后驱 --> 当前节点和当前节点的前驱
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
	 * 当前节点是否可达n
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
