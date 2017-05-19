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
import com.hnctdz.aiLock.domain.system.SysBasicData;
import com.hnctdz.aiLock.dto.NodeInfo;
import com.hnctdz.aiLock.dto.system.SysBasicDataDto;
import com.hnctdz.aiLock.service.system.SysBasicDataService;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName SysBasicDataAction.java
 * @Author WangXiangBo (wangxiangbo127@163.com)
 */
@SuppressWarnings("serial")
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/SysBasicDataAction")
@Controller
public class SysBasicDataAction extends BaseAction{
	private static final Logger LOG = Logger.getLogger(SysBasicDataAction.class);  
	
	@Autowired
	private SysBasicDataService sysBasicDataService;
	
	private List<SysBasicData> sbd;
	
	private SysBasicData sysBasicData;
	private SysBasicDataDto dto;
	private String basicDataIds;
	
	List<NodeInfo> nodeList = null;
	
	/**
	 * 查询基础数据列表
	 */
	@Action(value="findPageSysBasicData")
	@ToJson(outField="dataPackage")
	public String findPageSysBasicData(){
		dataPackage = getDataPackage();
		try {
			dataPackage = sysBasicDataService.findPageSysBasicData(dto, dataPackage);
			List<SysBasicData> datas = (List<SysBasicData>) dataPackage.getRows();
	 		for(SysBasicData data : datas){
	 			if(data.getParentId() != null){
	 				SysBasicData sysBasicData =  sysBasicDataService.getById(data.getParentId());
	 				data.setParentName(sysBasicData.getTypeName());
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
	 * 查询基础数据类型的子类型List
	 */
	@Action(value="findSysBasicDataList")
	@ToJson(outField="dataPackage")
	public String findSysBasicDataList(){
		dataPackage = getDataPackage();
		try {
			List<SysBasicData> list = sysBasicDataService.findSysBasicDataList(dto);
			dataPackage.setRows(list);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 保存基础数据信息
	 */
	@Action(value="saveSysBasicData")
	@ToJson(outField="simpleRespose")
	public String saveSysBasicData(){
		try{
			sysBasicDataService.saveSysBasicData(sbd);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			simpleRespose.setResultCode(0001);
			simpleRespose.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 删除基础数据信息
	 */
	@Action(value="deleteSysBasicData")
	@ToJson(outField="simpleRespose")
	public String deleteSysBasicData(){
		String result = "";
		try{
			result = sysBasicDataService.deleteSysBasicDataByIds(basicDataIds);
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
	 * 查询基础数据类型 下拉框使用
	 */
	@Action(value="findSysBasicDataCombobox")
	@ToJson(outField="dataPackage")
	public String findSysBasicDataCombobox(){
		dataPackage = getDataPackage();
		try {
			List list = sysBasicDataService.findSysBasicDataCombobox(dto);
			dataPackage.setRows(list);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}
	
	/**
	 * 获取基础数据下拉树列表
	 */
	@Action(value="findSysBasicDataTreeList")
	@ToJson(outField="nodeList")
	public String findSysBasicDataTreeList(){
		try{
			if(dto != null){
				List<SysBasicData> list = sysBasicDataService.findSysBasicDataList(dto);
				nodeList = NodeInfo.getSysBasicDataToNode(list);
			}
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}

	public SysBasicData getSysBasicData() {
		return sysBasicData;
	}

	public void setSysBasicData(SysBasicData sysBasicData) {
		this.sysBasicData = sysBasicData;
	}

	public SysBasicDataDto getDto() {
		return dto;
	}

	public void setDto(SysBasicDataDto dto) {
		this.dto = dto;
	}

	public List<SysBasicData> getSbd() {
		return sbd;
	}

	public void setSbd(List<SysBasicData> sbd) {
		this.sbd = sbd;
	}

	public String getBasicDataIds() {
		return basicDataIds;
	}

	public void setBasicDataIds(String basicDataIds) {
		this.basicDataIds = basicDataIds;
	}

	
}

