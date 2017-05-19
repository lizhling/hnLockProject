/*
 * FileName:     CommonUtil.java
 * @Description: 
 * Copyright (c) 2014 Sunrise Corporation.
 * #368, Guangzhou Avenue South, Haizhu District, Guangzhou
 * All right reserved.
 *
 * Modification  History:
 * Date           Author         Version         Discription
 * -----------------------------------------------------------------------------------
 * 2014-1-2   WangXiangBo        1.0              新建
 */
package com.hnctdz.aiLock.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;


/** 
 * @ClassName CommonUtil.java
 * @Author WangXiangBo 
 * @Date 2014-1-2 下午08:24:44
 */
public class CommonUtil {
	private static final Logger LOG = Logger.getLogger(CommonUtil.class);
	
	public static final String CONFIG_PATH = "/systemconfig.properties";
	
	/**
	 * @param propertyName
	 * @return
	 */
	public static String getPropertyValue(String propertyName) {
		String propertyValue = "";
		try {
			Properties p = new Properties();
			p.load(CommonUtil.class.getResourceAsStream(CONFIG_PATH));
			
			propertyValue = p.getProperty(propertyName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return propertyValue;
	}
	
	/**
	 * 获取整型属性
	 * @param name 属性名
	 * @param defaultValue 默认值
	 * @return
	 */
	public static int getIntProperty(String name, int defaultValue) {
		try {
			return Integer.parseInt(getPropertyValue(name));
		} catch (NumberFormatException e) {
			System.err.println(name + "格式错误");
		} catch (NullPointerException e) {
			System.err.println("未找到属性" + name);
		} catch (Exception e) {
			System.err.println("获取属性值失败");
		}
		return defaultValue;
	}
	
	public static String getResultCode(String returnValue){
		if(StringUtil.isEmpty(returnValue)){
			return null;
		}
		JSONObject obj= JSONObject.fromObject(returnValue);
		String retcode = "";
		if (obj.containsKey("return")) {
			obj = obj.getJSONObject("return");
		} else if (obj.containsKey("root")) {
			obj = obj.getJSONObject("root");
		} else if (obj.containsKey("data")) {
			obj = obj.getJSONObject("data");
		}
		retcode = obj.getString("returnCode");
		
		return retcode; 
	}
	
	/**
	 * 取系统资源的存放目录
	 * @param filePath
	 * @return
	 */
	public static String getResourcePath(String filePath){
		String path = "";
		String resourcePath = CommonUtil.getPropertyValue("resourcePath");
		if (resourcePath!=null && resourcePath.length()>0) {
			path = resourcePath + filePath;
		} else {
			File file = new File("");
			String abspath = file.getAbsolutePath();
			LOG.info("系统Tomcat根目录：" + abspath);
			//0:windows 1:liunx
			if("1".equalsIgnoreCase(Constants.SERVER_OS)){
				if(abspath.indexOf("/bin") > 0){
					path = abspath.replace("/bin", "/webapps/scmbhm") + filePath;
				}else{
					path = abspath + Constants.FILE_SEPARATOR + "webapps" + Constants.FILE_SEPARATOR + "scmbhm" + filePath;
				}
			} else {
				//windows系统,其分割符号是：\\，所以这里直接替换。
				path = abspath.replace("\\bin", "\\webapps\\scmbhm") + filePath;
			}
			LOG.info("跳转到文件存储目录：" + path);
			//path = ServletActionContext.getServletContext().getRealPath(filePath);
		}
		return path;
	}
	
	
	/**
	 * 在指定字符中随机生成指定长度的字符串
	 * @param sourceStr 字符串生成源
	 * @param length 生成字符串长度
	 * @return
	 */
	public static String randomString(String sourceStr, int length) {
		Random randGen = null;
		char[] numbersAndLetters = null;
		if (length < 1) {
			return "";
		}
		if (randGen == null) {
			randGen = new Random();
			numbersAndLetters = (sourceStr).toCharArray();
		}
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(sourceStr.length() - 1)];
		}
		return new String(randBuffer);
	}

