package com.hnctdz.aiLockdm.socket;

import java.util.List;
import java.util.logging.Logger;

import com.hnctdz.aiLockdm.dao.DevLockInfoDao;
import com.hnctdz.aiLockdm.dao.LockRecordDao;
import com.hnctdz.aiLockdm.socket.Server;
import com.hnctdz.aiLockdm.utils.ErrorCodeException;
import com.hnctdz.aiLockdm.utils.LockStatusRecords;
import com.hnctdz.aiLockdm.utils.UnlockRecords;

/** 
 * @ClassName SocketServer.java
 * @Author WangXiangBo 
 */
public class SocketServer extends Server {
	private final static Logger log = Logger.getLogger(SocketServer.class.getName());
	
	private LockRecordDao lockRecordDao;
	
	private DevLockInfoDao devLockInfoDao;
	
	private final static SocketServer instance = new SocketServer();
	
	public final static SocketServer getInstance() {
		return instance;
	}
	
	public SocketServer() {}
	
	/**
	 * 当客户端首次进行联接请求时，服务端会为每个客户端创建单独的线程进行处理．即
	 * 这里的socketHandler,实现这个方法可以创建自己的handler.实现自定义处理
	 */
	@Override
	public SocketHandler createSocketHandler() {
		return new SocketHandler(this);
	}
	
	@Override
	public List findActiveLockByModuleCode(String moduleCode) throws ErrorCodeException{
		return devLockInfoDao.findActiveLockByModuleCode(moduleCode);
	}
	
	@Override
	public void saveRecordCommand(UnlockRecords urs) throws ErrorCodeException{
		lockRecordDao.saveRecordCommand(urs);
	}
	
	@Override
	public void saveLockStatusRecords(LockStatusRecords lsr) throws ErrorCodeException{
		lockRecordDao.saveLockStatusRecords(lsr);
	}
	
	public boolean findLockRecordsIfHave(String lockCode, String recordCode){
		return lockRecordDao.findLockRecordsIfHave(lockCode, recordCode);
	}
	
	/**
	 * 当接收到客户端的注册命令时的处理操作．
	 * @param mess 注册命令
	 */
	@Override
	protected void onRegister(String mess) {
		log.info("注册成功,返回注册结果完毕.");
	}
	
	public LockRecordDao getLockRecordDao() {
		return lockRecordDao;
	}

	public void setLockRecordDao(LockRecordDao lockRecordDao) {
		this.lockRecordDao = lockRecordDao;
	}

	public DevLockInfoDao getDevLockInfoDao() {
		return devLockInfoDao;
	}

	public void setDevLockInfoDao(DevLockInfoDao devLockInfoDao) {
		this.devLockInfoDao = devLockInfoDao;
	}
}
