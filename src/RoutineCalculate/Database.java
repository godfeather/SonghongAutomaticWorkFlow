package RoutineCalculate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	static Connection con=null;
	public static Statement connect(String ipAndPort,String user,String pwd) {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("error:jdbc not exist!");
			System.exit(0);
		}
		
		try {
			con=DriverManager.getConnection("jdbc:sqlserver://"+ipAndPort+";DatabaseName=mrright_songhongoa",user,pwd);
			System.out.println("msg:success to connect sqlserver!");
			return con.createStatement();
		} catch (SQLException e) {
			if(con!=null) {
				try {
					con.close();
					
				} catch (SQLException e1) {}
				con=null;
			}
			System.out.println(e.getMessage());
		}
		return null;
	}
	public static Statement mysqlConnect(String ipAndPort,String user,String pwd){
		String url="jdbc:mysql://"+ipAndPort+"/mrright_songhongoa?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("error:jdbc not exist!!");
		}
		try {
			con=DriverManager.getConnection(url,user,pwd);
		System.out.println("msg：success to connect mysql!");
		} catch (SQLException e) {
			System.out.println("msg:"+e.getMessage());
			return null;
		}
		try {
			return con.createStatement();
		} catch (SQLException e) {
			System.out.println("error:fail to create Statement!");
			shut();
		}
		return null;
	}
	public static void shut() {
		if(con!=null) {
			try {
				con.close();
			} catch (SQLException e) {
			}
			con=null;
		}
	}
}
