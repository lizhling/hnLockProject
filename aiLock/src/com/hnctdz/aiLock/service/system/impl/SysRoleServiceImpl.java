package com.hnctdz.aiLock.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.system.SysRoleDao;
import com.hnctdz.aiLock.domain.system.SysRole;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysRoleDto;
import com.hnctdz.aiLock.service.system.SysRoleService;

/** 
 * @ClassName SysRoleServiceImpl.java
 * @Author WangXiangBo 
 */
@Service("com.hnctdz.aiLock.service.system.SysRoleService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class SysRoleServiceImpl implements SysRoleService{
	@Autowired
	private SysRoleDao sysRoleDao;
	
	public DataPackage findPageSysRole(SysRoleDto dto,DataPackage dp){
		return sysRoleDao.findPageSysRole(dto, dp);
	}
	
	public void saveSysRole(SysRole sysRole){
		sysRoleDao.save(sysRole);
	}
	
	public String deleteSysRoleByIds(String roleIds){
		return sysRoleDao.deleteSysRoleByIds(roleIds);
	}
	
	public List<SysRole> findSysRole(SysRoleDto dto){
		return sysRoleDao.findSysRole(dto);
	}
	
	public List<Object> findSysRoleByUserid(Long userId) {
		
		String queryStr = "select b.* from SYS_USER_ROLE a,sys_role b  where a.ROLE_ID=b.ROLE_ID and a.USER_ID=?";
		Object[] args = {userId};
		List<Object> sr = null;
		try {
			sr = (List<Object>) this.sysRoleDao.getObjectList(queryStr, args);
			if(sr != null){
				return sr;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sr;
	}
}
