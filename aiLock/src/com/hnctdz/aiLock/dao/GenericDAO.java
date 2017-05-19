package com.hnctdz.aiLock.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import com.hnctdz.aiLock.domain.info.PersonnelInfo;
import com.hnctdz.aiLock.dto.DataPackage;

/** 
 * @ClassName GenericDAO.java
 * @Author WangXiangBo 
 */
public interface GenericDAO<T, ID extends Serializable> {
	
	/**
	 * 添加记录
	 * @param transientInstance
	 */
	public void save(T transientInstance);
	
	public void performHql(String hql, Map propertiesMap);
	
	/**
	 * 修改记录
	 * @param transientInstance
	 */
	public void update(T transientInstance);

	/**
	 * 删除记录
	 * @param persistentInstance
	 */
	public void delete(T persistentInstance);
	
	/**
	 * 合并保存
	 * @param detachedInstance
	 * @return
	 */
	public T merge(T detachedInstance);

	/**
	 * 根据ID查找
	 * @param id
	 * @return
	 */
	public T getById(ID id);
	
	/**
	 * 查找所有记录
	 * @return
	 */
	public List<T> findAll();

	/**
	 * 根据条件查找所有记录
	 * @param criterion ->查询条件集合
	 * @return
	 */
	public List<T> findByCriteria(Criterion... criterion);
	
	/**
	 * 根据查询条件与排序方法查找所有记录
	 * @param orderList ->排序条件集合
	 * @param criterion ->查询条件集合
	 * @return
	 */
	public List<T> findByCriteria(List<Order> orderList,Criterion... criterion);
	
	/**
	 * 根据条件分页查找
	 * @param dp ->数据集合传输类
	 * @param orderList ->排序条件集合
	 * @param criterion ->查询条件集合
	 * @return
	 */
	public DataPackage findPageByCriteria(DataPackage dp, List<Order> orderList, Criterion... criterion);
	
	/**
	 * 无任何条件的分页查找
	 * @param dp ->数据集合传输类
	 * @return
	 */
	public DataPackage findPageByCriteria(DataPackage dp);
	
	/**
	 * 根据多个条件查找所有记录
	 * @param list ->查询条件集合
	 * @return
	 */
	public List<T> findAllByCriteria(List<Criterion> list);
	
	/**
	 * 根据sql分页查询结果
	 * @param sql ->SQL语句
	 * @param dp ->数据集合传输类
	 * @return
	 */
	public DataPackage findPageBySql(String sql,DataPackage dp);
	
	/**
	 * 根据hql分页查询结果
	 * @param hql ->HQL语句
	 * @param dp ->数据集合传输类
	 * @return
	 */
	public DataPackage findPageByHQL(String hql,Map propertiesMap,DataPackage dp);
	
	public List<T> findAllByHQL(String hql, Map propertiesMap, Class<?> pojoClass);
	
	/**
	 * 根据hql查询所有结果
	 * @param hql ->HQL语句
	 * @return
	 */
	public List<T> findAllByHQL(String hql,Map propertiesMap);
	
	public List findAllBySQL(String hql, Map propertiesMap, Class<?> pojoClass);
	
	public List findAllBySQL(String sql, Map propertiesMap);
	
	public List<T> findAllByHQL(String hql);
	
	/**
	 * 根据sql查询所有结果
	 * @param sql ->SQL语句
	 * @return
	 */
	public List findAllBySQL(String sql);
	
	public String addOrgPermissionHql(String... tdAlias);
	
	public String addOrgPermissionSql(String... tdAlias);

	/**
	 * 提交数据保存到数据库中（会令事务回滚失效，小心操作）
	 */
	public void flush();

	/**
	 * 清除缓存的数据
	 */
	public void clear();
	/**
	 * 清除指定的缓存对象
	 * @param t
	 */
	public void evict(T t);
	
	public int count(String countSql);
	
	public int findCount(String sql);
	
	public void commit();
	
	/**
	 * 跟据方法查询数据
	 * @param sql   语句
	 * @param args  参数
	 * @return  味一OBJECT值
	 * @throws Exception
	 */
	public Object getObjectDetail(String queryStr,Object[] args) throws Exception;
	
	
	/**
	 * 新增方法，跟跟sql查信息
	 * @param sql   语句
	 * @param args  参数
	 * @param currentPage 当前页
	 * @param pageSize    每页展示数
	 * @return
	 * @throws Exception
	 */
	public List<?> getObjectList(String sql, Object[] args) throws Exception;
	
	
	/**
	 * 执行sql
	 * @param sql
	 * @param args
	 */
	public void ExecuteBySql(String sql, Object[] args);
	
}