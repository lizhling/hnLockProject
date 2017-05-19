package com.hnctdz.aiLockdm.utils;

import net.sf.json.JSONObject;


/** 
 * @ClassName ReturnCodeUtil.java
 * @Author WangXiangBo 
 */
public class ReturnCodeUtil {
	public static final String SUCCESS_RETURN_CODE = "00";
	public static final String ERROR_RETURN_CODE = "1";
	
	
	public static String success(String returnCode, String returnMessage){
		return "{\"returnCode\":\"" + returnCode + "\",\"returnMessage\":\"" + returnMessage + "\"}";
	}
	
	public static String exception(String returnCode){
		return "{\"returnCode\":\"" + returnCode + "\",\"returnMessage\":\"\"}";
	}
	
	public static String exception(String returnCode, String returnMessage){
		return "{\"returnCode\":\"" + returnCode + "\",\"returnMessage\":\"" + returnMessage + "\"}";
	}
	
	public static String getResultCode(String returnValue){
		JSONObject obj= JSONObject.fromObject(returnValue);
		String retcode = "";
		if (obj.containsKey("return")) {
			obj = obj.getJSONObject("return");
		} else if (obj.containsKey("root")) {
			obj = obj.getJSONObject("root");
		} else if (obj.containsKey("data")) {
			obj = obj.getJSONObject("data");
		}
		retcode = obj.getString("resultCode");
		
		return retcode; 
	}
	
}
