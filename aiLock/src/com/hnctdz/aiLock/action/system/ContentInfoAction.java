package com.hnctdz.aiLock.action.system;

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
import com.hnctdz.aiLock.domain.system.ContentInfo;
import com.hnctdz.aiLock.domain.system.SysUser;
import com.hnctdz.aiLock.dto.system.ContentInfoDto;
import com.hnctdz.aiLock.security.SecurityUserHolder;
import com.hnctdz.aiLock.service.system.ContentInfoService;
import com.hnctdz.aiLock.service.system.SysUserService;
import com.hnctdz.aiLock.service.system.UpdateInfoService;
import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.hnctdz.aiLock.utils.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName ContentInfoAction.java
 * @Author WangXiangBo (wangxiangbo127@163.com)
 */
@SuppressWarnings("serial")
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/ContentInfoAction")
@Controller
public class ContentInfoAction extends BaseAction{
	private static final Logger LOG = Logger.getLogger(ContentInfoAction.class);  
	
	@Autowired
	private ContentInfoService ContentInfoService;
	@Autowired
 	private SysUserService sysUserService;
	@Autowired
	private UpdateInfoService updateInfoService;
	
	private ContentInfo contentInfo;
	private ContentInfoDto dto;
	private String contentIds;
	
	/**
	 * 查询系统内容列表
	 */
	@Action(value="findPageContentInfo")
	@ToJson(outField="dataPackage")
	public String findPageContentInfo(){
		dataPackage = getDataPackage();
		try {
			dataPackage = ContentInfoService.findPageContentInfo(dto, dataPackage);
			List<ContentInfo> datas = (List<ContentInfo>) dataPackage.getRows();
	 		for(ContentInfo data : datas){
	 			data.setIconImg(data.getIcon());
	 			data.setBigIconImg(data.getBigIcon());
	 			
	 			if(StringUtil.isNotEmpty(data.getSuportVersion())){
	 				String[] suportVersion = data.getSuportVersion().split(Constants.VERSION_CODE_SEPARATOR);
	 				data.setIosVersion(suportVersion[0]);
	 				data.setAndroidVersion(suportVersion[1]);
	 			}
	 			
	 			if(data.getCeataUser() != null){
	 				SysUser user = sysUserService.getSysUserById(data.getCeataUser());
	 				data.setCeataUserName(user.getUserName());
	 			}
	 			if(data.getUpdateUser() != null){
	 				SysUser user = sysUserService.getSysUserById(data.getUpdateUser());
	 				data.setUpdateUserName(user.getUserName());
	 			}
	 		}
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 保存系统内容信息
	 */
	@Action(value="saveContentInfo")
	@ToJson(outField="simpleRespose")
	public String saveContentInfo(){
		try{
			SysUser sysUser = SecurityUserHolder.getCurrentUser();
			
			if(contentInfo != null){
				if(contentInfo.getContentId() != null){
					contentInfo.setUpdateTime(new Date());
					contentInfo.setUpdateUser(sysUser.getUserId());
				}else{
					contentInfo.setCeataTime(new Date());
					contentInfo.setCeataUser(sysUser.getUserId());
				}
			}
			contentInfo.setSuportVersion(contentInfo.getIosVersion() + 
					Constants.VERSION_CODE_SEPARATOR + contentInfo.getAndroidVersion());
			
			ContentInfoService.saveContentInfo(contentInfo);
			
		} catch(ErrorCodeException e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(e.getMessage());
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 删除系统内容信息
	 */
	@Action(value="deleteContentInfo")
	@ToJson(outField="simpleRespose")
	public String deleteContentInfo(){
		String result = "";
		try{
			result = ContentInfoService.deleteContentInfoByIds(contentIds);
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

	public ContentInfo getContentInfo() {
		return contentInfo;
	}

	public void setContentInfo(ContentInfo contentInfo) {
		this.contentInfo = contentInfo;
	}

	public ContentInfoDto getDto() {
		return dto;
	}

	public void setDto(ContentInfoDto dto) {
		this.dto = dto;
	}

	public String getContentIds() {
		return contentIds;
	}

	public void setContentIds(String contentIds) {
		this.contentIds = contentIds;
	}
}
