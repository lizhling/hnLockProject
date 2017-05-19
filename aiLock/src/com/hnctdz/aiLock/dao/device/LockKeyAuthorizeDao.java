package com.hnctdz.aiLock.dao.device;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.device.LockKeyAuthorize;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.LockKeyAuthorizeDto;

/** 
 * @ClassName LockKeyAuthorizeDao.java
 * @Author WangXiangBo 
 */
@Repository
public interface LockKeyAuthorizeDao extends GenericDAO<LockKeyAuthorize, Long>{
	
	public DataPackage findPageLockKeyAuthorize(LockKeyAuthorizeDto dto, DataPackage dp);
	
	public List<LockKeyAuthorize> findLockKeyAuthorizeList(LockKeyAuthorizeDto dto);
	
	public List<LockKeyAuthorize> findLockKeyAuthorizeByPerId(Long perId);
	
	public List<LockKeyAuthorize> findLockKeyAuthorizeByCode(String authorizeCode);
	
	public void saveLka(LockKeyAuthorize lockKeyAuthorize);
	
	public void updateLka(LockKeyAuthorize lockKeyAuthorize);

//	public void deleteOfBatch(List<LockKeyAuthorize> listForDel);
	
	public void updateAuthorizeIds(LockKeyAuthorize authorizeInfo, String authorizeIds);
	
//	public void deleteAuthorizeIds(String authorizeCode, String authorizeIds);
//	
	public void deleteAuthorize(LockKeyAuthorizeDto dto);
//	
//	public void deleteAuthorizeByLockId(String lcokIds);
	
	public List findAuthorizeLockList(String authorizeCode);
	
	public List findAuthorizePerList(String authorizeCode);
	
}
