package com.hnctdz.aiLock.action.device;


import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.nutz.json.annotations.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hnctdz.aiLock.action.BaseAction;
import com.hnctdz.aiLock.domain.device.DevGroup;
import com.hnctdz.aiLock.dto.device.DevGroupDto;
import com.hnctdz.aiLock.service.device.DevGroupService;
import com.hnctdz.aiLock.service.device.DevLockInGroupService;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName DevGroupAction.java
 * @Author WangXiangBo (wangxiangbo127@163.com)
 */
@SuppressWarnings("serial")
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/DevGroupAction")
@Controller
public class DevGroupAction extends BaseAction{
	private static final Logger LOG = Logger.getLogger(DevGroupAction.class);  
	
 	@Autowired
 	private DevGroupService devGroupService;
 	@Autowired
 	private DevLockInGroupService devLockInGroupService;
 	
 	private DevGroup devGroup;
 	private DevGroupDto dto;
 	private String groupIds;
 	
 	/**
	 * 查询分组列表
	 */
	@Action(value="findPageDevGroup")
	@ToJson(outField="dataPackage")
	public String findPageDevGroup(){
		dataPackage = getDataPackage();
		try {
			dataPackage = devGroupService.findPageDevGroup(dto, dataPackage);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 查询分组List列表
	 */
	@Action(value="findDevGroupList")
	@ToJson(outField="dataPackage")
	public String findDevGroupList(){
		dataPackage = getDataPackage();
		try {
			dataPackage.setRows(devGroupService.findDevGroupList(dto));
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 查询分组中门锁列表
	 */
	@Action(value="findGroupLock")
	@ToJson(outField="dataPackage")
	public String findGroupLock(){
		dataPackage = getDataPackage();
		try {
			dataPackage = devGroupService.findGroupLock(dto, dataPackage);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 查询分组中门锁信息(关联默认开锁人员)List列表
	 */
	@Action(value="findLockByGroupIdsList")
	@ToJson(outField="dataPackage")
	public String findLockByGroupIdsList(){
		dataPackage = getDataPackage();
		try {
			dataPackage.setRows(devGroupService.findLockByGroupIdsList(dto));
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 保存分组信息
	 */
	@Action(value="saveDevGroup")
	@ToJson(outField="simpleRespose")
	public String saveDevGroup(){
		try{
			devGroupService.saveDevGroup(devGroup);
			
			devLockInGroupService.saveDevLockInGroup(devGroup.getGroupId(), devGroup.getLockIds());
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 删除分组信息
	 */
	@Action(value="deleteDevGroup")
	@ToJson(outField="simpleRespose")
	public String deleteDevGroup(){
		try{
			devGroupService.deleteDevGroupByIds(groupIds);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	public DevGroupDto getDto() {
		return dto;
	}

	public void setDto(DevGroupDto dto) {
		this.dto = dto;
	}

	public DevGroup getDevGroup() {
		return devGroup;
	}

	public void setDevGroup(DevGroup devGroup) {
		this.devGroup = devGroup;
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}
}
