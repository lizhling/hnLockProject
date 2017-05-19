package com.hnctdz.aiLockdm.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.hnctdz.aiLockdm.socket.Server;
import com.hnctdz.aiLockdm.utils.CommandTypeUtil;
import com.hnctdz.aiLockdm.utils.CommonUtil;
import com.hnctdz.aiLockdm.utils.CommunCrypUtil;
import com.hnctdz.aiLockdm.utils.DateUtil;
import com.hnctdz.aiLockdm.utils.ErrorCodeException;
import com.hnctdz.aiLockdm.utils.LockStatusRecords;
import com.hnctdz.aiLockdm.utils.RecordCommandUtil;
import com.hnctdz.aiLockdm.utils.StringUtil;
import com.hnctdz.aiLockdm.utils.UnlockRecords;
/** 
 * @ClassName SocketHandler.java
 * @Author WangXiangBo 
 */
public class SocketHandler extends Thread {
	private static Logger log = Logger.getLogger(SocketHandler.class);
	private static Log lockRecordLog = LogFactory.getLog("LockRecord");
	
	private Server server;
	protected Socket socket;
	private DataInputStream inputStream = null;
	private DataOutputStream outputStream = null;
	private ExtractLockRecords extractRecords;
	private String moduleCode;
	
	//不可配卡机号（定时去拿状态，门锁内无记录）
	private Map<String, Integer> doNotMaCaDeviceNoMap = new HashMap<String, Integer>();
	//可配卡机号（自动上报状态，门锁内有记录,定时去拿记录）
	private Map<String, Integer> canMaCaDeviceNoMap = new HashMap<String, Integer>();
	
	private int cmdLen = 14;
	private String results = "";
	private String waitTask = null;
	
	public SocketHandler(Server server) {
    	super("socketHandler");
    	this.server = server;
    }
	
