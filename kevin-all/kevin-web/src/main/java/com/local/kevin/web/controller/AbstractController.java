package com.local.kevin.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractController {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	private static String KV_ITEM_TYPE="KV";
}
