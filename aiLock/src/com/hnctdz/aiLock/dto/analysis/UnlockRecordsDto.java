package com.hnctdz.aiLock.dto.analysis;

import java.util.Date;

/**
 * @ClassName UnlockRecordsDto.java
 * @Author WangXiangBo
 */
public class UnlockRecordsDto {
	private Integer recordId;
	private Integer lockType;
	private String recordCode;
	private String recordTpye;
	private String keyCode;
	private String lockInModuleCode;
	private String lockDeviceNo;
	private String lockCode;
	private String smartKeyPerId;
	private Long unlockPerId;
	private String unlockTime;
	private String closeLockTime;
	private Long perId;
	private Date uploadTime;
	private Long userId;
	private Date confirmTime;
	private String note;
	private String message;
	
	private String recordIds;
	private String unlockPerName;
	private String keyName;
	private String lockName;
	private Long orgId;
	private Integer selectType;
	private String unStartTime;
	private String unEndTime;
	private String alarmLevel;
	private String ifDeal;
	private Long areaId;
	private int queryType;

	public String getRecordTpye() {
		return recordTpye;
	}

	public void setRecordTpye(String recordTpye) {
		this.recordTpye = recordTpye;
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

	public Long getPerId() {
		return perId;
	}

	public void setPerId(Long perId) {
		this.perId = perId;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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

	public String getRecordCode() {
		return recordCode;
	}

	public void setRecordCode(String recordCode) {
		this.recordCode = recordCode;
	}

	public String getLockInModuleCode() {
		return lockInModuleCode;
	}

	public void setLockInModuleCode(String lockInModuleCode) {
		this.lockInModuleCode = lockInModuleCode;
	}

	public String getLockDeviceNo() {
		return lockDeviceNo;
	}

	public void setLockDeviceNo(String lockDeviceNo) {
		this.lockDeviceNo = lockDeviceNo;
	}

	public String getSmartKeyPerId() {
		return smartKeyPerId;
	}

	public void setSmartKeyPerId(String smartKeyPerId) {
		this.smartKeyPerId = smartKeyPerId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUnlockPerName() {
		return unlockPerName;
	}

	public void setUnlockPerName(String unlockPerName) {
		this.unlockPerName = unlockPerName;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public void setUnlockTime(String unlockTime) {
		this.unlockTime = unlockTime;
	}

	public void setCloseLockTime(String closeLockTime) {
		this.closeLockTime = closeLockTime;
	}

	public Integer getRecordId() {
		return recordId;
	}

	public String getUnlockTime() {
		return unlockTime;
	}

	public String getCloseLockTime() {
		return closeLockTime;
	}

	public Integer getSelectType() {
		return selectType;
	}

	public void setSelectType(Integer selectType) {
		this.selectType = selectType;
	}

	public String getUnStartTime() {
		return unStartTime;
	}

	public void setUnStartTime(String unStartTime) {
		this.unStartTime = unStartTime;
	}

	public String getUnEndTime() {
		return unEndTime;
	}

	public void setUnEndTime(String unEndTime) {
		this.unEndTime = unEndTime;
	}

	public Long getUnlockPerId() {
		return unlockPerId;
	}

	public void setUnlockPerId(Long unlockPerId) {
		this.unlockPerId = unlockPerId;
	}

	public String getRecordIds() {
		return recordIds;
	}

	public void setRecordIds(String recordIds) {
		this.recordIds = recordIds;
	}

	public String getAlarmLevel() {
		return alarmLevel;
	}

	public void setAlarmLevel(String alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public String getIfDeal() {
		return ifDeal;
	}

	public void setIfDeal(String ifDeal) {
		this.ifDeal = ifDeal;
	}

	public int getQueryType() {
		return queryType;
	}

	public void setQueryType(int queryType) {
		this.queryType = queryType;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}


}
