package com.hnctdz.aiLock.dao.system;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.system.SysRole;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysRoleDto;

/** 
 * @ClassName SysRoleDao.java
 * @Author WangXiangBo 
 */
@Repository
public interface SysRoleDao extends GenericDAO<SysRole, Long>{
	
	/**
	 * 查询满足条件的角色列表，带分页
	 */
	public DataPackage findPageSysRole(SysRoleDto dto,DataPackage dp);
	
	/**
	 * 删除角色信息
	 */
	public String deleteSysRoleByIds(String roleIds);
	
	/**
	 * 查询满足条件的角色list
	 */
	public List<SysRole> findSysRole(SysRoleDto dto);
	
}
