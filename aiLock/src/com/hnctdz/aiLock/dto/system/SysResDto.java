package com.hnctdz.aiLock.dto.system;

/** 
 * @ClassName SysResDto.java
 * @Author WangXiangBo 
 */
public class SysResDto {
	private Long resId;
	private String resName;
	private Long resParentId;
	private Long resType; //0为不需要显示的资源、1为页面按钮资源、2为左边树菜单资源、3为顶部菜单资源第一层、4为顶部菜单资源第二层、5为顶部菜单资源第三层
	private String resUrl;
	private String resIcon;
	private String note;
	private Long status;
	private Long resOrder;
	private String tmpString1;

	private String resParentName; 
	
	private String userId;

	public Long getResId() {
		return resId;
	}
	
	public void setResId(Long resId) {
		this.resId = resId;
	}
	
	public String getResName() {
		return resName;
	}
	
	public void setResName(String resName) {
		this.resName = resName;
	}
	
	public Long getResParentId() {
		return resParentId;
	}
	
	public void setResParentId(Long resParentId) {
		this.resParentId = resParentId;
	}
	
	
	public Long getResType() {
		return resType;
	}

	
	public void setResType(Long resType) {
		this.resType = resType;
	}

	
	public String getResParentName() {
		return resParentName;
	}

	
	public void setResParentName(String resParentName) {
		this.resParentName = resParentName;
	}

	public String getResUrl() {
		return resUrl;
	}
	
	public void setResUrl(String resUrl) {
		this.resUrl = resUrl;
	}
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public String getTmpString1() {
		return tmpString1;
	}
	
	public void setTmpString1(String tmpString1) {
		this.tmpString1 = tmpString1;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getResOrder() {
		return resOrder;
	}

	public void setResOrder(Long resOrder) {
		this.resOrder = resOrder;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getResIcon() {
		return resIcon;
	}

	public void setResIcon(String resIcon) {
		this.resIcon = resIcon;
	}

}
