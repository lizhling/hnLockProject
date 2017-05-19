/*
 * FileName:     DevLockInGroupDto.java
 * @Description: 
 * Copyright (c) 2015 Sunrise Corporation.
 * #368, Guangzhou Avenue South, Haizhu District, Guangzhou
 * All right reserved.
 *
 * Modification  History:
 * Date           Author         Version         Discription
 * -----------------------------------------------------------------------------------
 * 2015-4-25   WangXiangBo        1.0              新建
 */
package com.hnctdz.aiLock.dto.device;

/**
 * @ClassName DevLockInGroupDto.java
 * @Author WangXiangBo
 * @Date 2015-4-25 上午11:16:11
 */
public class DevLockInGroupDto {
	private Long groupId;
	private String groupName;

	private String lockCode;
	private String lockName;

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

	public String getLockCode() {
		return lockCode;
	}

	public void setLockCode(String lockCode) {
		this.lockCode = lockCode;
	}

	public String getLockName() {
		return lockName;
	}

	public void setLockName(String lockName) {
		this.lockName = lockName;
	}
}
