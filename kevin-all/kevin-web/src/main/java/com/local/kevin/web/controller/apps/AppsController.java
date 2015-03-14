package com.local.kevin.web.controller.apps;

import com.local.kevin.web.controller.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("apps")
public class AppsController extends AbstractController
{
	private static final String LIST_APP = "screen/apps/list";
	private static final String APP_DETAIL = "screen/apps/detail";
	
	@RequestMapping("list.htm")
    public String listApp(final HttpServletRequest request, ModelMap result)
    {
    	return LIST_APP;
    }
	@RequestMapping("detail.htm")
    public String appDetail(final HttpServletRequest request, ModelMap result)
    {
    	return APP_DETAIL;
    }

	
}
