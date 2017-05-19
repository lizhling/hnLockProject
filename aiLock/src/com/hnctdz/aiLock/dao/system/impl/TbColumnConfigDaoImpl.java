/*
 * FileName:     TbColumnConfigDaoImpl.java
 * @Description: 
 * Copyright (c) 2014 Sunrise Corporation.
 * #368, Guangzhou Avenue South, Haizhu District, Guangzhou
 * All right reserved.
 *
 * Modification  History:
 * Date           Author         Version         Discription
 * -----------------------------------------------------------------------------------
 * 2014-7-13   WangXiangBo        1.0              新建
 */
package com.hnctdz.aiLock.dao.system.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.system.TbColumnConfigDao;
import com.hnctdz.aiLock.domain.system.TbColumnConfig;
import com.hnctdz.aiLock.dto.system.TbColumnConfigDto;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName TbColumnConfigDaoImpl.java
 * @Author WangXiangBo 
 * @Date 2014-7-13 下午04:19:13
 */
@Repository("TbColumnConfigDao")
public class TbColumnConfigDaoImpl extends GenericDaoImpl<TbColumnConfig, Long> implements TbColumnConfigDao{
	

	public String queryConditions(TbColumnConfigDto dto){
		StringBuffer conSql = new StringBuffer();
		if(dto != null){
			if(StringUtil.isNotEmpty(dto.getGridCode())){
				conSql.append(" and gridCode = '").append(dto.getGridCode()).append("'");
			}
			if(StringUtil.isNotEmpty(dto.getGridName())){
				conSql.append(" and gridName like '%").append(dto.getGridName()).append("%'");
			}
			if(null != dto.getStatus()){
				conSql.append(" and status = ").append(dto.getStatus());
			}
		}
		return conSql.toString();
	}
	
	public List<TbColumnConfig> findTbColumnConfig(TbColumnConfigDto dto){
		String hql = "from TbColumnConfig where 1=1 " + queryConditions(dto);
		hql += " order by lineLevel asc, resOrder asc";
		return this.findAllByHQL(hql);
	}
	
	public List<TbColumnConfig> findTbColConField(String gridCode){
		String hql = "from TbColumnConfig where field is not null and gridCode = '"+ gridCode + 
					 "' and status = 1";
		hql += " order by lineLevel asc, resOrder asc";
		return this.findAllByHQL(hql);
	}
}
