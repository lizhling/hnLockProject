package com.hnctdz.aiLock.action.system;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.nutz.json.annotations.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hnctdz.aiLock.action.BaseAction;
import com.hnctdz.aiLock.domain.system.SysNotice;
import com.hnctdz.aiLock.dto.system.SysNoticeDto;
import com.hnctdz.aiLock.security.SecurityUserHolder;
import com.hnctdz.aiLock.service.system.SysNoticeService;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName SysNoticeAction.java
 * @Author WangXiangBo (wangxiangbo127@163.com)
 */
@SuppressWarnings("serial")
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/SysNoticeAction")
@Controller
public class SysNoticeAction extends BaseAction{
	private static final Logger LOG = Logger.getLogger(SysNoticeAction.class);  
	
 	@Autowired
 	private SysNoticeService sysNoticeService;
 	
 	private SysNotice sysNotice;
 	private SysNoticeDto dto;
 	private String noticeIds;
 	
	@Action(value="findPageSysNotice")
	@ToJson(outField="dataPackage")
	public String findPageSysNotice(){
		dataPackage = getDataPackage();
		try {
			dataPackage = sysNoticeService.findPageSysNotice(dto, dataPackage);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 查询门锁最新5条记录List(首页用)
	 */
	@Action(value="findSysNoticeList")
	@ToJson(outField="dataPackage")
	public String findSysNoticeList(){
		dataPackage = getDataPackage();
		try {
			List<SysNotice> snList = new ArrayList<SysNotice>();
			List<SysNotice> list = sysNoticeService.findSysNoticeList(dto);
			int size = list.size();
			if(size > 5){
				for(int i= 0; i < 5 ; i++){
					snList.add(list.get(i));
				}
			}else{
				snList = list;
			}
			dataPackage.setRows(snList);
			dataPackage.setResultMessage(list.size()+"");
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	@Action(value="saveSysNotice")
	@ToJson(outField="simpleRespose")
	public String saveSysNotice(){
		try{
			sysNotice.setUserId(SecurityUserHolder.getCurrentUser().getUserId());
			sysNotice.setReleaseTime(new Date());
			sysNoticeService.saveSysNotice(sysNotice);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	@Action(value="deleteSysNotice")
	@ToJson(outField="simpleRespose")
	public String deleteSysNotice(){
		try{
			sysNoticeService.deleteSysNoticeByIds(noticeIds);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}

	public SysNoticeDto getDto() {
		return dto;
	}

	public void setDto(SysNoticeDto dto) {
		this.dto = dto;
	}

	public SysNotice getSysNotice() {
		return sysNotice;
	}

	public void setSysNotice(SysNotice sysNotice) {
		this.sysNotice = sysNotice;
	}

	public String getNoticeIds() {
		return noticeIds;
	}

	public void setNoticeIds(String noticeIds) {
		this.noticeIds = noticeIds;
	}
}
