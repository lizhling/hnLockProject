package com.hnctdz.aiLock.dto.device;

import java.util.Date;

/**
 * @ClassName LockKeyAuthorizeDto.java
 * @Author WangXiangBo
 */
public class LockKeyAuthorizeDto {
	private Long authorizeId;
	private String authorizeCode;
	private String keyCode;
	private Long unlockPerId;
	private Integer lockId;
	private String lockCode;
	private String unlockPassword;
	private Date startTime;
	private Date endTime;
	private Integer unlockNumber;
	private String statusCode;
	private Long cuUserId;
	private Date cuTime;

	private String keyName;
	private String lockName;
	private Long cuUserName;
	private String authorizeIds;
	private String lockIds;

	public Long getAuthorizeId() {
		return authorizeId;
	}

	public void setAuthorizeId(Long authorizeId) {
		this.authorizeId = authorizeId;
	}

	public String getAuthorizeCode() {
		return authorizeCode;
	}

	public void setAuthorizeCode(String authorizeCode) {
		this.authorizeCode = authorizeCode;
	}

	public String getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}

	public String getLockCode() {
		return lockCode;
	}

	public void setLockCode(String lockCode) {
		this.lockCode = lockCode;
	}

	public String getUnlockPassword() {
		return unlockPassword;
	}

	public void setUnlockPassword(String unlockPassword) {
		this.unlockPassword = unlockPassword;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getUnlockNumber() {
		return unlockNumber;
	}

	public void setUnlockNumber(Integer unlockNumber) {
		this.unlockNumber = unlockNumber;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public Long getCuUserId() {
		return cuUserId;
	}

	public void setCuUserId(Long cuUserId) {
		this.cuUserId = cuUserId;
	}

	public Date getCuTime() {
		return cuTime;
	}

	public void setCuTime(Date cuTime) {
		this.cuTime = cuTime;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getLockName() {
		return lockName;
	}

	public void setLockName(String lockName) {
		this.lockName = lockName;
	}

	public Long getCuUserName() {
		return cuUserName;
	}

	public void setCuUserName(Long cuUserName) {
		this.cuUserName = cuUserName;
	}

	public Long getUnlockPerId() {
		return unlockPerId;
	}

	public void setUnlockPerId(Long unlockPerId) {
		this.unlockPerId = unlockPerId;
	}

	public Integer getLockId() {
		return lockId;
	}

	public void setLockId(Integer lockId) {
		this.lockId = lockId;
	}

	public String getAuthorizeIds() {
		return authorizeIds;
	}

	public void setAuthorizeIds(String authorizeIds) {
		this.authorizeIds = authorizeIds;
	}

	public String getLockIds() {
		return lockIds;
	}

	public void setLockIds(String lockIds) {
		this.lockIds = lockIds;
	}
}
