package com.hnctdz.aiLock.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.hnctdz.aiLock.domain.system.SysUser;


public class SysUserDetail implements java.io.Serializable, UserDetails{
	
	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	private SysUser sysUser;
	
	private String userName;
	
	private String password;
	
	/**
	 * @param sysUser
	 */
	public SysUserDetail(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public Collection<GrantedAuthority> getAuthorities() {
		return sysUser.getAuthorities();
	}

	public String getPassword() {
		return this.password;
	}

	public String getUsername() {
		return userName;
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}

	public SysUser getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
