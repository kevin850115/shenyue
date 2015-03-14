package com.taobao.trip.tframe.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.taobao.trip.tframe.err.ProcessErrorEnum;
import com.taobao.trip.tframe.process.ctx.ProcessInitContext;
import com.taobao.trip.tframe.process.result.MyResultDO;
import com.taobao.trip.tframe.process.selector.FixedProcessSelector;
import com.taobao.trip.tframe.process.selector.IProcessSelector;
import com.taobao.trip.tframe.process.trigger.TframeEngine;

public class ProcessTest {
	@Test
	public void test_��ʼ��Context(){
		IStatusContext sc = new ProcessInitContext(TestStatusTypeEnum.BIZ);
		assertEquals(sc.getCurrentStatus(),IStatusValue.PROCESS_INITIAL);
		sc.changeStatusTo(BizStatus.N3);
		assertEquals(sc.getCurrentStatus(),BizStatus.N3);
		assertEquals(sc.getOldStatus(),IStatusValue.PROCESS_INITIAL);
		assertTrue(sc.isChanged());
		sc.changeStatusTo(BizStatus.N4);
		assertEquals(sc.getCurrentStatus(),BizStatus.N4);
		assertEquals(sc.getOldStatus(),BizStatus.N3);
		assertTrue(sc.isChanged());
	}
    @Test
    public void test_��ʼ״̬_һԪ״̬_��������_����֧�ֵ�Handlers() throws Exception{
    	TframeProcessManager proc = new TestOneTradeProcess();
        proc.initManager();
        Request cm = new TestRequest();
        IProcessSelector ps = new FixedProcessSelector(proc);
        TframeEngine trigger = new TframeEngine();
        trigger.setProcessSelector(ps);
        {
	    	//�ڵ�״̬Ϊ1
	        cm.addContext(new BizStatusContext(1));
	        Collection<IHandlerName> action = proc.getSupportedHandlers(cm);
	        assertTrue(action.contains(TestHandlerName.Act1to2));
	        assertTrue(action.contains(TestHandlerName.ActStay1and2));
	        assertFalse(action.contains(TestHandlerName.TwoAct1to2));
	        assertFalse(action.contains(TestHandlerName.Act3to5));
	        assertTrue(proc.isSupport(cm, TestHandlerName.Act1to2));
    		ProcessResultDO rs = trigger.isSupport(cm, TestHandlerName.Act1to2);
    		assertTrue(rs.isSuccess());
	        assertTrue(!proc.isSupport(cm, TestHandlerName.TwoAct1to2));
        }
        {
	    	//�ڵ�״̬Ϊ2
	        cm.addContext(new BizStatusContext(2));
	        Collection<IHandlerName> action = proc.getSupportedHandlers(cm);
	        assertTrue(action.contains(TestHandlerName.ActStay1and2));
	        assertTrue(action.contains(TestHandlerName.Act2to3));
        }
        {
	    	//�ڵ�״̬Ϊ5
	        cm.addContext(new BizStatusContext(5));
	        Collection<IHandlerName> action = proc.getSupportedHandlers(cm);
	        for ( IHandlerName hl:action ){
	            System.out.println(hl);
	        }
	        assertTrue(action.contains(TestHandlerName.ActAt4and5));
        }
        {
	    	//�ڵ�״̬Ϊ7
	        cm.addContext(new BizStatusContext(7));
	        Collection<IHandlerName> action = proc.getSupportedHandlers(cm);
	        assertTrue(action.size()==2);
        }
        {
	    	//�ڵ�״̬Ϊ8�������ڵ�״̬��ʲô����֧��
	        cm.addContext(new BizStatusContext(8));
	        Collection<IHandlerName> action = proc.getSupportedHandlers(cm);
	        assertTrue(action.size()==2);
        }
        {
	    	//�¼�һ���ڵ㣬����δע��״̬����û��ʲôӰ��
	        cm.addContext(new BizStatusContext(1));
	        cm.addContext(new PayStatusContext(2));
	        Collection<IHandlerName> action = proc.getSupportedHandlers(cm);
	        assertTrue(action.size()==8);
        }
        
    }
	@Test
	public void test_��ʼ״̬_һԪ״̬_��������() throws Exception{
		TestOneTradeProcess proc = new TestOneTradeProcess();
		proc.initManager();
		Request cm = new TestRequest();
		cm.addContext(new BizStatusContext(1));
        IProcessSelector ps = new FixedProcessSelector(proc);
        TframeEngine trigger = new TframeEngine();
        trigger.setProcessSelector(ps);
		ProcessResultDO execute;
		//status:1
		execute = trigger.trigger(cm, TestHandlerName.ActStay1and2);
		assertTrue(execute.isSuccess());
		{
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getCurrentStatus(),BizStatus.N1);
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getOldStatus(),null);
		}
		//����δ����״̬�ı��Handler�����޷�������Handler�Ƿ�ִ�й���
		assertTrue(proc.isAlreadyHandled(cm, TestHandlerName.ActStay1and2)==TframeProcessManager.HANDLER_CANNOT_DECIDE);
		assertTrue(proc.isAlreadyHandled(cm, TestHandlerName.Act1to2)==TframeProcessManager.HANDLER_NOT_HANDLED);
		//ִ��1-2
		 execute = trigger.trigger(cm, TestHandlerName.Act1to2);
		assertTrue(execute.isSuccess());
		{
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getCurrentStatus(),BizStatus.N2);
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getOldStatus(),BizStatus.N1);
		}
		assertTrue(proc.isAlreadyHandled(cm, TestHandlerName.Act1to2)==TframeProcessManager.HANDLER_HANDLED);
		assertTrue(proc.isAlreadyHandled(cm, TestHandlerName.Act2to3)==TframeProcessManager.HANDLER_NOT_HANDLED);
	
		//��ִ��ʧ��
		execute = trigger.trigger(cm, TestHandlerName.Act1to2);
		assertFalse(execute.isSuccess());
		assertTrue(ProcessErrorEnum.ALREADY_HANDLED.isEqual(execute));
		{
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getCurrentStatus(),BizStatus.N2);
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getOldStatus(),BizStatus.N1);
		}
		//
		execute = trigger.trigger(cm, TestHandlerName.ActStay1and2);
		assertTrue(execute.isSuccess());
		{
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getCurrentStatus(),BizStatus.N2);
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getOldStatus(),BizStatus.N1);
		}
		//2->3
		execute = trigger.trigger(cm, TestHandlerName.Act2to3);
		assertTrue(execute.isSuccess());
		{
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getCurrentStatus(),BizStatus.N3);
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getOldStatus(),BizStatus.N2);
		}
		assertTrue(proc.isAlreadyHandled(cm, TestHandlerName.Act1to2)==TframeProcessManager.HANDLER_HANDLED);
		assertTrue(proc.isAlreadyHandled(cm, TestHandlerName.Act2to3)==TframeProcessManager.HANDLER_HANDLED);
		//
		execute = trigger.trigger(cm, TestHandlerName.Act2to3);
		assertFalse(execute.isSuccess());
		assertTrue(ProcessErrorEnum.ALREADY_HANDLED.isEqual(execute));
		{
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getCurrentStatus(),BizStatus.N3);
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getOldStatus(),BizStatus.N2);
		}
		//3->5
		execute = trigger.trigger(cm, TestHandlerName.Act3to5);
		assertTrue(execute.isSuccess() );
		{
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getCurrentStatus(),BizStatus.N5);
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getOldStatus(),BizStatus.N3);
		}
		execute = trigger.trigger(cm, TestHandlerName.ActAt4and5);
		assertTrue(execute.isSuccess());
		{
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getCurrentStatus(),BizStatus.N5);
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getOldStatus(),BizStatus.N3);
		}
		execute = trigger.trigger(cm, TestHandlerName.ActStay1and2);
		assertFalse(execute.isSuccess());
		assertTrue(ProcessErrorEnum.HANDLER_NOT_SUPPORT.isEqual(execute));
	}
	@Test
	public void test_��ʼ״̬_һԪ״̬_�κ�״̬�ɷ���() throws Exception{
		TestOneTradeProcess proc = new TestOneTradeProcess();
		proc.initManager();
		Request cm = new TestRequest();
		cm.addContext(new BizStatusContext(1));
        IProcessSelector ps = new FixedProcessSelector(proc);
        TframeEngine trigger = new TframeEngine();
        trigger.setProcessSelector(ps);
		ProcessResultDO execute;
		//status:1
		execute = trigger.trigger(cm, TestHandlerName.ActAlways);
		assertTrue(execute.isSuccess());
		{
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getCurrentStatus(),BizStatus.N1);
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getOldStatus(),null);
		}
		Map<IStatusType, Set<IStatusValue>> sc = proc.getSupportedStatus(TestHandlerName.ActAlways);
		Set<IStatusValue> ssc = sc.get(TestStatusTypeEnum.BIZ);
		for (IStatusValue sv: TestStatusTypeEnum.BIZ.getValues()){
			assertTrue(ssc.contains(sv));
		}
	}
	@Test
	public void test_��ʼ״̬_һԪ״̬_���뱣���Handler() throws Exception{
		TestOneTradeProcess proc = new TestOneTradeProcess();
		proc.initManager();
		Request cm = new TestRequest();
		cm.addContext(new BizStatusContext(1));
        IProcessSelector ps = new FixedProcessSelector(proc);
        TframeEngine trigger = new TframeEngine();
        trigger.setProcessSelector(ps);
		ProcessResultDO execute;
		//status:1
		execute = trigger.trigger(cm, TestHandlerName.ActStay1and2WithoutSave);
		assertTrue(execute.isSuccess());
		{
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getCurrentStatus(),BizStatus.N1);
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getOldStatus(),null);
		}
	}
	@Test
	public void test_��ʼ״̬_һԪ״̬_Exec�쳣��Handler() throws Exception{
		TestOneTradeProcess proc = new TestOneTradeProcess();
		proc.initManager();
		Request cm = new TestRequest();
		cm.addContext(new BizStatusContext(1));
        IProcessSelector ps = new FixedProcessSelector(proc);
        TframeEngine trigger = new TframeEngine();
        trigger.setProcessSelector(ps);
		ProcessResultDO execute;
		//status:1
		execute = trigger.trigger(cm, TestHandlerName.ActStay1and2ExecException);
		assertFalse(execute.isSuccess());
		ProcessErrorEnum.UNKNOWN_ERROR.fillResult(execute);
		{
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getCurrentStatus(),BizStatus.N1);
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getOldStatus(),null);
		}
	}
	@Test
	public void test_��ʼ״̬_һԪ״̬_Exec_NULL��Handler() throws Exception{
		TestOneTradeProcess proc = new TestOneTradeProcess();
		proc.initManager();
		Request cm = new TestRequest();
		cm.addContext(new BizStatusContext(1));
        IProcessSelector ps = new FixedProcessSelector(proc);
        TframeEngine trigger = new TframeEngine();
        trigger.setProcessSelector(ps);
		ProcessResultDO execute;
		//status:1
		execute = trigger.trigger(cm, TestHandlerName.ActStay1and2ExecNullException);
		assertFalse(execute.isSuccess());
		ProcessErrorEnum.HANDLER_EXE_NULL.fillResult(execute);
		{
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getCurrentStatus(),BizStatus.N1);
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getOldStatus(),null);
		}
	}
	@Test
	public void test_��ʼ״̬_һԪ״̬_�Զ��巵��ֵ() throws Exception{
		TestOneTradeProcess proc = new TestOneTradeProcess();
		proc.initManager();
		Request cm = new TestRequest();
		cm.addContext(new BizStatusContext(1));
        IProcessSelector ps = new FixedProcessSelector(proc);
        TframeEngine trigger = new TframeEngine();
        trigger.setProcessSelector(ps);
        MyResultDO execute;
		//status:1
		execute = trigger.trigger(cm, TestHandlerName.MY,MyResultDO.class);
		assertTrue(execute.isSuccess());
		assertTrue(!execute.getRr().isEmpty());
	}
	@Test
	public void test_��ʼ״̬_һԪ״̬_Save�쳣��Handler() throws Exception{
		TestOneTradeProcess proc = new TestOneTradeProcess();
		proc.initManager();
		Request cm = new TestRequest();
		cm.addContext(new BizStatusContext(1));
        IProcessSelector ps = new FixedProcessSelector(proc);
        TframeEngine trigger = new TframeEngine();
        trigger.setProcessSelector(ps);
		ProcessResultDO execute;
		//status:1
		execute = trigger.trigger(cm, TestHandlerName.ActStay1and2SaveException);
		assertFalse(execute.isSuccess());
		ProcessErrorEnum.SAVE2DB_IN_HANDLER_EXCEPTION.fillResult(execute);
		{
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getCurrentStatus(),BizStatus.N1);
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getOldStatus(),null);
		}
	}
	@Test
	public void test_��ʼ״̬_һԪ״̬_Selector����() throws Exception{
		TestOneTradeProcess proc = new TestOneTradeProcess();
		proc.initManager();
		Request cm = new TestRequest();
		cm.addContext(new BizStatusContext(1));
        IProcessSelector ps = new FixedProcessSelector(null);
        TframeEngine trigger = new TframeEngine();
        trigger.setProcessSelector(ps);
		ProcessResultDO execute;
		//status:1
		execute = trigger.trigger(cm, TestHandlerName.ActStay1and2);
		assertFalse(execute.isSuccess());
		System.out.println(execute);
		ProcessErrorEnum.PROCESS_NOT_EXIST.fillResult(execute);
		ProcessResultDO support = trigger.isSupport(cm, TestHandlerName.ActStay1and2);
		ProcessErrorEnum.PROCESS_NOT_EXIST.fillResult(support);
	}
	@Test
	public void test_��ʼ״̬_һԪ״̬_Actionʧ��() throws Exception{
		TestOneTradeProcessHasFailedAction proc = new TestOneTradeProcessHasFailedAction();
		proc.initManager();
		Request cm = new TestRequest();
		cm.addContext(new BizStatusContext(1));
        IProcessSelector ps = new FixedProcessSelector(proc);
        TframeEngine trigger = new TframeEngine();
        trigger.setProcessSelector(ps);
		ProcessResultDO execute;
		//ִ��1-2
		execute = trigger.trigger(cm, TestHandlerName.FailAct1to2);
		assertFalse(execute.isSuccess());
		assertTrue(proc.isAlreadyHandled(cm, TestHandlerName.FailAct1to2)==TframeProcessManager.HANDLER_NOT_HANDLED);
		//ִ��δע���Action
		execute = trigger.trigger(cm, TestHandlerName.Act1to2);
		assertFalse(execute.isSuccess());
		assertTrue(ProcessErrorEnum.HANDLER_NOT_SUPPORT.isEqual(execute));
		
		assertTrue(proc.isAlreadyHandled(cm, TestHandlerName.Act1to2)==TframeProcessManager.HANDLER_NOT_HANDLED);
		assertTrue(proc.isAlreadyHandled(cm, TestHandlerName.Act2to3)==TframeProcessManager.HANDLER_NOT_HANDLED);
	}
	
	@Test(expected=ProcessException.class)
	public void test_��ʼ״̬_һԪ״̬_Actionע����Ч() throws Exception{
		TestOneTradeProcessHasInvalidAction proc = new TestOneTradeProcessHasInvalidAction();
		proc.initManager();
	}
	@Test
	public void test_��ʼ״̬_һԪ״̬_ִ��δע���Action() throws Exception{
		TestOneTradeProcess proc = new TestOneTradeProcess();
		proc.initManager();
		Request cm = new TestRequest();
		cm.addContext(new BizStatusContext(1));
        IProcessSelector ps = new FixedProcessSelector(proc);
        TframeEngine trigger = new TframeEngine();
        trigger.setProcessSelector(ps);
		ProcessResultDO execute;
		//status:1
		execute = trigger.trigger(cm, TestHandlerName.FailAct1to2);
		assertFalse(execute.isSuccess());
		assertTrue(ProcessErrorEnum.HANDLER_NOT_SUPPORT.isEqual(execute));
		assertFalse(cm.getContext(TestStatusTypeEnum.BIZ).isChanged());
	}
	@Test
	public void test_��ʼ״̬_��Ԫ״̬_��������() throws Exception{
		TestTwoTradeProcess proc = new TestTwoTradeProcess();
		proc.initManager();
		Request cm = new TestRequest();
		cm.addContext(new BizStatusContext(1));
		cm.addContext(new PayStatusContext(1));
        IProcessSelector ps = new FixedProcessSelector(proc);
        TframeEngine trigger = new TframeEngine();
        trigger.setProcessSelector(ps);
		ProcessResultDO execute;
		//status:1->2
		execute = trigger.trigger(cm, TestHandlerName.TwoAct1to2);
		assertTrue(execute.isSuccess());
		{
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getCurrentStatus(),BizStatus.N2);
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getOldStatus(),BizStatus.N1);
			assertEquals(cm.getContext(TestStatusTypeEnum.PAY).getCurrentStatus(),PayStatus.P2);
			assertEquals(cm.getContext(TestStatusTypeEnum.PAY).getOldStatus(),PayStatus.P1);
		}
		assertTrue(proc.isAlreadyHandled(cm, TestHandlerName.TwoAct2to3)==TframeProcessManager.HANDLER_NOT_HANDLED);
		execute = trigger.trigger(cm, TestHandlerName.TwoAct2to3);
		assertTrue(execute.isSuccess());
		{
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getCurrentStatus(),BizStatus.N2);//û�б�
			assertEquals(cm.getContext(TestStatusTypeEnum.BIZ).getOldStatus(),BizStatus.N1);//û��
			assertEquals(cm.getContext(TestStatusTypeEnum.PAY).getCurrentStatus(),PayStatus.P3);
			assertEquals(cm.getContext(TestStatusTypeEnum.PAY).getOldStatus(),PayStatus.P2);
		}
		assertTrue(proc.isAlreadyHandled(cm, TestHandlerName.TwoAct2to3)==TframeProcessManager.HANDLER_HANDLED);
		assertTrue(proc.isAlreadyHandled(cm, TestHandlerName.TwoAct1to2)==TframeProcessManager.HANDLER_HANDLED);
		//�ٴ�ִ��
		execute = trigger.trigger(cm, TestHandlerName.TwoAct2to3);
		assertFalse(execute.isSuccess());
		assertTrue(ProcessErrorEnum.ALREADY_HANDLED.isEqual(execute));
	}
	@Test
    public void test_��ʼ״̬_��Ԫ״̬_��������_֧�ֵ�Action() throws Exception{
        TestTwoTradeProcess proc = new TestTwoTradeProcess();
        proc.initManager();
        Request cm = new TestRequest();
		cm.addContext(new BizStatusContext(1));
		cm.addContext(new PayStatusContext(1));
        IProcessSelector ps = new FixedProcessSelector(proc);
        TframeEngine trigger = new TframeEngine();
        trigger.setProcessSelector(ps);
        Collection<IHandlerName> handlerNames = proc.getSupportedHandlers(cm);
        assertEquals(handlerNames.size(),1);
        assertTrue(handlerNames.contains(TestHandlerName.TwoAct1to2));
        assertTrue(!handlerNames.contains(TestHandlerName.TwoAct2to3));
        assertTrue(!handlerNames.contains(TestHandlerName.TwoAct3to5));
   
    }

	
	@Test
    public void test_��ʼ״̬_��Ԫ״̬_SupportedStatus() throws Exception{
		TestTwoTradeProcess proc = new TestTwoTradeProcess();
        proc.initManager();
        {
	        Map<IStatusType, Set<IStatusValue>> supportedStatus = proc.getSupportedStatus(TestHandlerName.TwoAct2to3);
	        assertTrue(supportedStatus.containsKey(TestStatusTypeEnum.BIZ));
	        assertTrue(supportedStatus.containsKey(TestStatusTypeEnum.PAY));
	        {
		        Set<IStatusValue> svs = supportedStatus.get(TestStatusTypeEnum.BIZ);
		        for (BizStatus bs:BizStatus.values() ){
		        	assertTrue(svs.contains(bs));
		        }
			}
	        {
	        	Set<IStatusValue> svs = supportedStatus.get(TestStatusTypeEnum.PAY);
	            assertTrue(svs.size()==1);
	            assertTrue(svs.contains(PayStatus.P2));
	        }
        }
        {
	        Map<IStatusType, Set<IStatusValue>> supportedStatus = proc.getSupportedStatus(TestHandlerName.TwoAct3to5);
            assertTrue(supportedStatus.isEmpty());
        }
    }
	
	@Test(expected=ProcessException.class)
	public void test_��ʼ״̬_��Ԫ״̬_Action�л�() throws Exception{
		TestTwoTradeProcessRing proc = new TestTwoTradeProcessRing();
		proc.initManager();
	}
	@Test
	public void test_��ʼ״̬_��Ԫ״̬_״̬������ȱto_from������() throws Exception{
		TestTwoNotComplete proc = new TestTwoNotComplete();
		proc.initManager();
		{
	        Map<IStatusType, Set<IStatusValue>> supportedStatus = proc.getSupportedStatus(TestHandlerName.TwoAct1to2);
        	Set<IStatusValue> svs = supportedStatus.get(TestStatusTypeEnum.PAY);
            assertTrue(svs.size()==1);
            assertTrue(svs.contains(PayStatus.P2));
            assertTrue(!svs.contains(PayStatus.P1));
        }
	}
	@Test
	public void test_��ʼ״̬_��Ԫ״̬_Contextȱʧ() throws Exception{
		TestTwoTradeProcess proc = new TestTwoTradeProcess();
		proc.initManager();
		Request cm = new TestRequest();
		cm.addContext(new PayStatusContext(1));
        IProcessSelector ps = new FixedProcessSelector(proc);
        TframeEngine trigger = new TframeEngine();
        trigger.setProcessSelector(ps);
		ProcessResultDO execute;
		//status:1->2
		//Biz������ʱ״̬ȱʧ���������˳�ʼ��ֵ��ִ��ʧ��
		execute = trigger.trigger(cm, TestHandlerName.TwoAct1to2);
		assertFalse(execute.isSuccess());
		System.out.println(execute);
		ProcessResultDO support = trigger.isSupport(cm, TestHandlerName.TwoAct1to2);
		assertFalse(support.isSuccess());

		cm.addContext(new PayStatusContext(2));//����ԭ����Context
		//Biz������ʱ״̬ȱʧ����TwoAct1to2��Biz����Ҫ��ģ�����ִ��
		execute = trigger.trigger(cm, TestHandlerName.TwoAct2to3);
		assertTrue(execute.isSuccess());

	}
	
	@Test
    public void test_��ʼ״̬_һԪ״̬_JSON() throws Exception{
    	TframeProcessManager proc = new TestOneTradeProcess();
        proc.initManager();
        Request cm = new TestRequest();
        IProcessSelector ps = new FixedProcessSelector(proc);
        TframeEngine trigger = new TframeEngine();
        trigger.setProcessSelector(ps);
        System.out.println(proc.toJson());
	}
	@Test
    public void test_��ʼ״̬_һԪ״̬_ע��ʧ��_FromNull() throws Exception{
		{
			class TestPM extends TframeProcessManager{
				@Override
				protected void initHandler() throws ProcessException {
					//step1.ע��Action
					register(new FailAct1to2()).from(null);
				}
			}
			boolean isExp=false;
			try{
				TestPM proc = new TestPM();
		        proc.initManager();
			}catch(Exception e){
				isExp = true;
			}
			assertTrue(isExp);
		}
		{
			class TestPM extends TframeProcessManager{
				@Override
				protected void initHandler() throws ProcessException {
					//step1.ע��Action
					register(new FailAct1to2()).from(BizStatus.N1).to(null);
				}
			}
			boolean isExp=false;
			try{
				TestPM proc = new TestPM();
		        proc.initManager();
			}catch(Exception e){
				isExp = true;
			}
			assertTrue(isExp);
		}
		{
			class TestPM extends TframeProcessManager{
				@Override
				protected void initHandler() throws ProcessException {
					//step1.ע��Action
					register(new FailAct1to2()).from(BizStatus.N1).to(PayStatus.P2);
				}
			}
			boolean isExp=false;
			try{
				TestPM proc = new TestPM();
		        proc.initManager();
			}catch(Exception e){
				isExp = true;
			}
			assertTrue(isExp);
		}
		{
			class TestPM extends TframeProcessManager{
				@Override
				protected void initHandler() throws ProcessException {
					//step1.ע��Action
					register(new FailAct1to2()).at(null);
				}
			}
			boolean isExp=false;
			try{
				TestPM proc = new TestPM();
		        proc.initManager();
			}catch(Exception e){
				isExp = true;
			}
			assertTrue(isExp);
		}
		{
			class TestPM extends TframeProcessManager{
				@Override
				protected void initHandler() throws ProcessException {
					//step1.ע��Action
					register(new FailAct1to2()).all(null);
				}
			}
			boolean isExp=false;
			try{
				TestPM proc = new TestPM();
		        proc.initManager();
			}catch(Exception e){
				isExp = true;
			}
			assertTrue(isExp);
		}
		{
			class TestPM extends TframeProcessManager{
				@Override
				protected void initHandler() throws ProcessException {
					//step1.ע��Action
					Register register = register(new FailAct1to2());
					register.at(BizStatus.N1);
					register.at(BizStatus.N2);
				}
			}
			boolean isExp=false;
			try{
				TestPM proc = new TestPM();
		        proc.initManager();
			}catch(Exception e){
				isExp = true;
			}
			assertFalse(isExp);
		}
	}
}
