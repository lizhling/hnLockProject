package com.hnctdz.aiLock.dao.info;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.info.OrgInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.info.OrgInfoDto;

/** 
 * @ClassName OrgInfoDao.java
 * @Author WangXiangBo 
 */
@Repository
public interface OrgInfoDao extends GenericDAO<OrgInfo, Long>{
	
	public DataPackage findPageOrgInfo(OrgInfoDto dto, DataPackage dp);
	
	public List<OrgInfo> findOrgInfoList(OrgInfoDto dto);
	
	public List<OrgInfo> findSysAreaLowerNodesList(Long orgId);
	
}
