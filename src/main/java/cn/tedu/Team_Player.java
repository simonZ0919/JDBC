package cn.tedu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import com.mysql.cj.protocol.Resultset;

public class Team_Player {

	public static void main(String[] args) {
		String sql1=
				"create table if not exists team(id int primary key auto_increment, name varchar(15))";
		String sql2=
				"create table if not exists player(id int primary key auto_increment, name varchar(15),teamid int)";
		
		Scanner scanner=new Scanner(System.in);
		System.out.println("Please input team name:");
		String teamName=scanner.nextLine();		
		
		System.out.println("Please input player name:");
		String playerName=scanner.nextLine();
		
		Connection connection = null;
		Statement statement=null;
		PreparedStatement prepState = null;
		ResultSet resultset=null;
		try {
			connection = DBUtils.getConnection();
			statement=connection.createStatement();
			statement.addBatch(sql1);
			statement.addBatch(sql2);
			statement.executeBatch();
			
			String sql3="select id from team where name=?";
			String sql4="insert into team values(null,?)";
			prepState=connection.prepareStatement(sql3);
			prepState.setString(1, teamName);
			resultset=prepState.executeQuery();
			
			//if teamName found in table
			int teamId=-1;
			while(resultset.next()) {
				teamId=resultset.getInt(1);
			}
			if(teamId==-1) {//if sno teamname, add to team table
				prepState.close();// close statement before reuse
				prepState=connection.prepareStatement(sql4,
						Statement.RETURN_GENERATED_KEYS);// set to return primary key
				prepState.setString(1, teamName);
				prepState.executeUpdate();
				
				// get primary key, close resultset befrore reuse
				resultset.close();
				resultset=prepState.getGeneratedKeys();// get primary key
				while(resultset.next()) {
					teamId=resultset.getInt(1);// get team Id
				}	
			}
			
			//add to player table through teamid
			String sql5="insert into player values(null,?,?)";
			prepState.close();
			prepState=connection.prepareStatement(sql5);
			prepState.setString(1, playerName);
			prepState.setInt(2, teamId);
			prepState.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(connection, prepState, null);
		}
		
	}

}
