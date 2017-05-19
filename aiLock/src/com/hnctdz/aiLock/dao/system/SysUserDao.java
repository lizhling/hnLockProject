package com.hnctdz.aiLock.dao.system;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.system.SysUser;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysUserDto;

/** 
 * @ClassName SysUserDao.java
 * @Author WangXiangBo 
 */
@Repository
public interface SysUserDao extends GenericDAO<SysUser, Long>{
	
	/**
	 * 通过用户名查找用户
	 */
	public SysUser getSysUserByName(String userName);
	
	/**
	 * 查询系统用户列表
	 */
	public DataPackage findPageSysUser(SysUserDto dto, DataPackage dp);
	
	public List<SysUser> findSysUserList(SysUserDto dto);
	
	public List findSysUserOptions(SysUserDto dto);
}
