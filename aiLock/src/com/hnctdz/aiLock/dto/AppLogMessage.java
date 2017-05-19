package com.hnctdz.aiLock.dto;

import com.google.gson.annotations.Expose;


/** 
 * @ClassName AppLogMessage.java
 * @Author WangXiangBo 
 */
public class AppLogMessage {
	@Expose
	private String[] smartKeyLog;

	public String[] getSmartKeyLog() {
		return smartKeyLog;
	}

	public void setSmartKeyLog(String[] smartKeyLog) {
		this.smartKeyLog = smartKeyLog;
	}
}
