package com.hnctdz.aiLock.service.device.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
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

import com.hnctdz.aiLock.dao.analysis.LockStatusRecordsDao;
import com.hnctdz.aiLock.dao.analysis.UnlockRecordsDao;
import com.hnctdz.aiLock.dao.device.DevLockInfoDao;
import com.hnctdz.aiLock.dao.device.LockKeyAuthorizeDao;
import com.hnctdz.aiLock.dao.info.OrgInfoDao;
import com.hnctdz.aiLock.dao.system.SysAreaDao;
import com.hnctdz.aiLock.dao.system.SysBasicDataDao;
import com.hnctdz.aiLock.domain.analysis.LockStatusRecords;
import com.hnctdz.aiLock.domain.device.DevLockInfo;
import com.hnctdz.aiLock.domain.info.OrgInfo;
import com.hnctdz.aiLock.domain.system.SysArea;
import com.hnctdz.aiLock.domain.system.SysBasicData;
import com.hnctdz.aiLock.dto.CommandInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.DevLockInfoDto;
import com.hnctdz.aiLock.dto.device.LockKeyAuthorizeDto;
import com.hnctdz.aiLock.dto.system.SysBasicDataDto;
import com.hnctdz.aiLock.https.HttpDeviceManageForRest;
import com.hnctdz.aiLock.service.device.DevLockInfoService;
import com.hnctdz.aiLock.utils.CommonUtil;
import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.hnctdz.aiLock.utils.ExcelUtil;
import com.hnctdz.aiLock.utils.GsonUtil;
import com.hnctdz.aiLock.utils.Mess;
import com.hnctdz.aiLock.utils.ResponseCommandUtil;
import com.hnctdz.aiLock.utils.ReturnCodeUtil;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName DevLockInfoServiceImpl.java
 * @Author WangXiangBo 
 */
