package com.taobao.trip.tframe.process;



/**
 * ĳ��״̬���Ͷ�Ӧ��״ֵ̬
 * Ӧ�÷�ʹ��enumö��ʵ�ָýӿڣ���ָ��������״̬����
 * @author leconte
 *
 */
public interface IStatusValue {
	Integer getStatusValue();
	String getStatusDesc();
	IStatusType getStatusType();
    /*
     * ��ʼ��״ֵ̬
     */
    static public IStatusValue PROCESS_INITIAL = new IStatusValue() {
		@Override
		public String getStatusDesc() {
			return "ϵͳ��ʼ��ֵ";
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
