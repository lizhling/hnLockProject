package com.hnctdz.aiLock.domain.system;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hnctdz.aiLock.domain.system.SysRole;
import com.hnctdz.aiLock.domain.system.SysUser;

/**
 * SysUserRole entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_USER_ROLE")
public class SysUserRole implements java.io.Serializable {

	// Fields

	private SysUserRoleId id;
	private SysRole sysRole;
	private SysUser sysUser;
	
	// Constructors

	/** default constructor */
	public SysUserRole() {
	}

	/** full constructor */
	public SysUserRole(SysUserRoleId id) {
		this.id = id;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "userId", column = @Column(name = "USER_ID", nullable = false, precision = 10, scale = 0)),
			@AttributeOverride(name = "roleId", column = @Column(name = "ROLE_ID", nullable = false, precision = 10, scale = 0)) })
	public SysUserRoleId getId() {
		return this.id;
	}

	public void setId(SysUserRoleId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_ID", nullable = false, insertable = false, updatable = false)
	public SysRole getSysRole() {
		return this.sysRole;
	}

	public void setSysRole(SysRole sysRole) {
		this.sysRole = sysRole;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = false, insertable = false, updatable = false)
	public SysUser getSysUser() {
		return this.sysUser;
	}

	public void setSysUser(SysUser frwSysUser) {
		this.sysUser = frwSysUser;
	}
}