package com.taobao.trip.tframe.process;


/*
 * Ö§¸¶×´Ì¬
 */
enum PayStatus implements IStatusValue{
	P1(1, "INIT"),
	P2(2, "CREATE"),
	P3(3, "PAID"),
	P4(4, "DONE"),
	P5(5, "UNKNOWN");
	
	private String statusDesc;
	private Integer statusValue;
	private PayStatus(Integer v,String d) {
		statusDesc = d;
		statusValue = v;
	}
	@Override
	public IStatusType getStatusType() {
		return TestStatusTypeEnum.PAY;
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