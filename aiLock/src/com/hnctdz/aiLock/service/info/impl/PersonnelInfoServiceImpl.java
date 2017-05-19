package com.hnctdz.aiLock.service.info.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.device.DevKeyGroupDao;
import com.hnctdz.aiLock.dao.device.DevKeyInfoDao;
import com.hnctdz.aiLock.dao.device.DevLockInGroupDao;
import com.hnctdz.aiLock.dao.device.DevLockInfoDao;
import com.hnctdz.aiLock.dao.device.LockKeyAuthorizeDao;
import com.hnctdz.aiLock.dao.info.OrgInfoDao;
import com.hnctdz.aiLock.dao.info.PersonnelInfoDao;
import com.hnctdz.aiLock.domain.device.DevGroup;
import com.hnctdz.aiLock.domain.device.DevKeyGroup;
import com.hnctdz.aiLock.domain.device.DevKeyInGroup;
import com.hnctdz.aiLock.domain.device.DevKeyInfo;
import com.hnctdz.aiLock.domain.device.DevLockInGroup;
import com.hnctdz.aiLock.domain.device.DevLockInGroupId;
import com.hnctdz.aiLock.domain.device.DevLockInfo;
import com.hnctdz.aiLock.domain.device.LockKeyAuthorize;
import com.hnctdz.aiLock.domain.info.OrgInfo;
import com.hnctdz.aiLock.domain.info.PersonnelInfo;
import com.hnctdz.aiLock.domain.system.SysArea;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.DevKeyInfoDto;
import com.hnctdz.aiLock.dto.info.PersonnelInfoDto;
import com.hnctdz.aiLock.service.info.PersonnelInfoService;
import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.DateUtil;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.hnctdz.aiLock.utils.ExcelUtil;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName PersonnelInfoServiceImpl.java
 * @Author WangXiangBo 
 */
