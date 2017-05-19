package com.hnctdz.aiLock.dao.system;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.system.SysBasicData;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysBasicDataDto;

/** 
 * @ClassName SysBasicDataDao.java
 * @Author WangXiangBo 
 */
@Repository
public interface SysBasicDataDao extends GenericDAO<SysBasicData, Long>{
	/**
	 * 查询满足条件的基础数据列表，带分页
	 */
	public DataPackage findPageSysBasicData(SysBasicDataDto dto,DataPackage dp);
	
	/**
	 * 查询满足条件的基础数据List
	 */
	public List<SysBasicData> findSysBasicDataList(SysBasicDataDto dto);
	
	/**
	 * 查询基础数据类型 下拉框使用
	 */
	public List<SysBasicData> findSysBasicDataCombobox(SysBasicDataDto dto);
	
	/**
	 * 批量删除基础数据信息
	 */
	public String deleteSysBasicDataByIds(String ids) throws Exception;
	
}
