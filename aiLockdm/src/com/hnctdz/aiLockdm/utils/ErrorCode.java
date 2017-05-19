package com.hnctdz.aiLockdm.utils;

/** 
 * @ClassName ErrorCode.java
 * @Author WangXiangBo 
 */
public class ErrorCode {
	public static final String ERROR_SYS_EXCEPTION = "81";//门锁系统处理失败，请稍后再试！
	public static final String ERROR_PARAMETER = "82";//处理失败，缺少请求信息！
	public static final String ERROR_WAIT_TIMEOUT = "83";//等待门锁响应超时
	public static final String ERROR_RESPONSE_MESS_ERROR = "84";//门锁设备响应信息错误
	public static final String ERROR_IP_CONNECTED_FAILED = "85";//门锁模块连接失败
	public static final String ERROR_IP_NOT_CONNECTED = "86";//门锁模块未连接服务器
	public static final String ERROR_SERVER_NOT_START = "87";//服务器未启动
	public static final String ERROR_WAIT_TASK_TRUE = "88";//当前有任务在执行
	
//	public static final String ERROR_HTTP_REQUEST = "55";//请求处理失败，请稍后再试！
//	public static final String ERROR_ILLEGAL_REQUEST = "50";//请求失败，请重新打开APP再试！
	
	
}
