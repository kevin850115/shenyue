package com.taobao.trip.tframe.demo.constant;

import com.taobao.trip.tframe.process.IHandlerName;

public enum DemoHandlerName implements IHandlerName {
	H1,
	H2,
	H3,
	H4,
	H5,
	H6,
	H7,
	H8,
	H9,
	H10,
	H11,
	H12,
	H13,
	H14,
	H15,
	H16,
	H17,
	H18,
	H19,
	H20,
	H21,
	H22,
	H23,
	H24,
	H25,
	H26,
	H27,
	H28,
	H29,
	H30;

	@Override
	public String getDesc() {
		return name();
	}

	@Override
	public String getUniqName() {
		return name();
	}
}
