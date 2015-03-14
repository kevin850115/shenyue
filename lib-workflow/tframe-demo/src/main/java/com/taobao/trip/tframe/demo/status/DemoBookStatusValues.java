package com.taobao.trip.tframe.demo.status;

import com.taobao.trip.tframe.process.IStatusType;
import com.taobao.trip.tframe.process.IStatusValue;

public enum DemoBookStatusValues implements IStatusValue {
	
	T1(1, "T1"),
	T2(2, "T2"),
	T3(3, "T3"),
	T4(4, "T4"),
	T5(5, "T5"),
	T6(6, "T6"),
	T7(7, "T7"),
	T8(8, "T8"),
	T9(10, "T9"),
	T10(10, "T10");

	
	private String statusDesc;
	private Integer statusValue;
	private DemoBookStatusValues(Integer v,String d) {
		statusDesc = d;
		statusValue = v;
	}
	public DemoBookStatusValues valueOf(Integer s){
		for (DemoBookStatusValues sv:DemoBookStatusValues.values() ){
			if ( sv.getStatusValue().equals(s)){
				return sv;
			}
		}
		return null;
	}
	@Override
	public IStatusType getStatusType() {
		return DemoStatusType.T;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	public void setStatusValue(Integer statusValue) {
		this.statusValue = statusValue;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public Integer getStatusValue() {
		return statusValue;
	}
}
