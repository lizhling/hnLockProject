package com.hnctdz.aiLock.dao.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.system.SysBasicDataDao;
import com.hnctdz.aiLock.domain.system.SysBasicData;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysBasicDataDto;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName SysBasicDataDaoImpl.java
 * @Author WangXiangBo 
 */
@Repository("SysBasicDataDao")
public class SysBasicDataDaoImpl extends GenericDaoImpl<SysBasicData, Long> implements SysBasicDataDao{

	private Map<String, Object> proMap;
	
	public String queryConditions(SysBasicDataDto dto){
		proMap = new HashMap<String, Object>();
		StringBuffer conSql = new StringBuffer();
		if(dto != null){
			if(null != dto.getBasicDataId()){
				conSql.append(" and basicDataId = :basicDataId");
				proMap.put("basicDataId", dto.getParentId());
			}
			if(StringUtil.isNotEmpty(dto.getTypeName())){
				conSql.append(" and typeName like :typeName");
				proMap.put("typeName", "%"+dto.getTypeName()+"%");
			}
			if(StringUtil.isNotEmpty(dto.getTypeCode())){
				conSql.append(" and typeCode like :typeCode");
				proMap.put("typeCode", "%"+dto.getTypeCode()+"%");
			}
			if(StringUtil.isNotEmpty(dto.getTypeTag())){
				conSql.append(" and typeTag = :typeTag");
				proMap.put("typeTag", dto.getTypeTag());
			}
			if(null != dto.getParentId()){
				conSql.append(" and parentId = :parentId");
				proMap.put("parentId", dto.getParentId());
			}
			if(StringUtil.isNotEmpty(dto.getParentName())){
				conSql.append(" and parentId in (select sbd.basicDataId from SysBasicData sbd where sbd.typeName = :typeName)");
				proMap.put("typeName", dto.getParentName());
			}
		}
		return conSql.toString();
	}
	
	public DataPackage findPageSysBasicData(SysBasicDataDto dto,DataPackage dp){
		String hql = " from SysBasicData where 1=1 " + queryConditions(dto).toString();
		return findPageByHQL(hql, proMap, dp);
	}
	
	public List<SysBasicData> findSysBasicDataList(SysBasicDataDto dto){
		String hql = " from SysBasicData where 1=1 " + queryConditions(dto).toString();
		return findAllByHQL(hql, proMap);
	}
	
	public List findSysBasicDataCombobox(SysBasicDataDto dto){
		String hql = "select typeCode, typeName from SysBasicData where 1=1 " + queryConditions(dto).toString();
		return findAllByHQL(hql, proMap);
	}
	
	public String deleteSysBasicDataByIds(String ids) throws Exception{
		String result = "";
		try{
			List<SysBasicData> datas = findAllByHQL("from SysBasicData t where t.parentId in("+ids+")");
			if(datas != null && datas.size() > 0){
				result = "您所删除的资源里有子节点，不允许删除！";
			}else{
				bulkUpdate("delete from SysBasicData t where t.basicDataId in("+ids+")");
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