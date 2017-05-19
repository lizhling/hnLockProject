package com.hnctdz.aiLock.domain.system;


import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * SysRes entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_RES")
public class SysRes implements java.io.Serializable {

	// Fields

	private Long resId;
	private String resName;
	private Long resParentId;
	private Long resType; //0为不需要显示的资源、1为页面按钮资源、2为左边树菜单资源、3为顶部菜单资源第一层、4为顶部菜单资源第二层、5为顶部菜单资源第三层
	private String resUrl;
	private String resIcon;
	private String note;
	private Long status;
	private Long resOrder;

	private String parentResName;
	private String resTypeName;
	// Constructors

	/** default constructor */
	public SysRes() {
	}

	/** full constructor */
	public SysRes(String resName, Long resParentId, Long resType, String resUrl, String resIcon, String note,
			Long status,Long resOrder) {
		this.resName = resName;
		this.resParentId = resParentId;
		this.resType = resType;
		this.resUrl = resUrl;
		this.resIcon = resIcon;
		this.note = note;
		this.status = status;
		this.resOrder = resOrder;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "RES_ID", unique = true, nullable = false)
	public Long getResId() {
		return this.resId;
	}

	public void setResId(Long resId) {
		this.resId = resId;
	}

	@Column(name = "RES_NAME", length = 60)
	public String getResName() {
		return this.resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	@Column(name = "RES_PARENT_ID")
	public Long getResParentId() {
		return this.resParentId;
	}

	public void setResParentId(Long resParentId) {
		this.resParentId = resParentId;
	}

	@Column(name = "RES_TYPE", precision = 1, scale = 0)
	public Long getResType() {
		return this.resType;
	}

	public void setResType(Long resType) {
		this.resType = resType;
	}

	@Column(name = "RES_URL")
	public String getResUrl() {
		return this.resUrl;
	}

	public void setResUrl(String resUrl) {
		this.resUrl = resUrl;
	}

	@Column(name = "RES_ICON")
	public String getResIcon() {
		return resIcon;
	}

	public void setResIcon(String resIcon) {
		this.resIcon = resIcon;
	}

	@Column(name = "NOTE", length = 100)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	@Column(name = "STATUS", precision = 1, scale = 0)
	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}
	
	@Column(name = "RES_ORDER", length = 20)
	public Long getResOrder() {
		return this.resOrder;
	}

	public void setResOrder(Long resOrder) {
		this.resOrder = resOrder;
	}
	
	@Transient
	public String getParentResName() {
		return parentResName;
	}

	public void setParentResName(String parentResName) {
		this.parentResName = parentResName;
	}

	@Transient
	public String getResTypeName() {
		return resTypeName;
	}

	public void setResTypeName(String resTypeName) {
		this.resTypeName = resTypeName;
	}

	
}
