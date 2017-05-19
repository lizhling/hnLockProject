package com.hnctdz.aiLock.domain.system;


import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * SysNotice entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_notice")
public class SysNotice implements java.io.Serializable {

	// Fields

	private Integer noticeId;
	private Long userId;
	private Date releaseTime;
	private String title;
	private String content;
	private String status;
	private String isPuttop;
	
	private String userName;
	private String releaseTimes;

	// Constructors

	/** default constructor */
	public SysNotice() {
	}

	/** minimal constructor */
	public SysNotice(Integer noticeId) {
		this.noticeId = noticeId;
	}

	/** full constructor */
	public SysNotice(Integer noticeId, Long userId, Date releaseTime,
			String title, String content, String status,
			String isPuttop) {
		this.noticeId = noticeId;
		this.userId = userId;
		this.releaseTime = releaseTime;
		this.title = title;
		this.content = content;
		this.status = status;
		this.isPuttop = isPuttop;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "NOTICE_ID", unique = true, nullable = false)
	public Integer getNoticeId() {
		return this.noticeId;
	}

	public void setNoticeId(Integer noticeId) {
		this.noticeId = noticeId;
	}

	@Column(name = "USER_ID")
	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "RELEASE_TIME", length = 19)
	public Date getReleaseTime() {
		return this.releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	@Column(name = "TITLE", length = 50)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "CONTENT", length = 1000)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "STATUS", length = 1)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "IS_PUTTOP", length = 1)
	public String getIsPuttop() {
		return this.isPuttop;
	}

	public void setIsPuttop(String isPuttop) {
		this.isPuttop = isPuttop;
	}

	@Transient
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Transient
	public String getReleaseTimes() {
		return releaseTimes;
	}

	public void setReleaseTimes(String releaseTimes) {
		this.releaseTimes = releaseTimes;
	}
}