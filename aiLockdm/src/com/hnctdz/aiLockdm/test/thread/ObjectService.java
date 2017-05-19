package com.hnctdz.aiLockdm.test.thread;

public class ObjectService {
	
	synchronized static public void serviceMethod() {
        try {
//            synchronized (this) {
        	 System.out.println("begin time=" + System.currentTimeMillis());
        	 for (int i = 0; i < 10; i++) {
        		 Thread.sleep(500);
				 System.out.println("outPut value:"+i);
			}
               
                System.out.println("end    end=" + System.currentTimeMillis());
//            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
