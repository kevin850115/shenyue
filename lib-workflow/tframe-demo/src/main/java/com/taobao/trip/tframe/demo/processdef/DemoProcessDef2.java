package com.taobao.trip.tframe.demo.processdef;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.trip.tframe.demo.handler.DemoHandler1;
import com.taobao.trip.tframe.demo.handler.DemoHandler10;
import com.taobao.trip.tframe.demo.handler.DemoHandler11;
import com.taobao.trip.tframe.demo.handler.DemoHandler12;
import com.taobao.trip.tframe.demo.handler.DemoHandler13;
import com.taobao.trip.tframe.demo.handler.DemoHandler14;
import com.taobao.trip.tframe.demo.handler.DemoHandler15;
import com.taobao.trip.tframe.demo.handler.DemoHandler16;
import com.taobao.trip.tframe.demo.handler.DemoHandler17;
import com.taobao.trip.tframe.demo.handler.DemoHandler18;
import com.taobao.trip.tframe.demo.handler.DemoHandler19;
import com.taobao.trip.tframe.demo.handler.DemoHandler2;
import com.taobao.trip.tframe.demo.handler.DemoHandler20;
import com.taobao.trip.tframe.demo.handler.DemoHandler21;
import com.taobao.trip.tframe.demo.handler.DemoHandler22;
import com.taobao.trip.tframe.demo.handler.DemoHandler23;
import com.taobao.trip.tframe.demo.handler.DemoHandler24;
import com.taobao.trip.tframe.demo.handler.DemoHandler25;
import com.taobao.trip.tframe.demo.handler.DemoHandler26;
import com.taobao.trip.tframe.demo.handler.DemoHandler27;
import com.taobao.trip.tframe.demo.handler.DemoHandler28;
import com.taobao.trip.tframe.demo.handler.DemoHandler29;
import com.taobao.trip.tframe.demo.handler.DemoHandler3;
import com.taobao.trip.tframe.demo.handler.DemoHandler30;
import com.taobao.trip.tframe.demo.handler.DemoHandler4;
import com.taobao.trip.tframe.demo.handler.DemoHandler5;
import com.taobao.trip.tframe.demo.handler.DemoHandler6;
import com.taobao.trip.tframe.demo.handler.DemoHandler7;
import com.taobao.trip.tframe.demo.handler.DemoHandler8;
import com.taobao.trip.tframe.demo.handler.DemoHandler9;
import com.taobao.trip.tframe.demo.status.DemoBaseStatusValues;
import com.taobao.trip.tframe.demo.status.DemoBookStatusValues;
import com.taobao.trip.tframe.demo.status.DemoPayStatusValues;
import com.taobao.trip.tframe.demo.status.DemoStatusType;
import com.taobao.trip.tframe.process.IStatusValue;
import com.taobao.trip.tframe.process.ProcessException;
import com.taobao.trip.tframe.process.TframeProcessManager;
@Component
public class DemoProcessDef2 extends TframeProcessManager{

