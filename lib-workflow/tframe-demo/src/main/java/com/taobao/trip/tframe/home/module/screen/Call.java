package com.taobao.trip.tframe.home.module.screen;

import com.alibaba.citrus.service.requestcontext.parser.ParameterParser;
import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.citrus.turbine.util.TurbineUtil;
import com.taobao.trip.qinchong.DemoC;
import com.taobao.trip.tframe.common.WebBase;

/**
 * ʱ����ӿڣ�ĳ����ĳʱ�䷶Χ��Ʊ�����ͼ�����
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
