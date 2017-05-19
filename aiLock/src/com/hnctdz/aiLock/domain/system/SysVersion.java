package com.hnctdz.aiLock.domain.system;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SysVersion entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_version")
public class SysVersion implements java.io.Serializable {

	// Fields

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

	// Constructors

	/** default constructor */
	public SysVersion() {
	}

	/** full constructor */
	public SysVersion(String versionCode, String versionName,
			Integer versionOs, String versionInfo, String resType,
			String donwloadUrl, Date createTime, Integer updateType,
			Integer versionStatus) {
		this.versionCode = versionCode;
		this.versionName = versionName;
		this.versionOs = versionOs;
		this.versionInfo = versionInfo;
		this.resType = resType;
		this.donwloadUrl = donwloadUrl;
		this.createTime = createTime;
		this.updateType = updateType;
		this.versionStatus = versionStatus;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "VERSION_ID", unique = true, nullable = false)
	public Integer getVersionId() {
		return this.versionId;
	}

	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}

	@Column(name = "VERSION_CODE", length = 100)
	public String getVersionCode() {
		return this.versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	@Column(name = "VERSION_NAME", length = 100)
	public String getVersionName() {
		return this.versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	@Column(name = "VERSION_OS")
	public Integer getVersionOs() {
		return this.versionOs;
	}

	public void setVersionOs(Integer versionOs) {
		this.versionOs = versionOs;
	}

	@Column(name = "VERSION_INFO", length = 200)
	public String getVersionInfo() {
		return this.versionInfo;
	}

	public void setVersionInfo(String versionInfo) {
		this.versionInfo = versionInfo;
	}

	@Column(name = "RES_TYPE", length = 1)
	public String getResType() {
		return this.resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	@Column(name = "DONWLOAD_URL", length = 200)
	public String getDonwloadUrl() {
		return this.donwloadUrl;
	}

	public void setDonwloadUrl(String donwloadUrl) {
		this.donwloadUrl = donwloadUrl;
	}

	@Column(name = "CREATE_TIME", length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "UPDATE_TYPE")
	public Integer getUpdateType() {
		return this.updateType;
	}

	public void setUpdateType(Integer updateType) {
		this.updateType = updateType;
	}

	@Column(name = "VERSION_STATUS")
	public Integer getVersionStatus() {
		return this.versionStatus;
	}

	public void setVersionStatus(Integer versionStatus) {
		this.versionStatus = versionStatus;
	}

}