package com.hnctdz.aiLock.dto.analysis;

import java.util.Date;

/**
 * @ClassName LockStatusRecordsDto.java
 * @Author WangXiangBo
 */
public class LockStatusRecordsDto {
	private Integer recordId;
	private String lockCode;
	private String lockInModuleCode;
	private String lockDeviceNo;
	private String menciStatus;
	private String bufangStatus;
	private String baojingStatus;
	private String yuliuStatus;
	private String jixieyaoshiStatus;
	private String xiesheStatus;
	private String shangshuoStatus;
	private String menguanhaoStatus;
	private String renyikaStatus;
	private String message;
	private Date reportTime;
	
	private Long orgId;
	private String lockName;
	private Long areaId;
	private int queryType;

	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public String getLockCode() {
		return lockCode;
	}

	public void setLockCode(String lockCode) {
		this.lockCode = lockCode;
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

	public String getMenciStatus() {
		return menciStatus;
	}

	public void setMenciStatus(String menciStatus) {
		this.menciStatus = menciStatus;
	}

	public String getBufangStatus() {
		return bufangStatus;
	}

	public void setBufangStatus(String bufangStatus) {
		this.bufangStatus = bufangStatus;
	}

	public String getBaojingStatus() {
		return baojingStatus;
	}

	public void setBaojingStatus(String baojingStatus) {
		this.baojingStatus = baojingStatus;
	}

	public String getYuliuStatus() {
		return yuliuStatus;
	}

	public void setYuliuStatus(String yuliuStatus) {
		this.yuliuStatus = yuliuStatus;
	}

	public String getJixieyaoshiStatus() {
		return jixieyaoshiStatus;
	}

	public void setJixieyaoshiStatus(String jixieyaoshiStatus) {
		this.jixieyaoshiStatus = jixieyaoshiStatus;
	}

	public String getXiesheStatus() {
		return xiesheStatus;
	}

	public void setXiesheStatus(String xiesheStatus) {
		this.xiesheStatus = xiesheStatus;
	}

	public String getShangshuoStatus() {
		return shangshuoStatus;
	}

	public void setShangshuoStatus(String shangshuoStatus) {
		this.shangshuoStatus = shangshuoStatus;
	}

	public String getMenguanhaoStatus() {
		return menguanhaoStatus;
	}

	public void setMenguanhaoStatus(String menguanhaoStatus) {
		this.menguanhaoStatus = menguanhaoStatus;
	}

	public String getRenyikaStatus() {
		return renyikaStatus;
	}

	public void setRenyikaStatus(String renyikaStatus) {
		this.renyikaStatus = renyikaStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public String getLockName() {
		return lockName;
	}

	public void setLockName(String lockName) {
		this.lockName = lockName;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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
