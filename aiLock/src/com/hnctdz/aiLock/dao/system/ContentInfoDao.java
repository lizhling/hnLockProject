/*
 * FileName:     ContentInfoDao.java
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
package com.hnctdz.aiLock.dao.system;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.system.ContentInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.ContentInfoDto;

/** 
 * @ClassName ContentInfoDao.java
 * @Author WangXiangBo 
 * @Date 2014-1-15 上午09:21:38
 */
@Repository
public interface ContentInfoDao extends GenericDAO<ContentInfo, Long>{
	/**
	 * 查询满足条件的系统内容列表，带分页
	 */
	public DataPackage findPageContentInfo(ContentInfoDto dto,DataPackage dp);
	
	/**
	 * 查询满足条件的系统内容List
	 */
	public List<ContentInfo> findContentInfoList(ContentInfoDto dto);
	
	/**
	 * 批量删除系统内容信息
	 */
	public String deleteContentInfoByIds(String ids) throws Exception;
	
}
