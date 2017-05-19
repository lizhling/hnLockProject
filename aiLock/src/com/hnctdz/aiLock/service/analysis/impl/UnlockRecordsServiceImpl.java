package com.hnctdz.aiLock.service.analysis.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.analysis.UnlockRecordsDao;
import com.hnctdz.aiLock.dao.device.DevKeyInfoDao;
import com.hnctdz.aiLock.dao.device.DevLockInfoDao;
import com.hnctdz.aiLock.dao.info.OrgInfoDao;
import com.hnctdz.aiLock.dao.info.PersonnelInfoDao;
import com.hnctdz.aiLock.dao.system.SysAreaDao;
import com.hnctdz.aiLock.dao.system.SysBasicDataDao;
import com.hnctdz.aiLock.dao.system.SysUserDao;
import com.hnctdz.aiLock.domain.analysis.UnlockRecords;
import com.hnctdz.aiLock.domain.device.DevKeyInfo;
import com.hnctdz.aiLock.domain.device.DevLockInfo;
import com.hnctdz.aiLock.domain.info.OrgInfo;
import com.hnctdz.aiLock.domain.info.PersonnelInfo;
import com.hnctdz.aiLock.domain.system.SysArea;
import com.hnctdz.aiLock.domain.system.SysBasicData;
import com.hnctdz.aiLock.domain.system.SysUser;
import com.hnctdz.aiLock.dto.AppLogMessage;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.analysis.UnlockRecordsDto;
import com.hnctdz.aiLock.service.analysis.UnlockRecordsService;
import com.hnctdz.aiLock.utils.BasicDataUtil;
import com.hnctdz.aiLock.utils.CommunCrypUtil;
import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.hnctdz.aiLock.utils.GsonUtil;
import com.hnctdz.aiLock.utils.ResponseCommandUtil;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName UnlockRecordsServiceImpl.java
 * @Author WangXiangBo 
 */
