package com.hnctdz.aiLock.action.info;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.nutz.json.annotations.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hnctdz.aiLock.action.BaseAction;
import com.hnctdz.aiLock.domain.device.DevKeyInfo;
import com.hnctdz.aiLock.domain.device.DevLockInfo;
import com.hnctdz.aiLock.domain.device.LockKeyAuthorize;
import com.hnctdz.aiLock.domain.info.PersonnelInfo;
import com.hnctdz.aiLock.domain.system.SysUser;
import com.hnctdz.aiLock.dto.device.DevKeyInfoDto;
import com.hnctdz.aiLock.dto.device.LockKeyAuthorizeDto;
import com.hnctdz.aiLock.dto.info.PersonnelInfoDto;
import com.hnctdz.aiLock.dto.system.SysRoleDto;
import com.hnctdz.aiLock.security.SecurityUserHolder;
import com.hnctdz.aiLock.service.device.DevKeyInfoService;
import com.hnctdz.aiLock.service.device.DevLockInfoService;
import com.hnctdz.aiLock.service.device.LockKeyAuthorizeService;
import com.hnctdz.aiLock.service.info.PersonnelInfoService;
import com.hnctdz.aiLock.service.system.SysRoleService;
import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.hnctdz.aiLock.utils.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName PersonnelInfoAction.java
 * @Author WangXiangBo (wangxiangbo127@163.com)
 */
@SuppressWarnings("serial")
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/PersonnelInfoAction")
@Controller
public class PersonnelInfoAction extends BaseAction{
	private static final Logger LOG = Logger.getLogger(PersonnelInfoAction.class);  
	
 	@Autowired
 	private PersonnelInfoService personnelInfoService;
 	@Autowired
 	private LockKeyAuthorizeService lockKeyAuthorizeService;
 	@Autowired
 	private DevKeyInfoService devKeyInfoService;
 	@Autowired
 	private DevLockInfoService devLockInfoService;
 	
 	@Autowired
	private SysRoleService sysRoleService;
 	
 	private PersonnelInfo perInfo;
 	private PersonnelInfoDto dto;
 	private String perIds;
 	
 	private File importFile;//文件上传
//	private String importFileName; //文件名称
 	
