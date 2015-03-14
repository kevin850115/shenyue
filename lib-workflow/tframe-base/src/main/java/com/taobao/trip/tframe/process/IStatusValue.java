package com.taobao.trip.tframe.process;



/**
 * 某个状态类型对应的状态值
 * 应用方使用enum枚举实现该接口，并指名所属的状态类型
 * @author leconte
 *
 */
public interface IStatusValue {
	Integer getStatusValue();
	String getStatusDesc();
	IStatusType getStatusType();
    /*
     * 初始化状态值
     */
    static public IStatusValue PROCESS_INITIAL = new IStatusValue() {
		@Override
		public String getStatusDesc() {
			return "系统初始化值";
		}
		@Override
		public Integer getStatusValue() {
			return -1234;
		}
		@Override
		public IStatusType getStatusType() {
			return null;
		}
	};
}
