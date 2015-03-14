package com.taobao.trip.tframe.process;


public interface IStatusContext {
	IStatusValue getCurrentStatus();
	IStatusValue getOldStatus();
	IStatusType getStatusType();
	void changeStatusTo(IStatusValue s);
	boolean isChanged();
}
