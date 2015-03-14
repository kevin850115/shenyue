package com.taobao.trip.tframe.process;


/*
 * »ù´¡×´Ì¬¶¨Òå
 */
enum BizStatus implements IStatusValue{
	N1(1, "1"),
	N2(2, "2"),
	N3(3, "3"),
	N4(4, "4"),
	N5(5, "5"),
	N6(6, "6");
	
	private String statusDesc;
	private Integer statusValue;
	private BizStatus(Integer v,String d) {
		statusDesc = d;
		statusValue = v;
	}
	@Override
	public IStatusType getStatusType() {
		return TestStatusTypeEnum.BIZ;
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