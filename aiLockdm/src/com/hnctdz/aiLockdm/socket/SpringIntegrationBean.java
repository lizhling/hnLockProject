package com.hnctdz.aiLockdm.socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.hnctdz.aiLockdm.dao.DevLockInfoDao;
import com.hnctdz.aiLockdm.dao.LockRecordDao;
import com.hnctdz.aiLockdm.utils.CommonUtil;


/** 
 * @ClassName SpringIntegrationBean.java
 * @Author WangXiangBo 
 */
public class SpringIntegrationBean implements BeanFactoryAware {
	private static Log log = LogFactory.getLog(SpringIntegrationBean.class);
	
	BeanFactory beanFactory = null;
	
	
	public void setBeanFactory(BeanFactory _beanFactory) throws BeansException {
		this.beanFactory = _beanFactory;
		initSocketServer();
	}
	
	private void initSocketServer() {
		
		LockRecordDao lockRecordDao = (LockRecordDao)beanFactory.getBean("lockRecordDao");
		DevLockInfoDao devLockInfoDao = (DevLockInfoDao)beanFactory.getBean("devLockInfoDao");
		
		int port = CommonUtil.getIntProperty("socket_server_port", 6001);
		System.out.println("设置端口:"+port+"，并启动监听服务");
		
		// 启动SocketServer服务器,启动之前需要设置下面几个参数
		SocketServer server = SocketServer.getInstance();
		server.setLockRecordDao(lockRecordDao);
		server.setDevLockInfoDao(devLockInfoDao);
		
		server.setPort(port);
		server.start();
	}
}
