package cn.tedu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;


public class TestUserLogin {
	public static void main(String[] args) {
		Scanner scanner=new Scanner(System.in);
		System.out.println("Please input username:");
		String username=scanner.nextLine();
		
		System.out.println("Please input password:");
		String password=scanner.nextLine();
		
		System.out.println("Please input page:");
		int page=scanner.nextInt();
		
		System.out.println("Please input lines");
		int lines=scanner.nextInt();
		boolean result=login(username,password,page,lines);
		System.out.println(result?"Succeed!":"Failed!");
		
	}
	private static boolean login(String username, String password,int page, int lines) {
		String sql1=
				"create table if not exists UserInfo(username varchar(15),password varchar(10))";
		String sql2=
				"insert into Userinfo values('root','adm123')";
		String sql3= "insert into Userinfo values(?,?)";
		String sql4="select * from UserInfo limit ?,?";
		String sql5=
				"select count(*) from UserInfo where username=? and password=?";
	
		Connection connection = null;
		Statement statement=null;
		PreparedStatement prepState = null;
		ResultSet resultset=null;

		try {
			connection = DBUtils.getConnection();
			// add to batch, concurrent
			statement=connection.createStatement();
			statement.addBatch(sql1);
			statement.addBatch(sql2);			
			statement.executeBatch();//execute in batch
			
			// precompile sql, fixed logic
			prepState = connection.prepareStatement(sql3);
			for(int i=1;i<50;i++) {
				prepState.setString(1, "user"+i);
				prepState.setString(2, "sysadm");
				prepState.addBatch();
				// execute bach for every 10 SQLs, avoid memory overflow
				if(i%10==0) {
					prepState.executeBatch();
					prepState.clearBatch();//clear batch
				}
			}
			prepState.executeBatch();
			
			prepState=connection.prepareStatement(sql4);
			prepState.setInt(1, (page-1)*lines);
			prepState.setInt(2, lines);
			resultset=prepState.executeQuery();
			while(resultset.next()) {
				System.out.println(resultset.getString("username")+":"+
						resultset.getString("password"));
			}			
			
			prepState=connection.prepareStatement(sql5);
			prepState.setString(1, username);
			prepState.setString(2, password);			
			resultset=prepState.executeQuery();
			while(resultset.next()) {
				if(resultset.getInt(1)>0)
					return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(connection, statement, resultset);
		}
		return false;
	}
}
