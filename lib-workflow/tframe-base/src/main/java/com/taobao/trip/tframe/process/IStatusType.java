package com.taobao.trip.tframe.process;

import java.util.List;

/**
 * 系统支持的状态类型。
 * 应用方使用enum枚举实现该接口
 * @author leconte
 *
 */
public interface IStatusType {
	String getDesc();
	void addValue(IStatusValue sv);
	//对于给定的整数值，返回对应的状态对象；
	//确保对于默认情况：返回IStatusValue.PROCESS_INITIAL
	IStatusValue getValue(Integer i);
	List<IStatusValue> getValues();
}
