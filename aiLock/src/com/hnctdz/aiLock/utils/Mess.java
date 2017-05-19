package com.hnctdz.aiLock.utils;

import java.util.Calendar;
import java.util.Date;


/**
 * @ClassName Mess.java
 * @Author WangXiangBo
 */
public class Mess {
	private static final String REQUEST_LOCKDM_URL = Constants.REQUEST_LOCKDM_URL;
	private String moduleCode;

	private static final String EB = "EB";
	private String deviceNo;
	private String mingLingMa;
	private String zhuangTaiMa;
	private String mingLingXuHao;
	private String neiRong = "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
	private String jiaoYanMa = "00";
	private static final String EA = "EA";
	
	
	public String getMessInfo(){
		return EB + deviceNo + mingLingMa + zhuangTaiMa + mingLingXuHao + neiRong + jiaoYanMa + EA;
	}
	
	public String getExpressMessInfo(){
		return EB + deviceNo + neiRong + EA;
	}
	
	//远程开锁
	public String remoteUnlock(){
		this.setMingLingMa("07");
		this.setZhuangTaiMa("00");
		this.setMingLingXuHao("0000");
		StringBuffer url = new StringBuffer();
		url.append(REQUEST_LOCKDM_URL).append("sendLockCommand?moduleIp=")
		   .append(this.getModuleCode()).append("&commands=").append(this.getMessInfo());
		return url.toString();
	}
	
	//远程加密指令
	public String remoteUnlockEncryption(){
		this.setMingLingMa("07");
		this.setZhuangTaiMa("00");
		this.setMingLingXuHao("0000");
		return this.getMessInfo();
	}
	
	//获取门锁状态
	public String getLockState(){
		this.setNeiRong("0000000000000000000000000000000000");
		StringBuffer url = new StringBuffer();
		url.append(REQUEST_LOCKDM_URL).append("sendExpressCommand?moduleIp=")
		   .append(this.getModuleCode()).append("&commands=").append(this.getExpressMessInfo());
		return url.toString();
	}
	
	//设置时间
	public String setLockTime(){
		this.setMingLingMa("05");
		this.setZhuangTaiMa("00");
		this.setMingLingXuHao("0000");
		
		Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String week = "";
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
        	week = "00";
        }else{
        	week = "0" + w;
        }
        
        String date = DateUtil.getDateTime("yyMMddHHmmss", cal.getTime());
        
        String timeNr = date.substring(0, 4) + week + date.substring(4, date.length());
        
		this.setNeiRong(timeNr + "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
		StringBuffer url = new StringBuffer();
		url.append(REQUEST_LOCKDM_URL).append("sendLockCommand?moduleIp=")
		   .append(this.getModuleCode()).append("&commands=").append(this.getMessInfo());
		return url.toString();
	}
	
	//添加修改卡片权限
	public String addOrUpdateCardPer(String cardId, Date dateTime, boolean wayTemporary, int number) throws ErrorCodeException{
		if(StringUtil.isEmpty(cardId) || null == dateTime){
			throw new ErrorCodeException("添加权限参数错误！");
		}
		
		this.setMingLingMa("0B");
		this.setZhuangTaiMa("01");
		this.setMingLingXuHao("0000");
		
		String termTime = DateUtil.getDateTime("yyyy-MM-dd HH",dateTime);
		int year = Integer.parseInt(termTime.substring(2, 4));
		int month = Integer.parseInt(termTime.substring(5, 7));
		int day = Integer.parseInt(termTime.substring(8, 10));
		//String hour = termTime.substring(11, 13);
		
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
		
		String temporarySign = "FF";
		if(wayTemporary){
			if(number < 1 || number > 15){
				throw new ErrorCodeException("临时卡开锁次数设置错误（需在1-15之间）！");
			}
			byte[] numberBt = CommunCrypUtil.getBooleanArray((byte)number);
			String byte3 = "1101" + numberBt[4] + numberBt[5] + numberBt[6] + numberBt[7];
			temporarySign = Integer.toHexString(Integer.valueOf(byte3, 2));
		}
		
		if(cardId.length() < 16){
			for(int i = cardId.length(); i < 16; i++){
				cardId = "0" + cardId;
			}
		}
		
		String cardInfo = cardId + bytes1 + bytes2 + "0000" + temporarySign + "000000";
		this.setNeiRong(cardInfo + "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
		
		StringBuffer url = new StringBuffer();
		url.append(REQUEST_LOCKDM_URL).append("sendLockCommand?moduleIp=")
		   .append(this.getModuleCode()).append("&commands=").append(this.getMessInfo());
		return url.toString();
	}
	
	//删除卡片权限
	public String deleteCardPer(String cardId) throws ErrorCodeException{
		if(StringUtil.isEmpty(cardId)){
			throw new ErrorCodeException("删除权限参数错误！");
		}
		
		this.setMingLingMa("0C");
		this.setZhuangTaiMa("00");
		this.setMingLingXuHao("0000");
		this.setNeiRong(cardId + "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
		StringBuffer url = new StringBuffer();
		url.append(REQUEST_LOCKDM_URL).append("sendLockCommand?moduleIp=")
		   .append(this.getModuleCode()).append("&commands=").append(this.getMessInfo());
		return url.toString();
	}
	
	//删除所有卡片权限
	public String deleteAllCardPer() throws ErrorCodeException{
		this.setMingLingMa("DB");
		this.setZhuangTaiMa("00");
		this.setMingLingXuHao("0000");
		StringBuffer url = new StringBuffer();
		url.append(REQUEST_LOCKDM_URL).append("sendLockCommand?moduleIp=")
		   .append(this.getModuleCode()).append("&commands=").append(this.getMessInfo());
		return url.toString();
	}
	
	//修改通信密码
	public String updateCommunKey() throws ErrorCodeException{
		this.setMingLingMa("F1");
		this.setZhuangTaiMa("00");
		this.setMingLingXuHao("0000");
		this.setNeiRong(Integer.toHexString(Constants.COLUMN).toUpperCase()+ "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
		StringBuffer url = new StringBuffer();
		url.append(REQUEST_LOCKDM_URL).append("sendLockCommand?moduleIp=")
		   .append(this.getModuleCode()).append("&commands=").append(this.getMessInfo()).append("&communKey=9999");
		return url.toString();
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public String getMingLingMa() {
		return mingLingMa;
	}

	public void setMingLingMa(String mingLingMa) {
		this.mingLingMa = mingLingMa;
	}

	public String getZhuangTaiMa() {
		return zhuangTaiMa;
	}

	public void setZhuangTaiMa(String zhuangTaiMa) {
		this.zhuangTaiMa = zhuangTaiMa;
	}

	public String getMingLingXuHao() {
		return mingLingXuHao;
	}

	public void setMingLingXuHao(String mingLingXuHao) {
		this.mingLingXuHao = mingLingXuHao;
	}

	public String getNeiRong() {
		return neiRong;
	}
	
	public void setNeiRong(String neiRong) {
		if(StringUtil.isNotEmpty(neiRong)){
			this.neiRong = neiRong;
		}
	}
	
	public String getJiaoYanMa() {
		return jiaoYanMa;
	}

	public void setJiaoYanMa(String jiaoYanMa) {
		this.jiaoYanMa = jiaoYanMa;
	}

}
