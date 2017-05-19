package com.hnctdz.aiLock.dao.system.impl;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.system.SysVersionDao;
import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.domain.system.SysVersion;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysVersionDto;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName SysVersionDaoImpl.java
 * @Author WangXiangBo 
 */
@Repository("SysVersionDao")
public class SysVersionDaoImpl extends GenericDaoImpl<SysVersion, Integer> implements SysVersionDao{
	public String queryConditions(SysVersionDto dto){
		StringBuffer conSql = new StringBuffer();
		if(dto != null){
			if(StringUtil.isNotEmpty(dto.getVersionCode())){
				conSql.append(" and versionCode = ").append(dto.getVersionCode());
			}
			if(StringUtil.isNotEmpty(dto.getVersionName())){
				conSql.append(" and versionName like '%").append(dto.getVersionName()).append("%'");
			}
			if(null != dto.getVersionId()){
				conSql.append(" and versionId = ").append(dto.getVersionId());
			}
			if(null != dto.getVersionOs()){
				conSql.append(" and versionOs = ").append(dto.getVersionOs());
			}
		}
		return conSql.toString();
	}
	
	public DataPackage findPageSysVersion(SysVersionDto dto,DataPackage dp){
		String hql = " from SysVersion where 1=1 " + queryConditions(dto).toString() + " order by createTime desc";
		return findPageByHQL(hql, dp);
	}
	
	public List<SysVersion> findSysVersionList(SysVersionDto dto){
		String hql = " from SysVersion where 1=1 " + queryConditions(dto).toString() + " order by createTime desc";
		return findAllByHQL(hql);
	}
	
	public String deleteSysVersionByIds(String ids) throws Exception{
		String result = "";
		try{
			bulkUpdate("delete from SysVersion t where t.versionId in("+ids+")");
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
	
	public boolean checkSysVersionCode(Integer versionOs,String versionCode,Integer versionId){
		if(StringUtil.isEmpty(versionCode)){
			return false;
		}
		
		String hql = " from SysVersion where versionCode = '" + versionCode + "' and versionOs = "+versionOs;
		if(null != versionId){
			hql += " and versionId != " + versionId;
		}
		List<SysVersion> list = findAllByHQL(hql);
		
		if(list.size() > 0){
			return false;
		}else{
			return true;
		}
	}
	
	public SysVersion getNewSysVersion(Integer versionOs){
		String hql = " from SysVersion where versionOs = " + versionOs + " order by createTime desc";
		List<SysVersion> list = findAllByHQL(hql);
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}
}
