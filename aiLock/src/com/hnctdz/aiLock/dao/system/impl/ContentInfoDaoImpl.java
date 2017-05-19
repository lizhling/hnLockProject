/*
 * FileName:     ContentInfoDaoImpl.java
 * @Description: 
 * Copyright (c) 2014 Sunrise Corporation.
 * #368, Guangzhou Avenue South, Haizhu District, Guangzhou
 * All right reserved.
 *
 * Modification  History:
 * Date           Author         Version         Discription
 * -----------------------------------------------------------------------------------
 * 2014-1-15   WangXiangBo        1.0              新建
 */
package com.hnctdz.aiLock.dao.system.impl;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.hnctdz.aiLock.dao.impl.GenericDaoImpl;
import com.hnctdz.aiLock.dao.system.ContentInfoDao;
import com.hnctdz.aiLock.domain.system.ContentInfo;
import com.hnctdz.aiLock.dto.DataPackage;
import com.hnctdz.aiLock.dto.system.ContentInfoDto;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName ContentInfoDaoImpl.java
 * @Author WangXiangBo 
 * @Date 2014-1-15 上午09:21:45
 */
@Repository("ContentInfoDao")
public class ContentInfoDaoImpl extends GenericDaoImpl<ContentInfo, Long> implements ContentInfoDao{

	public String queryConditions(ContentInfoDto dto){
		StringBuffer conSql = new StringBuffer();
		if(dto != null){
			if(StringUtil.isNotEmpty(dto.getTitle())){
				conSql.append(" and title like '%").append(dto.getTitle()).append("%'");
			}
			if(StringUtil.isNotEmpty(dto.getSubtitle())){
				conSql.append(" and subtitle like '%").append(dto.getSubtitle()).append("%'");
			}
			if(null != dto.getContentType()){
				conSql.append(" and contentType = ").append(dto.getContentType());
			}
			if(null != dto.getStatus()){
				conSql.append(" and status = ").append(dto.getStatus());
			}
			if(StringUtil.isNotEmpty(dto.getContentIds())){
				conSql.append(" and contentIds in(").append(dto.getContentIds()).append(")");
			}
		}
		return conSql.toString();
	}
	
	public DataPackage findPageContentInfo(ContentInfoDto dto,DataPackage dp){
		String hql = " from ContentInfo where 1=1 " + queryConditions(dto);
		return findPageByHQL(hql, dp);
	}
	
	public List<ContentInfo> findContentInfoList(ContentInfoDto dto){
		String hql = " from ContentInfo where 1=1 " + queryConditions(dto);
		return findAllByHQL(hql);
	}
	
	public String deleteContentInfoByIds(String ids) throws Exception{
		String result = "";
		try{
			bulkUpdate("delete from ContentInfo t where t.contentId in("+ids+")");
		}catch(DataIntegrityViolationException e){
			result = "该资源被其它数据关联，不能删除！";
			e.printStackTrace();
		}catch(Exception e){
			result = "数据库资源出错！";
			e.printStackTrace();
		}finally{
			return result;
		}
	}
}

