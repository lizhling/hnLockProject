/*
 * FileName:     TbColumnConfigService.java
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
package com.hnctdz.aiLock.service.system;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hnctdz.aiLock.domain.system.TbColumnConfig;
import com.hnctdz.aiLock.dto.system.TbColumnConfigDto;

/** 
 * @ClassName TbColumnConfigService.java
 * @Author WangXiangBo 
 * @Date 2014-7-13 下午04:24:30
 */
@Service
public interface TbColumnConfigService {
	
	public List<TbColumnConfig> findTbColumnConfig(TbColumnConfigDto dto);
	
	public List<TbColumnConfig> findTbColConField(String gridCode);

}
