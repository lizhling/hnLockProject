package com.hnctdz.aiLock.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.hnctdz.aiLock.action.BaseAction;
import com.hnctdz.aiLock.dao.GenericDAO;
import com.hnctdz.aiLock.domain.system.SysUser;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.security.SecurityUserHolder;
import com.hnctdz.aiLock.utils.EscColumnToBean;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName GenericDaoImpl.java
 * @Author WangXiangBo 
 */
public class GenericDaoImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

	private Class<T> persistentClass;
	
	@Autowired
	private HibernateTemplate hibernateTemplate;

	@SuppressWarnings("unchecked")
	public GenericDaoImpl() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	public List<T> findAll() {
		return findByCriteria();
	}

	public void flush() {
		getHibernateTemplate().flush();
	}

	public void clear() {
		getHibernateTemplate().clear();
	}
	
	public void evict(T t) {
		getHibernateTemplate().evict(t);
	}
	

	/**
	 * 只有查询条件，没有排序的查询方法
	 * 
	 */
	public List<T> findByCriteria(Criterion... criterion) {
		DetachedCriteria crit =
				DetachedCriteria.forClass(getPersistentClass());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		return getHibernateTemplate().findByCriteria(crit);
	}
	
	/**
	 * 有条件也有排序的查询方法
	 */
	public List<T> findByCriteria(List<Order> orderList,Criterion... criterion) {
		DetachedCriteria crit =
				DetachedCriteria.forClass(getPersistentClass());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		for (Order o : orderList) {
			crit.addOrder(o);
		}
		return getHibernateTemplate().findByCriteria(crit);
	}
	
	/**
	 * 根据条件分页查找
	 */
	@SuppressWarnings("unchecked")
	public DataPackage findPageByCriteria(DataPackage dataPackage, List<Order> orderList, Criterion... criterion) {
		DetachedCriteria crit = DetachedCriteria.forClass(getPersistentClass());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		
		//获取执行查询的对象
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria executableCriteria  = crit.getExecutableCriteria(session);
        
		//获取总记录数
		Object rowCount = executableCriteria.setProjection(Projections.rowCount()).uniqueResult();
		int totalCount = 0;
		if (rowCount != null) {
			totalCount = ((Integer) rowCount).intValue();
		}
        executableCriteria.setProjection(null);
        
        //添加排序条件
        for (Order o : orderList) {
			crit.addOrder(o);
		}
        
        //分页获取
        int startIndex = dataPackage.getPageNo() * dataPackage.getPageSize();
        executableCriteria.setFirstResult(startIndex);
        executableCriteria.setMaxResults(dataPackage.getPageSize());
        List datas = executableCriteria.list();
        
        //装载数据返回
        dataPackage.setTotal(totalCount);
        dataPackage.setRows(datas);
        return dataPackage;
	}
	
	public DataPackage findPageByCriteria(DataPackage dataPackage) {
		DetachedCriteria crit = DetachedCriteria.forClass(getPersistentClass());
		//获取执行查询的对象
		Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria executableCriteria  = crit.getExecutableCriteria(session);
        
		//获取总记录数
		Object rowCount = executableCriteria.setProjection(Projections.rowCount()).uniqueResult();
		int totalCount = 0;
		if (rowCount != null) {
			totalCount = ((Integer) rowCount).intValue();  
		}
        executableCriteria.setProjection(null);
        int startIndex = dataPackage.getPageNo() * dataPackage.getPageSize();
        executableCriteria.setFirstResult(startIndex);
        executableCriteria.setMaxResults(dataPackage.getPageSize());
        List datas = executableCriteria.list();
        //装载数据返回
        dataPackage.setTotal(totalCount);
        dataPackage.setRows(datas);
        return dataPackage;
	}
	
	/* (non-Javadoc)
	 * @see com.sunrise.safsfrontend.dao.GenericDAO#findPageByCriteria(com.sunrise.safsfrontend.dto.DataPackage, org.hibernate.criterion.Criterion[])
	 */
	public List<T> findAllByCriteria(List<Criterion> list){
		DetachedCriteria crit =
			DetachedCriteria.forClass(getPersistentClass());
		for (Criterion c : list) {
			crit.add(c);
		}
		return getHibernateTemplate().findByCriteria(crit);
	}

	public DataPackage findPageByHQL(String hql,DataPackage dp) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
		q.setMaxResults(dp.getPageSize());
		dp.setRows(q.list());
		//获取总记录数 
		//dp.setTotal(count("select count(*) " + hql));
		//dp.setTotal(countByHql(hql));
		return dp;
	}
	
	public DataPackage findPageByHQL(String hql, Map propertiesMap, DataPackage dp) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		q.setProperties(propertiesMap);
		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
		q.setMaxResults(dp.getPageSize());
		dp.setRows(q.list());
		//获取总记录数 
		dp.setTotal(countByHql(hql, propertiesMap));
		return dp;
	}
	
	public DataPackage findPageByHQL(String hql, Map propertiesMap, DataPackage dp, Class<?> pojoClass) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		q.setProperties(propertiesMap);
		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
		q.setMaxResults(dp.getPageSize());
		dp.setRows(q.list());
		//获取总记录数 
		dp.setTotal(countByHql(hql, propertiesMap));
		q.setResultTransformer(new EscColumnToBean(pojoClass));
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
	
	public List<T> findAllByHQL(String hql, Map propertiesMap, Class<?> pojoClass) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		q.setCacheable(true);
		q.setProperties(propertiesMap);
		q.setResultTransformer(new EscColumnToBean(pojoClass));
		return q.list();
	}
	
	public List<T> findAllByHQL(String hql, Map propertiesMap) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		q.setCacheable(true);
		q.setProperties(propertiesMap);
		return q.list();
	}
	
	public List<T> findAllByHQL(String hql) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		return q.list();
	}
	
	public List findAllBySQL(String sql, Map propertiesMap, Class<?> pojoClass) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		q.setProperties(propertiesMap);
		q.setResultTransformer(new EscColumnToBean(pojoClass));
		return q.list();
	}
	
	public List findAllBySQL(String sql, Map propertiesMap) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		q.setProperties(propertiesMap);
		return q.list();
	}
	
	public List findAllBySQL(String sql) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		return q.list();
	}

	public Long getAreaIdByUser() {
		SysUser sysUser = BaseAction.getLoginSysUser();
		if (sysUser == null) {
			sysUser = SecurityUserHolder.getCurrentUser();
		}
		List<Long> roleIds = sysUser.getUserInRoleIds();
		for (Long id : roleIds) {
			if (id.longValue() == 1L) {
				return null;
			}
		}
		return sysUser.getAreaId();
	}

	public String addAreaPermissionHql(String[] tdAlias) {
		Long areaId = getAreaIdByUser();
		if (areaId != null) {
			String areaIds = findSonAreasByAreaId(areaId);
			if (tdAlias.length > 1) {
				return " and " + tdAlias[0] + ".areaId in(" + areaIds + ")";
			}
			return " and areaId in(" + areaIds + ")";
		}
		return "";
	}

	public String findSonAreasByAreaId(Long areaId) {
		String sql = "select getChildLst_area(" + areaId + ")";
		List list = findAllBySQL(sql);
		String orgIds = list.get(0).toString();
		return orgIds.replace("$,", "");
	}

	public String findSonOrgsByOrgId(Long orgId) {
		String sql = "select getChildLst_org(" + orgId + ")";
		List list = findAllBySQL(sql);
		String orgIds = list.get(0).toString();
		return orgIds.replace("$,", "");
	}

	public Long getOrgIds() {
		SysUser sysUser = BaseAction.getLoginSysUser();
		if (sysUser == null) {
			sysUser = SecurityUserHolder.getCurrentUser();
		}
		List<Long> roleIds = sysUser.getUserInRoleIds();
		for (Long id : roleIds) {
			if (id.longValue() == 1L) {
				return null;
			}
		}
		return sysUser.getOrgId();
	}

	public String addOrgPermissionHql(String[] tdAlias) {
		Long orgId = getOrgIds();
		if (orgId != null) {
			String orgIds = findSonOrgsByOrgId(orgId);
			if (tdAlias.length > 1) {
				return " and " + tdAlias[0] + ".orgId in(" + orgIds + ")";
			}
			return " and orgId in(" + orgIds + ")";
		}
		return "";
	}

	public String addOrgPermissionSql(String[] tdAlias) {
		Long orgId = getOrgIds();
		if (orgId != null) {
			String orgIds = findSonOrgsByOrgId(orgId);
			if (tdAlias.length > 1) {
				return " and " + tdAlias[0] + ".org_id in(" + orgIds + ")";
			}
			return " and org_id in(" + orgIds + ")";
		}
		return "";
//		return " and org_id in(select distinct t.org_id from ORG_INFO t"+
//		   " start with t.org_id in("+ sysUser.getOrgId()+
//		   " )connect by t.org_parent_id = prior t.org_id)";
	}
	
	
	public int countByHql(String countHql, Map propertiesMap) {
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
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(sb.toString());
		q.setProperties(propertiesMap);
		return ((Long) q.list().get(0)).intValue();
		
		//return ((Long) (this.getHibernateTemplate().find(sb.toString()).listIterator().next())).intValue();
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
			sb.append(regerJpql.substring(lowerJpql.indexOf("from"),regerJpql.length()));
		} catch (RuntimeException e) {
			System.out.println("解析sql查询语句时出错，请检查sql查询语句的正确性。");
			throw e;
		}
		List cont = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sb.toString()).list();
		if (cont.size() == 1) {
			return Integer.valueOf(cont.get(0).toString());
		} else {
			return cont.size();
		}
	}

	public void performHql(String hql, Map propertiesMap){
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		q.setProperties(propertiesMap);
		q.executeUpdate();
	}
	
	public void delete(T persistentInstance) {
		getHibernateTemplate().delete(persistentInstance);
	}
	
	public int bulkUpdate(String hql){
		return getHibernateTemplate().bulkUpdate(hql);
	}

	public T getById(ID id) {
		if(null != id){
			return (T) getHibernateTemplate().get(getPersistentClass(), id);
		}else{
			return null;
		}
	}

	public T merge(T detachedInstance) {
		getHibernateTemplate().merge(detachedInstance);
		return detachedInstance;
	}

	public void save(T transientInstance) {
		getHibernateTemplate().saveOrUpdate(transientInstance);
	}
	
	public void update(T transientInstance) {
		getHibernateTemplate().update(transientInstance);
	}
	
	public int findCount(String sql){
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		return Integer.valueOf(q.list().get(0)+"");
	}

	public int count(String countSql) {
		return ((Long) (this.getHibernateTemplate().find(countSql).listIterator().next())).intValue();
	}

	public void commit() {
		getHibernateTemplate().getSessionFactory().getCurrentSession().flush();
	}

	public HibernateTemplate getHibernateTemplate() {
		return this.hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate template) {
		this.hibernateTemplate = template;
	}

	public List<?> getObjectList(String sql, Object[] args) throws Exception {
	List<?> list = null;
	try {
		Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		if(args.length > 0){
			for(int i=0;i<args.length;i++){
				if(args[i] instanceof String){
					if(StringUtil.isNotBlank(String.valueOf(args[i]))){
						query.setParameter(i,String.valueOf(args[i]));
					}
				}else if(args[i] instanceof Integer){
					query.setParameter(i,Integer.parseInt(args[i].toString()));
				}else if(args[i] instanceof Long){
					query.setParameter(i,Long.parseLong(args[i].toString()));
				}
			}
		}
		list = query.list();
		} catch (Exception e) {
		e.printStackTrace();
		throw new Exception("查询数据异常："+e.getMessage());
		}
		return list;
	}
	public Object getObjectDetail(String sql, Object[] args)
			throws Exception {
	Object object = null;
	try {
		Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		if(args.length > 0){
			for(int i=0;i<args.length;i++){
				if(args[i] instanceof String){	
					if(StringUtil.isNotBlank(String.valueOf(args[i]))){
						query.setParameter(i,String.valueOf(args[i]));
					}
				}else if(args[i] instanceof Integer){
						query.setParameter(i,Integer.parseInt(args[i].toString()));
				}else if(args[i] instanceof Long){
					query.setParameter(i,Long.parseLong(args[i].toString()));
				}
			}
		}
		object = query.uniqueResult();	
	}catch (Exception e) {
		e.printStackTrace();
		throw new Exception("查询数据异常："+e.getMessage());
	}
	return object;
	}
	
	
	public void ExecuteBySql(String sql, Object[] args) {
			SQLQuery query = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery( sql);
			for (int i = 0; i < args.length; i++) {// 循环比对类型，并设置正确类型
				if (args[i] instanceof String) {
					query.setParameter(i, String.valueOf(args[i]));
				} else if (args[i] instanceof Integer) {
					query.setParameter(i, Integer.parseInt(args[i].toString()));
				} else if (args[i] instanceof Long) {
					query.setParameter(i, Long.valueOf(args[i].toString()));
				}
			}
			int exeResult = query.executeUpdate();
			if(exeResult!=1){
				System.out.println("数据插入失败！！！"); 
			}
		
	}
}

