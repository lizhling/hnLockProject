package com.hnctdz.aiLock.dao.system;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.system.SysArea;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysAreaDto;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public abstract interface SysAreaDao extends GenericDAO<SysArea, Long> {
	public abstract DataPackage findPageSysArea(SysAreaDto paramSysAreaDto,DataPackage paramDataPackage);

	public abstract List<SysArea> findSysAreaList(SysAreaDto paramSysAreaDto);

	public abstract String findAreaPermissionByUserArea(Long paramLong);
	
	public List<SysArea> findSysAreaLowerNodesList(Long areaId);
}