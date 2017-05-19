package com.hnctdz.aiLock.dto.system;

import java.util.Date;

/**
 * @ClassName SysVersionDto.java
 * @Author WangXiangBo
 */
public class SysVersionDto {
	private Integer versionId;
	private String versionCode;
	private String versionName;
	private Integer versionOs;
	private String versionInfo;
	private String resType;
	private String donwloadUrl;
	private Date createTime;
	private Integer updateType;
	private Integer versionStatus;

	public Integer getVersionId() {
		return versionId;
	}

	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public Integer getVersionOs() {
		return versionOs;
	}

	public void setVersionOs(Integer versionOs) {
		this.versionOs = versionOs;
	}

	public String getVersionInfo() {
		return versionInfo;
	}

	public void setVersionInfo(String versionInfo) {
		this.versionInfo = versionInfo;
	}

	public String getResType() {
		return resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	public String getDonwloadUrl() {
		return donwloadUrl;
	}

	public void setDonwloadUrl(String donwloadUrl) {
		this.donwloadUrl = donwloadUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getUpdateType() {
		return updateType;
	}

	public void setUpdateType(Integer updateType) {
		this.updateType = updateType;
	}

	public Integer getVersionStatus() {
		return versionStatus;
	}

	public void setVersionStatus(Integer versionStatus) {
		this.versionStatus = versionStatus;
	}
}
