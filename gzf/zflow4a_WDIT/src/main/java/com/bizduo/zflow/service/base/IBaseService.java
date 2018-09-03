package com.bizduo.zflow.service.base;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IBaseService<T, PK extends Serializable>{
	
	/**
	 * 增加记录
	 * @author Administrator
	 * @param <T>
	 * @param obj
	 * @return T
	 * @throws Exception
	 */
	T create(T obj) throws Exception;
	
	/**
	 * 修改记录
	 * @author Administrator
	 * @param <T>
	 * @param obj
	 * @return T
	 * @throws Exception
	 */
	T update(T obj) throws Exception;
	
	/**
	 * 删除记录(多条)
	 * @author Administrator
	 * @param clazz
	 * @param primarykey
	 * @throws Exception
	 */
	void delete(Class<?> clazz, PK[] primarykey) throws Exception;
	
	/**
	 * 删除记录
	 * @author Administrator
	 * @param <T>
	 * @param clazz
	 * @param primarykey
	 * @throws Exception
	 */
	void delete(Class<?> clazz, PK primarykey) throws Exception;
	
	/**
	 * 根据主键查询记录
	 * @author Administrator
	 * @param <T>
	 * @param clazz
	 * @param primarykey
	 * @return T
	 * @throws Exception
	 */
	T findObjByKey(Class<?> clazz, PK primarykey) throws Exception;
	
	/**
	 * 查询所有的对应传入Class的记录
	 * @author Administrator
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	Collection<T> findAll(Class<?> clazz);
	
	Collection<T> findByParam(Map<String, Object> paramMap);
	
	public List<Map<String, Object>> callShellProcedure(Connection conn ,final String param) throws SQLException;
	List<Map<String, Object>> callShellProcedure(String param);
	
	List<Map<String, Object>> getResultSet(ResultSet rs)throws SQLException;
}
