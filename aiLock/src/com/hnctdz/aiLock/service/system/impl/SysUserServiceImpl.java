package com.hnctdz.aiLock.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.info.OrgInfoDao;
import com.hnctdz.aiLock.dao.system.SysUserDao;
import com.hnctdz.aiLock.dao.system.SysUserRoleDao;
import com.hnctdz.aiLock.domain.info.OrgInfo;
import com.hnctdz.aiLock.domain.system.SysUser;
import com.hnctdz.aiLock.domain.system.SysUserRole;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysUserDto;
import com.hnctdz.aiLock.service.system.SysUserService;

/** 
 * @ClassName SysUserServiceImpl.java
 * @Author WangXiangBo 
 */
@Service("com.hnctdz.aiLock.service.system.SysUserService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class SysUserServiceImpl implements SysUserService{
	
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	@Autowired
 	private OrgInfoDao orgInDao;
	
	public SysUser getSysUserByName(String userName){
		return sysUserDao.getSysUserByName(userName);
	}
	
	public SysUser getSysUserById(Long id){
		return sysUserDao.getById(id);
	}
	
	public DataPackage findPageSysUser(SysUserDto dto, DataPackage dp){
		dp = sysUserDao.findPageSysUser(dto, dp);
		List<SysUser> userList = (List<SysUser>)dp.getRows();
		for(SysUser use : userList){
			List<SysUserRole> roleList = sysUserRoleDao.findRoleByUserId(use.getUserId());
			StringBuffer roles = new StringBuffer();
			String f = "";
			for(SysUserRole sr : roleList){
				roles.append(f).append(sr.getSysRole().getRoleId());
				f = ",";
			}
			if(roles.length() != 0){
				use.setRoleId(roles.toString());
			}
			
			OrgInfo org = orgInDao.getById(use.getOrgId());
			use.setOrgName(org.getOrgName());
			
		}
		
		dp.setRows(userList);
		return dp;
	}

	public List findSysUserOptions(SysUserDto dto){
		return sysUserDao.findSysUserOptions(dto);
	}
	
	public void saveSysRes(SysUser sysUser){
		sysUserDao.save(sysUser);
	}
	
	public void deleteSysUserByIds(String userIds){
		String[] userIdArrey = userIds.split(",");
		for(String userId : userIdArrey){
			SysUser sysUser = sysUserDao.getById(Long.parseLong(userId));
			try{
				List<SysUserRole> roleList = sysUserRoleDao.findRoleByUserId(Long.parseLong(userId));
				if(sysUser!=null && roleList.size()!=0){
					sysUser.setStatus(3L);//删除标记
					sysUserDao.save(sysUser);
				} else {
					sysUserDao.delete(sysUser);
				}
			}catch(Exception e){
				sysUser.setStatus(3L);//删除标记
				sysUserDao.save(sysUser);
			}
		}
	}
}
