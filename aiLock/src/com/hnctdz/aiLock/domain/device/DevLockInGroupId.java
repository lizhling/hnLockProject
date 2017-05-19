package com.hnctdz.aiLock.domain.device;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * DevLockInGroupId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class DevLockInGroupId implements java.io.Serializable {

	// Fields

	private Long groupId;
	private Long lockId;

	// Constructors

	/** default constructor */
	public DevLockInGroupId() {
	}

	/** full constructor */
	public DevLockInGroupId(Long groupId, Long lockId) {
		this.groupId = groupId;
		this.lockId = lockId;
	}

	// Property accessors

	@Column(name = "GROUP_ID", nullable = false, precision = 10, scale = 0)
	public Long getGroupId() {
		return this.groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Column(name = "LOCK_ID", nullable = false, precision = 10, scale = 0)
	public Long getLockId() {
		return lockId;
	}

	public void setLockId(Long lockId) {
		this.lockId = lockId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DevLockInGroupId))
			return false;
		DevLockInGroupId castOther = (DevLockInGroupId) other;

		return ((this.getGroupId() == castOther.getGroupId()) || (this
				.getGroupId() != null && castOther.getGroupId() != null && this
				.getGroupId().equals(castOther.getGroupId())))
				&& ((this.getLockId() == castOther.getLockId()) || (this
						.getLockId() != null
						&& castOther.getLockId() != null && this
						.getLockId().equals(castOther.getLockId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getGroupId() == null ? 0 : this.getGroupId().hashCode());
		result = 37 * result
				+ (getLockId() == null ? 0 : this.getLockId().hashCode());
		return result;
	}

}