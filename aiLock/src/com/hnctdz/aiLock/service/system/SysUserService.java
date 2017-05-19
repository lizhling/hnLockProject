package com.hnctdz.aiLock.service.system;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.system.SysUser;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysUserDto;

/** 
 * @ClassName SysUserService.java
 * @Author WangXiangBo 
 */
@Service
public interface SysUserService {
	/**
	 * 通过用户名查找用户
	 */
	public SysUser getSysUserByName(String userName);
	
	/**
	 * 通过ID查找用户
	 */
	public SysUser getSysUserById(Long id);
	
	/**
	 * 查询系统用户列表
	 */
	public DataPackage findPageSysUser(SysUserDto dto, DataPackage dp);
	
	/**
	 * 保存系统用户信息
	 */
	public void saveSysRes(SysUser sysUser);
	
	/**
	 * 批量删除系统用户
	 */
	public void deleteSysUserByIds(String userIds);
	
	public List findSysUserOptions(SysUserDto dto);
}
