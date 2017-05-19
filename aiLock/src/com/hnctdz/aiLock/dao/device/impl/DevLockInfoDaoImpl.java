package com.hnctdz.aiLock.dao.device.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.device.DevLockInfoDao;
import com.hnctdz.aiLock.domain.device.DevLockInfo;
import com.hnctdz.aiLock.dto.Combobox;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.DevLockInfoDto;
import com.hnctdz.aiLock.utils.DateUtil;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName DevLockInfoDaoImpl.java
 * @Author WangXiangBo 
 */
@Repository("DevLockInfoDao")
public class DevLockInfoDaoImpl extends GenericDaoImpl<DevLockInfo, Integer> implements DevLockInfoDao {
private Map<String, Object> proMap;
	
	public String queryConditions(DevLockInfoDto dto){
		proMap = new HashMap<String, Object>();
		StringBuffer conSql = new StringBuffer();
		if(dto != null){
			if(StringUtil.isNotEmpty(dto.getLockCode())){
				conSql.append(" and lockCode = :lockCode");
				proMap.put("lockCode",dto.getLockCode());
			}
			if(StringUtil.isNotEmpty(dto.getLockName())){
				conSql.append(" and lockName like :lockName");
				proMap.put("lockName", "%"+dto.getLockName()+"%");
			}
			
			if(StringUtil.isNotBlank(dto.getAreaIds())){
				conSql.append(" and areaId in("+dto.getAreaIds()+")");
			}else if(null != dto.getAreaId()){
				String sql ="select getChildLst_area("+dto.getAreaId()+")";
				List list = findAllBySQL(sql);
				String areaIds = list.get(0).toString();
				
				conSql.append(" and areaId in("+areaIds+")");
			}
			if(null != dto.getUnlockPerId()){
				conSql.append(" and unlockPerId = :unlockPerId");
				proMap.put("unlockPerId", dto.getUnlockPerId());
			}
			if(null != dto.getLockType()){
				conSql.append(" and lockType = :lockType");
				proMap.put("lockType", dto.getLockType());
			}
			if(StringUtil.isNotEmpty(dto.getVicePassiveLockCode())){
				conSql.append(" and vicePassiveLockCode = :vicePassiveLockCode");
				proMap.put("vicePassiveLockCode", dto.getVicePassiveLockCode());
			}
			if(null != dto.getIsBlueConfig()){
				if(dto.getIsBlueConfig() == 1){
					conSql.append(" and lockInBlueCode >= :isBlueConfig");
					proMap.put("isBlueConfig", "0");
				}else{
					conSql.append(" and (lockInBlueCode = '' or lockInBlueCode is null)");
				}
			}
			if(null != dto.getLockInBlueCode()&&StringUtil.isNotBlank(dto.getLockInBlueCode())){
				conSql.append(" and lockInBlueCode like :lockInBlueCode ");
				proMap.put("lockInBlueCode", "%"+dto.getLockInBlueCode()+"%");
			}
			if(null != dto.getIsVicePassive()){
				if(dto.getIsVicePassive() == 1){
					conSql.append(" and vicePassiveLockCode >= :isVicePassive");
					proMap.put("isVicePassive", "0");
				}else{
					conSql.append(" and (vicePassiveLockCode = '' or vicePassiveLockCode is null)");
				}
			}
			if(null != dto.getOrgId()){
				String sql ="select getChildLst_org("+dto.getOrgId()+")";
				List list = findAllBySQL(sql);
				String orgIds = list.get(0).toString();
				
				conSql.append(" and orgId in("+orgIds+")");
				
//				conSql.append(" and orgId = :orgId");
//				proMap.put("orgId", dto.getOrgId());
			}
			if(StringUtil.isNotEmpty(dto.getStatus())){
				conSql.append(" and status = :status");
				proMap.put("status", dto.getStatus());
			}
		}
		return conSql.toString();
	}
	
