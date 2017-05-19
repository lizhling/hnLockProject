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
import com.hnctdz.aiLock.domain.system.AppMenuPermissions;
import com.hnctdz.aiLock.dto.system.AppMenuPermissionsDto;
import com.hnctdz.aiLock.service.system.AppMenuPermissionsService;
import com.hnctdz.aiLock.task.ScheduledTasks;
import com.hnctdz.aiLock.utils.AppMenuInfo;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName AppMenuPermissionsAction.java
 * @Author WangXiangBo (wangxiangbo127@163.com)
 */
@SuppressWarnings("serial")
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/AppMenuPermissionsAction")
@Controller
public class AppMenuPermissionsAction extends BaseAction{
	private static final Logger LOG = Logger.getLogger(AppMenuPermissionsAction.class);  
	
 	@Autowired
 	private AppMenuPermissionsService appMenuPermissionsService;
 	@Autowired
 	private ScheduledTasks scheduledTasks;
 	
 	private AppMenuPermissions appMenuPermissions;
 	private AppMenuPermissionsDto dto;
 	private String menuIds;
 	
	@Action(value="findPageAppMenuPermissions")
	@ToJson(outField="dataPackage")
	public String findPageAppMenuPermissions(){
		dataPackage = getDataPackage();
		try {
			dataPackage = appMenuPermissionsService.findPageAppMenuPermissions(dto, dataPackage);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	@Action(value="findAppMenuPermissionsOptions")
	@ToJson(outField="dataPackage")
	public String findAppMenuPermissionsOptions(){
		dataPackage = getDataPackage();
		try {
			List list = appMenuPermissionsService.findAppMenuPermissionsOptions(dto);
			dataPackage.setRows(list);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	@Action(value="saveAppMenuPermissions")
	@ToJson(outField="simpleRespose")
	public String saveAppMenuPermissions(){
		try{
			appMenuPermissionsService.saveAppMenuPermissions(appMenuPermissions);
			
			scheduledTasks.appMenuUpdateTask();
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	@Action(value="deleteAppMenuPermissions")
	@ToJson(outField="simpleRespose")
	public String deleteAppMenuPermissions(){
		try{
			appMenuPermissionsService.deleteAppMenuPermissionsByIds(menuIds);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}

	public AppMenuPermissionsDto getDto() {
		return dto;
	}

	public void setDto(AppMenuPermissionsDto dto) {
		this.dto = dto;
	}

	public AppMenuPermissions getAppMenuPermissions() {
		return appMenuPermissions;
	}

	public void setAppMenuPermissions(AppMenuPermissions appMenuPermissions) {
		this.appMenuPermissions = appMenuPermissions;
	}

	public String getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}
}
