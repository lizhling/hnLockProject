package com.hnctdz.aiLock.domain.system;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SysRoleResId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class SysRoleResId implements java.io.Serializable {

	// Fields

	private Long resId;
	private Long roleId;

	// Constructors

	/** default constructor */
	public SysRoleResId() {
	}

	/** full constructor */
	public SysRoleResId(Long resId, Long roleId) {
		this.resId = resId;
		this.roleId = roleId;
	}

	// Property accessors

	@Column(name = "RES_ID", nullable = false, precision = 10, scale = 0)
	public Long getResId() {
		return this.resId;
	}

	public void setResId(Long resId) {
		this.resId = resId;
	}

	@Column(name = "ROLE_ID", nullable = false, precision = 10, scale = 0)
	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SysRoleResId))
			return false;
		SysRoleResId castOther = (SysRoleResId) other;

		return ((this.getResId() == castOther.getResId()) || (this.getResId() != null
				&& castOther.getResId() != null && this.getResId().equals(
				castOther.getResId())))
				&& ((this.getRoleId() == castOther.getRoleId()) || (this
						.getRoleId() != null && castOther.getRoleId() != null && this
						.getRoleId().equals(castOther.getRoleId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getResId() == null ? 0 : this.getResId().hashCode());
		result = 37 * result
				+ (getRoleId() == null ? 0 : this.getRoleId().hashCode());
		return result;
	}

}