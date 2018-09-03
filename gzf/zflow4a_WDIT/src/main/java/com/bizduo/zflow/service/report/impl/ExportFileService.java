package com.bizduo.zflow.service.report.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bizduo.zflow.domain.exportdata.ExportExcel;
import com.bizduo.zflow.domain.exportdata.ExportProcedure;
import com.bizduo.zflow.service.exportdata.IExportExcelService;
import com.bizduo.zflow.service.exportdata.IExportProcedureService;
import com.bizduo.zflow.service.report.IExcelService;
import com.bizduo.zflow.service.report.IExportFileService;

@Service
public class ExportFileService implements IExportFileService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private IExcelService excelService;
	@Autowired
	private IExportExcelService exportExcelService;
	@Autowired
	private IExportProcedureService exportProcedureService;
	
	@SuppressWarnings({ "unchecked", "rawtypes"})
	public Workbook exportExcel(final String[] param) {
		List<Map<String, List<String[]>>> reportData = new ArrayList<Map<String, List<String[]>>>();
		ExportExcel expexl = exportExcelService.getByName(param[0]);
		if(null != expexl){
			List<ExportProcedure> procedures = exportProcedureService.getByExcelId(expexl.getId());
			if(null != procedures && 0 < procedures.size()){
				for(final ExportProcedure procedure : procedures){
					final String procname = procedure.getName();
					final String colparam = procedure.getParamIndexColumn();
					
					String result = jdbcTemplate.execute(
							new CallableStatementCreator(){   
								public CallableStatement createCallableStatement(Connection con) throws SQLException{ 
									StringBuilder storedProcedure = new StringBuilder();
									storedProcedure.append("{CALL [dbo].[USP_EXP_COLUMN_").append(procname).append("](");
									if(null != colparam && !("").equals(colparam)){
										String[] array = procedure.getParamIndexColumn().split("\\,");
										for(int i = 0; i < array.length; i++)
											storedProcedure.append("?, ");
										storedProcedure.append("?)}");
										CallableStatement cs = con.prepareCall(storedProcedure.toString());   
										for(int i = 0; i < array.length; i++)
											cs.setString((1 + i), param[Integer.parseInt(array[i])]);// 设置输入参数的值   
										cs.registerOutParameter(array.length + 1, Types.VARCHAR);// 注册输出参数的类型  
										cs.execute();
										return cs;
									}else{
										storedProcedure.append("?)}");
										CallableStatement cs = con.prepareCall(storedProcedure.toString());   
										cs.registerOutParameter(1, Types.VARCHAR);// 注册输出参数的类型  
										cs.execute();
										return cs;
									}
								}   
							}, new CallableStatementCallback(){   
								public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
									cs.execute();  
									if(null != colparam && !("").equals(colparam)){
										String[] array = procedure.getParamIndexColumn().split("\\,");
										return cs.getString(1 + array.length);
									}
									return cs.getString(1);// 获取输出参数的值   
								}   
							});  
					
					if(null == result || ("").equals(result.trim()))
						return new XSSFWorkbook();
					
					final String[] columns = result.split("\\,");
					
					List<String[]> values = jdbcTemplate.execute(
							new CallableStatementCreator(){   
								public CallableStatement createCallableStatement(Connection con) throws SQLException{ 
									StringBuilder storedProcedure = new StringBuilder();
									storedProcedure.append("{CALL [dbo].[USP_EXP_VALUE_").append(procedure.getName()).append("]");
									String valparam = procedure.getParamIndexValue();
									if(null != valparam && !("").equals(valparam)){
										storedProcedure.append("(");
										String[] array = procedure.getParamIndexValue().split("\\,");
										for(int i = 0; i < array.length; i++)
											storedProcedure.append((array.length - 1) == i ? "?" : "?, ");
										
										storedProcedure.append(")}");
										CallableStatement cs = con.prepareCall(storedProcedure.toString());   
										
										for(int i = 0; i < array.length; i++)
											cs.setString((1 + i), param[Integer.parseInt(array[i])]);// 设置输入参数的值  
										
										cs.execute();
										return cs;   
									}else{
										storedProcedure.append("}");
										CallableStatement cs = con.prepareCall(storedProcedure.toString());   
										cs.execute();
										return cs;   
									}
								}   
							}, new CallableStatementCallback(){   
								public List<String[]> doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {   
									cs.execute();   
									ResultSet resultSet = cs.getResultSet();// 获取输出参数的值   
									List<String[]> values = new ArrayList<String[]>();
									while(resultSet.next()){ 
										String[] value = new String[columns.length];
										for(int i = 0; i < columns.length; i++){
											value[i] = resultSet.getString(i + 1);
										}
										values.add(value);  
									}
									return values;
								}   
							});
					Map<String, List<String[]>> mc = new HashMap<String, List<String[]>>();
					mc.put(result, values);
					reportData.add(mc);
				}
			}
		}
		return excelService.exportExcel(reportData);
	}

}
