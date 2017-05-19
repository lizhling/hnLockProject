package com.hnctdz.aiLock.service.system;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.system.SysUserRole;

/** 
 * @ClassName SysUserRoleService.java
 * @Author WangXiangBo 
 */
@Service
public interface SysUserRoleService {
	
	/**
	 * 通过用户Id查询用户所拥有的角色
	 */
	public List<SysUserRole> findRoleByUserId(Long userId);
	
	/**
	 * 保存/修改用户拥有的角色
	 */
	public boolean saveSysUserRole(Long userId, String serviceIds)throws Exception;
	
	public String findOrgPermissionByUserOrg(Long orgId);

	/**
	 * 导出服务所器中的
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void exportOrgInfo(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException, IOException;

	/**
	 * 导入组织节点信息
	 * @param importFile
	 * @return
	 */
	public String importOrgInfos(File importFile);
}
