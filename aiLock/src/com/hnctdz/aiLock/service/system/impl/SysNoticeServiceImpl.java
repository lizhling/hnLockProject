package com.hnctdz.aiLock.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.system.SysNoticeDao;
import com.hnctdz.aiLock.dao.system.SysUserDao;
import com.hnctdz.aiLock.domain.system.SysNotice;
import com.hnctdz.aiLock.domain.system.SysUser;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysNoticeDto;
import com.hnctdz.aiLock.service.system.SysNoticeService;

/** 
 * @ClassName SysNoticeServiceImpl.java
 * @Author WangXiangBo 
 */
@Service("com.hnctdz.aiLock.service.system.SysNoticeService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class SysNoticeServiceImpl implements SysNoticeService{
	
	@Autowired
	private SysNoticeDao dao;
	@Autowired
	private SysUserDao sysUserDao;
	
	public DataPackage findPageSysNotice(SysNoticeDto dto,DataPackage dp){
		dp = this.dao.findPageSysNotice(dto, dp);
		List<SysNotice> list = (List<SysNotice>)dp.getRows();
		for(SysNotice notice : list){
			SysUser user = sysUserDao.getById(notice.getUserId());
			if(user != null){
				notice.setUserName(user.getName());
			}
		}
		dp.setRows(list);
		return dp;
	}
	
	public List<SysNotice> findSysNoticeList(SysNoticeDto dto){
		return dao.findSysNoticeList(dto);
	}
	
	public void saveSysNotice(SysNotice SysNotice) throws Exception{
		dao.save(SysNotice);
	}
	
	public String deleteSysNoticeByIds(String ids)throws Exception{
		return dao.deleteSysNoticeByIds(ids);
	}
}
