package com.taobao.trip.tframe.json;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.taobao.trip.tframe.tool.JsonUtils;

public class TestJson {
	
	static class B{
		private Integer b;
		private Integer c;
		private B self;
		private C cc;
		public Integer getB() {
			return b;
		}
		public void setB(Integer b) {
			this.b = b;
		}
		public Integer getC() {
			return c;
		}
		public void setC(Integer c) {
			this.c = c;
		}
		public void setSelf(B self) {
			this.self = self;
		}
		public B getSelf() {
			return self;
		}
		public void setCc(C cc) {
			this.cc = cc;
		}
		public C getCc() {
			return cc;
		}
	}
	public static class C{
		private String abcd;
		private B b;

		public void setAbcd(String abcd) {
			this.abcd = abcd;
		}

		public String getAbcd() {
			return abcd;
		}

		public void setB(B b) {
			this.b = b;
		}

		public B getB() {
			return b;
		}
	}

	@Test
	public void test_循环依赖_自依赖处理(){
		B b = new B();
		C c = new C();
		c.setAbcd("abcdefg");
		b.setB(22);
		c.setB(b);
		b.setSelf(b);
		b.setCc(c);
		b.setC(11);
		String text = JsonUtils.jsonEncode(b);
		System.out.println(text);
		B e_b= JsonUtils.jsonDecode(text,B.class);
		assertTrue(e_b.getSelf()==e_b);
		assertTrue(e_b.getB()==22);
		assertTrue(e_b.getC()==11);
		assertTrue(e_b.getCc().getB()==e_b);
	}
	@Test
	public void test_异常(){
		try{
			Throwable t = new NullPointerException("异常");
			throw t;
		}catch(Throwable t){
			String tt = JsonUtils.jsonEncode(t);
			System.out.println(tt);
		}
		
	}
}
