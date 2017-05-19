package com.hnctdz.aiLock.action.system;

import java.io.File;
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
import com.hnctdz.aiLock.domain.system.SysUser;
import com.hnctdz.aiLock.dto.system.SysUserDto;
import com.hnctdz.aiLock.security.SecurityUserHolder;
import com.hnctdz.aiLock.service.info.PersonnelInfoService;
import com.hnctdz.aiLock.service.system.SysUserRoleService;
import com.hnctdz.aiLock.service.system.SysUserService;
import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.hnctdz.aiLock.utils.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName SysUserAction.java
 * @Author WangXiangBo (wangxiangbo127@163.com)
 */
@SuppressWarnings("serial")
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/SysUserAction")
@Controller
public class SysUserAction extends BaseAction{
	private static final Logger LOG = Logger.getLogger(SysUserAction.class);  
	
 	@Autowired
 	private SysUserService sysUserService;
 	@Autowired
 	private SysUserRoleService sysUserRoleService;
 	@Autowired
 	private PersonnelInfoService personnelInfoService;
 	
 	private SysUser sysUser;
 	private SysUserDto dto;
 	private String userIds;
 	
 	private File importFile;//文件上传
 	
 	/**
	 * 查询系统用户列表
	 */
	@Action(value="findPageSysUser")
	@ToJson(outField="dataPackage")
	public String findPageSysUser(){
		dataPackage = getDataPackage();
		try {
			dataPackage = sysUserService.findPageSysUser(dto, dataPackage);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 保存系统用户信息
	 */
	@Action(value="saveSysUser")
	@ToJson(outField="simpleRespose")
	public String saveSysUser(){
		boolean bl = true;
		try{
			List list = personnelInfoService.findAccountBySysUserAndPer(sysUser.getUserName());
			if(list.size() > 0){
				Object[] obj = (Object[])list.get(0);
				if("2".equalsIgnoreCase(obj[0].toString())){
					bl = false;
				}
				if(sysUser.getUserId() == null || !obj[1].toString().equals(sysUser.getUserId().toString())){
					bl = false;
				}
			}
			if(bl){
				if(sysUser.getUserId() == null){
					sysUser.setPassword(Constants.PER_DEFAULT_PASSW);
				}
				sysUserService.saveSysRes(sysUser);
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
	 * 修改系统用户密码
	 */
	@Action(value="updateSysUserPasw")
	@ToJson(outField="simpleRespose")
	public String updateSysUserPasw(){
		try{
			if(StringUtil.isNotBlank(dto.getOldPassword()) && StringUtil.isNotBlank(dto.getPassword())){
				SysUser sysUser = sysUserService.getSysUserById(SecurityUserHolder.getCurrentUser().getUserId());
				if(sysUser.getPassword().equalsIgnoreCase(dto.getOldPassword())){
					sysUser.setPassword(dto.getPassword());
					sysUserService.saveSysRes(sysUser);
				}else{
					simpleRespose.setResultCode(0001);
					simpleRespose.setResultMessage("当前密码错误！");
				}
			}else{
				simpleRespose.setResultCode(0001);
				simpleRespose.setResultMessage("请先输入当前、新密码再修改！");
			}
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 重置人员密码
	 */
	@Action(value="resetPassword")
	@ToJson(outField="simpleRespose")
	public String resetPassword(){
		try{
			SysUser sysUser = sysUserService.getSysUserById(dto.getUserId());
			sysUser.setPassword(Constants.PER_DEFAULT_PASSW);
			sysUserService.saveSysRes(sysUser);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 删除系统用户信息
	 */
	@Action(value="deleteSysUser")
	@ToJson(outField="simpleRespose")
	public String deleteSysUser(){
		try{
			sysUserService.deleteSysUserByIds(userIds);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 保存/修改用户拥有的角色
	 */
	@Action(value="saveSysUserInRole")
	@ToJson(outField="simpleRespose")
	public String saveSysUserInRole(){
		String result = "";
		try{
			sysUserRoleService.saveSysUserRole(dto.getUserId(), dto.getRoleIds());
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			result = ErrorCode.ERROR_SYS_EXCEPTION;
		} finally {
			if(!"".equalsIgnoreCase(result)){
				simpleRespose.setResultCode(0001);
			}
			simpleRespose.setResultMessage(result);
		}
		return SUCCESS;
	}
	
	/**
	 * 获取用户List（下拉框使用数据）
	 */
	@Action(value="findSysUserOptions")
	@ToJson(outField="simpleRespose")
	public String findSysUserOptions(){
		dataPackage = getDataPackage();
		try {
			List list = sysUserService.findSysUserOptions(dto);
			dataPackage.setRows(list);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 导入织织节点
	 * @return
	 */
	@Action(value="importOrgInfo")
	@ToJson(outField="simpleRespose")
	public String importOrgInfo(){
		String result = "";
		try{
				result = sysUserRoleService.importOrgInfos(importFile);
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
	 * 导出组织节点
	 */
	@Action(value="exportOrgInfo")
	@ToJson(outField="dataPackage")
	public String exportOrgInfo(){
		String result = "";
		try{
			sysUserRoleService.exportOrgInfo(request, response);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			result = ErrorCode.ERROR_SYS_EXCEPTION;
		}
		return null;
	}
	

	public SysUser getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public SysUserDto getDto() {
		return dto;
	}

	public void setDto(SysUserDto dto) {
		this.dto = dto;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	public File getImportFile() {
		return importFile;
	}

	public void setImportFile(File importFile) {
		this.importFile = importFile;
	}

}
