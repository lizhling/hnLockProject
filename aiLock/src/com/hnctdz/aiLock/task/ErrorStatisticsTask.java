package com.hnctdz.aiLock.task;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.CannotCreateTransactionException;

import com.hnctdz.aiLock.service.system.ScanerrorService;
import com.hnctdz.aiLock.utils.Constants;
import com.hnctdz.aiLock.utils.DateUtil;
import com.hnctdz.aiLock.utils.StringUtil;

/** 
 * @ClassName ErrorStatisticsTask.java
 * @Author WangXiangBo 
 */
public class ErrorStatisticsTask {
	
	private static final Logger scanLog = Logger.getLogger(ErrorStatisticsTask.class);
	
	@Autowired
	private ScanerrorService scanerrorService;
	
	public void errorStatistics(){
		try {
			String inScope = Constants.ERROR_IN_SCOPE;//异常发生范围（单位：分钟）
			if(StringUtil.isEmpty(inScope)){
				inScope = "5";
			}
			
			Date date = new Date();
			//异常发生截止时间
			String transEndTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", date);
			
			long dateTime = date.getTime();
			long startTime = dateTime - (Long.parseLong(inScope) * 1000 * 60);
			date.setTime(startTime);
			//异常发生开始时间
			String transStartTime = DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", date);
			
			List list = scanerrorService.findSysBasicDataList(transStartTime, transEndTime);
			String smsContent = transStartTime + "至" + transEndTime.substring(11, 19) + " ";
			String errors = "";
			for(int i=0; i < list.size(); i++){
				Object[] obj = (Object[])list.get(i);
				String serverIp = obj[0] == null ? "未知" : obj[0].toString();
				String errorDesc = obj[1] == null ? "" : obj[1].toString();
				String con = obj[2] == null ? "" : obj[2].toString();
				
				errors += serverIp + "：" + errorDesc + con + "次;";
			}
			
			if(StringUtil.isNotEmpty(errors)){
				//写入发送短信log文件
				scanLog.info(smsContent + errors);
			}
		} catch (GenericJDBCException e) {
			String message =  e.getMessage();
			if(message.indexOf("org.hibernate.exception.GenericJDBCException: Cannot open connection") > 0){
				scanLog.info(DateUtil.getDateTime()+": 数据库连接异常1次;");
			}
		} catch (CannotCreateTransactionException e) {
			String message =  e.getMessage();
			if(message.indexOf("org.hibernate.exception.GenericJDBCException: Cannot open connection") > 0){
				scanLog.info(DateUtil.getDateTime()+": 数据库连接异常1次;");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
