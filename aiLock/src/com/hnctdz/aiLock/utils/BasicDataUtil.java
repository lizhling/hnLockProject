package com.hnctdz.aiLock.utils;

import java.util.HashMap;
import java.util.Map;

/** 
 * @ClassName BasicDataUtil.java
 * @Author WangXiangBo 
 */
public class BasicDataUtil {
	private static BasicDataUtil instance = new BasicDataUtil();
	
	public static final String KEY_TYPE = "KEY_TYPE";
	public static final String ALARM_LEVEL = "ALARM_LEVEL";
	
	
	public static final Map<String, Integer> basicDataTagMap = new HashMap<String, Integer>();
	public static final Map<Integer,  Map<String, String>> basicDataTypeMap = new HashMap<Integer, Map<String, String>>();
	

	static {
		basicDataTagMap.put(KEY_TYPE, 1);
		basicDataTypeMap.put(1, new HashMap<String, String>(){{put("1","智能钥匙");put("2","门卡");put("3","身份证");put("4","手机");}});
		
		basicDataTagMap.put(ALARM_LEVEL, 4);
		basicDataTypeMap.put(4, new HashMap<String, String>(){{put("05","紧急告警");put("02","重要告警");put("04","一般告警");put("25","一般告警");put("58","一般告警");}});
	
	}
	
	public static String getBasicDataTypeName(String typeTag, Object typeCode){
		if(StringUtil.isNotEmpty(typeTag) && typeCode != null){
			Integer basiDateId = basicDataTagMap.get(typeTag);
			if(basiDateId != null){
				Map<String, String> types = basicDataTypeMap.get(basiDateId);
				return types.get(typeCode.toString());
			}
		}
		return null;
	}
	
	public static BasicDataUtil getInstance() {
		return instance;
	}
}
