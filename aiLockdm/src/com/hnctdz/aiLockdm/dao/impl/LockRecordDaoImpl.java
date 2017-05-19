package com.hnctdz.aiLockdm.dao.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.hnctdz.aiLockdm.dao.LockRecordDao;
import com.hnctdz.aiLockdm.utils.CommandTypeUtil;
import com.hnctdz.aiLockdm.utils.DateUtil;
import com.hnctdz.aiLockdm.utils.ErrorCodeException;
import com.hnctdz.aiLockdm.utils.LockStatusRecords;
import com.hnctdz.aiLockdm.utils.StringUtil;
import com.hnctdz.aiLockdm.utils.UnlockRecords;

/** 
 * @ClassName LockRecordDaoImpl.java
 * @Author WangXiangBo 
 */
public class LockRecordDaoImpl  extends HibernateDaoSupport implements LockRecordDao{
	private static Log log = LogFactory.getLog("LockRecordDaoImpl");
	
	public void saveJob(Object obj){
		this.getHibernateTemplate().save(obj);
	}
	
	public void saveOrUpdateJob(Object obj){
		this.getHibernateTemplate().saveOrUpdate(obj);
	}
	
	public Object merge(Object obj) {
		this.getHibernateTemplate().merge(obj);
		return obj;
	}
	
	public void clear() {
		this.getHibernateTemplate().clear();
	}
	
