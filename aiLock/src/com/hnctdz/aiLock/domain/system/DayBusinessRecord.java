package com.hnctdz.aiLock.domain.system;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * DayBusinessRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "DAY_BUSINESS_RECORD")
public class DayBusinessRecord implements java.io.Serializable {

	// Fields

	private String recordId;
	private String businessId;
	private String interfaceName;
	private String wornServCode;
	private String modeCode;
	private String operateType;
	private String otherParams;
	private String phoneNo;
	private Date transactDate;
	private String transactTime;
	private String transactResult;

	// Constructors

	/** default constructor */
	public DayBusinessRecord() {
	}

	/** minimal constructor */
	public DayBusinessRecord(String recordId) {
		this.recordId = recordId;
	}

	/** full constructor */
	public DayBusinessRecord(String recordId, String businessId,
			String interfaceName, String wornServCode, String modeCode,
			String operateType, String otherParams, String phoneNo,
			Date transactDate, String transactTime, String transactResult) {
		this.recordId = recordId;
		this.businessId = businessId;
		this.interfaceName = interfaceName;
		this.wornServCode = wornServCode;
		this.modeCode = modeCode;
		this.operateType = operateType;
		this.otherParams = otherParams;
		this.phoneNo = phoneNo;
		this.transactDate = transactDate;
		this.transactTime = transactTime;
		this.transactResult = transactResult;
	}

	// Property accessors
	@Id
	@Column(name = "RECORD_ID", unique = true, nullable = false, length = 50)
	public String getRecordId() {
		return this.recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	@Column(name = "BUSINESS_ID", length = 10)
	public String getBusinessId() {
		return this.businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	@Column(name = "INTERFACE_NAME", length = 1000)
	public String getInterfaceName() {
		return this.interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	@Column(name = "WORN_SERV_CODE", length = 100)
	public String getWornServCode() {
		return this.wornServCode;
	}

	public void setWornServCode(String wornServCode) {
		this.wornServCode = wornServCode;
	}

	@Column(name = "MODE_CODE", length = 100)
	public String getModeCode() {
		return this.modeCode;
	}

	public void setModeCode(String modeCode) {
		this.modeCode = modeCode;
	}

	@Column(name = "OPERATE_TYPE", length = 10)
	public String getOperateType() {
		return this.operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	@Column(name = "OTHER_PARAMS")
	public String getOtherParams() {
		return this.otherParams;
	}

	public void setOtherParams(String otherParams) {
		this.otherParams = otherParams;
	}

	@Column(name = "PHONE_NO", length = 15)
	public String getPhoneNo() {
		return this.phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "TRANSACT_DATE", length = 7)
	public Date getTransactDate() {
		return this.transactDate;
	}

	public void setTransactDate(Date transactDate) {
		this.transactDate = transactDate;
	}

	@Column(name = "TRANSACT_TIME", length = 10)
	public String getTransactTime() {
		return this.transactTime;
	}

	public void setTransactTime(String transactTime) {
		this.transactTime = transactTime;
	}

	@Column(name = "TRANSACT_RESULT", length = 20)
	public String getTransactResult() {
		return this.transactResult;
	}

	public void setTransactResult(String transactResult) {
		this.transactResult = transactResult;
	}

}