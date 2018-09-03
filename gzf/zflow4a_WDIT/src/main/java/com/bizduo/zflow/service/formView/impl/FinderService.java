package com.bizduo.zflow.service.formView.impl;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.formView.IFinderService;
@Service
public class FinderService extends BaseService<Object, Integer> implements IFinderService {

	public String getBySql(String sql, String type, Map<String, String> fieldMap, Map<String, Integer> pageMap) {
		if(null != type){
			if(("query").equals(type)){
				int pageIndex = 0;
				int pageSize = 10;
				if(null != pageMap) {
					pageIndex = pageMap.get("pageIndex");
					pageSize = pageMap.get("pageSize");
				}
				return query(sql, fieldMap, pageIndex, pageSize).toString();
			}else if(("execution").equals(type)){
				return execution(sql).toString();
			}
		}
		return null;
	}
	
	public String executionSql(String sql, String type, Map<String, String> fieldMap, Map<String, Integer> pageMap) {
		if(null != type){
			if(type.equals("query")){
				int pageIndex = 0;
				int pageSize = 0;
				if(null != pageMap){
					pageIndex = pageMap.get("pageIndex");
					pageSize = pageMap.get("pageSize");
				}
				return query(sql, fieldMap, pageIndex, pageSize).toString();
			} else if (type.equals("execution")) {
				return execution(sql).toString();
			}
		}
		return null;
	}
	
	protected JSONObject query(String sql, Map<String,String> fieldMap, int pageIndex, int pageSize){
		JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();
		try {
			int count = 0;
	        if(0 < pageIndex && 0 < pageSize){ 
	        	String countStr = "";
	        	if(-1 != sql.indexOf("order")){
	        		countStr = sql.substring(sql.indexOf("from"), sql.indexOf("order"));
	        	}else{
	        		countStr = sql.substring(sql.indexOf("from"));
	        	} 
				String sqlCount = " select count(*) counts " + countStr;
				count = getCount(sqlCount);
				//MYSQL
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
					pageTrace.setTotal(count);
					sql = sql + " limit " + pageTrace.getStart() + ", " + pageSize;
					//MSSQL
//					sql = orderPaged(sql, count, pageIndex, pageSize);	
				}		    
	        } 
			SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
	        int index = 0;
	        
	        while(rs.next()){
	        	JSONObject temp = new JSONObject();
	        	for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
	        		String data = rs.getString(entry.getValue());
	        		if(null != data)
	        			temp.put(entry.getKey() , data);
	        		else 
	        			temp.put(entry.getKey() , ""); 
	        	} 
	        	array.put(index, temp);
	        	index++;
	        }
	        obj.put("code", 1);
	        obj.put("results", array);
	        if(0 < pageIndex && 0 < pageSize){ 
		        int pageCnt = count/pageSize;
		        if(0 != (count%pageSize))
		        	pageCnt = pageCnt + 1;
		        JSONObject paged = paged(pageIndex, pageCnt, pageSize, count);
		        obj.put("paged", paged);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	 protected Boolean execution(String sql){
		try {
			// 检测数据库连接
			super.jdbcTemplate.execute(sql);
	        return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
	}
	
	//一共多少条记录
	protected Integer getCount(String sql){
		return jdbcTemplate.queryForObject(sql, Integer.class);  
	}
		
	/**
	 * 获取分页的sql
	 * @param sql
	 * @param counts
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	protected String orderPaged(String sql, int count, int pageIndex, int pageSize){
		StringBuilder order = new StringBuilder();
		StringBuilder unOrder = new StringBuilder();
		if(-1 != sql.toUpperCase().indexOf("ORDER")){
			order.append(sql.substring(sql.toUpperCase().indexOf("ORDER")));
			if(-1 != order.toString().toUpperCase().indexOf(".")){ 
				String[] array = order.toString().split("\\.");//多个
				order.setLength(0); 
				order.append("  ORDER BY  ");
				unOrder.append("  ORDER BY  ");
				for(String s : array){
					if(-1 == s.toUpperCase().indexOf("ORDER")){
						order.append(s);
						if(s.toUpperCase().trim().endsWith(" DESC")){
							String t = order.toString();
							unOrder.append(t.substring(0, t.toUpperCase().indexOf(" DESC"))).append(" ,");
						}else {
							if(s.toUpperCase().trim().endsWith(" ASC")){
								String t = order.toString();
								unOrder.append(t.substring(0, t.toUpperCase().indexOf(" ASC "))).append(" DESC,");
							}else{
								unOrder.append(order).append(" DESC,");
							} 
						}
					}
				}
			}
		}
		String t1 = order.toString();
		String t2 = unOrder.toString();
		if(!("").equals(t2.trim()) && t2.toUpperCase().trim().endsWith(" ,")){
			unOrder.setLength(0);
			unOrder.append(t2.substring(0, t2.length() - 1));
		}else{
			if(!("").equals(t1.trim())){
				if(t1.trim().endsWith(" DESC")){
					unOrder.append(t1.substring(0, t1.toUpperCase().indexOf(" DESC"))).append("  ");
				}else{
					if(t1.toUpperCase().trim().endsWith(" ASC")){
						unOrder.append(t1.substring(0, t1.toUpperCase().indexOf(" ASC "))).append(" DESC ");
					}else{
						unOrder.append(t1).append(" DESC ");
					} 
				} 
			}
		}
		
		int pageCnt = count / pageSize;
		if(0 != count % pageSize)
			pageCnt = pageCnt + 1;
		 
		int endlimit = pageIndex * pageSize;
		int cursize = pageSize;
		if((count < endlimit))
			cursize = count % pageSize;
		StringBuilder sqls = new StringBuilder();
		sqls.append("SELECT TOP ")
		.append(endlimit).append(" ")
		.append(sql.substring(sql.toUpperCase().indexOf("SELECT") + 6))
		.append(" SELECT * FROM  (SELECT TOP ")
		.append(cursize)
		.append(" * FROM (")
		.append(sql)
		.append(") w ")
		.append(unOrder)
		.append(") w2 ")
		.append(order);
		return sqls.toString();
	}
		
	protected JSONObject paged(int pageIndex, int pageCnt, int lcsCount, int recordCnt) throws JSONException{
		JSONObject paged = new JSONObject();
	    ////#region  活动 分页
	    if(0 < pageIndex && pageIndex <= pageCnt && 0 < pageCnt){
	    	paged.put("pageIndex", pageIndex);//第页 
	    	paged.put("lcsCount", lcsCount);//本页条
	    	paged.put("cordCnt", recordCnt);//共条 
	        String pagem = "";
	        for(int i = ((pageIndex - 1) / 10) * 10 + 1; i <= ((pageIndex - 1) / 10) * 10 + 20 && i <= pageCnt; i++)
	            pagem = pagem + i + "_";
	        paged.put("pagem", pagem);//每页
	        paged.put("pageCnt", pageCnt);//共数页  
	    }
	   // //#endregion  活动 分页
	    return paged;
	}
}
