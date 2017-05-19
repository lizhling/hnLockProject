package com.hnctdz.aiLock.service.device;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.device.DevGroup;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.DevGroupDto;

/** 
 * @ClassName DevGroupService.java
 * @Author WangXiangBo 
 */
@Service
public interface DevGroupService {
	
	/**
	 * 查询满足条件的分组列表，带分页
	 */
	public DataPackage findPageDevGroup(DevGroupDto dto, DataPackage dp);
	
	public List<DevGroup> findDevGroupList(DevGroupDto dto);
	
	public DataPackage findGroupLock(DevGroupDto dto, DataPackage dp);
	
	public List findLockByGroupIdsList(DevGroupDto dto);
	
	/**
	 * 保存分组信息
	 */
	public void saveDevGroup(DevGroup devGroup);
	
	/**
	 * 删除分组信息
	 */
	public String deleteDevGroupByIds(String roleIds);
	
	/**
	 * 查询满足条件的分组list
	 */
	public List<DevGroup> findDevGroup(DevGroupDto dto);
	
}
