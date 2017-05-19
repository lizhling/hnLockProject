package com.hnctdz.aiLock.utils;


import java.util.Calendar;
import java.util.Date;

import com.hnctdz.aiLock.utils.CommunCrypUtil;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName CommunCrypUtil.java
 * @Author WangXiangBo 
 */
public class CommunCrypUtil {
	private static String PASSWORD_CRYPT_KEY = "270F";
	private static byte[] KEY = dataToByte(PASSWORD_CRYPT_KEY);;
	private static int CMD_LEN = 73;
	
	public CommunCrypUtil(){
	}
	public static byte[] getCrypCommand(String commands){
		byte[] dataByte = CommunCrypUtil.dataToByte(commands);
		//将校验码预设为0，然后计算全部73字节的累加和，取低8位和0xAC异或
		int temp = 0;
		for(int i=0; i < CMD_LEN; i++){
			temp += (dataByte[i] & 0xFF);
		}
		
		dataByte[71] = (byte)(temp & 0xFF ^ 0xAC);
		
		System.out.println("发送原始数据:"+toHexStr(dataByte));
		dataByte = commEncryption(dataByte);
		System.out.println("发送加密数据:"+CommunCrypUtil.toHexStr(dataByte));
		return dataByte;
	}
	
	public static byte[] commEncryption(byte[] dataByte){
		for(int i=0; i < CMD_LEN; i++){
			dataByte[i] ^= (byte) (KEY[i%2] ^ 0xDE);
    	}
        return dataByte;
	}
	
	//16进制编码字符转成byte数组
	public static byte[] dataToByte(String data){
		byte[] daBy = null;
		if(StringUtil.isNotEmpty(data)){
			int length = data.length();
			daBy = new byte[length / 2];
			int forLength = (length / 2) + 1;
			for(int i=1; i < forLength; i++){
				int bgIndex = (i * 2) - 2;
				String str = data.substring(bgIndex, bgIndex + 2); 
				int num = Integer.valueOf(str, 16);
				daBy[i-1] = (byte)num;
			}
		}
		return daBy;
	}
	
	//转成16进制编码字符输出
	public static String toHexStr(byte[] dataByte){
    	byte[] hex = "0123456789ABCDEF".getBytes();
    	byte[] buff = new byte[2 * dataByte.length];
        for (int i = 0; i < dataByte.length; i++) {
            buff[2 * i] = hex[(dataByte[i] >> 4) & 0x0f];
            buff[2 * i + 1] = hex[dataByte[i] & 0x0f];
        }
        return new String(buff);
	}
	
	//字节转换成8位
    public static byte[] getBooleanArray(byte b) {  
        byte[] array = new byte[8];  
        for (int i = 7; i >= 0; i--) {  
            array[i] = (byte)(b & 1);  
            b = (byte) (b >> 1);  
        }  
        return array;
    }
	
	//设置时间 deviceNo:机号
	public String setLockTime(String deviceNo){
		Calendar cal = Calendar.getInstance();
        String week = "";
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
        	week = "00";
        }else{
        	week = "0" + w;
        }
        
        String date = DateUtil.getDateTime("yyMMddHHmmss", cal.getTime());
        
        String timeNr = date.substring(0, 4) + week + date.substring(4, date.length());
        
        return "EB"+deviceNo+"05000000"+timeNr + "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000EA";
	}
	
	//添加修改卡片权限 deviceNo：机号，cardId：卡ID
	public static String addOrUpdateCardPer(String deviceNo, String cardId) throws ErrorCodeException{
		Calendar cal = Calendar.getInstance();
		String years = cal.get(Calendar.YEAR) + "";
		int year = Integer.parseInt(years.substring(2, 4));
		int month = cal.get(Calendar.MONTH) +1;;
		int day = cal.get(Calendar.DATE);
		
		byte[] yearBt = CommunCrypUtil.getBooleanArray((byte)year);
		byte[] monthBt = CommunCrypUtil.getBooleanArray((byte)month);
		byte[] dayBt = CommunCrypUtil.getBooleanArray((byte)day);
		
		String byte1 = "" + monthBt[4] + monthBt[5] + yearBt[2] + yearBt[3] + yearBt[4] + yearBt[5] + yearBt[6] + yearBt[7];
		String byte2 = "" + monthBt[6] + monthBt[7] + 0 + dayBt[3] + dayBt[4] + dayBt[5] + dayBt[6] + dayBt[7];
		
		String bytes1 = Integer.toHexString(Integer.valueOf(byte1, 2));
		if(bytes1.length() < 2){
			bytes1 = "0" + bytes1;
		}
		String bytes2 = Integer.toHexString(Integer.valueOf(byte2, 2));
		if(bytes2.length() < 2){
			bytes2 = "0" + bytes2;
		}
		if(cardId.length() < 16){
			for(int i = cardId.length(); i < 16; i++){
				cardId = "0" + cardId;
			}
		}
		
		String cardInfo = cardId + bytes1 + bytes2 + "0000FF000000";
		
		return "EB" + deviceNo + "0B010000" + cardInfo + "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000EA";
	}
	
	public static void main(String[] args) throws ErrorCodeException {
	}
}
