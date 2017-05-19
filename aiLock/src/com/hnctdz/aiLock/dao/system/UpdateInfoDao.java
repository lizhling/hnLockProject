/*
 * FileName:     UpdateInfoDao.java
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
package com.hnctdz.aiLock.dao.system;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.system.UpdateInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.UpdateInfoDto;

/** 
 * @ClassName UpdateInfoDao.java
 * @Author WangXiangBo 
 * @Date 2014-1-20 下午03:28:58
 */
@Repository
public interface UpdateInfoDao extends GenericDAO<UpdateInfo, Long>{
	/**
	 * 查询满足条件的更新信息列表，带分页
	 */
	public DataPackage findPageUpdateInfo(UpdateInfoDto dto,DataPackage dp);
	
	/**
	 * 查询满足条件的更新信息List
	 */
	public List<UpdateInfo> findUpdateInfoList(UpdateInfoDto dto);
	
	/**
	 * 批量修改版本信息状态
	 */
	public void updateInfoStatus(String typeIds);
}
