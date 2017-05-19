/*
 * FileName:     TbColumnConfigDao.java
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
package com.hnctdz.aiLock.dao.system;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.system.TbColumnConfig;
import com.hnctdz.aiLock.dto.system.TbColumnConfigDto;

/** 
 * @ClassName TbColumnConfigDao.java
 * @Author WangXiangBo 
 * @Date 2014-7-13 下午04:19:03
 */
@Repository
public interface TbColumnConfigDao extends GenericDAO<TbColumnConfig, Long>{

	public List<TbColumnConfig> findTbColumnConfig(TbColumnConfigDto dto);
	
	public List<TbColumnConfig> findTbColConField(String gridCode);
}
