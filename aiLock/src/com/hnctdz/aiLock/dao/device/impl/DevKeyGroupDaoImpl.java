package com.hnctdz.aiLock.dao.device.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.device.DevKeyGroupDao;
import com.hnctdz.aiLock.domain.device.DevKeyGroup;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.DevKeyGroupDto;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName DevKeyGroupDaoImpl.java
 * @Author WangXiangBo 
 */
@Repository("DevKeyGroupDao")
public class DevKeyGroupDaoImpl extends GenericDaoImpl<DevKeyGroup, Long> implements DevKeyGroupDao{
	
	private Map<String, Object> proMap;
	
	public String queryConditions(DevKeyGroupDto dto){
		proMap = new HashMap<String, Object>();
		StringBuffer conSql = new StringBuffer();
		if(dto != null){
			if(StringUtil.isNotEmpty(dto.getGroupName())){
				conSql.append(" and groupName like :groupName");
				proMap.put("groupName", "%"+dto.getGroupName()+"%");
			}
			if(StringUtil.isNotEmpty(dto.getGroupSecretKey())){
				conSql.append(" and groupSecretKey like :groupSecretKey");
				proMap.put("groupSecretKey", "%"+dto.getGroupName()+"%");
			}
		}
		
//		conSql.append(" and exists (select 1 from DevKeyInfo k where k.groupId = g.groupId " 
//				    + this.addOrgPermissionHql() + ")");//添加登录人所在组织权限
		return conSql.toString();
	}
	
	public DataPackage findPageDevKeyGroup(DevKeyGroupDto dto,DataPackage dp){
		String hql = " from DevKeyGroup g where 1=1 " + queryConditions(dto);
		return findPageByHQL(hql, proMap, dp);
	}
	
	public List<DevKeyGroup> findDevKeyGroupList(DevKeyGroupDto dto){
		String hql = " from DevKeyGroup g where 1=1 " + queryConditions(dto);
		return findAllByHQL(hql, proMap);
	}
	
	public List findKeyGroupOptions(DevKeyGroupDto dto){
		String hql = "select groupId, groupName from DevKeyGroup l where 1=1 " + queryConditions(dto);
		return findAllByHQL(hql, proMap);
	}
	
	public String deleteDevKeyGroupByIds(String groupIds){
		String result = "";
		try{
			bulkUpdate("delete from DevKeyGroup t where t.groupId in("+groupIds+")");
		}catch(DataIntegrityViolationException e){
			result = "该分组被其它数据关联，不能删除！";
			e.printStackTrace();
		}catch(Exception e){
			result = "数据库资源出错！";
			e.printStackTrace();
		}finally{
			return result;
		}
	}
	
	public List<DevKeyGroup> findDevKeyGroup(DevKeyGroupDto dto){
		String hql = " from DevKeyGroup where 1=1 " + queryConditions(dto);
		return findAllByHQL(hql, proMap);
	}
}
