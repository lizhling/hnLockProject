/*
 * FileName:     ScanerrorServiceImpl.java
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
package com.hnctdz.aiLock.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.system.ScanerrorDao;
import com.hnctdz.aiLock.domain.system.Scanerror;
import com.hnctdz.aiLock.service.system.ScanerrorService;
import com.hnctdz.aiLock.utils.CommonUtil;
import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.DateUtil;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName ScanerrorServiceImpl.java
 * @Author WangXiangBo 
 * @Date 2014-9-24 上午11:19:29
 */
@Service("ScanerrorService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class ScanerrorServiceImpl implements ScanerrorService{
	@Autowired
	private ScanerrorDao scanerrorDao;
	
	public List findSysBasicDataList(String transStartTime, String transEndTime){
		String alarmNumber = Constants.ERROR_ALARM_NUMBER;//异常报警次数
		if(StringUtil.isEmpty(alarmNumber)){
			alarmNumber = "5";
		}
		String sql = "select server_ip, error_desc, count(*)"+
                     "  from (select server_ip, error_desc from scanerror" +
					 "         where trans_time >= '" + transStartTime +
		             "'          and trans_time <= '" + transEndTime +
		             "'        group by server_ip, PHONE_NO, error_desc)"+
                     " group by server_ip, error_desc having count(*) >= " + alarmNumber;
		return scanerrorDao.findAllBySQL(sql);
	}
	
	public void saveScanerror(String serverName, String errorDesc, String phoneNo, String errorInput, String errorOut){
		Scanerror scanerror = new Scanerror();
		scanerror.setId(StringUtil.getUUID());
		scanerror.setServerIp(CommonUtil.getPcServerIP());
		scanerror.setTransTime(DateUtil.getDateTime());
		
		if(null != errorInput && errorInput.length() >= 380){
			errorInput = errorInput.substring(0, 380);
		}
		if(null != errorOut && errorOut.length() >= 360){
			errorOut = errorOut.substring(0, 360);
		}
		scanerror.setErrorDesc(errorDesc);
		scanerror.setErrorInput(errorInput);
		scanerror.setErrorOut(errorOut);
		scanerror.setServerName(serverName);
		scanerror.setPhoneNo(phoneNo);
		
		scanerrorDao.save(scanerror);
	}
}
