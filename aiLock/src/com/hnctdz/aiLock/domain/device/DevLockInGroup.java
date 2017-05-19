package com.hnctdz.aiLock.domain.device;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * DevLockInGroup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "DEV_LOCK_IN_GROUP")
public class DevLockInGroup implements java.io.Serializable {

	// Fields

	private DevLockInGroupId id;

	// Constructors

	/** default constructor */
	public DevLockInGroup() {
	}

	/** full constructor */
	public DevLockInGroup(DevLockInGroupId id) {
		this.id = id;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "groupId", column = @Column(name = "GROUP_ID", nullable = false, precision = 10, scale = 0)),
			@AttributeOverride(name = "lockCode", column = @Column(name = "LOCK_CODE", nullable = false, length = 50)) })
	public DevLockInGroupId getId() {
		return this.id;
	}

	public void setId(DevLockInGroupId id) {
		this.id = id;
	}

}