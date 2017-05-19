package com.hnctdz.aiLock.action.device;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.nutz.json.annotations.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hnctdz.aiLock.action.BaseAction;
import com.hnctdz.aiLock.domain.analysis.UnlockRecords;
import com.hnctdz.aiLock.domain.device.DevLockInfo;
import com.hnctdz.aiLock.domain.system.SysUser;
import com.hnctdz.aiLock.dto.CommandInfo;
import com.hnctdz.aiLock.dto.device.DevLockInfoDto;
import com.hnctdz.aiLock.dto.system.SysRoleDto;
import com.hnctdz.aiLock.https.HttpDeviceManageForRest;
import com.hnctdz.aiLock.security.SecurityUserHolder;
import com.hnctdz.aiLock.service.analysis.UnlockRecordsService;
import com.hnctdz.aiLock.service.device.DevLockInfoService;
import com.hnctdz.aiLock.service.system.SysRoleService;
import com.hnctdz.aiLock.utils.CommunCrypUtil;
import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.DateUtil;
import com.hnctdz.aiLock.utils.Mess;
import com.hnctdz.aiLock.utils.ResponseCommandUtil;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.hnctdz.aiLock.utils.GsonUtil;
import com.hnctdz.aiLock.utils.ReturnCodeUtil;
import com.hnctdz.aiLock.utils.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName DevLockInfoAction.java
 * @Author WangXiangBo (wangxiangbo127@163.com)
 */
@SuppressWarnings("serial")
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/DevLockInfoAction")
@Controller
public class DevLockInfoAction extends BaseAction{
	private static final Logger LOG = Logger.getLogger(DevLockInfoAction.class);  
	
 	@Autowired
 	private DevLockInfoService devLockInfoService;
 	@Autowired
	private UnlockRecordsService unlockRecordsService;
 	
 	@Autowired
	private SysRoleService sysRoleService;
 	
 	private DevLockInfo lockInfo;
 	private DevLockInfoDto dto;
 	private String lockIds;
 	private File importFile;//文件上传
 	
