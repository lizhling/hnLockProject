/*
 * FileName:     TokenResponse.java
 * @Description: 
 * Copyright (c) 2014 Sunrise Corporation.
 * #368, Guangzhou Avenue South, Haizhu District, Guangzhou
 * All right reserved.
 *
 * Modification  History:
 * Date           Author         Version         Discription
 * -----------------------------------------------------------------------------------
 * 2014-1-13   WangXiangBo        1.0              新建
 */
package com.hnctdz.aiLock.dto;

/** 
 * @ClassName TokenResponse.java
 * @Author WangXiangBo 
 * @Date 2014-1-13 上午11:15:28
 */
public class TokenResponse extends Response{
	
	private String token;//业务令牌

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	
}
