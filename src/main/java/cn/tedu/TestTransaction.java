package cn.tedu;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestTransaction {

	public static void main(String[] args) {
		String sql1=
				"create table if not exists person(id int,money int)";
		String sql2=
				"insert into person values(1,500),(2,5000)";
		String sql3="update person set money=money+2000 where id=1";
		String sql4="update person set money=money-2000 where id=2";
		
		Connection connection = null;
		Statement statement = null;
		ResultSet resultset = null;
		try {
			/**
			 * transaction:connection.commit()/rollback()/setautocommit(true,false)
			 */
			connection = DBUtils.getConnection();
			statement = connection.createStatement();
			//close autocommit
			connection.setAutoCommit(false);
			statement.addBatch(sql1);
			statement.addBatch(sql2);
			statement.executeBatch();
			statement.clearBatch();
			
			for(int i=0;i<5;i++) {
				statement.addBatch(sql3);
				statement.addBatch(sql4);
				statement.executeBatch();
				
				//check money of id2>0
				resultset=statement.executeQuery("select money from person where id=2");
				while(resultset.next()) {
					int remain=resultset.getInt("money");
					if(remain>0) {
						connection.commit();					
						System.out.println(remain);
					}else {
						connection.rollback();						
						System.out.println("Transaction failed");
					}
				}
				statement.clearBatch();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(connection, statement, resultset);
		}
	}

}
