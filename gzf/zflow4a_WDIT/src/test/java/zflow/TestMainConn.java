package zflow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.mail.MessagingException;



  

public class TestMainConn {
	public static void main(String[] args) throws MessagingException {
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";   //加载JDBC驱动   
		String dbURL = "jdbc:sqlserver://localhost:1433; DatabaseName=zflow";   //连接服务器和数据库sample   
		String userName = "sa";   //默认用户名   
		String userPwd = "ding871203";   //密码   
		try {
			Class.forName(driverName); 
		String sql="select id,createDate,name,company,department,competenceCenter,revenue,phone1,email," +
				" phone2, Topics,specialRFC, note,status,statusName ,infosource ,office,infosourceOther from clientcall";
		querySql(dbURL, userName, userPwd, sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	} 
	
	public static  void querySql(String jdbcUrl,String databaseUsername,String databasePassword,String sql){ 
		Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null; 
	        try {  
	    		try { 
	    			connection = DriverManager.getConnection(jdbcUrl, databaseUsername, databasePassword);
	    	        preparedStatement = connection.prepareStatement(sql);  
	    	        java.sql.ResultSet rset=  preparedStatement.executeQuery(); 
	    	        while(rset.next()){ 
	    	        	String id= rset.getString("id");
	    	        	System.out.println(id);
	    	        }
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}  
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if(resultSet != null) {
						resultSet.close();
						resultSet = null;
					}
					if(preparedStatement != null) {
		        		preparedStatement.close();
		        		preparedStatement = null;
					}
					if(connection != null) {
						connection.close();
						connection = null;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}  
	}
}
