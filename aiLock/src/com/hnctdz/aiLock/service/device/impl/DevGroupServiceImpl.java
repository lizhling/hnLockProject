package com.hnctdz.aiLock.service.device.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.device.DevGroupDao;
import com.hnctdz.aiLock.domain.device.DevGroup;
import com.hnctdz.aiLock.domain.device.DevLockInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.DevGroupDto;
import com.hnctdz.aiLock.service.device.DevGroupService;

/** 
 * @ClassName DevGroupServiceImpl.java
 * @Author WangXiangBo 
 */
@Service("com.hnctdz.aiLock.service.device.DevGroupService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class DevGroupServiceImpl implements DevGroupService{
	@Autowired
	private DevGroupDao dao;
	
	public DataPackage findPageDevGroup(DevGroupDto dto,DataPackage dp){
		return dao.findPageDevGroup(dto, dp);
	}
	
	public List<DevGroup> findDevGroupList(DevGroupDto dto){
		return dao.findDevGroupList(dto);
	}
	
	public DataPackage findGroupLock(DevGroupDto dto,DataPackage dp){
		return dao.findGroupLock(dto, dp);
	}
	
	public List<DevLockInfo> findLockByGroupIdsList(DevGroupDto dto){
		return this.dao.findLockByGroupIdsList(dto);
	}
	
	
	public void saveDevGroup(DevGroup devGroup){
		dao.save(devGroup);
	}
	
	public String deleteDevGroupByIds(String roleIds){
		return dao.deleteDevGroupByIds(roleIds);
	}
	
	public List<DevGroup> findDevGroup(DevGroupDto dto){
		return dao.findDevGroup(dto);
	}
}
