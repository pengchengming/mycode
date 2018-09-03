package com.bizduo.zflow.service.tableData.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.form.Form;
import com.bizduo.zflow.domain.table.ZTable;
import com.bizduo.zflow.domain.tableData.FormDATableDAId;
import com.bizduo.zflow.domain.tableData.TableData;
import com.bizduo.zflow.domain.tabulation.MiddleTable;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.customform.IMiddleTableService;
import com.bizduo.zflow.service.table.IZTableService;
import com.bizduo.zflow.service.tableData.ITableDataService;
import com.bizduo.zflow.status.ZFlowStatus;
import com.bizduo.zflow.util.ExecutionSql;
import com.bizduo.zflow.util.MySQLZDialect;
@Service
public class TableDataService extends BaseService<Object, Long> implements ITableDataService {
	@Autowired
	private IZTableService ztableService; 
	@Autowired
	private  JdbcTemplate jdbcTemplate;
	@Autowired
	private IMiddleTableService middleTableService;
	@Autowired
	private IFormService formService;
	/**
	 * 根据name获得register
	 * 
	 * @param register
	 * @return 2012-4-9
	 * @author lm
	 */
	@SuppressWarnings("unchecked")
	public TableData getTableDataById(Long id) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(TableData.class);
		cri.add(Restrictions.eq("id", id));
		List<TableData> tableDataList = super.queryDao.getByDetachedCriteria(cri);
		if (tableDataList != null && tableDataList.size() > 0)
			return tableDataList.get(0);
		return null;
	}
    /**
     * 获得所有的tableData
     * @return
     * 2012-5-3
     * @author lm
     */
     @SuppressWarnings("unchecked")
	public List<TableData>	getAllTableData(){
    	PageDetachedCriteria cri = PageDetachedCriteria.forClass(TableData.class);
 		cri.add(Restrictions.eq("isDelete", ZFlowStatus.ISDELETE_NO));
 		return super.queryDao.getByDetachedCriteria(cri);
     }	 
     /**
      *根据tableDataId删除TableData 
      * @param tableDataId
      * 2012-5-8
      * @author lm
      */
    public void deleteTableDataByTableDataId(Long tableDataId){
    	if (tableDataId != null) {
    		TableData tableData = getTableDataById(tableDataId);
    		tableData.setIsDelete(ZFlowStatus.ISDELETE_YES);
			super.queryDao.save(tableData);
		}
    }
/**
  * 查询页面数据
  * @return
  * dzt
  */
	public FormDATableDAId selectTableNameData(String tableName, String parameter){
		FormDATableDAId formDATableDAId=new FormDATableDAId();
		try {
			ZTable ztable=this.ztableService.getZTableByName(tableName);
			//查询form对应表的数据//这里要加条件
			String formTablesql= MySQLZDialect.selectData(tableName, parameter);
			System.out.println(formTablesql);
			String jsonStr = ExecutionSql.executionsql(ztable,"query", jdbcTemplate,formTablesql, null);
			JSONObject jsonObj = new JSONObject(jsonStr);
			org.json.JSONArray jsonArray = jsonObj.getJSONArray(tableName);
			jsonObj.remove(tableName);
			jsonObj.put("selectTableData", jsonArray);
			// 根据主表的名称，查询出mddleTable 
			Form form=this.formService.findByFormName(tableName);
			if(form!=null){
				List<MiddleTable> middleTableList= middleTableService.findByFormTableName(form.getId());
				org.json.JSONArray jsonArray1 = new JSONArray();
				if(middleTableList!=null&&middleTableList.size()>0){
					for(int i =0;i< jsonArray.length();i++){
						JSONObject tempObj = jsonArray.getJSONObject(i);
						Long tableDataId = Long.parseLong(tempObj.get("id").toString());//  table的一个标识
						for (MiddleTable middleTable : middleTableList) {
							String  parameterTableData="where "+form.getFormName().trim()+"_id="+tableDataId;
							String middelTableName=middleTable.getMiddleTableName() ;//form.getFormName() + "middle"+ middleTable.getMiddleTableId();//表名
							String middleTableSql=MySQLZDialect.selectData(middelTableName, parameterTableData);//sql语句
							System.out.println(middleTableSql);
							ZTable middleztable=this.ztableService.getZTableByName(middelTableName); //ztable middle
							String middleTableJson = ExecutionSql.executionsql(middleztable,"query", jdbcTemplate,middleTableSql, null);
							System.out.println(middleTableJson);
							JSONObject middleTableJsonObj = new JSONObject(middleTableJson);
							org.json.JSONArray middleTablejsonArray = middleTableJsonObj.getJSONArray(middelTableName);
							middleTableJsonObj.remove(middelTableName);
							tempObj.put(middleTable.getMiddleTableName(), middleTablejsonArray);
						}
						jsonArray1.put(i, tempObj);
					}
				}
				if(jsonArray1!=null&&jsonArray1.length()>0){
					jsonObj.put("selectTableData", jsonArray1);
				}
			}
			System.out.println("json___"+jsonObj.toString());
			formDATableDAId.setRegister(jsonObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formDATableDAId;
	}
	//根据表名和 条件查询 表中的数据
	public JSONArray getTableDataByTableName(ZTable ztable, String parameter) {
		try {
			String formTablesql= MySQLZDialect.selectData(ztable.getTablename(), parameter);
			System.out.println(formTablesql);
			String jsonStr = ExecutionSql.executionsql(ztable,"query", jdbcTemplate,formTablesql, null);
			JSONObject jsonObj = new JSONObject(jsonStr);
			org.json.JSONArray jsonArray = jsonObj.getJSONArray(ztable.getTablename());
			return jsonArray;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 删除 ztable 中对应的数据
	 * @param tableName
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	public Boolean deleteTableData(ZTable ztable, Long id) throws Exception{
		String deleteTableSql=MySQLZDialect.deleteDate(ztable, id);
		System.out.println(deleteTableSql);
		String deleteJson= ExecutionSql.executionsql(ztable, "execution", jdbcTemplate, deleteTableSql, null);
		if(deleteJson!=null&&!deleteJson.trim().equals("")){
			try {
				return Boolean.parseBoolean(deleteJson);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	
	
}
