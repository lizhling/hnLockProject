package com.hnctdz.aiLock.service.system;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.system.SysRes;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysResDto;

/** 
 * @ClassName SysResService.java
 * @Author WangXiangBo 
 */
@Service
public interface SysResService {
	
	/**
	 * 查询满足条件的功能菜单列表，带分页
	 */
	public DataPackage findPageSysRes(SysResDto dto,DataPackage dataPackage);
	
	/**
	 * 根据ID获取功能菜单信息
	 */
	public SysRes getById(Long id);
	
	/**
	 * 保存功能菜单信息
	 */
	public void saveSysRes(SysRes sysRes);
	
	/**
	 * 批量删除功能菜单信息
	 */
	public String deleteSysResByIds(String ids)throws Exception;
	
	/**
	 * 查询功能菜单树
	 */
	public List<SysRes> findSysResList(SysResDto dto);
	
	/**
	 * 查询登录用户角色拥有的功能菜单树
	 */
	public List<SysRes> findMenuResByRoleIds(List<Long> userInRoleIds, int resType);
}
