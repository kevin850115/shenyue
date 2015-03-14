package com.taobao.trip.tframe.process;
/**
 * 系统支持的Handler列表
 * 应用方使用enum枚举实现该接口
 * @author leconte
 *
 */
public interface IHandlerName {
	String getUniqName();
	String getDesc();
}
