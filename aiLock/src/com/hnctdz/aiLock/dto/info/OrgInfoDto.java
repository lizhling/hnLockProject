package com.hnctdz.aiLock.dto.info;

/**
 * @ClassName OrgInfoDto.java
 * @Author WangXiangBo
 */
public class OrgInfoDto {
	private Long orgId;
	private String orgName;
	private Long orgParentId;
	private String manager;
	private String phoneNo;
	private String note;

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Long getOrgParentId() {
		return orgParentId;
	}

	public void setOrgParentId(Long orgParentId) {
		this.orgParentId = orgParentId;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
