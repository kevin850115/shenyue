package com.taobao.trip.tframe.monitor;

import java.util.UUID;


/**
 * 为每个请求生成一个唯一键，以确保在日志分析的时候区分不同的执行流
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
