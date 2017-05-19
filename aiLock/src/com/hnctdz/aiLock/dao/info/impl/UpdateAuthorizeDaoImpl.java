package com.hnctdz.aiLock.dao.info.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.info.UpdateAuthorizeDao;
import com.hnctdz.aiLock.domain.info.UpdateAuthorize;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.info.UpdateAuthorizeDto;
import com.hnctdz.aiLock.utils.DateUtil;

/** 
 * @ClassName UpdateAuthorizeDaoImpl.java
 * @Author WangXiangBo 
 */
@Repository("UpdateAuthorizeDao")
public class UpdateAuthorizeDaoImpl extends GenericDaoImpl<UpdateAuthorize, Long> implements UpdateAuthorizeDao{
	
	private Map<String, Object> proMap;
	
	public String queryConditions(UpdateAuthorizeDto dto){
		proMap = new HashMap<String, Object>();
		StringBuffer conSql = new StringBuffer();
		if(dto != null){
			if(null != dto.getPerId()){
				conSql.append(" and perId = :perId");
				proMap.put("perId", dto.getPerId());
			}
			if(null != dto.getStatus()){
				conSql.append(" and status = :status");
				proMap.put("status", dto.getStatus());
			}
		}
		return conSql.toString();
	}
	
	public DataPackage findPageUpdateAuthorize(UpdateAuthorizeDto dto,DataPackage dp){
		String hql = " from UpdateAuthorize g where 1=1 " + queryConditions(dto);
		return findPageByHQL(hql, proMap, dp);
	}
	
	public List<UpdateAuthorize> findUpdateAuthorizeList(UpdateAuthorizeDto dto){
		String hql = " from UpdateAuthorize g where 1=1 " + queryConditions(dto);
		return findAllByHQL(hql, proMap);
	}
	
	public Boolean UpdateAuthorizeToChange(Long perId){
		String hql = " update UpdateAuthorize u set u.changeTime = '"+DateUtil.getDateTime()+ "', u.status = 1" +
					 " where u.perId = " + perId;
		int i = this.bulkUpdate(hql);
		if(i <= 0){
			UpdateAuthorize ua = new UpdateAuthorize();
			ua.setPerId(perId);
			ua.setChangeTime(new Date());
			ua.setStatus(0L);
			this.save(ua);
		}
		return true;
	}
	
	public void UpdateAuthorizeToUpdate(Long perId){
		String hql = " update UpdateAuthorize u set u.updateTime = '"+DateUtil.getDateTime()+ "', u.status = 0" +
					 " where u.perId = " + perId;
		this.bulkUpdate(hql);
	}
	
	public String deleteUpdateAuthorizeByIds(String groupIds){
		String result = "";
		try{
			bulkUpdate("delete from UpdateAuthorize t where t.groupId in("+groupIds+")");
		}catch(DataIntegrityViolationException e){
			result = "该分组被其它数据关联，不能删除！";
			e.printStackTrace();
		}catch(Exception e){
			result = "数据库资源出错！";
			e.printStackTrace();
		}finally{
			return result;
		}
	}
	
}
