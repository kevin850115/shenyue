package com.taobao.trip.tframe.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;


public class JsonUtils {
    private static Logger log = LoggerFactory.getLogger(JsonUtils.class);
    
    /**
     * Json���л�����
     * 1.���л�Java���������е�field
     * 
     * ���ƣ�����������������л�ʧ��)
     * 1.����������
     * 2.��ѭ������
     * @param obj
     * @return
     */
    public static String jsonEncode(Object obj) {
        long tic = System.currentTimeMillis();
        try {
        	return JSON.toJSONString(obj);
        } catch (Throwable t) {
            log.error("JsonEncodeFailed!,class:"+obj.getClass().getName(),t);
            return null;
        } finally{
        	Long used = (System.currentTimeMillis()-tic);
            log.info("JsonEncode "+obj.getClass().getName()+" used "+used+"ms");
        }
    }
    
    public static <T> T jsonDecode(String str,Class<T> c) {
        long tic = System.currentTimeMillis();
        try {
            T ret = JSON.parseObject(str,c);
            return ret;
        } catch (Throwable t) {
            log.error("JsonDecodeFailed!,class:"+c.getName(),t);
            return null;
        } finally{
        	Long used = (System.currentTimeMillis()-tic);
            log.info("JsonDecode "+c.getName()+" used "+used+"ms");
        }
    }

}