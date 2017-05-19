package com.hnctdz.aiLock.service.system;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.system.AppMenuPermissions;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.AppMenuPermissionsDto;

/** 
 * @ClassName AppMenuPermissionsService.java
 * @Author WangXiangBo 
 */
@Service
public interface AppMenuPermissionsService {
	public DataPackage findPageAppMenuPermissions(AppMenuPermissionsDto dto,DataPackage dataPackage);
	
	public List<AppMenuPermissions> findAppMenuPermissionsList(AppMenuPermissionsDto dto);
	
	public List findAppMenuPermissionsOptions(AppMenuPermissionsDto dto);
	
	public void saveAppMenuPermissions(AppMenuPermissions AppMenuPermissions) throws Exception;
	
	public String deleteAppMenuPermissionsByIds(String ids)throws Exception;
	
}

