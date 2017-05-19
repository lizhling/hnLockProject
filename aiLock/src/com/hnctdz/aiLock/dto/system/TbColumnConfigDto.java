package com.hnctdz.aiLock.dto.system;

/**
 * @ClassName TbColumnConfigDto.java
 * @Author WangXiangBo
 */
public class TbColumnConfigDto {
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGridCode() {
		return gridCode;
	}

	public void setGridCode(String gridCode) {
		this.gridCode = gridCode;
	}

	public String getGridName() {
		return gridName;
	}

	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Long getWidth() {
		return width;
	}

	public void setWidth(Long width) {
		this.width = width;
	}

	public Long getLineLevel() {
		return lineLevel;
	}

	public void setLineLevel(Long lineLevel) {
		this.lineLevel = lineLevel;
	}

	public Long getSortable() {
		return sortable;
	}

	public void setSortable(Long sortable) {
		this.sortable = sortable;
	}

	public Long getRowspan() {
		return rowspan;
	}

	public void setRowspan(Long rowspan) {
		this.rowspan = rowspan;
	}

	public Long getColspan() {
		return colspan;
	}

	public void setColspan(Long colspan) {
		this.colspan = colspan;
	}

	public Long getResOrder() {
		return resOrder;
	}

	public void setResOrder(Long resOrder) {
		this.resOrder = resOrder;
	}

	public String getFormatter() {
		return formatter;
	}

	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}
}
