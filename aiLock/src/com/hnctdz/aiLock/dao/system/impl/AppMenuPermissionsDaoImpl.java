package com.hnctdz.aiLock.dao.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.system.AppMenuPermissionsDao;
import com.hnctdz.aiLock.domain.system.AppMenuPermissions;
import com.hnctdz.aiLock.dto.Combobox;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.AppMenuPermissionsDto;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName AppMenuPermissionsDaoImpl.java
 * @Author WangXiangBo 
 */
@Repository("AppMenuPermissionsDao")
public class AppMenuPermissionsDaoImpl extends GenericDaoImpl<AppMenuPermissions, Long> implements AppMenuPermissionsDao{
	private Map<String, Object> proMap;
	
	public String queryConditions(AppMenuPermissionsDto dto){
		proMap = new HashMap<String, Object>();
		StringBuffer conSql = new StringBuffer();
		if(dto != null){
			if(StringUtil.isNotEmpty(dto.getMenuName())){
				conSql.append(" and menuName like :menuName");
				proMap.put("menuName", "%"+dto.getMenuName()+"%");
			}
			if(null != dto.getPermissionsType()){
				conSql.append(" and permissionsType = :permissionsType");
				proMap.put("permissionsType", dto.getPermissionsType());
			}
			if(null != dto.getStatus()){
				conSql.append(" and status = :status");
				proMap.put("status", dto.getStatus());
			}
		}
		return conSql.toString();
	}
	
	public DataPackage findPageAppMenuPermissions(AppMenuPermissionsDto dto,DataPackage dp){
		String hql = " from AppMenuPermissions where 1=1 " + queryConditions(dto);
		return findPageByHQL(hql, proMap, dp);
	}
	
	public List<AppMenuPermissions> findAppMenuPermissionsList(AppMenuPermissionsDto dto){
		String hql = " from AppMenuPermissions where 1=1 " + queryConditions(dto) + " order by menuOrder asc";
		return findAllByHQL(hql, proMap);
	}
	
	public List findAppMenuPermissionsOptions(AppMenuPermissionsDto dto){
		String hql = "select menuId, menuName from AppMenuPermissions where 1=1 " + queryConditions(dto) + " order by menuOrder asc";
		return findAllByHQL(hql, proMap);
	}
	
	public String deleteAppMenuPermissionsByIds(String ids) throws Exception{
		String result = "";
		try{
			bulkUpdate("delete from AppMenuPermissions t where t.menuId in("+ids+")");
		}catch(DataIntegrityViolationException e){
			result = "该资源被其它数据关联，不能删除！";
			e.printStackTrace();
		}catch(Exception e){
			result = "数据库资源出错！";
			e.printStackTrace();
		}finally{
			return result;
		}
	}
}

