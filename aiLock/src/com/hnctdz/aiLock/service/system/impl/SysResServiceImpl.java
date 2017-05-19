package com.hnctdz.aiLock.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnctdz.aiLock.dao.system.SysResDao;
import com.hnctdz.aiLock.domain.system.SysRes;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.SysResDto;
import com.hnctdz.aiLock.service.system.SysResService;

/** 
 * @ClassName SysResServiceImpl.java
 * @Author WangXiangBo 
 */
@Service("com.hnctdz.aiLock.service.system.SysResService")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
public class SysResServiceImpl implements SysResService{
	@Autowired
	private SysResDao sysResDao;
	
	public DataPackage findPageSysRes(SysResDto dto,DataPackage dp){
		dp = sysResDao.findPageSysRes(dto, dp);
		List<SysRes> datas = (List<SysRes>) dp.getRows();
 		for(SysRes data:datas){
 			if(data.getResParentId()!=null){
 				SysRes oldSysRes = sysResDao.getById(Long.valueOf(data.getResParentId()));
 	 			data.setParentResName(oldSysRes != null ? oldSysRes.getResName() : "");
 			}
 		}
		return dp;
	}
	
	public SysRes getById(Long id){
		return sysResDao.getById(id);
	}
	
	public void saveSysRes(SysRes sysRes){
		sysResDao.save(sysRes);
	}
	
	public String deleteSysResByIds(String ids)throws Exception{
		String result = "";
		try{
			result = sysResDao.deleteSysResByIds(ids);
		}catch(Exception e){
			result = "此资源还有有其他关联信息！";
			e.printStackTrace();
		}finally{
			return result;
		}
	}
	
	public List<SysRes> findSysResList(SysResDto dto){
//		if(dto != null){
//			if(dto.getResType() != null){
//				SimpleExpression resTypeEQ = Restrictions.eq("resType", dto.getResType());
//			}
//		}
//		
//		List<Order> orderList = new ArrayList<Order> ();
//		orderList.add(Order.asc("resType"));
//		orderList.add(Order.asc("resOrder"));
//		orderList.add(Order.asc("resId"));
		return sysResDao.findSysResList(dto);
	}
	
	public List<SysRes> findMenuResByRoleIds(List<Long> userInRoleIds, int resType){
		return sysResDao.findMenuResByRoleIds(userInRoleIds, resType);
	}
}
