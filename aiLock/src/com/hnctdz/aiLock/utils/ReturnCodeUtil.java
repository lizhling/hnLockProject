package com.hnctdz.aiLock.utils;

import net.sf.json.JSONObject;

import com.hnctdz.aiLock.utils.encryption.DesCrypUtil;
import com.hnctdz.aiLock.utils.encryption.ZipUtils;

/** 
 * @ClassName ReturnCodeUtil.java
 * @Author WangXiangBo 
 */
public class ReturnCodeUtil {
	public static final String SUCCESS_RESULT_CODE = "00";
	public static final String ERROR_RESULT_CODE = "1";
	
	public static final Boolean DES_WAY = "des".equalsIgnoreCase(Constants.ENCIPHER_WAY);

	public static String success(String resultCode, String resultMessage){
		return DesCrypUtil.DESEncrypt("{\"resultCode\":\"" + resultCode + "\",\"resultMessage\":\"" + resultMessage + "\"}");
	}
	
	public static String success(String resultCode){
		return DesCrypUtil.DESEncrypt("{\"resultCode\":\"" + resultCode + "\",\"resultMessage\":\"\"}");
	}
	
	public static String exception(String resultCode, String resultMessage){
		return DesCrypUtil.DESEncrypt("{\"resultCode\":\"" + resultCode + "\",\"resultMessage\":\"" + resultMessage + "\"}");
	}
	
	public static String successTojsons(String resultJsons){
		return DesCrypUtil.DESEncrypt(resultJsons);
	}
	
	public static String getResultCode(String returnValue){
//		String lowerStr = returnValue.toLowerCase();
//		System.out.println(lowerStr);
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
	
	/**
	 * 加密
	 */ 
	public static String encryptData(String param){
		return DES_WAY ? DesCrypUtil.DESEncrypt(param):ZipUtils.compressAndEncodeBase64(param, Constants.CHARTSET_NAME);
	}
	
	/**
	 * 解密
	 */
	public static String decryptData(String param){
		return DesCrypUtil.DESDecrypt(param);
	}
	
	/**
	 * 加密
	 */ 
	public static String encryptData(String param,String desKey){
		return DES_WAY ? DesCrypUtil.DESEncrypt(param,desKey):ZipUtils.compressAndEncodeBase64(param, Constants.CHARTSET_NAME);
	}
	
	/**
	 * 解密
	 */
	public static String decryptData(String param,String desKey){
		return DES_WAY ? DesCrypUtil.DESDecrypt(param,desKey):ZipUtils.decompressAndDecodeBase64(param, Constants.CHARTSET_NAME);
	}
	
	public static void main(String[] args) {
		System.out.println(ReturnCodeUtil.success("00","AA0000000000000000000001001C804100F40E2257D0F016081549DA6393C744DAF55B1AC9C0CABBC5DB90AA|AA0000000000000000000001001B8046F40E2257D0F000160849DA6393C744DAF55B1AC9C0CABBC5DB85AA"));
		System.out.println(ReturnCodeUtil.decryptData("H4sIAAAAAAAAALvtUWu9ddu6fMGJ+lbFrmf4da84aj1Z4B3GqJC6JnpJo/gP5WP/jy89dVtrVq2GKkdQDozWXWn//cv22AIVp+yGFxNOmW5rSraZt2bDNJ3PvrPZhH/H7bLYHTTvQcK1gw++JEYyzdIIFG7cLlzwKWWPhqQMq/O/2+Zs8XcMb0SEoJub/+5S4a/VPVO0vrNGzgnaHsoYm9vM/Tr1mGjPnQ9u/VWNxnFP1y/87bCAw6pc9vkn05PT3K+3+fFXv2Rf+k2Lj4Nx0rr0dDvp2ZPWAgDSDJzW2AAAAA=="));
	}
}
