package com.hnctdz.aiLock.service.system;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.system.SysRoleRes;

/** 
 * @ClassName SysRoleResService.java
 * @Author WangXiangBo 
 */
@Service
public interface SysRoleResService {
	/**
	 * 查询角色拥有的系统资源
	 */
	public List<SysRoleRes> findSysResByRole(Long roleId);
	
	/**
	 * 保存/修改角色拥有的系统资源
	 */
	public boolean saveSysRoleRes(Long roleId, String resIds)throws Exception;
}
