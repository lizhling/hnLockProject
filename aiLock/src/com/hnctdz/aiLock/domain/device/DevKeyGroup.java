package com.hnctdz.aiLock.domain.device;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

/**
 * DevKeyGroup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "dev_key_group")
public class DevKeyGroup implements java.io.Serializable {

	// Fields
	@Expose
	private Long groupId;
	@Expose
	private String groupName;
	private String groupSecretKey;
	private Long orgId;
	private String note;

	// Constructors

	/** default constructor */
	public DevKeyGroup() {
	}

	/** minimal constructor */
	public DevKeyGroup(String groupSecretKey) {
		this.groupSecretKey = groupSecretKey;
	}

	/** full constructor */
	public DevKeyGroup(String groupName, String groupSecretKey, Long orgId, String note) {
		this.groupName = groupName;
		this.groupSecretKey = groupSecretKey;
		this.orgId = orgId;
		this.note = note;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "GROUP_ID", unique = true, nullable = false)
	public Long getGroupId() {
		return this.groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Column(name = "GROUP_NAME")
	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Column(name = "GROUP_SECRET_KEY", nullable = false)
	public String getGroupSecretKey() {
		return this.groupSecretKey;
	}

	public void setGroupSecretKey(String groupSecretKey) {
		this.groupSecretKey = groupSecretKey;
	}

	@Column(name = "ORG_ID")
	public Long getOrgId() {
		return this.orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	@Column(name = "NOTE", length = 100)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}