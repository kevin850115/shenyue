package com.taobao.trip.qinchong;

import java.io.UnsupportedEncodingException;

public class TestCh {
	public static void main(String[] args) throws UnsupportedEncodingException {
		String b = "жа";
		System.out.println(b);
		byte[] bytes = b.getBytes("UTF-8");
		for ( byte c:bytes ){
			System.out.println(byte2bits(c));
		}
	}
	public static String byte2bits(byte b) {
		  int z = b; z |= 256;
		  String str = Integer.toBinaryString(z);
		  int len = str.length(); 
		  return str.substring(len-8, len);
		}
}
