package com.hnctdz.aiLock.utils;

import java.util.HashMap;
import java.util.Map;

import com.hnctdz.aiLock.utils.CommunCrypUtil;


/** 
 * @ClassName ResponseCommandUtil.java
 * @Author WangXiangBo 
 */
@SuppressWarnings("serial")
public class ResponseCommandUtil {
	private static ResponseCommandUtil instance = new ResponseCommandUtil();
	
	public static final String SUCCESS_COMMAND = "AA";
	public static final String SUCCESS_FAILED = "55";
	
	//public static final String ALARM_RECORD_CODE = "'05','10','15','16','25','02','07','17','08','04','09','14','58'";
	public static final String ALARM_RECORD_CODE = "'05','25','02','04','58'";
	//各告警级别包涵告警类型编码Map
	public static final Map<String, String[]> alarmLevelCodesMap = new HashMap<String, String[]>();
	
	//门锁记录分类（1：记录类；2：告警类）
	public static final Map<Integer, String> recordsTypeMap = new HashMap<Integer, String>();
	
	//电机锁门锁状态
	private static Map<Integer, String> lockStateTypeMap = new HashMap<Integer, String>();
	private static Map<Integer, Map<Integer, String>> lockStateMap = new HashMap<Integer, Map<Integer, String>>();
	
	//执手锁门锁状态
	private static Map<Integer, String> handLockStateTypeMap = new HashMap<Integer, String>();
	private static Map<Integer, Map<Integer, String>> handLockStateMap = new HashMap<Integer, Map<Integer, String>>();
	
	//扩张状态类型Map
	private static Map<Integer, String> lockExpStateTypeMap = new HashMap<Integer, String>();
	private static Map<Integer, Map<Integer, String>> lockExpStateMap = new HashMap<Integer, Map<Integer, String>>();
	
	//无源钥匙记录类型Map
	private static Map<String, String> smartKeyRecordTypeMap =  new HashMap<String, String>();
	
	/** 远程开门 */
	public static final String REMOTE_UNLOCK_RECORD_CODE = "06";
	/** 蓝牙开门 */
	public static final String BLUETOOTH_UNLOCK_RECORD_CODE = "30";
	
	//有源锁记录类型Map
	private static Map<String, String> passiveLockRecordMap =  new HashMap<String, String>();
	
