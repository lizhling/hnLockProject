package com.hnctdz.aiLock.dao.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.system.SysResDao;
import com.hnctdz.aiLock.domain.system.SysRes;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysResDto;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName SysResDaoImpl.java
 * @Author WangXiangBo 
 */
@Repository("SysResDao")
public class SysResDaoImpl extends GenericDaoImpl<SysRes, Long> implements SysResDao{
	private Map<String, Object> proMap;
	
	public String queryConditions(SysResDto dto){
		proMap = new HashMap<String, Object>();
		StringBuffer conSql = new StringBuffer();
		if(dto != null){
			if(StringUtil.isNotEmpty(dto.getResName())){
				conSql.append(" and resName like :resName");
				proMap.put("resName", "%"+dto.getResName()+"%");
			}
			if(null != dto.getResType()){
				conSql.append(" and resType = :resType");
				proMap.put("resType", dto.getResType());
			}
			if(null != dto.getStatus()){
				conSql.append(" and status = :status");
				proMap.put("status", dto.getStatus());
			}
			if(null != dto.getResParentId()){
				conSql.append(" and resParentId = :resParentId");
				proMap.put("resParentId", dto.getResParentId());
			}
		}
		return conSql.toString();
	}
	
	public DataPackage findPageSysRes(SysResDto dto,DataPackage dp){
		String hql = " from SysRes where 1=1 " + queryConditions(dto);
		return findPageByHQL(hql, proMap, dp);
	}

	public List<SysRes> findSysResList(SysResDto dto){
		String hql = " from SysRes where 1=1 " + queryConditions(dto) +
					 " order by resParentId asc , resOrder asc , resId asc ";
		return findAllByHQL(hql, proMap);
	}
	
	public List<SysRes> findMenuResByRoleIds(List<Long> userInRoleIds, int resType){
		StringBuffer roleIdBuffer = new StringBuffer();
		String f = "";
		for (Long roleId : userInRoleIds) {
			roleIdBuffer.append(f).append(roleId.toString());
			f = ",";
		}
		StringBuffer hql = new StringBuffer();
		hql.append(" select res from SysRes res")
		   .append(" where EXISTS (select 1 from SysRoleRes rir where res.resId = rir.frwSysRes.resId and rir.frwSysRole.roleId in ( ")
		   .append(roleIdBuffer.toString())
		   .append(" ) group by rir.frwSysRes.resId ) and res.resType = ").append(resType)
		   .append(" and res.status = 1")
		   .append(" order by res.resParentId asc ,res.resOrder asc ,res.resId asc");
		
		return findAllByHQL(hql.toString());
	}
	
	public String deleteSysResByIds(String ids) throws Exception{
		String result = "";
		try{
			List<SysRes> datas = findAllByHQL("from SysRes t where t.resParentId in("+ids+")");
			if(datas != null && datas.size() > 0){
				result = "您所删除的资源里有子节点，不允许删除！";
			}else{
				bulkUpdate("delete from SysRes t where t.resId in("+ids+")");
			}
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
