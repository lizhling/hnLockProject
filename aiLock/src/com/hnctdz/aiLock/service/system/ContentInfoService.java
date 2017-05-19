/*
 * FileName:     ContentInfoService.java
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
package com.hnctdz.aiLock.service.system;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.system.ContentInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.ContentInfoDto;

/** 
 * @ClassName ContentInfoService.java
 * @Author WangXiangBo 
 * @Date 2014-1-15 上午09:26:31
 */
@Service
public interface ContentInfoService {
	/**
	 * 查询满足条件的系统内容列表，带分页
	 */
	public DataPackage findPageContentInfo(ContentInfoDto dto,DataPackage dataPackage);
	
	/**
	 * 查询满足条件的系统内容List
	 */
	public List<ContentInfo> findContentInfoList(ContentInfoDto dto);
	
	/**
	 * 根据ID获取系统内容信息
	 */
	public ContentInfo getById(Long id);
	
	/**
	 * 保存系统内容信息
	 */
	public void saveContentInfo(ContentInfo contentInfo) throws Exception;
	
	/**
	 * 批量删除系统内容信息
	 */
	public String deleteContentInfoByIds(String ids)throws Exception;
	
}

