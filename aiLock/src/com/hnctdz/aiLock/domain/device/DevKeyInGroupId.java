package com.hnctdz.aiLock.domain.device;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * DevKeyInGroupId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class DevKeyInGroupId implements java.io.Serializable {

	// Fields

	private Integer groupId;
	private Integer keyId;

	// Constructors

	/** default constructor */
	public DevKeyInGroupId() {
	}

	/** full constructor */
	public DevKeyInGroupId(Integer groupId, Integer keyId) {
		this.groupId = groupId;
		this.keyId = keyId;
	}

	// Property accessors

	@Column(name = "GROUP_ID", nullable = false)
	public Integer getGroupId() {
		return this.groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	@Column(name = "KEY_ID", nullable = false)
	public Integer getKeyId() {
		return this.keyId;
	}

	public void setKeyId(Integer keyId) {
		this.keyId = keyId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DevKeyInGroupId))
			return false;
		DevKeyInGroupId castOther = (DevKeyInGroupId) other;

		return ((this.getGroupId() == castOther.getGroupId()) || (this
				.getGroupId() != null && castOther.getGroupId() != null && this
				.getGroupId().equals(castOther.getGroupId())))
				&& ((this.getKeyId() == castOther.getKeyId()) || (this
						.getKeyId() != null && castOther.getKeyId() != null && this
						.getKeyId().equals(castOther.getKeyId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getGroupId() == null ? 0 : this.getGroupId().hashCode());
		result = 37 * result
				+ (getKeyId() == null ? 0 : this.getKeyId().hashCode());
		return result;
	}

}