	public DataPackage findPageDevLockInfo(DevLockInfoDto dto, DataPackage dp){
		String str = queryConditions(dto);
		String str1 = this.addOrgPermissionHql();
//		String hql = "from DevLockInfo where 1=1 " + queryConditions(dto) + this.addOrgPermissionHql() + " order by status asc, lockName asc";
		StringBuffer sb = new StringBuffer("from DevLockInfo where 1=1 ");
		sb.append(str);
		sb.append(str1);
		sb.append(" order by status asc, lockName asc");
		
		System.out.println(sb.toString());
		return findPageByHQL(sb.toString(), proMap, dp);
	}
	
	public List<DevLockInfo> findDevLockInfoList(DevLockInfoDto dto){
		String hql = "from DevLockInfo where 1=1 " + queryConditions(dto) + this.addOrgPermissionHql();
		return findAllByHQL(hql, proMap);
	}
	
	public List<DevLockInfo> findNormalLockInfoList(DevLockInfoDto dto){
		String hql = "from DevLockInfo where lockParentId is null " + queryConditions(dto) + this.addOrgPermissionHql();
		if(dto == null || StringUtil.isEmpty(dto.getStatus())){
			hql += " and status in('0','1','2','3')";
		}
		return findAllByHQL(hql, proMap);
	}
	
	public List findNormalLockListByCombobox(){
		String hql = "select CONCAT(lock_id,'') as 'value', lock_name as 'text' from Dev_lock_info where status in('0','1','2','3') " 
			+ this.addOrgPermissionSql();
		return findAllBySQL(hql, proMap, Combobox.class);
	}
	
	public List findAreaStatisticalLock(DevLockInfoDto dto){
		String sql = "{CALL areaStatisticalLock("+dto.getAreaId()+")}";
		return findAllBySQL(sql);
	}
	
	public List<DevLockInfoDto> findPersonnelMaitLockList(DevLockInfoDto dto){
		String hql = "select d.lock_Id as 'lockId', d.lock_Code as 'lockCode', d.lock_Name as 'lockName', d.lock_Type as 'lockType', d.lock_Addres as 'lockAddres', d.longitude, d.latitude," +
				" d.status, a.start_Time as 'startTime', a.end_Time as 'endTime', a.blue_Unlock as 'blueUnlock', a.authorize_Type as 'authorizeType', " +
				" a.SCOPE_UNLOCK as 'scopeUnlock', d.LOCK_IN_BLUE_CODE as 'lockInBlueCode', d.VICE_PASSIVE_LOCK_CODE as 'vicePassiveLockCode'" +
				" from Dev_Lock_Info d, Lock_Key_Authorize a " +
				" where a.lock_Id = d.lock_Id and d.status = 1 and a.status_Code = '01'" +
				" and a.start_Time <= :sysTime and a.end_Time >= :sysTime and a.unlock_Per_Id = :perId";
		proMap = new HashMap<String, Object>();
		proMap.put("sysTime", DateUtil.getDateTime());
		proMap.put("perId", dto.getUnlockPerId());
		return findAllBySQL(hql, proMap, DevLockInfoDto.class);
	}
	
	public boolean findLockUsefulAuthList(Integer lockId){
		String hql = "select a.AUTHORIZE_ID from Lock_Key_Authorize a where a.lock_Id = :lockId " +
				" and a.status_Code = '01' and a.start_Time <= :sysTime and a.end_Time >= :sysTime";
		proMap = new HashMap<String, Object>();
		proMap.put("sysTime", DateUtil.getDateTime());
		proMap.put("lockId", lockId);
		List list = findAllBySQL(hql, proMap);
		if(list.size() > 0){
			return false;
		}
		return true;
	}
	
	public DevLockInfo getDevLockInfoByLockCode(String lockCode){
		if(StringUtil.isNotEmpty(lockCode)){
			proMap = new HashMap<String, Object>();
			proMap.put("lockCode", lockCode);
			String hql = "from DevLockInfo where lockCode = :lockCode";
			List<DevLockInfo> list = this.findAllByHQL(hql, proMap);
			if(list.size() > 0){
				return list.get(0);
			}
		}
		return null;
	}
	
	
}
