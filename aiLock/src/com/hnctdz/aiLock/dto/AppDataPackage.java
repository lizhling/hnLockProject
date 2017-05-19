package com.hnctdz.aiLock.dto;

import java.io.Serializable;
import java.util.Collection;

/** 
 * @ClassName AppDataPackage.java
 * @Author WangXiangBo 
 */
public class AppDataPackage extends Response implements Serializable {
	
	private static final long serialVersionUID = -3215516372610210135L;
	
	private Collection datas;
	
	public AppDataPackage() {}

	public Collection getDatas() {
		return datas;
	}

	public void setDatas(Collection datas) {
		this.datas = datas;
	}
	
}
