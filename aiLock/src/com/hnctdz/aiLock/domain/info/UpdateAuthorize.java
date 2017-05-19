package com.hnctdz.aiLock.domain.info;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * UpdateAuthorize entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "update_authorize")
public class UpdateAuthorize implements java.io.Serializable {

	// Fields

	private Long updateId;
	private Long perId;
	private Date changeTime;
	private Date updateTime;
	private Long status;

	// Constructors

	/** default constructor */
	public UpdateAuthorize() {
	}

	/** full constructor */
	public UpdateAuthorize(Long perId, Date changeTime,
			Date updateTime, Long status) {
		this.perId = perId;
		this.changeTime = changeTime;
		this.updateTime = updateTime;
		this.status = status;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "UPDATE_ID", unique = true, nullable = false)
	public Long getUpdateId() {
		return this.updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	@Column(name = "PER_ID")
	public Long getPerId() {
		return this.perId;
	}

	public void setPerId(Long perId) {
		this.perId = perId;
	}

	@Column(name = "CHANGE_TIME", length = 19)
	public Date getChangeTime() {
		return this.changeTime;
	}

	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}

	@Column(name = "UPDATE_TIME", length = 19)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "STATUS")
	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

}