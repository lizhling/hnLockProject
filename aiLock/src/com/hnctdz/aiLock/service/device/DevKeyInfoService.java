package com.hnctdz.aiLock.service.device;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.device.DevKeyInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.DevKeyInfoDto;

/** 
 * @ClassName DevKeyInfoService.java
 * @Author WangXiangBo 
 */
@Service
public interface DevKeyInfoService {
	
	public DataPackage findPageDevKeyInfo(DevKeyInfoDto dto, DataPackage dp);
	
	public DevKeyInfo getById(Long keyId);
	
	public void saveDevKeyInfo(DevKeyInfo DevKeyInfo);
	
	public List<DevKeyInfo> findDevKeyInfoList(DevKeyInfoDto dto);

	public DevKeyInfo getDevKeyInfoByKeyCode(String keyCode);
	
	public List findKeyInfoOptions(DevKeyInfoDto dto);
	
	public void deleteDevKeyInfoByIds(String keyIds);
	
	public void deleteRealDevKeyInfoByIds(String keyIds);
	
	public List getSecretKeyToDevkey(String devKeyCode);
}
