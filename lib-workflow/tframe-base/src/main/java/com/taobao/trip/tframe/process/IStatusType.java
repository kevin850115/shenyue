package com.taobao.trip.tframe.process;

import java.util.List;

/**
 * ϵͳ֧�ֵ�״̬���͡�
 * Ӧ�÷�ʹ��enumö��ʵ�ָýӿ�
 * @author leconte
 *
 */
public interface IStatusType {
	String getDesc();
	void addValue(IStatusValue sv);
	//���ڸ���������ֵ�����ض�Ӧ��״̬����
	//ȷ������Ĭ�����������IStatusValue.PROCESS_INITIAL
	IStatusValue getValue(Integer i);
	List<IStatusValue> getValues();
}
