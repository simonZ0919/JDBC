package cn.tedu;

import java.sql.Connection;

public class TestDBCP {
	public static void main(String[] args) {
		for (int i = 0; i < 4; i++) {
			Runnable r=new Runnable() {
				@Override
				public void run() {
					try {
						Connection connection=DBUtils.getConnection();
						System.out.println("get connection");
						Thread.sleep(3000);
						connection.close();	
						System.out.println("close connection");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			Thread thread=new Thread(r);
			thread.start();
		}
	}
}
