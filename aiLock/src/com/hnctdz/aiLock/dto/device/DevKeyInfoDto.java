package com.hnctdz.aiLock.dto.device;

import com.google.gson.annotations.Expose;

/**
 * @ClassName DevKeyInfoDto.java
 * @Author WangXiangBo
 */
public class DevKeyInfoDto {
	private Long keyId;
	private String keyCode;
	private String keyName;
	private Long keyType;
	private Long perId;
	private Long orgId;
	private Long status;
	private String note;
	@Expose
	public String blueName;
	public String orgids;
	
	@Expose
	public String groupSecretKey;
	private String orgName;
	private String perName;

	public String getOrgids() {
		return orgids;
	}

	public void setOrgids(String orgids) {
		this.orgids = orgids;
	}
	
	public String getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public Long getKeyType() {
		return keyType;
	}

	public void setKeyType(Long keyType) {
		this.keyType = keyType;
	}

	public Long getPerId() {
		return perId;
	}

	public void setPerId(Long perId) {
		this.perId = perId;
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

	public String getPerName() {
		return perName;
	}

	public void setPerName(String perName) {
		this.perName = perName;
	}

	public String getBlueName() {
		return blueName;
	}

	public void setBlueName(String blueName) {
		this.blueName = blueName;
	}

	public String getGroupSecretKey() {
		return groupSecretKey;
	}

	public void setGroupSecretKey(String groupSecretKey) {
		this.groupSecretKey = groupSecretKey;
	}

	public Long getKeyId() {
		return keyId;
	}

	public void setKeyId(Long keyId) {
		this.keyId = keyId;
	}
}
