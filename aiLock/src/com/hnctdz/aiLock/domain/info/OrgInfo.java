package com.hnctdz.aiLock.domain.info;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * OrgInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ORG_INFO")
public class OrgInfo implements java.io.Serializable {

	// Fields

	private Long orgId;
	private String orgName;
	private Long orgParentId;
	private Long userId;
	private String note;
	
	private String orgParentName;
	private String userName;

	// Constructors

	/** default constructor */
	public OrgInfo() {
	}

	/** minimal constructor */
	public OrgInfo(Long orgId) {
		this.orgId = orgId;
	}

	/** full constructor */
	public OrgInfo(Long orgId, String orgName, 
			Long orgParentId, Long userId, String phoneNo, String note) {
		this.orgId = orgId;
		this.orgName = orgName;
		this.orgParentId = orgParentId;
		this.userId = userId;
		this.note = note;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ORG_ID", unique = true, nullable = false)
	public Long getOrgId() {
		return this.orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	@Column(name = "ORG_NAME", length = 100)
	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@Column(name = "ORG_PARENT_ID", precision = 10, scale = 0)
	public Long getOrgParentId() {
		return this.orgParentId;
	}

	public void setOrgParentId(Long orgParentId) {
		this.orgParentId = orgParentId;
	}

	@Column(name = "USER_ID", precision = 10, scale = 0)
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "NOTE", length = 100)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Transient
	public String getOrgParentName() {
		return orgParentName;
	}

	public void setOrgParentName(String orgParentName) {
		this.orgParentName = orgParentName;
	}

	@Transient
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}