package com.hnctdz.aiLock.dao.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.system.SysRoleDao;
import com.hnctdz.aiLock.domain.system.SysRole;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysRoleDto;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName SysRoleDaoImpl.java
 * @Author WangXiangBo 
 */
@Repository("SysRoleDao")
public class SysRoleDaoImpl extends GenericDaoImpl<SysRole, Long> implements SysRoleDao{
	
	private Map<String, Object> proMap;
	
	public String queryConditions(SysRoleDto dto){
		proMap = new HashMap<String, Object>();
		StringBuffer conSql = new StringBuffer();
		if(dto != null){
			if(StringUtil.isNotEmpty(dto.getRoleName())){
				conSql.append(" and roleName like :roleName");
				proMap.put("roleName", "%"+dto.getRoleName()+"%");
			}
			if(null != dto.getStatus()){
				conSql.append(" and status = :status");
				proMap.put("status", dto.getStatus());
			}
		}
		return conSql.toString();
	}
	
	public DataPackage findPageSysRole(SysRoleDto dto,DataPackage dp){
		String hql = " from SysRole where 1=1 " + queryConditions(dto);
		return findPageByHQL(hql, proMap, dp);
	}
	
	public String deleteSysRoleByIds(String roleIds){
		String result = "";
		try{
			bulkUpdate("delete from SysRole t where t.roleId in("+roleIds+")");
		}catch(DataIntegrityViolationException e){
			result = "该角色被其它数据关联，不能删除！";
			e.printStackTrace();
		}catch(Exception e){
			result = "数据库资源出错！";
			e.printStackTrace();
		}finally{
			return result;
		}
	}
	
	public List<SysRole> findSysRole(SysRoleDto dto){
		String hql = " from SysRole where 1=1 " + queryConditions(dto);
		return findAllByHQL(hql, proMap);
	}
}
