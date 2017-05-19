package com.hnctdz.aiLock.dto.device;

import java.math.BigDecimal;
import java.util.Date;

import com.google.gson.annotations.Expose;

/**
 * @ClassName DevLockInfoDto.java
 * @Author WangXiangBo
 */
public class DevLockInfoDto {
	@Expose
	public Integer lockId;
	@Expose
	public String lockCode;
	@Expose
	public String lockName;
	@Expose
	public Integer lockType;
	@Expose
	public String lockAddres;
	@Expose
	public BigDecimal longitude;
	@Expose
	public BigDecimal latitude;
	public Long areaId;
	public Long orgId;
	@Expose
	public String status;
	public String note;
	public Long unlockPerId;
	public String lockDeviceNo;
	@Expose
	public String vicePassiveLockCode;
	
	
	@Expose
	public Date startTime;
	@Expose
	public Date endTime;
	@Expose
	public Integer blueUnlock;
	@Expose
	public Integer scopeUnlock;
	@Expose
	public Integer authorizeType;
	@Expose
	public String lockInBlueCode;
	
	public String orgName;
	public String areaName;
	public String lockIds;
	public Integer isVicePassive;
	public Integer isBlueConfig;
	public String areaIds;
	

	public String getAreaIds() {
		return areaIds;
	}

	public void setAreaIds(String areaIds) {
		this.areaIds = areaIds;
	}

	public String getLockCode() {
		return lockCode;
	}

	public void setLockCode(String lockCode) {
		this.lockCode = lockCode;
	}

	public String getLockName() {
		return lockName;
	}

	public void setLockName(String lockName) {
		this.lockName = lockName;
	}

	public Integer getLockType() {
		return lockType;
	}

	public void setLockType(Integer lockType) {
		this.lockType = lockType;
	}

	public String getLockAddres() {
		return lockAddres;
	}

	public void setLockAddres(String lockAddres) {
		this.lockAddres = lockAddres;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Integer getLockId() {
		return lockId;
	}

	public void setLockId(Integer lockId) {
		this.lockId = lockId;
	}

	public Long getUnlockPerId() {
		return unlockPerId;
	}

	public void setUnlockPerId(Long unlockPerId) {
		this.unlockPerId = unlockPerId;
	}

	public String getLockDeviceNo() {
		return lockDeviceNo;
	}

	public void setLockDeviceNo(String lockDeviceNo) {
		this.lockDeviceNo = lockDeviceNo;
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

	public Integer getBlueUnlock() {
		return blueUnlock;
	}

	public void setBlueUnlock(Integer blueUnlock) {
		this.blueUnlock = blueUnlock;
	}

	public Integer getAuthorizeType() {
		return authorizeType;
	}

	public void setAuthorizeType(Integer authorizeType) {
		this.authorizeType = authorizeType;
	}

	public Integer getScopeUnlock() {
		return scopeUnlock;
	}

	public void setScopeUnlock(Integer scopeUnlock) {
		this.scopeUnlock = scopeUnlock;
	}

	public String getLockInBlueCode() {
		return lockInBlueCode;
	}

	public void setLockInBlueCode(String lockInBlueCode) {
		this.lockInBlueCode = lockInBlueCode;
	}

	public String getVicePassiveLockCode() {
		return vicePassiveLockCode;
	}

	public void setVicePassiveLockCode(String vicePassiveLockCode) {
		this.vicePassiveLockCode = vicePassiveLockCode;
	}

	public String getLockIds() {
		return lockIds;
	}

	public void setLockIds(String lockIds) {
		this.lockIds = lockIds;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public Integer getIsVicePassive() {
		return isVicePassive;
	}

	public void setIsVicePassive(Integer isVicePassive) {
		this.isVicePassive = isVicePassive;
	}

	public Integer getIsBlueConfig() {
		return isBlueConfig;
	}

	public void setIsBlueConfig(Integer isBlueConfig) {
		this.isBlueConfig = isBlueConfig;
	}

}
