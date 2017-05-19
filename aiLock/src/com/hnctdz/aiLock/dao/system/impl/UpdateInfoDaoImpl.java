/*
 * FileName:     UpdateInfoDaoImpl.java
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
package com.hnctdz.aiLock.dao.system.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.system.UpdateInfoDao;
import com.hnctdz.aiLock.domain.system.UpdateInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.UpdateInfoDto;

/** 
 * @ClassName UpdateInfoDaoImpl.java
 * @Author WangXiangBo 
 * @Date 2014-1-20 下午03:29:05
 */
@Repository("UpdateInfoDao")
public class UpdateInfoDaoImpl extends GenericDaoImpl<UpdateInfo, Long> implements UpdateInfoDao{

	public String queryConditions(UpdateInfoDto dto){
		StringBuffer conSql = new StringBuffer();
		if(dto != null){
//			if(StringUtil.isNotEmpty(dto.getUpdateInfoName())){
//				conSql.append(" and UpdateInfoName like '%").append(dto.getUpdateInfoName()).append("%'");
//			}
			if(null != dto.getType()){
				conSql.append(" and type = ").append(dto.getType());
			}
//			if(null != dto.getUpdateInfoId()){
//				conSql.append(" and UpdateInfoId = ").append(dto.getUpdateInfoId());
//			}
		}
		return conSql.toString();
	}
	
	public DataPackage findPageUpdateInfo(UpdateInfoDto dto,DataPackage dp){
		String hql = " from UpdateInfo where 1=1 " + queryConditions(dto) +
					 " order by type";
		return findPageByHQL(hql, dp);
	}
	
	public List<UpdateInfo> findUpdateInfoList(UpdateInfoDto dto){
		String hql = " from UpdateInfo where 1=1 " + queryConditions(dto) +
		 			 " order by type";
		return findAllByHQL(hql);
	}
	
	public void updateInfoStatus(String typeIds){
		String hql = "update UpdateInfo i set i.status = 1 where i.type in("+typeIds+")" +
					 " and (i.status != 1 or i.status is null)";
		bulkUpdate(hql);
	}
}

