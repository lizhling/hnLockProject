/*
 * FileName:     ContentInfoServiceImpl.java
 * @Description: 
 * Copyright (c) 2014 Sunrise Corporation.
 * #368, Guangzhou Avenue South, Haizhu District, Guangzhou
 * All right reserved.
 *
 * Modification  History:
 * Date           Author         Version         Discription
 * -----------------------------------------------------------------------------------
 * 2014-1-15   WangXiangBo        1.0              新建
 */
package com.hnctdz.aiLock.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.system.ContentInfoDao;
import com.hnctdz.aiLock.dao.system.UpdateInfoDao;
import com.hnctdz.aiLock.domain.system.ContentInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.ContentInfoDto;
import com.hnctdz.aiLock.service.system.ContentInfoService;

/** 
 * @ClassName ContentInfoServiceImpl.java
 * @Author WangXiangBo 
 * @Date 2014-1-15 上午09:26:39
 */
@Service("com.hnctdz.aiLock.service.system.ContentInfoService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class ContentInfoServiceImpl implements ContentInfoService{
	
	@Autowired
	private ContentInfoDao contentInfoDao;
	@Autowired
	private UpdateInfoDao updateInfoDao;
	
	public DataPackage findPageContentInfo(ContentInfoDto dto,DataPackage dp){
		return contentInfoDao.findPageContentInfo(dto, dp);
	}
	
	public List<ContentInfo> findContentInfoList(ContentInfoDto dto){
		return contentInfoDao.findContentInfoList(dto);
	}
	
	public ContentInfo getById(Long id){
		return contentInfoDao.getById(id);
	}
	
	public void saveContentInfo(ContentInfo contentInfo) throws Exception{
		contentInfoDao.save(contentInfo);
	}
	
	public String deleteContentInfoByIds(String ids)throws Exception{
		return contentInfoDao.deleteContentInfoByIds(ids);
	}
}
