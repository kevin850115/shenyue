package com.local.kevin.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.local.kevin.dal.model.User;
import com.local.kevin.dal.persistence.KevinPersistence;
import com.taobao.util.CalendarUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author kevin
 */
@Controller
public class HomepageController {
    @Resource
    private KevinPersistence kevinPersistence;
	
	@RequestMapping("/check.htm")
	public String check(HttpServletRequest request, HttpServletResponse response, ModelMap result) throws ServletException, IOException {
		return "screen/check";
	}

	@RequestMapping("/index.htm")
	public String handleIndexRequest(HttpServletRequest request, HttpServletResponse response, ModelMap result) throws ServletException, IOException {
        return "redirect:index2.htm";
	}
	@RequestMapping("/showData.htm")
	public String showData(HttpServletRequest request, HttpServletResponse response, ModelMap result) throws ServletException, IOException {
		String json = request.getParameter("json");
		JSONObject obj = null;
		try{
			obj = JSON.parseObject(json);
		}catch(Throwable t){
			result.put("json",json);
		}
		Map<String,String> subObjs = new HashMap<String,String>();
		if ( obj != null ){
			Set<Entry<String, Object>> entrySet = obj.entrySet();
			for ( Entry<String,Object> ent:entrySet ){
				String value = ent.getValue().toString();
				try{
					JSONObject subObj = JSON.parseObject(value);
					//1.gmtModified,和gmtCreate的时间戳替换
					formatTimestamp("gmtModified",subObj);
					formatTimestamp("gmtCreate",subObj);
					if ( subObj != null ){
						subObjs.put(ent.getKey(), JSON.toJSONString(subObj, true));
					}
				}catch(Throwable t){
					
				}
			}
		}
		result.put("json",json);
		result.put("subObjs",subObjs);
		return "screen/showData";
	}
	private void formatTimestamp(String key,JSONObject objs){
		try{
			Long mo= (Long) objs.get(key);
			if ( mo != null ){
				Date d = new Date(mo);
				objs.put(key, CalendarUtil.toString(d, "yyyy-MM-dd HH:mm:ss.SSS"));
			}
		}catch(Throwable t){
			
		}
		
	}
	@RequestMapping("/indexOld.htm")
	public String handleIndexOldRequest(HttpServletRequest request, HttpServletResponse response, ModelMap result) throws ServletException, IOException {
		return "screen/index";
	}
	
	@RequestMapping("/index2.htm")
	public String handleIndex2Request(HttpServletRequest request, HttpServletResponse response, ModelMap result) throws ServletException, IOException, ParseException {
        User user = new User();
        user.setId(2);
        try {
            user = kevinPersistence.getUserById(user);
            result.put("user",user);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return "screen/index2";
	}

	@RequestMapping("/noPermission.htm")
	public String noPermissionRequest(HttpServletRequest request, HttpServletResponse response, ModelMap result) throws ServletException, IOException {
		return "screen/noPermission";
	}
	@RequestMapping("/err.htm")
	public String err(HttpServletRequest request, HttpServletResponse response, ModelMap result) throws ServletException, IOException {
		return "screen/err";
	}

}
