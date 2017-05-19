package com.hnctdz.aiLock.action.analysis;


import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.nutz.json.annotations.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hnctdz.aiLock.action.BaseAction;
import com.hnctdz.aiLock.domain.analysis.LockStatusRecords;
import com.hnctdz.aiLock.domain.analysis.UnlockRecords;
import com.hnctdz.aiLock.domain.system.SysBasicData;
import com.hnctdz.aiLock.dto.analysis.LockStatusRecordsDto;
import com.hnctdz.aiLock.dto.system.SysBasicDataDto;
import com.hnctdz.aiLock.service.analysis.LockStatusRecordsService;
import com.hnctdz.aiLock.service.system.SysBasicDataService;
import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.hnctdz.aiLock.utils.ExcelUtil;
import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName LockStatusRecordsAction.java
 * @Author WangXiangBo (wangxiangbo127@163.com)
 */
@SuppressWarnings("serial")
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/LockStatusRecordsAction")
@Controller
public class LockStatusRecordsAction extends BaseAction{
	private static final Logger LOG = Logger.getLogger(LockStatusRecordsAction.class);  
	
 	@Autowired
 	private LockStatusRecordsService lockStatusRecordsService;
 	@Autowired
 	private SysBasicDataService sysBasicDataService;
 	private LockStatusRecords keyInfo;
 	private LockStatusRecordsDto dto;
 	private String keyCodes;
 	
 	/**
	 * 查询门锁状态记录列表
	 */
	@Action(value="findPageLockStatusRecords")
	@ToJson(outField="dataPackage")
	public String findPageLockStatusRecords(){
		dataPackage = getDataPackage();
		try {
			dataPackage = lockStatusRecordsService.findPageLockStatusRecords(dto, dataPackage);
			List<LockStatusRecords> list = (List<LockStatusRecords>)dataPackage.getRows();
			
			SysBasicDataDto sbdDto = new SysBasicDataDto();
			sbdDto.setTypeTag("LOCK_STATUS");
			List<SysBasicData> sbdList = sysBasicDataService.findSysBasicDataList(sbdDto);
			String menguanhaoStatus = "";
			for(LockStatusRecords unlr : list){
				if("0".equalsIgnoreCase(unlr.getMenciStatus()) && "0".equalsIgnoreCase(unlr.getXiesheStatus())){
					menguanhaoStatus = "0";
				}else if("1".equalsIgnoreCase(unlr.getMenciStatus()) && "1".equalsIgnoreCase(unlr.getXiesheStatus())){
					menguanhaoStatus = "1";
				}else{
					menguanhaoStatus = "2";
				}
				
				unlr.setMenciStatus(getLockStatus(sbdList, "07", unlr.getMenciStatus()));
				unlr.setBufangStatus(getLockStatus(sbdList, "06", unlr.getBufangStatus()));
				unlr.setBaojingStatus(getLockStatus(sbdList, "05", unlr.getBaojingStatus()));
				unlr.setJixieyaoshiStatus(getLockStatus(sbdList, "03", unlr.getJixieyaoshiStatus()));
				unlr.setXiesheStatus(getLockStatus(sbdList, "02", unlr.getXiesheStatus()));
				unlr.setShangshuoStatus(getLockStatus(sbdList, "01", unlr.getShangshuoStatus()));
				//unlr.setMenguanhaoStatus(getLockStatus(sbdList, "00", unlr.getMenguanhaoStatus()));
				unlr.setMenguanhaoStatus(getLockStatus(sbdList, "08", menguanhaoStatus));
			}
			
			if(dto != null && dto.getQueryType() == 1L){
				List<LockStatusRecords> eList = (List<LockStatusRecords>)dataPackage.getRows();
				String[] columName = {"areaName", "lockCode", "lockName", "xiesheStatus", 
						"menciStatus", "menguanhaoStatus", "orgName", "reportTime"};
				ExcelUtil.getInstance().ExcelTemplateExport(request, response, eList, "/resources/moulddownload/门锁状态记录.xls", 1, columName);
				
				return null;
			}
			
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	public String getLockStatus(List<SysBasicData> sbdList, String statusTypeCode, String valueCode){
		for(SysBasicData sbd : sbdList){
			if(statusTypeCode.equalsIgnoreCase(sbd.getTypeCode())){
				for(SysBasicData sbdValue : sbdList){
					if(sbd.getBasicDataId().equals(sbdValue.getParentId()) && 
							sbdValue.getTypeCode().equalsIgnoreCase(valueCode)){
						return sbdValue.getTypeName();
					}
				}
				return valueCode;
			}
		}
		return valueCode;
	}
	
	
	public LockStatusRecordsDto getDto() {
		return dto;
	}

	public void setDto(LockStatusRecordsDto dto) {
		this.dto = dto;
	}

	public LockStatusRecords getKeyInfo() {
		return keyInfo;
	}

	public void setKeyInfo(LockStatusRecords keyInfo) {
		this.keyInfo = keyInfo;
	}

	public String getKeyCodes() {
		return keyCodes;
	}

	public void setKeyCodes(String keyCodes) {
		this.keyCodes = keyCodes;
	}
}
