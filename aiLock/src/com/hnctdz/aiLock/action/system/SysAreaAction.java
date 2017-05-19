package com.hnctdz.aiLock.action.system;

import com.hnctdz.aiLock.action.BaseAction;
import com.hnctdz.aiLock.domain.system.SysArea;
import com.hnctdz.aiLock.dto.NodeInfo;
import com.hnctdz.aiLock.dto.system.SysAreaDto;
import com.hnctdz.aiLock.service.system.SysAreaService;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.hnctdz.aiLock.utils.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

import java.io.File;
import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.nutz.json.annotations.ToJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@SuppressWarnings("serial")
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/SysAreaAction")
@Controller
public class SysAreaAction extends BaseAction {

	@Autowired
	private SysAreaService sysAreaService;
	private SysArea sysArea;
	private SysAreaDto dto;
	private String areaIds;
	List<NodeInfo> nodeList = null;
	
	private File importFile;//文件上传

	@Action("findPageSysArea")
	@ToJson(outField = "dataPackage")
	public String findPageSysArea() {
		this.dataPackage = getDataPackage();
		try {
			this.dataPackage = this.sysAreaService.findPageSysArea(this.dto,
					this.dataPackage);
		} catch (Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e), new String[0]);
			this.dataPackage.setResultCode(1);
			this.dataPackage.setResultMessage("系统处理失败，请稍后再试！");
		}
		return SUCCESS;
	}

	@Action("saveSysArea")
	@ToJson(outField = "simpleRespose")
	public String saveSysArea() {
		String results = "";
		try {
			results = this.sysAreaService.saveSysArea(this.sysArea);
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

	@Action("deleteSysArea")
	@ToJson(outField = "simpleRespose")
	public String deleteSysArea() {
		String results = "";
		try {
			results = this.sysAreaService.deleteSysAreaByIds(this.areaIds);
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

	@Action("findSysAreaTree")
	@ToJson(outField = "nodeList")
	public String findSysAreaTree() {
		this.dataPackage = getDataPackage();
		try {
			List<SysArea> list = this.sysAreaService.findSysAreaList(this.dto);
			this.nodeList = NodeInfo.getSysAreaToNode(list);
		} catch (Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e), new String[0]);
			this.dataPackage.setResultCode(1);
			this.dataPackage.setResultMessage("系统处理失败，请稍后再试！");
		}
		return SUCCESS;
	}
	
	/**
	 * 导入织织节点
	 * @return
	 */
	@Action(value="importAreaInfo")
	@ToJson(outField="simpleRespose")
	public String importAreaInfo(){
		String result = "";
		try{
				result = sysAreaService.importAreaInfos(importFile);
		}catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			result = ErrorCode.ERROR_SYS_EXCEPTION;
		} finally {
			if(StringUtil.isNotEmpty(result)){
				result = result.replaceAll("、行", "行");
				simpleRespose.setResultCode(0001);
				simpleRespose.setResultMessage(result);
			}
		}
		return SUCCESS;
	}
	/**
	 * 导出组织节点
	 */
	@Action(value="exportAreaInfo")
	@ToJson(outField="dataPackage")
	public String exportAreaInfo(){
		String result = "";
		try{
			sysAreaService.exportAreaInfo(request, response);
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			result = ErrorCode.ERROR_SYS_EXCEPTION;
		}
		return null;
	}
	
	
	

	public SysArea getSysArea() {
		return this.sysArea;
	}

	public void setSysArea(SysArea sysArea) {
		this.sysArea = sysArea;
	}

	public SysAreaDto getDto() {
		return this.dto;
	}

	public void setDto(SysAreaDto dto) {
		this.dto = dto;
	}

	public String getAreaIds() {
		return this.areaIds;
	}

	public void setAreaIds(String areaIds) {
		this.areaIds = areaIds;
	}
	
	public File getImportFile() {
		return importFile;
	}

	public void setImportFile(File importFile) {
		this.importFile = importFile;
	}
}