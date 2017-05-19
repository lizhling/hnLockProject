package com.hnctdz.aiLock.dto.info;

import java.util.Date;

/**
 * @ClassName UpdateAuthorizeDto.java
 * @Author WangXiangBo
 */
public class UpdateAuthorizeDto {
	private Long updateId;
	private Long perId;
	private Date changeTime;
	private Date updateTime;
	private Long status;

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public Long getPerId() {
		return perId;
	}

	public void setPerId(Long perId) {
		this.perId = perId;
	}

	public Date getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

}
