package com.hnctdz.aiLock.dao.device;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.device.DevLockInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.DevLockInfoDto;

/** 
 * @ClassName DevLockInfoDao.java
 * @Author WangXiangBo 
 */
@Repository
public interface DevLockInfoDao extends GenericDAO<DevLockInfo, Integer>{
	
	public DataPackage findPageDevLockInfo(DevLockInfoDto dto, DataPackage dp);
	
	public List<DevLockInfo> findNormalLockInfoList(DevLockInfoDto dto);
	
	public List findNormalLockListByCombobox();
	
	public List findAreaStatisticalLock(DevLockInfoDto dto);

	public List<DevLockInfo> findDevLockInfoList(DevLockInfoDto dto);
	
	public List<DevLockInfoDto> findPersonnelMaitLockList(DevLockInfoDto dto);
	
	public DevLockInfo getDevLockInfoByLockCode(String lockCode);
	
	public boolean findLockUsefulAuthList(Integer lockId);
	

}
