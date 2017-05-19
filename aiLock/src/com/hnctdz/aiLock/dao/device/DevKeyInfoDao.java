package com.hnctdz.aiLock.dao.device;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.device.DevKeyInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.DevKeyInfoDto;

/** 
 * @ClassName DevKeyInfoDao.java
 * @Author WangXiangBo 
 */
@Repository
public interface DevKeyInfoDao extends GenericDAO<DevKeyInfo, Long>{
	
	public DataPackage findPageDevKeyInfo(DevKeyInfoDto dto, DataPackage dp);
	
	public List<DevKeyInfo> findDevKeyInfoList(DevKeyInfoDto dto);
	
	public List findKeyInfoOptions(DevKeyInfoDto dto);
	
	public DevKeyInfo getDevKeyInfoByKeyCode(String keyCode);
	
	public List getSecretKeyToDevkey(String devKeyCode);
	
}
