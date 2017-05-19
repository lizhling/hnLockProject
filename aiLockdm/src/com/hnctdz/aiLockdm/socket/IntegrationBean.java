/*
 * FileName:     IntegrationBean.java
 * @Description: 
 * Copyright (c) 2015 Sunrise Corporation.
 * #368, Guangzhou Avenue South, Haizhu District, Guangzhou
 * All right reserved.
 *
 * Modification  History:
 * Date           Author         Version         Discription
 * -----------------------------------------------------------------------------------
 * 2015-5-26   WangXiangBo        1.0              新建
 */
package com.hnctdz.aiLockdm.socket;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.hnctdz.aiLockdm.utils.CommonUtil;

/** 
 * @ClassName IntegrationBean.java
 * @Author WangXiangBo 
 * @Date 2015-5-26 下午05:23:45
 */
public class IntegrationBean implements ServletContextListener{

	public IntegrationBean(){
//	   System.out.println("调用了构造方法");
	}
	
	public void contextInitialized(ServletContextEvent event) {
		
		int port = CommonUtil.getIntProperty("socket_server_port", 6001);
		System.out.println("设置端口:"+port+"，并启动监听服务");
		
		// 启动SocketServer服务器,启动之前需要设置下面几个参数
		SocketServer server = SocketServer.getInstance();
		server.setPort(port);
		server.start();
	}
	
	public void contextDestroyed(ServletContextEvent arg0) {
		 System.out.println("销毁了Context destroyed on");
	}
}
