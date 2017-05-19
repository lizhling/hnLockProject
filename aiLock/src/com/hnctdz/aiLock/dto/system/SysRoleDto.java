package com.hnctdz.aiLock.dto.system;

/**
 * @ClassName SysRoleDto.java
 * @Author WangXiangBo
 */
public class SysRoleDto {
	
	private Long roleId;
	private String roleName;
	private Long status;
	private String note;
	
	private String resIds;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getResIds() {
		return resIds;
	}

	public void setResIds(String resIds) {
		this.resIds = resIds;
	}

}
