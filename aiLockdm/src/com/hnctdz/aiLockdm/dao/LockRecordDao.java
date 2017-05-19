package com.hnctdz.aiLockdm.dao;

import com.hnctdz.aiLockdm.utils.ErrorCodeException;
import com.hnctdz.aiLockdm.utils.LockStatusRecords;
import com.hnctdz.aiLockdm.utils.UnlockRecords;

/** 
 * @ClassName LockRecordDao.java
 * @Author WangXiangBo 
 */
public interface LockRecordDao {
	
	public void saveRecordCommand(UnlockRecords urs) throws ErrorCodeException;
	
	public void saveLockStatusRecords(LockStatusRecords lsr) throws ErrorCodeException;
	
	public boolean findLockRecordsIfHave(String lockCode, String recordCode);
}
