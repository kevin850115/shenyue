package com.taobao.trip.tframe.config;

import org.springframework.transaction.support.TransactionTemplate;

/**
 * 系统配置。使用Tframe的业务方必须实现这个接口
 * @author leconte
 *
 */
public class TframeSystemConfig {
	private String appName;//应用名
	private TransactionTemplate txTemplate;//事务模板

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
