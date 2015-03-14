package com.taobao.trip.tframe.demo.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.taobao.trip.tframe.process.IStatusType;
import com.taobao.trip.tframe.process.IStatusValue;

public enum DemoStatusType implements IStatusType {
	B("Ö÷×´Ì¬",DemoBaseStatusValues.values()),
	P("Ö§¸¶×´Ì¬",DemoPayStatusValues.values()),
	T("³öÆ±×´Ì¬",DemoBookStatusValues.values());
	
	DemoStatusType(String d,IStatusValue[] vs){
		this.desc = d;
		for ( IStatusValue v:vs ){
			this.values.add(v);
		}
		Collections.sort(this.values, new Comparator<IStatusValue>(){
			@Override
			public int compare(IStatusValue arg0, IStatusValue arg1) {
				return arg0.getStatusValue() - arg1.getStatusValue();
			}
		});
	}
	private String desc;
	private List<IStatusValue> values=new ArrayList<IStatusValue>();
	@Override
	public String getDesc() {
		return desc;
	}
	@Override
	public void addValue(IStatusValue sv) {
		values.add(sv);
	}
	@Override
	public IStatusValue getValue(Integer i) {
		for ( IStatusValue sv:values ){
			if ( sv.getStatusValue().equals(i) ){
				return sv;
			}
		}
		return IStatusValue.PROCESS_INITIAL;
	}
	@Override
	public List<IStatusValue> getValues() {
		return values;
	}
	public static void main(String[] args) {
		System.out.println(DemoStatusType.B.getValues().size());
		System.out.println(DemoStatusType.P.getValues().size());
		System.out.println(DemoStatusType.T.getValues().size());
		System.out.println(JSON.toJSONString(DemoStatusType.B.getValues()));

	}

}
