package com.taobao.trip.tframe.demo.status;

import com.taobao.trip.tframe.process.IStatusType;
import com.taobao.trip.tframe.process.IStatusValue;

public enum DemoBaseStatusValues implements IStatusValue {
	B1(1, "B1"),
	B2(2, "B2"),
	B3(3, "B3"),
	B4(4, "B4"),
	B5(5, "B5"),
	B6(6, "B6"),
	B7(7, "B7"),
	B8(8, "B8"),
	B9(9, "B9"),
	B10(10, "B10");
	
	private String statusDesc;
	private Integer statusValue;
	private DemoBaseStatusValues(Integer v,String d) {
		statusDesc = d;
		statusValue = v;
	}
	public DemoBaseStatusValues valueOf(Integer s){
		for (DemoBaseStatusValues sv:DemoBaseStatusValues.values() ){
			if ( sv.getStatusValue().equals(s)){
				return sv;
			}
		}
		return null;
	}
	@Override
	public IStatusType getStatusType() {
		return DemoStatusType.B;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	public Integer getStatusValue() {
		return statusValue;
	}
	public void setStatusValue(Integer statusValue) {
		this.statusValue = statusValue;
	}
}
