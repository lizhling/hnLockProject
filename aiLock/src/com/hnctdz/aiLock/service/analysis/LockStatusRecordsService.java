package com.hnctdz.aiLock.service.analysis;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.analysis.LockStatusRecords;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.analysis.LockStatusRecordsDto;

/** 
 * @ClassName LockStatusRecordsService.java
 * @Author WangXiangBo 
 */
@Service
public interface LockStatusRecordsService {
	
	public DataPackage findPageLockStatusRecords(LockStatusRecordsDto dto, DataPackage dp);
	
	public List<LockStatusRecords> findLockStatusRecordsList(LockStatusRecordsDto dto);
	
	public LockStatusRecords findLockStatusLastRecords(String lockCode);
}
