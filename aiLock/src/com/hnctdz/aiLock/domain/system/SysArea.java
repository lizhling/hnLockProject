package com.hnctdz.aiLock.domain.system;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "sys_area")
public class SysArea implements Serializable {
	private Long areaId;
	private String areaName;
	private Integer areaType;
	private Long parentId;
	private Integer areaOrder;
	private String areaImage;
	private String note;
	private String parentName;

	public SysArea() {
	}

	public SysArea(String areaName, Integer areaType, Long parentId,
			Integer areaOrder, String note) {
		this.areaName = areaName;
		this.areaType = areaType;
		this.parentId = parentId;
		this.areaOrder = areaOrder;
		this.note = note;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AREA_ID", unique = true, nullable = false)
	public Long getAreaId() {
		return this.areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	@Column(name = "AREA_NAME", length = 50)
	public String getAreaName() {
		return this.areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@Column(name = "AREA_TYPE")
	public Integer getAreaType() {
		return this.areaType;
	}

	public void setAreaType(Integer areaType) {
		this.areaType = areaType;
	}

	@Column(name = "PARENT_ID")
	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Column(name = "AREA_ORDER")
	public Integer getAreaOrder() {
		return this.areaOrder;
	}

	public void setAreaOrder(Integer areaOrder) {
		this.areaOrder = areaOrder;
	}

	@Column(name = "AREA_IMAGE", length = 100)
	public String getAreaImage() {
		return this.areaImage;
	}

	public void setAreaImage(String areaImage) {
		this.areaImage = areaImage;
	}

	@Column(name = "NOTE", length = 100)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Transient
	public String getParentName() {
		return this.parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
}