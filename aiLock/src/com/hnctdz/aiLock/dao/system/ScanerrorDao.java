/*
 * FileName:     ScanerrorDao.java
 * @Description: 
 * Copyright (c) 2014 Sunrise Corporation.
 * #368, Guangzhou Avenue South, Haizhu District, Guangzhou
 * All right reserved.
 *
 * Modification  History:
 * Date           Author         Version         Discription
 * -----------------------------------------------------------------------------------
 * 2014-9-24   WangXiangBo        1.0              新建
 */
package com.hnctdz.aiLock.dao.system;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.system.Scanerror;

/** 
 * @ClassName ScanerrorDao.java
 * @Author WangXiangBo 
 * @Date 2014-9-24 上午11:20:56
 */

@Repository
public interface ScanerrorDao extends GenericDAO<Scanerror, String>{

}
