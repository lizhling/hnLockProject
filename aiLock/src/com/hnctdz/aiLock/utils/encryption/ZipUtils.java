package com.hnctdz.aiLock.utils.encryption;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.ReturnCodeUtil;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ZipUtils{
	
	public static byte[] bASE64Encoder(String param,String is_new_version){
		if(is_new_version!=null && is_new_version.length()!=0 && is_new_version.equals("1")){
			AES mAes = new AES();
			String enString = "";
			try {
				enString = mAes.encrypt(param.getBytes());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return enString.getBytes();
		} else {
			String sb = new BASE64Encoder().encode(param.getBytes());
			return sb.getBytes();
		}
	}
	
	public static byte[] bASE63Decoder(String base64,String is_new_version){
		if(is_new_version!=null && is_new_version.length()!=0 && is_new_version.equals("1")){
		    AES mAes = new AES();
			String deString = "";
			try {
				deString = mAes.decrypt(base64);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return deString.getBytes();
		} else {
			BASE64Decoder decode = new BASE64Decoder();
		    byte[] b = {};
			try {
				b = decode.decodeBuffer(base64);
			} catch (IOException e) {
				e.printStackTrace();
			}
		    return b;
		}
		
	}
	
	/**
	 * 加密压缩
	 * @param content 需要加密压缩的字符串
	 * @param chartSetName 字符编码
	 * @return 加密压缩后的字符串
	 */
	public static String compressAndEncodeBase64(String content,String chartSetName){
		byte[] source=content.getBytes();
		String result=Base64_bak_20140620.encodeBytes(source, Base64_bak_20140620.GZIP,chartSetName);
		return result;
	}
	
	/**
	 * 解压解密
	 * @param content 需要解压解密的字符串
	 * @param chartSetName 字符编码
	 * @return 解压解密后的字符串
	 */
	public static String decompressAndDecodeBase64(String content,String chartSetName){
		byte[] bytes=Base64_bak_20140620.decode(content, Base64_bak_20140620.GZIP,chartSetName);
		String result;
		try {
			result=new String(bytes, chartSetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			result=new String(bytes);
		}
		return result;
	}
	

	//	
	 public static void main(String[] args) {
	 
	 String s="model=12345678901&imei=123456&accept=1&recmmd=2";
//	 String ss=compressAndEncodeBase64(s, LocaleMessage.getChartSetName());
	 String ss= ReturnCodeUtil.DES_WAY ? DesCrypUtil.DESEncrypt(s):ZipUtils.compressAndEncodeBase64(s, Constants.CHARTSET_NAME);
	 System.out.println("ENCODE：" + ss);
//	 // System.out.println("=============================:" );
	  String s2 =
	 decompressAndDecodeBase64(ss,"UTF-8");
	  System.out.println("DECODE:" + s2);
//			
//	 // String str = "1380827219920130729183004749658tanghai";
//	 // System.out.println(str.substring(31));
		 
		
	 }
	


}
