package com.hnctdz.aiLock.service.device.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.device.DevKeyInfoDao;
import com.hnctdz.aiLock.dao.info.OrgInfoDao;
import com.hnctdz.aiLock.dao.info.PersonnelInfoDao;
import com.hnctdz.aiLock.domain.device.DevKeyInfo;
import com.hnctdz.aiLock.domain.info.OrgInfo;
import com.hnctdz.aiLock.domain.info.PersonnelInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.DevKeyInfoDto;
import com.hnctdz.aiLock.service.device.DevKeyInfoService;

/** 
 * @ClassName DevKeyInfoServiceImpl.java
 * @Author WangXiangBo 
 */
@Service("com.hnctdz.aiLock.service.device.DevKeyInfoService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class DevKeyInfoServiceImpl implements DevKeyInfoService{
	@Autowired
	private DevKeyInfoDao dao;
	@Autowired
	private PersonnelInfoDao personnelInfoDao;
	@Autowired
 	private OrgInfoDao orgInfoDao;
	
	public DataPackage findPageDevKeyInfo(DevKeyInfoDto dto, DataPackage dp){
		dp = this.dao.findPageDevKeyInfo(dto, dp);
		List<DevKeyInfo> perIfoList = (List<DevKeyInfo>)dp.getRows();
		for(DevKeyInfo key : perIfoList){
			OrgInfo org = orgInfoDao.getById(key.getOrgId());
			if(org != null){
				key.setOrgName(org.getOrgName());
			}
			if(key.getPerId() != null){
				PersonnelInfo perInfo = personnelInfoDao.getById(key.getPerId());
				if(perInfo!=null){
					key.setPerName(perInfo.getPerName());
				}
			}
		}
		dp.setRows(perIfoList);
		return dp;
	}
	
	public DevKeyInfo getById(Long keyId){
		return this.dao.getById(keyId);
	}
	
	public void saveDevKeyInfo(DevKeyInfo DevKeyInfo){
		this.dao.save(DevKeyInfo);
	}
	
	public List<DevKeyInfo> findDevKeyInfoList(DevKeyInfoDto dto){
		return this.dao.findDevKeyInfoList(dto);
	}
	
	public DevKeyInfo getDevKeyInfoByKeyCode(String keyCode){
		return dao.getDevKeyInfoByKeyCode(keyCode);
	}
	
	public List findKeyInfoOptions(DevKeyInfoDto dto){
		return this.dao.findKeyInfoOptions(dto);
	}
	
	public List getSecretKeyToDevkey(String devKeyCode){
		return this.dao.getSecretKeyToDevkey(devKeyCode);
	}
	
	public void deleteDevKeyInfoByIds(String keyIds){
		String[] keyIdArrey = keyIds.split(",");
		for(String keyId : keyIdArrey){
			try{
				DevKeyInfo devKeyInfo = dao.getById(Long.parseLong(keyId));
				devKeyInfo.setStatus(5L);//删除标记
				dao.save(devKeyInfo);
			} catch (Exception e) {
			}
		}
	}
	public void deleteRealDevKeyInfoByIds(String keyIds) {
		String[] keyIdArrey = keyIds.split(",");
		for(String keyId : keyIdArrey){
			try{
				DevKeyInfo devKeyInfo = dao.getById(Long.parseLong(keyId));
				dao.delete(devKeyInfo);
			} catch (Exception e) {
			}
		}
	}
}
