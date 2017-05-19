package com.hnctdz.aiLock.domain.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TbColumnConfig entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "TB_COLUMN_CONFIG")
public class TbColumnConfig implements java.io.Serializable {

	// Fields

	private Long id;
	private String gridCode;
	private String gridName;
	private String title;
	private String field;
	private Long width;
	private Long lineLevel;
	private Long sortable;
	private Long rowspan;
	private Long colspan;
	private Long resOrder;
	private String formatter;
	private Long status;

	// Constructors

	/** default constructor */
	public TbColumnConfig() {
	}

	/** minimal constructor */
	public TbColumnConfig(Long id) {
		this.id = id;
	}

	/** full constructor */
	public TbColumnConfig(Long id, String gridCode, String gridName,
			String title, String field, Long width, Long lineLevel,
			Long sortable, Long rowspan, Long colspan,
			Long resOrder, String formatter, Long status) {
		this.id = id;
		this.gridCode = gridCode;
		this.gridName = gridName;
		this.title = title;
		this.field = field;
		this.width = width;
		this.lineLevel = lineLevel;
		this.sortable = sortable;
		this.rowspan = rowspan;
		this.colspan = colspan;
		this.resOrder = resOrder;
		this.formatter = formatter;
		this.status = status;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "GRID_CODE", length = 50)
	public String getGridCode() {
		return this.gridCode;
	}

	public void setGridCode(String gridCode) {
		this.gridCode = gridCode;
	}

	@Column(name = "GRID_NAME", length = 100)
	public String getGridName() {
		return this.gridName;
	}

	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	@Column(name = "TITLE", length = 100)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "FIELD", length = 100)
	public String getField() {
		return this.field;
	}

	public void setField(String field) {
		this.field = field;
	}

	@Column(name = "WIDTH", precision = 5, scale = 0)
	public Long getWidth() {
		return this.width;
	}

	public void setWidth(Long width) {
		this.width = width;
	}

	@Column(name = "LINE_LEVEL", precision = 1, scale = 0)
	public Long getLineLevel() {
		return this.lineLevel;
	}

	public void setLineLevel(Long lineLevel) {
		this.lineLevel = lineLevel;
	}

	@Column(name = "SORTABLE", precision = 1, scale = 0)
	public Long getSortable() {
		return this.sortable;
	}

	public void setSortable(Long sortable) {
		this.sortable = sortable;
	}

	@Column(name = "ROWSPAN", precision = 5, scale = 0)
	public Long getRowspan() {
		return this.rowspan;
	}

	public void setRowspan(Long rowspan) {
		this.rowspan = rowspan;
	}

	@Column(name = "COLSPAN", precision = 5, scale = 0)
	public Long getColspan() {
		return this.colspan;
	}

	public void setColspan(Long colspan) {
		this.colspan = colspan;
	}

	@Column(name = "RES_ORDER", precision = 5, scale = 0)
	public Long getResOrder() {
		return this.resOrder;
	}

	public void setResOrder(Long resOrder) {
		this.resOrder = resOrder;
	}

	@Column(name = "FORMATTER", length = 300)
	public String getFormatter() {
		return this.formatter;
	}

	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}

	@Column(name = "STATUS", precision = 1, scale = 0)
	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

}