package com.hnctdz.aiLock.dao.analysis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.analysis.LockStatusRecords;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.analysis.LockStatusRecordsDto;

/** 
 * @ClassName LockStatusRecordsDao.java
 * @Author WangXiangBo 
 */
@Repository
public interface LockStatusRecordsDao extends GenericDAO<LockStatusRecords, String>{
	
	public DataPackage findPageLockStatusRecords(LockStatusRecordsDto dto, DataPackage dp);
	
	public List<LockStatusRecords> findLockStatusRecordsList(LockStatusRecordsDto dto);
	
	public LockStatusRecords findLockStatusLastRecords(String lockCode);

}
