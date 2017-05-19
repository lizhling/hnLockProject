/*
 * FileName:     UpdateInfoDto.java
 * @Description: 
 * Copyright (c) 2014 Sunrise Corporation.
 * #368, Guangzhou Avenue South, Haizhu District, Guangzhou
 * All right reserved.
 *
 * Modification  History:
 * Date           Author         Version         Discription
 * -----------------------------------------------------------------------------------
 * 2014-1-20   WangXiangBo        1.0              新建
 */
package com.hnctdz.aiLock.dto.system;

/**
 * @ClassName UpdateInfoDto.java
 * @Author WangXiangBo
 * @Date 2014-1-20 下午03:22:46
 */
public class UpdateInfoDto {
	private Long type;
	private String downloadUrl;
	private String newVersionCode;
	private Long suportVersion;
	private String suportVersionName;
	private String newVersionName;
	private String updateDescription;
	private Long status;

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getNewVersionCode() {
		return newVersionCode;
	}

	public void setNewVersionCode(String newVersionCode) {
		this.newVersionCode = newVersionCode;
	}

	public Long getSuportVersion() {
		return suportVersion;
	}

	public void setSuportVersion(Long suportVersion) {
		this.suportVersion = suportVersion;
	}

	public String getSuportVersionName() {
		return suportVersionName;
	}

	public void setSuportVersionName(String suportVersionName) {
		this.suportVersionName = suportVersionName;
	}

	public String getNewVersionName() {
		return newVersionName;
	}

	public void setNewVersionName(String newVersionName) {
		this.newVersionName = newVersionName;
	}

	public String getUpdateDescription() {
		return updateDescription;
	}

	public void setUpdateDescription(String updateDescription) {
		this.updateDescription = updateDescription;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}
}
