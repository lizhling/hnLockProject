package com.hnctdz.aiLock.utils;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.hnctdz.aiLock.dto.AppLogMessage;
import com.hnctdz.aiLock.dto.AppLoginJson;
import com.hnctdz.aiLock.utils.GsonUtil;
import com.hnctdz.aiLock.utils.encryption.DesCrypUtil;

/** 
 * @ClassName GsonUtil.java
 * @Author WangXiangBo 
 */
public class GsonUtil {
	
	private static String dateFormat = "yyyy-MM-dd HH:mm:ss";   //默认日期格式
	
	@Expose
	private String resultCode;
	@Expose
	private String resultMessage;
	@Expose
	private List datas;

	public List getDatas() {
		return datas;
	}

	public void setDatas(List datas) {
		this.datas = datas;
	}

	public static String buildArrayJsonString(String resultCode, String resultMessage, List datas){
		Gson gson =  new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
									  .enableComplexMapKeySerialization()
									  .setDateFormat(dateFormat)
									  .disableHtmlEscaping()
									  .create();
		//将对象转换成json
		GsonUtil dataGson = new GsonUtil();
		dataGson.setResultCode(resultCode);
		dataGson.setResultMessage(resultMessage);
		dataGson.setDatas(datas);
        String jsons = gson.toJson(dataGson);
        System.out.println(jsons);
		return DesCrypUtil.DESEncrypt(jsons);
	}
	
	public static String buildArrayJsonString(GsonUtil dataGson){
		Gson gson =  new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
									  .enableComplexMapKeySerialization()
									  .setDateFormat(dateFormat)
									  .disableHtmlEscaping()
									  .create();
		//将对象转换成json
        String jsons = gson.toJson(dataGson);
        System.out.println(jsons);
		return DesCrypUtil.DESEncrypt(jsons);
	}
	
	public static String buildArrayJsonString2(List datas){
		Gson gson =  new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
									  .enableComplexMapKeySerialization()
									  .setDateFormat(dateFormat)
									  .disableHtmlEscaping()
//									  .serializeNulls()//设置为null的属性也序列化在json中
									  .create();
		//将对象转换成json
		GsonUtil dataGson = new GsonUtil();
		dataGson.setDatas(datas);
		return gson.toJson(dataGson);
	}
	
	public static <T> T fromJson(String str, Class<T> type) {  
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }
	
	public static <T> T fromJson(String str, Type type) {  
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }
	
	public List jsonToObj(String json){
		Gson gson = new Gson();
		GsonUtil b = gson.fromJson(json,GsonUtil.class);
		return b.getDatas();
	}
	
	public static void main(String[] args) {
		AppLogMessage a =  new AppLogMessage();
		String[] smartKeyLog = new String[]{"sdddd","EADF093403203SDFSDF"};
		a.setSmartKeyLog(smartKeyLog);
		
		Gson gson =  new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
		  .enableComplexMapKeySerialization()
		  .setDateFormat(dateFormat)
		  .disableHtmlEscaping()
		  .create();
        String jsons = gson.toJson(a);
        System.out.println(jsons);
        
//        String json = "{\"smartKeyLog\":[\"sdddd\",\"EADF093403203SDFSDF\"]}";
//        
//        AppLogMessage a =  GsonUtil.fromJson(json, AppLogMessage.class);
//        System.out.println("ss");
		
//		AppLoginJson json = new AppLoginJson();
//		json.setSmartKeyPerId("120021");
//		json.setSmartKeyPassw(12341234L);
//		json.setResultCode("0");
//		json.setResultMessage("阿斯蒂芬阿斯蒂芬");
//		GsonUtil.buildArrayJsonString(json);
//		String ss = "H4sIAAAAAAAAAMs37drLJXJ1657r1QvP6Rx1OdBrFXBInXXOk4eP99Xu8hUoK5b5o9bzbMeWgtknXZNC9oRs5lxlevbnpwl3d7hlPH6jGq1U+3Sqke+nt0+iex8l+ixj+xjrHPBBL7bm+5ef+ccdFLTkZpus0L4bfqro70zvHoZJxla77xjtk1IO3qTzy7Zv50s9/csTri9bYrKZV8z+FWuwYm/U/YAddmVTIrcKsE7p+RqW7uT34VHm/u8blueuPu7sbPL0XBCXisj3aTcXL9zuEmqpuXOrzIdV6hvEyyzM3sdqxvpeYWZmrLsp/K9zkodXbF/Q8dzqVUksHkyXXGUXpEXdEjm3zvm82cZlnA57rk2zcxDQt5ofL8covcY091Bd1snO/9MmwtQtE7V+v2OtoyFMXeA6e0aLf1OSrBy0ePX22N20mPs5IfwVe7eOae4dc9OeZ1n/YrQvq2cfSnCus+949bl52cN/RmZCm5Z9mLvfpjXIL2jiLYk7JXLF67P+fw29+lW16fBTobVtmTG70s8cu3t57+/frDVr3dXMuPiMHHq3HOZcrNjO3HZ/cvCptveuN+vjJs4/HZZ/K3L1lcLYFENm8X1rDEyyLqxd9+JAkFVgKNvGAqvtJzUCFcW+vHvd1+Yd53ov3f1p7KP2yY+UP7WbXsqIcQmpjfISMNxwR6Si7NOyidZ8Rv0tB1fYCZ+pzYzCFd6fau7bLM6aMgvmH/aaE8+tTiRlHI7g5bevi9vqubmkRqcoysB9hanhi0DpP/tnaL24/CrXxVv8865gv50MMQbuLyfcDPP1mv2477/5LEX0+Pk7cZKmRM2pvbD4MVBYWdWi+67T5MrV64EXmH8EK0/+KfVk6cXCwPmrbjL0azfmbqlgrIvfVPO+JP/7Uss9N5SerWQR5W55r57DpcuqwV5gIsLt8NJJTYLV+fff59ZKdzc7V0xc3pI2cWunWuO7hney05OXGl6WkYS5q0N/66E+o4I5x13NrtmX23xKOj33xefFGmZ+rbfn9H0RN2F9OfnuH75VjwM/p167orAgcPm2vpkTsppvepVa79ye+fmFbKDLSa41qyp/NrzJO+NfvHNDSNWDN90vtp9Odz3/J3PdAvPgyty37nbXuZbrmtfrGzo0rOhNMPd3CCLWXE8Rtlxddkt5SfNmzmJB3qKoR3vEMjbfcjmrfemxWvSlA7unfdf8fKUodUbdvxxjTYkZxJqb8f1t8pSP3xZPOh8l3+DF6DLBa/l/Zq38QJi5B9axeC39MEdJ49ILWaWNvG3lAYvlZ+S8PC10vplL23/KzAc7ref9qVjOdbN7w+eNK/+tcpM/e0suVPvn+f0JOi7+6ivfWl7aJlqUuCTyrtMipTONDm/aWCtXX90ezFIT+vXubbWWo2Gue5Imx1oRip+dzs7pz6+zTfAOE+q/pyPmIL/CI3z6BmldBpnzVVdzFx0y81K1LLL6vTWf51WMZeQC9wury26wcPh4xsUFzfzgu36ND8OSU+mzVwnaHcypPJxj8f9jpKmj38Rc1ZR+Rou3pmHrO43PzrPz2cifM/NrsPCDoIr5D9xtmSsURQEpov6WEAUAAA==";
//		DesCrypUtil.DESDecrypt(ss);
		
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	
}
