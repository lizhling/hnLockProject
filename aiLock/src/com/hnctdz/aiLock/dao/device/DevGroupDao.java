package com.hnctdz.aiLock.dao.device;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.device.DevGroup;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.DevGroupDto;

/** 
 * @ClassName DevGroupDao.java
 * @Author WangXiangBo 
 */
@Repository
public interface DevGroupDao extends GenericDAO<DevGroup, Long>{
	
	public DataPackage findPageDevGroup(DevGroupDto dto,DataPackage dp);
	
	public List<DevGroup> findDevGroupList(DevGroupDto dto);
	
	public DataPackage findGroupLock(DevGroupDto dto,DataPackage dp);
	
	public List findLockByGroupIdsList(DevGroupDto dto);
	
	public String deleteDevGroupByIds(String roleIds);
	
	public List<DevGroup> findDevGroup(DevGroupDto dto);
}
