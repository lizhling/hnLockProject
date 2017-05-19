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
import com.hnctdz.aiLock.domain.system.SysRes;
import com.hnctdz.aiLock.domain.system.SysUser;
import com.hnctdz.aiLock.dto.NodeInfo;
import com.hnctdz.aiLock.dto.system.SysResDto;
import com.hnctdz.aiLock.security.SecurityUserHolder;
import com.hnctdz.aiLock.service.system.SysResService;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName SysResAction.java
 * @Author WangXiangBo (wangxiangbo127@163.com)
 */
@SuppressWarnings("serial")
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/SysResAction")
@Controller
public class SysResAction extends BaseAction{
	private static final Logger LOG = Logger.getLogger(SysResAction.class);  
	
	@Autowired
	private SysResService sysResService;
	List<NodeInfo> nodeList = null;
	
	private SysRes sysRes;
	private SysResDto dto;
	private String resIds;
	
	/**
	 * 查询功能菜单列表
	 */
	@Action(value="findPageSysRes")
	@ToJson(outField="dataPackage")
	public String findPageSysRes(){
		dataPackage = getDataPackage();
		try {
			dataPackage = sysResService.findPageSysRes(dto, dataPackage);
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
	@Action(value="saveSysRes")
	@ToJson(outField="simpleRespose")
	public String saveSysRes(){
		try{
			sysResService.saveSysRes(sysRes);
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
	@Action(value="deleteSysRes")
	@ToJson(outField="simpleRespose")
	public String deleteSysRes(){
		String result = "";
		try{
			result = sysResService.deleteSysResByIds(resIds);
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
	 * 获取所有菜单下拉树列表
	 */
	@Action(value="findSysResTreeList")
	@ToJson(outField="nodeList")
	public String findSysResTreeList(){
		try{
			if(dto == null){
				dto = new SysResDto();
			}
			dto.setStatus(1L);
			List<SysRes> list = sysResService.findSysResList(dto);
			nodeList = NodeInfo.getNodeChildren(list);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	
	/**
	 * 登录获取的菜单
	 */
	@Action(value="queryMenuTreeData")
	@ToJson(outField="nodeList")
	public String queryMenuTreeData(){
		
		try {
			List<SysRes> list = null;
			SysUser sysUser = SecurityUserHolder.getCurrentUser();
			if(sysUser.getUserName().equalsIgnoreCase("admin")){
				dto = new SysResDto();
				dto.setResType(0L);
				dto.setStatus(1L);
				list = sysResService.findSysResList(dto);
			}else{
				list = sysResService.findMenuResByRoleIds(sysUser.getUserInRoleIds(), 0);
			}
			nodeList = NodeInfo.getNodeChildren(list);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage("未知错误：" + e.getMessage());
		}
		return SUCCESS;
	}
	

	public SysResDto getDto() {
		return dto;
	}

	public void setDto(SysResDto dto) {
		this.dto = dto;
	}

	public SysRes getSysRes() {
		return sysRes;
	}

	public void setSysRes(SysRes sysRes) {
		this.sysRes = sysRes;
	}

	public String getResIds() {
		return resIds;
	}

	public void setResIds(String resIds) {
		this.resIds = resIds;
	}
	
}
