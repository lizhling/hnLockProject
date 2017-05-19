/*
 * FileName:     UpdateInfoServiceImpl.java
 * @Description: 
 * Copyright (c) 2014 Sunrise Corporation.
 * #368, Guangzhou Avenue South, Haizhu District, Guangzhou
 * All right reserved.
 *
 * Modification  History:
 * Date           Author         Version         Discription
 * -----------------------------------------------------------------------------------
 * 2014-1-20   WangXiangBo        1.0              新建
 */
package com.hnctdz.aiLock.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.system.UpdateInfoDao;
import com.hnctdz.aiLock.domain.system.UpdateInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.UpdateInfoDto;
import com.hnctdz.aiLock.service.system.UpdateInfoService;

/** 
 * @ClassName UpdateInfoServiceImpl.java
 * @Author WangXiangBo 
 * @Date 2014-1-20 下午03:18:35
 */
@Service("com.hnctdz.aiLock.service.service.UpdateInfoService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class UpdateInfoServiceImpl implements UpdateInfoService{
	
	@Autowired
	private UpdateInfoDao updateInfoDao;
	
	public DataPackage findPageUpdateInfo(UpdateInfoDto dto,DataPackage dp){
		return updateInfoDao.findPageUpdateInfo(dto, dp);
	}
	
	public List<UpdateInfo> findUpdateInfoList(UpdateInfoDto dto){
		return updateInfoDao.findUpdateInfoList(dto);
	}
	
	public void saveUpdateInfo(UpdateInfo newUpdateInfo){
		updateInfoDao.save(newUpdateInfo);
	}
	
	public void updateInfoStatus(String typeIds){
		updateInfoDao.updateInfoStatus(typeIds);
	}
}

