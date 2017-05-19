package com.hnctdz.aiLock.service.device.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.device.DevKeyGroupDao;
import com.hnctdz.aiLock.dao.info.PersonnelInfoDao;
import com.hnctdz.aiLock.domain.device.DevKeyGroup;
import com.hnctdz.aiLock.domain.device.DevKeyInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.DevKeyGroupDto;
import com.hnctdz.aiLock.service.device.DevKeyGroupService;

/** 
 * @ClassName DevKeyGroupServiceImpl.java
 * @Author WangXiangBo 
 */
@Service("com.hnctdz.aiLock.service.device.DevKeyGroupService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class DevKeyGroupServiceImpl implements DevKeyGroupService{
	@Autowired
	private DevKeyGroupDao dao;
	@Autowired
	private PersonnelInfoDao personnelInfoDao;
	
	public DataPackage findPageDevKeyGroup(DevKeyGroupDto dto,DataPackage dp){
		return dao.findPageDevKeyGroup(dto, dp);
	}
	
	public DevKeyGroup getById(Long groupId){
		return dao.getById(groupId);
	}
	
	public List<DevKeyGroup> findDevKeyGroupList(DevKeyGroupDto dto){
		return dao.findDevKeyGroupList(dto);
	}
	
	public List findKeyGroupOptions(DevKeyGroupDto dto){
		return dao.findKeyGroupOptions(dto);
	}
	
	public void saveDevKeyGroup(DevKeyGroup DevKeyGroup){
		dao.save(DevKeyGroup);
	}
	
	public String deleteDevKeyGroupByIds(String roleIds){
		return dao.deleteDevKeyGroupByIds(roleIds);
	}
	
	public List<DevKeyGroup> findDevKeyGroup(DevKeyGroupDto dto){
		return dao.findDevKeyGroup(dto);
	}
}