@Service("com.hnctdz.aiLock.service.device.DevLockInfoService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class DevLockInfoServiceImpl implements DevLockInfoService{
	@Autowired
	private DevLockInfoDao dao;
 	@Autowired
 	private OrgInfoDao orgInfoDao;
 	@Autowired
 	private SysAreaDao sysAreaDao;
 	@Autowired
	private LockStatusRecordsDao lockStatusRecordsDao;
 	@Autowired
	private LockKeyAuthorizeDao lockKeyAuthorizeDao;
 	@Autowired
	private UnlockRecordsDao unlockRecordsDao;
	
	public DataPackage findPageDevLockInfo(DevLockInfoDto dto, DataPackage dp){
		dp = this.dao.findPageDevLockInfo(dto, dp);
		
		List<DevLockInfo> lockList = (List<DevLockInfo>)dp.getRows();
		for(DevLockInfo lock : lockList){
			OrgInfo org = orgInfoDao.getById(lock.getOrgId());
			if(org != null){
				lock.setOrgName(org.getOrgName());
//				SysUser sysUser = sysUserDao.getById(org.getUserId());
//				lock.setManagerName(sysUser.getName());
			}
			
			SysArea sysArea = this.sysAreaDao.getById(lock.getAreaId());
		    if (sysArea != null) {
		        lock.setAreaName(sysArea.getAreaName());
		    }
			
			if(lock.getLockType() == 2){
				if(("1".equalsIgnoreCase(lock.getStatus()) || "2".equalsIgnoreCase(lock.getStatus()))){
					LockStatusRecords lsr = lockStatusRecordsDao.findLockStatusLastRecords(lock.getLockCode());
					if(lsr != null && (new Date().getTime() - lsr.getReportTime().getTime()) <= 
									  Integer.parseInt(Constants.LOCK_STATUS_TIMEOUT) * 60 * 1000){
							lock.setOnlineStauts("1");
					}else{
						lock.setOnlineStauts("0");
					}
				}else{
					lock.setOnlineStauts("0");
				}
			}else{
				lock.setOnlineStauts("2");
			}
		}
		dp.setRows(lockList);
		return dp;
	}
	
	public DevLockInfo getById(Integer lockId){
		return dao.getById(lockId);
	}
	
	public List<DevLockInfo> findDevLockInfoList(DevLockInfoDto dto){
		return this.dao.findDevLockInfoList(dto);
	}
	
	public List<DevLockInfo> findNormalLockInfoList(DevLockInfoDto dto){
		return this.dao.findNormalLockInfoList(dto);
	}
	
	public List findNormalLockListByCombobox(){
		return this.dao.findNormalLockListByCombobox();
	}
	
	public List findAreaStatisticalLock(DevLockInfoDto dto){
		return this.dao.findAreaStatisticalLock(dto);
	}

	public List<DevLockInfoDto> findPersonnelMaitLockList(DevLockInfoDto dto){
		return dao.findPersonnelMaitLockList(dto);
	}
	
	public DevLockInfo getDevLockInfoByLockCode(String lockCode){
		return this.dao.getDevLockInfoByLockCode(lockCode);
	}
	
	public boolean checkLocExist(DevLockInfo lockInfo){
		DevLockInfoDto dto = new DevLockInfoDto();
		dto.setLockCode(lockInfo.getLockCode());
		List<DevLockInfo> list = dao.findDevLockInfoList(dto);
		if(list.size() > 0){
			if(lockInfo.getLockId() == null || !list.get(0).getLockId().equals(lockInfo.getLockId())){
				return false;
			}
		}
		return true;
	}
	
	public boolean checkBluetoothExist(DevLockInfo lockInfo) {
		DevLockInfoDto dto = new DevLockInfoDto();
		boolean isExistBt = false;
		dto.setLockInBlueCode(lockInfo.getLockInBlueCode());
		if(StringUtil.isBlank(lockInfo.getLockInBlueCode())){
			isExistBt =  true;
		}else{
			List<DevLockInfo> list = dao.findDevLockInfoList(dto);
			if(list.size() > 0){
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					DevLockInfo devLockInfo = (DevLockInfo) iterator.next();
					if(lockInfo.getLockCode().equals(devLockInfo.getLockCode())){
						if(devLockInfo.getLockInBlueCode().equals(lockInfo.getLockInBlueCode())){
							isExistBt = true;
						}else{
							isExistBt = false;
						}
					}
				}
			}else{
				isExistBt =  true;
			}
		}
		return isExistBt;
	}
	

	public void saveDevLockInfo(DevLockInfo devLockInfo) throws ErrorCodeException{
		DevLockInfo viceDevLockInfo = null;
		
		if(StringUtil.isNotBlank(devLockInfo.getVicePassiveLockCode())){
			//查询备用无源锁编码在数据库中是否存在
			viceDevLockInfo = dao.getDevLockInfoByLockCode(devLockInfo.getVicePassiveLockCode());
			if(viceDevLockInfo != null){
				//有,则备用无源锁属性修改成该主锁属性
				if(viceDevLockInfo.getLockParentId() != null && 
						!viceDevLockInfo.getLockParentId().equals(devLockInfo.getLockId())){
					throw new ErrorCodeException("该副锁已有主锁，请确认副锁编码输入是否正确，或先从原主锁中清除该副锁！");
				}
			}else{//没有,则创建备用无源锁
				viceDevLockInfo = new DevLockInfo();
				viceDevLockInfo.setLockType(Constants.PASSIVE_LOCK);
				viceDevLockInfo.setStatus("1");
			}
			
			viceDevLockInfo.setLockCode(devLockInfo.getVicePassiveLockCode());
			viceDevLockInfo.setLockName(devLockInfo.getLockName());
			viceDevLockInfo.setLockAddres(devLockInfo.getLockAddres());
			viceDevLockInfo.setLatitude(devLockInfo.getLatitude());
			viceDevLockInfo.setLongitude(devLockInfo.getLongitude());
			viceDevLockInfo.setAreaId(devLockInfo.getAreaId());
			viceDevLockInfo.setOrgId(devLockInfo.getOrgId());
			viceDevLockInfo.setWheCanMatchCard(0);
			viceDevLockInfo.setNote("副锁，主锁编码："+devLockInfo.getLockCode());
		}
		
		if(null != devLockInfo.getLockId()){
			DevLockInfo dbdli = this.dao.getById(devLockInfo.getLockId());
			//如果修改了门锁编码
			if(!dbdli.getLockCode().equalsIgnoreCase(devLockInfo.getLockCode())){
				//为有源门锁，则需要修改历史门锁记录的门锁编码字段
				if(Constants.ACTIVE_LOCK.equals(dbdli.getLockType())){
					devLockInfo.setLockParentId(null);//清空主锁ID，防止无源锁修改成有源锁主锁ID还存在
					unlockRecordsDao.updateRecordsTheLockCode(devLockInfo.getLockCode(), dbdli.getLockCode());
				}else if(dbdli.getLockParentId() != null){//为备用无源锁，则需要修改主锁中备用锁编码
					DevLockInfo parentLock = this.dao.getById(dbdli.getLockParentId());
					parentLock.setVicePassiveLockCode(devLockInfo.getLockCode());
					this.dao.save(parentLock);
				}
			}
			//如果修改的副无源锁编码与旧的不一样，则需要处理
			if(StringUtil.isNotBlank(dbdli.getVicePassiveLockCode()) && 
					!dbdli.getVicePassiveLockCode().equalsIgnoreCase(devLockInfo.getVicePassiveLockCode())){
				//去除旧备用无源锁信息中主锁ID
				DevLockInfo oldViceLock = dao.getDevLockInfoByLockCode(dbdli.getVicePassiveLockCode());
				if(oldViceLock != null){
					oldViceLock.setLockParentId(null);
					oldViceLock.setNote("原:"+ dbdli.getLockCode() +"备用锁");
					this.dao.save(oldViceLock);
				}
			}
			if(!Constants.PASSIVE_LOCK.equals(devLockInfo.getLockType())){
				devLockInfo.setLockParentId(null);//清空主锁ID，防止无源锁修改成有源锁主锁ID还存在
				devLockInfo.setNote(null);
			}
			this.dao.merge(devLockInfo);
//			this.dao.save(dbdli);
		}else{
			this.dao.save(devLockInfo);
		}
		
		if(viceDevLockInfo != null){//保存副锁信息
			viceDevLockInfo.setLockParentId(devLockInfo.getLockId());
			this.dao.save(viceDevLockInfo);
		}
		
		if("5".equalsIgnoreCase(devLockInfo.getStatus()) && devLockInfo.getLockId() != null){
			deleteDevLockInfoByIds(devLockInfo.getLockId().toString());
		}
	}
	
	public String saveImportLockInfos(File importFile){
		String result = "";
		try {
			InputStream is = new FileInputStream(importFile);
			HSSFWorkbook workbook = new HSSFWorkbook(is);
			HSSFSheet sheet = workbook.getSheetAt(0);//第一个工作表
			
			String dataLack = "";
			String lockCodeExist = "";
			String passiveLockInfoExist = "";
			String deviceNoExist = "";
			String orgNotExist = "";
			String areaNotExist = "";
			String dateError = "";
			
			List<OrgInfo> orgList = orgInfoDao.findOrgInfoList(null);
			
			SysBasicDataDto sbdDto = new SysBasicDataDto();
			sbdDto.setTypeTag("AREA");
			List<SysArea> areaList = this.sysAreaDao.findSysAreaList(null);
			
			Map<String, String> importLockCodeMap = new HashMap<String, String>();
	
			for (int j = 3; j < sheet.getPhysicalNumberOfRows() + 2; j++) {//从第三行开始读
				try {
					HSSFRow rowline = sheet.getRow(j - 1);
					if(rowline == null) continue;
					
					String lockTypes = null;
					String orgName = null;
					String areaName = null;
					
					DevLockInfo lockInfo = new DevLockInfo();
					lockInfo.setWheCanMatchCard(0);
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
								case 0 : lockTypes = cellvalueStr.trim(); break;
								case 1 : lockInfo.setLockName(cellvalueStr.trim()); break;
								case 2 : lockInfo.setLockCode(cellvalueStr.trim()); break;
								case 3 : lockInfo.setLockInModuleCode(cellvalueStr.trim()); break;
								case 4 : lockInfo.setLockDeviceNo(cellvalueStr.trim()); break;
								case 5 : lockInfo.setVicePassiveLockCode(cellvalueStr.trim()); break;
								case 6 : lockInfo.setLockInBlueCode(cellvalueStr.trim()); break;
								case 7 : orgName = cellvalueStr.trim(); break;
								case 8 : areaName = cellvalueStr.trim(); break;
								case 9 : lockInfo.setLockAddres(cellvalueStr.trim()); break;
								case 10 : lockInfo.setBlueMac(cellvalueStr.trim()); break;
								case 11 : lockInfo.setPrivateKey(cellvalueStr.trim()); break;
							}
						}else{
							cellvalueStr = "";
							switch (i) {
							case 0 : lockTypes = cellvalueStr.trim(); break;
							case 1 : lockInfo.setLockName(cellvalueStr.trim()); break;
							case 2 : lockInfo.setLockCode(cellvalueStr.trim()); break;
							case 3 : lockInfo.setLockInModuleCode(cellvalueStr.trim()); break;
							case 4 : lockInfo.setLockDeviceNo(cellvalueStr.trim()); break;
							case 5 : lockInfo.setVicePassiveLockCode(cellvalueStr.trim()); break;
							case 6 : lockInfo.setLockInBlueCode(cellvalueStr.trim()); break;
							case 7 : orgName = cellvalueStr.trim(); break;
							case 8 : areaName = cellvalueStr.trim(); break;
							case 9 : lockInfo.setLockAddres(cellvalueStr.trim()); break;
							case 10 : lockInfo.setBlueMac(cellvalueStr.trim()); break;
							case 11 : lockInfo.setPrivateKey(cellvalueStr.trim()); break;
							}
						}
					}
					
					//判断类型、名称、组织名称、区域名称是否为空，为空则不保存
					if(StringUtil.isEmpty(lockTypes) || StringUtil.isEmpty(lockInfo.getLockName()) || 
							StringUtil.isEmpty(orgName)|| StringUtil.isEmpty(areaName)){
						dataLack += j + "、";
						break;
					}
					
					if(lockTypes.equalsIgnoreCase("无源锁") && StringUtil.isNotEmpty(lockInfo.getLockCode())){
						lockInfo.setLockInModuleCode(null);
						lockInfo.setLockDeviceNo(null);
						lockInfo.setLockInBlueCode(null);
						lockInfo.setLockType(1);
					}else if(lockTypes.equalsIgnoreCase("有源锁") && StringUtil.isNotEmpty(lockInfo.getLockInModuleCode()) 
							&& StringUtil.isNotEmpty(lockInfo.getLockDeviceNo())){
						lockInfo.setLockCode(lockInfo.getLockInModuleCode() + lockInfo.getLockDeviceNo());
						lockInfo.setLockInModuleCode("FA071302FA02" + lockInfo.getLockInModuleCode() + "FFAFF");
						lockInfo.setLockType(2);
					}else{
						dataLack += j + "、";
						break;
					}
					
					for(int h=0; h < orgList.size(); h++){
						OrgInfo org = orgList.get(h);
						if(orgName.equalsIgnoreCase(org.getOrgName())){
							lockInfo.setOrgId(org.getOrgId());
							break;
						}
					}
					if(lockInfo.getOrgId() == null){
						orgNotExist += j + "、";
						continue;
					}
					
					for(int h=0; h < areaList.size(); h++){
						SysArea area = areaList.get(h);
						if(areaName.equalsIgnoreCase(area.getAreaName())){
							lockInfo.setAreaId(area.getAreaId());
							break;
						}
					}
					if(lockInfo.getAreaId() == null){
						areaNotExist += j + "、";
						continue;
					}
					
					if(importLockCodeMap.get(lockInfo.getLockCode()) != null){
						if(lockInfo.getLockType().equals(1)){
							lockCodeExist += j + "、";
						}else{
							deviceNoExist += j + "、";
						}
						continue;
					}
					
					if(checkLocExist(lockInfo)){
						lockInfo.setStatus("1");
						if(lockInfo.getLockType().equals(Constants.ACTIVE_LOCK) && StringUtil.isNotEmpty(lockInfo.getVicePassiveLockCode())){
							DevLockInfo passiveLockInfo = new DevLockInfo();
							passiveLockInfo.setLockCode(lockInfo.getVicePassiveLockCode());
							if(checkLocExist(passiveLockInfo)){
								dao.save(lockInfo);
								importLockCodeMap.put(lockInfo.getLockCode(), lockInfo.getLockCode());
								
								passiveLockInfo.setLockName(lockInfo.getLockName());
								passiveLockInfo.setAreaId(lockInfo.getAreaId());
								passiveLockInfo.setOrgId(lockInfo.getOrgId());
								passiveLockInfo.setLockParentId(lockInfo.getLockId());
								passiveLockInfo.setLockType(Constants.PASSIVE_LOCK);
								passiveLockInfo.setLockAddres(lockInfo.getLockAddres());
								passiveLockInfo.setNote("副锁，主锁编码："+lockInfo.getLockCode());
								passiveLockInfo.setWheCanMatchCard(0);
								passiveLockInfo.setStatus(lockInfo.getStatus());
								passiveLockInfo.setBlueMac(lockInfo.getBlueMac());
								passiveLockInfo.setPrivateKey(lockInfo.getPrivateKey());
								dao.save(passiveLockInfo);
								importLockCodeMap.put(passiveLockInfo.getLockCode(), passiveLockInfo.getLockCode());
							}else{
								passiveLockInfoExist += j + "、";
								continue;
							}
						}
						lockInfo.setIpAddress("");
						dao.save(lockInfo);
						importLockCodeMap.put(lockInfo.getLockCode(), lockInfo.getLockCode());
					}else{
						if(lockInfo.getLockType().equals(1)){
							lockCodeExist += j + "、";
						}else{
							deviceNoExist += j + "、";
						}
					}
				} catch(Exception e) {
					e.printStackTrace();
					dateError += j + "、";
				}
			}
			
			if(!dataLack.equalsIgnoreCase("")){
				System.out.println("第" + dataLack + "行数据不全！");
			}
			if(!"".equalsIgnoreCase(lockCodeExist)){
				result += "第"+lockCodeExist+"行门锁编码已存在！<br>";
			}
			if(!"".equalsIgnoreCase(deviceNoExist)){
				result += "第"+deviceNoExist+"行门锁机号在该网关中已存在！<br>";
			}
			if(!"".equalsIgnoreCase(passiveLockInfoExist)){
				result += "第"+passiveLockInfoExist+"行备用无源锁编码已存在！<br>";
			}
			if(!"".equalsIgnoreCase(orgNotExist)){
				result += "第"+orgNotExist+ "行组织名称不存在！<br>";
			}
			if(!"".equalsIgnoreCase(areaNotExist)){
				result += "第"+areaNotExist+ "行区域名称不存在！<br>";
			}
			if(!"".equalsIgnoreCase(dateError)){
				result += "第"+dateError+"行导入出现异常，请检查该行数据！<br>";
			}
			if(StringUtil.isNotEmpty(result)){
				result = "导入文件中导入失败的数据有：<br>"+result;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return result;
	}

	
	public String deleteDevLockInfoByIds(String lockIds){
		String result = "", f = "";
		String[] lockIdArrey = lockIds.split(",");
		for(String lockId : lockIdArrey){
			DevLockInfo lockInfo = dao.getById(Integer.parseInt(lockId));
			boolean bl = true;
			try{
				if(Constants.ACTIVE_LOCK.equals(lockInfo.getLockType())){
					if(StringUtil.isNotBlank(lockInfo.getVicePassiveLockCode())){//判断是否有备用无源锁
						DevLockInfo viceLock = dao.getDevLockInfoByLockCode(lockInfo.getVicePassiveLockCode());
						viceLock.setStatus("5");
						dao.save(viceLock);
					}else{
						//bl = this.dao.findLockUsefulAuthList(lockInfo.getLockId());
						LockKeyAuthorizeDto dto = new LockKeyAuthorizeDto();
						dto.setLockId(Integer.parseInt(lockId));
						lockKeyAuthorizeDao.deleteAuthorize(dto);
						deleteLockAllCardPer(lockInfo.getLockDeviceNo(), lockInfo.getLockInModuleCode());
					}
				}else{
					if(null != lockInfo.getLockParentId()){
						DevLockInfo parentLock = dao.getById(lockInfo.getLockParentId());
						parentLock.setVicePassiveLockCode(null);
						dao.save(parentLock);
						
						lockInfo.setLockParentId(null);
						lockInfo.setNote("原:"+parentLock.getLockCode()+"副锁");
					}
				}
			}catch (ErrorCodeException e) {
			} catch (Exception e) {
				bl = false;
			}
			if(bl){
				lockInfo.setStatus("5");//删除标记
				dao.save(lockInfo);
			}else{
				result += f + lockInfo.getLockName();
				f = "、";
			}
		}
		if(StringUtil.isNotEmpty(result)){
			result = "门禁名称为："+result+"，删除失败，请稍后再试！";
		}
		return result;
	}
	
	public String deleteRealDevLockInfoByIds(String lockIds){
		String result = "", f = "";
		String[] lockIdArrey = lockIds.split(",");
		for(String lockId : lockIdArrey){
			DevLockInfo lockInfo = dao.getById(Integer.parseInt(lockId));
			boolean bl = true;
			try{
				if(Constants.ACTIVE_LOCK.equals(lockInfo.getLockType())){
					if(StringUtil.isNotBlank(lockInfo.getVicePassiveLockCode())){//判断是否有备用无源锁
						DevLockInfo viceLock = dao.getDevLockInfoByLockCode(lockInfo.getVicePassiveLockCode());
						viceLock.setStatus("5");
						dao.delete(viceLock);
					}else{
						//bl = this.dao.findLockUsefulAuthList(lockInfo.getLockId());
						LockKeyAuthorizeDto dto = new LockKeyAuthorizeDto();
						dto.setLockId(Integer.parseInt(lockId));
						lockKeyAuthorizeDao.deleteAuthorize(dto);
						deleteLockAllCardPer(lockInfo.getLockDeviceNo(), lockInfo.getLockInModuleCode());
					}
				}else{
					if(null != lockInfo.getLockParentId()){
						DevLockInfo parentLock = dao.getById(lockInfo.getLockParentId());
						parentLock.setVicePassiveLockCode(null);
						dao.delete(parentLock);
						
						lockInfo.setLockParentId(null);
						lockInfo.setNote("原:"+parentLock.getLockCode()+"副锁");
					}
				}
			}catch (ErrorCodeException e) {
			} catch (Exception e) {
				bl = false;
			}
			if(bl){
				lockInfo.setStatus("5");//删除标记
				dao.save(lockInfo);
			}else{
				result += f + lockInfo.getLockName();
				f = "、";
			}
		}
		if(StringUtil.isNotEmpty(result)){
			result = "门禁名称为："+result+"，删除失败，请稍后再试！";
		}
		return result;
	}
	
	public boolean deleteLockAllCardPer(String lockDeviceNo, String lockInModuleCode)throws ErrorCodeException{
		boolean bl = false;
		Mess mess = new Mess();
		mess.setDeviceNo(lockDeviceNo);
		mess.setModuleCode(lockInModuleCode);
		
		String returnJson = HttpDeviceManageForRest.getInstance().httpRequestForLockdm(mess.deleteAllCardPer());
		
		CommandInfo commandInfo = GsonUtil.fromJson(returnJson, CommandInfo.class);
		if(!ReturnCodeUtil.SUCCESS_RESULT_CODE.equalsIgnoreCase(commandInfo.getReturnCode())){
			throw new ErrorCodeException(ErrorCode.getRemoteOperationResults(commandInfo.getReturnCode()));
		}else{
			if(ResponseCommandUtil.SUCCESS_COMMAND.equalsIgnoreCase(commandInfo.getReturnMessage().substring(8, 10))){
				bl = true;
			}
		}
		return bl;
	}
	
	private Map pramsMap;
	
	/* (non-Javadoc)
	 * @see com.hnctdz.aiLock.service.device.DevLockInfoService#getLockCountInfo
	 * (com.hnctdz.aiLock.dto.device.DevLockInfoDto)
	 */
	public String getLockCountInfo(DevLockInfoDto dto) {
		String retMsg = "";
		pramsMap = new HashMap<Object, String>();
		String sql  = "select SUM(CASE WHEN t.LOCK_TYPE = 1 THEN 1 ELSE 0 END) as onlineLockNum, " +
						"SUM(CASE WHEN t.LOCK_TYPE = 2 THEN 1 ELSE 0 END) as unlineLockNum, " +
						"SUM(CASE WHEN t.Lock_in_blue_code<>'' THEN 1 ELSE 0 END) as btLockNum " +
						"from dev_lock_info t " +
						"where FIND_IN_SET (t.AREA_ID,? )";
		Object[] areaIds = new Object[1];
		try {
			if(StringUtil.isNotBlank(dto.areaIds)){
				areaIds[0] = dto.areaIds;
			}else{
				String sqlareas ="select getChildLst_area("+dto.getAreaId()+")";
				List list = this.dao.findAllBySQL(sqlareas);
				areaIds[0] = list.get(0).toString();
			}
//			String sql  = "select getLockCount("+areaIds+")";
//			List list1 = this.dao.findAllBySQL(sql);
//			Object retVal = list.get(0).toString();
//			System.out.println(retVal);
			Object[] retVal =  (Object[]) this.dao.getObjectDetail(sql, areaIds);
			String onLineLockNum = retVal[0]==null?"0":retVal[0].toString();
			String unlineLock = retVal[1]==null?"0":retVal[1].toString();
			String btLock = retVal[2]==null?"0":retVal[2].toString();
			retMsg ="{ \"onlineLock\": \""+onLineLockNum+"\", \"unlineLock\":\""+unlineLock+"\", \"btLock\": \""+btLock+"\" }";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retMsg;
	}
	
	/* (non-Javadoc)
	 * @see com.hnctdz.aiLock.service.device.DevLockInfoService#exportLockInfo
	 * (com.hnctdz.aiLock.dto.device.DevLockInfoDto)
	 */
	public void exportLockInfo(HttpServletRequest request,
			HttpServletResponse response, DevLockInfoDto dto) {
		List<DevLockInfo> eList = new ArrayList<DevLockInfo>();
		eList = this.dao.findDevLockInfoList(dto);
		
		List<OrgInfo> orgList = (List<OrgInfo>) request.getSession().getAttribute("orgList");
		List<SysArea> areaList = (List<SysArea>) request.getSession().getAttribute("areaList");
		if(orgList==null){
			orgList = orgInfoDao.findOrgInfoList(null);
			areaList = this.sysAreaDao.findSysAreaList(null);
			request.getSession().setAttribute("orgList", orgList);
			request.getSession().setAttribute("areaList", areaList);
		}
		List<DevLockInfo> lastList = new ArrayList<DevLockInfo>();
		for (DevLockInfo devLockInfo : eList) {
				for(int h=0; h < orgList.size(); h++){
					OrgInfo org = orgList.get(h);
					if(org.getOrgId()!=null||org.getOrgId().equals(devLockInfo.getOrgId())){
						devLockInfo.setOrgName(org.getOrgName());
						break;
					}
				}
				for(int h=0; h < areaList.size(); h++){
					SysArea area = areaList.get(h);
					if(area.getAreaId()!=null||area.getAreaId().equals(devLockInfo.getAreaId())){
						devLockInfo.setAreaName(area.getAreaName());
						break;
					}
				}
				if(devLockInfo.getLockType()==1){
					devLockInfo.setManagerName("无源");
				}else if(devLockInfo.getLockType()==2){
					devLockInfo.setManagerName("有源");
				}
				lastList.add(devLockInfo);
		}
		//门锁类型","门锁名称","门锁编码","所属模块编号","门锁对应机号","副无源锁编码","所属蓝牙编号","所属组织名称","所属区域名称","所在地址","蓝牙MAC","私钥KEY
		String[] columName ={"managerName","lockName","lockCode","lockInModuleCode","lockDeviceNo","vicePassiveLockCode","lockInBlueCode","orgName","areaName","lockAddres","blueMac","privateKey"};
		ExcelUtil.getInstance().ExcelTemplateExport(request, response, lastList, "/resources/moulddownload/lockInfoMould.xls", 2, columName);
	}
	
}
