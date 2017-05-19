package com.hnctdz.aiLock.utils.encryption;


import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


/** 
 * @ClassName DesCrypUtil.java
 * @Author WangXiangBo 
 */
public class DesCrypUtil {
	
    /** 加密、解密key. */
    private static String PASSWORD_CRYPT_KEY = null;
    /** 加密算法,可用 DES,DESede,Blowfish. */
    private final static String ALGORITHM = "DES";
    private final static String ALGORITHM_FORMATE = "DES/ECB/PKCS5Padding";
    
    
    /**
     * 对用DES加密过的数据进行解密.
     * @param data DES加密数据
     * @return 返回解密后的数据, 如果传入为null返回null
     */
    public final static String DESDecrypt(String data) {
    	String result = "";
    	try{
    		if (PASSWORD_CRYPT_KEY == null) {
        		PASSWORD_CRYPT_KEY = "J@c^kT~!";
        	}
        	
            if (data == null || data.length() == 0) {
            	return null;
            } else {
            	byte[] bytes=Base64.decode(data, Base64.GZIP,Base64.PREFERRED_ENCODING);
            	if (bytes != null) {
            		result = new String(decrypt(bytes,
    	        		PASSWORD_CRYPT_KEY.getBytes(Base64.PREFERRED_ENCODING)),Base64.PREFERRED_ENCODING);
            	}
            }
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
    	return result;
    }
    
    
    /**
     * 对用DES加密过的数据进行解密.
     * @param data DES加密数据
     * @return 返回解密后的数据, 如果传入为null返回null
     */
    public final static String DESDecrypt(String data, String key){
    	String result = "";
    	try{
    		if (data == null || data.length() == 0) {
 	        	return null;
 	        } else {
 	        	byte[] bytes=Base64.decode(data, Base64.GZIP,Base64.PREFERRED_ENCODING);
 	        	if (bytes != null) {
 	        		result = new String(decrypt(bytes,
 	        				key.getBytes(Base64.PREFERRED_ENCODING)),Base64.PREFERRED_ENCODING);
 	        	}
 	        } 
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
    	return result;
    }
    
    /**
     * 对数据进行DES加密.
     * @param data 待进行DES加密的数据
     * @return 返回经过DES加密后的数据
     */
    public final static String DESEncrypt(String data, String key)  {
    	String result = "";
    	try{
        	byte[] source = encrypt(data.getBytes(Base64.PREFERRED_ENCODING), key
                    .getBytes(Base64.PREFERRED_ENCODING));
        	result = Base64.encodeBytes(source, Base64.GZIP,Base64.PREFERRED_ENCODING);
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
        return result;
    }
    
    
    /**
     * 对数据进行DES加密.
     * @param data 待进行DES加密的数据
     * @return 返回经过DES加密后的数据
     */
    public final static String DESEncrypt(String data)  {
    	String result = "";
    	try{
    		if (PASSWORD_CRYPT_KEY == null) {
        		PASSWORD_CRYPT_KEY = "J@c^kT~!";
        	}
        	byte[] source = encrypt(data.getBytes(Base64.PREFERRED_ENCODING), PASSWORD_CRYPT_KEY
                    .getBytes(Base64.PREFERRED_ENCODING));
        	result = Base64.encodeBytes(source, Base64.GZIP,Base64.PREFERRED_ENCODING);
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
        return result;
    }
    
    /**
     * 用指定的key对数据进行DES加密.
     * @param data 待加密的数据
     * @param key DES加密的key
     * @return 返回DES加密后的数据
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
    	//System.out.println("encrypt:"+data.length);
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成
        // 一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(ALGORITHM_FORMATE);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        // 现在，获取数据并加密
        // 正式执行加密操作
        return cipher.doFinal(data);
    }
    
    /**
     * 用指定的key对数据进行DES解密.
     * @param data 待解密的数据
     * @param key DES解密的key
     * @return 返回DES解密后的数据
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
        // 一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(ALGORITHM_FORMATE);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        // 现在，获取数据并解密
        // 正式执行解密操作
        return cipher.doFinal(data);
    }
    
    /*public static byte[] hex2byte(byte[] b) {
    	//System.out.println(b.length);
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        System.out.println("hex2byte:"+b2.length);
        return b2;
    }
    
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }*/
    
    
    public static void main(String[] args) { 
//    	String str = "loginUser=lisi&password=123456&userType=1";
//		String str = "loginUser=lisi&keyCode=01000907";
		String str = "loginUser=lisi&password=123456&userType=1";
//    	String str = "loginUser=lisi&keyCode=01000907&phoneImei=990004287594646";
//    	String str = "timeStamp=201509&loginUser=lisi&data={\"smartKeyLog\":[\"EB01000907824527DE8E2CBBDEC74E85C6CD17C8199A0E23B59C2EFEE59B1EB9FFE962D269EA2AE3F3E14491E0FF65BBE1BB34E633CD0EAFEF9024FD998812CEB59E12D807E775E53D32AB316178F03B797BAF7F2954A7745620A9745A24A00740DF5AE5D9A423D490DD439BDFC05B85DE8D0CD1838307D88CAC23F7A6B72CF08AA82AEFB7A93CF28F91\"]}";
//    	System.out.println(DESEncrypt(str,"J@c^kT~!"));
//		
		System.out.println(DESDecrypt("H4sIAAAAAAAAAAHwAA//20h9O7W2rm8RkS86c0XMDy3UQSrkoEtWASBlrFukgRcRr0vH3H8NPVrlSy7vh5cdl1oxJoHX/fVo8y+0XoSSjyoCCPdheWjyW6zZnV/VGzJL+9cb2N2TZnrcGMmqw3myLkwYGtIqLarb8BrpDGocsd4LJAiIN6U7iB9IC91s455U0QyVhEzuev5MbnBXWMcfOaCLDATF6GtgVFZPvYShiM7Gzq5BqcAOxrgneg745kWnNzdbRhJ7eCmRwr9Sob3d9sXBjn4srtMj2yIQ3/dAVj9YOJEAHGjh5Q7mHQIIiyzaJGA8FtiIhYuFE3q9pVriOasNqPAAAAA="));
		
	}


	private static int parse(char c1) {
		// TODO Auto-generated method stub
		return 0;
	}
    
	
}
