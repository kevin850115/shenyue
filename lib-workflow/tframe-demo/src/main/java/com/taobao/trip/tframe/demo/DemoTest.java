package com.taobao.trip.tframe.demo;


public class DemoTest {
	/*
	public void createOrder(){
	    //step0.获取对象
	    ProcDO procDO = new ProcDO();
	    procDO.setStatus(ProcDO.STATUS_INIT);
	    procDO.setPayStatus(ProcDO.PAY_STATUS_INIT);
		//step1.构造ContextModel
		ContextModel now = new ProcCommonModel();
		now.addContext(new ProcPayStatusContext(procDO));
		now.addContext(new ProcStatusContext(procDO));
        IProcessSelector ps = new FixedProcessSelector(pm);
        TframeTrigger trigger = new TframeTrigger();
        trigger.setProcessSelector(ps);
		//step2.执行Action
		try {
			ProcessResultDO rs= trigger.exec(now, DemoHandlerName.CREATE);
			if (! rs.isSuccess() ){
				System.out.println("Action exe failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void payForOrder(){
		//step1.构造ContextModel
		ProcDO proc = ProcDAO.getUnPaidOrder(1234l);
		ContextModel now = new ProcCommonModel();
		now.addContext(new ProcPayStatusContext(proc));
		now.addContext(new ProcStatusContext(proc));		//step2.执行Action
        IProcessSelector ps = new FixedProcessSelector(pm);
        TframeTrigger trigger = new TframeTrigger();
        trigger.setProcessSelector(ps);
		try {
			ProcessResultDO rs= trigger.exec(now, DemoHandlerName.PAY);
			if (! rs.isSuccess() ){
				System.out.println("Action exe failed");
			}
		} catch (Exception e) {
		
			e.printStackTrace();
		}
	}
	public void successOrder(){
		//step1.构造ContextModel
		//step2.执行Action
		try {
			ContextModel ctx = buildCtx(1234l);
	        IProcessSelector ps = new FixedProcessSelector(pm);
	        TframeTrigger trigger = new TframeTrigger();
	        trigger.setProcessSelector(ps);
			ProcessResultDO rs= trigger.exec(ctx,DemoHandlerName.PAY);
			if (!rs.isSuccess() ){
				System.out.println("Action exe failed");
			}
			System.out.println(rs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private ContextModel buildCtx(Long orderId){
		ProcDO proc = ProcDAO.getUnPaidOrder(orderId);
		ContextModel now = new ProcCommonModel();
		now.addContext(new ProcPayStatusContext(proc));
		now.addContext(new ProcStatusContext(proc));		
		return now;
		
	}
	*/
}
