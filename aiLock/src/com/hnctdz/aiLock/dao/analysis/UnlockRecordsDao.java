package com.hnctdz.aiLock.dao.analysis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.analysis.UnlockRecords;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.analysis.UnlockRecordsDto;

/** 
 * @ClassName UnlockRecordsDao.java
 * @Author WangXiangBo 
 */
@Repository
public interface UnlockRecordsDao extends GenericDAO<UnlockRecords, String>{
	
	public DataPackage findPageUnlockRecords(UnlockRecordsDto dto, DataPackage dp);
	
	public List<UnlockRecords> findUnlockRecordsList(UnlockRecordsDto dto);

	public List findOrgStLockRecords(UnlockRecordsDto dto);
	
	public void updateAlarmConfirm(UnlockRecordsDto dto);
	
	public void updateRecordsTheLockCode(String newLockCode, String oldLockCode);
	
}
