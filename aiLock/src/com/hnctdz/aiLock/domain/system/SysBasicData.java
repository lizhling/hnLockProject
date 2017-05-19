package com.hnctdz.aiLock.domain.system;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * SysBasicData entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_BASIC_DATA")
public class SysBasicData implements java.io.Serializable {

	// Fields

	private Long basicDataId;
	private String typeName;
	private String typeCode;
	private String typeTag;
	private Long parentId;
	private String parentName;
	private String memo;
	private String typeOrder;
	private Boolean isValid;

	// Constructors

	/** default constructor */
	public SysBasicData() {
	}

	/** minimal constructor */
	public SysBasicData(Long basicDataId) {
		this.basicDataId = basicDataId;
	}

	/** full constructor */
	public SysBasicData(Long basicDataId, String typeName,
			Long parentId, String memo, String typeOrder, Boolean isValid) {
		this.basicDataId = basicDataId;
		this.typeName = typeName;
		this.parentId = parentId;
		this.memo = memo;
		this.typeOrder = typeOrder;
		this.isValid = isValid;
	}
	
	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "BASIC_DATA_ID", unique = true, nullable = false)
	public Long getBasicDataId() {
		return this.basicDataId;
	}

	public void setBasicDataId(Long basicDataId) {
		this.basicDataId = basicDataId;
	}

	@Column(name = "TYPE_NAME", length = 50)
	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	@Column(name = "TYPE_CODE", length = 50)
	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	
	@Column(name = "TYPE_TAG", length = 50)
	public String getTypeTag() {
		return typeTag;
	}

	public void setTypeTag(String typeTag) {
		this.typeTag = typeTag;
	}


	@Column(name = "PARENT_ID", precision = 10, scale = 0)
	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Column(name = "MEMO", length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "TYPE_ORDER", length = 4)
	public String getTypeOrder() {
		return this.typeOrder;
	}

	public void setTypeOrder(String typeOrder) {
		this.typeOrder = typeOrder;
	}

	@Column(name = "IS_VALID", precision = 1, scale = 0)
	public Boolean getIsValid() {
		return this.isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	@Transient
	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

}