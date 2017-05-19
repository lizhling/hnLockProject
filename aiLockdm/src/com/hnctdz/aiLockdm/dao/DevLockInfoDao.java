package com.hnctdz.aiLockdm.dao;

import java.util.List;

import com.hnctdz.aiLockdm.utils.ErrorCodeException;

/** 
 * @ClassName DevLockInfoDao.java
 * @Author WangXiangBo 
 */
public interface DevLockInfoDao {
	public List findActiveLockByModuleCode(String moduleCode) throws ErrorCodeException;
}
