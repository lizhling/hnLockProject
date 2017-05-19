/*
 * FileName:     SysBasicDataDto.java
 * @Description: 
 * Copyright (c) 2014 Sunrise Corporation.
 * #368, Guangzhou Avenue South, Haizhu District, Guangzhou
 * All right reserved.
 *
 * Modification  History:
 * Date           Author         Version         Discription
 * -----------------------------------------------------------------------------------
 * 2014-1-9   WangXiangBo        1.0              新建
 */
package com.hnctdz.aiLock.dto.system;

/**
 * @ClassName SysBasicDataDto.java
 * @Author WangXiangBo
 * @Date 2014-1-9 上午10:15:02
 */
public class SysBasicDataDto {
	
	private Long basicDataId;
	private String typeName;
	private String typeCode;
	private String typeTag;
	private Long parentId;
	private String parentName;
	private String memo;
	private String typeOrder;
	private Boolean isValid;
	
	

	public Long getBasicDataId() {
		return basicDataId;
	}

	public void setBasicDataId(Long basicDataId) {
		this.basicDataId = basicDataId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getTypeOrder() {
		return typeOrder;
	}

	public void setTypeOrder(String typeOrder) {
		this.typeOrder = typeOrder;
	}

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getTypeTag() {
		return typeTag;
	}

	public void setTypeTag(String typeTag) {
		this.typeTag = typeTag;
	}

}
