
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;  
import java.sql.ResultSetMetaData;
import java.sql.SQLException;  
  

public class ExportData {
	static String sql = null;  
    static DBHelper db1 = null;  
    static ResultSet ret = null;  
    static ResultSet rs = null;  
//    static String dbname="zflow2a4jiajuist_prod";
//    static String filepath1="D:\\zprojects\\jiajuist\\codes\\zflow2a\\scripts\\jiajuist_release\\tables\\";
//    static String filepath2="D:\\zprojects\\jiajuist\\codes\\zflow2a\\scripts\\jiajuist_release\\procedures\\";
  
    static String dbname="zflow2a4jiajuist3";
    static String filepath1="D:\\zprojects\\jiajuist\\codes\\zflow2a\\scripts\\jiajuist\\tables\\";
    static String filepath2="D:\\zprojects\\jiajuist\\codes\\zflow2a\\scripts\\jiajuist\\procedures\\";

   private static void updateparam_dev(){
	    dbname="zflow2a4jiajuist3";
	    filepath1="D:\\zprojects\\jiajuist\\codes\\zflow2a\\scripts\\jiajuist\\tables\\";
	    filepath2="D:\\zprojects\\jiajuist\\codes\\zflow2a\\scripts\\jiajuist\\procedures\\";	   
   }
   private static void updateparam_prod(){
	    dbname="zflow2a4jiajuist_prod";
	    filepath1="D:\\zprojects\\jiajuist\\codes\\zflow2a\\scripts\\jiajuist_release\\tables\\";
	    filepath2="D:\\zprojects\\jiajuist\\codes\\zflow2a\\scripts\\jiajuist_release\\procedures\\";
  }    
   
  private static void exportTables() throws IOException{
    	ExportTable("id_table");
       	
   	ExportTable("select_conditions");
   	ExportTable("select_conditions_complete");
   	ExportTable("select_conditionselect");
   	ExportTable("select_form_table");
   	ExportTable("select_form_table_complete");
   	ExportTable("select_list");
   	ExportTable("select_list_complete");
   	ExportTable("select_table_list");
   	ExportTable("select_table_list_complete");
      	ExportTable("sys_business_tables");
      	ExportTable("sys_datadictionary_code");
      	ExportTable("sys_datadictionary_type");
      	ExportTable("sys_datadictionary_value");
      	ExportTable("sys_generate_code");
      	ExportTable("sys_import_column");
      	ExportTable("sys_import_table");

     	ExportTable("global_organization");
      	ExportTable("global_organization_type");
       	
      	ExportTable("global_menu_item");
      	ExportTable("global_menu_item_permission");
      	ExportTable("global_module");
      	
      	ExportTable("global_permission");
      	ExportTable("global_permission_role");
      	ExportTable("global_phonepermission_role");
      	ExportTable("global_resource");
      	ExportTable("global_role");
      	ExportTable("global_user");
      	ExportTable("global_employee");
      	ExportTable("global_user_role");
             		
      	ExportTable("zflow_form");
      	ExportTable("zflow_form_property");
      	ExportTable("zflow_form_view");
      	ExportTable("zflow_form_view_property");
      	ExportTable("zsql_column");
      	ExportTable("zsql_database");
      	ExportTable("zsql_table");
     	ExportTable("zflow_property");
     	
     	ExportTable("deco_fin_formula_ap");
     	ExportTable("dm_biz_type");
     	ExportTable("dm_biz_value");

     	ExportTable("deco_construction_stage1");
     	ExportTable("deco_construction_stage2");
     	ExportTable("deco_construction_stage3");
     	ExportTable("deco_construction_stage4");
     	 
     	ExportTable("deco_construction_stage2_ex");
    	
   }
   
    public static void main(String[] args) throws IOException {  
    	 updateparam_dev();
    	ExportProcedures();
    	exportTables() ;
    	updateparam_prod();
    	ExportProcedures();
    	exportTables() ;
     	
      	           	
    }
    

