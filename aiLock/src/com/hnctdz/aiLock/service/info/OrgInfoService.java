package com.hnctdz.aiLock.service.info;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.info.OrgInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.info.OrgInfoDto;

/** 
 * @ClassName OrgInfoService.java
 * @Author WangXiangBo 
 */
@Service
public interface OrgInfoService {
	
	public OrgInfo getOrgInfoById(Long orgId);
	
	public DataPackage findPageOrgInfo(OrgInfoDto dto, DataPackage dp);
	
	public String saveOrgInfo(OrgInfo orgInfo);
	
	public String deleteOrgInfoByIds(String orgId);
	
	public List<OrgInfo> findOrgInfoList(OrgInfoDto dto);
	
}
