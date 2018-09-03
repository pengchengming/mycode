package com.bizduo.zflow.service.customform.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.common.module.dao.impl.hibernate.PageDetachedCriteria;
import com.bizduo.zflow.domain.bizType.BizValue;
import com.bizduo.zflow.domain.bizType.DataDictionaryValue;
import com.bizduo.zflow.domain.form.Form;
import com.bizduo.zflow.domain.form.FormProperty;
import com.bizduo.zflow.domain.form.SelectConditions;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.domain.table.ZColumn;
import com.bizduo.zflow.domain.table.ZColumn.ColType;
import com.bizduo.zflow.domain.table.ZTable;
import com.bizduo.zflow.domain.tableData.FormDATableDAId;
import com.bizduo.zflow.domain.tableData.TableDataId;
import com.bizduo.zflow.domain.tableData.ZCloumnData;
import com.bizduo.zflow.domain.tabulation.MiddleTable;
import com.bizduo.zflow.domain.tabulation.TableProperty;
import com.bizduo.zflow.service.base.ISpringJdbcService;
import com.bizduo.zflow.service.base.impl.BaseService;
import com.bizduo.zflow.service.bizType.IBizTypeService;
import com.bizduo.zflow.service.bizType.IDataDictionaryService;
import com.bizduo.zflow.service.customform.IFormService;
import com.bizduo.zflow.service.customform.IMiddleTableService;
import com.bizduo.zflow.service.table.IZTableService;
import com.bizduo.zflow.status.ZFlowStatus;
import com.bizduo.zflow.util.ExecutionSql;
import com.bizduo.zflow.util.FileUtil;
import com.bizduo.zflow.util.JsonToObjectUtil;
import com.bizduo.zflow.util.MySQLZDialect;
import com.bizduo.zflow.util.SaveFormproperty;
import com.bizduo.zflow.util.SelectToJson;
import com.bizduo.zflow.util.TimeUitl;
import com.bizduo.zflow.util.UserUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;
@Service
public class FormService extends BaseService<Object, Long> implements IFormService {
	@Autowired
	private ISpringJdbcService springJdbcService;
	@Autowired
	private  JdbcTemplate jdbcTemplate;
//	@Autowired
//	private SessionFactoryImpl sessionFactory;
//	@Autowired
//	private IBizTypeService bizTypeService;
	@Autowired
	private IMiddleTableService middleTableService;
//	@Autowired
	//private IJbpmService jbpmService; 
	@Autowired
	private ComboPooledDataSource dataSource;
	@Autowired
	private IBizTypeService  bizTypeService;
 
