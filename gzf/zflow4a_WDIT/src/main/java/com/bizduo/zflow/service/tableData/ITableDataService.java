package com.bizduo.zflow.service.tableData;

import java.util.List;

import org.json.JSONArray;

import com.bizduo.zflow.domain.table.ZTable;
import com.bizduo.zflow.domain.tableData.FormDATableDAId;
import com.bizduo.zflow.domain.tableData.TableData;
import com.bizduo.zflow.service.base.IBaseService;
/**
* @author lm
* @version 创建时间：2012-5-8 上午10:32:54
*/
public interface ITableDataService extends IBaseService<Object, Long> {

	/**
	 * 根据id获得tableData
	 * 
	 * @param code
	 * @return
	 * @author lm
	 */
	public TableData getTableDataById(Long id);
	
	/**
	 * 获得所有的tableData
	 * 
	 * @param code
	 * @return
	 * @author lm
	 */
     public List<TableData> getAllTableData();
      
     /**
      *根据tableDataId删除TableData 
      * @param tableDataId
      * 2012-5-8
      * @author lm
      */
    public void deleteTableDataByTableDataId(Long tableDataId);
    /**
     * 查询页面数据
     * @return
     * dzt
     */
	public FormDATableDAId selectTableNameData(String tableName, String parameter);
	//根据表名和 条件查询 表中的数据
	public JSONArray getTableDataByTableName(ZTable ztable, String parameter);
	/**
	 * 删除 ztable 中对应的数据
	 * @param tableName
	 * @param id
	 * @return
	 */
	public Boolean deleteTableData(ZTable ztable, Long id) throws Exception;
}
