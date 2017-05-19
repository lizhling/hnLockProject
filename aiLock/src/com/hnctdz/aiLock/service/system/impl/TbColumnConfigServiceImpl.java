/*
 * FileName:     TbColumnConfigServiceImpl.java
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
package com.hnctdz.aiLock.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.system.TbColumnConfigDao;
import com.hnctdz.aiLock.domain.system.TbColumnConfig;
import com.hnctdz.aiLock.dto.system.TbColumnConfigDto;
import com.hnctdz.aiLock.service.system.TbColumnConfigService;

/** 
 * @ClassName TbColumnConfigServiceImpl.java
 * @Author WangXiangBo 
 * @Date 2014-7-13 下午04:24:40
 */
@Service("TbColumnConfigService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class TbColumnConfigServiceImpl implements TbColumnConfigService{
	@Autowired
	private TbColumnConfigDao tbColumnConfigDao;
	
	public List<TbColumnConfig> findTbColumnConfig(TbColumnConfigDto dto){
		return tbColumnConfigDao.findTbColumnConfig(dto);
	}
	
	public List<TbColumnConfig> findTbColConField(String gridCode){
		return tbColumnConfigDao.findTbColConField(gridCode);
	}
}