	/**
	 * 根据code查询
	 */
	@SuppressWarnings("unchecked")
	public Form getFormByCode(String code) {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Form.class);
		if(null != code)
			cri.add(Restrictions.eq("formName", code));
		List<Form> formList= super.queryDao.getByDetachedCriteria(cri);
		if(formList!=null&&formList.size()==1)
			return formList.get(0);
		return null;
	}

	@Autowired
	private IZTableService ztableService;
	@Autowired
	private IDataDictionaryService dataDictionaryService;

	@SuppressWarnings("unused")
	private void saveFileJsp(Form form,String center,HttpServletRequest request){
		System.out.println(request.getSession().getServletContext().getRealPath("/"));
		
		String path= request.getSession().getServletContext().getRealPath("/")+"page/jsp/template/"+form.getFormName()+".jsp";
		FileUtil.saveFile(path, center);
	}
	//保存属性字段form和middleTable
	public void saveFormProperty(JSONObject jsonObj, Long id,HttpServletRequest request) throws Exception {
		Form form = (Form) super.findObjByKey(Form.class, id);
		//保存jsp模版
		/*String ISTEMPLATE = jsonObj.getString(ZFlowStatus.ISTEMPLATE); 
		if(ISTEMPLATE.equals("1")){
			String formhtml = jsonObj.getString(ZFlowStatus.TEMPLATE);
			saveFileJsp(form, formhtml,request);
		} */
		//1.将整个JSON拆分为Form中各个属性对应的JSON字符串
		String html = jsonObj.getString(ZFlowStatus.FORMHTML);
		form.setFormHtml(html);//2.保存formHtml
		//3.将拆分后的JSON字符串转为JSON对象
		org.json.JSONArray props = jsonObj.getJSONArray(ZFlowStatus.PROPERTY);
		org.json.JSONArray tables = jsonObj.getJSONArray(ZFlowStatus.TABLELIST);
		List<FormProperty> formPropertyList = SaveFormproperty.getformPropertyListByJson(form, props);//4.得到form属性集合(前台JSON转换的对象)
		List<MiddleTable> middleTables = SaveFormproperty.getMiddleTableByJson(form,tables);//5.得到form中列表的集合(列表控件)(前台JSON转换的对象)
		//List<FormProperty> formProps = SaveFormproperty.getUpdateFormPropertyList(form, formPropertyList);// 创建修改FormProperty对象
		//6.删除该form之前存在的Property(集合)
		if(form.getPropertyList()!=null){
			Long[] ids= SaveFormproperty.formpropertyIds(form.getPropertyList()) ;
			this.delete(FormProperty.class, ids);
		}
		form.setPropertyList(formPropertyList);
		// 保存对象
		form = (Form) super.update(form);
		//删除冗余的MiddleTable
		List<MiddleTable> mtObjs = (List<MiddleTable>) this.middleTableService.findByFormId(form.getId());
		if(mtObjs!=null&&mtObjs.size()>0){
			Long[] ids= getMiddleTableIds(mtObjs);
			this.delete(MiddleTable.class, ids);
		}
		//deleteMiddleTable(middleTables, mtObjs);
		//修改保存middleTable
		//updateMiddleTableList(middleTables, form);
		middleTableService.saveMiddleTables(middleTables);
	}
	/**
	 * 获取middletable的id集合
	 * @param mtObjs
	 * @return
	 */
	private Long[] getMiddleTableIds(List<MiddleTable> middleTableList) {
		List<Long> idsList=new ArrayList<Long>();
		for(MiddleTable middleTable: middleTableList)
			idsList.add(middleTable.getId());
		//Long[] idsLong=(Long[])idsList.toArray();//new Long[idsList.size()];
		Long[] idsLong=new Long[idsList.size()];
		for (int i = 0; i < idsList.size(); i++) 
			idsLong[i]=idsList.get(i);
		return idsLong;
	}
	/**
	 * 表单显示拼接json :返回json
	 * 
	 * @return
	 */
	public String formPropertyAndTableJson(Form form ){
		//1.new一个JSON对象封装数据
		JSONObject jsonObj= new JSONObject();
		try {
//			Form form  = (Form) super.findObjByKey(Form.class, id);
			//2.new一个formObj(对应Form的JSON对象)
			JSONObject formObj = new JSONObject();
			formObj.put("formId", form.getId());
			formObj.put("formHtml", form.getFormHtml());
			formObj.put("formName", form.getFormName());
			//3.new一个JSONArray对象存放该Form对应的属性集合(propertys)
			JSONArray props = new JSONArray();
			//4.循环当前Form对象中的属性集合
			for (int i = 0; i < form.getPropertyList().size(); i++) {
				FormProperty prop =  form.getPropertyList().get(i);
				//5.将FormProperty对象转换为JSONObj
				JSONObject propObj= JsonToObjectUtil.toJSON(prop);
				//6.判断FormProperty的属性,不为空就设置到当前FormProperty对应的JSON对象中
				if(null != prop.getExtraAttributes()&&!prop.getExtraAttributes().trim().equals("")){
					JSONObject attrs = new JSONObject(prop.getExtraAttributes());
					propObj.remove("extraAttributes");
					propObj.put("extraAttributes", attrs);
				}
				//7.判断当前对象是否绑定了数据字典中的数据,
				//如果绑定了就设置到当前FormProperty对应的JSON对象中
				//该控件如果绑定了值,在2012年7月14日 10:14:30 这个时间段中
				//该控件就是 checkbox radio select 三种中的一种
				if(null != prop.getDictionaryCode() && !(("").equals(prop.getDictionaryCode().trim()))){
					//8.将对应的数据字典的数据查询出来封装为JSON对象,存放到JSON数组中
					JSONArray options = optionArrayJson(prop);
					propObj.put("option", options);
				}
				//9.将对应当前FormProperty的JSON对象put到Form属性集合中
				props.put(i, propObj);
			}
			//10.将form属性集合put到formObj中
			formObj.put("formfields", props); 
			//11.查询当前Form中对应的MiddleTable对象
			List<MiddleTable> mts = (List<MiddleTable>) this.middleTableService.findByFormId(form.getId());
			//12. 一个JSONArray对象封装MiddleTable对象json集合
			JSONArray tableObjs = getMiddleTableJson(mts);  //new JSONArray();
			//22.将MiddleTable集合put到封装Form的对象中
			formObj.put("tablefields", tableObjs);
			//23.将formObj put 到整个大的JSON对象中
			jsonObj.put("configField", formObj);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return jsonObj.toString();
	}
	/**
	 * 对一个form下的middle生成json集合
	 * @param mts
	 * @return
	 * @throws JSONException 
	 */
	private JSONArray getMiddleTableJson(List<MiddleTable> mts) throws JSONException {
		JSONArray tableObjs = new JSONArray();
		if(null != mts && 0 < mts.size()){
			for (int i = 0; i < mts.size(); i++) {
				MiddleTable mt = mts.get(i);
				//13.new 一个JSON对象封装MiddleTable对象
				JSONObject mtObj = new JSONObject();
				mtObj.put("id", mt.getId());
				mtObj.put("middleTableId", mt.getMiddleTableId());
				mtObj.put("middleTableName", mt.getMiddleTableName());
				//14.new 一个JSONArray对象封装MiddleTable对象中属性的集合
				JSONArray tableProps = new JSONArray();
				for (int j = 0; j < mt.getTablePropertyList().size(); j++) {
					TableProperty tableProperty = mt.getTablePropertyList().get(j);
					//15.将TableProperty对象转换为JSON对象
					JSONObject propObj =  JsonToObjectUtil.toJSON(tableProperty);
					//16.如果TableProperty中控件的属性不为空,将其put到当前TableProperty对应的JSON对象中
					if(null != tableProperty.getExtraAttributes()&&!tableProperty.getExtraAttributes().trim().equals("")){
						JSONObject attrs = new JSONObject(tableProperty.getExtraAttributes());
						propObj.remove("extraAttributes");
						propObj.put("extraAttributes", attrs);
					}
					///17.判断当前对象是否绑定了数据字典中的数据,
					//如果绑定了就设置到当前TableProperty对应的JSON对象中
					//该控件如果绑定了值,在2012年7月14日 10:14:30 这个时间段中
					//该控件就是 checkbox radio select 三种中的一种
					if(null != tableProperty.getDictionaryCode() &&
							!(("").equals(tableProperty.getDictionaryCode().trim()))){
						//18.将对应的数据字典的数据查询出来封装为JSON对象,存放到JSON数组中
						JSONArray options = optionArrayJsonInTable(tableProperty.getDictionaryCode());
						propObj.put("option", options);
					}
					//19.将对应当前TableProperty的JSON对象put到MiddleTable属性集合中
					tableProps.put(j, propObj);
				}
				//20.将对应当前TableProperty的JSON数组put到MiddleTable对应的tablePropertyList属性中
				mtObj.put("tablePropertyFields", tableProps);
				//21.将MiddleTable对应的JSON对象put到上面封装MiddleTable的集合中 (116 line)
				tableObjs.put(i, mtObj);
			}
		}
		return tableObjs;
	}
	/**
	 * 选项的集合
	 * @param dictionaryCode
	 * @return
	 */
	private JSONArray optionArrayJson(FormProperty prop){
		JSONArray optionArray = new JSONArray();//选项的集合
		int k = 0;
		try {
			if(("select").equals(prop.getElementType())){
				JSONObject defaultOption = new JSONObject();
				defaultOption.put("id", 0L);
				defaultOption.put("name", "请选择");
				optionArray.put(k++, defaultOption);
			}
			
			List<DataDictionaryValue> dataDictionaryValueList = dataDictionaryService.getDataDictionaryValueByCode(prop.getDictionaryCode());
			
			if(dataDictionaryValueList != null && dataDictionaryValueList.size() > 0){
				for (int i=0; i < dataDictionaryValueList.size(); i++) {
					DataDictionaryValue dataDictionaryValue = dataDictionaryValueList.get(i);
					JSONObject optionJson = new JSONObject();
					optionJson.put("id", dataDictionaryValue.getId());
					optionJson.put("name", dataDictionaryValue.getDisplayValue());
					optionArray.put(k++, optionJson);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return optionArray;
	}
	
	private JSONArray optionArrayJsonInTable(String code){
		JSONArray optionArray = new JSONArray();//选项的集合
		try {
			List<DataDictionaryValue> dataDictionaryValueList = dataDictionaryService.getDataDictionaryValueByCode(code);
			
			if(dataDictionaryValueList != null && dataDictionaryValueList.size()>0){
				for (int k = 0; k < dataDictionaryValueList.size(); k++) {
					DataDictionaryValue dataDictionaryValue= dataDictionaryValueList.get(k);
					JSONObject optionJson = new JSONObject();
					optionJson.put("id", dataDictionaryValue.getId());
					optionJson.put("name", dataDictionaryValue.getDisplayValue());
					optionArray.put(k, optionJson);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return optionArray;
	}
	/**
	 * 保存修改后的MiddleTable(该方法判断同一ID的Form修改的时候,对应的MiddleTable有没有做相应的修改)
	 * @param mtbs MiddleTable的JSON对象集合
	 * @param form
	 * @throws Exception
	 */
//	private void updateMiddleTableList(List<MiddleTable> mtbs, Form form) throws Exception {
//		 //方法属性命名说明
//		 //*Obj : 代表实体对象
//		 //*JsonObj : 代表JSON对象
//		if(mtbs != null && mtbs.size() > 0) {
//			//1.遍历MiddleTable的JSON对象集合
//			for(MiddleTable mtJsonObj : mtbs) {
//				//2.根据form.id 和 middleTableId 获取MiddleTable对象
//				MiddleTable mtObj = middleTableService.findByFormIdAndMiddleTableId(form.getId(), mtJsonObj.getMiddleTableId());
//				3.修改mtObj
//				if(mtObj != null) {
//					//4.创建ArrayList保存需要序列化的TableProperty对象
//					List<TableProperty> tableProps = new ArrayList<TableProperty>();
//					
//					if(null != mtObj.getTablePropertyList() && 0 < mtObj.getTablePropertyList().size()) {
//						//5.原始的TableProperty对象和前台转换的对应JSONObj对比
//						//  先遍历JSONObj中的tableProp,再遍历原始中的tableProp
//						//  用map的键存放id,不会出现重复
//						Map<Long, Long> ids = new HashMap<Long, Long>();
//						for(TableProperty tablePropJson : mtJsonObj.getTablePropertyList()) {
//							TableProperty temp = new TableProperty();
//							for(TableProperty tableProp : mtObj.getTablePropertyList()) {
//								//6.将该mtObj中所有tableProp的id放到map
//								ids.put(tableProp.getId(), tableProp.getId());
//								//7.对比id,如果id相等则将ID Set到temp
//								if (tablePropJson.getTablePropertyId().equals(tableProp.getTablePropertyId())){ 
//									temp.setId(tableProp.getId());
//									break;
//								}
//							}
//							//8.其他属性以前台JSONObj为准
//							temp.setTablePropertyId(tablePropJson.getTablePropertyId());
//							temp.setTablePropertyLabel(tablePropJson.getTablePropertyLabel());
//							temp.setTablePropertyLength(tablePropJson.getTablePropertyLength());
//							temp.setTablePropertyName(tablePropJson.getTablePropertyName());
//							temp.setTablePropertyType(tablePropJson.getTablePropertyType());
//							temp.setDictionaryCode(tablePropJson.getDictionaryCode());
//							temp.setElementType(tablePropJson.getElementType());
//							temp.setExtraAttributes(tablePropJson.getExtraAttributes());
//							temp.setForeignKey(tablePropJson.getForeignKey());
//							
//							tableProps.add(temp);
//						}
//						//9.删除所有的之前存在的tableProp
//						this.middleTableService.delete(TableProperty.class, (Long[])ids.keySet().toArray());
//						//10.重新设置tablePropertyList
//						mtObj.setTablePropertyList(tableProps);
//						//11.更新mtObj
//						middleTableService.update(mtObj);
//					}else{
//						//12.如果之前的middelTable中,tableProperyList属性为空,则直接新增属性
//						for(TableProperty tableProp : mtJsonObj.getTablePropertyList()){
//							tableProp.setMiddleTable(mtObj);
//						}
//						//13.set tablePropertyList属性
//						mtObj.setTablePropertyList(mtJsonObj.getTablePropertyList());
//						//14.更新mtObj
//						middleTableService.update(mtObj);
//					}
//				} else {
//					//15.直接保存mtJsonObj
//					mtJsonObj.setForm(form);
//					middleTableService.create(mtJsonObj);
//				}
//			}
//		}
//	}

	// 删除多余数据(以mtJsonObjs为基准,如果mtObjs中的MiddleTable不存在于mtJsonObjs中,则表示是多余的数据)
//	private void deleteMiddleTable(List<MiddleTable> mtJsonObjs, List<MiddleTable> mtObjs) throws Exception {
//		if(null != mtObjs && 0 < mtObjs.size() && null != mtJsonObjs && 0 < mtJsonObjs.size()) {
//			//1.遍历数据库中的对象
//			for(MiddleTable mtObj : mtObjs) {
//				boolean isdelete = false;
//				//2.遍历JsonObjs中的数据
//				for(MiddleTable mtJsonObj : mtJsonObjs) {
//					//3.用middleTableId做判断
//					if(mtObj.getMiddleTableId().intValue() == mtJsonObj.getMiddleTableId().intValue()) {
//						isdelete = true;
//						break;
//					}
//				}
//				//4.如果json中不存在，说明数据库中的数据是多余的应删除
//				if(!isdelete) {
//					middleTableService.delete(MiddleTable.class,mtObj.getId());
//				}
//			}
//		}
//	}

	/**
	 * 对Form进行软删除
	 */
	public void deleteForm(Long formId) {
		if (formId != null) {
			Form form = (Form) super.queryDao.get(Form.class, formId);
			form.setIsDelete(ZFlowStatus.ISDELETE_YES);
			super.queryDao.save(form);
		}
	}

	/**
	 * 获得所有form(已经软删除的除外)
	 */
	@SuppressWarnings("unchecked")
	public List<Form> getAllForm() {
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Form.class);
		cri.add(Restrictions.eq("isDelete", ZFlowStatus.ISDELETE_NO));
		cri.addOrder(Order.asc("id"));
		return super.queryDao.getByDetachedCriteria(cri);
	}

	/**
	 * 创建数据库表,将该Form设置为发布状态
	 */
	public void updateTabelConfig(Form form) throws Exception { 
			if(form.getZtable()!=null&&form.getZtable().getId()!=null){
				try {
					Long formId= form.getId();
					Long tableId=form.getZtable().getId();
					String updateForm= "update  zflow_form set ztable_id=null where id='"+formId+"'";
					ExecutionSql.executionsql(null, "execution", jdbcTemplate, updateForm, null);
					
					String updateFormProperty="update  zflow_form_property set zcolumn_id=null where form_id='"+formId+"' ";
					ExecutionSql.executionsql(null, "execution", jdbcTemplate, updateFormProperty, null);  
					//子表
					List<MiddleTable> middleTables = (List<MiddleTable>) middleTableService.findByFormId(form.getId());
					//2.遍历 
					if(middleTables!=null&&middleTables.size()>0){
						String updatemiddletable= "update  zflow_middle_table set ztable_id=null where form_id='"+formId+"'";
						ExecutionSql.executionsql(null, "execution", jdbcTemplate, updatemiddletable, null);
						for (MiddleTable mtObj : middleTables) {
							String updatetableProperty="update  zflow_table_property  set zcolumn_id=null where middle_table_id='"+mtObj+"' "; 
							ExecutionSql.executionsql(null, "execution", jdbcTemplate, updatetableProperty, null);
							
							
							ZTable table= ztableService.getZTableByName(mtObj.getMiddleTableName());
							Long mtTableId= table.getId();
							String deleteMtTable= "delete from zsql_table where id='"+mtTableId+"'";
							ExecutionSql.executionsql(null, "execution", jdbcTemplate, deleteMtTable, null);
							
							String deleteMtTableColumn= "delete from zsql_column where zql_table_id='"+mtTableId+"'";
							ExecutionSql.executionsql(null, "execution", jdbcTemplate, deleteMtTableColumn, null);
							
							String dropTable= "drop table "+mtObj.getMiddleTableName(); 
							ExecutionSql.executionsql(null, "execution", jdbcTemplate, dropTable, null);
						}	
					}
					//删除form表的数据
					String deleteFormTable="delete from zsql_table where id='"+tableId+"'";
					ExecutionSql.executionsql(null, "execution", jdbcTemplate, deleteFormTable, null);
					String deleteFormTableColumn="delete from zsql_column where zql_table_id='"+tableId+"' ";
					ExecutionSql.executionsql(null, "execution", jdbcTemplate, deleteFormTableColumn, null);
					
					String dropformTable= "drop table "+form.getFormName();
					ExecutionSql.executionsql(null, "execution", jdbcTemplate, dropformTable, null);
					if(form.getIshistoryTable()!=null&&form.getIshistoryTable()==1){
						//历史表ID 
						String tableName= form.getFormName()+"_history";
						String dropformTable_history= "drop table "+tableName;
						ExecutionSql.executionsql(null, "execution", jdbcTemplate, dropformTable_history, null);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			//循环创建表
			for (ZTable zTable : this.saveZTableList(form)) {
				//拼接SQL
				String createTableSql = MySQLZDialect.createTable(zTable.getTablename(), zTable.getZcolumns());
				System.out.println(createTableSql);
				//执行SQL创建表
				ExecutionSql.executionsql(null, "execution", jdbcTemplate, createTableSql, null);
				 
				if(form.getIshistoryTable()!=null&&form.getIshistoryTable()==1){
					//历史表ID 
					String tableName= zTable.getTablename()+"_history";
					//zTableHistory.setTablename(tableName);
					Collection<ZColumn> zcolumns= zTable.getZcolumns(); 
					ZColumn history = new ZColumn();
					//history = (ZColumn)this.ztableService.create(history);
					history.setColName("historyId");
					history.setColType(ColType.VARCHAR);
					history.setLength(50l);
					history.setPrimaryKey(false);
					history.setComment("historyId");					
					zcolumns.add(history);
					//zTableHistory.setZcolumns(zcolumns);

					String createTableHistorySql = MySQLZDialect.createTable(tableName,zcolumns);
					System.out.println(createTableHistorySql);
					//执行SQL创建表
					ExecutionSql.executionsql(null, "execution", jdbcTemplate, createTableHistorySql, null);
				} 
			}
			//设置form的状态,更新
			form.setIsPublish(true);
			super.update(form);
	}

	//将form封装为ZTable对象,和其关联的MiddleTable也封装
	private List<ZTable> saveZTableList(Form form) throws Exception {
		//1. tables 存放所有封装为ZTable的对象
		List<ZTable> tables = new ArrayList<ZTable>();
		if (form != null) {
			List<FormProperty> propertyList = new ArrayList<FormProperty>();//2.存放Form中属性的集合
			ZTable formZtable = new ZTable();//3.封装当前Form的ztable
			formZtable.setTablename(form.getFormName());//4.设置表名和说明
			formZtable.setDescription(form.getFormCode());
			formZtable = (ZTable)ztableService.create(formZtable);//5.序列化当前ztable
			ZColumn pk = new ZColumn("id",ColType.BIGINT,20l,true,"主键",formZtable);//6.生成id主键
			pk = (ZColumn)this.ztableService.create(pk);
			formZtable.getZcolumns().add(pk);//7.将pk添加到formZtable中
			/**8.普通字段**/
			for (FormProperty property : form.getPropertyList()) {
				ZColumn zcolumn = new ZColumn();
				zcolumn.setZtable(formZtable);
				if (property.getFieldName() != null)
					zcolumn.setColName(property.getFieldName());
				if (property.getFieldLength() != null)
					zcolumn.setLength(property.getFieldLength().longValue());
				if (property.getComment() != null)
					zcolumn.setComment(property.getComment());
				if (property.getFieldType() != null) {
					zcolumn.setColType(getColTypeByPropertyType(property.getFieldType()));
					if (property.getFieldType().equals("attrFloat"))
						zcolumn.setDecimals(0l);
				}
				zcolumn = (ZColumn)this.ztableService.create(zcolumn);
				//7.将生成的列和Form中对应的property关联
				property.setZcolumn(zcolumn);
				propertyList.add(property);
				//8.将普通字段添加到formZtable中
				formZtable.getZcolumns().add(zcolumn);
			}
			//jpbm  实例Id
			ZColumn participation = new ZColumn("participationId", ColType.VARCHAR, 50l, false, "实例Id", formZtable);
			participation = (ZColumn)this.ztableService.create(participation);
			formZtable.getZcolumns().add(participation);
			//task
			ZColumn task = new ZColumn("taskId", ColType.VARCHAR, 50l, false, "taskId", formZtable);
			task = (ZColumn)this.ztableService.create(task);
			formZtable.getZcolumns().add(task);
			//扩展表ID
			ZColumn expand = new ZColumn("expandId", ColType.VARCHAR, 50l, false, "expandId", formZtable);
			expand = (ZColumn)this.ztableService.create(expand);
			formZtable.getZcolumns().add(expand);			 
			//9.创建时间
			ZColumn zcolumn = new ZColumn("createDate", ColType.DATETIME, 0l, false, "创建时间", formZtable);
			zcolumn = (ZColumn)this.ztableService.create(zcolumn);
			formZtable.getZcolumns().add(zcolumn);
			//createBy
			ZColumn createBy = new ZColumn("createBy", ColType.VARCHAR, 50l, false, "createBy", formZtable);
			createBy = (ZColumn)this.ztableService.create(createBy);
			formZtable.getZcolumns().add(createBy); 
			//ModifyBy
			ZColumn ModifyBy = new ZColumn("modifyBy", ColType.VARCHAR, 50l, false, "modifyBy", formZtable);
			ModifyBy = (ZColumn)this.ztableService.create(ModifyBy);
			formZtable.getZcolumns().add(ModifyBy); 
			//ModifyBy
			ZColumn ModifyDate = new ZColumn("modifyDate", ColType.DATETIME, 0l, false, "modifyDate", formZtable);
			ModifyDate = (ZColumn)this.ztableService.create(ModifyDate);
			formZtable.getZcolumns().add(ModifyDate);
			//guid
			ZColumn guid = new ZColumn("guid", ColType.VARCHAR, 50l, false, "guid", formZtable);
			guid = (ZColumn)this.ztableService.create(guid);
			formZtable.getZcolumns().add(guid);
			//10.将当前form封装的ZTable add to tables
			tables.add(formZtable);
			//11.设置form的PropertyList属性
			form.getPropertyList().clear();
			form.getPropertyList().addAll(propertyList);
			form.setZtable(formZtable);
			//12.更新form
			//super.update(form);
			//13.将和当前Form关联的MiddleTable生成的ZTable add to tables
			tables.addAll(middleTableByZTable(form));
		}
		return tables;
	}

	//将和Form关联的MiddleTable封装ZTable
	private List<ZTable> middleTableByZTable(Form form) throws Exception {
		List<ZTable> zTableList = new ArrayList<ZTable>();
		//1.查询出关联当前Form的MiddleTable
		List<MiddleTable> middleTables = (List<MiddleTable>) middleTableService.findByFormId(form.getId());
		//2.遍历
		for (MiddleTable mtObj : middleTables) {
			//3.new 一个ArrayList 存放mtObj中的属性
			List<TableProperty> tableProps = new ArrayList<TableProperty>();
			//4.封装middleTable 的 table
			ZTable table = new ZTable();
			//5.设置表名,序列化该table
			table.setTablename(mtObj.getMiddleTableName()); //form.getFormName() + "middle"+ mtObj.getMiddleTableId()
			table = (ZTable)ztableService.create(table);
			//6.生成 id主键
			ZColumn pk = new ZColumn();
			pk.setColName("id");
			pk.setColType(ColType.BIGINT);
			pk.setLength(20l);
			pk.setPrimaryKey(true);
			pk.setZtable(table);
			pk = (ZColumn)this.ztableService.create(pk);
			//7.add到table的zcolumns属性中
			table.getZcolumns().add(pk);
			//8.普通字段
			for(TableProperty tableProperty : mtObj.getTablePropertyList()) {
				ZColumn zcolumn = new ZColumn();
				if(tableProperty.getTablePropertyName() != null)
					zcolumn.setColName(tableProperty.getTablePropertyName());
				if(tableProperty.getTablePropertyLength() != null)
					zcolumn.setLength(tableProperty.getTablePropertyLength());
				if(tableProperty.getTablePropertyType() != null) {
					ColType coltype = getColTypeByPropertyType(tableProperty.getTablePropertyType());
					zcolumn.setColType(coltype);
					//9.float类型时设置默认值
					if(tableProperty.getTablePropertyType().equals("attrFloat")) 
						zcolumn.setDecimals(0l);
				}
				zcolumn.setZtable(table);
				table.getZcolumns().add(zcolumn);
				//10.序列化
				zcolumn = (ZColumn)this.ztableService.create(zcolumn);
				tableProperty.setZcolumn(zcolumn);
				tableProps.add(tableProperty);
			}
			//11.外键(和当前关联Form之间的外键关联)
			ZColumn zcolumnFormKey = new ZColumn();
			zcolumnFormKey.setColName(form.getFormName()+"_id");
			zcolumnFormKey.setColType(ColType.BIGINT);
			zcolumnFormKey.setLength(20l);
			zcolumnFormKey.setForeignColumn(form.getFormName());/**外键字段**/
			zcolumnFormKey.setForeignKey(form.getFormName()+"_id"+"_foreignkey");/**外键的key**/
			zcolumnFormKey.setZtable(table);
			zcolumnFormKey = (ZColumn)this.ztableService.create(zcolumnFormKey);
			table.getZcolumns().add(zcolumnFormKey);
		 
			//9.创建时间
			ZColumn zcolumn = new ZColumn("createDate", ColType.DATETIME, 0l, false, "创建时间", table);
			zcolumn = (ZColumn)this.ztableService.create(zcolumn);
			table.getZcolumns().add(zcolumn);
			//createBy
			ZColumn createBy = new ZColumn("createBy", ColType.VARCHAR, 50l, false, "createBy", table);
			createBy = (ZColumn)this.ztableService.create(createBy);
			table.getZcolumns().add(createBy); 
			//ModifyBy
			ZColumn ModifyBy = new ZColumn("modifyBy", ColType.VARCHAR, 50l, false, "modifyBy", table);
			ModifyBy = (ZColumn)this.ztableService.create(ModifyBy);
			table.getZcolumns().add(ModifyBy); 
			//ModifyBy
			ZColumn ModifyDate = new ZColumn("modifyDate", ColType.DATETIME, 0l, false, "modifyDate", table);
			ModifyDate = (ZColumn)this.ztableService.create(ModifyDate);
			table.getZcolumns().add(ModifyDate);
			//guid
			ZColumn guid = new ZColumn("guid", ColType.VARCHAR, 50l, false, "guid", table);
			guid = (ZColumn)this.ztableService.create(guid);
			table.getZcolumns().add(guid); 
			
			//12.将当前table add 到集合中
			zTableList.add(table);
			//13.设置mtObj(MiddleTable)的TablePropertyList属性
			mtObj.getTablePropertyList().clear();
			mtObj.getTablePropertyList().addAll(tableProps);
			mtObj.setZtable(table);
			//14.更新mtObj
			this.middleTableService.update(mtObj);
		}
		return zTableList;
	}

	//将页面自定时选择的类型转换为数据库中的类型
	private ColType getColTypeByPropertyType(String properyType) {
		if (properyType.equals("attrString"))
			return ColType.VARCHAR;
		else if (properyType.equals("attrInt"))
			return ColType.INT;
		else if (properyType.equals("attrFloat"))
			return ColType.FLOAT;
		else if (properyType.equals("attrDouble"))
			return ColType.DOUBLE;
		else if (properyType.equals("attrNumber"))
			return ColType.DOUBLE;
		else if (properyType.equals("longtext"))
			return ColType.LONGTEXT;
		else if (properyType.equals("attrDate"))
			return ColType.DATE;
		return null;
	}

	
	//保存页面表单提交过来的数据
	public Long saveFormData(Long fid, JSONObject jsonObj) throws Exception   {
			Long formNextId =0l;
		 
			Form form = (Form)super.findObjByKey(Form.class, fid);
			JSONObject tempJsonObj = jsonObj.getJSONObject("register");
			//1.得到form中所有的属性
			List<FormProperty> formPropertyList = form.getPropertyList();
			//2.获取对应Form的ZTable
			ZTable ztable = this.ztableService.getZTableByName(form.getFormName());
			//3.得到存储Form对象表的下一个ID
			formNextId = ExecutionSql.getnextId(jdbcTemplate,ztable.getTablename());
			/**保存form表的数据**/
			if(formPropertyList != null) {
				ExecutionSql.insertFormData(jdbcTemplate,formPropertyList, tempJsonObj, formNextId, ztable,form.getIshistoryTable());
			}
			//扩展表
			if(jsonObj.has("registerExpand")){
				List<Form>  formList=this.findSubsetForm(fid);
				if(formList!=null&&formList.size()>0){
					Form formExpand =formList.get(0);
					JSONObject registerExpandJsonObj = jsonObj.getJSONObject("registerExpand");
					registerExpandJsonObj.put("expandId", formNextId);
					List<FormProperty> formPropertyExpandList = formExpand.getPropertyList();
					
					ZTable ztableExpand = this.ztableService.getZTableByName(formExpand.getFormName());
					Long formNextExpandId =formNextId;// ExecutionSql.getnextId(dataSource,ztableExpand.getTablename());
					
					if(formPropertyExpandList != null) {
						ExecutionSql.insertFormData(jdbcTemplate,formPropertyExpandList, registerExpandJsonObj, formNextExpandId, ztableExpand,form.getIshistoryTable());
					}
				}
			}
			
			//11.解析JSON数据中的MiddleTable对象结构的值
			if(jsonObj.has("tableDataList")) {
				/** 保存middleTable表的数据**/
				try {
					insertTableData(jsonObj,form,formNextId);	
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		 
		return formNextId;
	}
	/**
	 * 修改form的TaskId
	 * @param taskId
	 * @param participationId
	 * @throws Exception 
	 */
	public void updateFormDataTask(Long formId,String tableName,Long dataId,String taskId, String participationId) throws Exception{
		
		//9.拼接SQLUser createUser= UserUtil.getUser();
		String updateFormsql = " update "+ tableName +" set taskId='"+taskId+"',participationId='"+participationId+"',createBy='"+UserUtil.getUser().getId()+"'" ;
		updateFormsql+=" where id="+dataId;
		/*System.out.println(updateFormsql);
		 
			if(taskId!=null){
				Map<String, Object> map1 = new HashMap<String, Object>();
				String username = UserUtil.getUser().getUsername();
				map1.put("owner", username);
				Date date = new Date();
		        SimpleDateFormat   dateFormat   =   new   SimpleDateFormat("yyyyMMdd");//可以方便地修改日期格式     
		        String  dateStr  = dateFormat.format(date);
				map1.put("day", Integer.parseInt(dateStr));
				map1.put("dataId", dataId);
				map1.put("tableName", tableName);
				map1.put("formId", formId);
				map1.put("name", username);
				jbpmService.complete(taskId, map1); 
		} */
		
		//10.执行SQL
		ExecutionSql.executionsql(null, "execution", jdbcTemplate, updateFormsql, null);	
	}
	/**
	 * 修改状态
	 * @param zform
	 * @param dataId
	 * @param statusCode
	 * @throws Exception 
	 */
	public void updateFormStatus(String tableName, Long dataId,String dicTypeCode,String dicCode,String dicValCode,User currentUser) throws Exception{
		DataDictionaryValue divValue= dataDictionaryService.getDataDictionaryValueById( dicTypeCode ,dicCode, dicValCode);
		String status= divValue.getValue();
		String statusName= divValue.getDisplayValue();
		
		String valueDate=TimeUitl.getQuerystartTime(new Date().getTime()).toString();  
		//9.拼接SQL
		String updateFormsql = " update "+ tableName +" set status='"+status+"',approvalUser='"+currentUser.getId()+"', endDateApproval ='"+valueDate+"',statusName='"+statusName+"'" ;
		updateFormsql+=" where id="+dataId;
		System.out.println(updateFormsql);
		//10.执行SQL
		ExecutionSql.executionsql(null, "execution", jdbcTemplate, updateFormsql, null);	
	} 
	/**修改数据 
	 * @param form
	 * @param jsonObj
	 * @throws Exception 
	 */
	public void updateFormData(Long fid, JSONObject jsonObj) throws Exception{
 
			Form form = (Form)super.findObjByKey(Form.class, fid);
			JSONObject tempJsonObj = jsonObj.getJSONObject("register");
			//1.得到form中所有的属性
			List<FormProperty> formPropertyList = form.getPropertyList();
			//2.获取对应Form的ZTable
			ZTable ztable = this.ztableService.getZTableByName(form.getFormName());
			//3.得到存储Form对象表的下一个ID
			Long tableDataId = jsonObj.getLong("tableDataId");
			/**保存form表的数据**/
			if(formPropertyList != null) {
				ExecutionSql.updateFormData(jdbcTemplate,formPropertyList, tempJsonObj, tableDataId, ztable); 
			}
			//扩展表
			if(jsonObj.has("registerExpand")){
				List<Form>  formList=this.findSubsetForm(fid);
				if(formList!=null&&formList.size()>0){
					Form formExpand =formList.get(0);
					JSONObject registerExpandJsonObj = jsonObj.getJSONObject("registerExpand");
					registerExpandJsonObj.put("expandId", tableDataId);
					List<FormProperty> formPropertyExpandList = formExpand.getPropertyList();
					
					ZTable ztableExpand = this.ztableService.getZTableByName(formExpand.getFormName());
					if(formPropertyExpandList != null) {
						ExecutionSql.updateFormData(jdbcTemplate,formPropertyExpandList, registerExpandJsonObj, tableDataId, ztableExpand); 
						//insertFormData(formPropertyExpandList, registerExpandJsonObj, formNextExpandId, ztableExpand,form.getIshistoryTable());
					}
				}
			}
			
			//11.解析JSON数据中的MiddleTable对象结构的值
			if(jsonObj.has("tableDataList")) {
				/** 保存middleTable表的数据**/ 
				insertTableData(jsonObj,form,tableDataId);
				//updateTableData(jsonObj,form,tableDataId);
			}
		 
	}
	
	
	/**
	 * 保存middleTable表的数据
	 * @param jsonObj
	 * @throws Exception 
	 */
	private void insertTableData(JSONObject jsonObj,Form form,Long formId) throws Exception {
		org.json.JSONArray jsonArray = jsonObj.getJSONArray("tableDataList");
		if(jsonArray.length() > 0) {
			//12.遍历Table对象结构的数据
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject tempObj = jsonArray.getJSONObject(i);
				Long middleTableId = Long.parseLong(tempObj.get("middleTableId").toString());//  table的一个标识
				org.json.JSONArray props =new JSONArray();
				if(tempObj.has("tablePropertyList"))props = tempObj.getJSONArray("tablePropertyList"); // table的数据
				//13.获取MiddleTable
				MiddleTable middleTable = middleTableService.findByFormIdAndMiddleTableId(form.getId(), middleTableId);
				boolean isdelete=true;
				if(tempObj.has("isdelete")){
					int isdeleteInt=Integer.parseInt(tempObj.get("isdelete").toString()) ;
					if(isdeleteInt==0){
						isdelete=false;
					}
				}
				if(isdelete){
					String  deleteStr="delete from "+middleTable.getMiddleTableName() +" where "+form.getFormName()+"_id ="+formId.toString() ; 
					ExecutionSql.executionsql(null, "execution", jdbcTemplate, deleteStr, null );	
				}
				for(int j = 0; j < props.length(); j++){
					//14.存放对应表的的字段
					List<ZCloumnData> zCloumnDataList = new ArrayList<ZCloumnData>();
					//15.得到MiddleTable中的property
					JSONObject tablePropObj = props.getJSONObject(j);
					//16.遍历当前MiddleTable的property
					for(TableProperty tableProperty : middleTable.getTablePropertyList()) {
						//17.得到当前MiddleTable的property 对应的JSON值
						String value2 = (String)tablePropObj.get(tableProperty.getTablePropertyName());
						//18.封装数据
						ZCloumnData zCloumnData = new ZCloumnData(
								tableProperty.getTablePropertyName(),
								tableProperty.getTablePropertyType(),
								value2);
						zCloumnDataList.add(zCloumnData);
					}
					//19.得到MiddleTable表对象
					/*ZTable middleZtable =  this.ztableService.getZTableByName(form.getFormName() + "middle"
							+ middleTable.getMiddleTableId());//middleTable.getZtable();
					*/
					ZTable middleZtable =  this.ztableService.getZTableByName(middleTable.getMiddleTableName());//middleTable.getZtable();
					//20.封装对应外键的对象
					ZCloumnData formCloumnDataId = new ZCloumnData(form.getFormName()+"_id","Long", formId.toString());//from的外键
					zCloumnDataList.add(formCloumnDataId);
					//21.得到MiddleTable对应表的下一个ID
					Long id = ExecutionSql.getnextId(jdbcTemplate,middleZtable.getTablename());
					//22.封装ID
					ZCloumnData zCloumnDataId = new ZCloumnData("id","Long", id.toString());//主键id
					zCloumnDataList.add(zCloumnDataId);
					
					
					ZCloumnData createBy = new ZCloumnData("createBy", "attrString", UserUtil.getUser().getId().toString());//主键id
					zCloumnDataList.add(createBy);
					//8.最后录入数据的时间
					 String nowdate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()); 
					ZCloumnData lastWriteData = new ZCloumnData("createDate", "attrDate", nowdate);
					zCloumnDataList.add(lastWriteData); 
					
					ZCloumnData ModifyBy = new ZCloumnData("modifyBy", "attrString", UserUtil.getUser().getId().toString());//主键id
					zCloumnDataList.add(ModifyBy);
					
					ZCloumnData ModifyDate = new ZCloumnData("modifyDate", "attrDate", nowdate);
					zCloumnDataList.add(ModifyDate);
					
					ZCloumnData guid = new ZCloumnData("guid", "attrString", java.util.UUID.randomUUID().toString());
					zCloumnDataList.add(guid);
					
					//23.拼接SQL
					String insertFormsql = MySQLZDialect.insertData(middleZtable,middleZtable.getTablename(), zCloumnDataList);
					System.out.println(insertFormsql);
					//24.执行SQL
					ExecutionSql.executionsql(null, "execution",jdbcTemplate, insertFormsql, null);
				}
			}
		}
	}
	
	/**
	 * 修改middleTable表的数据
	 * @param jsonObj
	 * @throws Exception 
	 */ 
	@SuppressWarnings("unused")
	private void updateTableData(JSONObject jsonObj,Form form,Long formId) throws Exception {
		org.json.JSONArray jsonArray = jsonObj.getJSONArray("tableDataList");
		if(jsonArray.length() > 0) {
			//12.遍历Table对象结构的数据
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject tempObj = jsonArray.getJSONObject(i);
				Long middleTableId = Long.parseLong(tempObj.get("middleTableId").toString());//  table的一个标识
				org.json.JSONArray props = tempObj.getJSONArray("tablePropertyList"); // table的数据
				//13.获取MiddleTable
				MiddleTable middleTable = middleTableService.findByFormIdAndMiddleTableId(form.getId(), middleTableId);
				for(int j = 0; j < props.length(); j++){
					//14.存放对应表的的字段
					List<ZCloumnData> zCloumnDataList = new ArrayList<ZCloumnData>();
					//15.得到MiddleTable中的property
					JSONObject tablePropObj = props.getJSONObject(j);
					//16.遍历当前MiddleTable的property
					for(TableProperty tableProperty : middleTable.getTablePropertyList()) {
						//17.得到当前MiddleTable的property 对应的JSON值
						String value2 = (String)tablePropObj.get(tableProperty.getTablePropertyName());
						//18.封装数据
						ZCloumnData zCloumnData = new ZCloumnData(
								tableProperty.getTablePropertyName(),
								tableProperty.getTablePropertyType(),
								value2);
						zCloumnDataList.add(zCloumnData);
					}
					//19.得到MiddleTable表对象
					/*ZTable middleZtable =  this.ztableService.getZTableByName(form.getFormName() + "middle"
							+ middleTable.getMiddleTableId());//middleTable.getZtable();
					*/ 
					ZTable middleZtable =  this.ztableService.getZTableByName(middleTable.getMiddleTableName());//middleTable.getZtable();
					
					//20.封装对应外键的对象
					ZCloumnData formCloumnDataId = new ZCloumnData(form.getFormName()+"_id","Long", formId.toString());//from的外键
					zCloumnDataList.add(formCloumnDataId); 
					/*
					//21.得到MiddleTable对应表的下一个ID
					Long id = ExecutionSql.getnextId(dataSource,middleZtable.getTablename());
					//22.封装ID
					ZCloumnData zCloumnDataId = new ZCloumnData("id","Long", id.toString());//主键id
					zCloumnDataList.add(zCloumnDataId);
					*/
					Long id = Long.parseLong(tablePropObj.get("id").toString());//  table的一个标识
					//23.拼接SQL
					String updateFormsql = MySQLZDialect.updateColumnsValue(middleZtable.getTablename(), zCloumnDataList);
					updateFormsql+=" where id="+id;
					System.out.println(updateFormsql);
					//24.执行SQL
					ExecutionSql.executionsql(null, "execution",jdbcTemplate, updateFormsql, null);
				}
			}
		}
	}
	
	// 获取form对应表的数据 ,和middleTable对应的表的标识
	public FormDATableDAId getFormDATableDAIdByFormId(Form form) {
		FormDATableDAId formDATableDAId = new FormDATableDAId();// 返回的对象
		try {
			ZTable ztable = form.getZtable();
			if(ztable != null) {
				// 查询form对应表的数据//这里要加条件
				String formTablesql = MySQLZDialect.selectData(
						ztable.getTablename(), null);
				System.out.println(formTablesql);
				String jsonStr = ExecutionSql.executionsql(ztable, "query",
						jdbcTemplate, formTablesql, null);
				// 取出对象
				JSONObject jsonObj = new JSONObject(jsonStr);
				org.json.JSONArray jsonArray = jsonObj.getJSONArray(ztable
						.getTablename());
				if(jsonArray.length() > 0) {
					// 应为
					JSONObject tempObj1 = jsonArray.getJSONObject(0);
					formDATableDAId.setRegister(tempObj1.toString());
				}
				formDATableDAId.setFormId(form.getId());
			}
			List<MiddleTable> middleTableList = (List<MiddleTable>) this.middleTableService.findByFormId(form.getId());
			if(middleTableList != null) {
				List<TableDataId> tableDataIdList = new ArrayList<TableDataId>();
				for(MiddleTable middleTable : middleTableList) {
					TableDataId tableDataId = new TableDataId();
					tableDataId.setMiddleTablename(middleTable.getMiddleTableName());
					tableDataId.setMiddleTableId(middleTable.getMiddleTableId());
					// 查询MiddleTable中对应表的数据个数//要加外键的条件
					/*String middleTableSql = MySQLZDialect.selectDataCount(
							form.getFormName() + "middle" + middleTable.getMiddleTableId(), null);
							*/
					String middleTableSql = MySQLZDialect.selectDataCount(
							middleTable.getMiddleTableName(), null);
					System.out.println(middleTableSql);
					String jsonStr = ExecutionSql.executionsql(ztable, "count",
							jdbcTemplate, middleTableSql, null);
					if (jsonStr != null) {
						try {
							Integer count = Integer.parseInt(jsonStr);
							if (count > 0)
								tableDataId.setIsData(true);
							else
								tableDataId.setIsData(false);
						} catch (Exception e) {
							e.printStackTrace();
							tableDataId.setIsData(false);
						}
					} else
						tableDataId.setIsData(false);
					tableDataIdList.add(tableDataId);
				}
				formDATableDAId.setTableDateList(tableDataIdList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formDATableDAId;
	}

	/**
	 * 获取单个middleTable对应的表的数据
	 * 
	 * @return
	 * @throws Exception 
	 */
	public TableDataId getTableDataIdByMiddel(MiddleTable middleTable, Form form) throws Exception {
		TableDataId tableDataId = new TableDataId();
		tableDataId.setMiddleTablename(middleTable.getMiddleTableName());
		tableDataId.setMiddleTableId(middleTable.getMiddleTableId());
		// 查询MiddleTable中对应表的数据个数//要加外键的条件
		/*String middleTableSql = MySQLZDialect.selectData(form.getFormName()
				+ "middle" + middleTable.getMiddleTableId(), null);
		*/
		String middleTableSql = MySQLZDialect.selectData(middleTable.getMiddleTableName(), null);
		System.out.println(middleTableSql);
		String jsonStr = ExecutionSql.executionsql(middleTable.getZtable(),
				"query", jdbcTemplate, middleTableSql, null);
		if (jsonStr != null) {
			tableDataId.setIsData(true);
			try {
				JSONObject jsonObject = new JSONObject(jsonStr);
				if (jsonObject != null) {
					//String middleTablename = form.getFormName() + "middle" + middleTable.getMiddleTableId();
					String middleTablename =middleTable.getMiddleTableName();
					JSONArray jsonArray = jsonObject
							.getJSONArray(middleTablename);
					jsonObject.remove(middleTablename);
					if (jsonArray != null)
						jsonObject.put("tabledataList", jsonArray);
					tableDataId.setTabledata(jsonObject.toString());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return tableDataId;
	}
	
	/**
	 * 根据数据库的名称查询出Form
	 * @param tableName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Form findByFormName(String tableName){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Form.class);
		cri.add(Restrictions.eq("formName", tableName));
		 List<Form> formList= super.queryDao.getByDetachedCriteria(cri);
		 if(formList!=null&&formList.size()>=1){
			 return formList.get(0);
		 }
		 return null;
	}
	/**
	 * 获取发布的form
	 * @param b
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Form> getFormPush(boolean b){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Form.class);
		cri.add(Restrictions.eq("isPublish", b));
		 List<Form> formList= super.queryDao.getByDetachedCriteria(cri);
		 return formList;
	}
	@SuppressWarnings("unchecked")
	public List<Form> getFormList(Map<String, Object>  map, PageTrace pageTrace){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Form.class);
		if(map!=null){
			if(map.get("isDelete")!=null&&!map.get("isDelete").toString().trim().equals("")){
				cri.add(Restrictions.eq("isDelete", map.get("isDelete")));		
			}
			if(map.get("isPublish")!=null&&!map.get("isPublish").toString().trim().equals("")){
				cri.add(Restrictions.eq("isPublish", map.get("isPublish")));		
			}
			if(map.get("formName")!=null&&!map.get("formName").toString().trim().equals("")){
				cri.add(Restrictions.ilike("formName", map.get("formName").toString(), MatchMode.ANYWHERE));
			}
		}
		//cri.addOrder(Order.desc("id"));
		return super.queryDao.getByDetachedCriteria(cri, pageTrace, false); 
	}
	
	@SuppressWarnings("unchecked")
	public  List<Form>  findSubsetForm(Long fromId){
		PageDetachedCriteria cri = PageDetachedCriteria.forClass(Form.class);
		cri.add(Restrictions.eq("baseFormId", fromId));
		 List<Form> formList= super.queryDao.getByDetachedCriteria(cri);
		 return formList;
	}
	
	
	/**
	 * 
	 * @param fid
	 * @param dataId
	 * @throws Exception
	 */
	public void deleteDataById(Long fid, Long  dataId) throws Exception{
			Form form = (Form)super.findObjByKey(Form.class, fid);
			String deleteData= "delete from "+form.getFormName()+" where id="+dataId; 
			ExecutionSql.executionsql(null, "execution", jdbcTemplate, deleteData, null);
	}
	
	public void updateBySql(String sql){
		super.jdbcTemplate.execute(sql);
	}

	
	
	

	public JSONObject saveFormDataJson(String[] jsons) throws Exception{ 
		String jdbcUrl = dataSource.getJdbcUrl();
		String databaseUsername = dataSource.getUser();
		String databasePassword = dataSource.getPassword();
		Connection connection = null;
        Statement stmt = null;
        ResultSet resultSet = null;

    	connection = DriverManager.getConnection(jdbcUrl, databaseUsername, databasePassword); 
    	connection.setAutoCommit(false);//禁止自动提交，设置回滚点
    	stmt =connection.createStatement();
    	
    	JSONObject returnObj = new JSONObject(); 
		boolean isCorrect=false;
        try {
        	if(jsons!=null&&jsons.length>0){
        		for (String jsonString : jsons) {
        	        JSONObject jsonObj = new JSONObject(jsonString);
        			Long formId = jsonObj.getLong("formId"); 
        			Form form = (Form)super.findObjByKey(Form.class, formId);
        			ZTable ztable = this.ztableService.getZTableByName(form.getFormName());
        			//1.得到form中所有的属性
        			List<FormProperty> formPropertyList = form.getPropertyList();
        			/**保存form表的数据**/
        			Long formNextId=0l; 

                	if(formPropertyList != null) { 
            			if(jsonObj.has("tableDataId"))
            			{
            				formNextId = jsonObj.getLong("tableDataId");
            				/**保存form表的数据**/
            				boolean isSave=isSaveForm(jsonObj);
            				if(isSave){
                    			JSONObject tempJsonObj = jsonObj.getJSONObject("register");
                    			ExecutionSql.updateFormDataConn(stmt,formPropertyList, tempJsonObj, formNextId, ztable);
            				}
            					
            			}else {
            				//3.得到存储Form对象表的下一个ID 
            				formNextId = ExecutionSql.getnextId(jdbcTemplate,form.getFormName());
                			JSONObject tempJsonObj = jsonObj.getJSONObject("register");
                			if(jsonObj.has("generateCode")&&jsonObj.has("fieldName")){
                				String generateCode=jsonObj.getString("generateCode");
                				if(jsonObj.has("fieldName")){
                					String generateCodeVal= springJdbcService.getGenerateCode(generateCode);
                					tempJsonObj.put(jsonObj.getString("fieldName"), generateCodeVal);	
                				}
                			}
            				ExecutionSql.insertFormDataConn(stmt,formPropertyList, tempJsonObj, formNextId, ztable,form.getIshistoryTable());
            			}
            		}
            		if(jsonObj.has("detail")){
            			insertFormDataDetailConn(stmt,resultSet,jsonObj, formNextId,1);
            		}
            		String insertJsonObjSql=" insert into zflow_saveFormDataJson_log(formDataId,jsonObj,saveType,isSuccess,createTime) values ('"+formNextId+"','"+jsonString+"',2,1,NOW())  ";
            		ExecutionSql.execution(jdbcTemplate, insertJsonObjSql);
            		
            		returnObj.put(form.getFormName(), formNextId);
    			}
        	}
    		connection.commit(); //事务提交
        	isCorrect=true;
		} catch (Exception e) {
			e.printStackTrace();
			connection.rollback(); //操作不成功则回滚 
		}finally {
			try {
				if(resultSet != null) {
					resultSet.close();
					resultSet = null;
				}
				if(stmt  != null) {
					stmt .close();
					stmt  = null;
				}
				if(connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}  

		returnObj.put("isCorrect", isCorrect);
		return returnObj;
	}


	public JSONObject saveFormDataJsonAndCalls(String[] jsons, List<String> callsParam) throws Exception{ 
		String jdbcUrl = dataSource.getJdbcUrl();
		String databaseUsername = dataSource.getUser();
		String databasePassword = dataSource.getPassword();
		Connection connection = null;
        Statement stmt = null;
        ResultSet resultSet = null;

    	connection = DriverManager.getConnection(jdbcUrl, databaseUsername, databasePassword); 
    	connection.setAutoCommit(false);//禁止自动提交，设置回滚点
    	stmt =connection.createStatement();
    	
    	JSONObject returnObj = new JSONObject(); 
		boolean isCorrect=false;
        try {
        	if(jsons!=null&&jsons.length>0){
        		for (String jsonString : jsons) {
        	        JSONObject jsonObj = new JSONObject(jsonString);
        			Long formId = jsonObj.getLong("formId"); 
        			Form form = (Form)super.findObjByKey(Form.class, formId);
        			ZTable ztable = this.ztableService.getZTableByName(form.getFormName());
        			//1.得到form中所有的属性
        			List<FormProperty> formPropertyList = form.getPropertyList();
        			/**保存form表的数据**/
        			Long formNextId=0l; 

                	if(formPropertyList != null) { 
            			if(jsonObj.has("tableDataId"))
            			{
            				formNextId = jsonObj.getLong("tableDataId");
            				/**保存form表的数据**/
            				boolean isSave=isSaveForm(jsonObj);
            				if(isSave){
                    			JSONObject tempJsonObj = jsonObj.getJSONObject("register");
                    			ExecutionSql.updateFormDataConn(stmt,formPropertyList, tempJsonObj, formNextId, ztable);
            				}
            					
            			}else {
            				//3.得到存储Form对象表的下一个ID 
            				formNextId = ExecutionSql.getnextId(jdbcTemplate,form.getFormName());
                			JSONObject tempJsonObj = jsonObj.getJSONObject("register");
            				ExecutionSql.insertFormDataConn(stmt,formPropertyList, tempJsonObj, formNextId, ztable,form.getIshistoryTable());
            			}
            		}
            		if(jsonObj.has("detail")){
            			insertFormDataDetailConn(stmt,resultSet,jsonObj, formNextId,1);
            		}
            		String insertJsonObjSql=" insert into zflow_saveFormDataJson_log(formDataId,jsonObj,saveType,isSuccess,createTime) values ('"+formNextId+"','"+jsonString+"',2,1,NOW())  ";
            		ExecutionSql.execution(jdbcTemplate, insertJsonObjSql);
            		
            		returnObj.put(form.getFormName(), formNextId);
    			}
        	}
    		//调用存储过程
        	try {
        		if(callsParam!=null&&callsParam.size()>0){
        			for (String param: callsParam) {
            			this.callShellProcedure(connection, param);	
    				}
        		}	
			} catch (Exception e) {
				// TODO: handle exception
			}
    		connection.commit(); //事务提交
        	isCorrect=true;
		} catch (Exception e) {
			e.printStackTrace();
			connection.rollback(); //操作不成功则回滚 
		}finally {
			try {
				if(resultSet != null) {
					resultSet.close();
					resultSet = null;
				}
				if(stmt  != null) {
					stmt .close();
					stmt  = null;
				}
				if(connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}  

		returnObj.put("isCorrect", isCorrect);
		return returnObj;
	}
	
	public Long saveFormDataJson(String jsonString) throws Exception{
		String jdbcUrl=dataSource.getJdbcUrl();
		String databaseUsername=dataSource.getUser();
		String databasePassword=dataSource.getPassword();
		Connection connection = null;
        Statement stmt  = null;
        ResultSet resultSet = null;
        
        JSONObject jsonObj = new JSONObject(jsonString);
		Long formId = jsonObj.getLong("formId");
		Form form = (Form)super.findObjByKey(Form.class, formId);
		JSONObject tempJsonObj = jsonObj.getJSONObject("register");
		if(jsonObj.has("generateCode")&&jsonObj.has("fieldName")){
			String generateCode=jsonObj.getString("generateCode");
			if(jsonObj.has("fieldName")){
				String generateCodeVal= springJdbcService.getGenerateCode(generateCode);
				tempJsonObj.put(jsonObj.getString("fieldName"), generateCodeVal);	
			}
		}
		ZTable ztable = this.ztableService.getZTableByName(form.getFormName());
		//1.得到form中所有的属性
		List<FormProperty> formPropertyList = form.getPropertyList();
		/**保存form表的数据**/
		Long formNextId=0l;
		int isSuccess=0;
        try {
        	connection = DriverManager.getConnection(jdbcUrl, databaseUsername, databasePassword); 
        	connection.setAutoCommit(false);//禁止自动提交，设置回滚点
        	stmt =connection.createStatement();
        	if(formPropertyList != null) { 
    			if(jsonObj.has("tableDataId"))
    			{
    				formNextId = jsonObj.getLong("tableDataId");
    				/**保存form表的数据**/
    				boolean isSave=isSaveForm(jsonObj);
    				if(isSave)
    					ExecutionSql.updateFormDataConn(stmt,formPropertyList, tempJsonObj, formNextId, ztable);
    			}else {
    				//3.得到存储Form对象表的下一个ID 
    				//formNextId = ExecutionSql.getnextId(jdbcTemplate,form.getFormName());
    				formNextId = ExecutionSql.getnextId(stmt, resultSet, form.getFormName());
    				ExecutionSql.insertFormDataConn(stmt,formPropertyList, tempJsonObj, formNextId, ztable,form.getIshistoryTable());
    			}
    		}
    		if(jsonObj.has("detail")){
    			insertFormDataDetailConn(stmt,resultSet,jsonObj, formNextId,1);
    		}
    		connection.commit(); //事务提交
    		isSuccess=1;
		} catch (Exception e) {
			e.printStackTrace();
			connection.rollback(); //操作不成功则回滚
			formNextId=0l;
		}finally {
			try {
				if(resultSet != null) {
					resultSet.close();
					resultSet = null;
				}
				if(stmt  != null) {
					stmt .close();
					stmt  = null;
				}
				if(connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}   
		String insertJsonObjSql=" insert into zflow_saveFormDataJson_log(formDataId,jsonObj,saveType,isSuccess,createTime) values ('"+formNextId+"','"+jsonString+"',1,"+isSuccess+",NOW())  ";
		ExecutionSql.execution(jdbcTemplate, insertJsonObjSql);
		return formNextId;
	}
	
 
	public Long saveFormDataJsonAndCalls(String jsonString, List<String> callsParam)throws Exception {
		String jdbcUrl=dataSource.getJdbcUrl();
		String databaseUsername=dataSource.getUser();
		String databasePassword=dataSource.getPassword();
		Connection connection = null;
        Statement stmt  = null;
        ResultSet resultSet = null;
        
        JSONObject jsonObj = new JSONObject(jsonString);
		Long formId = jsonObj.getLong("formId"); 
		Form form = (Form)super.findObjByKey(Form.class, formId);
		JSONObject tempJsonObj = jsonObj.getJSONObject("register");
		ZTable ztable = this.ztableService.getZTableByName(form.getFormName());
		//1.得到form中所有的属性
		List<FormProperty> formPropertyList = form.getPropertyList();
		/**保存form表的数据**/
		Long formNextId=0l;
		int isSuccess=0;
        try {
        	connection = DriverManager.getConnection(jdbcUrl, databaseUsername, databasePassword); 
        	connection.setAutoCommit(false);//禁止自动提交，设置回滚点
        	stmt =connection.createStatement();
        	if(formPropertyList != null) { 
    			if(jsonObj.has("tableDataId"))
    			{
    				formNextId = jsonObj.getLong("tableDataId");
    				/**保存form表的数据**/
    				boolean isSave=isSaveForm(jsonObj);
    				if(isSave)
    					ExecutionSql.updateFormDataConn(stmt,formPropertyList, tempJsonObj, formNextId, ztable);
    			}else {
    				//3.得到存储Form对象表的下一个ID 
    				formNextId = ExecutionSql.getnextId(jdbcTemplate,form.getFormName());
    				ExecutionSql.insertFormDataConn(stmt,formPropertyList, tempJsonObj, formNextId, ztable,form.getIshistoryTable());
    			}
    		}
    		if(jsonObj.has("detail")){
    			insertFormDataDetailConn(stmt,resultSet,jsonObj, formNextId,1);
    		}
    		//调用存储过程
    		if(callsParam!=null&&callsParam.size()>0){
    			for (String param: callsParam) {
    				if(param.indexOf("${id}")>=0){
    					String param2 = param.replace("${id}",formNextId.toString());
    					this.callShellProcedure(connection, param2);
    				}else{
    					this.callShellProcedure(connection, param);
    				}	
				}
    		}
    		connection.commit(); //事务提交
    		isSuccess=1;
		} catch (Exception e) {
			e.printStackTrace();
			connection.rollback(); //操作不成功则回滚
			formNextId=0l;
		}finally {
			try {
				if(resultSet != null) {
					resultSet.close();
					resultSet = null;
				}
				if(stmt  != null) {
					stmt .close();
					stmt  = null;
				}
				if(connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}   
		String insertJsonObjSql=" insert into zflow_saveFormDataJson_log(formDataId,jsonObj,saveType,isSuccess,createTime) values ('"+formNextId+"','"+jsonString+"',1,"+isSuccess+",NOW())  ";
		ExecutionSql.execution(jdbcTemplate, insertJsonObjSql);
		return formNextId;
	}
	
	/**
	 * 保存    数据回滚 接口
	 * @param jsonString
	 * @param stmt
	 * @return
	 * @throws Exception
	 */
	public Long saveFormDataJson(String jsonString,Statement stmt,ResultSet resultSet) throws Exception{
  
        JSONObject jsonObj = new JSONObject(jsonString);
		Long formId = jsonObj.getLong("formId"); 
		Form form = (Form)super.findObjByKey(Form.class, formId);
		JSONObject tempJsonObj = jsonObj.getJSONObject("register");
		if(jsonObj.has("generateCode")&&jsonObj.has("fieldName")){
			String generateCode=jsonObj.getString("generateCode");
			if(jsonObj.has("fieldName")){
				String generateCodeVal= springJdbcService.getGenerateCode(generateCode);
				tempJsonObj.put(jsonObj.getString("fieldName"), generateCodeVal);	
			}
		}
		ZTable ztable = this.ztableService.getZTableByName(form.getFormName());
		//1.得到form中所有的属性
		List<FormProperty> formPropertyList = form.getPropertyList();
		/**保存form表的数据**/
		Long formNextId=0l;
		 
        	if(formPropertyList != null) { 
    			if(jsonObj.has("tableDataId"))
    			{
    				formNextId = jsonObj.getLong("tableDataId");
    				/**保存form表的数据**/
    				boolean isSave=isSaveForm(jsonObj);
    				if(isSave)
    					ExecutionSql.updateFormDataConn(stmt,formPropertyList, tempJsonObj, formNextId, ztable);
    			}else {
    				//3.得到存储Form对象表的下一个ID 
    				formNextId = ExecutionSql.getnextId(jdbcTemplate,form.getFormName());
    				ExecutionSql.insertFormDataConn(stmt,formPropertyList, tempJsonObj, formNextId, ztable,form.getIshistoryTable());
    			}
    		}
    		if(jsonObj.has("detail")){
    			insertFormDataDetailConn(stmt,resultSet,jsonObj, formNextId,1);
    		}  

		return formNextId;
	}
	
	
	/**
	 * 保存    数据回滚 接口
	 * @param jsonString
	 * @param stmt
	 * @return
	 * @throws Exception
	 */
	public Long saveFormDataJson(String jsonString,Statement stmt,ResultSet resultSet,Connection connection, List<String> callsParam) throws Exception{
  
        JSONObject jsonObj = new JSONObject(jsonString);
		Long formId = jsonObj.getLong("formId"); 
		Form form = (Form)super.findObjByKey(Form.class, formId);
		JSONObject tempJsonObj = jsonObj.getJSONObject("register");
		ZTable ztable = this.ztableService.getZTableByName(form.getFormName());
		//1.得到form中所有的属性
		List<FormProperty> formPropertyList = form.getPropertyList();
		/**保存form表的数据**/
		Long formNextId=0l;
		 
        	if(formPropertyList != null) { 
    			if(jsonObj.has("tableDataId"))
    			{
    				formNextId = jsonObj.getLong("tableDataId");
    				/**保存form表的数据**/
    				boolean isSave=isSaveForm(jsonObj);
    				if(isSave)
    					ExecutionSql.updateFormDataConn(stmt,formPropertyList, tempJsonObj, formNextId, ztable);
    			}else {
    				//3.得到存储Form对象表的下一个ID 
    				formNextId = ExecutionSql.getnextId(jdbcTemplate,form.getFormName());
    				ExecutionSql.insertFormDataConn(stmt,formPropertyList, tempJsonObj, formNextId, ztable,form.getIshistoryTable());
    			}
    		}
    		if(jsonObj.has("detail")){
    			insertFormDataDetailConn(stmt,resultSet,jsonObj, formNextId,1);
    		}  


    		//调用存储过程
    		try {
    			if(callsParam!=null&&callsParam.size()>0){
    				for (String param: callsParam) {
    	    			this.callShellProcedure(connection, param);	
    				}
    			}	
    		} catch (Exception e) {
    			// TODO: handle exception
    		}
		return formNextId;
	}
	

	private void  deleteDetailData(Statement stmt,JSONObject jsonObj,int lin,Long tableDataId, List<String> deleteSqlList ,List<String> deleteIdList ) throws Exception{		
		if(jsonObj.has("subStructure")){
			org.json.JSONArray structureArray = jsonObj.getJSONArray("subStructure");
			if(structureArray.length()>0){
				for(int j = 0; j < structureArray.length(); j++) {
					JSONObject structure = structureArray.getJSONObject(j);
					List<String> deletesubIdList=new ArrayList<String>(); 
					if(structure.has("formId")){
						Long detailFormId=structure.getLong("formId"); 
						Form detailForm = (Form)super.findObjByKey(Form.class, detailFormId);
						if(lin==1){
							if(structure.has("parentId")){
								String sql="select id from "+detailForm.getFormName()+" where "+structure.getString("parentId")+"="+tableDataId;
								ResultSet rs = stmt.executeQuery(sql);
								 while(rs.next()){
									 Long tempId= rs.getLong("id");
									 deletesubIdList.add(tempId.toString());
							      }
								 String deleteSql="delete from  "+detailForm.getFormName()+"  where "+structure.getString("parentId")+"="+tableDataId;
								deleteSqlList.add(deleteSql);
							}
						}else {
							if(deleteIdList!=null&&deleteIdList.size()>0 && structure.has("subStructure")){
								for (String parentDataId : deleteIdList) {
									String sql="select id from "+detailForm.getFormName()+" where "+structure.getString("parentId")+"="+parentDataId;
									ResultSet rs = stmt.executeQuery(sql);
									 while(rs.next()){
										 Long tempId= rs.getLong("id");
										 deletesubIdList.add(tempId.toString());
								      }
								}	
							}
							if(deleteIdList!=null&&deleteIdList.size()>0){
								StringBuffer deleteIds=new StringBuffer();
								for (int i = 0; i < deleteIdList.size(); i++) {
									if(i!=deleteIdList.size()-1)
										deleteIds.append(deleteIdList.get(i)+",");
									else 
										deleteIds.append(deleteIdList.get(i));
								}
								String deleteSql="delete from  "+detailForm.getFormName()+"  where "+structure.getString("parentId")+" in ("+deleteIds+")";
								deleteSqlList.add(deleteSql);
							}
						}
					}
					if(structure.has("subStructure")){
						org.json.JSONArray jsonArray = structure.getJSONArray("subStructure");
						if(jsonArray.length() > 0) {
							deleteDetailData(stmt, structure,2,null,deleteSqlList,deletesubIdList);
						}
					}
				}
			} 
		}  
	}
	private void insertFormDataDetailConn(Statement stmt,ResultSet resultSet,JSONObject jsonObj,Long formNextId ,int lin ) throws Exception{
		//保存子集
		if(jsonObj.has("detail")){
			org.json.JSONArray detailJsonArray = jsonObj.getJSONArray("detail");
			if(detailJsonArray.length()>0){
				for(int j = 0; j < detailJsonArray.length(); j++) {
					JSONObject detailJsonObj = detailJsonArray.getJSONObject(j);
					if(lin==1){
						if(jsonObj.has("tableDataId"))
						{ 
							Long tableDataId = jsonObj.getLong("tableDataId");
							List<String> deleteSqlList=new ArrayList<String>();
			    			deleteDetailData(stmt, detailJsonObj, 1,tableDataId,deleteSqlList , null);
			    			if(deleteSqlList!=null&&deleteSqlList.size()>0){
			    				for (String deleteSql : deleteSqlList) {
			    					System.out.println("deleteSql："+deleteSql);
			    					stmt.executeUpdate(deleteSql);
								}
			    			}
						}	
						
						
					}
					if(detailJsonObj.has("formId")){
						Long detailFormId=detailJsonObj.getLong("formId"); 
						boolean isSave=isSaveForm(detailJsonObj);
						if(!isSave){
							continue;
						}
						Form detailForm = (Form)super.findObjByKey(Form.class, detailFormId);
						ZTable detailZtable = this.ztableService.getZTableByName(detailForm.getFormName());
						
						//删除数据
						boolean isDelete=false;
						Long tableDataId = null;
						if(lin==1){
							if(jsonObj.has("tableDataId"))
							{
								isDelete=true;
								tableDataId = jsonObj.getLong("tableDataId");
							}	
						}else if(lin==2){
							if(jsonObj.has("id"))
							{
								isDelete=true;
								tableDataId = jsonObj.getLong("id");
							}	
						}
						if(isDelete){
							if(detailJsonObj.has("parentId")){
								String deleteSql="delete from "+detailForm.getFormName()+" where "+detailJsonObj.getString("parentId")+"="+tableDataId;
								//SelectToJson.execution(jdbcTemplate, deleteSql);
								stmt.executeUpdate(deleteSql);
								//ExecutionSql.executionsql(null, "execution", dataSource, deleteSql, null); 
							}
						}
						if(!detailJsonObj.has("isDetele")){
							List<FormProperty> detailFormPropertyList = detailForm.getPropertyList();
							if(detailJsonObj.has("array")){
								org.json.JSONArray jsonArray = detailJsonObj.getJSONArray("array");
								if(jsonArray.length() > 0) {
									//12.遍历Table对象结构的数据
									for(int i = 0; i < jsonArray.length(); i++) {
										JSONObject tempObj = jsonArray.getJSONObject(i);
										if(detailJsonObj.has("parentId")){
											tempObj.put(detailJsonObj.getString("parentId"), formNextId);
										}
										if(jsonObj.has("generateCode")&&jsonObj.has("fieldName")){
											String generateCode=jsonObj.getString("generateCode");
											if(jsonObj.has("fieldName")){
												String generateCodeVal= springJdbcService.getGenerateCode(generateCode);
												tempObj.put(jsonObj.getString("fieldName"), generateCodeVal);	
											}
										}
										Long detailformNextId = 0l;
										if(tempObj.has("id")){
											detailformNextId=tempObj.getLong("id");
											String deleteSql="delete from "+detailZtable.getTablename()+" where id="+detailformNextId;
											stmt.executeUpdate(deleteSql); 
										}else{
											//detailformNextId = SelectToJson.getnextId(jdbcTemplate, detailForm.getFormName());
											detailformNextId = ExecutionSql.getnextId(stmt, resultSet, detailForm.getFormName());
										} 
										ExecutionSql.insertFormDataConn(stmt,detailFormPropertyList, tempObj, detailformNextId, detailZtable,detailForm.getIshistoryTable());
										//调用下一级
										if(tempObj.has("detail")){
											insertFormDataDetailConn(stmt,resultSet,tempObj, detailformNextId,2);
										}
									}
								} 
							}
						}
					}
				}
			}
			
		}
	}
	 
	public Long saveFormDataJson2(String jsonString) throws Exception{ 
		JSONObject jsonObj = new JSONObject(jsonString);
		Long formId = jsonObj.getLong("formId"); 
		Form form = (Form)super.findObjByKey(Form.class, formId);
		JSONObject tempJsonObj = jsonObj.getJSONObject("register");
		ZTable ztable = this.ztableService.getZTableByName(form.getFormName());
		//1.得到form中所有的属性
		List<FormProperty> formPropertyList = form.getPropertyList();
		/**保存form表的数据**/
		Long formNextId=0l; 
		if(formPropertyList != null) {
			if(jsonObj.has("tableDataId"))
			{
				formNextId = jsonObj.getLong("tableDataId");
				/**保存form表的数据**/
				boolean isSave=isSaveForm(jsonObj);
				if(isSave)
					ExecutionSql.updateFormData(jdbcTemplate,formPropertyList, tempJsonObj, formNextId, ztable);
			}else {
				//3.得到存储Form对象表的下一个ID 
				formNextId = ExecutionSql.getnextId(jdbcTemplate,form.getFormName());
				ExecutionSql.insertFormData(jdbcTemplate,formPropertyList, tempJsonObj, formNextId, ztable,form.getIshistoryTable());
			}
		}
		if(jsonObj.has("detail")){
			insertFormDataDetail(jsonObj, formNextId,1);	
		}
		return formNextId;
	}
	private boolean isSaveForm(JSONObject jsonObj){
		boolean isSave=true;
		if(jsonObj.has("isSave")){
			Integer saveType= jsonObj.getInt("isSave");
			if(saveType!=null&&saveType.intValue()!=1)
				isSave=false;
		}
		return isSave;
	}
	private void insertFormDataDetail(JSONObject jsonObj,Long formNextId ,int lin ) throws Exception{
		//保存子集
		if(jsonObj.has("detail")){
			org.json.JSONArray detailJsonArray = jsonObj.getJSONArray("detail");
			if(detailJsonArray.length()>0){
				for(int j = 0; j < detailJsonArray.length(); j++) {
					JSONObject detailJsonObj = detailJsonArray.getJSONObject(j);
					if(detailJsonObj.has("formId")){
						Long detailFormId=detailJsonObj.getLong("formId"); 
						boolean isSave=isSaveForm(detailJsonObj);
						if(!isSave){
							continue;
						}
						Form detailForm = (Form)super.findObjByKey(Form.class, detailFormId);
						ZTable detailZtable = this.ztableService.getZTableByName(detailForm.getFormName());
						//删除数据
						boolean isDelete=false;
						Long tableDataId = null;
						if(lin==1){
							if(jsonObj.has("tableDataId"))
							{
								isDelete=true;
								tableDataId = jsonObj.getLong("tableDataId");
							}	
						}else if(lin==2){
							if(jsonObj.has("id"))
							{
								isDelete=true;
								tableDataId = jsonObj.getLong("id");
							}	
						}
						if(isDelete){
							if(detailJsonObj.has("parentId")){
								String deleteSql="delete from "+detailForm.getFormName()+" where "+detailJsonObj.getString("parentId")+"="+tableDataId;
								SelectToJson.execution(jdbcTemplate, deleteSql);
								//ExecutionSql.executionsql(null, "execution", dataSource, deleteSql, null); 
							}
						}
						
						
						List<FormProperty> detailFormPropertyList = detailForm.getPropertyList();
						if(detailJsonObj.has("array")){
							org.json.JSONArray jsonArray = detailJsonObj.getJSONArray("array");
							if(jsonArray.length() > 0) {
								//12.遍历Table对象结构的数据
								
								for(int i = 0; i < jsonArray.length(); i++) {
									JSONObject tempObj = jsonArray.getJSONObject(i);
									
									if(detailJsonObj.has("parentId")){
										tempObj.put(detailJsonObj.getString("parentId"), formNextId);
									}
									Long detailformNextId = 0l;
									if(tempObj.has("id")){
										detailformNextId=tempObj.getLong("id");
									}else{
										//detailformNextId = ExecutionSql.getnextId(dataSource,detailForm.getFormName());
										detailformNextId = SelectToJson.getnextId(jdbcTemplate, detailForm.getFormName());										
									} 
									ExecutionSql.insertFormData(jdbcTemplate,detailFormPropertyList, tempObj, detailformNextId, detailZtable,detailForm.getIshistoryTable());
									//调用下一级
									if(tempObj.has("detail")){
										insertFormDataDetail(tempObj, detailformNextId,2);
									}
								}
							} 
						}
					}
				}
			}
			
		}
	}
	

	public JSONObject deleteFormDataJson(String jsonString) throws Exception{

        JSONObject returnObj = new JSONObject(); 
		 
        JSONObject jsonObj = new JSONObject(jsonString);
		Long formId = jsonObj.getLong("formId");
		 
		String jdbcUrl=dataSource.getJdbcUrl();
		String databaseUsername=dataSource.getUser();
		String databasePassword=dataSource.getPassword();
		Connection connection = null;
        Statement stmt  = null;
        ResultSet resultSet = null;
         
		Form form = (Form)super.findObjByKey(Form.class, formId);  
        try {
        	connection = DriverManager.getConnection(jdbcUrl, databaseUsername, databasePassword); 
        	connection.setAutoCommit(false);//禁止自动提交，设置回滚点
        	stmt =connection.createStatement();
        	
        	Long dataId = jsonObj.getLong("tableDataId");        	
        	List<String> deleteSqlList=new ArrayList<String>();
        	String deletesql="delete from "+form.getFormName() +" where id ="+dataId;
        	deleteSqlList.add(deletesql);
    		if(jsonObj.has("subStructure"))
    		{
    			deleteDetailData(stmt, jsonObj, 1,dataId,deleteSqlList , null);
    		}
    		if(deleteSqlList!=null&&deleteSqlList.size()>0){
				for (String deleteSql : deleteSqlList) {
					System.out.println("deleteSql："+deleteSql);
					stmt.executeUpdate(deleteSql);
				}
			}
    		connection.commit(); //事务提交 
		} catch (Exception e) {
			e.printStackTrace();
			connection.rollback(); //操作不成功则回滚
			throw new Exception("删除错误");
		}finally {
			try {
				if(resultSet != null) {
					resultSet.close();
					resultSet = null;
				}
				if(stmt  != null) {
					stmt .close();
					stmt  = null;
				}
				if(connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}    
		return returnObj;
	}
	
	public JSONObject deleteFormDataJsons(String[] jsons) throws Exception{ 
		if(jsons!=null&&jsons.length>0){
			for (String jsonString : jsons) {
				JSONObject returnObj = new JSONObject(); 
				boolean isCorrect=true;
		        JSONObject jsonObj = new JSONObject(jsonString); 
				if(jsonObj.has("dataIds")){
					String dataIdstr = jsonObj.getString("dataIds");
					if(StringUtils.isNotBlank(dataIdstr)){
						String[] dataIds =dataIdstr.split(",");
						
						for (String dataId : dataIds) {
							if(StringUtils.isBlank(dataId)){
								isCorrect=false;
							}
						}
					}
				}
				if(!isCorrect){
					returnObj.put("isCorrect", isCorrect);
					return returnObj;
				} 
			}
		}
		
		
		String jdbcUrl = dataSource.getJdbcUrl();
		String databaseUsername = dataSource.getUser();
		String databasePassword = dataSource.getPassword();
		Connection connection = null;
        Statement stmt = null;
        ResultSet resultSet = null;

    	connection = DriverManager.getConnection(jdbcUrl, databaseUsername, databasePassword); 
    	connection.setAutoCommit(false);//禁止自动提交，设置回滚点
    	stmt =connection.createStatement();
    	
    	JSONObject returnObj = new JSONObject(); 
		boolean isCorrect=false;
        try {
        	if(jsons!=null&&jsons.length>0){
        		for (String jsonString : jsons) {
        			
        	        JSONObject jsonObj = new JSONObject(jsonString);
        			Long formId = jsonObj.getLong("formId"); 
        			Form form = (Form)super.findObjByKey(Form.class, formId);  
        			 
        	     	String dataIdstr = jsonObj.getString("dataIds");
                	String deletesql="delete from "+form.getFormName() +" where id in ("+dataIdstr+")";
            		stmt.executeUpdate(deletesql); 
    			}
        	}
    		connection.commit(); //事务提交
        	isCorrect=true;
		} catch (Exception e) {
			e.printStackTrace();
			connection.rollback(); //操作不成功则回滚 
		}finally {
			try {
				if(resultSet != null) {
					resultSet.close();
					resultSet = null;
				}
				if(stmt  != null) {
					stmt .close();
					stmt  = null;
				}
				if(connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		returnObj.put("isCorrect", isCorrect);
		return returnObj;
	}
	
	public void saveTestDataJson(String param) throws SQLException{
		
		
		String jdbcUrl=dataSource.getJdbcUrl();
		String databaseUsername=dataSource.getUser();
		String databasePassword=dataSource.getPassword();
		Connection connection = null;
        Statement stmt  = null;
        ResultSet resultSet = null;
        
        try {
        	connection = DriverManager.getConnection(jdbcUrl, databaseUsername, databasePassword); 
        	connection.setAutoCommit(false);//禁止自动提交，设置回滚点
        	stmt =connection.createStatement();
        	
        	String sql="UPDATE  deco_test  SET test_2="+param+" where id=22";
    		stmt.executeUpdate(sql);
    		this.callShellProcedure(connection, param);
    		
    		connection.commit(); //事务提交 
		} catch (Exception e) {
			e.printStackTrace();
			connection.rollback(); //操作不成功则回滚 
		}finally {
			try {
				if(resultSet != null) {
					resultSet.close();
					resultSet = null;
				}
				if(stmt  != null) {
					stmt .close();
					stmt  = null;
				}
				if(connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}   
	 
		
	}

	//根据formId查询当前tableDate 
	public JSONObject getDataByFormId(Map<String,String> map,String parameter,PageTrace pageTrace,String byIdconfig ,int type){
		String formCode= map.get("formCode").toString();
		String dataId=null;
		if(map.get("dataId")!=null)
			dataId= map.get("dataId").toString();
		Form form=this.findByFormName(formCode);
		String parameter1="";
		if(dataId!=null&&!dataId.trim().equals(""))
			parameter1+=" id ="+ dataId+"  ";
		if(map.get("uuId")!=null){
			String uuId= map.get("uuId").toString();
			if(StringUtils.isNotBlank(uuId))
				parameter1+=" and  ";
			parameter1+="  uuId ='"+ uuId+"' ";
		}
		
		if(map.get("modifyDate")!=null){
			String modifyDate= map.get("modifyDate").toString();
			if(StringUtils.isNotBlank(modifyDate))
				parameter1+=" and  ";
			parameter1+="  modifyDate >= FROM_UNIXTIME("+modifyDate +")";
		}
	    List<BizValue> listBizValue = bizTypeService.getBizValuesByCode(SelectConditions.ISESCAPE);
		if (type==1 && listBizValue != null && listBizValue.size() > 0) {
 			String value = listBizValue.get(0).getDisplayValue();
 			if(StringUtils.isNotBlank(value)&& value.equals("1") ){
 				if(parameter.toLowerCase().indexOf(" where ")>0||
 						parameter.toLowerCase().indexOf(" select ")>0||
 						parameter.toLowerCase().indexOf(" count")>0||
 						parameter.toLowerCase().indexOf(" and ")>0||
 						parameter.toLowerCase().indexOf(" count ")>0||
 						parameter.toLowerCase().indexOf(" or ")>0||
 						parameter.toLowerCase().indexOf(" exists ")>0||
 						parameter.toLowerCase().indexOf("delete")>0||
 						parameter.toLowerCase().indexOf("drop")>0){ 
 					try {					
 	 					JSONObject saveInfo = new JSONObject();
 	 					JSONObject register = new JSONObject();
 	 					saveInfo.put("formId", 97);
 	 					register.put("code", formCode);
 	 					register.put("type", 1);
 	 					register.put("selectConditionSql",parameter);
 	 					register.put("remark", "getDataByFormId");
						saveFormDataJson(saveInfo.toString());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
 					 return null;
 				}
 				List<BizValue> keyBizValueList = bizTypeService.getBizValuesByCode(SelectConditions.KEYWORDS);
 		 		if (keyBizValueList != null && keyBizValueList.size() > 0) {
 		 			for (int i = 0; i < keyBizValueList.size(); i++) {
 		 				BizValue bizValue=keyBizValueList.get(i); 
 		 				parameter=parameter.replace(bizValue.getRemark(), " "+bizValue.getDisplayValue()+" ");
					}
 		 		}
 			}
 		}
		if(parameter!=null&&!parameter.trim().equals(""))
			parameter1+=parameter;
		String parameter2=" where  "+parameter1;
		String formTablesql= MySQLZDialect.selectData(formCode, parameter2);
		Map<String,String> sqlMap=new HashMap<String, String>();
		StringBuilder	sqlBuilder=new StringBuilder();
		sqlBuilder.append(" FROM  "+formCode+" " );
		if(parameter2!=null&&!parameter2.trim().equals(""))
			sqlBuilder.append(parameter2);
		sqlMap.put("sqlForm",sqlBuilder.toString());
		if(map.get("orderBy")!=null)
			formTablesql+="  " +map.get("orderBy");
		else
			formTablesql+="  order by modifyDate ,id ";
		
		System.out.println(formTablesql);
		sqlMap.put("sql",formTablesql);
		
		Map<String,String> fieldMap=new HashMap<String, String>(); 
		List<FormProperty> formPropertyList = form.getPropertyList();		
		if(formPropertyList!=null){
			fieldMap.put("id","id");
			fieldMap.put("createDate","createDate");
			fieldMap.put("createBy","createBy");
			fieldMap.put("modifyDate","modifyDate");
			fieldMap.put("modifyBy","modifyBy");
			fieldMap.put("nowDate","nowDate");
			for (FormProperty formProperty : formPropertyList) {
				fieldMap.put(formProperty.getFieldName(), formProperty.getFieldName()); 
			}
		}
		try {
			return  SelectToJson.jdbcTemplateQuerysql(jdbcTemplate, sqlMap, fieldMap, pageTrace, byIdconfig);
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	 
	
	/**
	 * 同步数据
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	public Long saveFormDataJsonSyn(String jdbcUrl,String databaseUsername,String databasePassword,String jsonString) throws Exception{
		/*String jdbcUrl=dataSource.getJdbcUrl();
		String databaseUsername=dataSource.getUser();
		String databasePassword=dataSource.getPassword();
		*/
		Connection connection = null;
        Statement stmt  = null;
        ResultSet resultSet = null;
        
        JSONObject jsonObj = new JSONObject(jsonString);
		Long formId = jsonObj.getLong("formId");
		Form form = (Form)super.findObjByKey(Form.class, formId);
		JSONObject tempJsonObj = jsonObj.getJSONObject("register");
		if(jsonObj.has("generateCode")&&jsonObj.has("fieldName")){
			String generateCode=jsonObj.getString("generateCode");
			if(jsonObj.has("fieldName")){
				String generateCodeVal= springJdbcService.getGenerateCode(generateCode);
				tempJsonObj.put(jsonObj.getString("fieldName"), generateCodeVal);	
			}
		}
		ZTable ztable = this.ztableService.getZTableByName(form.getFormName());
		//1.得到form中所有的属性
		List<FormProperty> formPropertyList = form.getPropertyList();
		/**保存form表的数据**/
		Long formNextId=0l;
		int isSuccess=0;
        try {
        	connection = DriverManager.getConnection(jdbcUrl, databaseUsername, databasePassword); 
        	connection.setAutoCommit(false);//禁止自动提交，设置回滚点
        	stmt =connection.createStatement();
        	if(formPropertyList != null) { 
    			if(jsonObj.has("tableDataId"))
    			{
    				formNextId = jsonObj.getLong("tableDataId");
    				/**保存form表的数据**/
    				boolean isSave=isSaveForm(jsonObj);
    				if(isSave)
    					ExecutionSql.updateFormDataConn(stmt,formPropertyList, tempJsonObj, formNextId, ztable);
    			}else {
    				//3.得到存储Form对象表的下一个ID 
    				//formNextId = ExecutionSql.getnextId(jdbcTemplate,form.getFormName());
    				formNextId = ExecutionSql.getnextId(stmt, resultSet, form.getFormName());
    				ExecutionSql.insertFormDataConn(stmt,formPropertyList, tempJsonObj, formNextId, ztable,form.getIshistoryTable());
    			}
    		}
    		if(jsonObj.has("detail")){
    			insertFormDataDetailConn(stmt,resultSet,jsonObj, formNextId,1);
    		}
    		connection.commit(); //事务提交
    		isSuccess=1;
		} catch (Exception e) {
			e.printStackTrace();
			connection.rollback(); //操作不成功则回滚
			formNextId=0l;
		}finally {
			try {
				if(resultSet != null) {
					resultSet.close();
					resultSet = null;
				}
				if(stmt  != null) {
					stmt .close();
					stmt  = null;
				}
				if(connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}   
		String insertJsonObjSql=" insert into zflow_saveFormDataJson_log(formDataId,jsonObj,saveType,isSuccess,createTime) values ('"+formNextId+"','"+jsonString+"',1,"+isSuccess+",NOW())  ";
		ExecutionSql.execution(jdbcTemplate, insertJsonObjSql);
		return formNextId;
	}
	/**
	 * 保存同步数据
	 * @param jdbcUrl
	 * @param databaseUsername
	 * @param databasePassword
	 * @param jsons
	 * @return
	 * @throws Exception
	 */
	public JSONObject saveFormDataJsonSyn(String jdbcUrl,String databaseUsername,String databasePassword,String[] jsons) throws Exception{ 
		/*String jdbcUrl = dataSource.getJdbcUrl();
		String databaseUsername = dataSource.getUser();
		String databasePassword = dataSource.getPassword();*/
		Connection connection = null;
        Statement stmt = null;
        ResultSet resultSet = null;
    	connection = DriverManager.getConnection(jdbcUrl, databaseUsername, databasePassword); 
    	connection.setAutoCommit(false);//禁止自动提交，设置回滚点
    	stmt =connection.createStatement();
    	JSONObject returnObj = new JSONObject(); 
		boolean isCorrect=false;
        try {
        	if(jsons!=null&&jsons.length>0){
        		for (String jsonString : jsons) {
        	        JSONObject jsonObj = new JSONObject(jsonString);
        			Long formId = jsonObj.getLong("formId"); 
        			Form form = (Form)super.findObjByKey(Form.class, formId);
        			ZTable ztable = this.ztableService.getZTableByName(form.getFormName());
        			//1.得到form中所有的属性
        			List<FormProperty> formPropertyList = form.getPropertyList();
        			/**保存form表的数据**/
        			Long formNextId=0l; 

                	if(formPropertyList != null) { 
            			if(jsonObj.has("tableDataId"))
            			{
            				formNextId = jsonObj.getLong("tableDataId");
            				/**保存form表的数据**/
            				boolean isSave=isSaveForm(jsonObj);
            				if(isSave){
                    			JSONObject tempJsonObj = jsonObj.getJSONObject("register");
                    			ExecutionSql.updateFormDataConn(stmt,formPropertyList, tempJsonObj, formNextId, ztable);
            				}
            					
            			}else {
            				//3.得到存储Form对象表的下一个ID 
            				formNextId = ExecutionSql.getnextId(jdbcTemplate,form.getFormName());
                			JSONObject tempJsonObj = jsonObj.getJSONObject("register");
                			if(jsonObj.has("generateCode")&&jsonObj.has("fieldName")){
                				String generateCode=jsonObj.getString("generateCode");
                				if(jsonObj.has("fieldName")){
                					String generateCodeVal= springJdbcService.getGenerateCode(generateCode);
                					tempJsonObj.put(jsonObj.getString("fieldName"), generateCodeVal);	
                				}
                			}
            				ExecutionSql.insertFormDataConn(stmt,formPropertyList, tempJsonObj, formNextId, ztable,form.getIshistoryTable());
            			}
            		}
            		if(jsonObj.has("detail")){
            			insertFormDataDetailConn(stmt,resultSet,jsonObj, formNextId,1);
            		}
            		String insertJsonObjSql=" insert into zflow_saveFormDataJson_log(formDataId,jsonObj,saveType,isSuccess,createTime) values ('"+formNextId+"','"+jsonString+"',2,1,NOW())  ";
            		ExecutionSql.execution(jdbcTemplate, insertJsonObjSql);
            		
            		returnObj.put(form.getFormName(), formNextId);
    			}
        	}
    		connection.commit(); //事务提交
        	isCorrect=true;
		} catch (Exception e) {
			e.printStackTrace();
			connection.rollback(); //操作不成功则回滚 
		}finally {
			try {
				if(resultSet != null) {
					resultSet.close();
					resultSet = null;
				}
				if(stmt  != null) {
					stmt .close();
					stmt  = null;
				}
				if(connection != null) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}  

		returnObj.put("isCorrect", isCorrect);
		return returnObj;
	}
	
}