 	/**
	 * 查询门锁资料列表
	 */
	@Action(value="findPageDevLockInfo")
	@ToJson(outField="dataPackage")
	public String findPageDevLockInfo(){
		dataPackage = getDataPackage();
		try {
			dataPackage = devLockInfoService.findPageDevLockInfo(dto, dataPackage);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 保存门锁资料信息
	 */
	@Action(value="saveDevLockInfo")
	@ToJson(outField="simpleRespose")
	public String saveDevLockInfo(){
		String resultMessage = "";
		try{
			if(devLockInfoService.checkLocExist(lockInfo)){
				if(devLockInfoService.checkBluetoothExist(lockInfo)){  //判断是否存在相同蓝牙
					if(lockInfo.getLockType() == Constants.ACTIVE_LOCK){
						lockInfo.setLockInModuleCode("FA071302FA02" + lockInfo.getLockInModuleCode() + "FAFF");
					}
					devLockInfoService.saveDevLockInfo(lockInfo);
				}else{
					if(lockInfo.getLockType() == Constants.ACTIVE_LOCK){
						resultMessage = "蓝牙锁已存在该模块中！";
					}else{
						resultMessage = "该门蓝牙名称已存在！";
					}
				}
			}else{
				if(lockInfo.getLockType() == Constants.ACTIVE_LOCK){
					resultMessage = "门锁机号已存在该模块中！";
				}else{
					resultMessage = "该门锁编码已存在！";
				}
			}
		} catch(ErrorCodeException e) {
			resultMessage = e.getMessage();
		}  catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			resultMessage = ErrorCode.ERROR_SYS_EXCEPTION;
		} finally{
			if(StringUtil.isNotEmpty(resultMessage)){
				simpleRespose.setResultCode(0001);
				simpleRespose.setResultMessage(resultMessage);
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 导入门锁
	 */
	@Action(value="importLockInfos")
	@ToJson(outField="simpleRespose")
	public String importLockInfos() {
		String result = "";
		try{
			result = devLockInfoService.saveImportLockInfos(importFile);
		}catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			result = ErrorCode.ERROR_SYS_EXCEPTION;
		} finally {
			if(StringUtil.isNotEmpty(result)){
				result = result.replaceAll("、行", "行");
				simpleRespose.setResultCode(0001);
				simpleRespose.setResultMessage(result);
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 查询门锁资料列表List
	 */
	@Action(value="findNormalLockInfoList")
	@ToJson(outField="dataPackage")
	public String findNormalLockInfoList(){
		dataPackage = getDataPackage();
		try {
			List<DevLockInfo> list = devLockInfoService.findNormalLockInfoList(dto);
			dataPackage.setRows(list);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 查询门锁资料列表List
	 */
	@Action(value="getLockCountInfo")
	@ToJson(outField="simpleRespose")
	public String getLockCountInfo(){
		String result = "";
		try {
			result = devLockInfoService.getLockCountInfo(dto);
			JSONObject json = new JSONObject().fromObject(result);
			result = json.toString();
		}catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(00001);
			result = ErrorCode.ERROR_SYS_EXCEPTION;
		} finally {
			if(StringUtil.isNotEmpty(result)){
				result = result.replaceAll("、行", "行");
				simpleRespose.setResultCode(0000);
				simpleRespose.setResultMessage(result);
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 查询门锁资料列表List(下拉框、dataList 使用)
	 */
	@Action(value="findNormalLockListByCombobox")
	@ToJson(outField="dataPackage")
	public String findNormalLockListByCombobox(){
		dataPackage = getDataPackage();
		try {
			dataPackage.setRows(devLockInfoService.findNormalLockListByCombobox());
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 删除门锁资料信息
	 */
	@Action(value="deleteDevLockInfo")
	@ToJson(outField="simpleRespose")
	public String deleteDevLockInfo(){
		String result = "";
		try{
			
			boolean isRealDele = false;
			SysRoleDto srd = new SysRoleDto();
			SysUser sysUser = SecurityUserHolder.getCurrentUser();
			List<Object> sr = this.sysRoleService.findSysRoleByUserid(sysUser.getUserId());
			Object [] obj =  (Object[]) sr.get(0);
			for (Iterator iterator = sr.iterator(); iterator.hasNext();) {
				Object[] object = (Object[]) iterator.next();
				System.out.println(object[1].toString());
				if(object[1].toString().contains("超级管理员")){
					isRealDele = true;
					break;
				}
			}
			if(isRealDele){
				result = devLockInfoService.deleteRealDevLockInfoByIds(lockIds); //物理删除
			}else{
				result = devLockInfoService.deleteDevLockInfoByIds(lockIds); //逻辑删除
			}
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		} finally{
			if(StringUtil.isNotEmpty(result)){
				simpleRespose.setResultCode(0001);
				simpleRespose.setResultMessage(result);
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 查询门锁按区域统计List
	 */
	@Action(value="findAreaStatisticalLock")
	@ToJson(outField="dataPackage")
	public String findAreaStatisticalLock(){
		dataPackage = getDataPackage();
		try {
			if(dto == null){
				dto = new DevLockInfoDto();
			}
			dto.setAreaId(SecurityUserHolder.getCurrentUser().getAreaId());
			dataPackage.setRows(devLockInfoService.findAreaStatisticalLock(dto));
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	
	/**
	 * 操作门锁前检查相关信息
	 */
	private String checkLockInfo(){
		String resultMessage = "";
		if(dto.getLockId() != null){
			DevLockInfo dli = devLockInfoService.getById(dto.getLockId());
			if(dli.getLockType() == 2){
				if(StringUtil.isNotEmpty(dli.getLockDeviceNo())){
					if("1".equalsIgnoreCase(dli.getStatus())){
						lockInfo = dli;
					}else{
						resultMessage = "门锁处于故障等状态中,不能对该门锁进行操作！";
					}
				}else{
					resultMessage = "该门锁没有配置对应机号！";
				}
			}else{
				resultMessage = "该门锁不支持远程操作！";
			}
		}else{
			resultMessage = "请求参数不全，请刷新数据重新操作！";
		}
		return resultMessage;
	}
	
	/**
	 * 远程开锁
	 */
	@Action(value="remoteUnlock")
	@ToJson(outField="simpleRespose")
	public String remoteUnlock(){
		System.out.println("接收到开锁请求："+ DateUtil.getDateTime());
		String returnCode = "", resultMsg = "";
		UnlockRecords ur = new UnlockRecords();
		try{
			resultMsg = checkLockInfo();
			if(StringUtil.isEmpty(resultMsg)){
				ur.setLockType(Constants.ACTIVE_LOCK);
				ur.setPerId(SecurityUserHolder.getCurrentUser().getUserId());
				ur.setUnlockTime(DateUtil.getDateTime());
				
				ur.setLockInModuleCode(lockInfo.getLockInModuleCode());
				ur.setLockDeviceNo(lockInfo.getLockDeviceNo());
				ur.setLockCode(lockInfo.getLockCode());
				ur.setRecordCode(ResponseCommandUtil.REMOTE_UNLOCK_RECORD_CODE);
				ur.setRecordTpye(ResponseCommandUtil.getPassiveLockRecord(ur.getRecordCode()));
				
				Mess mess = new Mess();
				mess.setModuleCode(lockInfo.getLockInModuleCode());
				mess.setDeviceNo(lockInfo.getLockDeviceNo());
				
				String returnJson = HttpDeviceManageForRest.getInstance()
						.httpRequestForLockdm(mess.remoteUnlock());
				
				CommandInfo commandInfo = GsonUtil.fromJson(returnJson, CommandInfo.class);
				if(!ReturnCodeUtil.SUCCESS_RESULT_CODE.equalsIgnoreCase(commandInfo.getReturnCode())){
					returnCode = commandInfo.getReturnCode();
				}else{
					if(!ResponseCommandUtil.SUCCESS_COMMAND
							.equalsIgnoreCase(commandInfo.getReturnMessage().substring(8, 10))){
						returnCode = ErrorCode.SYS_UNLOCK_FAILED;
					}
				}
				ur.setMessage(commandInfo.getReturnMessage());
			}
		} catch(ErrorCodeException e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			resultMsg = e.getMessage();
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			resultMsg = ErrorCode.ERROR_SYS_EXCEPTION;
		} finally {
			if(StringUtil.isNotEmpty(resultMsg)) {
				simpleRespose.setResultCode(0001);
				simpleRespose.setResultMessage(resultMsg);
			}else if(StringUtil.isNotEmpty(returnCode)){
				resultMsg = ErrorCode.getRemoteOperationResults(returnCode);
				simpleRespose.setResultCode(0001);
				simpleRespose.setResultMessage(resultMsg);
			}else{
				returnCode = ErrorCode.SYS_UNLOCK_COMMAND;
				resultMsg = ErrorCode.getRemoteOperationResults(returnCode);
			}
			ur.setRemoteUnlockResults(returnCode);
			ur.setNote(resultMsg+"(平台远程开门)");
			ur.setUploadTime(new Date());
			
			unlockRecordsService.saveUnlockRecords(ur);
		}
		return SUCCESS;
	}
	
	/**
	 * 设置时间
	 */
	@Action(value="setLockTime")
	@ToJson(outField="simpleRespose")
	public String setLockTime(){
		String resultMsg = "";
		try{
			resultMsg = checkLockInfo();
			if(StringUtil.isEmpty(resultMsg)){
				Mess mess = new Mess();
				mess.setModuleCode(lockInfo.getLockInModuleCode());
				mess.setDeviceNo(lockInfo.getLockDeviceNo());
				
				String returnJson = HttpDeviceManageForRest.getInstance()
						.httpRequestForLockdm(mess.setLockTime());
				CommandInfo commandInfo = GsonUtil.fromJson(returnJson, CommandInfo.class);
				if(!ReturnCodeUtil.SUCCESS_RESULT_CODE.equalsIgnoreCase(commandInfo.getReturnCode())){
					resultMsg = ErrorCode.getRemoteOperationResults(commandInfo.getReturnCode());
				}else{
					if(!ResponseCommandUtil.SUCCESS_COMMAND
							.equalsIgnoreCase(commandInfo.getReturnMessage().substring(8, 10))){
						resultMsg = "设置时间失败！";
					}
				}
			}
		} catch(ErrorCodeException e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			resultMsg = e.getMessage();
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			resultMsg = ErrorCode.ERROR_SYS_EXCEPTION;
		} finally {
			if(StringUtil.isNotEmpty(resultMsg)){
				simpleRespose.setResultCode(0001);
				simpleRespose.setResultMessage(resultMsg);
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 获取门锁状态
	 */
	@Action(value="getLockState")
	@ToJson(outField="simpleRespose")
	public String getLockState(){
		int resultCode = 1;
		String resultMsg = "";
		try{
			resultMsg = checkLockInfo();
			if(StringUtil.isEmpty(resultMsg)){
				Mess mess = new Mess();
				mess.setModuleCode(lockInfo.getLockInModuleCode());
				mess.setDeviceNo(lockInfo.getLockDeviceNo());
				
				String returnJson = HttpDeviceManageForRest.getInstance()
						.httpRequestForLockdm(mess.getLockState());
				
				CommandInfo commandInfo = GsonUtil.fromJson(returnJson, CommandInfo.class);
				if(!ReturnCodeUtil.SUCCESS_RESULT_CODE.equalsIgnoreCase(commandInfo.getReturnCode())){
					resultMsg = ErrorCode.getRemoteOperationResults(commandInfo.getReturnCode());
				}else{
					resultMsg = ResponseCommandUtil.getHandLockStateInfo(commandInfo.getReturnMessage());
					resultCode = 0;
				}
			}
		} catch(ErrorCodeException e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			resultMsg = e.getMessage();
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			resultMsg = ErrorCode.ERROR_SYS_EXCEPTION;
		} finally {
			simpleRespose.setResultCode(resultCode);
			simpleRespose.setResultMessage(resultMsg);
		}
		return SUCCESS;
	}
	
	
	/**
	 * 到出锁资源信息
	 */
	@Action(value="exportLockInfo")
	@ToJson(outField="dataPackage")
	public String exportLockInfo(){
		int resultCode = 1;
		String resultMsg = "";
		try{
			this.devLockInfoService.exportLockInfo(request, response,dto);
		} catch(Exception e) {
			e.printStackTrace();
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return null;
	}
	
	public DevLockInfoDto getDto() {
		return dto;
	}

	public void setDto(DevLockInfoDto dto) {
		this.dto = dto;
	}

	public DevLockInfo getLockInfo() {
		return lockInfo;
	}

	public void setLockInfo(DevLockInfo lockInfo) {
		this.lockInfo = lockInfo;
	}

	public String getLockIds() {
		return lockIds;
	}

	public void setLockIds(String lockIds) {
		this.lockIds = lockIds;
	}

	public File getImportFile() {
		return importFile;
	}

	public void setImportFile(File importFile) {
		this.importFile = importFile;
	}
}
