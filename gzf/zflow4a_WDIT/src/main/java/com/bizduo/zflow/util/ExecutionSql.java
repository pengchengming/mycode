package com.bizduo.zflow.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.bizduo.zflow.domain.form.FormProperty;
import com.bizduo.zflow.domain.table.ZColumn;
import com.bizduo.zflow.domain.table.ZTable;
import com.bizduo.zflow.domain.tableData.ZCloumnData;


public class ExecutionSql {
	
public void aa(){
	String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";   //加载JDBC驱动  

	String dbURL = "jdbc:sqlserver://localhost:1433; DatabaseName=UniversityDB";   //连接服务器和数据库sample  

	String userName = "sa";   //默认用户名  

	String userPwd = "wkt19910602";   //密码  

	Connection dbConn;  
	Statement sql;

 

	try {
		Class.forName(driverName);
	

	dbConn = DriverManager.getConnection(dbURL, userName, userPwd); 

	sql=dbConn.createStatement();

	sql.executeQuery("select*from course");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  
	
}
	// 获取下一个Id
	public static Long getnextId(JdbcTemplate jdbcTemplate,String zTablename) throws Exception {
		// 根据表,查询下一个Id,如果不存在就
		String sqlCurrentid = "select next_val as nextId from id_table where sequence_name= '"
				+ zTablename + "'";
		String idStr = executionsql(null, "nextId", jdbcTemplate, sqlCurrentid, null);
		Long id = null;
		if (idStr == null || idStr.trim().equals("")) {
			id = 1000l;
			Long nextId = id + 1;
			String sqlnextId = "INSERT INTO id_table(sequence_name,next_val )VALUES('"
					+ zTablename + "','" + nextId + "') ";
			Boolean isexecution = Boolean.parseBoolean(executionsql(null, "execution", jdbcTemplate, sqlnextId,
							null));
			if (isexecution) {
				System.out.println("isexecution");
			}
		} else {
			id = Long.parseLong(idStr);
			Long nextId = id + 1;
			String sqlnextId = "update id_table set  next_val=" + nextId
					+ " where sequence_name= '" + zTablename + "'";
			executionsql(null, "execution", jdbcTemplate, sqlnextId, null);
		}
		return id;
	}
	// 获取下一个Id
		public static Long getnextId(Statement stmt,ResultSet rs1,String zTablename) throws Exception {
			//JdbcTemplate jdbcTemplate,
			// 根据表,查询下一个Id,如果不存在就
			String sqlCurrentid = "select next_val as nextId from id_table where sequence_name= '"+ zTablename + "'";
			//String idStr = executionsql(null, "nextId", jdbcTemplate, sqlCurrentid, null);
			 
			rs1 = stmt.executeQuery(sqlCurrentid);
			Long id =null;
			while (rs1.next()) {
				id=rs1.getLong("nextId");
			 } 
			if (id == null) {
				id = 1000l;
				Long nextId = id + 1;
				String sqlnextId = "INSERT INTO id_table(sequence_name,next_val )VALUES('"+ zTablename + "','" + nextId + "') ";
				//Boolean isexecution = Boolean.parseBoolean(executionsql(null, "execution", jdbcTemplate, sqlnextId,null));
				stmt.executeUpdate(sqlnextId);
			} else { 
				Long nextId = id + 1;
				String sqlnextId = "update id_table set  next_val=" + nextId+ " where sequence_name= '" + zTablename + "'";
				//executionsql(null, "execution", jdbcTemplate, sqlnextId, null);
				stmt.executeUpdate(sqlnextId);
			}
			return id;
		}
	/**
	 * 执行sql，
	 * @param ztable  对应数据库中的表
	 * @param type    执行类型，nextId 下一个ID ，execution 【insert,update ,delete】  ，query 查询sql ，count  查询个数
	 * @param dataSource  jdbc 对数据库查询
	 * @param sql    sql语句
	 * @param list    查询时的参数
	 * @return
	 */ 
	public static  String executionsql(ZTable ztable,String type,JdbcTemplate jdbcTemplate,String  sql,List<String> list)throws Exception{{
   
	        	if(type.equals("nextId")){
	        		Long id= getByTableId(jdbcTemplate, sql);
	        		if(id!=null)
	        			return id.toString();
	        		else
	        			return null;
	        	}
	        	else if(type.equals("execution")){
	        		return  execution(jdbcTemplate, sql).toString();
	        	}else if(type.equals("query")){
	        		return querysql(ztable,jdbcTemplate, sql, list).toString();
	        	}else if(type.equals("count")){
	        		return queryCountsql(jdbcTemplate, sql).toString();
	        	} 
			} 
			return null;
	}
	//执行sql insert,update ,delete
	public static Boolean execution(JdbcTemplate jdbcTemplate,String sql){
	
			// 检测数据库连接
			 jdbcTemplate.execute(sql);
	        return true;
		
	}
	//查询sql
	private static JSONObject querysql(ZTable ztable,JdbcTemplate jdbcTemplate,String sql,List<String> list)  {
		// 检测数据库连接
		System.out.println(sql);
		//json全部
		JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
		try { 
	        SqlRowSet rset = jdbcTemplate.queryForRowSet(sql);
	        int index=0;
	        while(rset.next()){
	        	JSONObject jsonObject1 = new JSONObject();
	        	for (ZColumn column : ztable.getZcolumns()) {
	        		//System.out.println(rset.getString("nextId"));
	        		String coumnData= rset.getString(column.getColName());
	        		if(coumnData!=null)
	        			jsonObject1.put(column.getColName(), coumnData);
	        		else 
	        			jsonObject1.put(column.getColName(), "");
				}
	        	jsonArray.put(index, jsonObject1);
	        	index++;
	        }
	        jsonObject.put(ztable.getTablename(), jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	private static Long getByTableId(JdbcTemplate jdbcTemplate,String sql){

			SqlRowSet rset = jdbcTemplate.queryForRowSet(sql); 
	        while(rset.next()){
	        	return rset.getLong("nextId");
	        }
		
		return null;
	}
	
	private static Integer queryCountsql(JdbcTemplate jdbcTemplate,String sql){
	
			SqlRowSet rset = jdbcTemplate.queryForRowSet(sql); 
	        while(rset.next()){
	        	return rset.getInt("counts");
	        }
		
		return null;
	}
	
	//查询sql  //单独执行
	/**
	 * 
	 * @param dataSource  数据库连接
	 * @param sql 执行sql
	 * @param list 返回的对应值
	 * @return
	 */
	public static JSONArray  querysqlJSONArray(JdbcTemplate jdbcTemplate,String sql,List<String> list)  {
	 
		// 检测数据库连接
		//json全部
        JSONArray jsonArray = new JSONArray();
		try { 
			SqlRowSet rset = jdbcTemplate.queryForRowSet(sql); 
	        int index=0;
	        while(rset.next()){
	        	JSONObject jsonObject1 = new JSONObject();
	        	for (String column : list) {
	        		//System.out.println(rset.getString("nextId"));
	        		String coumnData= rset.getString(column);
	        		if(coumnData!=null)
	        			jsonObject1.put(column, coumnData);
	        		else 
	        			jsonObject1.put(column, "");
				}
	        	jsonArray.put(index, jsonObject1);
	        	index++;
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} finally { 
		} 
		return jsonArray;
	}

	//查询sql
	public static JSONObject querysqlOrCal(ZTable ztable,JdbcTemplate jdbcTemplate,
			 String sql,List<String> list,Map<String ,String> valueMap)  { 
		// 检测数据库连接
		//json全部
		JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
		try {
			SqlRowSet rset = jdbcTemplate.queryForRowSet(sql); 
	        int index=0;
	        while(rset.next()){
	        	JSONObject jsonObject1 = new JSONObject();
	        	for (ZColumn column : ztable.getZcolumns()) {
	        		//System.out.println(rset.getString("nextId"));
	        		String coumnData= rset.getString(column.getColName());
	        		if(coumnData!=null)
	        			jsonObject1.put(column.getColName(), coumnData);
	        		else 
	        			jsonObject1.put(column.getColName(), "");
				}
	        	 for (Map.Entry<String, String> entry : valueMap.entrySet()) {
	        		 String coumnData= rset.getString(entry.getValue());
		        		if(coumnData!=null)
		        			jsonObject1.put(entry.getKey(), coumnData);
		        		else 
		        			jsonObject1.put(entry.getKey(), ""); 
	   		   	} 
	        	jsonArray.put(index, jsonObject1);
	        	index++;
	        }
	        jsonObject.put(ztable.getTablename(), jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
		} finally { 
		} 
		return jsonObject;
	}
	
	
	
	/**
	 * 保存form表的数据
	 * @param formPropertyList
	 * @param tempJsonObj
	 * @param formId
	 * @param ztable
	 * @throws Exception 
	 */
	public static void updateFormData(JdbcTemplate jdbcTemplate,List<FormProperty> formPropertyList,JSONObject tempJsonObj,Long tableDateId,ZTable ztable) throws Exception {
			//4.用zCloumnDataList来封装前台数据
			List<ZCloumnData> zCloumnDataList = new ArrayList<ZCloumnData>();
			for(FormProperty formProperty : formPropertyList) {
				//5.取到对应Form属性的值(?校验前台传过来的类型)
				if(tempJsonObj.has(formProperty.getFieldName())){
					String value = tempJsonObj.get(formProperty.getFieldName()).toString();
					//6.构造数据
					ZCloumnData zCloumnData = new ZCloumnData(formProperty.getFieldName(), formProperty.getFieldType(), value);
					zCloumnDataList.add(zCloumnData);	
				}
			}  
			 Date de= new Date();
			 SimpleDateFormat dataFm=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			 String data=dataFm.format(de); 
			
			ZCloumnData ModifyBy = new ZCloumnData("modifyBy", "attrString", UserUtil.getUser().getId().toString());//主键id
			zCloumnDataList.add(ModifyBy);
			ZCloumnData ModifyDate = new ZCloumnData("modifyDate", "attrDate", data);
			zCloumnDataList.add(ModifyDate);
			 
			if(tempJsonObj.has("expandId")){
				ZCloumnData expand = new ZCloumnData("expandId", "attrString", tempJsonObj.get("expandId").toString());
				zCloumnDataList.add(expand);
			}  
			//9.拼接SQL
			String updateFormsql = MySQLZDialect.updateColumnsValue(ztable.getTablename(), zCloumnDataList);
			updateFormsql+=" where id="+tableDateId;
			System.out.println(updateFormsql);
			//10.执行SQL
			SelectToJson.execution(jdbcTemplate, updateFormsql); 
		}
	public static void updateFormDataConn(Statement stmt ,List<FormProperty> formPropertyList,JSONObject tempJsonObj,Long tableDateId,ZTable ztable) throws Exception {
		//4.用zCloumnDataList来封装前台数据
		List<ZCloumnData> zCloumnDataList = new ArrayList<ZCloumnData>();
		for(FormProperty formProperty : formPropertyList) {
			//5.取到对应Form属性的值(?校验前台传过来的类型)
			if(tempJsonObj.has(formProperty.getFieldName())){
				String value = tempJsonObj.get(formProperty.getFieldName()).toString();
				//6.构造数据
				ZCloumnData zCloumnData = new ZCloumnData(formProperty.getFieldName(), formProperty.getFieldType(), value);
				zCloumnDataList.add(zCloumnData);	
			}
		}   
		boolean ismodifyDate=false;
		if(tempJsonObj.has("modifyBy")){
			ZCloumnData ModifyBy = new ZCloumnData("modifyBy", "attrString", tempJsonObj.getString("modifyBy")); 
			zCloumnDataList.add(ModifyBy); 
		}
		if(tempJsonObj.has("modifyDate")){
			ZCloumnData modifyDate = new ZCloumnData("modifyDate", "attrString", tempJsonObj.getString("modifyDate"));
			zCloumnDataList.add(modifyDate);
			ismodifyDate=true;
		}
		if(tempJsonObj.has("createBy")){
			ZCloumnData ModifyBy = new ZCloumnData("createBy", "attrString", tempJsonObj.getString("createBy"));
			zCloumnDataList.add(ModifyBy);
		}
		if(tempJsonObj.has("createDate")){
			ZCloumnData modifyDate = new ZCloumnData("createDate", "attrString", tempJsonObj.getString("createDate"));
			zCloumnDataList.add(modifyDate);
		}
		 Date de= new Date();
		 if(UserUtil.getUser()!=null){
			 ZCloumnData ModifyBy = new ZCloumnData("modifyBy", "attrString", UserUtil.getUser().getId().toString());//主键id
			zCloumnDataList.add(ModifyBy);	

			 SimpleDateFormat dataFm=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			 String data=dataFm.format(de); 
			ZCloumnData ModifyDate = new ZCloumnData("modifyDate", "attrDate", data);
			zCloumnDataList.add(ModifyDate);  
			ismodifyDate=true;
		 }
		 if(!ismodifyDate){
			 SimpleDateFormat dataFm=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			 String data=dataFm.format(de);  
			ZCloumnData modifyDate = new ZCloumnData("createDate", "attrString", data);
			zCloumnDataList.add(modifyDate);
			ZCloumnData ModifyDate = new ZCloumnData("modifyDate", "attrDate", data);
			zCloumnDataList.add(ModifyDate); 
		 }
		 
		if(tempJsonObj.has("expandId")){
			ZCloumnData expand = new ZCloumnData("expandId", "attrString", tempJsonObj.get("expandId").toString());
			zCloumnDataList.add(expand);
		}  
		//9.拼接SQL
		String updateFormsql = MySQLZDialect.updateColumnsValue(ztable.getTablename(), zCloumnDataList);
		updateFormsql+=" where id="+tableDateId;
		System.out.println(updateFormsql);
		//10.执行SQL
		//SelectToJson.executionConn(stmt, updateFormsql);
		stmt.executeUpdate(updateFormsql);
	}

	
	
	/**
	 * 保存form表的数据
	 * @param formPropertyList
	 * @param tempJsonObj
	 * @param formId
	 * @param ztable
	 * @throws Exception 
	 */
		public static void insertFormData(JdbcTemplate jdbcTemplate,List<FormProperty> formPropertyList,JSONObject tempJsonObj,Long formId,ZTable ztable,Integer  ishistoryTable) throws Exception {
			//4.用zCloumnDataList来封装前台数据
			List<ZCloumnData> zCloumnDataList = new ArrayList<ZCloumnData>();
			for(FormProperty formProperty : formPropertyList) {
				//5.取到对应Form属性的值(?校验前台传过来的类型)
				if(tempJsonObj.has(formProperty.getFieldName())){
					String value = tempJsonObj.get(formProperty.getFieldName()).toString(); 
					//6.构造数据
					ZCloumnData zCloumnData = new ZCloumnData(formProperty.getFieldName(), formProperty.getFieldType(), value);
					zCloumnDataList.add(zCloumnData);
				}
			}
			ZCloumnData pk = new ZCloumnData("id", "Long", formId.toString());//主键id
			zCloumnDataList.add(pk);

			ZCloumnData createBy = new ZCloumnData("createBy", "attrString", UserUtil.getUser().getId().toString());//主键id
			zCloumnDataList.add(createBy);
			//8.最后录入数据的时间
			 Date de= new Date();
			 SimpleDateFormat dataFm=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			 String data=dataFm.format(de);
			 
			ZCloumnData lastWriteData = new ZCloumnData("createDate", "attrDate", data);
			zCloumnDataList.add(lastWriteData); 
			
			ZCloumnData ModifyBy = new ZCloumnData("modifyBy", "attrString", UserUtil.getUser().getId().toString());//主键id
			zCloumnDataList.add(ModifyBy);
			
			ZCloumnData ModifyDate = new ZCloumnData("modifyDate", "attrDate", data);
			zCloumnDataList.add(ModifyDate);
			
			ZCloumnData guid = new ZCloumnData("guid", "attrString", java.util.UUID.randomUUID().toString());
			zCloumnDataList.add(guid);
			//taskId 和participationId
			if(tempJsonObj.has("participationId")){
				ZCloumnData participationId = new ZCloumnData("participationId", "attrString", tempJsonObj.get("participationId").toString());
				zCloumnDataList.add(participationId);
			}
			if(tempJsonObj.has("taskId")){
				ZCloumnData taskId = new ZCloumnData("taskId", "attrString", tempJsonObj.get("taskId").toString());
				zCloumnDataList.add(taskId);
			}
			if(tempJsonObj.has("expandId")){
				ZCloumnData expand = new ZCloumnData("expandId", "attrString", tempJsonObj.get("expandId").toString());
				zCloumnDataList.add(expand);
			} 
			//9.拼接SQL
			String insertFormsql = MySQLZDialect.insertData(ztable, ztable.getTablename() ,zCloumnDataList);
			System.out.println(insertFormsql);
			//10.执行SQL
			//ExecutionSql.executionsql(null, "execution", dataSource, insertFormsql, null);
			SelectToJson.execution(jdbcTemplate, insertFormsql);
			//7.构造ID
			if(ishistoryTable!=null&&ishistoryTable==1){
				ZCloumnData history = new ZCloumnData("historyId", "Long", formId.toString());//主键id
				zCloumnDataList.add(history);				
				String insertFormHistorysql = MySQLZDialect.insertData(ztable,ztable.getTablename()+"_history", zCloumnDataList);
				System.out.println(insertFormHistorysql);
				
				SelectToJson.execution(jdbcTemplate, insertFormHistorysql);
				//ExecutionSql.executionsql(null, "execution", dataSource, insertFormHistorysql, null); 
			}
		}
		
		public static void insertFormDataConn(Statement stmt ,List<FormProperty> formPropertyList,JSONObject tempJsonObj,Long formId,ZTable ztable,Integer  ishistoryTable) throws Exception {
			//4.用zCloumnDataList来封装前台数据
			List<ZCloumnData> zCloumnDataList = new ArrayList<ZCloumnData>();
			for(FormProperty formProperty : formPropertyList) {
				//5.取到对应Form属性的值(?校验前台传过来的类型)
				if(tempJsonObj.has(formProperty.getFieldName())){
					String value = tempJsonObj.get(formProperty.getFieldName()).toString(); 
					//6.构造数据
					ZCloumnData zCloumnData = new ZCloumnData(formProperty.getFieldName(), formProperty.getFieldType(), value);
					zCloumnDataList.add(zCloumnData);
				}
			}
			boolean  ismodifyDate=false;
			if(tempJsonObj.has("modifyBy")){
				ZCloumnData ModifyBy = new ZCloumnData("modifyBy", "attrString", tempJsonObj.getString("modifyBy"));//主键id
				zCloumnDataList.add(ModifyBy);
			}
			if(tempJsonObj.has("modifyDate")){
				ZCloumnData modifyDate = new ZCloumnData("modifyDate", "attrString", tempJsonObj.getString("modifyDate"));//主键id
				zCloumnDataList.add(modifyDate);
				ismodifyDate=true;
			}
			if(tempJsonObj.has("createBy")){
				ZCloumnData ModifyBy = new ZCloumnData("createBy", "attrString", tempJsonObj.getString("createBy"));
				zCloumnDataList.add(ModifyBy);
			}
			if(tempJsonObj.has("createDate")){
				ZCloumnData modifyDate = new ZCloumnData("createDate", "attrString", tempJsonObj.getString("createDate"));
				zCloumnDataList.add(modifyDate);
			}
			
			ZCloumnData pk = new ZCloumnData("id", "Long", formId.toString());//主键id
			zCloumnDataList.add(pk);
			if(UserUtil.getUser()!=null){
				ZCloumnData createBy = new ZCloumnData("createBy", "attrString", UserUtil.getUser().getId().toString());//主键id
				zCloumnDataList.add(createBy);	
				ZCloumnData ModifyBy = new ZCloumnData("modifyBy", "attrString", UserUtil.getUser().getId().toString());//主键id
				zCloumnDataList.add(ModifyBy);

				//8.最后录入数据的时间
				 Date de= new Date();
				 SimpleDateFormat dataFm=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				 String data=dataFm.format(de);				 
				ZCloumnData lastWriteData = new ZCloumnData("createDate", "attrDate", data);
				zCloumnDataList.add(lastWriteData);
				
				ZCloumnData ModifyDate = new ZCloumnData("modifyDate", "attrDate", data);
				zCloumnDataList.add(ModifyDate);
				ismodifyDate=true;
			}
			 if(!ismodifyDate){
				 Date de= new Date();
				 SimpleDateFormat dataFm=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				 String data=dataFm.format(de);  
				ZCloumnData modifyDate = new ZCloumnData("createDate", "attrString",data);
				zCloumnDataList.add(modifyDate);
				ZCloumnData ModifyDate = new ZCloumnData("modifyDate", "attrDate", data);
				zCloumnDataList.add(ModifyDate); 
			 }
			ZCloumnData guid = new ZCloumnData("guid", "attrString", java.util.UUID.randomUUID().toString());
			zCloumnDataList.add(guid);
			//taskId 和participationId
			if(tempJsonObj.has("participationId")){
				ZCloumnData participationId = new ZCloumnData("participationId", "attrString", tempJsonObj.get("participationId").toString());
				zCloumnDataList.add(participationId);
			}
			if(tempJsonObj.has("taskId")){
				ZCloumnData taskId = new ZCloumnData("taskId", "attrString", tempJsonObj.get("taskId").toString());
				zCloumnDataList.add(taskId);
			}
			if(tempJsonObj.has("expandId")){
				ZCloumnData expand = new ZCloumnData("expandId", "attrString", tempJsonObj.get("expandId").toString());
				zCloumnDataList.add(expand);
			} 
			//9.拼接SQL
			String insertFormsql = MySQLZDialect.insertData(ztable, ztable.getTablename() ,zCloumnDataList);
			System.out.println(insertFormsql);
			//10.执行SQL
			//SelectToJson.execution(jdbcTemplate, insertFormsql);
			stmt.executeUpdate(insertFormsql);
			//7.构造ID
			if(ishistoryTable!=null&&ishistoryTable==1){
				ZCloumnData history = new ZCloumnData("historyId", "Long", formId.toString());//主键id
				zCloumnDataList.add(history);
				String insertFormHistorysql = MySQLZDialect.insertData(ztable,ztable.getTablename()+"_history", zCloumnDataList);
				System.out.println(insertFormHistorysql);
				
				//SelectToJson.execution(jdbcTemplate, insertFormHistorysql);
				stmt.executeUpdate(insertFormHistorysql);
				//ExecutionSql.executionsql(null, "execution", dataSource, insertFormHistorysql, null); 
			}
		}
		
		
		
		
}
