package com.bizduo.zflow.service.base.impl;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.IQueryDao;
import com.bizduo.zflow.service.base.IBaseService;
@Service
public class BaseService<T, PK extends Serializable> implements IBaseService<T, PK> {

	@Autowired
	public IQueryDao queryDao;
	@Autowired
	public JdbcTemplate jdbcTemplate;

	@SuppressWarnings("unchecked")
	public T create(T obj) throws Exception {
		return ((T)queryDao.save(obj));
	}

	@SuppressWarnings("unchecked")
	public T update(T obj) throws Exception {
		return ((T)queryDao.save(obj));
	}

	public void delete(Class<?> clazz, PK[] primarykey) throws Exception {
		for (Serializable s : primarykey) {			
			queryDao.remove(clazz, s);
		}
	}

	public void delete(Class<?> clazz, PK primarykey) throws Exception {
		queryDao.remove(clazz, primarykey);
	}

	@SuppressWarnings("unchecked")
	public T findObjByKey(Class<?> clazz, PK primarykey) throws Exception {
		return (T)queryDao.get(clazz, primarykey);
	}

	@SuppressWarnings("unchecked")
	public Collection<T> findAll(Class<?> clazz) {
		return queryDao.getAll(clazz);
	}

	public Collection<T> findByParam(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

//	public List<Map<String, Object>> callShellProcedure(final String param) {
//		// TODO Auto-generated method stub
//		List<Map<String, Object>> resultlist = (List<Map<String, Object>>) jdbcTemplate.execute(new CallableStatementCreator(){   
//				public CallableStatement createCallableStatement(
//						Connection con) throws SQLException { 
//	           String storedProc = "{call usp_prg_CallShellProcedure(?)}";// 调用的sql   
//	           CallableStatement cs = con.prepareCall(storedProc);   
//	           cs.setString(1, param);// 设置输入参数的值   
//	           return cs;   
//	        }
//	     }, new CallableStatementCallback<List<Map<String, Object>>>(){   
//	         public List<Map<String, Object>> doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException{   
//	           cs.execute(); 
//	           List<Map<String, Object>> resultlist = null;
//	           ResultSet resultset = cs.getResultSet();
//	           resultlist = getResultSet(resultset);
//	           JdbcUtils.closeResultSet(resultset);
//
//	           int i = 2;
//	           boolean moreresults = false;
//	           int updatecount = -1;
//	           do{
//	        	   moreresults = cs.getMoreResults();
//	        	   updatecount = cs.getUpdateCount();
//	        	   if(moreresults){
//	        		   System.out.println("getMoreResults:"+i);
//			           resultset = cs.getResultSet();
//			           resultlist = getResultSet(resultset);
//			           JdbcUtils.closeResultSet(resultset);  
//			           i++;
//	        	   }
////				   if(cs.getUpdateCount() != -1) continue;
//	           }while(moreresults || updatecount != -1);
//	           		return resultlist ;// 获取输出参数的值   
//	         }   
//	  });
//		return resultlist;
//	}
	public List<Map<String, Object>> callShellProcedure(Connection conn ,final String param) throws SQLException{
		//创建存储过程的对象  
		CallableStatement cs=conn.prepareCall("{call USP_PRG_CallShellProcedure(?)}");// 调用存储过程名
		cs.setString(1, param);// 设置输入参数的值
		cs.execute();   
		//得到存储过程的输出参数值  
		//System.out.println (c.getInt(2));  
		ResultSet rs = cs.getResultSet();
		//result 封装结果集
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
        try {  
        	if(null != rs){
        		ResultSetMetaData data = rs.getMetaData();  
        		if(null != data){
        			//每循环一次遍历出来1条记录，记录对应的所有列值存放在map中(columnName:columnValue)  
        			while(rs.next()){  
        				Map<String, Object> map = new HashMap<String, Object>(); 
        				for(int i = 0; i < data.getColumnCount(); i++)  
        					//以键值对存放数据
        					//直接存放字段名称
        					//map.put(data.getColumnName(i + 1), rs.getObject(i + 1));
        					//直接存放字段的别名		        					
        					map.put(data.getColumnLabel(i + 1), rs.getObject(i + 1));  
        				result.add(map);  
        			}  
        		}
        	}
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
		JdbcUtils.closeResultSet(rs);
		return result;
	}
	
	public List<Map<String, Object>> callShellProcedure(final String param){
		List<Map<String, Object>> list = (List<Map<String, Object>>) jdbcTemplate.execute(new CallableStatementCreator(){   
			public CallableStatement createCallableStatement(Connection con) throws SQLException { 
				String storedProc = "{call USP_PRG_CallShellProcedure(?)}";// 调用存储过程名   
				CallableStatement cs = con.prepareCall(storedProc);   
				cs.setString(1, param);// 设置输入参数的值   
				return cs;   
			}
		}, new CallableStatementCallback<List<Map<String, Object>>>(){   
			public List<Map<String, Object>> doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException{   
				cs.execute(); 
				ResultSet rs = cs.getResultSet();
				//result 封装结果集
				List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();  
		        try {  
		        	if(null != rs){
		        		ResultSetMetaData data = rs.getMetaData();  
		        		if(null != data){
		        			//每循环一次遍历出来1条记录，记录对应的所有列值存放在map中(columnName:columnValue)  
		        			while(rs.next()){  
		        				Map<String, Object> map = new HashMap<String, Object>(); 
		        				for(int i = 0; i < data.getColumnCount(); i++)  
		        					//以键值对存放数据
		        					//直接存放字段名称
		        					//map.put(data.getColumnName(i + 1), rs.getObject(i + 1));
		        					//直接存放字段的别名		        					
		        					map.put(data.getColumnLabel(i + 1), rs.getObject(i + 1));  
		        				result.add(map);  
		        			}  
		        		}
		        	}
		        } catch (SQLException e) {  
		            e.printStackTrace();  
		        }  
				JdbcUtils.closeResultSet(rs);
				return result;
			}
		});
		return list;
	}
	
	  /** 
     * 方法功能说明：将分页取出的结果集ResultSet对象组装成 List<--Map<--(columnName:columnValue), 
     * 每一个map对应一条记录，map长度 == column数量 
     * 创建：2012-10-16 by hsy  
     * 修改：日期 by 修改者 
     * 修改内容： 
     * @参数： @param rs 
     * @参数： @return     
     * @return Map    
     * @throws 
     */  
	public List<Map<String, Object>> getResultSet(ResultSet rs)throws SQLException{  
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();  
        try {  
        	if(null != rs){
        		ResultSetMetaData rsmd = rs.getMetaData();  
        		if(null != rsmd){
        			//每循环一次遍历出来1条记录，记录对应的所有列值存放在map中(columnName:columnValue)  
        			while(rs.next()){  
        				Map<String, Object> map = new HashMap<String, Object>();   
        				int columnCount = rsmd.getColumnCount();  
        				for(int i = 0; i < columnCount; i++){  
        					String columnName = rsmd.getColumnName(i + 1);  
        					map.put(columnName, rs.getObject(i + 1));  
//        					System.out.println("columnName" + columnName);
//        					System.out.println("columnData" + rs.getObject(i + 1).toString());
        				}  
        				list.add(map);  
        			}  
        		}
        	}
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return list;  
    }
}
