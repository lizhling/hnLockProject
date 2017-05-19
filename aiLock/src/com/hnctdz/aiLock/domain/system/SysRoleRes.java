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
import com.hnctdz.aiLock.domain.system.SysRes;


/**
 * SysRoleRes entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_ROLE_RES")
public class SysRoleRes implements java.io.Serializable {

	// Fields

	private SysRoleResId id;
	private SysRole frwSysRole;
	private SysRes frwSysRes;

	// Constructors

	/** default constructor */
	public SysRoleRes() {
	}

	/** full constructor */
	public SysRoleRes(SysRoleResId id) {
		this.id = id;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "resId", column = @Column(name = "RES_ID", nullable = false, precision = 10, scale = 0)),
			@AttributeOverride(name = "roleId", column = @Column(name = "ROLE_ID", nullable = false, precision = 10, scale = 0)) })
	public SysRoleResId getId() {
		return this.id;
	}

	public void setId(SysRoleResId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ROLE_ID", nullable = false, insertable = false, updatable = false)
	public SysRole getFrwSysRole() {
		return this.frwSysRole;
	}

	public void setFrwSysRole(SysRole frwSysRole) {
		this.frwSysRole = frwSysRole;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "RES_ID", nullable = false, insertable = false, updatable = false)
	public SysRes getFrwSysRes() {
		return this.frwSysRes;
	}

	public void setFrwSysRes(SysRes frwSysRes) {
		this.frwSysRes = frwSysRes;
	}

}