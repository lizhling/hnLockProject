package com.hnctdz.aiLock.dao.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.system.SysUserDao;
import com.hnctdz.aiLock.domain.system.SysUser;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysUserDto;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName SysUserDaoImpl.java
 * @Author WangXiangBo 
 */
@Repository("SysUserDao")
public class SysUserDaoImpl extends GenericDaoImpl<SysUser, Long> implements SysUserDao{
	
	private Map<String, Object> proMap;
	
	public String queryConditions(SysUserDto dto){
		proMap = new HashMap<String, Object>();
		StringBuffer conSql = new StringBuffer();
		if(dto != null){
			if(StringUtil.isNotEmpty(dto.getName())){
				conSql.append(" and name like :name");
				proMap.put("name", "%"+dto.getName()+"%");
			}
			if(StringUtil.isNotEmpty(dto.getUserName())){
				conSql.append(" and userName like :userName");
				proMap.put("userName", "%"+dto.getUserName()+"%");
			}
			if(null != dto.getStatus()){
				conSql.append(" and status = :status");
				proMap.put("status", dto.getStatus());
			}
			if(null != dto.getOrgId()){
				conSql.append(" and orgId = :orgId");
				proMap.put("orgId", dto.getOrgId());
			}
		}
		return conSql.toString();
	}
	
	public List<SysUser> findSysUserList(SysUserDto dto){
		String hql = "from SysUser su where 1=1 " + queryConditions(dto);
		return findAllByHQL(hql, proMap);
	}
	
	public DataPackage findPageSysUser(SysUserDto dto, DataPackage dp){
		String hql = "from SysUser su where 1=1 " + queryConditions(dto);
		return findPageByHQL(hql, proMap, dp);
	}
	
	public List findSysUserOptions(SysUserDto dto){
		String hql = "select userId, name from SysUser su where 1=1 " + queryConditions(dto);
		return findAllByHQL(hql, proMap);
	}
	
	public SysUser getSysUserByName(String userName){
		String hql = "from SysUser su where su.userName = :userName";
		Map<String, Object> proMap = new HashMap<String, Object>();
		proMap.put("userName", userName);
		List<SysUser> list = findAllByHQL(hql, proMap);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
}
