/*
 * FileName:     CommonDao.java
 * @Description: 
 * Copyright (c) 2014 Sunrise Corporation.
 * #368, Guangzhou Avenue South, Haizhu District, Guangzhou
 * All right reserved.
 *
 * Modification  History:
 * Date           Author         Version         Discription
 * -----------------------------------------------------------------------------------
 * 2014-4-1   WangXiangBo        1.0              新建
 */
package com.hnctdz.aiLock.dao;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateTemplate;


import com.hnctdz.aiLock.dto.DataPackage;


/** 
 * @ClassName CommonDao.java
 * @Author WangXiangBo 
 * @Date 2014-4-1 下午05:02:33
 */
public interface CommonDao {
	
	public DataPackage findPageByHQL(String hql,DataPackage dp);
	
	public DataPackage findPageBySql(String sql,DataPackage dp);
	
	public DataPackage findPageBySqlToBean(String sql,DataPackage dp, Class<?> pojoClass);
	
	public List<?> findAllBySQLToBean(String sql,Class<?> pojoClass);

	public List findAllByHQL(String hql);
	
	public List findAllBySQL(String sql);
	
	public int saveTbToSql(String sql);
	
	public void saveOrUpdate(Object tbObj);
	
	public void save(Object tbObj);
	
	public void update(Object tbObj);
	
	public HibernateTemplate getHibernateTemplate();
}
