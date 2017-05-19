package com.hnctdz.aiLock.domain.analysis;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * LockStatusRecords entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "lock_status_records")
public class LockStatusRecords implements java.io.Serializable {

	// Fields

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
	
	private String lockName;
	private String orgName;
	private String areaName;

	// Constructors

	/** default constructor */
	public LockStatusRecords() {
	}

	/** minimal constructor */
	public LockStatusRecords(Integer recordId) {
		this.recordId = recordId;
	}

	/** full constructor */
	public LockStatusRecords(Integer recordId, String lockCode,
			String menciStatus, String bufangStatus, String baojingStatus,
			String yuliuStatus, String jixieyaoshiStatus, String xiesheStatus,
			String shangshuoStatus, String menguanhaoStatus,
			String renyikaStatus, Date reportTime) {
		this.recordId = recordId;
		this.lockCode = lockCode;
		this.menciStatus = menciStatus;
		this.bufangStatus = bufangStatus;
		this.baojingStatus = baojingStatus;
		this.yuliuStatus = yuliuStatus;
		this.jixieyaoshiStatus = jixieyaoshiStatus;
		this.xiesheStatus = xiesheStatus;
		this.shangshuoStatus = shangshuoStatus;
		this.menguanhaoStatus = menguanhaoStatus;
		this.renyikaStatus = renyikaStatus;
		this.reportTime = reportTime;
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

	@Column(name = "LOCK_CODE", length = 50)
	public String getLockCode() {
		return this.lockCode;
	}

	@Column(name = "LOCK_IN_MODULE_CODE", length = 50)
	public String getLockInModuleCode() {
		return this.lockInModuleCode;
	}

	public void setLockInModuleCode(String lockInModuleCode) {
		this.lockInModuleCode = lockInModuleCode;
	}
	
	public void setLockCode(String lockCode) {
		this.lockCode = lockCode;
	}
	
	@Column(name = "LOCK_DEVICE_NO", length = 50)
	public String getLockDeviceNo() {
		return this.lockDeviceNo;
	}

	public void setLockDeviceNo(String lockDeviceNo) {
		this.lockDeviceNo = lockDeviceNo;
	}

	@Column(name = "MENCI_STATUS", length = 5)
	public String getMenciStatus() {
		return this.menciStatus;
	}

	public void setMenciStatus(String menciStatus) {
		this.menciStatus = menciStatus;
	}

	@Column(name = "BUFANG_STATUS", length = 5)
	public String getBufangStatus() {
		return this.bufangStatus;
	}

	public void setBufangStatus(String bufangStatus) {
		this.bufangStatus = bufangStatus;
	}

	@Column(name = "BAOJING_STATUS", length = 5)
	public String getBaojingStatus() {
		return this.baojingStatus;
	}

	public void setBaojingStatus(String baojingStatus) {
		this.baojingStatus = baojingStatus;
	}

	@Column(name = "YULIU_STATUS", length = 5)
	public String getYuliuStatus() {
		return this.yuliuStatus;
	}

	public void setYuliuStatus(String yuliuStatus) {
		this.yuliuStatus = yuliuStatus;
	}

	@Column(name = "JIXIEYAOSHI_STATUS", length = 5)
	public String getJixieyaoshiStatus() {
		return this.jixieyaoshiStatus;
	}

	public void setJixieyaoshiStatus(String jixieyaoshiStatus) {
		this.jixieyaoshiStatus = jixieyaoshiStatus;
	}

	@Column(name = "XIESHE_STATUS", length = 5)
	public String getXiesheStatus() {
		return this.xiesheStatus;
	}

	public void setXiesheStatus(String xiesheStatus) {
		this.xiesheStatus = xiesheStatus;
	}

	@Column(name = "SHANGSHUO_STATUS", length = 5)
	public String getShangshuoStatus() {
		return this.shangshuoStatus;
	}

	public void setShangshuoStatus(String shangshuoStatus) {
		this.shangshuoStatus = shangshuoStatus;
	}

	@Column(name = "MENGUANHAO_STATUS", length = 5)
	public String getMenguanhaoStatus() {
		return this.menguanhaoStatus;
	}

	public void setMenguanhaoStatus(String menguanhaoStatus) {
		this.menguanhaoStatus = menguanhaoStatus;
	}

	@Column(name = "RENYIKA_STATUS", length = 5)
	public String getRenyikaStatus() {
		return this.renyikaStatus;
	}

	public void setRenyikaStatus(String renyikaStatus) {
		this.renyikaStatus = renyikaStatus;
	}

	@Column(name = "MESSAGE")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "REPORT_TIME", length = 19)
	public Date getReportTime() {
		return this.reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	@Transient
	public String getLockName() {
		return lockName;
	}

	public void setLockName(String lockName) {
		this.lockName = lockName;
	}

	@Transient
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@Transient
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

}