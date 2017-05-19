package com.hnctdz.aiLockdm.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.hnctdz.aiLockdm.socket.SocketHandler;
import com.hnctdz.aiLockdm.utils.CommunCrypUtil;
import com.hnctdz.aiLockdm.utils.ErrorCode;
import com.hnctdz.aiLockdm.utils.ErrorCodeException;
import com.hnctdz.aiLockdm.utils.LockStatusRecords;
import com.hnctdz.aiLockdm.utils.StringUtil;
import com.hnctdz.aiLockdm.utils.UnlockRecords;

/** 
 * @ClassName Server.java
 * @Author WangXiangBo 
 */
public abstract class Server extends Thread {
	private final static Logger log = Logger.getLogger(Server.class);
	protected Integer port;
	protected ServerSocket server;
	protected Map<String, SocketHandler> handlers 
		= Collections.synchronizedMap(new HashMap<String, SocketHandler>());
	private ResourceHelper resHelper;
	
	public Server() {
		super("Server");
	}
	
	public Server(int port) {
		super("Server");
		this.port = port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public void run() {
		try {
			log.info("Server's run()*****************");
			if (port == null) {
				throw new NullPointerException("在启动SocketServer之前请先设置一个端口号.");
			}
			
			server = new ServerSocket(port);// 启动服务器
			
			// 启动心跳检测线程
			resHelper = new ResourceHelper("ResourceHelper");
			resHelper.start();
			while(true) {
				if (!server.isClosed()) {
					Socket socket = server.accept();
					socket.setSoTimeout(0);// 不要设置timeout
					
					log.info("监听到门禁终端连接，创建处理类并启动监听通信");
					// 创建处理类并启动监听通信
					SocketHandler handler = createSocketHandler();
					handler.setSocket(socket);
					handler.start();
				} else {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (server != null) {
					server.close();					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 目标客户端注册到服务端
	 * @param moduleCode
	 * @param socketHandler
	 */
	protected final void register(String moduleCode, SocketHandler socketHandler) {
		// 如果存在旧的handler则关闭并移除它.
		SocketHandler oldHandler = this.handlers.remove(moduleCode);
		if (oldHandler != null) {
			oldHandler.close();
		}
		
		this.handlers.put(moduleCode, socketHandler);// 添加新的socketHandler
		
		log.info("Register socket handler ok, handlers.size:" + this.handlers.size());
		//2013-1-11日 注释掉原有方案
//		onRegister(mess);
		//2013-1-11日  更改注册时将设备信息进行保存
//		onRegister(mess, socketHandler);
	}
	
	protected final List getActiveLockInfo(String moduleCode) throws ErrorCodeException{
		return findActiveLockByModuleCode(moduleCode);
	}
	protected abstract List findActiveLockByModuleCode(String moduleCode) throws ErrorCodeException;
	
	
	protected final void handleRecordCommand(UnlockRecords urs) throws ErrorCodeException{
		saveRecordCommand(urs);
	}
	protected abstract void saveRecordCommand(UnlockRecords urs) throws ErrorCodeException;
	
	
	protected final void handleLockStatusCommand(LockStatusRecords lsr) throws ErrorCodeException{
		saveLockStatusRecords(lsr);
	}
	protected abstract void saveLockStatusRecords(LockStatusRecords lsr) throws ErrorCodeException;
	
	/**
	 * 发送命令给目标模块,并返回与发送该命令相关的SocketHandler
	 * @param moduleCode, commands
	 * @throws ErrorCodeException, IOException
	 */
	public SocketHandler send(String moduleCode, String commands) throws ErrorCodeException, IOException {
		if (!isRunning()) {
			throw new ErrorCodeException(ErrorCode.ERROR_SERVER_NOT_START);
		}
		SocketHandler socketHandler = handlers.get(moduleCode);
		if (socketHandler != null) {
			if(StringUtil.isNotEmpty(socketHandler.getWaitTask())){
				return socketHandler;
			}
			
			byte[] crypCommand = CommunCrypUtil.getCrypCommand(commands);
			socketHandler.sendCommand(crypCommand);
			return socketHandler;
		} else {
			throw new ErrorCodeException(ErrorCode.ERROR_IP_NOT_CONNECTED);
		}
	}
	
	/**
	 * 发送快送命令给目标模块,并返回与发送该命令相关的SocketHandler
	 * @param moduleCode, commands
	 * @throws ErrorCodeException, IOException
	 */
	public SocketHandler sendExpress(String moduleCode, String commands) throws ErrorCodeException, IOException {
		if (!isRunning()) {
			throw new ErrorCodeException(ErrorCode.ERROR_SERVER_NOT_START);
		}
		SocketHandler socketHandler = handlers.get(moduleCode);
		if (socketHandler != null) {
			if(StringUtil.isNotEmpty(socketHandler.getWaitTask())){
				return socketHandler;
			}
			
			byte[] crypCommand = CommunCrypUtil.dataToByte(commands);
			socketHandler.sendExpressCommand(crypCommand);
			return socketHandler;
		} else {
			throw new ErrorCodeException(ErrorCode.ERROR_IP_NOT_CONNECTED);
		}
	}
	
	/**
	 * 获取所有的socketHandler,这些socketHandler分别与目标
	 * 客户端对应，直接与相应的客户端通讯。可以通过key(目标模块号) 来获取特定的socketHandler
	 * 也可能直接通过 {@link #getHandler(String)} 来获取特定的 handler.
	 * @return handlers
	 */
	public Map<String, SocketHandler> getHandlers() {
		return this.handlers;
	}
	
	/**
	 * 获取与模块号相对应的socketHandler,在客户端未链接或
	 * 客户端未向服务端发送注册请求时将找不到socketHandler,这个 时候会返回null
	 * 获取该handler之后可以直接与相应的目标客户端直接通讯。
	 * @param imsi
	 * @return socketHandler
	 */
	public SocketHandler getHandler(String imsi) {
		SocketHandler socketHandler = handlers.get(imsi);
		return socketHandler;
	}
	
	/**
	 * 停止服务器，断开与所有客户端的链接。
	 */
	public void stopServer() throws IOException {
		if (server != null) {
			server.close();
			this.handlers.clear();
		}
	}
	
	/**
	 * 判断服务器是否处于运行状态
	 * @return
	 */
	public boolean isRunning() {
		return (!isClosed());
	}
	
	/**
	 * 判断服务器是否处于关闭（停止）状态
	 * @return
	 */
	public boolean isClosed() {
		return (this.server == null || this.server.isClosed());
	}
	
	/**
	 * 为特定的客户端链接创建SocketHandler.通过实现该方法可以实现
	 * 自定义的socketHandler来自定义处理与目标客户端的链接。
	 * @return
	 */
	protected abstract SocketHandler createSocketHandler();
	
	/**
	 * 客户端发送了注册命令。
	 * @param mess 注册命令
	 */
	protected abstract void onRegister(String mess);
	
	
	private class ResourceHelper extends Thread {
		
		public ResourceHelper(String name) {
			super(name);
		}
		
		@Override
		public void run() {
			while (true) {
				Set<Entry<String, SocketHandler>> sets = handlers.entrySet();
				Iterator<Entry<String, SocketHandler>> it = sets.iterator();
				while (it.hasNext()) {
					Entry<String, SocketHandler> entry = it.next();
					SocketHandler sh = entry.getValue();
					// 测试连接性
					sh.testConnected();
					// 如果客户端已经断开,则退出
					if (!sh.isConnected()) {
						log.info("释放资源:" + entry.getKey());
						it.remove();
					}
				}
				try {
					Thread.sleep(1 * 60 * 1000);
				} catch (InterruptedException e) {
					log.error("资源管理器出错.系统可能无法释放资源,建议重新启动服务器." + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		
	}
	
}
