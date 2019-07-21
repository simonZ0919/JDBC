package cn.tedu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import org.junit.Test;

public class DBOperations {
	@Test
	public void insert() throws Exception {
		Scanner scanner=new Scanner(System.in);
		System.out.println("Please input name:");
		String name=scanner.nextLine();
		System.out.println("Please input age:");
		int age=scanner.nextInt();		
		String sql="insert into jdbcuser values(?,?)";
		
		Connection connection=null;
		PreparedStatement statement=null;
		try {
			connection=DBUtils.getConnection();
			//precompile sql
			statement=connection.prepareStatement(sql);
			
			statement.execute(
					"create table if not exists jdbcuser(name varchar(15),age int)");
			//replace ? in sql with value
			statement.setString(1, name);
			statement.setInt(2, age);
			statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtils.close(connection, statement, null);
		}
	}
		
	@Test
	public void delete() throws Exception {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultset = null;
		try {
			connection = DBUtils.getConnection();
			statement = connection.createStatement();
			statement.executeUpdate("delete from emp where empno=10");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(connection, statement, resultset);
		}
	}
	
	@Test
	public void select() throws Exception {
		Connection connection=null;
		Statement statement=null;
		ResultSet resultset=null;
		try {
			connection =DBUtils.getConnection();
			statement=connection.createStatement();
			resultset=statement.executeQuery("select empno,ename,sal from emp");
			
			// get next element, if exists
			while(resultset.next()) {
				// get value by name or index
				int empno=resultset.getInt("empno");
				String name=resultset.getString("ename");
				//double sal=resulset.getDouble("sal")
				double sal=resultset.getDouble(3);// index in query result
				System.out.println(empno+name+sal);
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			DBUtils.close(connection, statement, resultset);
		}
	}
	@Test
	public void update() {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = DBUtils.getConnection();
			statement = connection.createStatement();
			statement.executeUpdate("update emp set deptno=10 where deptno=15");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(connection, statement, null);
		}
	}
}
