package com.hnctdz.aiLock.domain.device;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * DevKeyInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "DEV_KEY_INFO")
public class DevKeyInfo implements java.io.Serializable {

	// Fields
	private Long keyId;
	private String keyCode;
	private String keyName;
	private Long keyType;
	private Long perId;
	private Long orgId;
	private Long status;
	private String note;
	private Long groupId;
	private Integer lockingTime;
	private String phoneImei;
	private String blueName;
	
	private String orgName;
	private String perName;

	// Constructors

	/** default constructor */
	public DevKeyInfo() {
	}

	/** minimal constructor */
	public DevKeyInfo(String keyCode) {
		this.keyCode = keyCode;
	}

	/** full constructor */
	public DevKeyInfo(Long keyId, String keyCode, String keyName, Long keyType,
			Long perId, Long orgId, Long status, String note) {
		this.keyId = keyId;
		this.keyCode = keyCode;
		this.keyName = keyName;
		this.keyType = keyType;
		this.perId = perId;
		this.orgId = orgId;
		this.status = status;
		this.note = note;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "KEY_ID", unique = true, nullable = false)
	public Long getKeyId() {
		return keyId;
	}

	public void setKeyId(Long keyId) {
		this.keyId = keyId;
	}

	@Column(name = "KEY_CODE", length = 50)
	public String getKeyCode() {
		return this.keyCode;
	}

	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}

	@Column(name = "KEY_NAME", length = 100)
	public String getKeyName() {
		return this.keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	@Column(name = "KEY_TYPE", precision = 1, scale = 0)
	public Long getKeyType() {
		return this.keyType;
	}

	public void setKeyType(Long keyType) {
		this.keyType = keyType;
	}

	@Column(name = "PER_ID", precision = 10, scale = 0)
	public Long getPerId() {
		return this.perId;
	}

	public void setPerId(Long perId) {
		this.perId = perId;
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

	@Column(name = "NOTE", length = 200)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	@Column(name = "GROUP_ID", precision = 10, scale = 0)
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
	@Column(name = "LOCKING_TIME", precision = 5)
	public Integer getLockingTime() {
		return lockingTime;
	}

	public void setLockingTime(Integer lockingTime) {
		this.lockingTime = lockingTime;
	}

	@Column(name = "PHONE_IMEI", length = 30)
	public String getPhoneImei() {
		return phoneImei;
	}

	public void setPhoneImei(String phoneImei) {
		this.phoneImei = phoneImei;
	}

	@Column(name = "BLUE_NAME", length = 30)
	public String getBlueName() {
		return blueName;
	}

	public void setBlueName(String blueName) {
		this.blueName = blueName;
	}

	@Transient
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	@Transient
	public String getPerName() {
		return perName;
	}

	public void setPerName(String perName) {
		this.perName = perName;
	}


}