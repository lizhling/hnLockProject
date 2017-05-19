package com.hnctdz.aiLock.dao.system;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.system.SysVersion;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysVersionDto;

/** 
 * @ClassName SysVersionDao.java
 * @Author WangXiangBo 
 */
@Repository
public interface SysVersionDao extends GenericDAO<SysVersion, Integer>{
	/**
	 * 查询满足条件的版本信息列表，带分页
	 */
	public DataPackage findPageSysVersion(SysVersionDto dto,DataPackage dp);
	
	/**
	 * 查询满足条件的版本信息List
	 */
	public List<SysVersion> findSysVersionList(SysVersionDto dto);
	
	/**
	 * 批量删除版本信息
	 */
	public String deleteSysVersionByIds(String ids) throws Exception;
	
	/**
	 * 检测版本号是否已存在
	 */
	public boolean checkSysVersionCode(Integer versionOs,String versionCode,Integer versionId);
	
	/**
	 * 获取客户端系统的最新版本
	 */
	public SysVersion getNewSysVersion(Integer versionOs);
}
