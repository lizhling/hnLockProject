/*
 * FileName:     ScanerrorService.java
 * @Description: 
 * Copyright (c) 2014 Sunrise Corporation.
 * #368, Guangzhou Avenue South, Haizhu District, Guangzhou
 * All right reserved.
 *
 * Modification  History:
 * Date           Author         Version         Discription
 * -----------------------------------------------------------------------------------
 * 2014-9-24   WangXiangBo        1.0              新建
 */
package com.hnctdz.aiLock.service.system;

import java.util.List;

import org.springframework.stereotype.Service;

/** 
 * @ClassName ScanerrorService.java
 * @Author WangXiangBo 
 * @Date 2014-9-24 上午11:17:57
 */
@Service
public interface ScanerrorService {
	
	/**
	 * 查询需要短信告警的异常List
	 */
	public List findSysBasicDataList(String transStartTime, String transEndTime);
	
	/**
	 * 保存监控异常信息
	 * @param serverName 异常code
	 * @param errorDesc  异常名称
	 * @param phoneNo    用户手机号码
	 * @param errorInput 异常输入
	 * @param errorOut   异常输出
	 */
	public void saveScanerror(String serverName, String errorDesc,String phoneNo, String errorInput, String errorOut);
}
