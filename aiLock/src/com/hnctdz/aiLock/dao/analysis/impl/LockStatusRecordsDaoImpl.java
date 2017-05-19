package com.hnctdz.aiLock.dao.analysis.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.analysis.LockStatusRecordsDao;
import com.hnctdz.aiLock.domain.analysis.LockStatusRecords;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.analysis.LockStatusRecordsDto;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName LockStatusRecordsDaoImpl.java
 * @Author WangXiangBo 
 */
@Repository("LockStatusRecordsDao")
public class LockStatusRecordsDaoImpl extends GenericDaoImpl<LockStatusRecords, String> implements LockStatusRecordsDao {
private Map<String, Object> proMap;
	
	public String queryConditions(LockStatusRecordsDto dto){
		proMap = new HashMap<String, Object>();
		StringBuffer conSql = new StringBuffer();
		boolean isAddPermissions = true;
		if(dto != null){
			if(null != dto.getRecordId()){
				conSql.append(" and recordId = :recordId");
				proMap.put("recordId", dto.getRecordId());
			}
			if(StringUtil.isNotEmpty(dto.getLockCode())){
				conSql.append(" and lockCode like :lockCode");
				proMap.put("lockCode", "%"+dto.getLockCode()+"%");
			}
			if(StringUtil.isNotEmpty(dto.getLockName())){
				conSql.append(" and lsr.lockCode in (select d.lockCode from DevLockInfo d where d.lockName like :lockName)");
				proMap.put("lockName", "%"+dto.getLockName()+"%");
			}
			if(null != dto.getOrgId()){
				conSql.append(" and lsr.lockCode in (select d.lockCode from DevLockInfo d where d.orgId in("+findSonOrgsByOrgId(dto.getOrgId())+"))");
				isAddPermissions = false;
			}
			if(null != dto.getAreaId()){
				conSql.append(" and lsr.lockCode in (select d.lockCode from DevLockInfo d where d.areaId in("+findSonAreasByAreaId(dto.getAreaId())+"))");
			}
			if(StringUtil.isNotEmpty(dto.getMenciStatus())){
				conSql.append(" and menciStatus = :menciStatus");
				proMap.put("menciStatus", dto.getMenciStatus());
			}
			if(StringUtil.isNotEmpty(dto.getXiesheStatus())){
				conSql.append(" and xiesheStatus = :xiesheStatus");
				proMap.put("xiesheStatus", dto.getXiesheStatus());
			}
			if(StringUtil.isNotEmpty(dto.getMenguanhaoStatus())){
				if("0".equalsIgnoreCase(dto.getMenguanhaoStatus())){
					conSql.append(" and xiesheStatus = 0 and menciStatus = 0");
				}else if("1".equalsIgnoreCase(dto.getMenguanhaoStatus())){
					conSql.append(" and xiesheStatus = 1 and menciStatus = 1");
				}else if("2".equalsIgnoreCase(dto.getMenguanhaoStatus())){
					conSql.append(" and (xiesheStatus = 0 or menciStatus = 0)");
				}
			}
		}
		if(isAddPermissions){//添加登录人所在组织权限
			String orgSql = this.addOrgPermissionHql("d");
			if(StringUtil.isNotBlank(orgSql)){
				conSql.append(" and EXISTS (select 1 from DevLockInfo d where lsr.lockCode = d.lockCode "+orgSql+")");
			}
		}
		return conSql.toString();
	}
	
	public DataPackage findPageLockStatusRecords(LockStatusRecordsDto dto, DataPackage dp){
		String hql = "from LockStatusRecords lsr where 1=1 " + queryConditions(dto) + " order by reportTime desc";
		return findPageByHQL(hql, proMap, dp);
	}
	
	public List<LockStatusRecords> findLockStatusRecordsList(LockStatusRecordsDto dto){
		String hql = "from LockStatusRecords lsr where 1=1 " + queryConditions(dto) + " order by reportTime desc";
		return findAllByHQL(hql, proMap);
	}
	
	public LockStatusRecords findLockStatusLastRecords(String lockCode){
		String hql = "from LockStatusRecords where lockCode ='" + lockCode + "' order by reportTime desc";
//		List<LockStatusRecords> list =  findAllByHQL(hql, proMap);
		Session ss = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = ss.createQuery(hql);
		query.setFirstResult(0);  
        query.setMaxResults(1); 
        List<LockStatusRecords> list = query.list();
        if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}
}
