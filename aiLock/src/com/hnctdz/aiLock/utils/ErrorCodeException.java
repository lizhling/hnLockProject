package com.hnctdz.aiLock.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/** 
 * @ClassName ErrorCodeException.java
 * @Author WangXiangBo 
 */
public class ErrorCodeException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private String errorCause;
	
	public ErrorCodeException(String msg){
		super(msg);
		this.setErrorCause(msg);
	}
	
	public ErrorCodeException(String msg, String errorCause) {
		this(msg);
		this.setErrorCause(errorCause); // 重置errorCause;
	}

//	public ErrorCodeException(String msg, Map<String, String> map){
//		super(msg);
//		
//		String e = ErrorCode.fillContext2TranslateStr(msg, map);
//		
//		this.setErrorCause(e);
//	}
	
	public static String getExceInfo(Exception e){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
	
	public String getErrorCause() {
		return errorCause;
	}

	public void setErrorCause(String errorCause) {
		this.errorCause = errorCause;
	}
}
