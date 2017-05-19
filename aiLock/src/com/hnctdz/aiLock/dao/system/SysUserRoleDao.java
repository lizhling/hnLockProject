package com.hnctdz.aiLock.dao.system;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.system.SysUserRole;
import com.hnctdz.aiLock.dto.system.SysUserDto;

/** 
 * @ClassName SysUserInRoleDao.java
 * @Author WangXiangBo 
 */
@Repository
public interface SysUserRoleDao extends GenericDAO<SysUserRole, Long>{
	
	/**
	 * 通过用户Id查询用户所拥有的角色
	 */
	public List<SysUserRole> findRoleByUserId(Long userId);
	
	/**
	 * 根据用户所在组织查询所拥有的组织架构权限
	 */
	public String findOrgPermissionByUserOrg(Long orgId);
	
	public void saveOfBatch(List<SysUserRole> businessServiceListForSave);

	public void deleteOfBatch(List<SysUserRole> businessServiceListForDel);
	
}
