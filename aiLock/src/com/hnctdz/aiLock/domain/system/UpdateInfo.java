package com.hnctdz.aiLock.domain.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;


/**
 * UpdateInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "UPDATE_INFO")
public class UpdateInfo implements java.io.Serializable {

	// Fields
	@Expose
	private Long type;
	@Expose
	private String downloadUrl;
	@Expose
	private String newVersionCode;
	@Expose
	private String suportVersion;
	@Expose
	private String suportVersionName;
	@Expose
	private String newVersionName;
	@Expose
	private String updateDescription;
	private Long status;
	@Expose
	private Long updateType;

	// Constructors

	/** default constructor */
	public UpdateInfo() {
	}

	/** minimal constructor */
	public UpdateInfo(Long type) {
		this.type = type;
	}

	/** full constructor */
	public UpdateInfo(Long type, String downloadUrl, String newVersionCode,
			String suportVersion, String suportVersionName,
			String newVersionName, String updateDescription,Long status) {
		this.type = type;
		this.downloadUrl = downloadUrl;
		this.newVersionCode = newVersionCode;
		this.suportVersion = suportVersion;
		this.suportVersionName = suportVersionName;
		this.newVersionName = newVersionName;
		this.updateDescription = updateDescription;
		this.status = status;
	}

	// Property accessors
	@Id
	@Column(name = "TYPE", unique = true, nullable = false, precision = 5, scale = 0)
	public Long getType() {
		return this.type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	@Column(name = "DOWNLOAD_URL", length = 200)
	public String getDownloadUrl() {
		return this.downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	@Column(name = "NEW_VERSION_CODE", length = 100)
	public String getNewVersionCode() {
		return this.newVersionCode;
	}

	public void setNewVersionCode(String newVersionCode) {
		this.newVersionCode = newVersionCode;
	}

	@Column(name = "SUPORT_VERSION", length = 100)
	public String getSuportVersion() {
		return this.suportVersion;
	}

	public void setSuportVersion(String suportVersion) {
		this.suportVersion = suportVersion;
	}

	@Column(name = "SUPORT_VERSION_NAME", length = 100)
	public String getSuportVersionName() {
		return this.suportVersionName;
	}

	public void setSuportVersionName(String suportVersionName) {
		this.suportVersionName = suportVersionName;
	}

	@Column(name = "NEW_VERSION_NAME", length = 100)
	public String getNewVersionName() {
		return this.newVersionName;
	}

	public void setNewVersionName(String newVersionName) {
		this.newVersionName = newVersionName;
	}

	@Column(name = "UPDATE_DESCRIPTION", length = 1000)
	public String getUpdateDescription() {
		return this.updateDescription;
	}

	public void setUpdateDescription(String updateDescription) {
		this.updateDescription = updateDescription;
	}
	
	@Column(name = "STATUS", precision = 1, scale = 0)
	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}
	
	@Column(name = "UPDATE_TYPE", precision = 1, scale = 0)
	public Long getUpdateType() {
		return this.updateType;
	}

	public void setUpdateType(Long updateType) {
		this.updateType = updateType;
	}

}