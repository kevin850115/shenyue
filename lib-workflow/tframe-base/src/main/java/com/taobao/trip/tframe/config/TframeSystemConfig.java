package com.taobao.trip.tframe.config;

import org.springframework.transaction.support.TransactionTemplate;

/**
 * ϵͳ���á�ʹ��Tframe��ҵ�񷽱���ʵ������ӿ�
 * @author leconte
 *
 */
public class TframeSystemConfig {
	private String appName;//Ӧ����
	private TransactionTemplate txTemplate;//����ģ��

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppName() {
		return appName;
	}

	public void setTxTemplate(TransactionTemplate txTemplate) {
		this.txTemplate = txTemplate;
	}

	public TransactionTemplate getTxTemplate() {
		return txTemplate;
	}

	

}
