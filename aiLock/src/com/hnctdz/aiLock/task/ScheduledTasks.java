package com.hnctdz.aiLock.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hnctdz.aiLock.domain.system.AppMenuPermissions;
import com.hnctdz.aiLock.dto.system.AppMenuPermissionsDto;
import com.hnctdz.aiLock.service.system.AppMenuPermissionsService;
import com.hnctdz.aiLock.utils.AppMenuInfo;
import com.hnctdz.aiLock.utils.Constants;

/** 
 * @ClassName ScheduledTasks.java
 * @Author WangXiangBo 
 */
public class ScheduledTasks {
	@Autowired
 	private AppMenuPermissionsService appMenuPermissionsService;
	
	public void appMenuUpdateTask(){
		AppMenuPermissionsDto dto = new AppMenuPermissionsDto();
		dto.setStatus(1L);
		List<AppMenuPermissions> list = appMenuPermissionsService.findAppMenuPermissionsList(dto);
		
		AppMenuInfo appMenuInfo = AppMenuInfo.getInstance();
		appMenuInfo.initializeList();
		
		for(AppMenuPermissions amp : list){
			if(amp.getParentId() == null){
				amp.setParentId(0L);//表示最上级菜单
			}
			if(amp.getPermissionsType().equals(Long.parseLong(Constants.PERSONNEL_LOGIN))){
				appMenuInfo.addPerMenuList(amp);
			}else if(amp.getPermissionsType().equals(Long.parseLong(Constants.SYSUSER_LOGIN))){
				appMenuInfo.addSysUserMenuList(amp);
			}else{
				appMenuInfo.addPerMenuList(amp);
				appMenuInfo.addSysUserMenuList(amp);
			}
		}
	}
}
