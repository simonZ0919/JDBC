package cn.tedu;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;

public class DBUtils {
	private static String driver,url, username,password;
	private static BasicDataSource dataSource;
	static {
		// create property object
		Properties properties=new Properties();
		// read file and load to properties
		InputStream ipStream=DBUtils.class.
				getClassLoader().getResourceAsStream("jdbc.properties");
		try {
			properties.load(ipStream);
			//get value from properties
			driver=properties.getProperty("driver");
			url=properties.getProperty("url");
			username=properties.getProperty("username");
			password=properties.getProperty("password");	
			
			dataSource=new BasicDataSource();	
			//set datasource
			dataSource.setDriverClassName(driver);
			dataSource.setUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(password);
			
			//set db pool size
			dataSource.setInitialSize(3);
			dataSource.setMaxTotal(3);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				ipStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//connect to database
	public static Connection getConnection() throws Exception {
//		Class.forName(driver);
//		// get connection object
//		Connection connection=DriverManager.getConnection(url, username, password);	

		//connect to db
		return dataSource.getConnection();
	}
	
	//close resources
	public static void close(Connection connection,
			Statement statement,ResultSet resultSet) {
		try {
			if(resultSet!=null) {
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			if(statement!=null) {
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			if(connection!=null) {
				//restore autocommit to defaullt
				connection.setAutoCommit(true);
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
