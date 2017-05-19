package com.hnctdz.aiLock.domain.system;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * SysRole entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_ROLE")
public class SysRole implements java.io.Serializable {

	// Fields

	private Long roleId;
	private String roleName;
	private Long status;
	private String note;

	// Constructors

	/** default constructor */
	public SysRole() {
	}

	/** minimal constructor */
	public SysRole(Long roleId) {
		this.roleId = roleId;
	}

	/** full constructor */
	public SysRole(Long roleId, String roleName, Long status, String note) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.status = status;
		this.note = note;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ROLE_ID", unique = true, nullable = false)
	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Column(name = "ROLE_NAME", length = 50)
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "STATUS", precision = 1, scale = 0)
	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	@Column(name = "NOTE", length = 100)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}