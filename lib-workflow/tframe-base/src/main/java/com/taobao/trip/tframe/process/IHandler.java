package com.taobao.trip.tframe.process;



/**
 * ������
 * @author leconte
 */
public interface IHandler<Param, Result extends ProcessResultDO, Model extends DataModel> {
	/*
	 * ״̬ת��ǰ����������׼������
	 */
	Result execute(Request<Param,Model> request);
	/*
     * ״̬ת������
     */
	void save(Model dataModel,Result result) throws Exception;
	/*
	 * Action����
	 */
	IHandlerName getName();

}
