package com.hnctdz.aiLock.dao.system;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.system.SysRes;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysResDto;

/** 
 * @ClassName SysResDao.java
 * @Author WangXiangBo 
 */
@Repository
public interface SysResDao extends GenericDAO<SysRes, Long>{
	
	/**
	 * 查询满足条件的功能菜单列表，带分页
	 */
	public DataPackage findPageSysRes(SysResDto dto,DataPackage dp);
	
	/**
	 * 批量删除功能菜单信息
	 */
	public String deleteSysResByIds(String ids) throws Exception;
	
	/**
	 * 查询满足条件的功能菜单list
	 */
	public List<SysRes> findSysResList(SysResDto dto);
	
	/**
	 * 查询登录用户角色拥有的功能菜单树
	 */
	public List<SysRes> findMenuResByRoleIds(List<Long> userInRoleIds, int resType);
	
}
