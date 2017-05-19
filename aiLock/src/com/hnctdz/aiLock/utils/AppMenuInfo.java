package com.hnctdz.aiLock.utils;

import java.util.ArrayList;
import java.util.List;

import com.hnctdz.aiLock.domain.system.AppMenuPermissions;

/**
 * @ClassName AppMenuInfo.java
 * @Author WangXiangBo
 */
public class AppMenuInfo {
	private static final AppMenuInfo instance = new AppMenuInfo();

	private List<AppMenuPermissions> sysUserMenuList = new ArrayList<AppMenuPermissions>();
	private List<AppMenuPermissions> perMenuList = new ArrayList<AppMenuPermissions>();

	public static AppMenuInfo getInstance() {
		return instance;
	}
	
	public void initializeList(){
		perMenuList = new ArrayList<AppMenuPermissions>();
		sysUserMenuList = new ArrayList<AppMenuPermissions>();
	}
	
	public void addSysUserMenuList(AppMenuPermissions amp){
		sysUserMenuList.add(amp);
	}
	
	public void addPerMenuList(AppMenuPermissions amp){
		perMenuList.add(amp);
	}
	
	public void perMenuList(AppMenuPermissions amp){
		perMenuList.add(amp);
	}

	public List<AppMenuPermissions> getSysUserMenuList() {
		return sysUserMenuList;
	}

	public void setSysUserMenuList(List<AppMenuPermissions> sysUserMenuList) {
		this.sysUserMenuList = sysUserMenuList;
	}

	public List<AppMenuPermissions> getPerMenuList() {
		return perMenuList;
	}

	public void setPerMenuList(List<AppMenuPermissions> perMenuList) {
		this.perMenuList = perMenuList;
	}
}
