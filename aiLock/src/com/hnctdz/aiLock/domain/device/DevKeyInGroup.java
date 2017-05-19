package com.hnctdz.aiLock.domain.device;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * DevKeyInGroup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "dev_key_in_group")
public class DevKeyInGroup implements java.io.Serializable {

	// Fields

	private DevKeyInGroupId id;

	// Constructors

	/** default constructor */
	public DevKeyInGroup() {
	}

	/** full constructor */
	public DevKeyInGroup(DevKeyInGroupId id) {
		this.id = id;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "groupId", column = @Column(name = "GROUP_ID", nullable = false)),
			@AttributeOverride(name = "keyId", column = @Column(name = "KEY_ID", nullable = false)) })
	public DevKeyInGroupId getId() {
		return this.id;
	}

	public void setId(DevKeyInGroupId id) {
		this.id = id;
	}

}