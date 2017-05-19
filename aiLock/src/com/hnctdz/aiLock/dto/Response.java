/*
 * FileName:     Response.java
 * @Description: 
 * Copyright (c) 2013 Sunrise Corporation.
 * #368, Guangzhou Avenue South, Haizhu District, Guangzhou
 * All right reserved.
 *
 * Modification  History:
 * Date           Author         Version         Discription
 * -----------------------------------------------------------------------------------
 * 2013-12-17   WangXiangBo        1.0              新建
 */
package com.hnctdz.aiLock.dto;

/** 
 * @ClassName Response.java
 * @Author WangXiangBo 
 * @Date 2013-12-17 下午03:09:53
 */
public class Response {
	
	/**
	 * 结果代码
	 * 默认0为正常返回，有有错则是具体的错误代码
	 */
	private int resultCode = 0;
	
	/**
	 * 结果描述
	 */
	private String resultMessage = "";

	/**
	 * @return the resultCode
	 */
	public int getResultCode() {
		return resultCode;
	}

	
	/**
	 * @param resultCode the resultCode to set
	 */
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	
	/**
	 * @return the resultMessage
	 */
	public String getResultMessage() {
		return resultMessage;
	}

	
	/**
	 * @param resultMessage the resultMessage to set
	 */
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	
}

