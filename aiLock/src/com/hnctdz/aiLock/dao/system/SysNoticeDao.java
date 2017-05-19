package com.hnctdz.aiLock.dao.system;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.system.SysNotice;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysNoticeDto;

/** 
 * @ClassName SysNoticeDao.java
 * @Author WangXiangBo 
 */
@Repository
public interface SysNoticeDao extends GenericDAO<SysNotice, Long>{
	public DataPackage findPageSysNotice(SysNoticeDto dto,DataPackage dp);
	
	public List<SysNotice> findSysNoticeList(SysNoticeDto dto);
	
	public String deleteSysNoticeByIds(String ids) throws Exception;
	
}
