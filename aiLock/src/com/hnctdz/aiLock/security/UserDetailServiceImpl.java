package com.hnctdz.aiLock.security;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hnctdz.aiLock.dao.system.SysAreaDao;
import com.hnctdz.aiLock.dao.system.SysUserDao;
import com.hnctdz.aiLock.dao.system.SysUserRoleDao;
import com.hnctdz.aiLock.domain.system.SysUser;
import com.hnctdz.aiLock.domain.system.SysUserRole;
import com.hnctdz.aiLock.dto.SysUserDetail;
import com.hnctdz.aiLock.utils.CommonUtil;
import com.hnctdz.aiLock.utils.encryption.ZipUtils;

/**
 * 实现SpringSecurity的UserDetailsService接口,实现获取用户Detail信息的回调函数.
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private SysUserDao userDao;
	
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	@Autowired
	private SysAreaDao sysAreaDao;
	
	private final String charSet = "utf-8";


	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		try{
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			
			String usernames = new String(ZipUtils.bASE63Decoder(username, ""));
			SysUser sysUser = userDao.getSysUserByName(usernames);
			if (sysUser != null) {
				List<SysUserRole> userInRolelist = sysUserRoleDao.findRoleByUserId(sysUser.getUserId());
				sysUser.setUserInRoleIds(extractUserInRoleIds(userInRolelist));
				
//				String orgPermission = sysUserRoleDao.findOrgPermissionByUserOrg(sysUser.getOrgId());
//				sysUser.setOrgPermissionIds(orgPermission);
////				sysUser.setOrgPermissionIds(extractUserInOrgPermission(orgPermissionList, sysUser.getOrgId()));
//				
//				String areaPermission = this.sysAreaDao.findAreaPermissionByUserArea(sysUser.getAreaId());
//		        sysUser.setAreaPermissionIds(areaPermission);
		        
				SysUserDetail userDetail = new SysUserDetail(sysUser);
				userDetail.setSysUser(sysUser);
				userDetail.setUserName(sysUser.getUserName());
	        	userDetail.setPassword(new String(ZipUtils.bASE64Encoder(sysUser.getPassword(), "")));
	        	
	        	try {
	    			request.getSession(true).setAttribute("checkCodeSession",null);
	    		    request.getSession().invalidate();
	    		    if (request.getCookies() != null) {  
	    		         Cookie cookie = request.getCookies()[0];// 获取cookie
	    		         cookie.setMaxAge(0);// 让cookie过期  
	    		    }  
	    		} catch (Exception e) {
	    		      e.printStackTrace();
	    		}
	    		
	    		String ip = CommonUtil.getIpAddr(request);
	    		request.getSession(true).setAttribute("userAccIp",ip);
	        	
				return userDetail;
			} else {
				throw new UsernameNotFoundException("系统中没有该用户ID: "+usernames+"或用户无效。");
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("登录异常");
		}
		
	}
	
	private List<Long> extractUserInRoleIds(List<SysUserRole> frwSysUserRoles) {
    	List<Long> userInRoleIds = new ArrayList<Long>();
    	if (frwSysUserRoles != null) {
    		for (SysUserRole uir : frwSysUserRoles) {
    			userInRoleIds.add(uir.getId().getRoleId());
    		}
    	}
    	return userInRoleIds;
    }
	
	private String extractUserInOrgPermission(List orgPermissionList, Long orgId){
		String orgPermissions = "";
		String f = "";
		for(int i=0; i < orgPermissionList.size(); i++){
			orgPermissions += f + orgPermissionList.get(i);
			f = ",";
		}
		return orgPermissions.equalsIgnoreCase("") ? orgId.toString() : orgPermissions;
	}

}
