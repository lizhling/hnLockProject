package com.hnctdz.aiLock.domain.device;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.google.gson.annotations.Expose;

/**
 * LockKeyAuthorize entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "LOCK_KEY_AUTHORIZE")
public class LockKeyAuthorize implements java.io.Serializable {

	// Fields
	@Expose
	private Long authorizeId;
	private String authorizeCode;
	@Expose
	private Integer lockId;
	private Long unlockPerId;
	@Expose
	private Date startTime;
	@Expose
	private Date endTime;
	@Expose
	private Long unlockNumber;
	@Expose
	private Long blueUnlock;
	@Expose
	private String statusCode;
	private Long cuUserId;
	private Date cuTime;
	@Expose
	private Long authorizeType;
	@Expose
	private Integer scopeUnlock;
	
	private String keyName;
	private String lockCode;
	@Expose
	private String lockName;
	private String cuUserName;
	@Expose
	private String unlockPerName;
	private String lockDeviceNo;
	private String lockInModuleCode;
	private String authorizeLockIds;
	private String authorizePerIds;
	public Integer lockType;

	// Constructors

	/** default constructor */
	public LockKeyAuthorize() {
	}

	/** full constructor */
	public LockKeyAuthorize(Long authorizeId, String authorizeCode,
			Long unlockPerId, Integer lockId, String unlockPassword,
			Date startTime, Date endTime, Long unlockNumber,
			String statusCode, Long cuUserId, Date cuTime) {
		this.authorizeId = authorizeId;
		this.authorizeCode = authorizeCode;
		this.unlockPerId = unlockPerId;
		this.lockId = lockId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.unlockNumber = unlockNumber;
		this.statusCode = statusCode;
		this.cuUserId = cuUserId;
		this.cuTime = cuTime;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "AUTHORIZE_ID", unique = true, nullable = false)
	public Long getAuthorizeId() {
		return this.authorizeId;
	}

	public void setAuthorizeId(Long authorizeId) {
		this.authorizeId = authorizeId;
	}

	@Column(name = "AUTHORIZE_CODE", nullable = false, length = 50)
	public String getAuthorizeCode() {
		return this.authorizeCode;
	}

	public void setAuthorizeCode(String authorizeCode) {
		this.authorizeCode = authorizeCode;
	}

	@Column(name = "LOCK_ID", nullable = false, precision = 10, scale = 0)
	public Integer getLockId() {
		return this.lockId;
	}

	public void setLockId(Integer lockId) {
		this.lockId = lockId;
	}
	
	@Column(name = "UNLOCK_PER_ID", precision = 10, scale = 0)
	public Long getUnlockPerId() {
		return unlockPerId;
	}

	public void setUnlockPerId(Long unlockPerId) {
		this.unlockPerId = unlockPerId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME", length = 7)
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_TIME", length = 7)
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "UNLOCK_NUMBER", precision = 5, scale = 0)
	public Long getUnlockNumber() {
		return this.unlockNumber;
	}

	public void setUnlockNumber(Long unlockNumber) {
		this.unlockNumber = unlockNumber;
	}

	@Column(name = "STATUS_CODE", length = 50)
	public String getStatusCode() {
		return this.statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	@Column(name = "CU_USER_ID", precision = 10, scale = 0)
	public Long getCuUserId() {
		return this.cuUserId;
	}

	public void setCuUserId(Long cuUserId) {
		this.cuUserId = cuUserId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CU_TIME", length = 7)
	public Date getCuTime() {
		return this.cuTime;
	}

	public void setCuTime(Date cuTime) {
		this.cuTime = cuTime;
	}
	
	@Column(name = "BLUE_UNLOCK", precision = 1, scale = 0)
	public Long getBlueUnlock() {
		return blueUnlock;
	}

	public void setBlueUnlock(Long blueUnlock) {
		this.blueUnlock = blueUnlock;
	}

	@Column(name = "AUTHORIZE_TYPE", precision = 1, scale = 0)
	public Long getAuthorizeType() {
		return authorizeType;
	}

	public void setAuthorizeType(Long authorizeType) {
		this.authorizeType = authorizeType;
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
	
	@Column(name = "SCOPE_UNLOCK", precision = 1, scale = 0)
	public Integer getScopeUnlock() {
		return scopeUnlock;
	}

	public void setScopeUnlock(Integer scopeUnlock) {
		this.scopeUnlock = scopeUnlock;
	}

	@Transient
	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	@Transient
	public String getLockCode() {
		return lockCode;
	}

	public void setLockCode(String lockCode) {
		this.lockCode = lockCode;
	}

	@Transient
	public String getLockName() {
		return lockName;
	}

	public void setLockName(String lockName) {
		this.lockName = lockName;
	}

	@Transient
	public String getCuUserName() {
		return cuUserName;
	}

	public void setCuUserName(String cuUserName) {
		this.cuUserName = cuUserName;
	}

	@Transient
	public String getUnlockPerName() {
		return unlockPerName;
	}

	public void setUnlockPerName(String unlockPerName) {
		this.unlockPerName = unlockPerName;
	}

	@Transient
	public String getAuthorizeLockIds() {
		return authorizeLockIds;
	}

	public void setAuthorizeLockIds(String authorizeLockIds) {
		this.authorizeLockIds = authorizeLockIds;
	}

	@Transient
	public String getAuthorizePerIds() {
		return authorizePerIds;
	}

	public void setAuthorizePerIds(String authorizePerIds) {
		this.authorizePerIds = authorizePerIds;
	}
	
	@Transient
	public Integer getLockType() {
		return lockType;
	}

	public void setLockType(Integer lockType) {
		this.lockType = lockType;
	}


}