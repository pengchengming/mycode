package com.bizduo.zflow.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.bizduo.common.module.dao.PageTrace;

public class SelectToJson {
 
	public static List<Map<String,String>>  executionsqlMap(JdbcTemplate jdbcTemplate, String sql, 
			String type, Map<String, String> fieldMap, Map<String, String> pageMap) {
   
		try {
			if (null != type) {
				if (type.equals("query")) {
					return querysqlMap(jdbcTemplate , sql, fieldMap, pageMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally { 
		}
		return null;
	}
	
	
	public static  String executionsql(JdbcTemplate jdbcTemplate, String  sql, String type, 
			Map<String, String> fieldMap, Map<String, Integer> pageMap) { 
		try {
			if (type != null) {
				if (type.equals("query")) {
					int pageIndex = 0;
					int pageSize = 0;
					if (pageMap != null) {
						pageIndex = pageMap.get("pageIndex");
						pageSize = pageMap.get("pageSize");
					}
					return querysql(jdbcTemplate , sql, fieldMap, pageIndex, pageSize).toString();
				} else if (type.equals("execution")) {
					return executionsql(jdbcTemplate, sql).toString();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 
		}
		return null;
	}
	 
	private static Boolean executionsql(JdbcTemplate jdbcTemplate, String sql ) {
		try {
			// 检测数据库连接
			 jdbcTemplate.execute(sql); 
	        return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}


	private static List<Map<String,String>> querysqlMap(JdbcTemplate jdbcTemplate, String sql, Map<String, String> fieldMap, Map<String, String> pageMap) {
		try {
			int counts = 0;
			int pageIndex = 0;
			int pageSize = 0;
			if (null != pageMap) {
				pageIndex = Integer.parseInt(pageMap.get("pageNo"));
				pageSize = Integer.parseInt(pageMap.get("pageSize"));
			}
			if (0 != pageIndex && 0 != pageSize) {
				String countStr = "";
				if (sql.toUpperCase().indexOf("ORDER") != -1) {
					countStr = sql.substring(sql.toUpperCase().indexOf("FROM"),
							sql.toUpperCase().indexOf("ORDER"));
				} else {
					countStr = sql.substring(sql.toUpperCase().indexOf("FROM"));
				}
				String sqlCount = " select count(*) counts   " + countStr;
				counts = queryCountsql(jdbcTemplate, sqlCount);
				sql= getsqlpage(sql,counts,pageIndex,pageSize);
			} 
			System.out.println(sql); 
			SqlRowSet rset = jdbcTemplate.queryForRowSet(sql);
			List<Map<String, String>> temmapList = new ArrayList<Map<String, String>>();
			while (rset.next()) {
				Map<String, String> temmap = new HashMap<String, String>();
				for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
					String coumnData = rset.getString(entry.getValue());
					if (coumnData != null)
						temmap.put(entry.getKey(), coumnData);
					else
						temmap.put(entry.getKey(), "");
				}
				temmapList.add(temmap);
			}
			if (pageIndex != 0 && pageSize != 0) {

				int pageCnt = counts / pageSize;
				if (counts % pageSize != 0)
					pageCnt = pageCnt + 1;
				JSONObject pagedJson = pageStr(pageIndex, pageCnt, pageSize,
						counts);
				if(pagedJson.has("pagem")) pageMap.put("pagem", pagedJson.getString("pagem"));// 每页
				if(pagedJson.has("pageCnt")) pageMap.put("pageCnt", pagedJson.getString("pageCnt"));// 一共多少页
				if(pagedJson.has("cordCnt")) pageMap.put("cordCnt", pagedJson.getString("cordCnt"));// 共条
			}

			return temmapList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@SuppressWarnings("unused")
	private static JSONObject querysql( JdbcTemplate jdbcTemplate,String sql,Map<String,String> fieldMap,int pageIndex, int pageSize)  {
		// 检测数据库连接
		//json全部
		JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
		try {
			int counts=0;
	        if(pageIndex!=0&&pageSize!=0){ 
	        	String countStr="";
	        	sql= sql.toUpperCase();
	        	if(sql.indexOf("order")!=-1){
	        		countStr=sql.substring(sql.indexOf("FROM"), sql.indexOf("ORDER"));
	        	}else {
	        		countStr=sql.substring(sql.indexOf("FROM"));
	        	} 
				String sqlCount=" select count(*) counts   " +countStr;
				counts= queryCountsql(jdbcTemplate, sqlCount);
				  
				if(true){
					/*
					int pageCnt = counts / pageSize;
					if (counts % pageSize != 0)pageCnt = pageCnt + 1;
					 
					int endlimit = pageIndex * pageSize;
					int currentPageSize=pageSize;
					if((counts<endlimit))
						currentPageSize=counts % pageSize;
					*/
					PageTrace pageTrace = new PageTrace(pageSize);
					pageTrace.setPageIndex(pageIndex);
					pageTrace.setTotal(counts);
					sql= sql+ " limit  "+pageTrace.getStart()+", "+pageSize;
				}else{
					sql= getsqlpage(sql,counts,pageIndex,pageSize);	
				}		    
	        } 
	        SqlRowSet rset = jdbcTemplate.queryForRowSet(sql);	      
	        int index=0;
	        while(rset.next()){
	        	JSONObject jsonObject1 = new JSONObject();
	        	for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
	        		String coumnData= rset.getString(entry.getValue());
	        		if(coumnData!=null)
	        			jsonObject1.put(entry.getKey() , coumnData);
	        		else 
	        			jsonObject1.put(entry.getKey() , ""); 
	        	} 
	        	jsonArray.put(index, jsonObject1);
	        	index++;
	        }
	        jsonObject.put("code", 1);
	        jsonObject.put("results", jsonArray);
	        if(pageIndex!=0&&pageSize!=0){ 
		        int pageCnt=counts/pageSize;
		        if(counts%pageSize!=0)pageCnt=pageCnt+1;
		        JSONObject pagedJson= pageStr(pageIndex,pageCnt,pageSize,counts);
		        jsonObject.put("paged", pagedJson);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	
	/**
	 * 获取分页的sql
	 * @param sql
	 * @param counts
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	private static String getsqlpage(String sql,int counts,int pageIndex,int pageSize){
  		if (sql.toUpperCase().indexOf("SELECT") != -1) {
			sql=sql.substring(sql.toUpperCase().indexOf("SELECT")+6); 
		}
		int pageCnt = counts / pageSize;
		if (counts % pageSize != 0)pageCnt = pageCnt + 1;
		 
		int endlimit = pageIndex * pageSize;
//		int currentPageSize=pageSize;
//		if((counts<endlimit))
//			currentPageSize=counts % pageSize; 
		/*
		sql = "select top "
				+ endlimit
				+ "  "
				+ sql.substring(sql.toUpperCase().indexOf("SELECT") + 6); 
		sql=" SELECT * FROM  (     SELECT TOP "+currentPageSize+" * FROM    ("+
		sql +"    ) w "+unOrderBySql+"  ) w2    "+orderBySql;*/
		
		sql = "   select *  from ( select row_number()over(order by tempColumn)rownumber,*  "
				+ " from (select top "+endlimit+" tempColumn=0,    "
				+  sql+ "  )tableOne   )tableTwo "
				+ "   where rownumber > "+(pageIndex-1)*10;
		   
		return sql;
	}
	//一共多少条记录
	private static Integer queryCountsql( JdbcTemplate jdbcTemplate,String sql){
	 
        int returnCount=0;
		try {
	        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
	        while(rs.next()){
	        	returnCount= rs.getInt("counts");
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} finally { 
		} 
		return returnCount;
	}
	
	 
    public static JSONObject pageStr(int pageIndex, int pageCnt, int lcsCount, int RecordCnt) throws JSONException
    {
    	JSONObject pagedJson = new JSONObject();
        ////#region  活动 分页
        if (pageIndex > 0 && pageIndex <= pageCnt && pageCnt > 0)
        {
        	pagedJson.put("pageIndex", pageIndex);//第页 
        	pagedJson.put("lcsCount", lcsCount);//本页条
        	pagedJson.put("cordCnt", RecordCnt);//共条 
            String pagem = "";
            for (int i = ((pageIndex - 1) / 10) * 10 + 1; i <= ((pageIndex - 1) / 10) * 10 + 20 && i <= pageCnt; i++)
            {
                pagem = pagem + i + "_";
            }
            pagedJson.put("pagem", pagem);//每页
            pagedJson.put("pageCnt", pageCnt);//共数页  
        }
       // //#endregion  活动 分页
        return pagedJson;
    } 
    
    
    
    @SuppressWarnings("unused")
	public static JSONObject jdbcTemplateQuerysql(JdbcTemplate jdbcTemplate, Map<String,String> sqlmap,Map<String,String> fieldMap,PageTrace pageTrace) throws Exception  {
		// 检测数据库连接
		//json全部
		JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
		
			String sql=sqlmap.get("sql").toUpperCase();
			String sqlForm=sqlmap.get("sqlForm"); 
			int counts=0;
			Integer pageSize=0;
			    if(pageTrace!=null){
	        	pageSize=pageTrace.getRecordPerPage();
	        	String countStr="";
	        	if(sqlForm!=null&&!sqlForm.equals("")){
	        		countStr=" select count(*) counts "+sqlForm;
	        	}else { 
		        	if(sql.indexOf("order")!=-1){
		        		countStr=sql.substring(sql.indexOf("FROM"), sql.indexOf("ORDER"));
		        	}else {
		        		countStr=sql.substring(sql.indexOf("FROM"));
		        	} 
		        	countStr=" select count(*) counts   " +countStr;
	        	}
				String groupBy=sqlmap.get("groupBy");
				if(StringUtils.isNotBlank(groupBy) ) {
					countStr=" SELECT count(*) as counts from (  " +countStr +" ) as countstable ";
				}
	        	SqlRowSet rs = jdbcTemplate.queryForRowSet(countStr);  
	        	 while(rs.next()){
	        		 counts= rs.getInt("counts"); 
	 	        }
				if(true){ 
					pageTrace.setTotal(counts);
					sql= sql+ " limit  "+pageTrace.getStart()+", "+pageSize;
				}else{
					pageTrace.setTotal(counts);
					sql= getsqlpage(sql,counts,pageTrace.getPageIndex(),pageSize);	
				}		    
	        } 
	        
	        
			SqlRowSet rset = jdbcTemplate.queryForRowSet(sql); 
	        int index=0;
	        while(rset.next()){
	        	JSONObject jsonObject1 = new JSONObject();
	        	for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
	        		String coumnData= rset.getString(entry.getValue().toUpperCase());
	        		if(coumnData!=null)
	        			jsonObject1.put(entry.getKey() , coumnData);
	        		else 
	        			jsonObject1.put(entry.getKey() , ""); 
	        	} 
	        	jsonArray.put(index, jsonObject1);
	        	index++;
	        }
	        jsonObject.put("code", 1);
	        jsonObject.put("results", jsonArray);
	        if(pageTrace!=null){  
		        int pageCnt=counts/pageSize;
		        if(counts%pageSize!=0)pageCnt=pageCnt+1;
		        JSONObject pagedJson= pageStr(pageTrace.getPageIndex(),pageCnt,pageSize,counts);
		        jsonObject.put("paged", pagedJson);
	        }
	
		return jsonObject;
	}
    
    
    @SuppressWarnings({ "unused" })
   	public static JSONObject jdbcTemplateQuerysql(JdbcTemplate jdbcTemplate,
   			Map<String,String> sqlmap,Map<String,String> fieldMap,PageTrace pageTrace,
   			String byIdconfig) throws Exception  {
   		// 检测数据库连接
   		//json全部
   		JSONObject jsonObject = new JSONObject();
           JSONArray jsonArray = new JSONArray();
   		
   			String sql=sqlmap.get("sql").toUpperCase();
   			String sqlForm=sqlmap.get("sqlForm"); 
   			int counts=0;
   			Integer pageSize=0;
   			    if(pageTrace!=null){
   	        	pageSize=pageTrace.getRecordPerPage();
   	        	String countStr="";
   	        	if(sqlForm!=null&&!sqlForm.equals("")){
   	        		countStr=" select count(*) counts "+sqlForm;
   	        	}else { 
   		        	if(sql.indexOf("order")!=-1){
   		        		countStr=sql.substring(sql.indexOf("FROM"), sql.indexOf("ORDER"));
   		        	}else {
   		        		countStr=sql.substring(sql.indexOf("FROM"));
   		        	} 
   		        	countStr=" select count(*) counts   " +countStr;
   	        	}
   				String groupBy=sqlmap.get("groupBy");
   				if(StringUtils.isNotBlank(groupBy) ) {
   					countStr=" SELECT count(*) as counts from (  " +countStr +" ) as countstable ";
   				}
   	        	SqlRowSet rs = jdbcTemplate.queryForRowSet(countStr);  
   	        	 while(rs.next()){
   	        		 counts= rs.getInt("counts"); 
   	 	        }
   				if(false){ 
   					pageTrace.setTotal(counts);
   					sql= sql+ " limit  "+pageTrace.getStart()+", "+pageSize;
   				}else{
   					pageTrace.setTotal(counts);
   					sql= getsqlpage(sql,counts,pageTrace.getPageIndex(),pageSize);	
   				}		    
   	        } 
   	        boolean isconfig=false;
   			if(StringUtils.isNotBlank(byIdconfig))
   				isconfig=true;
   			SqlRowSet rset = jdbcTemplate.queryForRowSet(sql); 
   	        int index=0;
   	        while(rset.next()){
   	        	JSONObject jsonObject1 = new JSONObject();
   	        	for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
   	        		String coumnData= rset.getString(entry.getValue().toUpperCase());
   	        		if(coumnData!=null)
   	        			jsonObject1.put(entry.getKey() , coumnData);
   	        		else 
   	        			jsonObject1.put(entry.getKey() , "");
   	        		if(isconfig){
   	        			JSONObject jsonObj = new JSONObject(byIdconfig);
	   	     			if(jsonObj.has("fromConfig")){
		   	     			JSONArray formArray= jsonObj.getJSONArray("fromConfig"); 
	   	     				for(int i = 0; i < formArray.length(); i++) {
		   	     				JSONObject tempObj = formArray.getJSONObject(i);	
		   	     				if(tempObj.has("formCode")&&tempObj.has("parentId")){
		   	     					String parentId= tempObj.getString("parentId");
			   	     				if(parentId.toUpperCase().equals(entry.getValue().toUpperCase())){
			   	     					String formCodetemp= tempObj.getString("formCode");
			   	     					String fieldNamestr= tempObj.getString("fieldName");
			   	     					String subsql="select "+fieldNamestr+" from "+formCodetemp+" where "+tempObj.getString("currentField")+"="+coumnData;
				   	     				JSONArray subArray=subJsonConfig(jdbcTemplate, tempObj, subsql);
			   	     					String aliasesName= tempObj.getString("aliasesName");
	  		   	   	        			jsonObject1.put(aliasesName, subArray);
			   	     				}
		   	     				}
	   	     				}	
	   	     			}
   	     				
   	        		}
   	        		/*
   	        		if(returnsubMap!=null){
   	        			for (Map.Entry<String,Map<String,Map<String,Map<String,String>>>> subreturnMap : returnsubMap.entrySet()) {
   	        				String mapkey= subreturnMap.getKey();
   	        				Map subMap=returnsubMap.get(mapkey);
   	        				Map<String,Map<String,String>> subSqlMap=(Map<String, Map<String, String>>) subMap.get("sqlMap");
   	        				Map<String,Map<String,String>> subFieldMap= (Map<String, Map<String, String>>) subMap.get("fieldMap");
   	        				boolean isSubMap=false;
   	        				if(subMap.size()>2)
   	        					isSubMap=true;
   	        				for (Map.Entry<String, Map<String,String>> submap : subSqlMap.entrySet()) {
   	   	        				String aliasesName= submap.getKey();
   	   	        				Map<String,String> subSqltempMap=subSqlMap.get(aliasesName);
   	   	        				String formCode=subSqltempMap.get("formCode");
   	   	        				String parentId= subSqltempMap.get("parentId").toUpperCase();
   	   	        				if(isSubMap){
   	   	   	        				if(parentId.equals(entry.getValue().toUpperCase())){
   	   	   	   	        				//String subSql=subSqltempMap.get("sql")+coumnData ;
   	   		   	   	        			JSONArray subArray= subJson(jdbcTemplate,  subMap,  rset);
   	   		   	   	        			jsonObject1.put(aliasesName, subArray);
   	   	   	        				}	
   	   	        				}
   	   	        			} 
   	        			} 
   	        		} 
   	        		*/
   	        	} 
   	        	jsonArray.put(index, jsonObject1);
   	        	index++;
   	        }
   	        jsonObject.put("code", 1);
   	        jsonObject.put("results", jsonArray);
   	        if(pageTrace!=null){  
   		        int pageCnt=counts/pageSize;
   		        if(counts%pageSize!=0)pageCnt=pageCnt+1;
   		        JSONObject pagedJson= pageStr(pageTrace.getPageIndex(),pageCnt,pageSize,counts);
   		        jsonObject.put("paged", pagedJson);
   	        }
   	
   		return jsonObject;
   	}
 
    public static  JSONArray subJsonConfig(JdbcTemplate jdbcTemplate,JSONObject tempObj,String subsql){
    	boolean isconfig=false;
    	if(tempObj.has("fromConfig"))
			isconfig=true;
        JSONArray jsonArray = new JSONArray();
    	SqlRowSet rset = jdbcTemplate.queryForRowSet(subsql); 
		String fieldNamestr= tempObj.getString("fieldName");
    	 int index=0;
	        while(rset.next()){
	        	JSONObject jsonObject1 = new JSONObject();
	        	String[] fieldNames= fieldNamestr.split(",");
				for (String field : fieldNames) {
					String coumnData= rset.getString(field.toUpperCase());
	        		if(coumnData!=null)
	        			jsonObject1.put(field , coumnData);
	        		else 
	        			jsonObject1.put(field , "");
	        		if(isconfig){
	    				JSONArray formArray= tempObj.getJSONArray("fromConfig"); 
	    				for(int i = 0; i < formArray.length(); i++) {
	   	     				JSONObject subtempObj = formArray.getJSONObject(i);	
	   	     				if(subtempObj.has("formCode")&&subtempObj.has("parentId")){
	   	     					String parentId= subtempObj.getString("parentId");	   	     					
		   	     				if(parentId.toUpperCase().equals(field.toUpperCase())){
		   	     					String subformCodetemp= subtempObj.getString("formCode");
		   	     					String subfieldNamestr= subtempObj.getString("fieldName");
		   	     					String subsql1="select "+subfieldNamestr+" from "+subformCodetemp+" where "+subtempObj.getString("currentField")+"="+coumnData;
		   	     					JSONArray subArray=subJsonConfig(jdbcTemplate, subtempObj, subsql1);
		   	     					String aliasesName= subtempObj.getString("aliasesName");
  		   	   	        			jsonObject1.put(aliasesName, subArray);
		   	     				}
	   	     				}
   	     				}
	        		}
				}
	        	jsonArray.put(index, jsonObject1);
	        	index++;
	        }
	        return  jsonArray;
    }
    
    
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static JSONArray  subJson(JdbcTemplate jdbcTemplate,   Map subMap,SqlRowSet rsetParent){

	        JSONArray jsonArray = new JSONArray();
    	  for (Object obj : subMap.keySet()) {
    		  String aliasesName=obj.toString();
    		  if(!aliasesName.equals("sqlMap")&&!aliasesName.equals("fieldMap")){
    			  	Map<String,Map<String,Map<String,String>>> subMap1= (Map<String, Map<String, Map<String, String>>>) subMap.get(obj);
    			  	boolean isSubMap=false;
       				if(subMap1.size()>2)
       					isSubMap=true;
  					Map<String,Map<String,String>> subSqlMap=(Map<String, Map<String, String>>) subMap1.get("sqlMap");
  					Map<String,Map<String,String>> subFieldMap= (Map<String, Map<String, String>>) subMap1.get("fieldMap");
  					Map<String,String>  fieldMap= subFieldMap.get(aliasesName);
  					Map<String,String> subSqltempMap=subSqlMap.get(aliasesName);
  					String parentIdData= rsetParent.getString(subSqltempMap.get("parentId").toUpperCase());
  					String sql=subSqltempMap.get("sql")+parentIdData ;
 
  			    	SqlRowSet rset = jdbcTemplate.queryForRowSet(sql); 
  				        int index=0;
  				        while(rset.next()){
  				        	JSONObject jsonObject1 = new JSONObject();
  				        	for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
  				        		String coumnData= rset.getString(entry.getValue().toUpperCase());
  				        		if(coumnData!=null)
  				        			jsonObject1.put(entry.getKey() , coumnData);
  				        		else 
  				        			jsonObject1.put(entry.getKey() , ""); 
  				        		
  				        		if(isSubMap){
  	   	   	        				String parentId= subSqltempMap.get("parentId").toUpperCase();
		  	   	   	        		if(parentId.equals(entry.getValue().toUpperCase())){ 
			   	   	        			JSONArray subArray= subJson(jdbcTemplate, subMap1,rset);
			   	   	        			jsonObject1.put(aliasesName, subArray);
				        			}	
  					        		  
  				        		}
  				        		
  				        	} 
  				        	jsonArray.put(index, jsonObject1);
  				        	index++;
  				        }
    		  } 				
    	  }
	        return jsonArray;
    }
    
    public static Boolean execution(JdbcTemplate jdbcTemplate,String sql){
			try {
				// 检测数据库连接
				 jdbcTemplate.execute(sql);
		        return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} 
		}
	
 // 获取下一个Id
	public static Long getnextId(JdbcTemplate jdbcTemplate,String zTablename) throws Exception {
		// 根据表,查询下一个Id,如果不存在就
		String sqlCurrentid = "select next_val as nextId from id_table where sequence_name= '"
				+ zTablename + "'";
		//String idStr = executionsql(null, "nextId", dataSource, sqlCurrentid, null);
		String idStr ="";
		SqlRowSet rset = jdbcTemplate.queryForRowSet(sqlCurrentid); 
		 while(rset.next()){
			 idStr= rset.getString("nextId");
	     } 
		
		Long id = null;
		if (idStr == null || idStr.trim().equals("")) {
			id = 1000l;
			Long nextId = id + 1;
			String sqlnextId = "INSERT INTO id_table(sequence_name,next_val )VALUES('"+ zTablename + "','" + nextId + "') ";
			execution(jdbcTemplate, sqlnextId);
			 
		} else {
			id = Long.parseLong(idStr);
			Long nextId = id + 1;
			String sqlnextId = "update id_table set  next_val=" + nextId + " where sequence_name= '" + zTablename + "'";
			execution(jdbcTemplate, sqlnextId); 
		}
		return id;
	}
}
