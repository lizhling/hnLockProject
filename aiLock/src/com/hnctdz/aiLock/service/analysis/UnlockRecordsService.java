package com.hnctdz.aiLock.service.analysis;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.analysis.UnlockRecords;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.analysis.UnlockRecordsDto;
import com.hnctdz.aiLock.utils.ErrorCodeException;

/** 
 * @ClassName UnlockRecordsService.java
 * @Author WangXiangBo 
 */
@Service
public interface UnlockRecordsService {
	
	public DataPackage findPageUnlockRecords(UnlockRecordsDto dto, DataPackage dp);
	
	public List<UnlockRecords> findUnlockRecordsList(UnlockRecordsDto dto);
	
	public void saveUnlockRecords(UnlockRecords unlockRecords);
	
	public String saveSmartKeyLog(Long perId, String dataLog) throws ErrorCodeException;
	
	public List findOrgStLockRecords(UnlockRecordsDto dto);
	
	public void updateAlarmConfirm(UnlockRecordsDto dto);
	
	public String extractLockId(String dataLog) throws ErrorCodeException;
	
}
