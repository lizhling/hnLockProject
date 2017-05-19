package com.hnctdz.aiLock.dao.device;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.device.DevKeyGroup;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.DevKeyGroupDto;

/** 
 * @ClassName DevKeyGroupDao.java
 * @Author WangXiangBo 
 */
@Repository
public interface DevKeyGroupDao extends GenericDAO<DevKeyGroup, Long>{
	
	public DataPackage findPageDevKeyGroup(DevKeyGroupDto dto,DataPackage dp);
	
	public List<DevKeyGroup> findDevKeyGroupList(DevKeyGroupDto dto);
	
	public List findKeyGroupOptions(DevKeyGroupDto dto);
	
	public String deleteDevKeyGroupByIds(String roleIds);
	
	public List<DevKeyGroup> findDevKeyGroup(DevKeyGroupDto dto);
}