@Service("com.hnctdz.aiLock.service.info.PersonnelInfoService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class PersonnelInfoServiceImpl implements PersonnelInfoService{
	@Autowired
	private PersonnelInfoDao dao;
	@Autowired
 	private OrgInfoDao orgInDao;
	
	@Autowired
	private DevKeyInfoDao devKeyInfoDao;
	
	@Autowired
	private DevKeyGroupDao devKeyGroupdao;
	
	@Autowired
	private LockKeyAuthorizeDao lockAuthdao;
	
	@Autowired
	private DevLockInfoDao devLockInfodao;
	
	@Autowired
	private DevLockInGroupDao devLockGroupdao;
	
	public PersonnelInfo getPersonnelInfoById(Long perId){
		return this.dao.getById(perId);
	}
	
	public DataPackage findPagePersonnelInfo(PersonnelInfoDto dto, DataPackage dp){
		dp = this.dao.findPagePersonnelInfo(dto, dp);
		List<PersonnelInfo> perIfoList = (List<PersonnelInfo>)dp.getRows();
		
		for(PersonnelInfo perIfo : perIfoList){
			OrgInfo org = orgInDao.getById(perIfo.getOrgId());
			perIfo.setOrgName(org.getOrgName());
		}
		dp.setRows(perIfoList);
		return dp;
	}
	
	public boolean checkPerExist(PersonnelInfo perInfo){
		List list = dao.findAccountBySysUserAndPer(perInfo.getPerAccounts());
		if(list.size() > 0){
			Object[] obj = (Object[])list.get(0);
			if("1".equalsIgnoreCase(obj[0].toString())){
				return false;
			}
			if(perInfo.getPerId() == null || !obj[1].toString().equals(perInfo.getPerId().toString())){
				return false;
			}
		}
		return true;
	}
	
	public String getSmartKeyPerId(){
		String maxSmartKeyPerId =  dao.getMaxSmartKeyPerId();
		if(StringUtil.isNotEmpty(maxSmartKeyPerId)){
			Integer idInt = Integer.parseInt(maxSmartKeyPerId);
			idInt++;
			String newId = idInt.toString();
			int newIdLength = newId.length();
			switch (newIdLength) {
				case 1: newId = "00000" + newId; break;
				case 2: newId = "0000" + newId; break;
				case 3: newId = "000" + newId; break;
				case 4: newId = "00" + newId; break;
				case 5: newId = "0" + newId; break;
				default: break;
			}
			return newId;
		}
		return "000001";
	}
	
	public PersonnelInfo savePersonnelInfo(PersonnelInfo personnelInfo){
		this.dao.save(personnelInfo);
		return personnelInfo;
	}
	
	public String saveImportPersonnelInfos(File importFile) {
		String result = "";
		try {
			InputStream is = new FileInputStream(importFile);
			HSSFWorkbook workbook = new HSSFWorkbook(is);
			HSSFSheet sheet = workbook.getSheetAt(0);//第一个工作表
			
			String dataLack = "";
			String perExist = "";
			String orgNotExist = "";
			String numberFormat = "";
			String dateError = "";
			List<OrgInfo> orgList = orgInDao.findOrgInfoList(null);
			Map<String, Long> existOrg = new HashMap<String, Long>();
			Map<String, String> importPerMap = new HashMap<String, String>();
	
			Date date = new Date();
			for (int j = 3; j < sheet.getPhysicalNumberOfRows() + 1; j++) {//从第二行开始读
				try {
					HSSFRow rowline = sheet.getRow(j - 1);
					if(rowline == null) continue;
					
					String perName = null;
					String perAccounts = null;
					Long phoneNo = null;
					String address = null;
					String note = null;
					String orgName = null;
					
					for (int i = 0; i < rowline.getLastCellNum(); i++) {// 循环遍历所有列
						HSSFCell cell = rowline.getCell((short) i); // 取得当前Cell
						String cellvalueStr = null;
						if (cell != null) {
							switch (cell.getCellType()) {// 判断当前Cell的Type
								case HSSFCell.CELL_TYPE_NUMERIC : {// 如果当前Cell的Type为NUMERIC
									Double value = cell.getNumericCellValue();// 取得当前Cell的数值
									BigDecimal bigDecimal = new BigDecimal(value);//格式转换，避免以科学计数法表示
									cellvalueStr = bigDecimal.toString();
									break;
								}
								case HSSFCell.CELL_TYPE_STRING : // 如果当前Cell的Type为STRING
									cellvalueStr = cell.getStringCellValue(); // 取得当前的Cell字符串
									break;
							}
						}
						if(StringUtil.isNotEmpty(cellvalueStr)){
							switch (i) {
								case 0 : perName = cellvalueStr.trim(); break;
								case 1 : perAccounts = cellvalueStr.trim(); break;
								case 2 : orgName = cellvalueStr.trim(); break;
								case 3 : phoneNo = Long.parseLong(cellvalueStr.trim()); break;
								case 4 : address = cellvalueStr.trim(); break;
								case 5 : note = cellvalueStr.trim(); break;
							}
						}
					}
					
					PersonnelInfo personnelInfo = new PersonnelInfo();
					//判断名称、帐号、组织名称、电话是否为空，为空则不保存
					if(StringUtil.isEmpty(perName) || StringUtil.isEmpty(perAccounts) || 
							StringUtil.isEmpty(orgName)|| null == phoneNo){
						dataLack += j + "、";
						continue;
					}
					
					if(existOrg.get(orgName) == null){
						for(int h=0; h < orgList.size(); h++){
							OrgInfo org = orgList.get(h);
							if(orgName.equalsIgnoreCase(org.getOrgName())){
								existOrg.put(orgName, org.getOrgId());
								personnelInfo.setOrgId(org.getOrgId());
								break;
							}
						}
					}else{
						personnelInfo.setOrgId(existOrg.get(orgName));
					}
					
					if(personnelInfo.getOrgId() == null){
						orgNotExist += j + "、";
						continue;
					}
					
					personnelInfo.setPerName(perName);
					personnelInfo.setPerAccounts(perAccounts);
					
					if(importPerMap.containsValue(perAccounts) || importPerMap.get(perName) != null){
						perExist += j + "、";
						continue;
					}
					if(checkPerExist(personnelInfo)){
						personnelInfo.setPhoneNo(phoneNo.toString());
						personnelInfo.setAddress(address);
						personnelInfo.setNote(note);
						personnelInfo.setPerPassword(Constants.PER_DEFAULT_PASSW);
						personnelInfo.setStatus(1L);
						personnelInfo.setSmartKeyPerId(getSmartKeyPerId());
						personnelInfo.setSmartKeyPassw(Constants.DEFAULT_SMART_KEY_PASSW);
						personnelInfo.setCuTime(date);
						dao.save(personnelInfo);
						importPerMap.put(perName, perAccounts);
					}else{
						perExist += j + "、";
					}
				} catch(NumberFormatException e) {
					numberFormat += j + "、";
				} catch(Exception e) {
					e.printStackTrace();
					dateError += j + "、";
				}
			}
			result=logicProcess(dataLack,perExist, numberFormat, orgNotExist, dateError, result);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return result;
	}
	
	
	//-------------------------------数据库批量导入-----一建导入的相关方法------------------------------------START
	/* (non-Javadoc)
	 * @see com.hnctdz.aiLock.service.info.PersonnelInfoService#importPersonnelInfos(java.io.File)
	 */
	public String importPersonnelInfos(File importFile) {
		String result = "";
		try {
			InputStream is = new FileInputStream(importFile);
			HSSFWorkbook workbook = new HSSFWorkbook(is);
			importPersoninfo(workbook);   //1.导入人员信息
			importKeyinfo(workbook);	 //2.导入钥匙信息
			importKeyGroup(workbook);	//3.导入钥匙分组信息
			importLockKeyAuth(workbook); //4.导入授权信息
			importLock(workbook); //5.导入门锁信息
			importLockGroup(workbook); //改入门锁分组信息
			 
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return result;
	}
	private String importLockGroup(HSSFWorkbook workbook){
		HSSFSheet sheet = workbook.getSheetAt(5);//第六个工作表
		String result = "";
		String dataLack = "";
		String perExist = "";
		String orgNotExist = "";
		String numberFormat = "";
		String dateError = "";
		
		for (int j = 2; j < sheet.getPhysicalNumberOfRows() + 1; j++) {//从第二行开始读
			try {
				HSSFRow rowline = sheet.getRow(j - 1);
				if(rowline == null) continue;
				String lockId="";
				String groupId="";
				
				for (int i = 0; i < rowline.getLastCellNum(); i++) {// 循环遍历所有列
					HSSFCell cell = rowline.getCell((short) i); // 取得当前Cell
					String cellvalueStr = null;
					if (cell != null) {
						switch (cell.getCellType()) {// 判断当前Cell的Type
							case HSSFCell.CELL_TYPE_NUMERIC : {// 如果当前Cell的Type为NUMERIC
								Double value = cell.getNumericCellValue();// 取得当前Cell的数值
								BigDecimal bigDecimal = new BigDecimal(value);//格式转换，避免以科学计数法表示
								cellvalueStr = bigDecimal.toString();
								break;
							}
							case HSSFCell.CELL_TYPE_STRING : // 如果当前Cell的Type为STRING
								cellvalueStr = cell.getStringCellValue(); // 取得当前的Cell字符串
								break;
						}
					}
					if(StringUtil.isNotEmpty(cellvalueStr)){
						switch (i) {
						case 0 :  lockId = cellvalueStr.trim(); break;
						case 1 :  groupId = cellvalueStr.trim(); break;
						}
					}
				}
				String sql = "insert into DEV_LOCK_IN_GROUP(GROUP_ID,LOCK_ID)" +
								 " values (?,?)";
				Object[] args = {lockId,groupId};
				dao.ExecuteBySql(sql, args);
			} catch(NumberFormatException e) {
				e.printStackTrace();
				numberFormat += j + "、";
			} catch(Exception e) {
				e.printStackTrace();
				dateError += j + "、";
			}
		}
		result=logicProcess(dataLack, perExist, numberFormat, orgNotExist, dateError, result);
		return result;
	}
	
	private String importLock(HSSFWorkbook workbook){
		String result = "";
		HSSFSheet sheet = workbook.getSheetAt(4);//第五个工作表
		String dataLack = "";
		String perExist = "";
		String orgNotExist = "";
		String numberFormat = "";
		String dateError = "";
		
		for (int j = 2; j < sheet.getPhysicalNumberOfRows() + 1; j++) {//从第二行开始读
			try {
				HSSFRow rowline = sheet.getRow(j - 1);
				if(rowline == null) continue;
				String lockId="";
				String lockCode="";
				String lockName="";
				String unlockPassword="";
				String lockType="";
				String lockAddres="";
				String longitude="";
				String latitude="";
				String areaId="";
				String orgId="";
				String status="";
				String note="";
				String lockDeviceNo="";
				String lockInModuleCode="";
				String ipAddress="";
				String lockInBlueCode="";
				String wheCanMatchCard="";
				String vicePassiveLockCode="";
				String lockParentId="";
				String blueMac="";
				String privateKey="";
				
				for (int i = 0; i < rowline.getLastCellNum(); i++) {// 循环遍历所有列
					HSSFCell cell = rowline.getCell((short) i); // 取得当前Cell
					String cellvalueStr = null;
					if (cell != null) {
						switch (cell.getCellType()) {// 判断当前Cell的Type
							case HSSFCell.CELL_TYPE_NUMERIC : {// 如果当前Cell的Type为NUMERIC
								Double value = cell.getNumericCellValue();// 取得当前Cell的数值
								BigDecimal bigDecimal = new BigDecimal(value);//格式转换，避免以科学计数法表示
								cellvalueStr = bigDecimal.toString();
								break;
							}
							case HSSFCell.CELL_TYPE_STRING : // 如果当前Cell的Type为STRING
								cellvalueStr = cell.getStringCellValue(); // 取得当前的Cell字符串
								break;
						}
					}
					if(StringUtil.isNotEmpty(cellvalueStr)){
						switch (i) {
						case 0 :  lockId = cellvalueStr.trim(); break;
						case 1 :  lockCode =  cellvalueStr.trim(); break;
						case 2 :  lockName =  cellvalueStr.trim(); break;
						case 3 :  unlockPassword =  cellvalueStr.trim(); break;
						case 4 :  lockType =  cellvalueStr.trim(); break;
						case 5 :  lockAddres =  cellvalueStr.trim(); break;
						case 6 :  longitude =  cellvalueStr.trim(); break;
						case 7 :  latitude =  cellvalueStr.trim(); break;
						case 8 :  areaId =  cellvalueStr.trim(); break;
						case 9 :  orgId =  cellvalueStr.trim(); break;
						case 10 :  status =  cellvalueStr.trim(); break;
						case 11 :  note =  cellvalueStr.trim(); break;
						case 12 :  lockDeviceNo =  cellvalueStr.trim(); break;
						case 13 :  lockInModuleCode =  cellvalueStr.trim(); break;
						case 14 :  ipAddress =  cellvalueStr.trim(); break;
						case 15 :  lockInBlueCode =  cellvalueStr.trim(); break;
						case 16 :  wheCanMatchCard =  cellvalueStr.trim(); break;
						case 17 :  vicePassiveLockCode =  cellvalueStr.trim(); break;
						case 18 :  lockParentId =  cellvalueStr.trim();break;
						case 19 :  blueMac =  cellvalueStr.trim(); break;
						case 20 :  privateKey =  cellvalueStr.trim(); break;
						}
					}
				}
				if(StringUtil.isBlank(lockParentId)||"null".equals(lockParentId)){
					String sql = "insert into DEV_LOCK_INFO(LOCK_ID,LOCK_CODE,LOCK_NAME,UNLOCK_PASSWORD,LOCK_TYPE,LOCK_ADDRES,LONGITUDE,LATITUDE,AREA_ID,ORG_ID,STATUS,NOTE,LOCK_DEVICE_NO,LOCK_IN_MODULE_CODE,IP_ADDRESS,LOCK_IN_BLUE_CODE,WHE_CAN_MATCH_CARD,VICE_PASSIVE_LOCK_CODE,BLUE_MAC,PRIVATE_KEY)" +
					 " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					Object[] args = {lockId,lockCode,lockName,unlockPassword,lockType,lockAddres,longitude,latitude,areaId,orgId,status,note,lockDeviceNo,lockInModuleCode,ipAddress,lockInBlueCode,wheCanMatchCard,vicePassiveLockCode,blueMac,privateKey};
					dao.ExecuteBySql(sql, args);
				}else{
					String sql = "insert into DEV_LOCK_INFO(LOCK_ID,LOCK_CODE,LOCK_NAME,UNLOCK_PASSWORD,LOCK_TYPE,LOCK_ADDRES,LONGITUDE,LATITUDE,AREA_ID,ORG_ID,STATUS,NOTE,LOCK_DEVICE_NO,LOCK_IN_MODULE_CODE,IP_ADDRESS,LOCK_IN_BLUE_CODE,WHE_CAN_MATCH_CARD,VICE_PASSIVE_LOCK_CODE,LOCK_PARENT_ID,BLUE_MAC,PRIVATE_KEY)" +
					 " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					Object[] args = {lockId,lockCode,lockName,unlockPassword,lockType,lockAddres,longitude,latitude,areaId,orgId,status,note,lockDeviceNo,lockInModuleCode,ipAddress,lockInBlueCode,wheCanMatchCard,vicePassiveLockCode,lockParentId,blueMac,privateKey};
					dao.ExecuteBySql(sql, args);
				}
				
			} catch(NumberFormatException e) {
				e.printStackTrace();
				numberFormat += j + "、";
			} catch(Exception e) {
				e.printStackTrace();
				dateError += j + "、";
			}
		}
		result=logicProcess(dataLack, perExist, numberFormat, orgNotExist, dateError, result);
		return result;
	}
	
	private String importLockKeyAuth(HSSFWorkbook workbook){
		String result = "";
		HSSFSheet sheet = workbook.getSheetAt(3);//第四个工作表
		String dataLack = "";
		String perExist = "";
		String orgNotExist = "";
		String numberFormat = "";
		String dateError = "";
		
		for (int j = 2; j < sheet.getPhysicalNumberOfRows() + 1; j++) {//从第二行开始读
			try {
				HSSFRow rowline = sheet.getRow(j - 1);
				if(rowline == null) continue;
				String authorizeId="";
				String authorizeCode="";
				String lockId="";
				String unlockPerId="";
				String startTime="";
				String endTime="";
				String unlockNumber="";
				String lockInModuleCode="";
				String lockDeviceNo="";
				String blueUnlock="";
				String statusCode="";
				String cuUserId="";
				String cuTime="";
				String authorizeType="";
				String scopeUnlock ="";
				
				for (int i = 0; i < rowline.getLastCellNum(); i++) {// 循环遍历所有列
					HSSFCell cell = rowline.getCell((short) i); // 取得当前Cell
					String cellvalueStr = null;
					if (cell != null) {
						switch (cell.getCellType()) {// 判断当前Cell的Type
							case HSSFCell.CELL_TYPE_NUMERIC : {// 如果当前Cell的Type为NUMERIC
								Double value = cell.getNumericCellValue();// 取得当前Cell的数值
								BigDecimal bigDecimal = new BigDecimal(value);//格式转换，避免以科学计数法表示
								cellvalueStr = bigDecimal.toString();
								break;
							}
							case HSSFCell.CELL_TYPE_STRING : // 如果当前Cell的Type为STRING
								cellvalueStr = cell.getStringCellValue(); // 取得当前的Cell字符串
								break;
						}
					}
					if(StringUtil.isNotEmpty(cellvalueStr)){
						switch (i) {
						case 0 :  authorizeId = cellvalueStr.trim(); break;
						case 1 :  authorizeCode = cellvalueStr.trim(); break;
						case 2 :  lockId = cellvalueStr.trim(); break;
						case 3 :  unlockPerId = cellvalueStr.trim(); break;
						case 4 :  startTime = cellvalueStr.trim(); break;
						case 5 :  endTime = cellvalueStr.trim(); break;
						case 6 :  unlockNumber = cellvalueStr.trim(); break;
						case 7 :  lockInModuleCode = cellvalueStr.trim(); break;
						case 8 :  lockDeviceNo = cellvalueStr.trim(); break;
						case 9 :  blueUnlock = cellvalueStr.trim(); break;
						case 10 :  statusCode = cellvalueStr.trim(); break;
						case 11 :  cuUserId = cellvalueStr.trim(); break;
						case 12 :  cuTime = cellvalueStr.trim(); break;
						case 13 :  authorizeType = cellvalueStr.trim(); break;
						case 14 :  scopeUnlock  = cellvalueStr.trim(); break;
						}
					}
				}
				String sql = "insert into LOCK_KEY_AUTHORIZE(AUTHORIZE_ID,AUTHORIZE_CODE,LOCK_ID,UNLOCK_PER_ID,START_TIME,END_TIME,UNLOCK_NUMBER,LOCK_IN_MODULE_CODE,LOCK_DEVICE_NO,BLUE_UNLOCK,STATUS_CODE,CU_USER_ID,CU_TIME,AUTHORIZE_TYPE,SCOPE_UNLOCK)" +
								 " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				Object[] args = {authorizeId,authorizeCode,lockId,unlockPerId,startTime,endTime, StringUtil.isBlank(unlockNumber)?0:unlockNumber,lockInModuleCode,lockDeviceNo,blueUnlock,statusCode,cuUserId,cuTime,authorizeType,scopeUnlock};
				dao.ExecuteBySql(sql, args);
			} catch(NumberFormatException e) {
				e.printStackTrace();
				numberFormat += j + "、";
			} catch(Exception e) {
				e.printStackTrace();
				dateError += j + "、";
			}
		}
		result=logicProcess(dataLack, perExist, numberFormat, orgNotExist, dateError, result);
		return result;
	}
	
	
	private String importKeyGroup(HSSFWorkbook workbook){
		String result = "";
		HSSFSheet sheet = workbook.getSheetAt(2);//第三个工作表
		String dataLack = "";
		String perExist = "";
		String orgNotExist = "";
		String numberFormat = "";
		String dateError = "";
		
		for (int j = 2; j < sheet.getPhysicalNumberOfRows() + 1; j++) {//从第二行开始读
			try {
				HSSFRow rowline = sheet.getRow(j - 1);
				if(rowline == null) continue;
				String groupId="";
				String groupName="";
				String groupSecretKey="";
				String orgId="";
				String note="";
				
				for (int i = 0; i < rowline.getLastCellNum(); i++) {// 循环遍历所有列
					HSSFCell cell = rowline.getCell((short) i); // 取得当前Cell
					String cellvalueStr = null;
					if (cell != null) {
						switch (cell.getCellType()) {// 判断当前Cell的Type
							case HSSFCell.CELL_TYPE_NUMERIC : {// 如果当前Cell的Type为NUMERIC
								Double value = cell.getNumericCellValue();// 取得当前Cell的数值
								BigDecimal bigDecimal = new BigDecimal(value);//格式转换，避免以科学计数法表示
								cellvalueStr = bigDecimal.toString();
								break;
							}
							case HSSFCell.CELL_TYPE_STRING : // 如果当前Cell的Type为STRING
								cellvalueStr = cell.getStringCellValue(); // 取得当前的Cell字符串
								break;
						}
					}
					if(StringUtil.isNotEmpty(cellvalueStr)){
						switch (i) {
						case 0 :  groupId = cellvalueStr.trim(); break;
						case 1 :  groupName = cellvalueStr.trim(); break;
						case 2 :  groupSecretKey = cellvalueStr.trim(); break;
						case 3 :  orgId = cellvalueStr.trim(); break;
						case 4 :  note = cellvalueStr.trim(); break;
						case 5 :  groupId = cellvalueStr.trim(); break;
						}
					}
				}
				if(StringUtil.isBlank(orgId)){
					String sql = "insert into dev_key_group(GROUP_ID,GROUP_NAME,GROUP_SECRET_KEY,NOTE)" +
					" values (?,?,?,?)";
					Object[] args = {groupId,groupName,groupSecretKey,note};
					dao.ExecuteBySql(sql, args);
				}else{
					String sql = "insert into dev_key_group(GROUP_ID,GROUP_NAME,GROUP_SECRET_KEY,ORG_ID,NOTE)" +
					" values (?,?,?,?,?)";
					Object[] args = {groupId,groupName,groupSecretKey,orgId,note};
					dao.ExecuteBySql(sql, args);
				}
				
			} catch(NumberFormatException e) {
				numberFormat += j + "、";
			} catch(Exception e) {
				e.printStackTrace();
				dateError += j + "、";
			}
		}
		result=logicProcess(dataLack, perExist, numberFormat, orgNotExist, dateError, result);
		return result;
	}
	
	private String importKeyinfo(HSSFWorkbook workbook){
		String result = "";
		HSSFSheet sheet = workbook.getSheetAt(1);//第二个工作表
		String dataLack = "";
		String perExist = "";
		String orgNotExist = "";
		String numberFormat = "";
		String dateError = "";
		
		for (int j = 2; j < sheet.getPhysicalNumberOfRows() + 1; j++) {//从第二行开始读
			try {
				HSSFRow rowline = sheet.getRow(j - 1);
				if(rowline == null) continue;
				String keyId="";
				String keyCode="";
				String keyName="";
				String keyType="";
				String perId="";
				String orgId="";
				String status="";
				String note="";
				String groupId="";
				String lockingTime="";
				String phoneImei="";
				String blueName="";
				
				for (int i = 0; i < rowline.getLastCellNum(); i++) {// 循环遍历所有列
					HSSFCell cell = rowline.getCell((short) i); // 取得当前Cell
					String cellvalueStr = null;
					if (cell != null) {
						switch (cell.getCellType()) {// 判断当前Cell的Type
							case HSSFCell.CELL_TYPE_NUMERIC : {// 如果当前Cell的Type为NUMERIC
								Double value = cell.getNumericCellValue();// 取得当前Cell的数值
								BigDecimal bigDecimal = new BigDecimal(value);//格式转换，避免以科学计数法表示
								cellvalueStr = bigDecimal.toString();
								break;
							}
							case HSSFCell.CELL_TYPE_STRING : // 如果当前Cell的Type为STRING
								cellvalueStr = cell.getStringCellValue(); // 取得当前的Cell字符串
								break;
						}
					}
					if(StringUtil.isNotEmpty(cellvalueStr)){
						switch (i) {
						case 0 :  keyId = cellvalueStr.trim(); break;
						case 1 :  keyCode = cellvalueStr.trim(); break;
						case 2 :  keyName = cellvalueStr.trim(); break;
						case 3 :  keyType = cellvalueStr.trim(); break;
						case 4 :  perId = cellvalueStr.trim(); break;
						case 5 :  orgId = cellvalueStr.trim(); break;
						case 6 :  status = cellvalueStr.trim(); break;
						case 7 :  note = cellvalueStr.trim(); break;
						case 8 :  groupId = cellvalueStr.trim(); break;
						case 9 :  lockingTime = cellvalueStr.trim(); break;
						case 10 :  phoneImei = cellvalueStr.trim(); break;
						case 11 :  blueName = cellvalueStr.trim(); break;
						}
					}
				}
				
				//判断名称、帐号、组织名称、电话是否为空，为空则不保存
				if(StringUtil.isEmpty(keyId) || StringUtil.isEmpty(keyCode) || 
						StringUtil.isEmpty(keyName)||StringUtil.isEmpty(keyType)||StringUtil.isEmpty(perId)||
						StringUtil.isEmpty(groupId)||StringUtil.isEmpty(status)){
					dataLack += j + "、";
					continue;
				}
				String sql = "insert into dev_key_info(KEY_ID,KEY_CODE,KEY_NAME,KEY_TYPE,PER_ID,ORG_ID,GROUP_ID,STATUS,NOTE,BLUE_NAME,LOCKING_TIME,PHONE_IMEI)" +
								 " values (?,?,?,?,?,?,?,?,?,?,?,?)";
				Object[] args = {keyId,keyCode,keyName,keyType,perId,orgId,groupId,status,note,blueName,lockingTime,phoneImei};
				dao.ExecuteBySql(sql, args);
			} catch(NumberFormatException e) {
				numberFormat += j + "、";
			} catch(Exception e) {
				e.printStackTrace();
				dateError += j + "、";
			}
		}
		result=logicProcess(dataLack, perExist, numberFormat, orgNotExist, dateError, result);
		return result;
	}
	
	private String importPersoninfo(HSSFWorkbook workbook){
		String result = "";
		HSSFSheet sheet = workbook.getSheetAt(0);//第一个工作表
		String dataLack = "";
		String perExist = "";
		String orgNotExist = "";
		String numberFormat = "";
		String dateError = "";
		List<OrgInfo> orgList = orgInDao.findOrgInfoList(null);
		Map<String, String> importPerMap = new HashMap<String, String>();

		for (int j = 2; j < sheet.getPhysicalNumberOfRows() + 1; j++) {//从第二行开始读
			try {
				HSSFRow rowline = sheet.getRow(j - 1);
				if(rowline == null) continue;
				
				String perName= "";
				String perAccounts= "";
				String orgName= "";
				String phoneNo= "";
				String address= "";
				String note= "";
				String perId= "";
				String perPassword= "";
				String status= "";
				String smartKeyPerId= "";
				String smartKeyPassw= "";
				String cuTime = "";
				
				for (int i = 0; i < rowline.getLastCellNum(); i++) {// 循环遍历所有列
					HSSFCell cell = rowline.getCell((short) i); // 取得当前Cell
					String cellvalueStr = null;
					if (cell != null) {
						switch (cell.getCellType()) {// 判断当前Cell的Type
							case HSSFCell.CELL_TYPE_NUMERIC : {// 如果当前Cell的Type为NUMERIC
								Double value = cell.getNumericCellValue();// 取得当前Cell的数值
								BigDecimal bigDecimal = new BigDecimal(value);//格式转换，避免以科学计数法表示
								cellvalueStr = bigDecimal.toString();
								break;
							}
							case HSSFCell.CELL_TYPE_STRING : // 如果当前Cell的Type为STRING
								cellvalueStr = cell.getStringCellValue(); // 取得当前的Cell字符串
								break;
						}
					}
					if(StringUtil.isNotEmpty(cellvalueStr)){
						switch (i) {
							case 0 : perName = cellvalueStr.trim(); break;
							case 1 : perAccounts = cellvalueStr.trim(); break;
							case 2 : orgName = cellvalueStr.trim(); break;
							case 3 : phoneNo = cellvalueStr.trim(); break;
							case 4 : address = cellvalueStr.trim(); break;
							case 5 : note = cellvalueStr.trim(); break;
							case 6 : perId= cellvalueStr.trim(); break;
							case 7 : perPassword= cellvalueStr.trim(); break;
							case 8 : status= cellvalueStr.trim(); break;
							case 9 : smartKeyPerId= cellvalueStr.trim(); break;
							case 10 : smartKeyPassw= cellvalueStr.trim(); break;
							case 11 : cuTime = cellvalueStr.trim(); break;
						}
					}
				}
				
				PersonnelInfo personnelInfo = new PersonnelInfo();
				//判断名称、帐号、组织名称、电话是否为空，为空则不保存
				if(StringUtil.isEmpty(perName) || StringUtil.isEmpty(perAccounts) || 
						StringUtil.isEmpty(orgName)|| null == phoneNo||StringUtil.isEmpty(perId)||
						StringUtil.isEmpty(perPassword)||StringUtil.isEmpty(smartKeyPerId)||StringUtil.isEmpty(smartKeyPassw)||
						StringUtil.isEmpty(cuTime)||StringUtil.isEmpty(status)){
					dataLack += j + "、";
					continue;
				}
				
				if(StringUtil.isDigit(orgName)){
					for(int h=0; h < orgList.size(); h++){
						OrgInfo org = orgList.get(h);
						if(orgName.equalsIgnoreCase(org.getOrgId().toString())){
							personnelInfo.setOrgId(org.getOrgId());
							break;
						}
					}
				}else{
					for(int h=0; h < orgList.size(); h++){
						OrgInfo org = orgList.get(h);
						if(orgName.equalsIgnoreCase(org.getOrgName())){
							personnelInfo.setOrgId(org.getOrgId());
							break;
						}
					}
				}
				
				if(personnelInfo.getOrgId() == null){
					orgNotExist += j + "、";
					continue;
				}
				
				personnelInfo.setPerName(perName);
				personnelInfo.setPerAccounts(perAccounts);
				
				if(importPerMap.containsValue(perAccounts) || importPerMap.get(perName) != null){
					perExist += j + "、";
					continue;
				}
				String sql = "insert into PERSONNEL_INFO(PER_NAME,PER_ACCOUNTS,ORG_ID,PHONE_NO,ADDRESS,NOTE,PER_ID,PER_PASSWORD,STATUS,SMART_KEY_PER_ID,SMART_KEY_PASSW,CU_TIME)" +
								 " values (?,?,?,?,?,?,?,?,?,?,?,?)";
				Object[] args = {perName,perAccounts,personnelInfo.getOrgId(),phoneNo,address,note,perId,perPassword,status,smartKeyPerId,smartKeyPassw,cuTime};
				dao.ExecuteBySql(sql, args);
				importPerMap.put(perName, perAccounts);
			} catch(NumberFormatException e) {
				numberFormat += j + "、";
			} catch(Exception e) {
				e.printStackTrace();
				dateError += j + "、";
			}
		}
		
		result=logicProcess(dataLack, perExist, numberFormat, orgNotExist, dateError, result);
		
		return result;
	}
	
	/**
	 * 处量并返回导入数据中存在的错识吴信息
	 * @param dataLack
	 * @param numberFormat
	 * @param orgNotExist
	 * @param dateError
	 * @param result
	 * @param perExist 
	 * @return
	 */
	private String logicProcess(String dataLack, String perExist,String numberFormat,String orgNotExist,String dateError,String result){
		if(!dataLack.equalsIgnoreCase("")){
			result = "第"+dataLack+"行数据不全！<br>";
		}
		if(!"".equalsIgnoreCase(perExist)){
			result += "第"+perExist+"行名字或帐号已存在！<br>";
		}
		if(!"".equalsIgnoreCase(numberFormat)){
			result += "第"+numberFormat+"行数字类型错误！<br>";
		}
		if(!"".equalsIgnoreCase(orgNotExist)){
			result += "第"+orgNotExist+ "行组织名称不存在！<br>";
		}
		if(!"".equalsIgnoreCase(dateError)){
			result += "第"+dateError+"行导入出现异常，请检查该行数据！<br>";
		}
		if(StringUtil.isNotEmpty(result)){
			result = "导入的文件中导入失败的有：<br>"+result;
		}
		return result;
	}
	
	//------------------数据库批量导入------------------END
	
	
	
	
	
	public void deletePersonnelInfoByIds(String perIds){
		String[] preIdArrey = perIds.split(",");
		for(String preId : preIdArrey){
			try{
				PersonnelInfo personnelInfo = dao.getById(Long.parseLong(preId));
				personnelInfo.setStatus(3L);//删除标记
				dao.save(personnelInfo);
			} catch (Exception e) {
			}
		}
	}
	
	public void deleteRealPersonnelInfoByIds(String perIds){
		String[] preIdArrey = perIds.split(",");
		for(String preId : preIdArrey){
			try{
				PersonnelInfo personnelInfo = dao.getById(Long.parseLong(preId));
				personnelInfo.setStatus(3L);//删除标记
				dao.delete(personnelInfo);
			} catch (Exception e) {
			}
		}
	}
	
	public List findPersonnelInfoOptions(PersonnelInfoDto dto){
		return this.dao.findPersonnelInfoOptions(dto);
	}
	
	public PersonnelInfo getPersonnelByAccounts(String perAccounts){
		return this.dao.getPersonnelByAccounts(perAccounts);
	}
	
	public List findAccountBySysUserAndPer(String account){
		return this.dao.findAccountBySysUserAndPer(account);
	}
	
	public void updateResetPerPassword(Long perId, String password){
		this.dao.updateResetPerPassword(perId, password);
	}
	
	
	/* (non-Javadoc)
	 * @see com.hnctdz.aiLock.service.info.PersonnelInfoService#exportLockInfo(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, com.hnctdz.aiLock.dto.info.PersonnelInfoDto)
	 */
	public void exportPersonInfo(HttpServletRequest request,
			HttpServletResponse response, PersonnelInfoDto dto) throws IOException {
		InputStream in = null;
		OutputStream out = null;
		String templatePath = "/resources/moulddownload/personnelInfo_exp_model1.xls";
		String excelName = DateUtil.getDateTime2()+"_"+StringUtil.getFileName(templatePath);
		response.setContentType("application/vnd.ms-excel;charset=GBK");
		response.setHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(excelName, "GB2312"));
		in = request.getSession().getServletContext().getResourceAsStream(templatePath);
		out = response.getOutputStream();
		
		
		List<PersonnelInfo> personList = null ; //人员信息
		List<DevKeyInfo> devKeyList = null ;	//钥匙信息
		List<DevKeyGroup> keygroupList = null;  //钥匙分组信息表
		List<LockKeyAuthorize> lockkeyAuthList = null; //门锁授权表
		List<DevLockInfo> lockInfoList = null;   	//门锁信息表
		List<DevLockInGroup> devLockingroupList = null;    //门锁分组
		List<DevGroup> devgroupList = null;   	   //门锁分组信息表
		
		HashMap<DBTABLE, List> propertyMap = new HashMap<DBTABLE, List>(); //定义参数信息
		
		//-------------------------获取信息-----------------------------
		StringBuffer psId = null;
		//一,获取人员列表     跟据组织节点ID和personid
		List eList = this.dao.getPersonnelInfos(dto); 
		if(eList!=null){
			personList = new ArrayList<PersonnelInfo>() ;
			psId = new StringBuffer();
			for (Iterator iterator = eList.iterator(); iterator.hasNext();) {
				PersonnelInfo personnelInfo = (PersonnelInfo) iterator.next();
				personList.add(personnelInfo);
				psId.append(personnelInfo.getPerId()+",");
			}
			if(StringUtil.length(psId.toString())>0){
				psId.deleteCharAt(psId.length() - 1);
			}
			
			//导出人员信息到excel表格1中
			propertyMap.put(DBTABLE.PERSONTABLE, personList);
		}
		
		
		
		//二，获取钥匙列表--跟据人员ID获取所有钥匙信息
		StringBuffer groupId = new StringBuffer();
		if(StringUtil.isNotBlank(psId.toString())){
			String hql = "from DevKeyInfo where perId in("+psId.toString()+") ";
			try {
				devKeyList = this.devKeyInfoDao.findAllByHQL(hql);
				for (Iterator iterator = devKeyList.iterator(); iterator.hasNext();) {
					DevKeyInfo devkey = (DevKeyInfo) iterator.next();
					if(null!=devkey.getGroupId()){
						if(!groupId.toString().contains(devkey.getGroupId().toString())){
							groupId.append(devkey.getGroupId()+",");   //获取钥匙的groupId
						}
					}
				}
				if(StringUtil.length(groupId.toString())>0){
					groupId.deleteCharAt(groupId.length() - 1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			propertyMap.put(DBTABLE.KEYTABLE, devKeyList);
			//导出人员信息到excel表格2中
		}
		
		//三，获取钥匙分组信息  --获取钥匙分组信息
		if(groupId!=null&&StringUtil.isNotBlank(groupId.toString())){
			String keygroupHql = "from DevKeyGroup where groupId in("+groupId.toString()+") ";
			try {
				keygroupList = this.devKeyGroupdao.findAllByHQL(keygroupHql);
			} catch (Exception e) {
				e.printStackTrace();
			}	
			propertyMap.put(DBTABLE.KEYGROUPTAPE, keygroupList);
			//导出人员信息到excel表格3中
		}
		
		//四，获取门锁授权表 --获取钥匙门锁授权表   ----0K
		StringBuffer authlockIds = new StringBuffer();  //跟据授权信息获取对应的锁信息
		if(StringUtil.isNotBlank(psId.toString())){
			String hql = "from LockKeyAuthorize where unlockPerId in("+psId.toString()+") ";
			try {
				lockkeyAuthList = this.lockAuthdao.findAllByHQL(hql);
				for (Iterator iterator = lockkeyAuthList.iterator(); iterator.hasNext();) {
					LockKeyAuthorize lockAuth = (LockKeyAuthorize) iterator.next();
					authlockIds.append(lockAuth.getLockId()+",");   //获取钥匙的groupId
				}
				if(StringUtil.length(authlockIds.toString())>0){
					authlockIds.deleteCharAt(authlockIds.length() - 1);
				}
				//导出人员信息到excel表格4中
				propertyMap.put(DBTABLE.LOCKKEYAUTHTABLE, lockkeyAuthList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		 
		//五，获取门锁信息   --跟据用户授权表中拥有的锁ID获取对应的锁信息数据
		StringBuffer lockIds2 = new StringBuffer();
		String devLockinfoHql = "";
		if(null!=authlockIds&&StringUtil.isNotBlank(authlockIds.toString())){//如果没有选择组织，默认导出全部组织
			devLockinfoHql = "from DevLockInfo where 1=1 and lockId in("+authlockIds.toString()+") ";
			try {
				lockInfoList = this.devLockInfodao.findAllByHQL(devLockinfoHql);
				for (Iterator iterator = lockInfoList.iterator(); iterator.hasNext();) {
					DevLockInfo devkey = (DevLockInfo) iterator.next();
					lockIds2.append(devkey.getLockId()+",");   //获取钥匙的groupId
				}
				if(StringUtil.length(lockIds2.toString())>0){
					lockIds2.deleteCharAt(lockIds2.length() - 1);
				}
				//导出人员信息到excel表格5中
				propertyMap.put(DBTABLE.LOCKTABLE, lockInfoList);
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		
		//六，获取门锁分组表(跟据选择的区域导出所关联的锁信息
		if(lockIds2!=null&&StringUtil.isNotBlank(lockIds2.toString())){
			String lockgroupHql = "from DevLockInGroup where id.lockId in("+lockIds2.toString()+") ";
			try {
				devLockingroupList = this.devLockGroupdao.findAllByHQL(lockgroupHql);
				
				List<DevLockInGroupId> newData = null;
				if(devLockingroupList!=null){newData = new ArrayList<DevLockInGroupId>();
					for (Iterator iterator = devLockingroupList.iterator(); iterator.hasNext();) {
						DevLockInGroup dlg = (DevLockInGroup) iterator.next();
						DevLockInGroupId dlgi = new DevLockInGroupId();
						dlgi.setLockId(dlg.getId().getLockId());
						dlgi.setGroupId(dlg.getId().getGroupId());
						newData.add(dlgi);
					}
				}
				//导出人员信息到excel表格6中
				propertyMap.put(DBTABLE.DEVLOCKGROUPTABLE, newData);
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		//执行excel导出功能
		ExcelUtil.getInstance().ExcelTemplateExport(in, out, propertyMap);
		try {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	public enum DBTABLE {  
		PERSONTABLE, KEYTABLE, KEYGROUPTAPE, LOCKKEYAUTHTABLE,LOCKTABLE  ,DEVLOCKGROUPTABLE
	} 
	

	
}
