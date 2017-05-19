package com.hnctdz.aiLockdm.utils;


/** 
 * @ClassName CommunCrypUtil.java
 * @Author WangXiangBo 
 */
public class CommunCrypUtil {
	private static String PASSWORD_CRYPT_KEY = "270F";
	private static byte[] KEY = dataToByte(PASSWORD_CRYPT_KEY);;
	public static int CMD_LEN = 73;
	
	public CommunCrypUtil(){
	}
	
	public static byte[] getCrypCommand(String commands){
		System.out.println(DateUtil.getDateMs()+"发送报文:"+commands);
		byte[] dataByte = CommunCrypUtil.dataToByte(commands);
		
		int temp = 0;
		for(int i=0; i < CMD_LEN; i++){
			temp += (dataByte[i] & 0xFF);
		}
		
		dataByte[71] = (byte)(temp & 0xFF ^ 0xAC);
		
		dataByte = commEncryption(dataByte);
//		System.out.println(DateUtil.getDateMs()+"加密报文:"+toHexStr(dataByte));
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
//		System.out.println(dataByte[0]);
    	byte[] hex = "0123456789ABCDEF".getBytes();
    	byte[] buff = new byte[2 * dataByte.length];
        for (int i = 0; i < dataByte.length; i++) {
            buff[2 * i] = hex[(dataByte[i] >> 4) & 0x0f];
            buff[2 * i + 1] = hex[dataByte[i] & 0x0f];
        }
//      System.out.println(new String(buff)); //16进制编码
//      System.out.println(buff[0]); // ASCII码
        
//    	String buff = "";
//		for (int i = 0; i < dataByte.length; i++) {
//			if(dataByte[i] < 0){
//				buff+= Integer.toHexString((256 + dataByte[i]));
//			}else{
//				buff+= Integer.toHexString(dataByte[i]);
//			}
//		}
        return new String(buff);
	}
	
	//转成8位字节
    public static byte[] getBooleanArray(byte b) {  
        byte[] array = new byte[8];  
        for (int i = 7; i >= 0; i--) {  
            array[i] = (byte)(b & 1);  
            b = (byte) (b >> 1);  
        }  
        return array;
    }
	
	public static void main(String[] args) {
//		byte b = 0x00;
//		byte[] a =  getBooleanArray(b);
//		String s = "EB00010500000015080413162158000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000EA";
//		System.out.println(s.substring(6, 8));
//		String data = "12d1fbd653d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9d1f9f513";
//		byte[] dataByte = dataToByte(data);
////		for(int i = 0;i<dataByte.length;i++){
////			System.out.println(dataByte[i]);
////		}
//		
////		byte s = (byte)0xFF;
////		System.out.println(s);
//		byte[] daBy = commEncryption(dataByte);
//		System.out.println(toHexStr(daBy));
	}
}
