package com.hnctdz.aiLock.utils;

import java.util.HashMap;
import java.util.Map;

/** 
 * @ClassName ErrorCode.java
 * @Author WangXiangBo 
 */
public class ErrorCode {
	
	public static final String ERROR_IMPORT_GROUP = "文件导入出现异常，请检查导入的文件或联系管理员！";
	
	public static final String ERROR_IMPORT_FORMAT = "导入失败，请检查导入文件中数值型内容是否正确！";
	
	public static final String ERROR_SYS_EXCEPTION = "系统处理失败，请稍后再试！";
	public static final String ERROR_TOKEN_CHECK_FAIL = "登录超时，请重新登录！";
	public static final String ERROR_HTTP_REQUEST = "请求门锁平台失败，请稍后再试！";
	public static final String ERROR_REQUEST_TIME_OUT = "请求门锁平台超时，请稍后再试！";
	public static final String ERROR_HTTP_WAS_BLOCKED = "请求门锁平台被拦截，请稍后再试！";
	
	public static final String ERROR_ILLEGAL_REQUEST = "请求失败，请重新打开APP再试！";
	
	/** 系统开门成功 */
	public static final String SYS_UNLOCK_COMMAND = "80";
	/** 系统开门失败 */
	public static final String SYS_UNLOCK_FAILED = "89";
	
	/** 蓝牙开门成功 */
	public static final String BLUE_UNLOCK_COMMAND = "90";
	/** 蓝牙开门失败 */
	public static final String BLUE_UNLOCK_FAILED = "91";
	
	private static Map<String, String> remoteOperationResultsMap = new HashMap<String, String>();
	static {
		remoteOperationResultsMap.put("80", "开门成功！");
		remoteOperationResultsMap.put("81", "门锁系统处理失败，请稍后再试！");
		remoteOperationResultsMap.put("82", "处理失败，缺少请求信息！");
		remoteOperationResultsMap.put("83", "等待门锁响应超时！");
		remoteOperationResultsMap.put("84", "门锁设备响应信息错误！");
		remoteOperationResultsMap.put("85", "门锁网关连接失败！");
		remoteOperationResultsMap.put("86", "门锁网关未连接服务器！");
		remoteOperationResultsMap.put("87", "门锁服务未启动！");
		remoteOperationResultsMap.put("88", "门锁有任务在执行，请15秒后再操作！");
		remoteOperationResultsMap.put("89", "开门失败！");
		remoteOperationResultsMap.put("", "");
	}
	
	public static String getRemoteOperationResults(String key){
		return remoteOperationResultsMap.get(key);
	}
	
}
