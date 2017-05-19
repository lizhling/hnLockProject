package com.hnctdz.aiLockdm.utils;


/** 
 * @ClassName RecordCommandUtil.java
 * @Author WangXiangBo 
 */
public class RecordCommandUtil {
	
	public static UnlockRecords getLockRecordInfo(UnlockRecords urs, byte[] recordByte, int recordHead){
		String recordCode = toHexStr(new byte[]{recordByte[recordHead]});//记录类型
		CommandTypeUtil.getInstance();
		String recordTpye = CommandTypeUtil.getLockRecordType(recordCode);
		urs.setRecordCode(recordCode);
		urs.setRecordTpye(recordTpye);
		
		byte[] keyCodeByte = new byte[8];
		for(int i = recordHead + 1; i < (recordHead + 1 + 8); i++){
			keyCodeByte[i - (recordHead + 1)] = recordByte[i];
		}
		urs.setKeyCode(toHexStr(keyCodeByte));//卡号（钥匙编号）
		
		byte[] timeBt1 = CommunCrypUtil.getBooleanArray(recordByte[recordHead + 9]);
		byte[] timeBt2 = CommunCrypUtil.getBooleanArray(recordByte[recordHead + 10]);
		byte[] timeBt3 = CommunCrypUtil.getBooleanArray(recordByte[recordHead + 11]);
		byte[] timeBt4 = CommunCrypUtil.getBooleanArray(recordByte[recordHead + 12]);
		
		String yearBts = "00" + timeBt1[2] + timeBt1[3] + timeBt1[4] + timeBt1[5] + timeBt1[6] + timeBt1[7];
		String monthBts = "0000" + timeBt1[0] + timeBt1[1] + timeBt2[0] + timeBt2[1];
		String dayBts = "000" + timeBt3[3] + timeBt3[4] + timeBt3[5] + timeBt3[6] + timeBt3[7];
		String hoursBts = "000" + timeBt4[3] + timeBt4[4] + timeBt4[5] + timeBt4[6] + timeBt4[7];
		String minutesBts = "00" + timeBt2[2] + timeBt2[3] + timeBt2[4] + timeBt2[5] + timeBt2[6] + timeBt2[7];
		String secondsBts = "00" + timeBt3[0] + timeBt3[1] + timeBt3[2] + timeBt4[0] + timeBt4[1] + timeBt4[2];
		
		String year = "20" + timeFormatting(Integer.valueOf(yearBts, 2));
		String month = timeFormatting(Integer.valueOf(monthBts, 2));
		String day = timeFormatting(Integer.valueOf(dayBts, 2));
		String hours = timeFormatting(Integer.valueOf(hoursBts, 2));
		String minutes = timeFormatting(Integer.valueOf(minutesBts, 2));
		String seconds = timeFormatting(Integer.valueOf(secondsBts, 2));
		
		urs.setUnlockTime(year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds);
		return urs;
	}

	public static LockStatusRecords getLockStatusInfo(byte lockStatusRecordByte){
		byte[] data = CommunCrypUtil.getBooleanArray(lockStatusRecordByte);
		
		LockStatusRecords lsr = new LockStatusRecords();
		lsr.setMenciStatus(data[7] + "");
		lsr.setBufangStatus(data[6] + "");
		lsr.setBaojingStatus(data[5] + "");
		lsr.setYuliuStatus(data[4] + "");
		lsr.setJixieyaoshiStatus(data[3] + "");
		lsr.setXiesheStatus(data[2] + "");
		lsr.setShangshuoStatus(data[1] + "");
		lsr.setMenguanhaoStatus(data[0] + "");
		//lsr.setRenyikaStatus(data[0] + "");
		return lsr;
	}
	
	public static String toHexStr(byte[] dataByte){
    	byte[] hex = "0123456789ABCDEF".getBytes();
    	byte[] buff = new byte[2 * dataByte.length];
        for (int i = 0; i < dataByte.length; i++) {
            buff[2 * i] = hex[(dataByte[i] >> 4) & 0x0f];
            buff[2 * i + 1] = hex[dataByte[i] & 0x0f];
        }
        return new String(buff);
	}
	
	public static String timeFormatting(Integer value){
		if(value < 10){
			return "0" + value;
		}
		return value.toString();
	}
	
	public static void main(String[] args) {
//		RecordCommandUtil.getLockRecordInfo(null);
		byte b = (byte)0xEA;
		System.out.println(b);
	}
}
