package com.hnctdz.aiLock.service.system;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.system.SysNotice;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysNoticeDto;

/** 
 * @ClassName SysNoticeService.java
 * @Author WangXiangBo 
 */
@Service
public interface SysNoticeService {
	public DataPackage findPageSysNotice(SysNoticeDto dto,DataPackage dataPackage);
	
	public List<SysNotice> findSysNoticeList(SysNoticeDto dto);
	
	public void saveSysNotice(SysNotice SysNotice) throws Exception;
	
	public String deleteSysNoticeByIds(String ids)throws Exception;
	
}

