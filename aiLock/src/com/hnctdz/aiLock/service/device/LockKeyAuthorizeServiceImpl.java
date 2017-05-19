/*package com.hnctdz.aiLock.service.device;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.device.DevKeyInfoDao;
import com.hnctdz.aiLock.dao.device.DevLockInfoDao;
import com.hnctdz.aiLock.dao.device.LockKeyAuthorizeDao;
import com.hnctdz.aiLock.dao.info.PersonnelInfoDao;
import com.hnctdz.aiLock.dao.system.SysUserDao;
import com.hnctdz.aiLock.domain.device.DevKeyInfo;
import com.hnctdz.aiLock.domain.device.LockKeyAuthorize;
import com.hnctdz.aiLock.domain.device.DevLockInfo;
import com.hnctdz.aiLock.domain.info.PersonnelInfo;
import com.hnctdz.aiLock.domain.system.SysUser;
import com.hnctdz.aiLock.dto.CommandInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.DevKeyInfoDto;
import com.hnctdz.aiLock.dto.device.LockKeyAuthorizeDto;
import com.hnctdz.aiLock.https.HttpDeviceManageForRest;
import com.hnctdz.aiLock.service.device.LockKeyAuthorizeService;
import com.hnctdz.aiLock.utils.CommonUtil;
import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.DateUtil;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.hnctdz.aiLock.utils.GsonUtil;
import com.hnctdz.aiLock.utils.Mess;
import com.hnctdz.aiLock.utils.ResponseCommandUtil;
import com.hnctdz.aiLock.utils.ReturnCodeUtil;
import com.hnctdz.aiLock.utils.StringUtil;

*//** 
 * @ClassName LockKeyAuthorizeServiceImpl.java
 * @Author WangXiangBo 
 *//*
@Service("com.hnctdz.aiLock.service.device.LockKeyAuthorizeService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class LockKeyAuthorizeServiceImpl implements LockKeyAuthorizeService{
	@Autowired
	private LockKeyAuthorizeDao dao;
	@Autowired
	private PersonnelInfoDao personnelInfoDao;
	@Autowired
	private DevLockInfoDao devLockInfoDao;
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private DevKeyInfoDao devKeyInfoDao;
	
	
	public DataPackage findPageLockKeyAuthorize(LockKeyAuthorizeDto dto, DataPackage dp){
		dp = this.dao.findPageLockKeyAuthorize(dto, dp);
		List<LockKeyAuthorize> autList = (List<LockKeyAuthorize>)dp.getRows();
		for(LockKeyAuthorize aut : autList){
			PersonnelInfo per = personnelInfoDao.getById(aut.getUnlockPerId());
			if(per != null){
				aut.setUnlockPerName(per.getPerName());
			}
			DevLockInfo locke = devLockInfoDao.getById(aut.getLockId());
			if(locke != null){
				aut.setLockCode(locke.getLockCode());
				aut.setLockName(locke.getLockName());
			}
			SysUser user = sysUserDao.getById(aut.getCuUserId());
			if(user != null){
				aut.setCuUserName(user.getName());
			}
		}
		dp.setRows(autList);
		return dp;
	}
	
	public List<LockKeyAuthorize> findLockKeyAuthorizeByCode(String authorizeCode){
		return dao.findLockKeyAuthorizeByCode(authorizeCode);
	}
	
	
	public String saveLockKeyAuthorize(LockKeyAuthorize lockAh) throws ErrorCodeException{
		StringBuffer saveResult = new StringBuffer();
		try {
			//存放用于新增的对象
			List<LockKeyAuthorize> lockKeyAuthorizeListForSave = new ArrayList<LockKeyAuthorize>();
			//存放用于修改开锁人员的对象
			List<LockKeyAuthorize> authorizeListForUpdatePer = new ArrayList<LockKeyAuthorize>();
			//获取数据库中当前分组中的门锁对象
			List<LockKeyAuthorize> currentLockKeyAuthorizeList = findLockKeyAuthorizeByCode(lockAh.getAuthorizeCode());
			
			String updateInfoIds = "";
			String uf = "";
			if (StringUtil.isEmpty(lockAh.getLockAuthorizeInfos())) {//前台传过来的是空对象
				//删除当前数据库中的所有对象
				this.dao.deleteAuthorizeIds(lockAh.getAuthorizeCode(),null);
			} else {
				LockKeyAuthorize lockKeyAuthorize = null;
				String[] lockAuthorizeInfoArray = lockAh.getLockAuthorizeInfos().split(";");
				
				for(int i=0; i < lockAuthorizeInfoArray.length; i++){
					String[] lockAuthorizeInfo = lockAuthorizeInfoArray[i].split(",");
					DevLockInfo lock = devLockInfoDao.getById(Long.parseLong(lockAuthorizeInfo[1]));
					try{
						boolean bl = true;
						//判断授权ID是否为空，为空则是新增数据
						if(StringUtil.isNotBlank(lockAuthorizeInfo[0])){
							for(LockKeyAuthorize lka : currentLockKeyAuthorizeList){
								//判断前端传过来的对象与数据库中的对象是否相同
								if(lka.getAuthorizeId().equalsIgnoreCase(lockAuthorizeInfo[0])
										&& lka.getLockId().toString().equalsIgnoreCase(lockAuthorizeInfo[1])){
									
									if(Constants.ELECTRIC_LOCK.equals(lock.getLockType())){
										//如果是有源锁，且开锁时间或开锁次数有修改，则需要发送修改指令给门锁
										if(!lka.getUnlockPerId().toString().equalsIgnoreCase(lockAuthorizeInfo[2]) 
												|| lka.getEndTime() != lockAh.getEndTime() 
												|| lka.getUnlockNumber().equals(lockAh.getUnlockNumber())){
//											lockKeyAuthorize = lockAh;
//											lockKeyAuthorize.setAuthorizeId(lka.getAuthorizeId());
//											lockKeyAuthorize.setLockId(lka.getLockId());
//											lockKeyAuthorize.setUnlockPerId(Long.parseLong(lockAuthorizeInfo[2]));
											lka.setUnlockPerId(Long.parseLong(lockAuthorizeInfo[2]));
											lka.setStartTime(lockAh.getStartTime());
											lka.setEndTime(lockAh.getEndTime());
											lka.setUnlockNumber(lockAh.getUnlockNumber());
											lka.setCuUserId(lockAh.getCuUserId());
											lka.setCuTime(lockAh.getCuTime());
											
											lka.setLockDeviceNo(lock.getLockDeviceNo());
											lka.setLockInModuleCode(lock.getLockInModuleCode());
											boolean cardPerBl = addOrUpdateCardPer(lka);
											if(cardPerBl){
												dao.updateLka(lka);
											}else{
												saveResult.append("门锁:").append(lock.getLockCode()).append("修改开锁信息失败<br>");
											}
										}
									}else{
										if(!lka.getUnlockPerId().toString().equalsIgnoreCase(lockAuthorizeInfo[2])){
											lka.setUnlockPerId(Long.parseLong(lockAuthorizeInfo[2]));
											lka.setStartTime(lockAh.getStartTime());
											lka.setEndTime(lockAh.getEndTime());
											lka.setUnlockNumber(lockAh.getUnlockNumber());
											lka.setCuUserId(lockAh.getCuUserId());
											lka.setCuTime(lockAh.getCuTime());
											
											dao.updateLka(lka);
										}else{
											updateInfoIds += uf + lka.getAuthorizeId();
											uf = ",";
										}
									}
									
									bl = false;
									break;
								}
							}
						}
						if(bl){
							if(StringUtil.isEmpty(lockAh.getAuthorizeCode())){
								lockAh.setAuthorizeCode(CommonUtil.getRandomLettersUp(4) + DateUtil.getDateTime2());
							}
							
							lockKeyAuthorize = lockAh;
							lockKeyAuthorize.setAuthorizeId(null);
							lockKeyAuthorize.setLockId(Long.parseLong(lockAuthorizeInfo[1]));
							lockKeyAuthorize.setUnlockPerId(Long.parseLong(lockAuthorizeInfo[2]));
							
							if(Constants.ELECTRIC_LOCK.equals(lock.getLockType())){
								lockKeyAuthorize.setLockDeviceNo(lock.getLockDeviceNo());
								lockKeyAuthorize.setLockInModuleCode(lock.getLockInModuleCode());
								boolean cardPerBl = addOrUpdateCardPer(lockKeyAuthorize);
								if(cardPerBl){
									dao.saveLka(lockKeyAuthorize);
								}else{
									saveResult.append("门锁:").append(lock.getLockCode()).append("修改开锁信息失败<br>");
								}
							}else{
								dao.saveLka(lockKeyAuthorize);
							}
						}
						
						
					}catch (ErrorCodeException e) {
						saveResult.append("门锁:").append(lock.getLockCode()).append(e.getMessage()).append("<br>");
					}catch (Exception e) {
						e.printStackTrace();
						saveResult.append("门锁:").append(lock.getLockCode()).append("授权出现异常<br>");
					}
				}
				
//				if(lockKeyAuthorizeListForSave != null && lockKeyAuthorizeListForSave.size() != 0){
//					this.dao.saveOfBatch(lockKeyAuthorizeListForSave);
//				}
				
				String deleteAuthorizeIds = "";
				String df = "";
				for(LockKeyAuthorize lka : currentLockKeyAuthorizeList){
					boolean bl = true;
					for(int i=0; i < lockAuthorizeInfoArray.length; i++){
						String[] lockAuthorizeInfo = lockAuthorizeInfoArray[i].split(",");
						
						if(lka.getAuthorizeId().toString().equalsIgnoreCase(lockAuthorizeInfo[0])){
							bl = false;
							break;
						}
					}
					if(bl){
						deleteAuthorizeIds += df + lka.getAuthorizeId();
						df = ",";
						//LockKeyAuthorizeListForDel.add(lka);
					}
				}
				if(StringUtil.isNotEmpty(deleteAuthorizeIds)){
					this.dao.deleteAuthorizeIds(lockAh.getAuthorizeCode(), deleteAuthorizeIds);
				}
//				if(LockKeyAuthorizeListForDel != null && LockKeyAuthorizeListForDel.size() != 0){
//					this.dao.deleteOfBatch(LockKeyAuthorizeListForDel);
//				}
			}
			if(StringUtil.isNotEmpty(updateInfoIds)){
				this.dao.updateAuthorizeIds(lockAh, updateInfoIds);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorCodeException("授权配置失败，请刷新页面重新配置！", e.getMessage());
		}
		return saveResult.toString();
	}
	
	public boolean addOrUpdateCardPer(LockKeyAuthorize lka)throws ErrorCodeException{
		boolean bl = false;
		Mess mess = new Mess();
		mess.setDeviceNo(lka.getLockDeviceNo());
		mess.setModuleCode(lka.getLockInModuleCode());
		
		DevKeyInfoDto dto = new DevKeyInfoDto();
		dto.setPerId(lka.getUnlockPerId());
		List<DevKeyInfo> list = devKeyInfoDao.findDevKeyInfoList(dto);
		if(list.size() <= 0){
			throw new ErrorCodeException("授权人没有有效钥匙");
		}
		for(DevKeyInfo ki : list){
			String url = mess.addOrUpdateCardPer(ki.getKeyCode(), lka.getEndTime(), false, 
					Integer.parseInt(lka.getUnlockNumber().toString()));
			String returnJson = HttpDeviceManageForRest.getInstance().httpRequestForLockdm(url);
			
			CommandInfo commandInfo = GsonUtil.fromJson(returnJson, CommandInfo.class);
			if(!ReturnCodeUtil.SUCCESS_RESULT_CODE.equalsIgnoreCase(commandInfo.getReturnCode())){
				throw new ErrorCodeException(ErrorCode.getErrorInfo(commandInfo.getReturnCode()));
			}else{
				if(ResponseCommandUtil.SUCCESS_COMMAND.equalsIgnoreCase(commandInfo.getReturnMessage().substring(8, 10))){
					bl = true;
				}
			}
		}
		return bl;
	}
	
	public List findAuthorizeLockList(String authorizeCode){
		return dao.findAuthorizeLockList(authorizeCode);
	}
	
	public List findAuthorizePerList(String authorizeCode){
		return dao.findAuthorizePerList(authorizeCode);
	}
	
	public void deleteLockKeyAuthorizeByIds(String keyCodes){
		String[] keyCodeArrey = keyCodes.split(",");
		for(String keyCode : keyCodeArrey){
			try{
				LockKeyAuthorize LockKeyAuthorize = dao.getById(keyCode);
				LockKeyAuthorize.setStatusCode("0");//失效标记
				dao.save(LockKeyAuthorize);
			} catch (Exception e) {
			}
		}
	}
}
*/