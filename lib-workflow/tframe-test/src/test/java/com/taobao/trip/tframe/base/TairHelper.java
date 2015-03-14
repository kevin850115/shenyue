package com.taobao.trip.tframe.base;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import com.alibaba.common.lang.StringUtil;

/**
 * 
 * @description tair�Ĺ����࣬����ע��namespace������key�Ȳ���
 * @version 1.0
 * @author zhanghei
 * @update 2011-6-23 ����09:46:48
 */
public class TairHelper {
    public static final String TAIR_KEYPREFIX = "ATO";
    public static final String SEPERATOR = "_";
    public static final String COMMON_NAMESPACE = "common";
    public static final String CORPNAMESPACE = "corp";

    public static Map<String, Integer> nameSpaceMap = new HashMap<String, Integer>();
    //�������������Ի��������ϻ���
    static {
        nameSpaceMap.put(COMMON_NAMESPACE, 229);
        nameSpaceMap.put(CORPNAMESPACE, 192);
    }

    public static String listAllNamespaces() {
        return JSONObject.fromObject(nameSpaceMap).toString();
    }

    public static int getNameSpaceByKey(String key) {
        return nameSpaceMap.get(key);
    }

    public static String genKey(String... params) {
        StringBuilder sb = new StringBuilder(TAIR_KEYPREFIX + SEPERATOR);
        for (String param : params) {
            sb.append(param).append(SEPERATOR);
        }
        String keyTmp = sb.toString();
        return keyTmp.substring(0, keyTmp.length() - 1);
    }

    public static String genUUID(String prefix) {
        StringBuffer key = new StringBuffer();
        UUID uid = UUID.randomUUID();
        key.append(prefix + "_").append(StringUtil.replaceChars(uid.toString(), "-", ""));
        return key.toString();
    }
}
