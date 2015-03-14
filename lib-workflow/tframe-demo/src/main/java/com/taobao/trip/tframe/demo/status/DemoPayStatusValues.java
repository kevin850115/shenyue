package com.taobao.trip.tframe.demo.status;

import com.taobao.trip.tframe.process.IStatusType;
import com.taobao.trip.tframe.process.IStatusValue;

public enum DemoPayStatusValues implements IStatusValue {
	
	P1(1, "P1"),
	P2(2, "P2"),
	P3(3, "P3"),
	P4(4, "P4"),
	P5(5, "P5"),
	P6(6, "P6"),
	P7(7, "P7"),
	P8(8, "P8"),
	P9(10, "P9"),
	P10(10, "P10");

	private String statusDesc;
	private Integer statusValue;
	private DemoPayStatusValues(Integer v,String d) {
		statusDesc = d;
		statusValue = v;
	}
	
	public DemoPayStatusValues valueOf(Integer s){
		for (DemoPayStatusValues sv:DemoPayStatusValues.values() ){
			if ( sv.getStatusValue().equals(s)){
				return sv;
			}
		}
		return null;
	}
	@Override
	public IStatusType getStatusType() {
		return DemoStatusType.P;
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
