package com.hnctdz.aiLock.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.system.AppMenuPermissionsDao;
import com.hnctdz.aiLock.domain.system.AppMenuPermissions;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.AppMenuPermissionsDto;
import com.hnctdz.aiLock.service.system.AppMenuPermissionsService;

/** 
 * @ClassName AppMenuPermissionsServiceImpl.java
 * @Author WangXiangBo 
 */
@Service("com.hnctdz.aiLock.service.system.AppMenuPermissionsService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class AppMenuPermissionsServiceImpl implements AppMenuPermissionsService{
	
	@Autowired
	private AppMenuPermissionsDao AppMenuPermissionsDao;
	
	public DataPackage findPageAppMenuPermissions(AppMenuPermissionsDto dto,DataPackage dp){
		return AppMenuPermissionsDao.findPageAppMenuPermissions(dto, dp);
	}
	
	public List<AppMenuPermissions> findAppMenuPermissionsList(AppMenuPermissionsDto dto){
		return AppMenuPermissionsDao.findAppMenuPermissionsList(dto);
	}
	
	public List findAppMenuPermissionsOptions(AppMenuPermissionsDto dto){
		return AppMenuPermissionsDao.findAppMenuPermissionsOptions(dto);
	}
	
	public void saveAppMenuPermissions(AppMenuPermissions appMenuPermissions) throws Exception{
		AppMenuPermissionsDao.save(appMenuPermissions);
	}
	
	public String deleteAppMenuPermissionsByIds(String ids)throws Exception{
		return AppMenuPermissionsDao.deleteAppMenuPermissionsByIds(ids);
	}
}
