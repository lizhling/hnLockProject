package com.hnctdz.aiLock.dto.info;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
 * @ClassName PersonnelInfoDto.java
 * @Author WangXiangBo 
 */
public class PersonnelInfoDto {
	private Long perId;
	private String perAccounts;
	private String perPassword;
	private String perName;
	private String phoneNo;
	private String address;
	private Long orgId;
	private Long status;
	private String note;
	private String smartKeyPerId;
	private Long smartKeyPassw;
	
	private String orgName;
	
	private String orgIds;
	
	private String perIds;
	
	private String importType;

	
	
	public String getPerIds() {
		return perIds;
	}

	public void setPerIds(String perIds) {
		this.perIds = perIds;
	}

	public String getImportType() {
		return importType;
	}

	public void setImportType(String importType) {
		this.importType = importType;
	}

	public String getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}

	public Long getPerId() {
		return perId;
	}

	public void setPerId(Long perId) {
		this.perId = perId;
	}

	public String getPerAccounts() {
		return perAccounts;
	}

	public void setPerAccounts(String perAccounts) {
		this.perAccounts = perAccounts;
	}

	public String getPerPassword() {
		return perPassword;
	}

	public void setPerPassword(String perPassword) {
		this.perPassword = perPassword;
	}

	public String getPerName() {
		return perName;
	}

	public void setPerName(String perName) {
		this.perName = perName;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getSmartKeyPerId() {
		return smartKeyPerId;
	}

	public void setSmartKeyPerId(String smartKeyPerId) {
		this.smartKeyPerId = smartKeyPerId;
	}

	public Long getSmartKeyPassw() {
		return smartKeyPassw;
	}

	public void setSmartKeyPassw(Long smartKeyPassw) {
		this.smartKeyPassw = smartKeyPassw;
	}

}
