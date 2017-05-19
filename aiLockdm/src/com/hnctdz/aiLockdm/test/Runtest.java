package com.hnctdz.aiLockdm.test;

import com.hnctdz.aiLockdm.test.thread.ObjectService;
import com.hnctdz.aiLockdm.test.thread.ThreadA;
import com.hnctdz.aiLockdm.test.thread.ThreadB;

public class Runtest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ObjectService os = new ObjectService();
		ObjectService os1 = new ObjectService();
		ThreadA threada = new ThreadA(os);
		threada.start();
		
		ThreadB threadB = new ThreadB(os1);
		threadB.start();
		
	}

}
