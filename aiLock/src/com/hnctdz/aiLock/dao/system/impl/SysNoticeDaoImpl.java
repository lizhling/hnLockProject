package com.hnctdz.aiLock.dao.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.system.SysNoticeDao;
import com.hnctdz.aiLock.domain.system.SysNotice;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysNoticeDto;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName SysNoticeDaoImpl.java
 * @Author WangXiangBo 
 */
@Repository("SysNoticeDao")
public class SysNoticeDaoImpl extends GenericDaoImpl<SysNotice, Long> implements SysNoticeDao{
	private Map<String, Object> proMap;
	
	public String queryConditions(SysNoticeDto dto){
		proMap = new HashMap<String, Object>();
		StringBuffer conSql = new StringBuffer();
		if(dto != null){
//			if(StringUtil.isNotEmpty(dto.getMenuName())){
//				conSql.append(" and menuName like :menuName");
//				proMap.put("menuName", "%"+dto.getMenuName()+"%");
//			}
//			if(null != dto.getPermissionsType()){
//				conSql.append(" and permissionsType = :permissionsType");
//				proMap.put("permissionsType", dto.getPermissionsType());
//			}
//			if(null != dto.getStatus()){
//				conSql.append(" and status = :status");
//				proMap.put("status", dto.getStatus());
//			}
		}
		return conSql.toString();
	}
	
	public DataPackage findPageSysNotice(SysNoticeDto dto,DataPackage dp){
		String hql = " from SysNotice where 1=1 " + queryConditions(dto);
		return findPageByHQL(hql, proMap, dp);
	}
	
	public List<SysNotice> findSysNoticeList(SysNoticeDto dto){
		String hql = " from SysNotice where 1=1 " + queryConditions(dto) + " order by isPuttop asc, releaseTime desc";
		return findAllByHQL(hql, proMap);
	}
	
	public String deleteSysNoticeByIds(String ids) throws Exception{
		String result = "";
		try{
			bulkUpdate("delete from SysNotice t where t.noticeId in("+ids+")");
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

