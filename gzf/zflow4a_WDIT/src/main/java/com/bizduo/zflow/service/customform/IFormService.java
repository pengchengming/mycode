package com.bizduo.zflow.service.customform;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.zflow.domain.form.Form;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.domain.tableData.FormDATableDAId;
import com.bizduo.zflow.domain.tableData.TableDataId;
import com.bizduo.zflow.domain.tabulation.MiddleTable;
import com.bizduo.zflow.service.base.IBaseService;

public interface IFormService extends IBaseService<Object, Long>  {
	/**
	 * 根据code查询
	 */
	public Form getFormByCode(String code);
	/**
	 * 获得所有Form
	 */
	public List<Form> getAllForm();

	/**
	 * 创建表
	 * @param form
	 */
	public void updateTabelConfig(Form form) throws Exception;
	
	/**保存数据
	 * 
	 * @param form
	 * @param jsonObj
	 */
	public Long saveFormData(Long id, JSONObject jsonObj)throws Exception;

	/**修改数据 
	 * @param form
	 * @param jsonObj
	 */
	public void updateFormData(Long id, JSONObject jsonObj)throws Exception;
	/**
	 * 保存属性字段 form和middleTable
	 * @param tempJsonObj
	 * @param form
	 */
	public void saveFormProperty(JSONObject tempJsonObj, Long id,HttpServletRequest request) throws Exception;
	/**
	 * 获取form对应表的数据 ,和middleTable对应的表的标识
	 * @param form
	 * @return
	 */
	public FormDATableDAId  getFormDATableDAIdByFormId(Form form);
	/**
	 * 获取单个middleTable对应的表的数据
	 * @return
	 */
	public TableDataId getTableDataIdByMiddel(MiddleTable middleTable, Form form)throws Exception;
	/**
	 * 表单显示拼接json  :返回json
	 * @return
	 */
	public String formPropertyAndTableJson(Form form );
	
	/**
	 * 根据id删除form
	 * 
	 * @param form
	 * @return 2012-4-9
	 * @author lm
	 */
	public void deleteForm(Long formId);
	/**
	 * 根据数据库的名称查询出Form
	 * @param tableName
	 * @return
	 */
	public Form findByFormName(String tableName);
	/**
	 * 修改form的TaskId
	 * @param taskId
	 * @param participationId
	 */
	public void updateFormDataTask(Long formId,String zform,Long dataId,String taskId, String participationId)throws Exception;
	/**
	 * 修改状态
	 * @param zform
	 * @param dataId
	 * @param statusCode
	 */
	public void updateFormStatus(String zform, Long dataId, String dicTypeCode,String dicCode,String dicValCode,User currentUser)throws Exception;
	/**
	 * 获取发布的form
	 * @param b
	 * @return
	 */
	public List<Form> getFormPush(boolean b);
	
	public List<Form> getFormList(Map<String, Object>  map,PageTrace pageTrace);
	
	public  List<Form>  findSubsetForm(Long fromId);
	public void updateBySql(String sql);
	/**
	 * 保存一个对象的json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public Long saveFormDataJson(String json) throws Exception;
	/**
	 * 保存多个对象的json
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public JSONObject saveFormDataJson(String[] json) throws Exception;
	public void deleteDataById(Long fid, Long  dataId) throws Exception;
	/**
	 * 保存    数据回滚 接口
	 * @param jsonString
	 * @param stmt
	 * @return
	 * @throws Exception
	 */
	
	public Long saveFormDataJson(String jsonString,Statement stmt,ResultSet resultSet) throws Exception;
	public JSONObject deleteFormDataJson(String json)throws Exception;
	public JSONObject deleteFormDataJsons(String[] jsons) throws Exception;
	/**
	 * 
	 * @param param
	 */
	public void saveTestDataJson(String param)throws SQLException;
	/**
	 * 保存json对象和对应的存储过程
	 * @param json 保存的Json
	 * @param calls 执行的存储过程
	 * @param isCurrentId 是否用到当前返回的Id 如果用到就放在Id最后
	 * @return
	 */
	public Long saveFormDataJsonAndCalls(String json, List<String> callsParam)throws Exception;
	/**
	 * 保存json数组和对应的存储过程
	 * @param json 保存的Json
	 * @param calls 执行的存储过程
	 * @param isCurrentId 是否用到当前返回的Id 如果用到就放在Id最后
	 * @return
	 */
	public JSONObject saveFormDataJsonAndCalls(String[] jsons, List<String> callsParam) throws Exception;
	
	public Long saveFormDataJson(String jsonString,Statement stmt,ResultSet resultSet,Connection connection, List<String> callsParam) throws Exception;
	/**
	 * 根据formId查询当前tableDate
	 * @param map
	 * @param parameter
	 * @param pageTrace
	 * @return
	 */
	public JSONObject getDataByFormId(Map<String,String> map,String parameter,PageTrace pageTrace,String byIdconfig ,int type);
	/**
	 * 
	 */
	public Long saveFormDataJsonSyn(String jdbcUrl,String databaseUsername,String databasePassword,String jsonString) throws Exception;
	public JSONObject saveFormDataJsonSyn(String jdbcUrl,String databaseUsername,String databasePassword,String[] jsons) throws Exception;
}
