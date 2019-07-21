package cn.tedu;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;


public class DBMetaData {

	public static void main(String[] args) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultset=null;
		try {
			connection = DBUtils.getConnection();
			statement = connection.createStatement();
			//得到数据库的元数据
			DatabaseMetaData dbData = connection.getMetaData();
			System.out.println("驱动版本:"+dbData.getDriverVersion());
			System.out.println("用户名:"+dbData.getUserName());
			System.out.println("连接地址:"+dbData.getURL());
			System.out.println("数据库名称:"+dbData.getDatabaseProductName());
			
			//获取表相关的元数据
			resultset = statement.executeQuery("select * from emp");
			ResultSetMetaData rsData = resultset.getMetaData();
			//得到表的字段数量
			int count = rsData.getColumnCount();
			for (int i = 0; i < count; i++) {
				String name = rsData.getColumnName(i+1);
				String type = rsData.getColumnTypeName(i+1);
				System.out.println(name+":"+type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(connection, statement, null);
		}
	}

}