    public static void ExportTable(String tablename) throws IOException{
//	 	String url1 = "jdbc:mysql://www.bizduo.com/mysql?createDatabaseIfNotExist=true&amp;amp;useUnicode=true&amp;amp;characterEncoding=utf-8";  
	 	String url = "jdbc:mysql://www.bizduo.com/"+dbname+"?createDatabaseIfNotExist=true&amp;amp;useUnicode=true&amp;amp;characterEncoding=utf-8";  
	    String name = "com.mysql.jdbc.Driver";  
	    String user = "root";  
	    String password = "Sh201101";  
	    
//       String sql ="SELECT  TABLE_CATALOG,TABLE_SCHEMA,TABLE_NAME,COLUMN_NAME,ORDINAL_POSITION,COLUMN_DEFAULT,IS_NULLABLE,DATA_TYPE,CHARACTER_MAXIMUM_LENGTH,CHARACTER_OCTET_LENGTH,NUMERIC_PRECISION,NUMERIC_SCALE,DATETIME_PRECISION,CHARACTER_SET_NAME,COLLATION_NAME,COLUMN_TYPE,COLUMN_KEY,EXTRA,`PRIVILEGES`,COLUMN_COMMENT,GENERATION_EXPRESSION“"
//+" FROM `COLUMNS` "
//+"where TABLE_SCHEMA='zflow2a4jiajuist4' and TABLE_NAME='"+tablename+"' ";
	    System.out.print(tablename+"exporting ");
	    String sql = "select * from `"+tablename+"` ";

       
        db1 = new DBHelper(url,user,password);//创建DBHelper对象  
        db1.Connect();
        try {  
            rs = db1.Query(sql);//执行语句，得到结果集  
            ResultSetMetaData rsmd = rs.getMetaData();

            File procfile;
			procfile = new File(filepath1+tablename+".sql");
			if (!procfile.exists()) {     
				procfile.createNewFile();     
		      }     
			FileWriter resultFile = new FileWriter(procfile); 
            while (rs.next()) {  
                String columnname ="";
                String columnvalue="";
                String columnstring ="";
                String columnvaluestring ="";
                String insertsql="";            	
            	
            	int columnCount = rsmd.getColumnCount();  
//            	columnCount 就是ResultSet的总列数
            	for(int i=1;i<=columnCount;i++){
            		columnname=rsmd.getColumnName(i);
            		columnstring=columnstring+"`"+columnname+"`,"; 
            		if(rs.getString(i)!=null)
//            			columnvalue="'"+new String( rs.getString(i).getBytes("8859_1"),"UTF-8")+"'";
            			columnvalue="'"+rs.getString(i).replace("'","''")+"'";
            		else
            			columnvalue="null";
            		columnvaluestring=columnvaluestring +columnvalue +","; 
            	}
            	columnstring=columnstring.substring(0,columnstring.length()-1);
            	columnvaluestring=columnvaluestring.substring(0,columnvaluestring.length()-1);
            	insertsql=" INSERT INTO `"+ tablename + "`  (  " +columnstring+" ) "+" VALUES (" +columnvaluestring+") ;"; 
            	
  
				
				resultFile.write(insertsql +" \n\n"); 
				resultFile.write("################################### \n\n"); 
				
                
                System.out.print("*");
            }//显示数据  
            rs.close();  
            db1.close();//关闭连接  
            resultFile.close(); 
            System.out.println("");
            System.out.println(tablename+" exported ");
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  

    
    public static void ExportProcedures() throws IOException{
	 	String url = "jdbc:mysql://www.bizduo.com/mysql?createDatabaseIfNotExist=true&amp;amp;useUnicode=true&amp;amp;characterEncoding=utf-8";  
	    String name = "com.mysql.jdbc.Driver";  
	    String user = "root";  
	    String password = "Sh201101";  
	    
        sql = "select * from proc where db='"+dbname+"' order by name ";//SQL语句  
        db1 = new DBHelper(url,user,password);//创建DBHelper对象  
        db1.Connect();
        try {  
            ret = db1.Query(sql);//执行语句，得到结果集  
            File procfile;
            while (ret.next()) {  
                String proc_type = ret.getString("type");  
                String proc_name = ret.getString("name");  
                String proc_param_list= ret.getString("param_list");  
                String proc_returns= ret.getString("returns");  
//                String ulname = ret.getString(3);  
//                String body_utf8 = ret.getString("body_utf8");  
                String proc_body_utf8="";
				try {
					proc_body_utf8 = new String(ret.getString("body_utf8").getBytes("8859_1"),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				procfile = new File(filepath2+proc_name+".sql");
				if (!procfile.exists()) {     
					procfile.createNewFile();     
			      }     
				FileWriter resultFile = new FileWriter(procfile); 
				resultFile.write("drop ");
				resultFile.write(proc_type+" ");
				resultFile.write("`"+proc_name+"`"+"; \n\n");
				
				resultFile.write("Create ");
				resultFile.write(proc_type+" ");
				resultFile.write("`"+proc_name+"`"+" ");
				resultFile.write("("+proc_param_list+")"+" \n");
				if("FUNCTION".equals(proc_type)){
					resultFile.write(" RETURNS "+proc_returns+" \n");
				}
				resultFile.write(proc_body_utf8+";\n");
				
//				CREATE DEFINER=`root`@`%` FUNCTION `getStrArrayLength`(f_string varchar(1000), f_delimiter varchar(2)) RETURNS int(11)

//				System.out.println();
                System.out.println(proc_name);
                resultFile.close();
                //System.out.println(uid + "\t" + ufname + "\t" + ulname + "\t" + udate );  
            }//显示数据  
            ret.close();  
            db1.close();//关闭连接  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  

}
