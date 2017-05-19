package com.hnctdz.aiLock.service.info.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.device.DevKeyInfoDao;
import com.hnctdz.aiLock.dao.device.DevLockInfoDao;
import com.hnctdz.aiLock.dao.info.OrgInfoDao;
import com.hnctdz.aiLock.dao.info.PersonnelInfoDao;
import com.hnctdz.aiLock.dao.system.SysUserDao;
import com.hnctdz.aiLock.domain.info.OrgInfo;
import com.hnctdz.aiLock.domain.system.SysUser;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.DevKeyInfoDto;
import com.hnctdz.aiLock.dto.device.DevLockInfoDto;
import com.hnctdz.aiLock.dto.info.OrgInfoDto;
import com.hnctdz.aiLock.dto.info.PersonnelInfoDto;
import com.hnctdz.aiLock.dto.system.SysAreaDto;
import com.hnctdz.aiLock.dto.system.SysUserDto;
import com.hnctdz.aiLock.service.info.OrgInfoService;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName OrgInfoServiceImpl.java
 * @Author WangXiangBo 
 */
@Service("com.hnctdz.aiLock.service.info.OrgInfoService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class OrgInfoServiceImpl implements OrgInfoService{
	@Autowired
	private OrgInfoDao dao;
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private PersonnelInfoDao personnelInfoDao;
	@Autowired
	private DevKeyInfoDao devKeyInfoDao;
	@Autowired
	private DevLockInfoDao devLockInfoDao;
	
	public OrgInfo getOrgInfoById(Long orgId){
		return this.dao.getById(orgId);
	}
	
	public DataPackage findPageOrgInfo(OrgInfoDto dto, DataPackage dp){
		dp = this.dao.findPageOrgInfo(dto, dp);
		List<OrgInfo> perIfoList = (List<OrgInfo>)dp.getRows();
		
		for(OrgInfo org : perIfoList){
			if(org.getOrgParentId() != null){
				OrgInfo perorg = dao.getById(org.getOrgParentId());
				org.setOrgParentName(perorg.getOrgName());
			}
			
			if(org.getUserId() != null){
				SysUser sysUser = sysUserDao.getById(org.getUserId());
				org.setUserName(sysUser.getName());
			}
		}
		
		return dp;
	}
	
	public String saveOrgInfo(OrgInfo orgInfo){
		String results = "";
		OrgInfoDto dto = new OrgInfoDto();
		dto.setOrgName(orgInfo.getOrgName());
		List<OrgInfo> list = this.dao.findOrgInfoList(dto);
		for (OrgInfo dbOrg : list) {
			if ((!dbOrg.getOrgName().equalsIgnoreCase(orgInfo.getOrgName())) 
					|| ((orgInfo.getOrgId() != null) && (dbOrg.getOrgId().equals(orgInfo.getOrgId())))){
				continue;
			}
			results = "该组织名称已存在，请重新输入！";
		}
		if(null != orgInfo.getOrgId() && null != orgInfo.getOrgParentId()){
			if(orgInfo.getOrgId().equals(orgInfo.getOrgParentId())){
				results = "上级组织不能为当前组织！";
			}else{
				List<OrgInfo> lowerNodesList = this.dao.findSysAreaLowerNodesList(orgInfo.getOrgId());
				for (OrgInfo lnOrg : lowerNodesList) {
					if (orgInfo.getOrgParentId().equals(lnOrg.getOrgId())){
						results = "上级组织不能为当前的下级组织，请重新选择！";
						continue;
					}
				}
			}
		}
		if(StringUtil.isEmpty(results)) {
			this.dao.merge(orgInfo);
		}
		return results;
	}
	
	public String deleteOrgInfoByIds(String orgIds){
		String results  = "";
		String[] orgIdArrey = orgIds.split(",");
		for(String orgIdst : orgIdArrey){
			Long orgId = Long.parseLong(orgIdst);
			OrgInfo orgInfo = dao.getById(orgId);
			try{
				PersonnelInfoDto pDto = new PersonnelInfoDto();
				pDto.setOrgId(orgId);
				if(personnelInfoDao.findPersonnelInfoList(pDto).size() > 0){
					results += orgInfo.getOrgName() + "、";
					break;
				}
				
				DevKeyInfoDto dto = new DevKeyInfoDto();
				dto.setOrgId(orgId);
				if(devKeyInfoDao.findDevKeyInfoList(dto).size() > 0){
					results += orgInfo.getOrgName() + "、";
					break;
				}
				
				DevLockInfoDto liDto = new DevLockInfoDto();
				liDto.setOrgId(orgId);
				if(devLockInfoDao.findDevLockInfoList(liDto).size() > 0){
					results += orgInfo.getOrgName() + "、";
					break;
				}
				
				SysUserDto sudto = new SysUserDto();
				sudto.setOrgId(orgId);
				if(sysUserDao.findSysUserList(sudto).size() > 0){
					results += orgInfo.getOrgName() + "、";
					break;
				}
				
				dao.delete(orgInfo);
			} catch (Exception e) {
				results += orgInfo.getOrgName() + "、";
			}
		}
		if(StringUtil.isNotEmpty(results)){
			results = "删除的组织中：" + results + "有关联数据，不能删除，请先清除关连数据！";
		}
		return results;
	}
	
	public List<OrgInfo> findOrgInfoList(OrgInfoDto dto){
		return this.dao.findOrgInfoList(dto);
	}
}
