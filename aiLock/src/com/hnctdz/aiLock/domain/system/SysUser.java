package com.hnctdz.aiLock.domain.system;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

/**
 * SysUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_USER")
public class SysUser implements java.io.Serializable {

	// Fields

	private Long userId;
	private String userName;
	private String password;
	private String name;
	private String phoneNo;
	private Long status;
	private String note;
	private Long areaId;
	private Long orgId;
	
	private String roleId;
	private String roleName;
	private String orgName;
	private String orgPermissionIds;
	private String areaPermissionIds;
	
	/** 保存该用户对应的roleId */
	private List<Long> userInRoleIds = new ArrayList<Long>();

	// Constructors

	/** default constructor */
	public SysUser() {
	}

	/** minimal constructor */
	public SysUser(Long userId) {
		this.userId = userId;
	}

	/** full constructor */
	public SysUser(Long userId, String userName, String password, String name,
			String phoneNo, Long status, String note, Long orgId) {
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.name = name;
		this.phoneNo = phoneNo;
		this.status = status;
		this.note = note;
		this.orgId = orgId;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "USER_ID", unique = true, nullable = false)
	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "USER_NAME", length = 20)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "PASSWORD", length = 50)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "NAME", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "PHONE_NO", length = 20)
	public String getPhoneNo() {
		return this.phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
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

	@Column(name = "ORG_ID", precision = 10, scale = 0)
	public Long getOrgId() {
		return this.orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	@Column(name = "AREA_ID", precision = 10, scale = 0)
	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	
	//-----实现UserDetails的接口，通过spring security对用户权限进行管理
	/*
	 * 返回RoleId，可以根据这个ID查找对应的资源
	 * 
	 * @see
	 * org.springframework.security.userdetails.UserDetails#getAuthorities()
	 */
	@Transient
	public Collection<GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		//添加用户对对应的RoleId
		for (Long userInRoleId : getUserInRoleIds()) {
			grantedAuthorities.add(new GrantedAuthorityImpl(userInRoleId.toString()));
		}
		//添加用户对对应的组织架构的RoleId
//		for (Long userInOrgRoleId : getUserInOrgRoleIds()) {
//			grantedAuthorities.add(new GrantedAuthorityImpl(userInOrgRoleId.toString()));
//		}
		//默认添加一个操作ACL的权限,以跳过spring security内置的ACL操作检查,本系统的操作检查由URL过滤进行控制。
		grantedAuthorities.add(new GrantedAuthorityImpl("4000"));
		
		return grantedAuthorities;
	}
	
	/**
	 * @return the userInRoleIds
	 */
	@Transient
	public List<Long> getUserInRoleIds() {
		return userInRoleIds;
	}
	
	/**
	 * @param userInRoleIds the userInRoleIds to set
	 */
	@Transient
	public void setUserInRoleIds(List<Long> userInRoleIds) {
		this.userInRoleIds = userInRoleIds;
	}
	
	@Transient
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	@Transient
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Transient
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@Transient
	public String getOrgPermissionIds() {
		return orgPermissionIds;
	}

	public void setOrgPermissionIds(String orgPermissionIds) {
		this.orgPermissionIds = orgPermissionIds;
	}

	@Transient
	public String getAreaPermissionIds() {
		return areaPermissionIds;
	}

	public void setAreaPermissionIds(String areaPermissionIds) {
		this.areaPermissionIds = areaPermissionIds;
	}

}