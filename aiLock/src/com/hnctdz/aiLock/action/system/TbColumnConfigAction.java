/*
 * FileName:     TbColumnConfigAction.java
 * @Description: 
 * Copyright (c) 2014 Sunrise Corporation.
 * #368, Guangzhou Avenue South, Haizhu District, Guangzhou
 * All right reserved.
 *
 * Modification  History:
 * Date           Author         Version         Discription
 * -----------------------------------------------------------------------------------
 * 2014-7-13   WangXiangBo        1.0              新建
 */
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
import com.hnctdz.aiLock.domain.system.TbColumnConfig;
import com.hnctdz.aiLock.dto.system.TbColumnConfigDto;
import com.hnctdz.aiLock.service.system.TbColumnConfigService;
import com.hnctdz.aiLock.utils.ErrorCode;
import com.hnctdz.aiLock.utils.ErrorCodeException;
import com.hnctdz.aiLock.utils.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

/** 
 * @ClassName TbColumnConfigAction.java
 * @Author WangXiangBo 
 * @Date 2014-7-13 下午04:23:32
 */
@SuppressWarnings("serial")
@Result(name = ActionSupport.SUCCESS, type = "json")
@Namespace("/TbColumnConfigAction")
@Controller
public class TbColumnConfigAction extends BaseAction{
	private static final Logger LOG = Logger.getLogger(TbColumnConfigAction.class);  
	
	@Autowired
	private TbColumnConfigService tbColumnConfigService;
	
	private TbColumnConfigDto dto;
	
	/**
	 * 查询  模块数据表格 表头，返回list
	 * @param dto.gridCode 表格编码
	 */
	@Action(value="findTbColumnConfigToCode")
	@ToJson(outField="dataPackage")
	public String findTbColumnConfigToCode(){
		dataPackage = getDataPackage();
		try {
			if(dto != null && StringUtil.isNotEmpty(dto.getGridCode())){
				dto.setStatus(1L);//只返回状态为: 显示 的列
				dataPackage.setRows(tbColumnConfigService.findTbColumnConfig(dto));
			}
		} catch(Exception e) {
			LOG.error(ErrorCodeException.getExceInfo(e));
			dataPackage.setResultCode(0001);
			dataPackage.setResultMessage(ErrorCode.ERROR_SYS_EXCEPTION);
		}
		return SUCCESS;
	}

	public TbColumnConfigDto getDto() {
		return dto;
	}

	public void setDto(TbColumnConfigDto dto) {
		this.dto = dto;
	}
}
