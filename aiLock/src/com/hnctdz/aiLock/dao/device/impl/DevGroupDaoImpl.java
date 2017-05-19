package com.hnctdz.aiLock.dao.device.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.device.DevGroupDao;
import com.hnctdz.aiLock.domain.device.DevGroup;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.device.DevGroupDto;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName DevGroupDaoImpl.java
 * @Author WangXiangBo 
 */
@Repository("DevGroupDao")
public class DevGroupDaoImpl extends GenericDaoImpl<DevGroup, Long> implements DevGroupDao{
	
	private Map<String, Object> proMap;
	
	public String queryConditions(DevGroupDto dto){
		proMap = new HashMap<String, Object>();
		StringBuffer conSql = new StringBuffer();
		if(dto != null){
			if(StringUtil.isNotEmpty(dto.getGroupName())){
				conSql.append(" and groupName like :groupName");
				proMap.put("groupName", "%"+dto.getGroupName()+"%");
			}
			if(StringUtil.isNotEmpty(dto.getLockCode())){
				conSql.append(" and lockCode = :lockCode");
				proMap.put("lockCode", dto.getLockCode());
			}
			if(StringUtil.isNotEmpty(dto.getLockCode())){
				conSql.append(" and lockCode = :lockCode");
				proMap.put("lockCode", dto.getLockCode());
			}
		}
		
		conSql.append(" and exists (select 1 from DevLockInGroup lg where g.groupId = lg.id.groupId " +
					  				" and exists (select 1 from DevLockInfo where lg.id.lockId = lockId " 
				    + this.addOrgPermissionHql() + "))");//添加登录人所在组织权限
		return conSql.toString();
	}
	
	public DataPackage findPageDevGroup(DevGroupDto dto,DataPackage dp){
		String hql = " from DevGroup g where 1=1 " + queryConditions(dto);
		return findPageByHQL(hql, proMap, dp);
	}
	
	public List<DevGroup> findDevGroupList(DevGroupDto dto){
		String hql = " from DevGroup g where 1=1 " + queryConditions(dto);
		return findAllByHQL(hql, proMap);
	}
	
	public DataPackage findGroupLock(DevGroupDto dto,DataPackage dp){
		proMap = new HashMap<String, Object>();
		String hql = " from DevLockInfo l where exists (select 1 from DevLockInGroup lg" +
					 " where lg.id.groupId = :groupId and lg.id.lockId = l.lockId)";
		proMap.put("groupId", dto.getGroupId());
		return findPageByHQL(hql, proMap, dp);
	}
	
	public List findLockByGroupIdsList(DevGroupDto dto){
		proMap = new HashMap<String, Object>();
		String[] groupId = dto.getGroupIds().split(",");
		String idSql = "";
		String f = "";
		for(int i=0; i < groupId.length; i++){
			idSql += f + "lg.id.groupId = :groupId" + i;
			f = " or ";
			proMap.put("groupId" + i, Long.parseLong(groupId[i]));
		}
		String hql = " from DevLockInfo l where exists (select 1 from DevLockInGroup lg" +
					 " where ("+ idSql +") and lg.id.lockId = l.lockId)";
		return findAllByHQL(hql, proMap);
	}
	
	public String deleteDevGroupByIds(String groupIds){
		String result = "";
		try{
			bulkUpdate("delete from DevLockInGroup where id.groupId in("+groupIds+")");
			bulkUpdate("delete from DevGroup t where t.groupId in("+groupIds+")");
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
	
	public List<DevGroup> findDevGroup(DevGroupDto dto){
		String hql = " from DevGroup where 1=1 " + queryConditions(dto);
		return findAllByHQL(hql, proMap);
	}
}
