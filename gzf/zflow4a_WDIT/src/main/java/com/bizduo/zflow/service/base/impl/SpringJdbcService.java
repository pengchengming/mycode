package com.bizduo.zflow.service.base.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bizduo.zflow.domain.importdata.ImportColumn;
import com.bizduo.zflow.service.base.ISpringJdbcService;
import com.bizduo.zflow.util.TimeUitl;
@Service
public class SpringJdbcService implements ISpringJdbcService {
	@Autowired
	public JdbcTemplate jdbcTemplate;

	public void saveDataBySql(List<String> sc) {
		for(String sql : sc){
			jdbcTemplate.execute(sql);
		}
	}
	
	public String saveDataBySql(String tablename, List<ImportColumn> cols, Object[][] objs, String sheet, String batchNo){
		StringBuilder sql1 = new StringBuilder();
		StringBuilder sql2 = new StringBuilder();
		StringBuilder retMsg = new StringBuilder();
		
		sql1.append("INSERT INTO ").append(tablename).append("(");
		sql2.append(" VALUES(");
		for(int i = 0; i < cols.size(); i++){
			sql1.append(cols.get(i).getName()).append(",");
			sql2.append("?, ");
		}
		//MSSQL
//		if(null != dateMonth && !("").equals(dateMonth)){
//			sql1.append("[DateMonth],[ValidTag],[ImportBy],[ImportDate],[ExcelRowIndex],[BatchNo]").append(")");
//			sql2.append("?, ?, ?, ?, ?, ?)");
//		}else{
//			sql1.append("[ValidTag],[ImportBy],[ImportDate],[ExcelRowIndex],[BatchNo]").append(")");
//			sql2.append("?, ?, ?, ?, ?)");
//		}
		//MYSQL
		if(null != sheet && !("").equals(sheet)){
			sql1.append("`sheetFlag`,`validTag`,`importBy`,`importDate`,`excelRowIndex`,`batchNo`").append(")");
			sql2.append("?, ?, ?, ?, ?, ?)");
		}else{
			sql1.append("`validTag`,`importBy`,`importDate`,`excelRowIndex`,`batchNo`").append(")");
			sql2.append("?, ?, ?, ?, ?)");
		}
		String sql = sql1.append(sql2).toString();
		
		for(int i = 0; i < objs.length; i++){
			try{
				jdbcTemplate.update(sql, objs[i]);
			}catch(Exception e){
				e.printStackTrace();
				retMsg.append("第" + objs[i][objs[i].length-1] + "行发生致命错误:" + e.getMessage());
				return retMsg.toString();
			}
		}
		return "";
	}

	public String getGenerateCode(String name){
		//SQLServer写法
		//String sql = "SELECT CASE WHEN prefix IS NULL THEN '' ELSE prefix END + CAST(value AS VARCHAR) + CASE WHEN suffix IS NULL THEN '' ELSE suffix END FROM sys_generate_code  WHERE name = ?";
		//MySql写法
		String sql = "SELECT CONCAT(CASE  WHEN prefix IS NULL THEN ''  ELSE prefix END, CAST(value AS CHAR), CASE WHEN suffix IS NULL THEN '' ELSE suffix END) FROM sys_generate_code WHERE name = ?";
		String value = (String)jdbcTemplate.queryForObject(sql, new Object[] {name}, java.lang.String.class);		
		sql = "UPDATE Sys_generate_code SET value = value + 1 WHERE name = ?";
		jdbcTemplate.update(sql, new Object[] {name});
		return value;
	}
	
	public void updateDataByProc(String tablename, String batchNo){
		//MSSQL调用存储过程
//		StringBuilder sql = new StringBuilder();
//		sql.append("EXEC USP_Imp_")
//		.append(tablename)
//		.append(" '")
//		.append(batchNo)
//		.append("'");	
//		jdbcTemplate.execute(sql.toString());
		
		//MYSSQL调用存储过程
//		String sql = "{call OYP_TO_NEWYP(?,?)}";
		
		
//		
		if(!tablename.equals("") && tablename.equals("tmp_wdit_request_user")){
			String nowdate="";
			try {
				  nowdate= TimeUitl.getTimeLongTime(new Date().getTime(),"yyyy-MM-dd HH:mm:ss");
			} catch (ParseException e) { 
				e.printStackTrace();
			}
			batchNo = batchNo + "|" + nowdate;
		}
		StringBuilder sql = new StringBuilder();
		sql.append("{call USP_Imp_")
		.append(tablename)
		.append("(?)}");
        jdbcTemplate.update(sql.toString(), batchNo);  
	}
	
}
