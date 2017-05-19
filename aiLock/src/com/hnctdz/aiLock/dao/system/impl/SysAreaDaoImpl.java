package com.hnctdz.aiLock.dao.system.impl;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.system.SysAreaDao;
import com.hnctdz.aiLock.domain.system.SysArea;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysAreaDto;
import com.hnctdz.aiLock.utils.StringUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository("SysAreaDao")
public class SysAreaDaoImpl extends GenericDaoImpl<SysArea, Long> implements SysAreaDao {
	private Map<String, Object> proMap;

	public String queryConditions(SysAreaDto dto) {
		this.proMap = new HashMap();
		StringBuffer conSql = new StringBuffer();
		if (dto != null) {
			if (StringUtil.isNotEmpty(dto.getAreaName())) {
				conSql.append(" and areaName like :areaName");
				this.proMap.put("areaName", "%" + dto.getAreaName() + "%");
			}
			if (dto.getParentId() != null) {
				conSql.append(" and parentId = :parentId");
				this.proMap.put("parentId", dto.getParentId());
			}
		}
		conSql.append(addAreaPermissionHql(new String[0]));
		return conSql.toString();
	}

	public DataPackage findPageSysArea(SysAreaDto dto, DataPackage dp) {
		String hql = "from SysArea where 1=1 " + queryConditions(dto)
				+ " order by parentId asc, areaOrder asc";
		return findPageByHQL(hql, this.proMap, dp);
	}

	public String findAreaPermissionByUserArea(Long areaId) {
		return findSonAreasByAreaId(areaId);
	}

	public List<SysArea> findSysAreaList(SysAreaDto dto) {
		String hql = "from SysArea where 1=1 " + queryConditions(dto)
				+ " order by parentId asc, areaOrder asc";
		return findAllByHQL(hql, this.proMap);
	}
	
	public List<SysArea> findSysAreaLowerNodesList(Long areaId) {
		String hql = "from SysArea where areaId in("+findSonAreasByAreaId(areaId)+")";
		return findAllByHQL(hql);
	}
}