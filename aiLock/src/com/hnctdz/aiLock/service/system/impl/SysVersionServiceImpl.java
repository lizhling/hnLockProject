package com.hnctdz.aiLock.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.system.SysVersionDao;
import com.hnctdz.aiLock.domain.system.SysVersion;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysVersionDto;
import com.hnctdz.aiLock.service.system.SysVersionService;

/** 
 * @ClassName SysVersionServiceImpl.java
 * @Author WangXiangBo 
 */
@Service("com.hnctdz.aiLock.service.app.SysVersionService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class SysVersionServiceImpl implements SysVersionService{
	@Autowired
	private SysVersionDao SysVersionDao;
	
	public DataPackage findPageSysVersion(SysVersionDto dto,DataPackage dp){
		return SysVersionDao.findPageSysVersion(dto, dp);
	}
	
	public List<SysVersion> findSysVersionList(SysVersionDto dto){
		return SysVersionDao.findSysVersionList(dto);
	}
	
	public SysVersion getById(Integer id){
		return SysVersionDao.getById(id);
	}
	
	public void saveSysVersion(SysVersion SysVersion){
		SysVersionDao.save(SysVersion);
	}
	
	public String deleteSysVersionByIds(String ids)throws Exception{
		return SysVersionDao.deleteSysVersionByIds(ids);
	}
	
	public boolean checkSysVersionCode(Integer versionOs, String versionCode,Integer versionId){
		return SysVersionDao.checkSysVersionCode(versionOs, versionCode, versionId);
	}

	public SysVersion getNewSysVersion(Integer versionOs){
		return SysVersionDao.getNewSysVersion(versionOs);
	}
}
