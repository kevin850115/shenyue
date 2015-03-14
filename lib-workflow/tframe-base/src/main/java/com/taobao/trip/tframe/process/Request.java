package com.taobao.trip.tframe.process;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.taobao.trip.tframe.process.ctx.ProcessInitContext;


/**
 * 
 * Handler的请求数据模型,运行时数据模型
 * 
 * Param:请求参数
 * @author leconte
 *
 */
public abstract class Request<Param,Model extends DataModel> {

    private Param requestParam;//输入参数
    private Model dataModel;//数据模型
	abstract public IStatusType[] getStatusTypes();//指定所支撑的状态类型
	abstract protected void initCtx();//初始化数据模型，子类必须实现
    abstract public Long getUniqId();//唯一id，用于跟踪.
	/*多个上下文管理*/
	private Map<IStatusType,IStatusContext> contexts = new HashMap<IStatusType,IStatusContext>();
   
    public Request(){
    }
    public void doInit(){
    	//1.具体初始化过程
    	initCtx();
    	//2.补全未初始化的状态类型为固定类型
    	for ( IStatusType st:getStatusTypes() ){
    		if ( !contexts.containsKey(st) ){
    			contexts.put(st, new ProcessInitContext(st));
    		}
    	}
    }
	public IStatusContext getContext(IStatusType key) {
		return contexts.get(key);
	}
	public void addContext(IStatusContext value){
		contexts.put(value.getStatusType(), value);
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for ( Entry<IStatusType,IStatusContext> ent:this.contexts.entrySet() ){
			if ( ent.getValue().getOldStatus() == null ) {
				sb.append(String.format("| %s:%d(%s) ", 
						ent.getKey(),
						ent.getValue().getCurrentStatus().getStatusValue(),
						ent.getValue().getCurrentStatus().getStatusDesc()
						));
			}else{
				sb.append(String.format("| %s:%d(%s)->%d(%s) ", 
						ent.getKey(),
						ent.getValue().getOldStatus().getStatusValue(),
						ent.getValue().getOldStatus().getStatusDesc(),
						ent.getValue().getCurrentStatus().getStatusValue(),
						ent.getValue().getCurrentStatus().getStatusDesc()
						));
			}
		}
		return sb.toString();
	}
	
	public void setRequestParam(Param requestParam) {
		this.requestParam = requestParam;
	}
	public Param getRequestParam() {
		return requestParam;
	}
	public void setDataModel(Model dataModel) {
		this.dataModel = dataModel;
	}
	public Model getDataModel() {
		return dataModel;
	}
}
