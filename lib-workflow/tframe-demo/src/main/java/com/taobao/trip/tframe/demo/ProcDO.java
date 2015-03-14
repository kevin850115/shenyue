package com.taobao.trip.tframe.demo;

import java.util.Date;
import java.util.Random;

import org.apache.commons.lang.time.DateUtils;

import com.taobao.trip.tframe.process.DataModel;


public class ProcDO implements DataModel{
	private static Integer cachedId=0;
	private static Date timeout;
	private long id;
	int status;
	int payStatus;
	private int type;
	private int bookStatus;
	public ProcDO(){
		if ( timeout == null || timeout.before(new Date())){
			Random r = new Random();
			cachedId = Math.abs(r.nextInt());
			timeout = DateUtils.addSeconds(new Date(), 20);
		}
		id = cachedId;
	}
	public static void main(String[] args) {
		System.out.println((new ProcDO()).getId());
		System.out.println((new ProcDO()).getId());
		System.out.println((new ProcDO()).getId());
		System.out.println((new ProcDO()).getId());
		System.out.println((new ProcDO()).getId());
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}
	String buyer;
	String seller;
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public void setBookStatus(int bookStatus) {
		this.bookStatus = bookStatus;
	}
	public int getBookStatus() {
		return bookStatus;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}
}
