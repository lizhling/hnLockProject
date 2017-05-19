package com.hnctdz.aiLock.service.device;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.device.DevKeyGroup;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.DevKeyGroupDto;

/** 
 * @ClassName DevKeyGroupService.java
 * @Author WangXiangBo 
 */
@Service
public interface DevKeyGroupService {
	
	/**
	 * 查询满足条件的分组列表，带分页
	 */
	public DataPackage findPageDevKeyGroup(DevKeyGroupDto dto, DataPackage dp);
	
	public DevKeyGroup getById(Long groupId);
	
	public List<DevKeyGroup> findDevKeyGroupList(DevKeyGroupDto dto);
	
	public List findKeyGroupOptions(DevKeyGroupDto dto);
	
	/**
	 * 保存分组信息
	 */
	public void saveDevKeyGroup(DevKeyGroup DevKeyGroup);
	
	/**
	 * 删除分组信息
	 */
	public String deleteDevKeyGroupByIds(String roleIds);
	
	/**
	 * 查询满足条件的分组list
	 */
	public List<DevKeyGroup> findDevKeyGroup(DevKeyGroupDto dto);
	
}