	public void initHandler() throws ProcessException{
		//step1.demo1
		register(demoHandler11).from(IStatusValue.PROCESS_INITIAL).to(DemoBaseStatusValues.B1);
		register(demoHandler11).from(IStatusValue.PROCESS_INITIAL).to(DemoPayStatusValues.P1);
		register(demoHandler11).from(IStatusValue.PROCESS_INITIAL).to(DemoBookStatusValues.T1);
		//demo2
		register(demoHandler12).from(DemoBaseStatusValues.B1).to(DemoBaseStatusValues.B2);
		register(demoHandler12).at(DemoBaseStatusValues.B2);
		register(demoHandler12).at(DemoPayStatusValues.P1);
		register(demoHandler12).at(DemoPayStatusValues.P2);
		register(demoHandler12).at(DemoPayStatusValues.P3);
		register(demoHandler12).at(DemoBookStatusValues.T1);
		//demo3
		register(demoHandler23).from(DemoBaseStatusValues.B1).to(DemoBaseStatusValues.B3);
		register(demoHandler23).from(DemoPayStatusValues.P1).to(DemoPayStatusValues.P2);
		register(demoHandler23).from(DemoBookStatusValues.T1).to(DemoBookStatusValues.T4);
		//demo4
		register(demoHandler24).from(DemoBaseStatusValues.B3).to(DemoBaseStatusValues.B4);
		register(demoHandler24).from(DemoPayStatusValues.P2).to(DemoPayStatusValues.P10);
		//demo5
		register(demoHandler25).from(DemoBaseStatusValues.B4).to(DemoBaseStatusValues.B10);
		//demo6
		register(demoHandler26).from(DemoBookStatusValues.T4).to(DemoBookStatusValues.T10);
		register(demoHandler26).from(DemoPayStatusValues.P4).to(DemoPayStatusValues.P5);
		//demo7
		register(demoHandler27).all(DemoStatusType.B);
		register(demoHandler27).from(DemoBookStatusValues.T4).to(DemoBookStatusValues.T5);
		//demo8
		register(demoHandler28).from(DemoBaseStatusValues.B4).to(DemoBaseStatusValues.B5);
		register(demoHandler28).from(DemoBookStatusValues.T5).to(DemoBookStatusValues.T10);
		//demo9
		register(demoHandler29).from(DemoPayStatusValues.P2).to(DemoPayStatusValues.P4);
		register(demoHandler29).from(DemoPayStatusValues.P3).to(DemoPayStatusValues.P4);
		register(demoHandler29).from(DemoBookStatusValues.T5).to(DemoBookStatusValues.T7);
		register(demoHandler29).from(DemoBookStatusValues.T6).to(DemoBookStatusValues.T7);
		register(demoHandler29).from(DemoBookStatusValues.T4).to(DemoBookStatusValues.T7);
		register(demoHandler29).from(DemoBookStatusValues.T7).to(DemoBookStatusValues.T7);
		//demo10
		register(demoHandler30).all(DemoStatusType.B);
		//demo11
		register(demoHandler1).from(DemoPayStatusValues.P4).to(DemoPayStatusValues.P6);
		register(demoHandler1).from(DemoPayStatusValues.P5).to(DemoPayStatusValues.P6);
		register(demoHandler1).from(DemoBookStatusValues.T7).to(DemoBookStatusValues.T9);
		register(demoHandler1).from(DemoBookStatusValues.T6).to(DemoBookStatusValues.T9);
		register(demoHandler1).from(DemoBookStatusValues.T5).to(DemoBookStatusValues.T9);
		register(demoHandler1).from(DemoBookStatusValues.T8).to(DemoBookStatusValues.T9);
		register(demoHandler1).from(DemoBaseStatusValues.B3).to(DemoBaseStatusValues.B5);
		register(demoHandler1).from(DemoBaseStatusValues.B1).to(DemoBaseStatusValues.B5);
		register(demoHandler1).from(DemoBaseStatusValues.B2).to(DemoBaseStatusValues.B5);
		register(demoHandler1).from(DemoBaseStatusValues.B4).to(DemoBaseStatusValues.B5);
		//demo12
		register(demoHandler2).from(DemoBaseStatusValues.B5).to(DemoBaseStatusValues.B6);
		register(demoHandler2).from(DemoPayStatusValues.P6).to(DemoPayStatusValues.P9);
		//demo13
		register(demoHandler3).from(DemoBaseStatusValues.B6).to(DemoBaseStatusValues.B9);
		register(demoHandler3).from(DemoPayStatusValues.P6).to(DemoPayStatusValues.P9);
		//demo14
		register(demoHandler4).from(DemoBaseStatusValues.B4).to(DemoBaseStatusValues.B9);
		register(demoHandler4).from(DemoPayStatusValues.P4).to(DemoPayStatusValues.P9);
		register(demoHandler4).at(DemoBookStatusValues.T10);
		register(demoHandler4).at(DemoBookStatusValues.T9);
		register(demoHandler4).at(DemoBookStatusValues.T8);
		register(demoHandler4).at(DemoBookStatusValues.T7);
		register(demoHandler4).at(DemoBookStatusValues.T6);
		register(demoHandler4).at(DemoBookStatusValues.T1);
		register(demoHandler4).at(DemoBookStatusValues.T2);
		register(demoHandler4).at(DemoBookStatusValues.T3);
		register(demoHandler4).at(DemoBookStatusValues.T4);
		//demo15
		register(demoHandler5).from(DemoBaseStatusValues.B9).to(DemoBaseStatusValues.B10);
		//demo1
		register(demoHandler6).from(DemoBookStatusValues.T3).to(DemoBookStatusValues.T8);
		register(demoHandler6).from(DemoBookStatusValues.T4).to(DemoBookStatusValues.T8);
		register(demoHandler6).from(DemoBookStatusValues.T7).to(DemoBookStatusValues.T8);
		register(demoHandler6).from(DemoBookStatusValues.T1).to(DemoBookStatusValues.T8);
		//demo17
		register(demoHandler7).from(DemoPayStatusValues.P8).to(DemoPayStatusValues.P10);
		register(demoHandler7).from(DemoPayStatusValues.P9).to(DemoPayStatusValues.P10);
		register(demoHandler7).from(DemoPayStatusValues.P5).to(DemoPayStatusValues.P10);
		//demo18
		register(demoHandler8).from(DemoBaseStatusValues.B9).to(DemoBaseStatusValues.B10);
		register(demoHandler8).from(DemoPayStatusValues.P6).to(DemoPayStatusValues.P7);
		register(demoHandler8).from(DemoPayStatusValues.P2).to(DemoPayStatusValues.P7);
		//demo19
		register(demoHandler9).from(DemoBaseStatusValues.B3).to(DemoBaseStatusValues.B4);
		register(demoHandler9).from(DemoBaseStatusValues.B2).to(DemoBaseStatusValues.B4);
		register(demoHandler9).at(DemoPayStatusValues.P6);
		register(demoHandler9).at(DemoPayStatusValues.P5);
		register(demoHandler9).at(DemoPayStatusValues.P7);
		//demo20
		register(demoHandler10).from(DemoBookStatusValues.T2).to(DemoBookStatusValues.T5);
		register(demoHandler10).from(DemoBookStatusValues.T3).to(DemoBookStatusValues.T5);
		register(demoHandler10).from(DemoBookStatusValues.T4).to(DemoBookStatusValues.T5);
		register(demoHandler10).at(DemoPayStatusValues.P6);
		register(demoHandler10).at(DemoPayStatusValues.P10);
		register(demoHandler10).at(DemoPayStatusValues.P8);
	}
	@Autowired
	DemoHandler1 demoHandler1;
	@Autowired
	DemoHandler2 demoHandler2;
	@Autowired
	DemoHandler3 demoHandler3;
	@Autowired
	DemoHandler4 demoHandler4;
	@Autowired
	DemoHandler5 demoHandler5;
	@Autowired
	DemoHandler6 demoHandler6;
	@Autowired
	DemoHandler7 demoHandler7;
	@Autowired
	DemoHandler8 demoHandler8;
	@Autowired
	DemoHandler9 demoHandler9;
	@Autowired
	DemoHandler10 demoHandler10;
	@Autowired
	DemoHandler11 demoHandler11;
	@Autowired
	DemoHandler12 demoHandler12;
	@Autowired
	DemoHandler13 demoHandler13;
	@Autowired
	DemoHandler14 demoHandler14;
	@Autowired
	DemoHandler15 demoHandler15;
	@Autowired
	DemoHandler16 demoHandler16;
	@Autowired
	DemoHandler17 demoHandler17;
	@Autowired
	DemoHandler18 demoHandler18;
	@Autowired
	DemoHandler19 demoHandler19;
	@Autowired
	DemoHandler20 demoHandler20;
	@Autowired
	DemoHandler21 demoHandler21;
	@Autowired
	DemoHandler22 demoHandler22;
	@Autowired
	DemoHandler23 demoHandler23;
	@Autowired
	DemoHandler24 demoHandler24;
	@Autowired
	DemoHandler25 demoHandler25;
	@Autowired
	DemoHandler26 demoHandler26;
	@Autowired
	DemoHandler27 demoHandler27;
	@Autowired
	DemoHandler28 demoHandler28;
	@Autowired
	DemoHandler29 demoHandler29;
	@Autowired
	DemoHandler30 demoHandler30;

}
