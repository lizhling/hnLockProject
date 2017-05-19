package com.hnctdz.aiLock.action.analysis;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.nutz.json.annotations.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hnctdz.aiLock.action.BaseAction;
import com.hnctdz.aiLock.domain.analysis.UnlockRecords;
import com.hnctdz.aiLock.dto.analysis.UnlockRecordsDto;
import com.hnctdz.aiLock.security.SecurityUserHolder;
import com.hnctdz.aiLock.service.analysis.UnlockRecordsService;
import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.hnctdz.aiLock.utils.ExcelUtil;
import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName UnlockRecordsAction.java
 * @Author WangXiangBo (wangxiangbo127@163.com)
 */
@SuppressWarnings("serial")
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/UnlockRecordsAction")
@Controller
public class UnlockRecordsAction extends BaseAction{
	private static final Logger LOG = Logger.getLogger(UnlockRecordsAction.class);  
	
 	@Autowired
 	private UnlockRecordsService unlockRecordsService;
 	
 	private UnlockRecordsDto dto;
 	private String recordIds;
 	
 	/**
	 * 查询门锁记录列表
	 */
	@Action(value="findPageUnlockRecords")
	@ToJson(outField="dataPackage")
	public String findPageUnlockRecords(){
		dataPackage = getDataPackage();
		try {
			dataPackage = unlockRecordsService.findPageUnlockRecords(dto, dataPackage);
			
			if(dto != null && dto.getQueryType() == 1L){
				if(dto.getSelectType() == Constants.SELECT_ALARM){
					List<UnlockRecords> list = (List<UnlockRecords>)dataPackage.getRows();
					String[] columName = {"areaName", "alarmLevel", "lockCode", "lockName", "recordTpye", 
							"unlockTime", "uploadTime", "orgName", "userName", "confirmTime", "note"};
					ExcelUtil.getInstance().ExcelTemplateExport(request, response, list, "/resources/moulddownload/告警记录.xls", 1, columName);
				}else{
					List<UnlockRecords> list = (List<UnlockRecords>)dataPackage.getRows();
					String[] columName = {"recordTpye", "areaName", "lockCode", "lockName", "keyCode", 
							"keyTypeName", "unlockPerName", "unlockPerPhone", "orgName", "unlockTime", "uploadTime", "note"};
					ExcelUtil.getInstance().ExcelTemplateExport(request, response, list, "/resources/moulddownload/门锁记录.xls", 1, columName);
				}
				return null;
			}
		} catch(Exception e) {
			e.printStackTrace();
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 查询门锁最新5条记录List(首页用)
	 */
	@Action(value="findUnlockRecordsList")
	@ToJson(outField="dataPackage")
	public String findUnlockRecordsList(){
		dataPackage = getDataPackage();
		try {
//			dto = new UnlockRecordsDto();
//			dto.setUserId(userId)
			List<UnlockRecords> urList = new ArrayList<UnlockRecords>();
			List<UnlockRecords> list = unlockRecordsService.findUnlockRecordsList(dto);
			int size = list.size();
			if(size > 5){
				for(int i= 0; i < 5 ; i++){
					urList.add(list.get(i));
				}
			}else{
				urList = list;
			}
			dataPackage.setRows(urList);
			dataPackage.setResultMessage(list.size()+"");
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 告警处理确认
	 */
	@Action(value="alarmConfirm")
	@ToJson(outField="simpleRespose")
	public String alarmConfirm(){
		try{
			dto.setUserId(SecurityUserHolder.getCurrentUser().getUserId());
			unlockRecordsService.updateAlarmConfirm(dto);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 查询门锁按组织统计List
	 */
	@Action(value="findOrgStLockRecords")
	@ToJson(outField="dataPackage")
	public String findOrgStLockRecords(){
		dataPackage = getDataPackage();
		try {
			if(dto == null){
				dto = new UnlockRecordsDto();
			}
			dto.setOrgId(SecurityUserHolder.getCurrentUser().getOrgId());
			dataPackage.setRows(unlockRecordsService.findOrgStLockRecords(dto));
		} catch(Exception e) {
			LOG.error("oprate orgId:" + this.dto.getOrgId() + "  and errorInfo:" + ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	
	public UnlockRecordsDto getDto() {
		return dto;
	}

	public void setDto(UnlockRecordsDto dto) {
		this.dto = dto;
	}

	public String getRecordIds() {
		return recordIds;
	}

	public void setRecordIds(String recordIds) {
		this.recordIds = recordIds;
	}
}
