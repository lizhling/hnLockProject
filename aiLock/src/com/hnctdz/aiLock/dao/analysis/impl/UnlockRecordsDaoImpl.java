package com.hnctdz.aiLock.dao.analysis.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.analysis.UnlockRecordsDao;
import com.hnctdz.aiLock.domain.analysis.UnlockRecords;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.analysis.UnlockRecordsDto;
import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.ResponseCommandUtil;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName UnlockRecordsDaoImpl.java
 * @Author WangXiangBo 
 */
@Repository("UnlockRecordsDao")
public class UnlockRecordsDaoImpl extends GenericDaoImpl<UnlockRecords, String> implements UnlockRecordsDao {
private Map<String, Object> proMap;
	
	public String queryConditions(UnlockRecordsDto dto){
		proMap = new HashMap<String, Object>();
		StringBuffer conSql = new StringBuffer();
		boolean isAddPermissions = true;
		if(dto != null){
			if(null != dto.getRecordId()){
				conSql.append(" and recordId = :recordId");
				proMap.put("recordId", dto.getRecordId());
			}
			if(StringUtil.isNotEmpty(dto.getKeyCode())){
				conSql.append(" and keyCode like :keyCode");
				proMap.put("keyCode", "%"+dto.getKeyCode()+"%");
			}
			if(StringUtil.isNotEmpty(dto.getKeyName())){
				conSql.append(" and ur.keyCode in (select d.keyCode from DevKeyInfo d where d.keyName like :keyName)");
				proMap.put("keyName", "%"+dto.getKeyName()+"%");
			}
			if(StringUtil.isNotEmpty(dto.getLockCode())){
				conSql.append(" and lockCode like :lockCode");
				proMap.put("lockCode", "%"+dto.getLockCode()+"%");
			}
			if(StringUtil.isNotEmpty(dto.getLockName())){
				conSql.append(" and ur.lockCode in (select d.lockCode from DevLockInfo d where d.lockName like :lockName)");
				proMap.put("lockName", "%"+dto.getLockName()+"%");
			}
			if(null != dto.getOrgId()){
				conSql.append(" and ur.lockCode in (select d.lockCode from DevLockInfo d where d.orgId in("+findSonOrgsByOrgId(dto.getOrgId())+"))");
				isAddPermissions = false;
			}
			if(null != dto.getAreaId()){
				String sql ="select getChildLst_area("+dto.getAreaId()+")";
				List list = findAllBySQL(sql);
				String areaIds = list.get(0).toString();
				conSql.append(" and ur.lockCode in (select d.lockCode from DevLockInfo d where d.areaId in("+areaIds+"))");
			}
			if(StringUtil.isNotEmpty(dto.getUnStartTime())){
				conSql.append(" and unlockTime >= :unStartTime");
				proMap.put("unStartTime", dto.getUnStartTime());
			}
			if(StringUtil.isNotEmpty(dto.getUnEndTime())){
				conSql.append(" and unlockTime < :unEndTime");
				proMap.put("unEndTime", dto.getUnEndTime());
			}

			if(null != dto.getLockType()){
				conSql.append(" and lockType = :lockType");
				proMap.put("lockType", dto.getLockType());
			}
			
			if(StringUtil.isNotBlank(dto.getIfDeal())){
				conSql.append(" and userId is null ");//表示确认人不为空
			}

			if(StringUtil.isNotEmpty(dto.getRecordCode())){
				conSql.append(" and recordCode = :recordCode");
				proMap.put("recordCode", dto.getRecordCode());
			}else if(StringUtil.isNotEmpty(dto.getAlarmLevel())){
				conSql.append(" and recordCode in(:recordCode)");
				proMap.put("recordCode",ResponseCommandUtil.getAlarmLevelCodes(dto.getAlarmLevel()));
			}else {
				if(dto.getSelectType() == Constants.SELECT_ALARM){
					conSql.append(" and recordCode in("+ResponseCommandUtil.ALARM_RECORD_CODE+")");
				}else{
					conSql.append(" and recordCode not in("+ResponseCommandUtil.ALARM_RECORD_CODE+")");
				}
			}
		}

		if(dto.getSelectType() == 1){
			//查询门锁记录，添加开锁人员的开锁记录条件
			if(dto.getUnlockPerId() != null){
				conSql.append(" and unlockPerId = :unlockPerId");
				proMap.put("unlockPerId", dto.getUnlockPerId());
				isAddPermissions = false;
			}
		}else{
			//查询告警信息，且门锁开锁人员不为空，这添加人员过滤条件（APP人员登录是查询自己的告警信息）
			if(dto.getUnlockPerId() != null){
				conSql.append(" and EXISTS (select 1 from DevLockInfo d where ur.lockCode = d.lockCode " +
											" and EXISTS (select 1 from LockKeyAuthorize lka where lka.lockId = d.lockId and lka.unlockPerId = :unlockPerId))");
				proMap.put("unlockPerId", dto.getUnlockPerId());
				isAddPermissions = false;
			}
		}
		
		if(isAddPermissions){//是否添加登录人所在组织权限(管理员才有权限查询整个组织的告警)
			String orgSql = this.addOrgPermissionHql("d");
			if(StringUtil.isNotBlank(orgSql)){
				conSql.append(" and EXISTS (select 1 from DevLockInfo d where ur.lockCode = d.lockCode "+orgSql+")");
			}
		}
		return conSql.toString();
	}
	
	public DataPackage findPageUnlockRecords(UnlockRecordsDto dto, DataPackage dp){
		String hql = "from UnlockRecords ur where 1=1 " + queryConditions(dto) + " order by unlockTime desc";
		return findPageByHQL(hql, proMap, dp);
	}
	
	public List<UnlockRecords> findUnlockRecordsList(UnlockRecordsDto dto){
		String hql = "from UnlockRecords ur where 1=1 " + queryConditions(dto) + " order by unlockTime desc";
		return findAllByHQL(hql, proMap);
	}
	
	public List findOrgStLockRecords(UnlockRecordsDto dto){
		String sql = "{CALL orgStatisticalLockRecords("+dto.getOrgId()+")}";
		return findAllBySQL(sql);
	}
	
	public void updateAlarmConfirm(UnlockRecordsDto dto){
		String hql = "update UnlockRecords set confirmTime = :confirmTime, userId = :userId, note = :note " +
				"where recordId in(:recordId)";
		proMap = new HashMap<String, Object>();
		proMap.put("confirmTime", new Date());
		proMap.put("userId", dto.getUserId());
		proMap.put("note", dto.getNote());
		proMap.put("recordId", StringUtil.stringToIntArray(dto.getRecordIds()));
		performHql(hql, proMap);
	}
	
	public void updateRecordsTheLockCode(String newLockCode, String oldLockCode){
		String hql = "update UnlockRecords set lockCode = :newLockCode where lockCode = :oldLockCode";
		proMap = new HashMap<String, Object>();
		proMap.put("newLockCode", newLockCode);
		proMap.put("oldLockCode", oldLockCode);
		performHql(hql, proMap);
	}
	
//	public DataPackage findPageUnlockRecords(UnlockRecordsDto dto, DataPackage dp){
//		String hql = "from UnlockRecords where 1=1 " + queryConditions(dto) + " order by unlockTime desc";
//		return findPageByHQL(hql, proMap, dp);
//	}
}
