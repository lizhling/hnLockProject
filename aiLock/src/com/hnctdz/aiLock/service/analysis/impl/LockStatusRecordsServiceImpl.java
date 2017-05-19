package com.hnctdz.aiLock.service.analysis.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.analysis.LockStatusRecordsDao;
import com.hnctdz.aiLock.dao.device.DevLockInfoDao;
import com.hnctdz.aiLock.dao.info.OrgInfoDao;
import com.hnctdz.aiLock.dao.system.SysAreaDao;
import com.hnctdz.aiLock.dao.system.SysBasicDataDao;
import com.hnctdz.aiLock.domain.analysis.LockStatusRecords;
import com.hnctdz.aiLock.domain.analysis.UnlockRecords;
import com.hnctdz.aiLock.domain.device.DevLockInfo;
import com.hnctdz.aiLock.domain.info.OrgInfo;
import com.hnctdz.aiLock.domain.system.SysArea;
import com.hnctdz.aiLock.domain.system.SysBasicData;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.analysis.LockStatusRecordsDto;
import com.hnctdz.aiLock.service.analysis.LockStatusRecordsService;

/** 
 * @ClassName LockStatusRecordsServiceImpl.java
 * @Author WangXiangBo 
 */
@Service("com.hnctdz.aiLock.service.analysis.LockStatusRecordsService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class LockStatusRecordsServiceImpl implements LockStatusRecordsService{
	@Autowired
	private LockStatusRecordsDao dao;
	@Autowired
 	private DevLockInfoDao devLockInfoDao;
	@Autowired
 	private SysAreaDao sysAreaDao;
	@Autowired
 	private OrgInfoDao orgInfoDao;
	
	public DataPackage findPageLockStatusRecords(LockStatusRecordsDto dto, DataPackage dp){
		if(dto != null && dto.getQueryType() == 1){
			List<LockStatusRecords> allList = this.dao.findLockStatusRecordsList(dto);
			dp.setRows(allList);
		}else{
			dp = this.dao.findPageLockStatusRecords(dto, dp);
		}
		
		List<LockStatusRecords> list = (List<LockStatusRecords>)dp.getRows();
		for(LockStatusRecords unlr : list){
			DevLockInfo lock = devLockInfoDao.getDevLockInfoByLockCode(unlr.getLockCode());
			if(lock != null){
				unlr.setLockName(lock.getLockName());
				unlr.setLockCode(lock.getLockCode());
				
				SysArea sysArea = this.sysAreaDao.getById(lock.getAreaId());
			    if (sysArea != null) {
			    	unlr.setAreaName(sysArea.getAreaName());
			    }
				
				OrgInfo org = orgInfoDao.getById(lock.getOrgId());
				if(org != null){
					unlr.setOrgName(org.getOrgName());
				}
			}
		}
		dp.setRows(list);
		return dp;
	}
	
	public List<LockStatusRecords> findLockStatusRecordsList(LockStatusRecordsDto dto){
		List<LockStatusRecords> list = dao.findLockStatusRecordsList(dto);
		int i = 1;
		for(LockStatusRecords unlr : list){
			DevLockInfo lock = devLockInfoDao.getDevLockInfoByLockCode(unlr.getLockCode());
			if(lock != null){
				unlr.setLockName(lock.getLockName());
				unlr.setLockCode(lock.getLockCode());
			}
			i++;
			if(i > 5){
				break;
			}
		}
		return list;
	}
	
	public LockStatusRecords findLockStatusLastRecords(String lockCode){
		return dao.findLockStatusLastRecords(lockCode);
	}
}
