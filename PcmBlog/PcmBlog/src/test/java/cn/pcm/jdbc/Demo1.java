package cn.pcm.jdbc;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

public class Demo1 {
	@Test
	public void test1(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//Connection conn=DriverManager.getConnection("jdbc:mysql://localhost/pcmblog?createDatabaseIfNotExist=true&amp;amp;useUnicode=true&amp;amp;characterEncoding=utf-8", "root", "root");
			Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3307/pcmblog?useUnicode=true&amp;characterEncoding=utf8", "root", "root");
			  
			Statement st=conn.createStatement();
		   String sql="insert into t_user(id,email,password,type,userName) values(1,'a','a','a','占伤感')";
		   st.execute(sql);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
