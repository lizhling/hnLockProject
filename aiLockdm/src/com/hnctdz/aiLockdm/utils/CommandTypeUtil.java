package com.hnctdz.aiLockdm.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CommandTypeUtil.java
 * @Author WangXiangBo
 */
public class CommandTypeUtil {
	private static CommandTypeUtil instance = new CommandTypeUtil();

	private static Map<String, String> lockRecordTypeMap = new HashMap<String, String>();
	private static Map<String, String> lockAlarmTypeMap = new HashMap<String, String>();

	public static int regularReadingTime = 3;
	
	static {
		regularReadingTime = CommonUtil.getIntProperty("regularReadingTime", 3);
		
		System.out.println("-------------------------------------初始化-------------------------------------");
		lockRecordTypeMap.put("00", "用户进门");
		lockRecordTypeMap.put("05", "非法入侵");
//		lockRecordTypeMap.put("10", "限制状态试图进门");
//		lockRecordTypeMap.put("15", "超过时效的卡出门失败");
//		lockRecordTypeMap.put("20", "删除母卡删卡");
//		lockRecordTypeMap.put("01", "遥控开锁");
		lockRecordTypeMap.put("06", "远程开锁");
//		lockRecordTypeMap.put("11", "用户出门");
//		lockRecordTypeMap.put("16", "非法卡试图出门");
		lockRecordTypeMap.put("25", "门未关好报警");
		lockRecordTypeMap.put("02", "火警联动触发自动开锁");
//		lockRecordTypeMap.put("07", "禁行时段用户进门失败");
//		lockRecordTypeMap.put("12", "门外自助查询权限");
//		lockRecordTypeMap.put("17", "限制状态试图出门");
		lockRecordTypeMap.put("29", "心跳包推送");
//		lockRecordTypeMap.put("03", "密码开锁");
//		lockRecordTypeMap.put("08", "非法卡试图进门");
//		lockRecordTypeMap.put("13", "门内自助查询权限");
//		lockRecordTypeMap.put("18", "卡+密码开门");
		lockRecordTypeMap.put("04", "钥匙开门");
//		lockRecordTypeMap.put("09", "超过时效的卡进门失败");
//		lockRecordTypeMap.put("14", "禁行时段用户出门失败");
		lockRecordTypeMap.put("19", "出门按钮开门");
		lockRecordTypeMap.put("30", "APP蓝牙开门");
		
		lockAlarmTypeMap.put("05", "非法入侵");
		lockAlarmTypeMap.put("25", "门未关好报警");
		lockAlarmTypeMap.put("02", "火警联动触发自动开锁");
		lockAlarmTypeMap.put("04", "机械钥匙技术性开锁");
	}
	
	public static String getLockRecordType(String typeCode){
		return lockRecordTypeMap.get(typeCode);
	}
	
	public static String getLockAlarmType(String typeCode){
		return lockAlarmTypeMap.get(typeCode);
	}

	public static CommandTypeUtil getInstance() {
		return instance;
	}
}
