package com.hnctdz.aiLock.action.system;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.nutz.json.annotations.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hnctdz.aiLock.action.BaseAction;
import com.hnctdz.aiLock.domain.system.SysRole;
import com.hnctdz.aiLock.domain.system.SysRoleRes;
import com.hnctdz.aiLock.dto.system.SysRoleDto;
import com.hnctdz.aiLock.service.system.SysRoleService;
import com.hnctdz.aiLock.service.system.SysRoleResService;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName SysRoleAction.java
 * @Author WangXiangBo (wangxiangbo127@163.com)
 */
@SuppressWarnings("serial")
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/SysRoleAction")
@Controller
public class SysRoleAction extends BaseAction{
	private static final Logger LOG = Logger.getLogger(SysRoleAction.class);  
	
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleResService sysRoleResService;
	
	private SysRole sysRole;
	private String roleIds;
	
	private SysRoleDto dto;
	
	
	/**
	 * 查询角色列表
	 */
	@Action(value="findPageSysRole")
	@ToJson(outField="dataPackage")
	public String findPageSysRole(){
		dataPackage = getDataPackage();
		try {
			dataPackage = sysRoleService.findPageSysRole(dto, dataPackage);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 保存功能菜单信息
	 */
	@Action(value="saveSysRole")
	@ToJson(outField="simpleRespose")
	public String saveSysRole(){
		try{
			sysRoleService.saveSysRole(sysRole);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 删除功能菜单信息
	 */
	@Action(value="deleteSysRole")
	@ToJson(outField="simpleRespose")
	public String deleteSysRole(){
		String result = "";
		try{
			result = sysRoleService.deleteSysRoleByIds(roleIds);
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
	 * 查询角色拥有的资源列表
	 */
	@Action(value="findSysResByRole")
	@ToJson(outField="dataPackage")
	public String findSysResByRole(){
		dataPackage = getDataPackage();
		try {
			List<SysRoleRes> list = sysRoleResService.findSysResByRole(dto.getRoleId());
			dataPackage.setRows(list);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 保存/修改角色拥有的系统资源
	 */
	@Action(value="saveSysRoleUseRes")
	@ToJson(outField="simpleRespose")
	public String saveSysRoleUseRes(){
		String result = "";
		try{
			sysRoleResService.saveSysRoleRes(dto.getRoleId(), dto.getResIds());
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
	 * 查询满足条件的角色List
	 */
	@Action(value="findSysRole")
	@ToJson(outField="dataPackage")
	public String findSysRole(){
		dataPackage = getDataPackage();
		try {
			List<SysRole> list = sysRoleService.findSysRole(dto);
			dataPackage.setRows(list);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}


	public SysRoleDto getDto() {
		return dto;
	}


	public void setDto(SysRoleDto dto) {
		this.dto = dto;
	}


	public SysRole getSysRole() {
		return sysRole;
	}


	public void setSysRole(SysRole sysRole) {
		this.sysRole = sysRole;
	}


	public String getRoleIds() {
		return roleIds;
	}


	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
}
