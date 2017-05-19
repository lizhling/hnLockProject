package com.hnctdz.aiLock.action.device;


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
import com.hnctdz.aiLock.domain.system.SysRole;
import com.hnctdz.aiLock.domain.system.SysUser;
import com.hnctdz.aiLock.dto.device.DevKeyInfoDto;
import com.hnctdz.aiLock.dto.system.SysRoleDto;
import com.hnctdz.aiLock.security.SecurityUserHolder;
import com.hnctdz.aiLock.service.device.DevKeyInfoService;
import com.hnctdz.aiLock.service.system.SysRoleService;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName DevKeyInfoAction.java
 * @Author WangXiangBo (wangxiangbo127@163.com)
 */
@SuppressWarnings("serial")
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/DevKeyInfoAction")
@Controller
public class DevKeyInfoAction extends BaseAction{
	private static final Logger LOG = Logger.getLogger(DevKeyInfoAction.class);  
	
 	@Autowired
 	private DevKeyInfoService devKeyInfoService;
 	
 	@Autowired
	private SysRoleService sysRoleService;
 	
 	private DevKeyInfo keyInfo;
 	private DevKeyInfoDto dto;
 	private String keyIds;
 	
 	/**
	 * 查询钥匙资料列表
	 */
	@Action(value="findPageDevKeyInfo")
	@ToJson(outField="dataPackage")
	public String findPageDevKeyInfo(){
		dataPackage = getDataPackage();
		try {
			dataPackage = devKeyInfoService.findPageDevKeyInfo(dto, dataPackage);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 保存钥匙资料信息
	 */
	@Action(value="saveDevKeyInfo")
	@ToJson(outField="simpleRespose")
	public String saveDevKeyInfo(){
		boolean bl = true;
		try{
			DevKeyInfoDto dto = new DevKeyInfoDto();
			dto.setKeyCode(keyInfo.getKeyCode());
			List<DevKeyInfo> list = devKeyInfoService.findDevKeyInfoList(dto);
			if(list.size() > 0){
				if(keyInfo.getKeyId() == null || !list.get(0).getKeyId().equals(keyInfo.getKeyId())){
					bl = false;
				}
			}
			if(bl){
				devKeyInfoService.saveDevKeyInfo(keyInfo);
			}else{
				simpleRespose.setResultCode(0001);
				simpleRespose.setResultMessage("该钥匙编码已存在！");
			}
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 获取钥匙资料（下拉框使用）
	 */
	@Action(value="findKeyInfoOptions")
	@ToJson(outField="dataPackage")
	public String findKeyInfoOptions(){
		dataPackage = getDataPackage();
		try {
			List list = devKeyInfoService.findKeyInfoOptions(dto);
			dataPackage.setRows(list);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 删除钥匙资料信息
	 */
	@Action(value="deleteDevKeyInfo")
	@ToJson(outField="simpleRespose")
	public String deleteDevKeyInfo(){
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
				devKeyInfoService.deleteRealDevKeyInfoByIds(keyIds); //物理删除
			}else{
				devKeyInfoService.deleteDevKeyInfoByIds(keyIds); //逻辑删除
			}
			
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 钥匙解绑
	 */
	@Action(value="keyNnbundling")
	@ToJson(outField="dataPackage")
	public String keyNnbundling(){
		dataPackage = getDataPackage();
		try {
			DevKeyInfo key = devKeyInfoService.getById(dto.getKeyId());
			key.setPhoneImei(null);
			devKeyInfoService.saveDevKeyInfo(key);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	public DevKeyInfoDto getDto() {
		return dto;
	}

	public void setDto(DevKeyInfoDto dto) {
		this.dto = dto;
	}

	public DevKeyInfo getKeyInfo() {
		return keyInfo;
	}

	public void setKeyInfo(DevKeyInfo keyInfo) {
		this.keyInfo = keyInfo;
	}

	public String getKeyIds() {
		return keyIds;
	}

	public void setKeyIds(String keyIds) {
		this.keyIds = keyIds;
	}
}