	public void setSocket(Socket socket) {
		this.socket = socket;
		try {
			inputStream = new DataInputStream(socket.getInputStream());// 获取输入流，用于接收客户端发送来的数据
			outputStream = new DataOutputStream(socket.getOutputStream());// 获取输出流，用于服务端向客户端发送数据
		} catch (SocketException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			while (isConnected()) {
				receiveContent();
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
    private String receiveContent() throws IOException {
    	String returns = "";
		byte[] buff = new byte[73];
		int index = 0;
		int off = 0;
		int len = 14;
		cmdLen = 14;
		while(index < cmdLen){
//			log.info("模块:"+this.moduleCode+"等待解析报文长度:"+len);
			int iReaded = inputStream.read(buff, off, len);
//			log.info("cmdLen:"+cmdLen+";index="+index+";off="+off+";len="+len+";iReaded="+iReaded);
			
			if(cmdLen > 14 && buff[0] == -6 && buff[1] == 7 && buff[2] == 19 && buff[12] == -6 && buff[13] == -1){
				buff = new byte[73];
				off = 0;
				len = 14;
				index = 0;
				log.info("网关编号:"+this.moduleCode+"等待报文返回时收到心跳包报文，屏蔽继续等待");
			}else{
				if(cmdLen > len){
					off = len;
					len = cmdLen - len;
				}
				index += iReaded;
			}
//		 	log.info("cmdLen:"+cmdLen);
		}
		if(cmdLen == 73){
			byte[] contentByte = CommunCrypUtil.commEncryption(buff);
			returns = CommunCrypUtil.toHexStr(contentByte);
			saveLockRecord(contentByte, returns);
		}else if(cmdLen == 21){
			returns = CommunCrypUtil.toHexStr(buff);
			saveLockRecord(buff, returns);
		}else if(cmdLen == 14){
			returns = CommunCrypUtil.toHexStr(buff);
			onConnect(returns.substring(0, 28));
		}else{
			returns = CommunCrypUtil.toHexStr(buff);
			receive(returns);
		}
		log.info(DateUtil.getDateMs()+"收到网关IP:"+this.socket.getInetAddress().getHostAddress()+"编号:"+this.moduleCode+"响应报文:"+returns);
		
		return returns;
    }
    
    public void saveLockRecord(byte[] contentByte, String returns){
    	try{
    		if(contentByte[0] == -21 && contentByte[72] == -22){//判断报文格式是否正确(头:EB;尾:EA)
    			if(returns.substring(6, 8).equalsIgnoreCase(this.waitTask)){//判断收到报文是否和等待任务匹配
    				receive(returns);										//匹配则返回结果，否则继续等待。
    			}
    			if(contentByte[3] == 6 && contentByte[4] == -88){//判断是否为读取门锁记录(命令码:06;状态码:A8)
					UnlockRecords urs = new UnlockRecords();
					urs.setLockDeviceNo(returns.substring(2, 6));//机号
					boolean messageSaveLog = false;
					for(int j = 0; j <= 3; j++){//一条命令最多包涵4条记录
						try{
							int recordHead = j * 16 + 7;
							if(contentByte[recordHead] == -1 && contentByte[recordHead + 1] == -1
									&& contentByte[recordHead + 2] == -1 && contentByte[recordHead + 3] == -1){
								break;
							}
							urs = RecordCommandUtil.getLockRecordInfo(urs, contentByte, recordHead);
							urs.setLockInModuleCode(this.moduleCode);
							urs.setMessage(returns);
							server.handleRecordCommand(urs);
						} catch (Exception e) {
							e.printStackTrace();
							if(!messageSaveLog){
								lockRecordLog.info("{moduleCode:\""+this.moduleCode+"\",recordNum:"+j+1+",message:\""+returns+"\"}");
							}
							messageSaveLog = true;
						}
					}
    			}else if(contentByte[3] == -1){//判断是否为：门锁状态上报(推送标识:FF)
    				LockStatusRecords lsr = RecordCommandUtil.getLockStatusInfo(contentByte[18]);
    				if(lsr != null){
    					lsr.setLockInModuleCode(this.moduleCode);
    					lsr.setLockDeviceNo(returns.substring(2, 6));//机号
    					lsr.setMessage(returns);
    					server.handleLockStatusCommand(lsr);
    				}
    			}/*else if(contentByte[3] == 5){//判断是否为：门锁设置时间返回
    				canMaCaDeviceNoMap.put(returns.substring(2, 6), 1);
    			}*/
    		}else if(contentByte[0] == -21 && contentByte[20] == -22 && contentByte[21] == 00){//判断是否正确状态报文(头:EB;尾:EA)
    			if("00".equalsIgnoreCase(this.waitTask)){//判断收到报文是否和等待任务匹配
    				receive(returns);					 //匹配则返回结果，否则继续等待。
    			}
    			LockStatusRecords lsr = RecordCommandUtil.getLockStatusInfo(contentByte[3]);
				if(lsr != null){
					lsr.setLockInModuleCode(this.moduleCode);
					lsr.setLockDeviceNo(returns.substring(2, 6));//机号
					lsr.setMessage(returns);
					server.handleLockStatusCommand(lsr);
				}
				
				//从状态记录中读到的记录只保存告警记录
				if(contentByte[3] != -1 && contentByte[4] != -1){
					UnlockRecords urs = new UnlockRecords();
					urs.setLockDeviceNo(returns.substring(2, 6));//机号
					
					urs = RecordCommandUtil.getLockRecordInfo(urs, contentByte, 4);
					//判断是否为告警记录
					if(CommandTypeUtil.getLockAlarmType(urs.getRecordCode()) != null){
//						if(urs.getRecordCode().equalsIgnoreCase("05")){
//						}
						urs.setLockInModuleCode(this.moduleCode);
						urs.setMessage(returns);
						urs.setUnlockTime(DateUtil.getDateTime());
						server.handleRecordCommand(urs);
					}
				}
    		}
    	}catch (Exception e) {
    		e.printStackTrace();
    		lockRecordLog.info("{moduleCode:\""+this.moduleCode+"\",recordNum:0,message:\""+returns+"\"}");
		}
    }
    
	/**
     * 当目标客户端首次连接到服务端时执行。该方法针对每个客户端只执行一次
     */
    protected void onConnect(String moduleCode) {
    	if(this.moduleCode == null && isConnected()){
    		this.moduleCode = moduleCode;
    		String hostAddress = this.socket.getInetAddress().getHostAddress();
    		log.info("接收到网关链接IP：" + hostAddress + ";编码："+moduleCode);
    		server.register(moduleCode, this);// 执行注册动作,将目标客户端注册到服务端
    		
    		extractRecords = new ExtractLockRecords("ExtractLockRecords");
			extractRecords.start();
    	}
    }
    
    /**
     * 发送命令
     * @param command
     * @throws IOException 如果客户端已经断开了连接则抛出该异常
     */
    public void sendCommand(byte[] crypCommand) throws IOException {
    	setResults(null);
    	setCmdLen(73);
		outputStream.write(crypCommand);
		outputStream.flush();
    }
    
    /**
     * 发送快速命令
     * @param command
     * @throws IOException 如果客户端已经断开了连接则抛出该异常
     */
    public void sendExpressCommand(byte[] crypCommand) throws IOException {
    	setResults(null);
    	setCmdLen(21);
		outputStream.write(crypCommand);
		outputStream.flush();
    }
	
	private void receive(String content) {
		setResults(content);
    }

    // 确定服务器本地是否已经打开了连接端口(这里并不能保证与客户端的连接是正常的)
    public boolean isConnected() {
    	return (this.socket != null && !this.socket.isClosed() && this.socket.isConnected());
    }
	
	/*** 测试客户端是否仍然连接着  */
    public boolean testConnected() {
    	try {
    		outputStream.write(new byte[]{0x00});//socket.sendUrgentData(0xFF);
    		outputStream.flush();
    		return true;
    	} catch (Exception e) {
    		log.info("客户端已经断开(心跳检测):" + e.getMessage());
    		this.close();// 与客户端连接不通,断开与客户端的连接
    		return false;
    	}
    }
	
	/*** 关闭客户端 */
    public void close() {
    	if (this.socket != null) {
    		try {
				this.socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
    

    private void setLockTime(String deNo){
    	Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String week = "";
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
        	week = "00";
        }else{
        	week = "0" + w;
        }
        
        String date = DateUtil.getDateTime("yyMMddHHmmss", cal.getTime());
        String timeNr = date.substring(0, 4) + week + date.substring(4, date.length());
//        String setTimeInst = "EB0001050000001508042019090300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000EA";
        String setTimeInst = "EB"+ deNo + "05000000" + timeNr + "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000EA";
        try {
			sendCommand(CommunCrypUtil.getCrypCommand(setTimeInst));
			
			long waitStartTime = new Date().getTime();
			int waitingLong = CommonUtil.getIntProperty("waitingLong", 15000);
			
			while(isConnected()) {
				String res = getResults();
				if(StringUtil.isNotEmpty(res) && res.substring(6,8).equalsIgnoreCase("05")){
					break;
				}
				if((waitStartTime + waitingLong) <= new Date().getTime()){
					break;
				}
				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    

    private class ExtractLockRecords extends Thread {
		public ExtractLockRecords(String name) {
			super(name);
		}
		
		@Override
		public void run() {
			try {
				List list = server.getActiveLockInfo(moduleCode);
				for(int i=0; i < list.size(); i++){
					Object[] obj = (Object[])list.get(i);
					if((Integer)obj[1] == 1){
						canMaCaDeviceNoMap.put(obj[0].toString(), 1);
					}else{
						doNotMaCaDeviceNoMap.put(obj[0].toString(), 1);
					}
				}
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ErrorCodeException e) {
				e.printStackTrace();
			}
			
			for(String deviceNo : canMaCaDeviceNoMap.keySet()){
				setLockTime(deviceNo);
			}
			
			if(doNotMaCaDeviceNoMap.size() > 0){
				ExtractLockStatus extractStatus = new ExtractLockStatus("ExtractLockStatus");
				extractStatus.start();
			}
			
			while (isConnected()) {
				int useTime = 0;
				String extractInst = null;
				for(String deviceNo : canMaCaDeviceNoMap.keySet()){
					extractInst = "EA" + deviceNo + "06AA0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000059";
					try {
						sendCommand(CommunCrypUtil.getCrypCommand(extractInst));
						useTime += 10000;
						sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				try {
					//30分钟轮循一次主动去获取门锁记录数据
					sleep(CommandTypeUtil.regularReadingTime * 60 * 1000 - useTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		

		private class ExtractLockStatus extends Thread {
			public ExtractLockStatus(String name) {
				super(name);
			}
			
			@Override
			public void run() {
				while (isConnected()) {
					int useTime = 0;
					String extractInst = "";
					for(String deviceNo : doNotMaCaDeviceNoMap.keySet()){
						extractInst = "EA" + deviceNo + "000000000000000000000000000000000059";
						try {
							sendExpressCommand(CommunCrypUtil.dataToByte(extractInst));
							useTime += 5000;
							sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					try {
						//1分钟轮循一次主动去获取门锁状态数据
						sleep(1 * 60 * 1000 - useTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}


	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public int getCmdLen() {
		return cmdLen;
	}

	public void setCmdLen(int cmdLen) {
		this.cmdLen = cmdLen;
	}

	public String getWaitTask() {
		return waitTask;
	}

	public void setWaitTask(String waitTask) {
		this.waitTask = waitTask;
	}

}