@Service("com.hnctdz.aiLock.service.analysis.UnlockRecordsService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class UnlockRecordsServiceImpl implements UnlockRecordsService{
	private static Log smartKeyRecordLog = LogFactory.getLog("SmartKeyRecord");
	
	@Autowired
	private UnlockRecordsDao dao;
	@Autowired
 	private DevKeyInfoDao devKeyInfoDao;
	@Autowired
 	private DevLockInfoDao devLockInfoDao;
 	@Autowired
 	private PersonnelInfoDao personnelInfoDao;
 	@Autowired
 	private SysUserDao sysUserDao;
 	@Autowired
 	private SysAreaDao sysAreaDao;
 	@Autowired
 	private OrgInfoDao orgInfoDao;
	
	public DataPackage findPageUnlockRecords(UnlockRecordsDto dto, DataPackage dp){
		if(dto != null && dto.getQueryType() == 1){
			List<UnlockRecords> allList = this.dao.findUnlockRecordsList(dto);
			dp.setRows(allList);
		}else{
			dp = this.dao.findPageUnlockRecords(dto, dp);
		}
		
		List<UnlockRecords> list = (List<UnlockRecords>)dp.getRows();
		for(UnlockRecords unlr : list){
			if(dto.getSelectType() == Constants.SELECT_ALARM){
				unlr.setAlarmLevel(BasicDataUtil.getBasicDataTypeName(BasicDataUtil.ALARM_LEVEL, unlr.getRecordCode()));
			}
			
			DevKeyInfo key = devKeyInfoDao.getDevKeyInfoByKeyCode(unlr.getKeyCode());
			if(key != null){
				unlr.setKeyTypeName(BasicDataUtil.getBasicDataTypeName(BasicDataUtil.KEY_TYPE, key.getKeyType()));
			}
			if(unlr.getUnlockPerId() != null){
				PersonnelInfo unLockPer = personnelInfoDao.getById(unlr.getUnlockPerId());
				if(unLockPer != null){
					unlr.setUnlockPerName(unLockPer.getPerName());
					unlr.setUnlockPerPhone(unLockPer.getPhoneNo());
				}
			}else{
				SysUser sysUser = sysUserDao.getById(unlr.getPerId());
				if(sysUser != null){
					unlr.setUnlockPerName(sysUser.getName());
					unlr.setUnlockPerPhone(sysUser.getPhoneNo());
				}
			}
			
			if(unlr.getUserId() != null){
				SysUser sysUser = sysUserDao.getById(unlr.getUserId());
				unlr.setUserName(sysUser != null ? sysUser.getName() : null);
			}
			
			DevLockInfo lock = devLockInfoDao.getDevLockInfoByLockCode(unlr.getLockCode());
			if(lock != null){
				unlr.setLockName(lock.getLockName());
				unlr.setLockCode(lock.getLockCode());
				
				SysArea sysArea = this.sysAreaDao.getById(lock.getAreaId());
			    if (sysArea != null) {
			    	unlr.setAreaName(sysArea.getAreaName());
			    }
			    
				OrgInfo org = orgInfoDao.getById(lock.getOrgId());
				if(org != null){
					unlr.setOrgName(org.getOrgName());
				}
			}
		}
		dp.setRows(list);
		return dp;
	}
	
	public List<UnlockRecords> findUnlockRecordsList(UnlockRecordsDto dto){
		List<UnlockRecords> list = dao.findUnlockRecordsList(dto);
		int i = 1;
		for(UnlockRecords unlr : list){
			DevLockInfo lock = devLockInfoDao.getDevLockInfoByLockCode(unlr.getLockCode());
			if(lock != null){
				unlr.setLockName(lock.getLockName());
				unlr.setLockCode(lock.getLockCode());
			}
			i++;
			if(i > 5){
				break;
			}
		}
		return list;
	}
	
	public void saveUnlockRecords(UnlockRecords unlockRecords){
		this.dao.save(unlockRecords);
	}
	
	public List findOrgStLockRecords(UnlockRecordsDto dto){
		return this.dao.findOrgStLockRecords(dto);
	}
	
	public void updateAlarmConfirm(UnlockRecordsDto dto){
		this.dao.updateAlarmConfirm(dto);
	}
	
	public String saveSmartKeyLog(Long perId, String dataLog) throws ErrorCodeException {
		try{
			AppLogMessage smartKeyLog = GsonUtil.fromJson(dataLog, AppLogMessage.class);
			
			ResponseCommandUtil.getInstance();
			Date uploadTime = new Date();
			for(String sklog : smartKeyLog.getSmartKeyLog()){
				try{
					sklog = sklog.substring(12, sklog.length() - 4);
					String lockId = null;
					String smartKeyPerId = null;
					String keyId = null;
					String typeCode = null;
					for (int i = 0; i < 8; i++) { //默认读取8条记录
						UnlockRecords ur = new UnlockRecords();
						ur.setLockType(Constants.PASSIVE_LOCK);
						ur.setMessage(sklog);
						ur.setPerId(perId);
						ur.setUploadTime(uploadTime);
						
						if(!sklog.substring(i*32, i*32+32).contains("FFFFFFFF")){
							lockId = sklog.substring(i*32+4, i*32+12);
							smartKeyPerId = sklog.substring(i*32+12, i*32+18);
							keyId = sklog.substring(i*32+18, i*32+26);
							typeCode = sklog.substring(i*32+26,i*32+28);
							
							String time = getUnlockTime(sklog.substring(i*32+28, i*32+36)); //时间
							
							ur.setLockCode(lockId);
							ur.setKeyCode(keyId);
							ur.setSmartKeyPerId(smartKeyPerId);
							ur.setRecordCode(typeCode);
							ur.setRecordTpye(ResponseCommandUtil.getSmartKeyRecordType(typeCode));
							ur.setUnlockTime(time);
							
							dao.save(ur);
						}else{
							break;
						}
					}
				}catch (Exception e) {
					smartKeyRecordLog.info(sklog);
				}
			}
		}catch (Exception e) {
			throw new ErrorCodeException(e.getMessage());
		}
		return null;
	}
	
	public static String getUnlockTime(String hexStr) throws Exception{
		byte[] recordByte = CommunCrypUtil.dataToByte(hexStr);
		
        byte[] timeBt1 = CommunCrypUtil.getBooleanArray(recordByte[0]);
		byte[] timeBt2 = CommunCrypUtil.getBooleanArray(recordByte[1]);
		byte[] timeBt3 = CommunCrypUtil.getBooleanArray(recordByte[2]);
		byte[] timeBt4 = CommunCrypUtil.getBooleanArray(recordByte[3]);
		
		String yearBts = "00" + timeBt1[2] + timeBt1[3] + timeBt1[4] + timeBt1[5] + timeBt1[6] + timeBt1[7];
		String monthBts = "0000" + timeBt1[0] + timeBt1[1] + timeBt2[0] + timeBt2[1];
		String dayBts = "000" + timeBt3[3] + timeBt3[4] + timeBt3[5] + timeBt3[6] + timeBt3[7];
		String hoursBts = "000" + timeBt4[3] + timeBt4[4] + timeBt4[5] + timeBt4[6] + timeBt4[7];
		String minutesBts = "00" + timeBt2[2] + timeBt2[3] + timeBt2[4] + timeBt2[5] + timeBt2[6] + timeBt2[7];
		String secondsBts = "00" + timeBt3[0] + timeBt3[1] + timeBt3[2] + timeBt4[0] + timeBt4[1] + timeBt4[2];
		
		String year = "20" + timeFormatting(Integer.valueOf(yearBts, 2));
		String month = timeFormatting(Integer.valueOf(monthBts, 2));
		String day = timeFormatting(Integer.valueOf(dayBts, 2));
		String hours = timeFormatting(Integer.valueOf(hoursBts, 2));
		String minutes = timeFormatting(Integer.valueOf(minutesBts, 2));
		String seconds = timeFormatting(Integer.valueOf(secondsBts, 2));
		
		return year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
    }
	
	public String extractLockId(String dataLog) throws ErrorCodeException {
		try{
			AppLogMessage smartKeyLog = GsonUtil.fromJson(dataLog, AppLogMessage.class);
			String[] lockIds = new String[3];
			for(String sklog : smartKeyLog.getSmartKeyLog()){
				sklog = sklog.substring(12, sklog.length() - 4);
				String lockId = null;
				for (int i = 0; i < 8; i++) { //默认读取8条记录
					if(!sklog.substring(i*32, i*32+32).contains("FFFFFFFF")){
						lockId = sklog.substring(i*32+4, i*32+12);
						lockIds[0] = lockIds[1];
						lockIds[1] = lockIds[2];
						lockIds[2] = lockId;
					}else{
						break;
					}
				}
			}
			if(StringUtil.isNotEmpty(lockIds[0]) && 
					(lockIds[0].equalsIgnoreCase(lockIds[1]) || lockIds[0].equalsIgnoreCase(lockIds[2]))){
				return lockIds[0];
			}else if(StringUtil.isNotEmpty(lockIds[1]) && lockIds[1].equalsIgnoreCase(lockIds[2])){
				return lockIds[1];
			}else{
				throw new ErrorCodeException("锁芯ID提取失败，请重新开锁3次，再提取！");
			}
		}catch (Exception e) {
			throw new ErrorCodeException(e.getMessage());
		}
	}
	
	public static String timeFormatting(Integer value){
		if(value < 10){
			return "0" + value;
		}
		return value.toString();
	}
}
