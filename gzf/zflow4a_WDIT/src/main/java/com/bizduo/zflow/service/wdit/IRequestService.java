package com.bizduo.zflow.service.wdit;

import java.sql.SQLException;
import java.text.ParseException;

import org.json.JSONObject;

public interface IRequestService {
	public JSONObject getDataById(String tableName,Long dataId,String condition);
	
	
	/**
	 * 获取内网的表信息,该函数用于内外网数据同步测试用
	 * @param jdbcUrl
	 * @param user
	 * @param password
	 * @param tableName
	 * @param condition
	 * @return
	 */
	public JSONObject  getDataByIdSyn(String jdbcUrl,String user,String password,String tableName,String condition);
	
	
	
	
	
	
	
	
	/**
	 * 获取外网的请求数据 按日期
	 * @param importDateLong
	 * @return
	 * @throws ParseException
	 */
	public JSONObject getInfoBy(Long importDateLong ,Integer type) throws Exception;
	/**
	 * 获取外网的照片 按日期
	 * @param nowDate
	 * @return
	 */
	public JSONObject getImageRequest(String synImagePath,Long importDateLong);
	/**
	 * 内网的同步数据
	 * @param jdbcUrl
	 * @param databaseUsername
	 * @param databasePassword
	 * @param condition
	 * @return
	 * @throws ParseException
	 * @throws SQLException
	 */
	public JSONObject getInfoBySyn(String jdbcUrl,String databaseUsername,String databasePassword,String  condition) throws ParseException, SQLException;
	
}
