package com.hnctdz.aiLock.action.system;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.nutz.json.annotations.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hnctdz.aiLock.action.BaseAction;
import com.hnctdz.aiLock.domain.system.SysVersion;
import com.hnctdz.aiLock.dto.system.SysVersionDto;
import com.hnctdz.aiLock.service.system.SysVersionService;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName SysSysVersionAction.java
 * @Author WangXiangBo 
 */
@SuppressWarnings("serial")
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/SysVersionAction")
@Controller
public class SysVersionAction extends BaseAction{
private static final Logger LOG = Logger.getLogger(SysVersionAction.class);  
	
	@Autowired
	private SysVersionService sysVersionService;
	
	private SysVersion sysVersion;
	private SysVersionDto dto;
	private String versionIds;
	
	/**
	 * 查询版本信息列表
	 */
	@Action(value="findPageSysVersion")
	@ToJson(outField="dataPackage")
	public String findPageSysVersion(){
		dataPackage = getDataPackage();
		try {
			dataPackage = sysVersionService.findPageSysVersion(dto, dataPackage);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 保存版本信息
	 */
	@Action(value="saveSysVersion")
	@ToJson(outField="simpleRespose")
	public String saveSysVersion(){
		String result = "";
		try{
			if(sysVersion != null){
				//检查版本号是否存在
				boolean checkResult  = sysVersionService.checkSysVersionCode(sysVersion.getVersionOs(), sysVersion.getVersionCode(), sysVersion.getVersionId());
				if(checkResult){
					if(sysVersion.getVersionId() == null){
						sysVersion.setCreateTime(new Date());
					}
					
//					if(sysVersion.getVersionOs() == 0){
//						String versionInfo = sysVersion.getVersionInfo();
//						sysVersion.setDonwloadUrl(versionInfo.substring(versionInfo.indexOf("http"),versionInfo.length()));
//						sysVersion.setVersionInfo(versionInfo.substring(0,versionInfo.indexOf("http")));
//					}
					
					sysVersionService.saveSysVersion(sysVersion);
				}else{
					result = "该版本号已存在！";
				}
			}
		}catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			result = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			if(!result.equalsIgnoreCase("")){
				simpleRespose.setResultCode(0001);
				simpleRespose.setResultMessage(result);
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 删除版本信息
	 */
	@Action(value="deleteSysVersion")
	@ToJson(outField="simpleRespose")
	public String deleteSysVersion(){
		String result = "";
		try{
			result = sysVersionService.deleteSysVersionByIds(versionIds);
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

	public SysVersion getSysVersion() {
		return sysVersion;
	}

	public void setSysVersion(SysVersion sysVersion) {
		this.sysVersion = sysVersion;
	}

	public SysVersionDto getDto() {
		return dto;
	}

	public void setDto(SysVersionDto dto) {
		this.dto = dto;
	}
}
