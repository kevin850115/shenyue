package com.taobao.trip.tframe.process;


/*
 * һ�����͵�����
 */
public class TestRequest extends Request {


	public IStatusType[] getStatusTypes() { return TestStatusTypeEnum.values(); }

	@Override
	public Long getUniqId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initCtx() {
		// TODO Auto-generated method stub
		
	}


}
