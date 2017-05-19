package com.hnctdz.aiLock.domain.analysis;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.google.gson.annotations.Expose;

/**
 * UnlockRecords entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "unlock_records")
public class UnlockRecords implements java.io.Serializable {

	// Fields
	@Expose
	private Integer recordId;
	private Integer lockType;
	private String recordCode;
	@Expose
	private String recordTpye;
	private String keyCode;
	private String lockInModuleCode;
	private String lockDeviceNo;
	private String lockCode;
	private String smartKeyPerId;
	private Long unlockPerId;
	@Expose
	private String unlockTime;
	private Long perId;
	@Expose
	private Date uploadTime;
	private Long userId;
	private Date confirmTime;
	private String note;
	private String message;
	private String remoteUnlockResults;
	
	@Expose
	private String unlockPerName;
	private String keyName;
	private String keyTypeName;
	@Expose
	private String lockName;
	private String perName;
	private String userName;
	private String unlockPerPhone;
	private String orgName;
	private String areaName;
	private String alarmLevel;


	// Constructors

	/** default constructor */
	public UnlockRecords() {
	}

	/** minimal constructor */
	public UnlockRecords(Integer recordId) {
		this.recordId = recordId;
	}

	/** full constructor */
	public UnlockRecords(Integer recordId, Integer lockType, String recordCode,
			String recordTpye, String keyCode, String lockInModuleCode,
			String lockDeviceNo, String lockCode, String smartKeyPerId,
			String unlockTime, String remoteUnlockResults, Long perId,
			Date uploadTime, Long userId, Date confirmTime,
			String note) {
		this.recordId = recordId;
		this.lockType = lockType;
		this.recordCode = recordCode;
		this.recordTpye = recordTpye;
		this.keyCode = keyCode;
		this.lockInModuleCode = lockInModuleCode;
		this.lockDeviceNo = lockDeviceNo;
		this.lockCode = lockCode;
		this.smartKeyPerId = smartKeyPerId;
		this.unlockTime = unlockTime;
		this.remoteUnlockResults = remoteUnlockResults;
		this.perId = perId;
		this.uploadTime = uploadTime;
		this.userId = userId;
		this.confirmTime = confirmTime;
		this.note = note;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "RECORD_ID", unique = true, nullable = false)
	public Integer getRecordId() {
		return this.recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	@Column(name = "LOCK_TYPE")
	public Integer getLockType() {
		return this.lockType;
	}

	public void setLockType(Integer lockType) {
		this.lockType = lockType;
	}

	@Column(name = "RECORD_CODE", length = 5)
	public String getRecordCode() {
		return this.recordCode;
	}

	public void setRecordCode(String recordCode) {
		this.recordCode = recordCode;
	}

	@Column(name = "RECORD_TPYE", length = 10)
	public String getRecordTpye() {
		return this.recordTpye;
	}

	public void setRecordTpye(String recordTpye) {
		this.recordTpye = recordTpye;
	}

	@Column(name = "KEY_CODE", length = 50)
	public String getKeyCode() {
		return this.keyCode;
	}

	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}

	@Column(name = "LOCK_IN_MODULE_CODE", length = 50)
	public String getLockInModuleCode() {
		return this.lockInModuleCode;
	}

	public void setLockInModuleCode(String lockInModuleCode) {
		this.lockInModuleCode = lockInModuleCode;
	}

	@Column(name = "LOCK_DEVICE_NO", length = 50)
	public String getLockDeviceNo() {
		return this.lockDeviceNo;
	}

	public void setLockDeviceNo(String lockDeviceNo) {
		this.lockDeviceNo = lockDeviceNo;
	}

	@Column(name = "LOCK_CODE")
	public String getLockCode() {
		return this.lockCode;
	}

	public void setLockCode(String lockCode) {
		this.lockCode = lockCode;
	}

	@Column(name = "SMART_KEY_PER_ID")
	public String getSmartKeyPerId() {
		return this.smartKeyPerId;
	}

	public void setSmartKeyPerId(String smartKeyPerId) {
		this.smartKeyPerId = smartKeyPerId;
	}

	@Column(name = "UNLOCK_TIME")
	public String getUnlockTime() {
		return this.unlockTime;
	}

	public void setUnlockTime(String unlockTime) {
		this.unlockTime = unlockTime;
	}

	@Column(name = "PER_ID")
	public Long getPerId() {
		return this.perId;
	}

	public void setPerId(Long perId) {
		this.perId = perId;
	}
	
	@Column(name = "UNLOCK_PER_ID")
	public Long getUnlockPerId() {
		return unlockPerId;
	}

	public void setUnlockPerId(Long unlockPerId) {
		this.unlockPerId = unlockPerId;
	}

	@Column(name = "UPLOAD_TIME", length = 19)
	public Date getUploadTime() {
		return this.uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	@Column(name = "USER_ID")
	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "CONFIRM_TIME", length = 19)
	public Date getConfirmTime() {
		return this.confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}
	
	@Column(name = "MESSAGE")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "NOTE", length = 200)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	@Column(name = "REMOTE_UNLOCK_RESULTS")
	public String getRemoteUnlockResults() {
		return remoteUnlockResults;
	}

	public void setRemoteUnlockResults(String remoteUnlockResults) {
		this.remoteUnlockResults = remoteUnlockResults;
	}

	@Transient
	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	@Transient
	public String getLockName() {
		return lockName;
	}

	public void setLockName(String lockName) {
		this.lockName = lockName;
	}

	@Transient
	public String getPerName() {
		return perName;
	}

	public void setPerName(String perName) {
		this.perName = perName;
	}

	@Transient
	public String getUnlockPerName() {
		return unlockPerName;
	}

	public void setUnlockPerName(String unlockPerName) {
		this.unlockPerName = unlockPerName;
	}

	@Transient
	public String getKeyTypeName() {
		return keyTypeName;
	}

	public void setKeyTypeName(String keyTypeName) {
		this.keyTypeName = keyTypeName;
	}

	@Transient
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Transient
	public String getUnlockPerPhone() {
		return unlockPerPhone;
	}

	public void setUnlockPerPhone(String unlockPerPhone) {
		this.unlockPerPhone = unlockPerPhone;
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
	public String getAlarmLevel() {
		return alarmLevel;
	}

	public void setAlarmLevel(String alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

}