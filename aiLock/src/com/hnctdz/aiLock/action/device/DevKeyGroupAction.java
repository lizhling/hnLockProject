package com.hnctdz.aiLock.action.device;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.nutz.json.annotations.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hnctdz.aiLock.action.BaseAction;
import com.hnctdz.aiLock.domain.device.DevKeyGroup;
import com.hnctdz.aiLock.dto.device.DevKeyGroupDto;
import com.hnctdz.aiLock.service.device.DevKeyGroupService;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName DevKeyGroupAction.java
 * @Author WangXiangBo (wangxiangbo127@163.com)
 */
@SuppressWarnings("serial")
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/DevKeyGroupAction")
@Controller
public class DevKeyGroupAction extends BaseAction{
	private static final Logger LOG = Logger.getLogger(DevKeyGroupAction.class);  
	
 	@Autowired
 	private DevKeyGroupService devKeyGroupService;
 	
 	private DevKeyGroup devKeyGroup;
 	private DevKeyGroupDto dto;
 	private String groupIds;
 	
 	/**
	 * 查询分组列表
	 */
	@Action(value="findPageDevKeyGroup")
	@ToJson(outField="dataPackage")
	public String findPageDevKeyGroup(){
		dataPackage = getDataPackage();
		try {
			dataPackage = devKeyGroupService.findPageDevKeyGroup(dto, dataPackage);
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
	@Action(value="findDevKeyGroupList")
	@ToJson(outField="dataPackage")
	public String findDevKeyGroupList(){
		dataPackage = getDataPackage();
		try {
			dataPackage.setRows(devKeyGroupService.findDevKeyGroupList(dto));
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 查询钥匙分组List（下拉框使用）
	 */
	@Action(value="findKeyGroupOptions")
	@ToJson(outField="dataPackage")
	public String findGroupLock(){
		dataPackage = getDataPackage();
		try {
			List list = devKeyGroupService.findKeyGroupOptions(dto);
			dataPackage.setRows(list);
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
	@Action(value="saveDevKeyGroup")
	@ToJson(outField="simpleRespose")
	public String saveDevKeyGroup(){
		try{
			devKeyGroupService.saveDevKeyGroup(devKeyGroup);
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
	@Action(value="deleteDevKeyGroup")
	@ToJson(outField="simpleRespose")
	public String deleteDevKeyGroup(){
		try{
			devKeyGroupService.deleteDevKeyGroupByIds(groupIds);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	public DevKeyGroupDto getDto() {
		return dto;
	}

	public void setDto(DevKeyGroupDto dto) {
		this.dto = dto;
	}

	public DevKeyGroup getDevKeyGroup() {
		return devKeyGroup;
	}

	public void setDevKeyGroup(DevKeyGroup devKeyGroup) {
		this.devKeyGroup = devKeyGroup;
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}
}
