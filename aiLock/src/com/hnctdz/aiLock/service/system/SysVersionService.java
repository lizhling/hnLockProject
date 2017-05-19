package com.hnctdz.aiLock.service.system;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.system.SysVersion;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysVersionDto;

/** 
 * @ClassName SysVersionService.java
 * @Author WangXiangBo 
 */
@Service
public interface SysVersionService {
	/**
	 * 查询满足条件的版本信息列表，带分页
	 */
	public DataPackage findPageSysVersion(SysVersionDto dto,DataPackage dataPackage);
	
	/**
	 * 查询满足条件的版本信息List
	 */
	public List<SysVersion> findSysVersionList(SysVersionDto dto);
	
	/**
	 * 根据ID获取版本信息信息
	 */
	public SysVersion getById(Integer id);
	
	/**
	 * 保存版本信息信息
	 */
	public void saveSysVersion(SysVersion SysVersion);
	
	/**
	 * 批量删除版本信息信息
	 */
	public String deleteSysVersionByIds(String ids)throws Exception;
	
	/**
	 * 检测版本号是否已存在
	 */
	public boolean checkSysVersionCode(Integer SysVersionOs,String SysVersionCode,Integer SysVersionId);
	
	/**
	 * 获取客户端系统的最新版本
	 */
	public SysVersion getNewSysVersion(Integer SysVersionOs);
	
}
