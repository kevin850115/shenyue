package com.taobao.trip.tframe.home.module.screen;

import com.alibaba.citrus.service.requestcontext.parser.ParameterParser;
import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.citrus.turbine.util.TurbineUtil;
import com.taobao.trip.qinchong.DemoC;
import com.taobao.trip.tframe.common.WebBase;

/**
 * 时光机接口，某航段某时间范围机票订单低价曲线
 * 
 */
public class Call extends WebBase {
	
	DemoC democClient;
	

	public void execute(Context context) {
		TurbineRunData rundata = TurbineUtil.getTurbineRunData(request);
		ParameterParser parameters = rundata.getParameters();
		int s = parameters.getInt("s");
		democClient.getA(s);
	}


}
