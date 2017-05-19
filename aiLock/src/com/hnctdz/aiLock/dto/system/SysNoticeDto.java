package com.hnctdz.aiLock.dto.system;

import java.util.Date;

/**
 * @ClassName SysNoticeDto.java
 * @Author WangXiangBo
 */
public class SysNoticeDto {
	private Integer noticeId;
	private Integer userId;
	private Date releaseTime;
	private String title;
	private String content;
	private String status;
	private String isPuttop;

	public Integer getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(Integer noticeId) {
		this.noticeId = noticeId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public String getIsPuttop() {
		return isPuttop;
	}

	public void setIsPuttop(String isPuttop) {
		this.isPuttop = isPuttop;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
