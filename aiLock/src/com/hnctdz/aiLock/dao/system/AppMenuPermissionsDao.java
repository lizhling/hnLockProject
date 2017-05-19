package com.hnctdz.aiLock.dao.system;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.system.AppMenuPermissions;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.AppMenuPermissionsDto;

/** 
 * @ClassName AppMenuPermissionsDao.java
 * @Author WangXiangBo 
 */
@Repository
public interface AppMenuPermissionsDao extends GenericDAO<AppMenuPermissions, Long>{
	public DataPackage findPageAppMenuPermissions(AppMenuPermissionsDto dto,DataPackage dp);
	
	public List<AppMenuPermissions> findAppMenuPermissionsList(AppMenuPermissionsDto dto);
	
	public List findAppMenuPermissionsOptions(AppMenuPermissionsDto dto);
	
	public String deleteAppMenuPermissionsByIds(String ids) throws Exception;
	
}
