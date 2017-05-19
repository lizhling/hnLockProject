package com.hnctdz.aiLock.domain.info;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;

/**
 * PersonnelInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PERSONNEL_INFO")
public class PersonnelInfo implements java.io.Serializable {

	// Fields
	@Expose
	private Long perId;
	private String perAccounts;
	private String perPassword;
	@Expose
	private String perName;
	private String phoneNo;
	private String address;
	private Long orgId;
	private Long status;
	private String note;
	@Expose
	private String smartKeyPerId;
	@Expose
	private Long smartKeyPassw;
	private Date cuTime;
	
	private String orgName;

	// Constructors

	/** default constructor */
	public PersonnelInfo() {
	}

	/** minimal constructor */
	public PersonnelInfo(Long perId) {
		this.perId = perId;
	}

	/** full constructor */
	public PersonnelInfo(Long perId, String perAccounts, String perPassword,
			String perName, String phoneNo, String address, Long orgId,
			Long status, String note) {
		this.perId = perId;
		this.perAccounts = perAccounts;
		this.perPassword = perPassword;
		this.perName = perName;
		this.phoneNo = phoneNo;
		this.address = address;
		this.orgId = orgId;
		this.status = status;
		this.note = note;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "PER_ID", unique = true, nullable = false)
	public Long getPerId() {
		return this.perId;
	}

	public void setPerId(Long perId) {
		this.perId = perId;
	}

	@Column(name = "PER_ACCOUNTS", length = 20)
	public String getPerAccounts() {
		return this.perAccounts;
	}

	public void setPerAccounts(String perAccounts) {
		this.perAccounts = perAccounts;
	}

	@Column(name = "PER_PASSWORD", length = 50)
	public String getPerPassword() {
		return this.perPassword;
	}

	public void setPerPassword(String perPassword) {
		this.perPassword = perPassword;
	}

	@Column(name = "PER_NAME", length = 50)
	public String getPerName() {
		return this.perName;
	}

	public void setPerName(String perName) {
		this.perName = perName;
	}

	@Column(name = "PHONE_NO", length = 20)
	public String getPhoneNo() {
		return this.phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	@Column(name = "ADDRESS", length = 200)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "ORG_ID", precision = 10, scale = 0)
	public Long getOrgId() {
		return this.orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	@Column(name = "STATUS", precision = 1, scale = 0)
	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	@Column(name = "NOTE", length = 100)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	@Column(name = "SMART_KEY_PER_ID", length = 10)
	public String getSmartKeyPerId() {
		return smartKeyPerId;
	}

	public void setSmartKeyPerId(String smartKeyPerId) {
		this.smartKeyPerId = smartKeyPerId;
	}

	@Column(name = "SMART_KEY_PASSW", precision = 10, scale = 0)
	public Long getSmartKeyPassw() {
		return smartKeyPassw;
	}

	public void setSmartKeyPassw(Long smartKeyPassw) {
		this.smartKeyPassw = smartKeyPassw;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CU_TIME", length = 7)
	public Date getCuTime() {
		return this.cuTime;
	}

	public void setCuTime(Date cuTime) {
		this.cuTime = cuTime;
	}

	@Transient
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
}