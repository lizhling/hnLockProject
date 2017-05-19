package com.hnctdz.aiLock.service.device;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.DevLockInGroupDto;

/** 
 * @ClassName DevLockInGroupService.java
 * @Author WangXiangBo 
 */
@Service
public interface DevLockInGroupService {
	
	public DataPackage findPageDevLockInGroup(DevLockInGroupDto dto, DataPackage dp);

	public boolean saveDevLockInGroup(Long groupId, String lockIds)throws Exception;
}
