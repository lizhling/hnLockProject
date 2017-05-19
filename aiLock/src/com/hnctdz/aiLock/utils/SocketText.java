/*
 * FileName:     SocketText.java
 * @Description: 
 * Copyright (c) 2015 Sunrise Corporation.
 * #368, Guangzhou Avenue South, Haizhu District, Guangzhou
 * All right reserved.
 *
 * Modification  History:
 * Date           Author         Version         Discription
 * -----------------------------------------------------------------------------------
 * 2015-5-14   WangXiangBo        1.0              新建
 */
package com.hnctdz.aiLock.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @ClassName SocketText.java
 * @Author WangXiangBo
 * @Date 2015-5-14 上午09:16:25
 */
public class SocketText extends Thread {
	private static SocketText instance;
	private ServerSocket socket = null;
	private BufferedReader reader;
	private PrintWriter writer;
	private DataInputStream dis = null;
	private DataOutputStream dos = null;
	
	private Socket clientSocket = null;
	
	private String lockDeviceNo = "0001";
	

	public SocketText() {
		try {
			socket = new ServerSocket(6001);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("服务端初始化scoket失败！");
		}
	}
	
	public static SocketText getInstance() {
		if (instance == null) {
            synchronized (SocketText.class) {
                instance = new SocketText();
                instance.start();   //启动线程
            }
        }
        return instance;
	}

	/**
	 * 继承父类方法
	 */
	public void run() {
		try {
			System.out.println("启动监听");
			while (true) {
				clientSocket = socket.accept();
				writer = new PrintWriter(clientSocket.getOutputStream());;
				reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				dos = new DataOutputStream(clientSocket.getOutputStream());// 获取输出流，用于服务端向客户端发送数据
//				dis = new DataInputStream(clientSocket.getInputStream());// 获取输入流，用于接收客户端发送来的数据
				System.out.println("监听到门禁终端连接");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean sendInstr(){
		boolean bl = false;
		try {
			String instructions = "EB0001070000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000071EA";
			byte[] dataByte = CommunCrypUtil.dataToByte(instructions);
			dataByte = CommunCrypUtil.commEncryption(dataByte);
			System.out.println("加密后数据:"+CommunCrypUtil.toHexStr(dataByte));
//			dos.write(dataByte);
			writer.print(dataByte);
			writer.flush();
			
			int total = 73;
			byte[] buff = new byte[total];
			while(true){
				System.out.println("等待客户端返回报文");
				String iReaded = reader.readLine();
				System.out.println("收到加密数据:"+iReaded);
				break;
//				System.out.println("收到加密数据:"+CommunCrypUtil.toHexStr(buff));
			}
			
			byte[] sd = CommunCrypUtil.commEncryption(buff);
			
			System.out.println("收到解密数据："+CommunCrypUtil.toHexStr(sd));
//			try {
//				Thread.sleep(1500);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
		}
		return bl;
	}
	
	public static void main(String[] args) {
		SocketText t= new SocketText();
		t.start();   //启动线程
		try {
			Thread.sleep(1500);
			t.sendInstr();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	public String getLockDeviceNo() {
		return lockDeviceNo;
	}

	public void setLockDeviceNo(String lockDeviceNo) {
		this.lockDeviceNo = lockDeviceNo;
	}
}
