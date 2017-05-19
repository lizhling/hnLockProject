package com.hnctdz.aiLock.dao.info;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.info.PersonnelInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.info.PersonnelInfoDto;

/** 
 * @ClassName PersonnelInfoDao.java
 * @Author WangXiangBo 
 */
@Repository
public interface PersonnelInfoDao extends GenericDAO<PersonnelInfo, Long>{
	
	public DataPackage findPagePersonnelInfo(PersonnelInfoDto dto, DataPackage dp);
	
	public List<PersonnelInfo> findPersonnelInfoList(PersonnelInfoDto dto);
	
	public List findPersonnelInfoOptions(PersonnelInfoDto dto);
	
	public PersonnelInfo getPersonnelByAccounts(String perAccounts);
	
	public PersonnelInfo getPersonnelInfoByKeyCode(String keyCode);
	
	public List findAccountBySysUserAndPer(String account);
	
	public void updateResetPerPassword(Long perId, String password);
	
	public String getMaxSmartKeyPerId();

	/**
	 * 获取用户信息列表
	 * @param dto
	 * @return
	 */
	public List<PersonnelInfo> getPersonnelInfos(PersonnelInfoDto dto);

}
