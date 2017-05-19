package com.hnctdz.aiLock.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.hnctdz.aiLock.dao.CommonDao;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.utils.EscColumnToBean;

/** 
 * @ClassName CommonDaoImpl.java
 * @Author WangXiangBo 
 */
public class CommonDaoImpl implements CommonDao{
	
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	public DataPackage findPageByHQL(String hql,DataPackage dp) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
		q.setMaxResults(dp.getPageSize());
		dp.setRows(q.list());
		//获取总记录数 
		dp.setTotal(countByHql(hql));
		return dp;
	}
	
	public DataPackage findPageBySql(String sql,DataPackage dp) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
		q.setMaxResults(dp.getPageSize());
		dp.setRows(q.list());
		//获取总记录数
		dp.setTotal(countBySql(sql));
		return dp;
	}
	
	public DataPackage findPageBySqlToBean(String sql,DataPackage dp, Class<?> pojoClass) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
		q.setMaxResults(dp.getPageSize());
		q.setResultTransformer(new EscColumnToBean(pojoClass));
		
		dp.setRows(q.list());
		//获取总记录数
		dp.setTotal(countBySql(sql));
		return dp;
	}
	
	/*public DataPackage findPageBySqlToBean(String sql,DataPackage dp, Class<?> pojoClass,ErrorDto dto) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);

		if(dto != null){
			int i=0;
			if(StringUtil.isNotEmpty(dto.getContext())){
				q.setString(i,"%"+dto.getContext()+"%");
			}
			if(StringUtil.isNotEmpty(dto.getDeviceModel())){
				i++;
				q.setString(i, dto.getDeviceModel());
			}
			
		}
		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
		q.setMaxResults(dp.getPageSize());
		q.setResultTransformer(new EscColumnToBean(pojoClass));
		
		dp.setRows(q.list());
		//获取总记录数
		Query countQ = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		if(dto != null){
			int i=0;
			if(StringUtil.isNotEmpty(dto.getContext())){
				countQ.setString(i,"%"+dto.getContext()+"%");
				i++;
			}
			if(StringUtil.isNotEmpty(dto.getDeviceModel())){
				countQ.setString(i, dto.getDeviceModel());
			}
		}
		int countV = countQ.list().size();
		dp.setTotal(countV);
		return dp;
	}*/
	
	
	
	public List findAllByHQL(String hql) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		return q.list();
	}
	
	public List findAllBySQL(String sql) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		return q.list();
	}
	
	public List<?> findAllBySQLToBean(String sql,Class<?> pojoClass) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		q.setResultTransformer(new EscColumnToBean(pojoClass));
		return q.list();
	}
	
	public int countByHql(String countHql) {
		String regerJpql = countHql.trim();
		regerJpql = regerJpql.replaceAll("\\s+", " ");

		String lowerJpql = regerJpql.toLowerCase();
		int orderIndex = lowerJpql.indexOf("order by");

		// 去掉结尾的order by语句
		if (orderIndex != -1) {
			String afterOrder = lowerJpql.substring(orderIndex, lowerJpql.length());
			if (afterOrder.indexOf(")") == -1) {
				regerJpql = regerJpql.substring(0, orderIndex);
			}
		}

		StringBuilder sb = new StringBuilder();
		try {
			sb.append("select count(*) ");
			sb.append(regerJpql.substring(lowerJpql.indexOf("from"),regerJpql.length()));
		} catch (RuntimeException e) {
			System.out.println("解析sql查询语句时出错，请检查sql查询语句的正确性。");
			throw e;
		}
		return ((Long) (this.getHibernateTemplate().find(sb.toString()).listIterator().next())).intValue();
	}
	
	public int countBySql(String countSql) {
		String regerJpql = countSql.trim();
		regerJpql = regerJpql.replaceAll("\\s+", " ");

		String lowerJpql = regerJpql.toLowerCase();
		int orderIndex = lowerJpql.indexOf("order by");

		// 去掉结尾的order by语句
		if (orderIndex != -1) {
			String afterOrder = lowerJpql.substring(orderIndex, lowerJpql.length());
			if (afterOrder.indexOf(")") == -1) {
				regerJpql = regerJpql.substring(0, orderIndex);
			}
		}

		StringBuilder sb = new StringBuilder();
		try {
			sb.append("select count(*) ");
			sb.append(lowerJpql.substring(lowerJpql.indexOf("from"),regerJpql.length()));
		} catch (RuntimeException e) {
			System.out.println("解析sql查询语句时出错，请检查sql查询语句的正确性。");
			throw e;
		}
		List cont = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sb.toString()).list();
		if (sb.toString().contains("group by")) {
			return cont.size();
		} else {
			return Integer.valueOf(cont.get(0).toString());
		}
	}
	
	public int saveTbToSql(String sql){
		return this.getHibernateTemplate().getSessionFactory().getCurrentSession()
				.createSQLQuery(sql).executeUpdate();
	}
	
	public void saveOrUpdate(Object tbObj) {
		getHibernateTemplate().saveOrUpdate(tbObj);
	}
	
	public void save(Object tbObj) {
		getHibernateTemplate().save(tbObj);
	}
	
	public void update(Object tbObj) {
		getHibernateTemplate().update(tbObj);
	}
	
	public HibernateTemplate getHibernateTemplate() {
		return this.hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate template) {
		this.hibernateTemplate = template;
	}
}
