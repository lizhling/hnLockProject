package com.hnctdz.aiLock.action.info;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.nutz.json.annotations.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hnctdz.aiLock.action.BaseAction;
import com.hnctdz.aiLock.domain.info.OrgInfo;
import com.hnctdz.aiLock.dto.NodeInfo;
import com.hnctdz.aiLock.dto.info.OrgInfoDto;
import com.hnctdz.aiLock.service.info.OrgInfoService;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.hnctdz.aiLock.utils.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName OrgInfoAction.java
 * @Author WangXiangBo (wangxiangbo127@163.com)
 */
@SuppressWarnings("serial")
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/OrgInfoAction")
@Controller
public class OrgInfoAction extends BaseAction{
	@Autowired
 	private OrgInfoService orgInfoService;
 	
 	private OrgInfo orgInfo;
 	private OrgInfoDto dto;
 	private String orgIds;
 	
 	List<NodeInfo> nodeList = null;
 	
 	/**
	 * 查询组织资料列表
	 */
	@Action(value="findPageOrgInfo")
	@ToJson(outField="dataPackage")
	public String findPageOrgInfo(){
		dataPackage = getDataPackage();
		try {
			dataPackage = orgInfoService.findPageOrgInfo(dto, dataPackage);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	
	/**
	 * 保存组织资料信息
	 */
	@Action(value="saveOrgInfo")
	@ToJson(outField="simpleRespose")
	public String saveOrgInfo(){
		String results = "";
		try {
			results = this.orgInfoService.saveOrgInfo(orgInfo);
		} catch (Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e), new String[0]);
			results = "系统处理失败，请稍后再试！";
		} finally {
			if (StringUtil.isNotEmpty(results)) {
				this.simpleRespose.setResultCode(1);
				this.simpleRespose.setResultMessage(results);
			}
		}
		return SUCCESS;
	}
	
	
	/**
	 * 删除组织资料信息
	 */
	@Action(value="deleteOrgInfo")
	@ToJson(outField="simpleRespose")
	public String deleteOrgInfo(){
		String results = "";
		try{
			results = orgInfoService.deleteOrgInfoByIds(orgIds);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			results = ErrorCode.ERROR_SYS_EXCEPTION;
		}finally{
			if(StringUtil.isNotEmpty(results)){
				simpleRespose.setResultCode(0001);
				simpleRespose.setResultMessage(results);
			}
		}
		return SUCCESS;
	}
	
	/**
	 * 获取组织资料List（下拉树使用数据）
	 */
	@Action(value="findOrgInfoTree")
	@ToJson(outField="nodeList")
	public String findOrgInfoTree(){
		dataPackage = getDataPackage();
		try {
			List<OrgInfo> list = orgInfoService.findOrgInfoList(dto);
			nodeList = NodeInfo.getOrgInfoToNode(list);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}

	public OrgInfo getOrgInfo() {
		return orgInfo;
	}

	public void setOrgInfo(OrgInfo orgInfo) {
		this.orgInfo = orgInfo;
	}

	public OrgInfoDto getDto() {
		return dto;
	}

	public void setDto(OrgInfoDto dto) {
		this.dto = dto;
	}

	public String getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}
}
