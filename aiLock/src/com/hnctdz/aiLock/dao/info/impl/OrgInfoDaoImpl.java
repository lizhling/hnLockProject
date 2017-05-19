package com.hnctdz.aiLock.dao.info.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.info.OrgInfoDao;
import com.hnctdz.aiLock.domain.info.OrgInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.info.OrgInfoDto;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName OrgInfoDaoImpl.java
 * @Author WangXiangBo 
 */
@Repository("OrgInfoDao")
public class OrgInfoDaoImpl extends GenericDaoImpl<OrgInfo, Long> implements OrgInfoDao {
private Map<String, Object> proMap;
	
	public String queryConditions(OrgInfoDto dto){
		proMap = new HashMap<String, Object>();
		StringBuffer conSql = new StringBuffer();
		if(dto != null){
			if(StringUtil.isNotEmpty(dto.getOrgName())){
				conSql.append(" and orgName like :orgName");
				proMap.put("orgName", "%"+dto.getOrgName()+"%");
			}
			if(StringUtil.isNotEmpty(dto.getManager())){
				conSql.append(" and manager like :manager");
				proMap.put("manager", "%"+dto.getManager()+"%");
			}
			if(StringUtil.isNotEmpty(dto.getPhoneNo())){
				conSql.append(" and phoneNo like :phoneNo");
				proMap.put("phoneNo", "%"+dto.getPhoneNo()+"%");
			}
		}
		conSql.append(addOrgPermissionHql());
		return conSql.toString();
	}
	
	public DataPackage findPageOrgInfo(OrgInfoDto dto, DataPackage dp){
		String hql = "from OrgInfo where 1=1 " + queryConditions(dto);
		return findPageByHQL(hql, proMap, dp);
	}
	
	public List<OrgInfo> findOrgInfoList(OrgInfoDto dto){
		String hql = "from OrgInfo where 1=1 " + queryConditions(dto);
		return findAllByHQL(hql, proMap);
	}
	
	public List<OrgInfo> findSysAreaLowerNodesList(Long orgId) {
		String hql = "from OrgInfo where orgId in("+findSonOrgsByOrgId(orgId)+")";
		return findAllByHQL(hql);
	}
}
