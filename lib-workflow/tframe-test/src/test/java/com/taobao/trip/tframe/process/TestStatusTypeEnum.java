package com.taobao.trip.tframe.process;

import java.util.ArrayList;
import java.util.List;


/*
 * ×´Ì¬ºÏ¼¯
 */
public enum TestStatusTypeEnum implements IStatusType {
	BIZ("Ö÷×´Ì¬",BizStatus.values()),
	PAY("Ö§¸¶×´Ì¬",PayStatus.values());
	
	TestStatusTypeEnum(String d,IStatusValue[] vs){
		this.desc = d;
		for ( IStatusValue v:vs ){
			this.values.add(v);
		}
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

}
