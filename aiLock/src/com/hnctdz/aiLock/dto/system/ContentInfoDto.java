package com.hnctdz.aiLock.dto.system;

import java.util.Date;

/**
 * @ClassName SysContentDto.java
 * @Author WangXiangBo
 */
public class ContentInfoDto {
	private Long contentId;
	private String title;
	private String subtitle;
	private String icon;
	private String bigicon;
	private Long contentType;
	private String contentText;
	private Date ceataTime;
	private Long ceataUser;
	private Date updateTime;
	private Long updateUser;
	private Long status;
	
	private String contentIds;

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getBigicon() {
		return bigicon;
	}

	public void setBigicon(String bigicon) {
		this.bigicon = bigicon;
	}

	public Long getContentType() {
		return contentType;
	}

	public void setContentType(Long contentType) {
		this.contentType = contentType;
	}

	public String getContentText() {
		return contentText;
	}

	public void setContentText(String contentText) {
		this.contentText = contentText;
	}

	public Date getCeataTime() {
		return ceataTime;
	}

	public void setCeataTime(Date ceataTime) {
		this.ceataTime = ceataTime;
	}

	public Long getCeataUser() {
		return ceataUser;
	}

	public void setCeataUser(Long ceataUser) {
		this.ceataUser = ceataUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getContentIds() {
		return contentIds;
	}

	public void setContentIds(String contentIds) {
		this.contentIds = contentIds;
	}

}
