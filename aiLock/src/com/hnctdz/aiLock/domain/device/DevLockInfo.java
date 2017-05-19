package com.hnctdz.aiLock.domain.device;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;

/**
 * DevLockInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "DEV_LOCK_INFO")
public class DevLockInfo implements java.io.Serializable {

	// Fields
	@Expose
	private Integer lockId;
	@Expose
	private String lockCode;
	@Expose
	private String lockName;
	private String unlockPassword;
	@Expose
	private Integer lockType;
	@Expose
	private String lockAddres;
	@Expose
	private Double longitude;
	@Expose
	private Double latitude;
	private Long areaId;
	private Long orgId;
	@Expose
	private String status;
	private String note;
	private String lockDeviceNo;
	private String lockInModuleCode;
	private String ipAddress;
	private String lockInBlueCode;
	private Integer wheCanMatchCard;
	private String vicePassiveLockCode;
	private Integer lockParentId;
	private String blueMac;
	private String privateKey;
	
	
	private String orgName;
	private String managerName;
	private String areaName;
	private String onlineStauts;

	// Constructors

	/** default constructor */
	public DevLockInfo() {
	}

	/** minimal constructor */
	public DevLockInfo(String lockCode) {
		this.lockCode = lockCode;
	}

	/** full constructor */
	public DevLockInfo(Integer lockId, String lockCode, String lockName, Integer lockType,
			String lockAddres, Double longitude, Double latitude,
			Long areaId, Long orgId, String status,
			String note, String unlockPassword) {
		this.lockId = lockId;
		this.lockCode = lockCode;
		this.lockName = lockName;
		this.lockType = lockType;
		this.lockAddres = lockAddres;
		this.longitude = longitude;
		this.latitude = latitude;
		this.areaId = areaId;
		this.orgId = orgId;
		this.status = status;
		this.note = note;
		this.unlockPassword = unlockPassword;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "LOCK_ID", unique = true, nullable = false)
	public Integer getLockId() {
		return lockId;
	}

	public void setLockId(Integer lockId) {
		this.lockId = lockId;
	}
	
	@Column(name = "LOCK_CODE", length = 50)
	public String getLockCode() {
		return this.lockCode;
	}

	public void setLockCode(String lockCode) {
		this.lockCode = lockCode;
	}

	@Column(name = "LOCK_NAME", length = 100)
	public String getLockName() {
		return this.lockName;
	}

	public void setLockName(String lockName) {
		this.lockName = lockName;
	}

	@Column(name = "LOCK_TYPE", precision = 1, scale = 0)
	public Integer getLockType() {
		return this.lockType;
	}

	public void setLockType(Integer lockType) {
		this.lockType = lockType;
	}

	@Column(name = "LOCK_ADDRES", length = 200)
	public String getLockAddres() {
		return this.lockAddres;
	}

	public void setLockAddres(String lockAddres) {
		this.lockAddres = lockAddres;
	}

	@Column(name = "LONGITUDE", precision = 12, scale = 8)
	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	@Column(name = "LATITUDE", precision = 12, scale = 9)
	public Double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	@Column(name = "AREA_ID", precision = 10, scale = 0)
	public Long getAreaId() {
		return this.areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	@Column(name = "ORG_ID", precision = 10, scale = 0)
	public Long getOrgId() {
		return this.orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	@Column(name = "STATUS", length = 10)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "NOTE", length = 200)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	@Column(name = "UNLOCK_PASSWORD", length = 100)
	public String getUnlockPassword() {
		return this.unlockPassword;
	}

	public void setUnlockPassword(String unlockPassword) {
		this.unlockPassword = unlockPassword;
	}
	
	@Column(name = "LOCK_DEVICE_NO", length = 50)
	public String getLockDeviceNo() {
		return lockDeviceNo;
	}

	public void setLockDeviceNo(String lockDeviceNo) {
		this.lockDeviceNo = lockDeviceNo;
	}

	@Column(name = "LOCK_IN_MODULE_CODE", length = 50)
	public String getLockInModuleCode() {
		return lockInModuleCode;
	}

	public void setLockInModuleCode(String lockInModuleCode) {
		this.lockInModuleCode = lockInModuleCode;
	}
	
	@Column(name = "IP_ADDRESS", length = 20)
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	@Column(name = "LOCK_IN_BLUE_CODE", length = 20)
	public String getLockInBlueCode() {
		return lockInBlueCode;
	}

	public void setLockInBlueCode(String lockInBlueCode) {
		this.lockInBlueCode = lockInBlueCode;
	}

	@Column(name = "WHE_CAN_MATCH_CARD", precision = 1, scale = 0)
	public Integer getWheCanMatchCard() {
		return wheCanMatchCard;
	}

	public void setWheCanMatchCard(Integer wheCanMatchCard) {
		this.wheCanMatchCard = wheCanMatchCard;
	}

	@Column(name = "VICE_PASSIVE_LOCK_CODE", length = 50)
	public String getVicePassiveLockCode() {
		return vicePassiveLockCode;
	}

	public void setVicePassiveLockCode(String vicePassiveLockCode) {
		this.vicePassiveLockCode = vicePassiveLockCode;
	}
	
	@Column(name = "LOCK_PARENT_ID", precision = 10, scale = 0)
	public Integer getLockParentId() {
		return lockParentId;
	}

	public void setLockParentId(Integer lockParentId) {
		this.lockParentId = lockParentId;
	}
	
	@Column(name = "BLUE_MAC", length = 50)
	public String getBlueMac() {
		return blueMac;
	}

	public void setBlueMac(String blueMac) {
		this.blueMac = blueMac;
	}

	@Column(name = "PRIVATE_KEY", length = 50)
	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	
	@Transient
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@Transient
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@Transient
	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	@Transient
	public String getOnlineStauts() {
		return onlineStauts;
	}

	public void setOnlineStauts(String onlineStauts) {
		this.onlineStauts = onlineStauts;
	}

}