package com.hnctdz.aiLock.domain.device;

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
 * DevGroup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "DEV_GROUP")
public class DevGroup implements java.io.Serializable {

	// Fields

	private Long groupId;
	private String groupName;
	private String note;
	
	private String lockIds;

	// Constructors

	/** default constructor */
	public DevGroup() {
	}

	/** minimal constructor */
	public DevGroup(Long groupId) {
		this.groupId = groupId;
	}

	/** full constructor */
	public DevGroup(Long groupId, String groupName, String note) {
		this.groupId = groupId;
		this.groupName = groupName;
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

	@Column(name = "GROUP_NAME", length = 100)
	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Column(name = "NOTE", length = 100)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Transient
	public String getLockIds() {
		return lockIds;
	}

	public void setLockIds(String lockIds) {
		this.lockIds = lockIds;
	}

}