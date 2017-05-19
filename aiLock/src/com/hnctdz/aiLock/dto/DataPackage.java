/*
 * FileName:     DataPackage.java
 * @Description: 
 * Copyright (c) 2013 Sunrise Corporation.
 * #368, Guangzhou Avenue South, Haizhu District, Guangzhou
 * All right reserved.
 *
 * Modification  History:
 * Date           Author         Version         Discription
 * -----------------------------------------------------------------------------------
 * 2013-12-17   WangXiangBo        1.0              新建
 */
package com.hnctdz.aiLock.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** 
 * @ClassName DataPackage.java
 * @Author WangXiangBo 
 * @Date 2013-12-17 下午03:09:04
 */
public class DataPackage extends Response implements Serializable {
	
	private static final long serialVersionUID = -3215516372610210134L;

	private int total;

	private int pageSize;

	private int pageNo;

	private Collection rows;
	
	private Collection footer;
	
	public DataPackage() {}
	
	public DataPackage(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public Collection getRows() {
		return rows;
	}

	public void setRows(Collection rows) {
		this.rows = rows;
	}

	public Collection getFooter() {
		return footer;
	}

	public void setFooter(Collection footer) {
		this.footer = footer;
	}

}