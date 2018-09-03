import java.sql.Connection;
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;
import java.sql.SQLException; 


public class DBHelper {
	 	public   String url = "jdbc:mysql://www.bizduo.com/mysql?createDatabaseIfNotExist=true&amp;amp;useUnicode=true&amp;amp;characterEncoding=utf-8";  
	    public   String name = "com.mysql.jdbc.Driver";  
	    public   String user = "root";  
	    public   String password = "Sh201101";  
	  
	    public Connection conn = null;  
	    public PreparedStatement pst = null;  
	  
	    public DBHelper(String  _url,String _user,String _password) {  
	    	url =_url;
	    	user=_user;
	    	password=_password;
	    }  
	    
	    public void Connect(){
	        try {  
	            Class.forName(name);//指定连接类型  
	            conn = DriverManager.getConnection(url, user, password);//获取连接  
	            
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  	    	
	    }
	    public ResultSet Query(String sql) throws SQLException{
	    	pst = conn.prepareStatement(sql);//准备执行语句  
	    	ResultSet ret = pst.executeQuery(sql);//执行语句，得到结果集
	    	return ret;
	    }
	  
	    public void close() {  
	        try {  
	            this.conn.close();  
	            this.pst.close();  
	        } catch (SQLException e) {  
	            e.printStackTrace();  
	        }  
	    }  
}
