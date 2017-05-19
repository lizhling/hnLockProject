/*
 * FileName:     ScanerrorDaoImpl.java
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
package com.hnctdz.aiLock.dao.system.impl;

import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.system.ScanerrorDao;
import com.hnctdz.aiLock.domain.system.Scanerror;

/** 
 * @ClassName ScanerrorDaoImpl.java
 * @Author WangXiangBo 
 * @Date 2014-9-24 上午11:21:03
 */
@Repository("ScanerrorDao")
public class ScanerrorDaoImpl extends GenericDaoImpl<Scanerror, String> implements ScanerrorDao{


}
