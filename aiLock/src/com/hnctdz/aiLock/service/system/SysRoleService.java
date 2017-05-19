package com.hnctdz.aiLock.service.system;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.system.SysRole;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysRoleDto;

/** 
 * @ClassName SysRoleService.java
 * @Author WangXiangBo 
 */
@Service
public interface SysRoleService {
	
	/**
	 * 查询满足条件的角色列表，带分页
	 */
	public DataPackage findPageSysRole(SysRoleDto dto,DataPackage dp);
	
	/**
	 * 保存角色信息
	 */
	public void saveSysRole(SysRole sysRole);
	
	/**
	 * 删除角色信息
	 */
	public String deleteSysRoleByIds(String roleIds);
	
	/**
	 * 查询满足条件的角色list
	 */
	public List<SysRole> findSysRole(SysRoleDto dto);

	/**
	 * 跟据用户ID查询用户解色
	 * @param userId
	 * @return 
	 */
	public List<Object> findSysRoleByUserid(Long userId);
	
}