 	/**
	 * 查询人员资料列表
	 */
	@Action(value="findPagePersonnelInfo")
	@ToJson(outField="dataPackage")
	public String findPagePersonnelInfo(){
		dataPackage = getDataPackage();
		try {
			dataPackage = personnelInfoService.findPagePersonnelInfo(dto, dataPackage);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 保存人员资料信息
	 */
	@Action(value="savePersonnelInfo")
	@ToJson(outField="simpleRespose")
	public String savePersonnelInfo(){
		try{
			if(personnelInfoService.checkPerExist(perInfo)){
				if(perInfo.getPerId() == null){
					perInfo.setPerPassword(Constants.PER_DEFAULT_PASSW);
				}
				if(StringUtil.isNotEmpty(perInfo.getPerPassword()) && StringUtil.isEmpty(perInfo.getSmartKeyPerId())){
					perInfo.setSmartKeyPerId(personnelInfoService.getSmartKeyPerId());
				}
				
				boolean delPerAuth = true;
				if(!perInfo.getStatus().equals(1L)){
					
					//此处理需一个逻辑处理，如果是禁用
					if(!perInfo.getStatus().equals(2L)){
						//执行禁用操作 ,不删除授权信息
					//查询人员所持有的钥匙
					DevKeyInfoDto dto = new DevKeyInfoDto();
					dto.setPerId(perInfo.getPerId());
					List<DevKeyInfo> keyList = devKeyInfoService.findDevKeyInfoList(dto);
					//查询人员所拥有的有效权限
					List<LockKeyAuthorize> authorizeList = lockKeyAuthorizeService.findLockKeyAuthorizeByPerId(perInfo.getPerId());
					for(LockKeyAuthorize lka : authorizeList){
						DevLockInfo lock = devLockInfoService.getById(lka.getLockId());
						boolean delAuthBl = true;
						if(Constants.ELECTRIC_LOCK.equals(lock.getLockType()) && lock.getWheCanMatchCard() == 1){
							try{
								//删除有源门锁中的钥匙开门权限
								delAuthBl = lockKeyAuthorizeService.deleteCardPer(lka, keyList);
							}catch(Exception e) {
								delAuthBl = false;
							}
							if(!delAuthBl){
								delPerAuth = false;
							}
						}
						if(delAuthBl){
							LockKeyAuthorizeDto aDto = new LockKeyAuthorizeDto();
							aDto.setAuthorizeId(lka.getAuthorizeId());
							lockKeyAuthorizeService.deleteAuthorize(aDto);
//							lockKeyAuthorizeService.deleteLockKeyAuthorizeByIds(lka.getAuthorizeId().toString());
						}
					}
				}
				}
				if(delPerAuth){
					perInfo.setCuTime(new Date());
					PersonnelInfo per = personnelInfoService.savePersonnelInfo(perInfo);
					simpleRespose.setResultMessage(per.getPerId().toString());
				}else{
					simpleRespose.setResultCode(0001);
					simpleRespose.setResultMessage("未完全清除人员门锁权限，人员信息保存失败！");
				}
			}else{
				simpleRespose.setResultCode(0001);
				simpleRespose.setResultMessage("该账号已存在，请重新输入！");
			}
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 导入人员
	 */
	@Action(value="importPersonnelInfos")
	@ToJson(outField="simpleRespose")
	public String importPersonnelInfos() {
		String result = "";
		try{
			System.out.println(dto.getImportType());
			if(dto.getImportType().equals("1")){ //普通导入
				result = personnelInfoService.saveImportPersonnelInfos(importFile);
			}else if(dto.getImportType().equals("2")){//系统级导入
				
				SysUser sysUser = SecurityUserHolder.getCurrentUser();
				List<Object> sr = this.sysRoleService.findSysRoleByUserid(sysUser.getUserId());
				Object [] obj =  (Object[]) sr.get(0);
				for (Iterator iterator = sr.iterator(); iterator.hasNext();) {
					Object[] object = (Object[]) iterator.next();
					System.out.println(object[1].toString());
					if(object[1].toString().contains("超级管理员")){
						personnelInfoService.importPersonnelInfos(importFile);
						break;
					}
				}
				
			}
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
	 * 重置人员密码
	 */
	@Action(value="resetPerPassword")
	@ToJson(outField="simpleRespose")
	public String resetPerPassword(){
		try{
			personnelInfoService.updateResetPerPassword(dto.getPerId(), Constants.PER_DEFAULT_PASSW);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	
	/**
	 * 删除人员资料信息
	 */
	@Action(value="deletePersonnelInfo")
	@ToJson(outField="simpleRespose")
	public String deletePersonnelInfo(){
		String delResult = null;
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
			
			String[] perId = perIds.split(",");
			for(int i=0; i < perId.length; i++){
				PersonnelInfo perInfo = personnelInfoService.getPersonnelInfoById(Long.parseLong(perId[i]));
				
				boolean delPerAuth = true;
				if(!perInfo.getStatus().equals(1L)){
					//查询人员所持有的钥匙
					DevKeyInfoDto dto = new DevKeyInfoDto();
					dto.setPerId(perInfo.getPerId());
					List<DevKeyInfo> keyList = devKeyInfoService.findDevKeyInfoList(dto);
					//查询人员所拥有的有效权限
					List<LockKeyAuthorize> authorizeList = lockKeyAuthorizeService.findLockKeyAuthorizeByPerId(perInfo.getPerId());
					for(LockKeyAuthorize lka : authorizeList){
						DevLockInfo lock = devLockInfoService.getById(lka.getLockId());
						boolean delAuthBl = true;
						if(Constants.ELECTRIC_LOCK.equals(lock.getLockType()) && lock.getWheCanMatchCard() == 1){
							try{
								//删除有源门锁中的钥匙开门权限
								delAuthBl = lockKeyAuthorizeService.deleteCardPer(lka, keyList);
							}catch(Exception e) {
								delAuthBl = false;
							}
							if(!delAuthBl){
								delPerAuth = false;
							}
						}
						if(delAuthBl){
							LockKeyAuthorizeDto aDto = new LockKeyAuthorizeDto();
							aDto.setAuthorizeId(lka.getAuthorizeId());
							lockKeyAuthorizeService.deleteAuthorize(aDto);
						}
					}
				}
				if(delPerAuth){
					if(isRealDele){
						this.personnelInfoService.deleteRealPersonnelInfoByIds(perInfo.getPerId().toString());
					}else{
						perInfo.setStatus(3L);
						personnelInfoService.savePersonnelInfo(perInfo);
					}
				}else{
					delResult += perInfo.getPerName() + "、";
				}
			}
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}finally{
			if(StringUtil.isNotEmpty(delResult)){
				simpleRespose.setResultCode(0001);
				simpleRespose.setResultMessage("删除人员中："+delResult+"未完全清除各门锁权限，删除失败，其余删除成功！");
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 获取人员资料List（下拉框使用数据）
	 */
	@Action(value="findPersonnelInfoOptions")
	@ToJson(outField="dataPackage")
	public String findPersonnelInfoOptions(){
		dataPackage = getDataPackage();
		try {
			List list = personnelInfoService.findPersonnelInfoOptions(dto);
			dataPackage.setRows(list);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	
	@Action(value="exportPersonInfo")
	@ToJson(outField="simpleRespose")
	public String exportPersonInfo(){
		simpleRespose.setResultCode(0);
		simpleRespose.setResultMessage("导出成功!");
		try{
			this.personnelInfoService.exportPersonInfo(request, response,dto);
		} catch(Exception e) {
			e.printStackTrace();
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return null; //如果这儿不设置为NULL   会报异常
	}
	
	public PersonnelInfoDto getDto() {
		return dto;
	}

	public void setDto(PersonnelInfoDto dto) {
		this.dto = dto;
	}

	public PersonnelInfo getPerInfo() {
		return perInfo;
	}

	public void setPerInfo(PersonnelInfo perInfo) {
		this.perInfo = perInfo;
	}

	public String getPerIds() {
		return perIds;
	}

	public void setPerIds(String perIds) {
		this.perIds = perIds;
	}

	public File getImportFile() {
		return importFile;
	}

	public void setImportFile(File importFile) {
		this.importFile = importFile;
	}
}