	static {
		System.out.println("-------------------------------------初始化-------------------------------------");
		alarmLevelCodesMap.put("1", new String[]{"05"});
		alarmLevelCodesMap.put("2", new String[]{"02"});
		alarmLevelCodesMap.put("3", new String[]{"04","25","58"});
		
		//电机锁门锁状态类型Map
		lockStateTypeMap.put(0, "门磁状态");
		lockStateTypeMap.put(1, "布防状态");
		lockStateTypeMap.put(2, "报警状态");
//		lockStateTypeMap.put(3, "");
		lockStateTypeMap.put(45, "锁舌位置");
		lockStateTypeMap.put(6, "上锁不成功标志");
		lockStateTypeMap.put(7, "门未关好提醒标志");
		
		//电机锁门锁状态类型状态明细描述
		lockStateMap.put(0, new HashMap<Integer, String>(){{put(0,"门关闭");put(1,"门打开");}});
		lockStateMap.put(1, new HashMap<Integer, String>(){{put(0,"正常状态");put(1,"布防状态 禁止刷卡开门");}});
		lockStateMap.put(2, new HashMap<Integer, String>(){{put(0,"门锁无报警");put(1,"AL端子处于输出报警状态");}});
		lockStateMap.put(3, null);
		lockStateMap.put(45, new HashMap<Integer, String>(){{put(0,"锁舌上锁不到位");put(1,"上锁到位");
															 put(10,"全开到位");put(11,"锁舌传感器异常");}});
		lockStateMap.put(6, new HashMap<Integer, String>(){{put(0,"门锁正常");put(1,"上锁失败");}});
		lockStateMap.put(7, new HashMap<Integer, String>(){{put(0,"无门未关好状况");put(1,"有门未关好的情况");}});
		
		
		
		//执手锁门锁状态类型Map
		handLockStateTypeMap.put(8, "门的状态");
		handLockStateTypeMap.put(7, "门磁状态");
//		handLockStateTypeMap.put(6, "布防状态");
//		handLockStateTypeMap.put(5, "报警状态");
//		handLockStateTypeMap.put(4, "");
//		handLockStateTypeMap.put(3, "机械钥匙");
		handLockStateTypeMap.put(2, "锁舌状态");
//		handLockStateTypeMap.put(1, "上锁不成功标志");
//		handLockStateTypeMap.put(0, "门未关好状态");
		
		//执手锁门锁状态类型状态明细描述
		handLockStateMap.put(8, new HashMap<Integer, String>(){{put(0,"门已关好");put(1,"门已打开");put(2,"门未关好");}});
		handLockStateMap.put(7, new HashMap<Integer, String>(){{put(0,"门磁关闭");put(1,"门磁打开");}});
		handLockStateMap.put(6, new HashMap<Integer, String>(){{put(0,"正常状态");put(1,"布防状态 禁止刷卡开门");}});
		handLockStateMap.put(5, new HashMap<Integer, String>(){{put(0,"无报警");put(1,"有报警");}});
		handLockStateMap.put(4, null);
		handLockStateMap.put(3, new HashMap<Integer, String>(){{put(0,"未使用");put(1,"钥匙开门");}});
		handLockStateMap.put(2, new HashMap<Integer, String>(){{put(0,"锁舌伸出");put(1,"锁舌缩回");}});
		handLockStateMap.put(1, new HashMap<Integer, String>(){{put(0,"门锁正常");put(1,"上锁失败");}});
		handLockStateMap.put(0, new HashMap<Integer, String>(){{put(0,"门已关好");put(1,"门未关好");}});
		
		
		//扩张状态类型Map
		lockExpStateTypeMap.put(7, "任意卡开门状态");
		//扩张状态类型状态明细描述
		lockExpStateMap.put(7, new HashMap<Integer, String>(){{put(0,"禁止任意卡开门");put(1,"允许任意卡开门");}});
		
		//智能钥匙记录类型
		smartKeyRecordTypeMap.put("33", "智能钥匙开门");
		smartKeyRecordTypeMap.put("34", "远程授权开门");//远程授权开锁
        smartKeyRecordTypeMap.put("35", "开门无权限");//预留记录类型
        smartKeyRecordTypeMap.put("36", "开门无权限");//用户码错误
        smartKeyRecordTypeMap.put("37", "开门无权限");//未到有效期开锁
        smartKeyRecordTypeMap.put("38", "开门无权限");//超过有效期开锁
        smartKeyRecordTypeMap.put("39", "开门无权限");//开锁密码错误
        smartKeyRecordTypeMap.put("3A", "开门无权限");
        smartKeyRecordTypeMap.put("3B", "挂失钥匙开门");//钥匙已挂失
        
        passiveLockRecordMap.put("06", "远程开门");
        passiveLockRecordMap.put("30", "APP蓝牙开门");
	}
	
	public static String getHandLockStateInfo(String command){
		StringBuffer info = new StringBuffer();
		byte[] by = CommunCrypUtil.dataToByte(command.substring(6, 8));
		byte[] data = CommunCrypUtil.getBooleanArray(by[0]);
		for(int i = 0; i < 8; i++){
			if(handLockStateTypeMap.get(i) != null){
				info.append(handLockStateTypeMap.get(i)).append("：")
					.append(handLockStateMap.get(i).get((int)data[i])).append(";<br>");
			}
		}
		int mendezhuangtai = 0;
		if(data[7] == 0 && data[2] == 0){
			mendezhuangtai = 0;
		}else if(data[7] == 1 && data[2] == 1){
			mendezhuangtai = 1;
		}else {
			mendezhuangtai = 2;
		}
		
		info.append(handLockStateTypeMap.get(8)).append("：")
			.append(handLockStateMap.get(8).get(mendezhuangtai)).append(";<br>");
		
		return info.toString();
	}
	

	public static String getLockStateInfo(String command){
		StringBuffer info = new StringBuffer();
		byte[] by = CommunCrypUtil.dataToByte(command.substring(4, 6));
		byte[] data = CommunCrypUtil.getBooleanArray(by[0]);
		for(int i = 0; i < 8; i++){
			if(lockStateTypeMap.get(i) != null){
				info.append(lockStateTypeMap.get(i)).append("：")
				.append(lockStateMap.get(i).get((int)data[i])).append(";<br>");
			}
		}
		
		return info.toString();
	}
	
	public static String getSmartKeyRecordType(String typeCode){
		return smartKeyRecordTypeMap.get(typeCode);
	}
	
	public static String[] getAlarmLevelCodes(String typeCode){
		return alarmLevelCodesMap.get(typeCode);
	}
	
	public static String getPassiveLockRecord(String typeCode){
		return passiveLockRecordMap.get(typeCode);
	}

	public static ResponseCommandUtil getInstance() {
		return instance;
	}
}
