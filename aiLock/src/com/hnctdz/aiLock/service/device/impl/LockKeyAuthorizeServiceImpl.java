package com.hnctdz.aiLock.service.device.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.appInterface.AppServices;
import com.hnctdz.aiLock.dao.device.DevKeyInfoDao;
import com.hnctdz.aiLock.dao.device.DevLockInfoDao;
import com.hnctdz.aiLock.dao.device.LockKeyAuthorizeDao;
import com.hnctdz.aiLock.dao.info.PersonnelInfoDao;
import com.hnctdz.aiLock.dao.info.UpdateAuthorizeDao;
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

/** 
 * @ClassName LockKeyAuthorizeServiceImpl.java
 * @Author WangXiangBo 
 */
@Service("com.hnctdz.aiLock.service.device.LockKeyAuthorizeService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class LockKeyAuthorizeServiceImpl implements LockKeyAuthorizeService{
	private static final Logger LOG = Logger.getLogger(AppServices.class);
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
	@Autowired
	private UpdateAuthorizeDao updateAuthorizeDao;
	
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
				aut.setLockType(locke.getLockType());
			}
			
			if(Constants.AUTHORIZE_MANAGE.equals(aut.getAuthorizeType())){
				SysUser user = sysUserDao.getById(aut.getCuUserId());
				if(user != null){
					aut.setCuUserName(user.getName());
				}
			}else{
				PersonnelInfo cuPer = personnelInfoDao.getById(aut.getCuUserId());
				if(cuPer != null){
					aut.setCuUserName(cuPer.getPerName());
				}
			}
		}
		dp.setRows(autList);
		return dp;
	}
	
	public List<LockKeyAuthorize> findTurnAuthorization(LockKeyAuthorizeDto dto, DataPackage dp){
		dp = this.dao.findPageLockKeyAuthorize(dto, dp);
		List<LockKeyAuthorize> autList = (List<LockKeyAuthorize>)dp.getRows();
		for(LockKeyAuthorize aut : autList){
			PersonnelInfo per = personnelInfoDao.getById(aut.getUnlockPerId());
			if(per != null){
				aut.setUnlockPerName(per.getPerName());
			}
			DevLockInfo locke = devLockInfoDao.getById(aut.getLockId());
			if(locke != null){
				aut.setLockName(locke.getLockName());
			}
		}
		return autList;
	}
	
	public LockKeyAuthorize getById(Long id){
		return dao.getById(id);
	}
	
	public List<LockKeyAuthorize> findLockKeyAuthorizeByPerId(Long perId){
		return dao.findLockKeyAuthorizeByPerId(perId);
	}
	
	public List<LockKeyAuthorize> findLockKeyAuthorizeByCode(String authorizeCode){
		return dao.findLockKeyAuthorizeByCode(authorizeCode);
	}
	
	public String saveLockKeyAuthorize(LockKeyAuthorize lockAh) throws ErrorCodeException{
		StringBuffer saveResult = new StringBuffer();
		try {
			Map<Long, Long> changePerMap = new HashMap<Long, Long>();
			//获取数据库中当前分组中的门锁对象
			List<LockKeyAuthorize> currentLockKeyAuthorizeList = findLockKeyAuthorizeByCode(lockAh.getAuthorizeCode());
			
			String updateInfoIds = "";
			String uf = "";
			if (StringUtil.isEmpty(lockAh.getAuthorizeLockIds())) {//前台传过来的是空对象
				//删除当前数据库中的所有对象
//				this.dao.deleteAuthorizeIds(lockAh.getAuthorizeCode(),null);
				LockKeyAuthorizeDto dto = new LockKeyAuthorizeDto();
				dto.setAuthorizeCode(lockAh.getAuthorizeCode());
				this.dao.deleteAuthorize(dto);
				return null;
			}
			
			LockKeyAuthorize lockKeyAuthorize = null;
			String[] authorizeLockArray = lockAh.getAuthorizeLockIds().split(",");
			String[] authorizePerArray = lockAh.getAuthorizePerIds().split(",");
			
			for(int i=0; i < authorizeLockArray.length; i++){
				String autLockId = authorizeLockArray[i];
				DevLockInfo lock = devLockInfoDao.getById(Integer.parseInt(autLockId));
				lockAh.setLockDeviceNo(lock.getLockDeviceNo());
				lockAh.setLockInModuleCode(lock.getLockInModuleCode());
				try{
					for(int j=0; j < authorizePerArray.length; j++){
						String autPerId = authorizePerArray[j];
						lockAh.setUnlockPerId(Long.parseLong(autPerId));
						boolean newBl = true;
						for(LockKeyAuthorize lka : currentLockKeyAuthorizeList){
							//判断前端传过来的开锁人和门锁在数据库中是否存在
							if(lka.getLockId().toString().equalsIgnoreCase(autLockId)
									&& lka.getUnlockPerId().equals(lockAh.getUnlockPerId())){
								//判断门锁如果是有源门锁且权限时间有变更，则需要把新的权限时间写入到门锁中。
								if(Constants.ELECTRIC_LOCK.equals(lock.getLockType()) && lock.getWheCanMatchCard().equals(Constants.CAN_MATCH_CARD)
										&& lka.getEndTime().getTime() != lockAh.getEndTime().getTime()){
									boolean cardPerBl = addOrUpdateCardPer(lockAh);
									if(cardPerBl){
										lka.setStartTime(lockAh.getStartTime());
										lka.setEndTime(lockAh.getEndTime());
										lka.setUnlockNumber(lockAh.getUnlockNumber());
										lka.setCuUserId(lockAh.getCuUserId());
										lka.setCuTime(lockAh.getCuTime());
										lka.setBlueUnlock(lockAh.getBlueUnlock());
										lka.setAuthorizeType(lockAh.getAuthorizeType());
										lka.setStatusCode(lockAh.getStatusCode());
										lka.setScopeUnlock(lockAh.getScopeUnlock());
										
										lka.setLockDeviceNo(lock.getLockDeviceNo());
										lka.setLockInModuleCode(lock.getLockInModuleCode());
										dao.updateLka(lka);
										
										if(changePerMap.get(lockAh.getUnlockPerId()) == null){
											updateAuthorizeDao.UpdateAuthorizeToChange(lockAh.getUnlockPerId());
											changePerMap.put(lockAh.getUnlockPerId(), lockAh.getUnlockPerId());
										}
									}else{
										if(StringUtil.isNotBlank(lock.getVicePassiveLockCode())){
											DevLockInfo viceLock = devLockInfoDao.getDevLockInfoByLockCode(lock.getVicePassiveLockCode());
											authorizeLockArray[i] = viceLock.getLockId().toString();
											i--;
										}
										saveResult.append("门锁：").append(lock.getLockName()).append("写入开锁权限失败<br>");
									}
								}else{
									if(lka.getStartTime().getTime() != lockAh.getStartTime().getTime() || lka.getEndTime().getTime() != lockAh.getEndTime().getTime()){
										if(changePerMap.get(lockAh.getUnlockPerId()) == null){
											updateAuthorizeDao.UpdateAuthorizeToChange(lockAh.getUnlockPerId());
											changePerMap.put(lockAh.getUnlockPerId(), lockAh.getUnlockPerId());
										}
									}
									updateInfoIds += uf + lka.getAuthorizeId();
									uf = ",";
								}
								
								newBl = false;
								break;
							}
						}
						
						if(newBl){
							if(StringUtil.isEmpty(lockAh.getAuthorizeCode())){//新增添加授权批次码
								lockAh.setAuthorizeCode(CommonUtil.getRandomLettersUp(4) + DateUtil.getDateTime2());
							}
							
							lockKeyAuthorize = lockAh;
							lockKeyAuthorize.setAuthorizeId(null);
							lockKeyAuthorize.setLockId(Integer.parseInt(autLockId));
							if(Constants.ELECTRIC_LOCK.equals(lock.getLockType()) && 
									 Constants.CAN_MATCH_CARD.equals(lock.getWheCanMatchCard())){    //有源锁授权
								boolean cardPerBl = addOrUpdateCardPer(lockKeyAuthorize);
								if(cardPerBl){
										System.out.println("有源授权！成功！");
										dao.saveLka(lockKeyAuthorize);
								}else{
									if(StringUtil.isNotBlank(lock.getVicePassiveLockCode())){
										DevLockInfo viceLock = devLockInfoDao.getDevLockInfoByLockCode(lock.getVicePassiveLockCode());
										authorizeLockArray[i] = viceLock.getLockId().toString();
										i--;
									}
									saveResult.append("门锁：").append(lock.getLockName()).append("写入开锁权限失败<br>");
								}
							}else{//无源锁授权
									System.out.println("无源授权！成功！");
									dao.saveLka(lockKeyAuthorize);
							}
							
							if(changePerMap.get(lockAh.getUnlockPerId()) == null){
								updateAuthorizeDao.UpdateAuthorizeToChange(lockAh.getUnlockPerId());
								changePerMap.put(lockAh.getUnlockPerId(), lockAh.getUnlockPerId());
							}
						}
					}
				}catch (ErrorCodeException e) {
					if(StringUtil.isNotBlank(lock.getVicePassiveLockCode())){
						DevLockInfo viceLock = devLockInfoDao.getDevLockInfoByLockCode(lock.getVicePassiveLockCode());
						authorizeLockArray[i] = viceLock.getLockId().toString();
						i--;
					}
					saveResult.append("门锁：").append(lock.getLockName()).append(e.getMessage()).append("<br>");
				}catch (Exception e) {
					e.printStackTrace();
					saveResult.append("门锁：").append(lock.getLockName()).append("授权出现异常<br>");
				}
			}
			
			String deleteAuthorizeIds = "";
			List<LockKeyAuthorize> delActiveAuthorizeArray = new ArrayList<LockKeyAuthorize>();
			String df = "";
			for(LockKeyAuthorize lka : currentLockKeyAuthorizeList){
				boolean bl = true;
				
				for(int i=0; i < authorizeLockArray.length; i++){
					String autLockId = authorizeLockArray[i];
					for(int j=0; j < authorizePerArray.length; j++){
						String autPerId = authorizePerArray[j];
						if(lka.getLockId().toString().equalsIgnoreCase(autLockId)
								&& lka.getUnlockPerId().toString().equalsIgnoreCase(autPerId)){
							bl = false;
							break;
						}
					}
				}
				if(bl){
					boolean delBl = true;
					if(StringUtil.isNotEmpty(lka.getLockDeviceNo())){
						DevLockInfo lock = devLockInfoDao.getById(lka.getLockId());
						if(lock.getWheCanMatchCard() == Constants.CAN_MATCH_CARD){
							delActiveAuthorizeArray.add(lka);
							boolean cardPerBl = deleteCardPer(lka, null);
							if(!cardPerBl){
								delBl = false;
								saveResult.append("门锁：").append(lock.getLockName()).append("删除开锁权限失败<br>");
							}
						}
					}
					if(delBl){
//						if(changePerMap.get(lka.getUnlockPerId()) == null){
//							updateAuthorizeDao.UpdateAuthorizeToChange(lka.getUnlockPerId());
//							changePerMap.put(lka.getUnlockPerId(), lka.getUnlockPerId());
//						}
						deleteAuthorizeIds += df + lka.getAuthorizeId();
						df = ",";
					}
				}
			}
			if(StringUtil.isNotEmpty(deleteAuthorizeIds)){
//				this.dao.deleteAuthorizeIds(lockAh.getAuthorizeCode(), deleteAuthorizeIds);
				LockKeyAuthorizeDto dto = new LockKeyAuthorizeDto();
				dto.setAuthorizeIds(deleteAuthorizeIds);
				this.dao.deleteAuthorize(dto);
			}
			
			if(StringUtil.isNotEmpty(updateInfoIds)){
				this.dao.updateAuthorizeIds(lockAh, updateInfoIds);
			}
		} catch (Exception e) {
			LOG.error(e);
			throw new ErrorCodeException("授权配置失败，请刷新页面重新配置！", e.getMessage());
		}
		if(StringUtil.isNotEmpty(saveResult.toString())){
			saveResult.append("其余门锁授权成功！");
		}
		return saveResult.toString();
	}
	
	public boolean addOrUpdateCardPer(LockKeyAuthorize lka)throws ErrorCodeException{
		boolean bl = true;
		Mess mess = new Mess();
		mess.setDeviceNo(lka.getLockDeviceNo());
		mess.setModuleCode(lka.getLockInModuleCode());
		
		DevKeyInfoDto dto = new DevKeyInfoDto();
		dto.setPerId(lka.getUnlockPerId());
		List<DevKeyInfo> list = devKeyInfoDao.findDevKeyInfoList(dto);
		if(list.size() > 0){
			for(DevKeyInfo ki : list){
				if(ki.getKeyType() != 1L){//智能钥匙 不需要添加有源门锁权限中
					String url = mess.addOrUpdateCardPer(ki.getKeyCode(), lka.getEndTime(), false, 0);
					String returnJson = HttpDeviceManageForRest.getInstance().httpRequestForLockdm(url);
					
					CommandInfo commandInfo = GsonUtil.fromJson(returnJson, CommandInfo.class);
					if(!ReturnCodeUtil.SUCCESS_RESULT_CODE.equalsIgnoreCase(commandInfo.getReturnCode())){
						throw new ErrorCodeException(ErrorCode.getRemoteOperationResults(commandInfo.getReturnCode()));
					}else{
						if(ResponseCommandUtil.SUCCESS_COMMAND.equalsIgnoreCase(commandInfo.getReturnMessage().substring(8, 10))){
							bl = false;
						}
					}
				}
			}
		}
		return bl;
	}
	
	public boolean deleteCardPer(LockKeyAuthorize lka, List<DevKeyInfo> keyList)throws ErrorCodeException{
		boolean bl = true;
		Mess mess = new Mess();
		mess.setDeviceNo(lka.getLockDeviceNo());
		mess.setModuleCode(lka.getLockInModuleCode());
		
		if(keyList == null){
			DevKeyInfoDto dto = new DevKeyInfoDto();
			dto.setPerId(lka.getUnlockPerId());
			keyList = devKeyInfoDao.findDevKeyInfoList(dto);
		}
		for(DevKeyInfo ki : keyList){
			if(ki.getKeyType() != 1L){//智能钥匙 不会配置在有源门锁中
				String url = mess.deleteCardPer(ki.getKeyCode());
				String returnJson = HttpDeviceManageForRest.getInstance().httpRequestForLockdm(url);
				
				CommandInfo commandInfo = GsonUtil.fromJson(returnJson, CommandInfo.class);
				if(!ReturnCodeUtil.SUCCESS_RESULT_CODE.equalsIgnoreCase(commandInfo.getReturnCode())){
					throw new ErrorCodeException(ErrorCode.getRemoteOperationResults(commandInfo.getReturnCode()));
				}else{
					if(!ResponseCommandUtil.SUCCESS_COMMAND.equalsIgnoreCase(commandInfo.getReturnMessage().substring(8, 10))){
						bl = false;
					}
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
	
	public void deleteAuthorize(LockKeyAuthorizeDto dto){
		dao.deleteAuthorize(dto);
	}
}
