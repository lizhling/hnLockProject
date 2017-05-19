package com.hnctdz.aiLock.service.device;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.device.DevKeyInfo;
import com.hnctdz.aiLock.domain.device.LockKeyAuthorize;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.LockKeyAuthorizeDto;
import com.hnctdz.aiLock.utils.ErrorCodeException;

/** 
 * @ClassName LockKeyAuthorizeService.java
 * @Author WangXiangBo 
 */
@Service
public interface LockKeyAuthorizeService {
	
	public DataPackage findPageLockKeyAuthorize(LockKeyAuthorizeDto dto, DataPackage dp);
	
	public List findTurnAuthorization(LockKeyAuthorizeDto dto, DataPackage dp);
	
	public LockKeyAuthorize getById(Long id);
	
	public List<LockKeyAuthorize> findLockKeyAuthorizeByPerId(Long perId);
	
	public String saveLockKeyAuthorize(LockKeyAuthorize LockKeyAuthorize) throws ErrorCodeException;
	
	/**
	 * 如果已知钥匙列表，则删除权限时不需要再查询人员的钥匙，否则会查询人员所持有的钥匙
	 * @param keyList 人员所持有的钥匙list
	 */
	public boolean deleteCardPer(LockKeyAuthorize lka, List<DevKeyInfo> keyList)throws ErrorCodeException;
	
	public void deleteAuthorize(LockKeyAuthorizeDto dto);
	
	public List findAuthorizeLockList(String authorizeCode);
	
	public List findAuthorizePerList(String authorizeCode);
}
