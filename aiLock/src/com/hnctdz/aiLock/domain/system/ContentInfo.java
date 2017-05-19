package com.hnctdz.aiLock.domain.system;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;

/**
 * ContentInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CONTENT_INFO")
public class ContentInfo implements java.io.Serializable {

	// Fields
	@Expose
	private Long contentId;
	@Expose
	private String title;
	@Expose
	private String subtitle;
	@Expose
	private String icon;
	private String bigIcon;
	@Expose
	private Long contentType;
	@Expose
	private String contentText;
	@Expose
	private Date ceataTime;
	@Expose
	private Long ceataUser;
	@Expose
	private Date updateTime;
	@Expose
	private Long updateUser;
	private Long status;
	private String suportVersion;
	
	private String iconImg;
	private String bigIconImg;
	private String ceataUserName;
	private String updateUserName;
	
	private String iosVersion;
	private String androidVersion;	
	
	// Constructors

	/** default constructor */
	public ContentInfo() {
	}

	/** minimal constructor */
	public ContentInfo(Long contentId) {
		this.contentId = contentId;
	}

	/** full constructor */
	public ContentInfo(Long contentId, String title, String subtitle,
			String icon, String bigIcon, Long contentType,
			String contentText, Date ceataTime, Long ceataUser,
			Date updateTime, Long updateUser, Long status) {
		this.contentId = contentId;
		this.title = title;
		this.subtitle = subtitle;
		this.icon = icon;
		this.bigIcon = bigIcon;
		this.contentType = contentType;
		this.contentText = contentText;
		this.ceataTime = ceataTime;
		this.ceataUser = ceataUser;
		this.updateTime = updateTime;
		this.updateUser = updateUser;
		this.status = status;
	}

	// Property accessors
	@Id
	@Column(name = "CONTENT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getContentId() {
		return this.contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	@Column(name = "TITLE", length = 100)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "SUBTITLE", length = 200)
	public String getSubtitle() {
		return this.subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	@Column(name = "ICON", length = 200)
	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name = "BIG_ICON", length = 200)
	public String getBigIcon() {
		return this.bigIcon;
	}

	public void setBigIcon(String bigIcon) {
		this.bigIcon = bigIcon;
	}

	@Column(name = "CONTENT_TYPE", precision = 1, scale = 0)
	public Long getContentType() {
		return this.contentType;
	}

	public void setContentType(Long contentType) {
		this.contentType = contentType;
	}

	@Column(name = "CONTENT_TEXT", length = 2000)
	public String getContentText() {
		return this.contentText;
	}

	public void setContentText(String contentText) {
		this.contentText = contentText;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CEATA_TIME", length = 7)
	public Date getCeataTime() {
		return this.ceataTime;
	}

	public void setCeataTime(Date ceataTime) {
		this.ceataTime = ceataTime;
	}

	@Column(name = "CEATA_USER", precision = 10, scale = 0)
	public Long getCeataUser() {
		return this.ceataUser;
	}

	public void setCeataUser(Long ceataUser) {
		this.ceataUser = ceataUser;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "UPDATE_USER", precision = 10, scale = 0)
	public Long getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(Long updateUser) {
		this.updateUser = updateUser;
	}

	@Column(name = "STATUS", precision = 1, scale = 0)
	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}
	
	@Column(name = "SUPORT_VERSION", length = 100)
	public String getSuportVersion() {
		return this.suportVersion;
	}

	public void setSuportVersion(String suportVersion) {
		this.suportVersion = suportVersion;
	}

	@Transient
	public String getIconImg() {
		return iconImg;
	}

	public void setIconImg(String iconImg) {
		this.iconImg = iconImg;
	}

	@Transient
	public String getBigIconImg() {
		return bigIconImg;
	}

	public void setBigIconImg(String bigIconImg) {
		this.bigIconImg = bigIconImg;
	}

	@Transient
	public String getCeataUserName() {
		return ceataUserName;
	}

	public void setCeataUserName(String ceataUserName) {
		this.ceataUserName = ceataUserName;
	}

	@Transient
	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}
	
	@Transient
	public String getIosVersion() {
		return iosVersion;
	}

	public void setIosVersion(String iosVersion) {
		this.iosVersion = iosVersion;
	}
	
	@Transient
	public String getAndroidVersion() {
		return androidVersion;
	}

	public void setAndroidVersion(String androidVersion) {
		this.androidVersion = androidVersion;
	}

}