	/*
	 * 门禁记录入库
	 */
	public void saveRecordCommand(UnlockRecords urs) throws ErrorCodeException{
		Session session = null;
		try{
			if(CommandTypeUtil.getLockRecordType(urs.getRecordCode()) != null){
				session = getSession();
				String recordId = "";
				String lockCode = urs.getLockInModuleCode().substring(12, 24) + urs.getLockDeviceNo();
				if(urs.getKeyCode() != null && "0000000000000007".equalsIgnoreCase(urs.getKeyCode())){
					//查询该门锁编码当前时间的前10分钟至后5分钟的开锁记录进行匹配合并。
					String sql = 
						"SELECT ur.PER_ID, ur.UNLOCK_TIME, ur.MESSAGE, ur.NOTE, ur.RECORD_ID "+
						"  FROM unlock_records ur WHERE ur.LOCK_CODE = '"+lockCode+"' AND ur.REMOTE_UNLOCK_RESULTS IN('80','89','81','83')"+
						"   AND ur.UNLOCK_TIME >= DATE_ADD('"+urs.getUnlockTime()+"',INTERVAL -10 SECOND) AND ur.UNLOCK_TIME <= DATE_ADD('"+urs.getUnlockTime()+"',INTERVAL 5 SECOND)"+
						" ORDER BY ur.UNLOCK_TIME DESC LIMIT 1";
					List list = session.createSQLQuery(sql).list();
					if(list.size() > 0){
						Object[] obj = (Object[])list.get(0);
						urs.setPerId(Integer.parseInt(obj[0].toString()));
						urs.setUnlockTime(obj[1].toString());
						urs.setMessage(obj[2].toString());
						urs.setNote(obj[3].toString());
						recordId = obj[4].toString();
					}
				}
				
				String insertSql = "insert into unlock_records(LOCK_TYPE, RECORD_CODE, RECORD_TPYE, KEY_CODE, LOCK_IN_MODULE_CODE, " +
				"LOCK_DEVICE_NO, LOCK_CODE, UNLOCK_TIME, UPLOAD_TIME, MESSAGE, NOTE";
				if(urs.getPerId() == null){
					insertSql += ") values(2,?,?,?,?,?,?,?,?,?,?)";
				}else{
					insertSql += ", PER_ID) values(2,?,?,?,?,?,?,?,?,?,?,?)";
				}
				SQLQuery query = session.createSQLQuery(insertSql);
				query.setString(0, urs.getRecordCode());
				query.setString(1, urs.getRecordTpye());
				query.setString(2, urs.getKeyCode());
				query.setString(3, urs.getLockInModuleCode());
				query.setString(4, urs.getLockDeviceNo());
				query.setString(5, lockCode);
				query.setString(6, urs.getUnlockTime());
				query.setString(7, DateUtil.getDateTime());
				query.setString(8, urs.getMessage());
				query.setString(9, urs.getNote());
				if(urs.getPerId() != null){
					query.setInteger(10, urs.getPerId());
				}
				query.executeUpdate();
				
				if(StringUtil.isNotEmpty(recordId)){
					String sql = "DELETE FROM unlock_records WHERE RECORD_ID = " + recordId;
					session.createSQLQuery(sql).executeUpdate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorCodeException(e.getMessage());
		}finally{
			if(session != null){
				session.close();
			}
		}
	}
	
	public boolean findLockRecordsIfHave(String lockCode, String recordCode){
		Session session = null;
		try{
			session = getSession();
			String sql = 
				"SELECT ur.UNLOCK_TIME FROM unlock_records ur " +
				" WHERE ur.LOCK_CODE = '"+lockCode+"' AND ur.RECORD_CODE = '"+recordCode+"' "+
				"   AND ur.UNLOCK_TIME >= DATE_ADD(SYSDATE(),INTERVAL -5 SECOND) "+
				" ORDER BY ur.UNLOCK_TIME DESC LIMIT 1";
			List list = session.createSQLQuery(sql).list();
			if(list.size() > 0){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session != null){
				session.close();
			}
		}
		return false;
	}
	
	public boolean findLockAlarm(String lockCode, String alarmCode){
		Session session = null;
		try{
			session = getSession();
			String sql = 
				"SELECT ur.PER_ID, ur.UNLOCK_TIME, ur.MESSAGE, ur.NOTE, ur.RECORD_ID "+
				"  FROM unlock_records ur WHERE ur.LOCK_CODE = '"+lockCode+"' AND ur.RECORD_CODE = '"+alarmCode+"' "+
				"   AND ur.UNLOCK_TIME >= DATE_ADD(SYSDATE(),INTERVAL -5 SECOND) "+
				" ORDER BY ur.UNLOCK_TIME DESC LIMIT 1";
			List list = session.createSQLQuery(sql).list();
			if(list.size() > 0){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(session != null){
				session.close();
			}
		}
		return false;
	}
	
	public void saveLockStatusRecords(LockStatusRecords lsr) throws ErrorCodeException {
		Session session = null;
//		Connection conn = null;
//		PreparedStatement stm = null;
		try{
			session = getSession();
			String lockCode = lsr.getLockInModuleCode().substring(12, 24) + lsr.getLockDeviceNo();
			String sql = "insert into lock_status_records (LOCK_CODE, LOCK_IN_MODULE_CODE, " +
					 "LOCK_DEVICE_NO, MENCI_STATUS, BUFANG_STATUS, BAOJING_STATUS, YULIU_STATUS, " +
					 "JIXIEYAOSHI_STATUS, XIESHE_STATUS, SHANGSHUO_STATUS, MENGUANHAO_STATUS, RENYIKA_STATUS, " +
					 "MESSAGE, REPORT_TIME) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLQuery query = session.createSQLQuery(sql);
			query.setString(0, lockCode);
			query.setString(1, lsr.getLockInModuleCode());
			query.setString(2, lsr.getLockDeviceNo());
			query.setString(3, lsr.getMenciStatus());
			query.setString(4, lsr.getBufangStatus());
			query.setString(5, lsr.getBaojingStatus());
			query.setString(6, lsr.getYuliuStatus());
			query.setString(7, lsr.getJixieyaoshiStatus());
			query.setString(8, lsr.getXiesheStatus());
			query.setString(9, lsr.getShangshuoStatus());
			query.setString(10, lsr.getMenguanhaoStatus());
			query.setString(11, lsr.getRenyikaStatus());
			query.setString(12, lsr.getMessage());
			query.setString(13, DateUtil.getDateTime());
			query.executeUpdate();
			/*conn = getConnection();
			
			stm = conn.prepareStatement("insert into lock_status_records (LOCK_CODE, LOCK_IN_MODULE_CODE, " +
					 "LOCK_DEVICE_NO, MENCI_STATUS, BUFANG_STATUS, BAOJING_STATUS, YULIU_STATUS, " +
					 "JIXIEYAOSHI_STATUS, XIESHE_STATUS, SHANGSHUO_STATUS, MENGUANHAO_STATUS, RENYIKA_STATUS, " +
					 "MESSAGE, REPORT_TIME) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			stm.setString(1, lockCode);
			stm.setString(2, lsr.getLockInModuleCode());
			stm.setString(3, lsr.getLockDeviceNo());
			stm.setString(4, lsr.getMenciStatus());
			stm.setString(5, lsr.getBufangStatus());
			stm.executeUpdate();
			conn.commit();*/
        } catch (Exception e) {
        	throw new ErrorCodeException(e.getMessage());
		}finally{
			if(session != null){
				session.close();
			}
//			closePreparedStatement(stm);
//			closeConnection(conn);
		}
	}
}
