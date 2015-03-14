package com.taobao.trip.tframe.process;


/*
 * 枚举所有的Handler名称
 */
public enum TestHandlerName implements IHandlerName {
	Act1to2,
	ActStay1and2,
	ActStay1and2WithoutSave,
	ActStay1and2ExecException,
	ActStay1and2SaveException,
	ActStay1and2ExecNullException,
	Act2to3,
	Act3to5,
	ActAt4and5,
	ActAlways,
	TwoAct1to2,
	TwoAct1to2Sub1,
	TwoAct1to2Sub2,
	TwoActStay1and2,
	TwoAct2to3,
	TwoActBefore2,
	TwoActAfter2,
	TwoAct3to5,
	TwoActAt4and5,
	TwoActAlways,
	FailAct1to2,
	SAct1to2,
	SAct1to2Fail1,
	SAct1to2Fail2,
	MY,
	NONE;

	@Override
	public String getDesc() {
		return name();
	}

	@Override
	public String getUniqName() {
		return name();
	}

}
