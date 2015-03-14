package com.taobao.trip.tframe.monitor;

import java.util.UUID;


/**
 * Ϊÿ����������һ��Ψһ������ȷ������־������ʱ�����ֲ�ͬ��ִ����
 * @author leconte
 *
 */
public class UniqRequest {
    private static ThreadLocal<String> id = new ThreadLocal<String>();
    
    public static String getId(){
    	if ( id.get() == null ){
    		id.set(UUID.randomUUID().toString());
    	}
    	return id.get();
    }
    
    public static void clearId(){
    	id.set(null);
    }
    
    public static void main(String[] args) {
		System.out.println(UniqRequest.getId());
		System.out.println(UniqRequest.getId());
		System.out.println(UniqRequest.getId());
		System.out.println(UniqRequest.getId());
	}

}
