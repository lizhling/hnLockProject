package com.hnctdz.aiLock.service.info;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.info.PersonnelInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.info.PersonnelInfoDto;

/** 
 * @ClassName PersonnelInfoService.java
 * @Author WangXiangBo 
 */
@Service
public interface PersonnelInfoService {
	public PersonnelInfo getPersonnelInfoById(Long perId);
	
	public DataPackage findPagePersonnelInfo(PersonnelInfoDto dto, DataPackage dp);
	
	public boolean checkPerExist(PersonnelInfo perInfo);
	
	public String getSmartKeyPerId();
	
	public PersonnelInfo savePersonnelInfo(PersonnelInfo personnelInfo);
	
	public String saveImportPersonnelInfos(File importFile);
	
	public void deletePersonnelInfoByIds(String preIds);
	
	public void deleteRealPersonnelInfoByIds(String perIds);
	
	public List findPersonnelInfoOptions(PersonnelInfoDto dto);
	
	public PersonnelInfo getPersonnelByAccounts(String perAccounts);
	
	public List findAccountBySysUserAndPer(String account);
	
	public void updateResetPerPassword(Long perId, String password);

	/**
	 * 导出用户信息
	 * @param request
	 * @param response
	 * @param dto
	 * @throws IOException 
	 */
	public void exportPersonInfo(HttpServletRequest request,
			HttpServletResponse response, PersonnelInfoDto dto) throws IOException;

	/**
	 * 导入用户数据到数据库
	 * @param importFile
	 */
	public String importPersonnelInfos(File importFile);
}
