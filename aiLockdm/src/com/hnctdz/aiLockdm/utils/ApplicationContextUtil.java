package com.hnctdz.aiLockdm.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class ApplicationContextUtil implements ApplicationContextAware {
	
	private static ApplicationContext context;//声明一个静态变量保存
	
	public void setApplicationContext(ApplicationContext contex) {
		ApplicationContextUtil.context = contex;
	}
	
	public static ApplicationContext getContext(){
		return context;
	}
}
