/*
 * FileName:     UpdateInfoService.java
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
package com.hnctdz.aiLock.service.system;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.system.UpdateInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.UpdateInfoDto;

/** 
 * @ClassName UpdateInfoService.java
 * @Author WangXiangBo 
 * @Date 2014-1-20 下午03:18:08
 */
@Service
public interface UpdateInfoService {
	/**
	 * 查询满足条件的版本更新信息列表，带分页
	 */
	public DataPackage findPageUpdateInfo(UpdateInfoDto dto,DataPackage dataPackage);
	
	/**
	 * 查询满足条件的版本更新信息List
	 */
	public List<UpdateInfo> findUpdateInfoList(UpdateInfoDto dto);
	
	/**
	 * 保存版本更新信息
	 */
	public void saveUpdateInfo(UpdateInfo updateInfo);
	
	/**
	 * 批量修改版本信息状态
	 */
	public void updateInfoStatus(String typeIds);
	
}

