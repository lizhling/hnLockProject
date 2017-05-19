/*
 * FileName:     DevKeyGroupDto.java
 * @Description: 
 * Copyright (c) 2015 Sunrise Corporation.
 * #368, Guangzhou Avenue South, Haizhu District, Guangzhou
 * All right reserved.
 *
 * Modification  History:
 * Date           Author         Version         Discription
 * -----------------------------------------------------------------------------------
 * 2015-7-15   WangXiangBo        1.0              新建
 */
package com.hnctdz.aiLock.dto.device;

/**
 * @ClassName DevKeyGroupDto.java
 * @Author WangXiangBo
 * @Date 2015-7-15 下午03:49:26
 */
public class DevKeyGroupDto {
	private Long groupId;
	private String groupName;
	private String groupSecretKey;
	private Long orgId;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupSecretKey() {
		return groupSecretKey;
	}

	public void setGroupSecretKey(String groupSecretKey) {
		this.groupSecretKey = groupSecretKey;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

}