	/**
	 * 随机生成数字 + 字母的字符串
	 * @param length 生成字符串长度
	 * @return
	 */
	public static String getRandomLettAndDigStr(int length) {
		return randomString("0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ", length);
	}
	
	/**
	 * 随机生成0-9随机字符串
	 * @param length 生成字符串长度
	 * @return
	 */
	public static String getRandomNumberString(int length) {
		/*StringBuffer sb = new StringBuffer();
		String str = "0123456789";
		Random r = new Random();
		for (int i = 0; i < count; i++) {
			int num = r.nextInt(str.length());
			sb.append(str.charAt(num));
			str = str.replace((str.charAt(num) + ""), "");
		}*/
		return randomString("0123456789", length);
	}
	
	/**
	 * 随机生成大写字母的字符串
	 * @param length 生成字符串长度
	 * @return
	 */
	public static String getRandomLettersUp(int length) {
		return randomString("ABCDEFGHIJKLMNOPQRSTUVWXYZ", length);
	}
	
	/**
	 * 字符转换
	 * @return
	 */
	public static String getDynamicKey(String oldKeys) {
		char a[] = {'@','#','*','~',',','}','+','%','_',':','{','-','?','.','!','c','=','w',']','[',')',';','(','t','T','R','x','P','O','N','m','y','>','<'};
		char b[] = {'a','T','c','d','e','f','g','i','j','k','l','n','o','p','r','u','w','x','z','C','E','F','H','J','L','N','P','R','U','V','W','X','Y','Z'};
		char oldKey[] = oldKeys.toCharArray();
		char newKey[] = new char[oldKey.length];
    	for (int i = 0; i < oldKey.length; i++) {
    		for(int j = 0; j < b.length; j++){
    			if(oldKey[i] == b[j]){
    				newKey[i] = a[j];
    				break;
        		}
    		}
    		if(newKey[i] == '\0'){
    			newKey[i] = oldKey[i];
    		}
    	}
    	return new String(newKey);
    }
	
	/**
	  * 获取指定参数后面的值(返回String)
	  * @param src url
	  * @param tag 指定参数
	  * @return 获取后的值
	  */
	public static String getStringValueFromTag(String src ,String tag){
		String result = "";
		if(null != src && !src.equals("")){
			if(src.contains(tag)){
				String temp = src.substring(src.indexOf(tag), src.length());
				if(null != temp && temp.indexOf("&") != -1){
					result = temp.substring(tag.length() + 1, temp.indexOf("&"));
				}else{
					result = temp.substring(tag.length() + 1, temp.length());
				}
			}
		}
		return result;
	}
	
	
	public static String getPcServerIP(){
		String pcServerIp = "";
        try {
            Enumeration<?> e1 = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
            while (e1.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) e1.nextElement();
                if (!ni.getName().equals("eth0")) {
                    continue;
                } else {
                    Enumeration<?> e2 = ni.getInetAddresses();
                    while (e2.hasMoreElements()) {
                        InetAddress ia = (InetAddress) e2.nextElement();
                        if (ia instanceof Inet6Address){
                        	continue;
                        }
                        pcServerIp = ia.getHostAddress();
                    }
                    break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        
		return pcServerIp;
	}
	
	public static  String getIpAddr(HttpServletRequest request) {
		String ip = "10.13.202.11";
		try{
		
			ip = request.getHeader("X-Forwarded-For");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
			//对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		    if(ip!=null && ip.length()>15){ //"***.***.***.***".length() = 15
		        if(ip.indexOf(",")>0){
		        	ip = ip.substring(0,ip.indexOf(","));
		        }
		    }
		    //判断下格式
		    if(StringUtil.isNotBlank(ip) && !"10.13.202.11".equals(ip)){
		    	 ip = judgeIp(ip);
		    }
		}catch(Exception e){
		}
	    return ip;
	 }
	
	 public static String judgeIp(String oldIp) {
		String newIp = "10.13.202.11";
		try {
			Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
			Matcher matcher = pattern.matcher(oldIp);
			if (matcher.matches()) {
				newIp = oldIp;
			}
		} catch (Exception e) {
			System.out.println("=================ip错误==================");
			newIp = "10.134.222.11";
		}
		return newIp;
	}
}
