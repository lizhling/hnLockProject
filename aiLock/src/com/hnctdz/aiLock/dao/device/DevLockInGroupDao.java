package com.hnctdz.aiLock.dao.device;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.device.DevLockInGroup;

/** 
 * @ClassName SysUserInRoleDao.java
 * @Author WangXiangBo 
 */
@Repository
public interface DevLockInGroupDao extends GenericDAO<DevLockInGroup, Long>{
	
	public List<DevLockInGroup> findDevLockByGroupId(Long groupId);
	
	public void saveOfBatch(List<DevLockInGroup> listForSave);

	public void deleteOfBatch(List<DevLockInGroup> listForDel);
	
}
