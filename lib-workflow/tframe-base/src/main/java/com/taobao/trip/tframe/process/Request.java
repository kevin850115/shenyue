package com.taobao.trip.tframe.process;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.taobao.trip.tframe.process.ctx.ProcessInitContext;


/**
 * 
 * Handler����������ģ��,����ʱ����ģ��
 * 
 * Param:�������
 * @author leconte
 *
 */
public abstract class Request<Param,Model extends DataModel> {

    private Param requestParam;//�������
    private Model dataModel;//����ģ��
	abstract public IStatusType[] getStatusTypes();//ָ����֧�ŵ�״̬����
	abstract protected void initCtx();//��ʼ������ģ�ͣ��������ʵ��
    abstract public Long getUniqId();//Ψһid�����ڸ���.
	/*��������Ĺ���*/
	private Map<IStatusType,IStatusContext> contexts = new HashMap<IStatusType,IStatusContext>();
   
    public Request(){
    }
    public void doInit(){
    	//1.�����ʼ������
    	initCtx();
    	//2.��ȫδ��ʼ����״̬����Ϊ�̶�����
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
