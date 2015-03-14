package com.taobao.trip.tframe.process;



/**
 * 处理器
 * @author leconte
 */
public interface IHandler<Param, Result extends ProcessResultDO, Model extends DataModel> {
	/*
	 * 状态转换前动作，用以准备数据
	 */
	Result execute(Request<Param,Model> request);
	/*
     * 状态转换结束
     */
	void save(Model dataModel,Result result) throws Exception;
	/*
	 * Action名称
	 */
	IHandlerName getName();

}
