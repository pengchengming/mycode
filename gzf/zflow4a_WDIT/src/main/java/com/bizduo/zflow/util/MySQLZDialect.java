package com.bizduo.zflow.util;

import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bizduo.common.module.dao.PageTrace;
import com.bizduo.zflow.domain.table.ZColumn;
import com.bizduo.zflow.domain.table.ZTable;
import com.bizduo.zflow.domain.tableData.ZCloumnData;

public class MySQLZDialect {
	
	public static String createTable(String tableName, Collection<ZColumn> zcolumns){
		 //创建表(总方法)
	   StringBuilder createTableSql = new StringBuilder();
       createTableSql.append("CREATE  TABLE " +" ");
       if(tableName!=null&&zcolumns!=null&& zcolumns.size()>0){
    	   if(tableName!=null&&!tableName.equals("")){
    		   createTableSql.append(""+tableName+" ");
    	   }
       } 
	  //创建列
		String createSql= createColumn(tableName,zcolumns);
	  //创建索引 (没做)
		//createIndex();
	  //创建外键关联(没做)	
		//createReference();
		createTableSql.append(createSql);
		return createTableSql.toString() ;
	}
	
	
	//创建列
	private static String createColumn(String tableName, Collection<ZColumn> columnSet){
		StringBuilder createColumnSql = new StringBuilder();
		//Collection<ZColumn> columnSet = ztable.getZcolumns();
	    createColumnSql.append("(");
	    for(ZColumn column:columnSet){
	    	if(column.getForeignKey()!=null&&!column.getForeignKey().trim().equals("")){
	    		if("".equals("MYSQL")){
		    		createColumnSql.append("KEY "+column.getForeignKey()+tableName+" ("+column.getForeignColumn()+"_id),");
		    		createColumnSql.append("CONSTRAINT  "+column.getForeignKey()+tableName+"  FOREIGN KEY  ("+column.getForeignColumn()+"_id) REFERENCES "+column.getForeignColumn()+" (id),");
		    		createColumnSql.append(" "+column.getColName()+" ");
	    		}
	    		else if("SQL".equals("SQL")){
	    			createColumnSql.append(" FOREIGN KEY ("+column.getForeignColumn()+"_id) REFERENCES "+column.getForeignColumn()+"(id) , "); 
	    			createColumnSql.append(" "+column.getColName()+"  ");	
	    		} 
	    	}else {
	    		createColumnSql.append(" "+column.getColName()+"  ");
	    	}
	    	String  typeLength = ColumnUtil.colTypeDefinition(column);
    		createColumnSql.append(typeLength);
    		
	  	    	if(column.getMandatory()!=null&&column.getMandatory()){
	  	    		createColumnSql.append(" NOT NULL ");
	  	    	}
	  	    	createColumnSql.append(" ,");
	  	    	if(column.getPrimaryKey()!=null&&column.getPrimaryKey()){
	  	    		createColumnSql.append("primary key"+"( "+column.getColName()+" )");
	  	    		createColumnSql.append(" ,");
	  	    	}
		    
	    }
	    createColumnSql=createColumnSql.delete(createColumnSql.length()-1, createColumnSql.length());//去除最后一个逗号
		createColumnSql.append(")");
		return createColumnSql.toString();
	}
	
	
	//删除表
	public static String deleteTable(ZTable ztable){
//		DROP [TEMPORARY] TABLE [IF EXISTS] tbl_name [, tbl_name] ... [RESTRICT | CASCADE]
		StringBuilder deleteTableSql = new StringBuilder();
		deleteTableSql.append("DROP  TABLE  ");
		if(ztable!=null){
	    	   if(ztable.getTablename()!=null&&!ztable.getTablename().equals("")){
	    		   deleteTableSql.append(" "+ztable.getTablename()+" ");
	    	   }
	       }
		return deleteTableSql.toString();
	}
	
	//增加一列(不是主键)
	public static String addColumn(ZTable ztable){
		//ALTER TABLE `opponent_product` add crawl_params3 varchar(2000) default null COMMENT 'crawl parameters store'
		StringBuilder addColumnSql = new StringBuilder();
		addColumnSql.append("ALTER  TABLE  ");
		if(ztable!=null){
	    	   if(ztable.getTablename()!=null&&!ztable.getTablename().equals("")){
	    		   addColumnSql.append(" "+ztable.getTablename()+" ");
	    	   }
	       }
		addColumnSql.append("  ADD  COLUMN  ");
		Collection<ZColumn> columnSet = ztable.getZcolumns();
		for(ZColumn column:columnSet){
  	    	if(column.getPrimaryKey()!=null&&!column.getPrimaryKey()){
  	    		addColumnSql.append(" "+column.getColName()+" ");
  	  	    	String  typeLength = ColumnUtil.colTypeDefinition(column);
  	  	    	addColumnSql.append(typeLength);
  	  	    	if(column.getMandatory()!=null&&column.getMandatory()){
  	  	    		addColumnSql.append(" NOT NULL ");
  	  	    	}
  	  	    	addColumnSql.append(" ;");
  	    	}
		}
		return addColumnSql.toString();
	}
	
	//增加一列（是主键）
	public static String addColumnPrimary(ZTable ztableNew){
		StringBuilder addColumnPrimarySql = new StringBuilder();
		addColumnPrimarySql.append("ALTER  TABLE  ");
		if(ztableNew!=null){
	    	   if(ztableNew.getTablename()!=null&&!ztableNew.getTablename().equals("")){
	    		   addColumnPrimarySql.append(" "+ztableNew.getTablename()+" ");
	    	   }
	       }
		addColumnPrimarySql.append("  ADD  ");
		Collection<ZColumn> columnSet = ztableNew.getZcolumns();
		for(ZColumn column:columnSet){
			if(column.getPrimaryKey()!=null&&column.getPrimaryKey()){
				addColumnPrimarySql.append(" "+column.getColName()+" "+" "+ column.getColType()+"("+column.getLength()+")"+" PRIMARY KEY;");
		}
	 }
		return addColumnPrimarySql.toString();
  }	
	//删除一列（非主键）
	public static String deleteColumn(ZTable ztable){
		//alter table table_name drop字段名;
		StringBuilder deleteColumnSql = new StringBuilder();
		deleteColumnSql.append("ALTER TABLE  ");
		if(ztable!=null){
	    	   if(ztable.getTablename()!=null&&!ztable.getTablename().equals("")){
	    		   deleteColumnSql.append(" "+ztable.getTablename()+" ");
	    	   }
	       }
		deleteColumnSql.append(" DROP COLUMN ");
		Collection<ZColumn> columnSet = ztable.getZcolumns();
		for(ZColumn column:columnSet){
			if(column.getColName()!=null&&!column.getColName().equals(""))deleteColumnSql.append(" "+column.getColName()+" ");
		}
		return deleteColumnSql.toString();
	}
	
	//删除一列（主键）
	public static String deleteColumnPrimary(ZTable ztable){
		StringBuilder deleteColumnPrimarySql = new StringBuilder();
		deleteColumnPrimarySql.append("ALTER  TABLE  ");
		Collection<ZColumn> columnSetOld = ztable.getZcolumns();
		if(ztable!=null){
	    	   if(ztable.getTablename()!=null&&!ztable.getTablename().equals("")){
	    		   deleteColumnPrimarySql.append(" " +ztable.getTablename()+ " ");
	    	   }
	       }
		for(ZColumn column:columnSetOld){
			if(column.getPrimaryKey()){
				//先删除自增，再删除主键（规则）
				deleteColumnPrimarySql.append("  CHANGE  " + " " +column.getPrimary()+ " " + "  " +" " + column.getPrimary()+ " " +  "   int(10);");
			}
		}
		String deleteColumnPrimarySqltemp = "ALTER  TABLE   " + " " +ztable.getTablename() + " " +"  drop primary key;";
		return deleteColumnPrimarySql.toString() + deleteColumnPrimarySqltemp;
	}
	
	//修改一列（不是主键）
	public static String changeColumn(ZTable ztableOld, ZTable ztableNew){
		//ALTER TABLE `TableName`MODIFY COLUMN `FieldName`VARCHAR(14)
		StringBuilder changeColumnSql = new StringBuilder();
		Collection<ZColumn> columnSetNew = ztableNew.getZcolumns();
		Collection<ZColumn> columnSetOld = ztableOld.getZcolumns();
		changeColumnSql.append("ALTER  TABLE  ");
		if(ztableNew!=null){
	    	   if(ztableNew.getTablename()!=null&&!ztableNew.getTablename().equals("")){
	    		   changeColumnSql.append(" " +ztableNew.getTablename()+ " ");
	    	   }
	       }
		changeColumnSql.append("  CHANGE    ");
		 for(ZColumn column:columnSetNew){
			 for(ZColumn columnOld:columnSetOld){
			  if(column.getPrimaryKey()!=null&&!column.getPrimaryKey()){
				changeColumnSql.append(" "+columnOld.getColName()+"  "+" "+column.getColName()+"  ");
  	  	    	String  typeLength = ColumnUtil.colTypeDefinition(column);
  	  	    	changeColumnSql.append(typeLength);
  	  	    	if(column.getMandatory()!=null&&column.getMandatory()){
  	  	    	changeColumnSql.append(" NOT NULL ");
  	  	    	}
  	  	    	changeColumnSql.append(" ;");
  	    	}
		   }
	      }
		 return changeColumnSql.toString();
	}
	
    //修改一列（是主键）
  public static String changeColumnPrimary(ZTable ztableOld , ZTable ztableNew){
    String del=deleteColumnPrimary(ztableOld);
    String add=addColumnPrimary(ztableNew);
    String del2=deleteColumn(ztableOld);
    return del+add+del2;
    
  }
	
	/**插入数据
	 * @throws ParseException 
	 * @throws NumberFormatException */
	public  static String insertData(ZTable ztable,String tableName,List<ZCloumnData> zCloumnDataList) throws NumberFormatException, ParseException{
			StringBuilder	sqlBuilder=new StringBuilder();
			sqlBuilder.append("INSERT ");
			if(ztable!=null){
				if(ztable.getLowPrinority()) sqlBuilder.append("LOW_PRIORITY ");
				if(ztable.getDelayed())sqlBuilder.append("DELAYED ");
				if(ztable.getHighPriority())sqlBuilder.append("HIGH_PRIORITY ");
				if(ztable.getIgnore())sqlBuilder.append("IGNORE ");
				sqlBuilder.append("INTO  "+tableName+" ");
				String columns= getColumnsValue(tableName,zCloumnDataList);
				sqlBuilder.append(columns);
			}
			return sqlBuilder.toString();
	}
	/**
	 * 删除数据
	 * id 可以为空
	 * @return
	 */
	public static String deleteDate(ZTable ztable,Long id){
		StringBuilder	sqlBuilder=new StringBuilder();
		if(ztable!=null){
			sqlBuilder.append("DELETE ");
			if(ztable.getLowPrinority()!=null&&ztable.getLowPrinority()) sqlBuilder.append("LOW_PRIORITY ");
			if(ztable.getQuick()!=null&&ztable.getQuick())sqlBuilder.append("QUICK ");
			if(ztable.getIgnore()!=null&&ztable.getIgnore())sqlBuilder.append("IGNORE ");
			sqlBuilder.append("FROM  "+ztable.getTablename()+" ");
			if(id!=null)
				sqlBuilder.append(" WHERE  id ="+id);
			 if(ztable.getOrderBy()!=null&&ztable.getOrderBy().trim().equals(""))
				 sqlBuilder.append("ORDER BY "+ztable.getOrderBy());
			 if(ztable.getRowCount()!=null)
				 sqlBuilder.append("LIMIT "+ztable.getRowCount());
		}
		return sqlBuilder.toString();
	}
	
	
	/**
	 * 查询语句
	 * @param ztable  数据库中的表
	 * @param definition  查询条件
	 * @return
	 */
	public static String selectData(String tableName,String definition){
		//SELECT    [ALL | DISTINCT | DISTINCTROW ]      
		//[HIGH_PRIORITY]      [STRAIGHT_JOIN]      
		//[SQL_SMALL_RESULT] [SQL_BIG_RESULT] [SQL_BUFFER_RESULT]      [SQL_CACHE | SQL_NO_CACHE] [SQL_CALC_FOUND_ROWS]    
		//select_expr, ...    [INTO OUTFILE 'file_name' export_options      | INTO DUMPFILE 'file_name']    
		//[FROM table_references    [WHERE where_definition]    [GROUP BY {col_name | expr | position}      [ASC | DESC], ... [WITH ROLLUP]]   
		//[HAVING where_definition]    [ORDER BY {col_name | expr | position}      [ASC | DESC] , ...]    
		//[LIMIT {[offset,] row_count | row_count OFFSET offset}]    [PROCEDURE procedure_name(argument_list)]    [FOR UPDATE | LOCK IN SHARE MODE]]
		StringBuilder	sqlBuilder=new StringBuilder();
		sqlBuilder.append("SELECT *,now() as nowDate FROM  "+tableName+" " );
		if(definition!=null&&!definition.trim().equals(""))
			sqlBuilder.append(definition);
		return sqlBuilder.toString();
	}
	/**
	 * 获取查询分页的总个数
	 * @param ztable   数据库中的表
	 * @param definition   查询条件
	 * @return
	 */
	public static String selectDataCount(String tableName,String definition){
		StringBuilder	sqlBuilder=new StringBuilder();
		sqlBuilder.append("SELECT count(*) as counts  FROM  "+tableName+" " );
		if(definition!=null)
			sqlBuilder.append(definition);
		return sqlBuilder.toString();
	}
	/**
	 * 分页 查询语句
	 * @param ztable
	 * @param definition
	 * @return
	 */
	public static String selectDataPage(String tableName,String definition ,PageTrace pageTrace){
		StringBuilder	sqlBuilder=new StringBuilder();
		sqlBuilder.append(selectData(tableName, definition));
		int startPageIndex=0;
		int i= (pageTrace.getPageIndex()+1)*pageTrace.getRecordPerPage()-pageTrace.getRecordPerPage();
		if(i>0){
			startPageIndex=i;
		}
		 int endPageIndex= (pageTrace.getPageIndex()+1)*pageTrace.getRecordPerPage();
		 sqlBuilder.append("limit "+startPageIndex+" , "+endPageIndex);
		return sqlBuilder.toString();
	}
	
	
	/**
	 * 修改
	 * @param ztable
	 * @param columnMap
	 * @return
	 */
	public static String updateDataStr(ZTable ztable ,Map<String ,String> columnMap){
		StringBuilder	sqlBuilder=new StringBuilder();
		if(ztable!=null){
			sqlBuilder.append("UPDATE ");
			if(ztable.getLowPrinority()) sqlBuilder.append("LOW_PRIORITY ");
			if(ztable.getIgnore())sqlBuilder.append("IGNORE ");
			sqlBuilder.append(" "+ztable.getTablename()+"  SET");
			sqlBuilder.append(getUpdateValueMap(columnMap));
		}
		return sqlBuilder.toString();
	}
	/**
	 * 修改
	 * @param ztable
	 * @param columnMap
	 * @param definition
	 * @return
	 */
	public static String updateDateByDefInt(ZTable ztable ,Map<String ,String> columnMap,String definition) {
		StringBuilder	sqlBuilder=new StringBuilder();
		sqlBuilder.append(updateDataStr(ztable, columnMap));
		if(definition!=null)
			sqlBuilder.append(definition);
		return sqlBuilder.toString();
	}
	/**
	 * 修改definition orderBy 
	 * @param columnMap
	 * @return
	 */
	public static String updateDateByDefIntOrBy(ZTable ztable ,Map<String ,String> columnMap,String definition,String orderBy){
		StringBuilder	sqlBuilder=new StringBuilder();
		if(ztable!=null){
			sqlBuilder.append(updateDateByDefInt(ztable, columnMap, definition));
			if(orderBy!=null&&orderBy.trim().equals(""))
				sqlBuilder.append(" ORDER BY " +orderBy);
		}
		return sqlBuilder.toString();
	}

	/**
	 * 修改
	 * @param ztable
	 * @param columnMap
	 * @param definition
	 * @param orderBy
	 * @param rowCount
	 * @return
	 */
	public static String updateDateByDefIntOrByOrLimit(ZTable ztable ,Map<String ,String> columnMap,String definition,String orderBy,Integer rowCount){
		StringBuilder	sqlBuilder=new StringBuilder();
		if(ztable!=null){
			sqlBuilder.append(updateDateByDefIntOrBy(ztable, columnMap, definition, orderBy));
			if(rowCount!=null)
				sqlBuilder.append(" LIMIT " +rowCount);
		}
		return sqlBuilder.toString();
	}
	
	//拼接字段 值
	private static String  getColumnsValue(String tableName,List<ZCloumnData> zCloumnDataList) throws NumberFormatException, ParseException{
		//字段
		StringBuilder columnBuilder=new StringBuilder();
		columnBuilder.append(" ( ");
		//值
		StringBuilder valuesBuilder=new StringBuilder();
		valuesBuilder.append(" ( ");
		
		for( ZCloumnData zcloumnData: zCloumnDataList){
				String zcolumn=zcloumnData.getColname();
				if(zcolumn.equals("age")){
					System.out.println(zcolumn);
				}
				String value=zcloumnData.getValue();
				 if(value!=null ){
			          	columnBuilder.append(" "+zcolumn+" "+","); 
			           if(zcloumnData.getColType().equals("Long")){
			        	   valuesBuilder.append("'"+Long.parseLong(value)+"'"+",");
			           }else if(zcloumnData.getColType().equals("attrInt")||zcloumnData.getColType().equals("attrNumber")||zcloumnData.getColType().equals("attrFloat")){
			        	   if(value!=null&&!value.trim().equals("")){			        		   
			        		   valuesBuilder.append("'"+value+"'"+","); 
			        	   }else 
			        		   valuesBuilder.append("null"+",");
			           }else if(zcloumnData.getColType().equals("attrDate")){
			        	   if(value!=null&&!value.trim().equals("")){
			        		   //String valueDate=TimeUitl.getTimeLongTime(Long.parseLong(value),"yyyy-MM-dd").toString();//yyyy-MM-dd HH:mm:ss   
				        	   //valuesBuilder.append("'"+valueDate+"'"+",");
			        		   valuesBuilder.append("'"+value+"'"+","); 
			        	   }else 
			        		   valuesBuilder.append("null"+",");
			           }else{
			        	   if(value.indexOf("'")>1){
			        		   value= value.replaceAll("'", "''");
			        		   valuesBuilder.append("'"+value+"'"+",");  
			        	   }else {
			        		   valuesBuilder.append("'"+value+"'"+",");
			        	   }
			           }
				 }
	           System.out.println(zcolumn+"===="+value);
		}
		
		columnBuilder=columnBuilder.delete(columnBuilder.length()-1, columnBuilder.length());//去除最后一个,
		valuesBuilder=valuesBuilder.delete(valuesBuilder.length()-1, valuesBuilder.length());//去除最后一个,
		columnBuilder.append(" ) ");
		valuesBuilder.append(" ) ");
		columnBuilder.append(" VALUES ");
		columnBuilder.append(valuesBuilder);
		return columnBuilder.toString();
	}
	//修改拼接字符串
	public static String  updateColumnsValue(String tableName,List<ZCloumnData> zCloumnDataList){
		//字段
		StringBuilder columnBuilder=new StringBuilder();  
		columnBuilder.append("update " + tableName+ " set ");
		for( ZCloumnData zcloumnData: zCloumnDataList){
				String zcolumn=zcloumnData.getColname();
				String value=zcloumnData.getValue(); 
				 if(value!=null){
					 if(value.indexOf("'")>1){
		        		   value= value.replaceAll("'", "''"); 
		        	   } 
					 if(zcloumnData.getColType().equals("Long")){ 
						  if(value!=null&&!value.trim().equals(""))	
				          	columnBuilder.append("  "+zcolumn+" ='"+Long.parseLong(value)+"',");
						  else
							  columnBuilder.append("  "+zcolumn+" =null,");
			          }
					 else if(zcloumnData.getColType().equals("attrInt")||zcloumnData.getColType().equals("attrNumber")||zcloumnData.getColType().equals("attrFloat")){
			        	   if(value!=null&&!value.trim().equals("")){			        		   
			        		   columnBuilder.append("  "+zcolumn+" ="+value+",");
			        	   }else 
			        		   columnBuilder.append("  "+zcolumn+" =null,");
			           }
					 else if(zcloumnData.getColType().equals("attrDate")){
			        	   if(value!=null&&!value.trim().equals("")){
//					        	  String valueDate=TimeUitl.getQuerystartTime(Long.parseLong(value)).toString(); 
//					        	   if(value!=null)
//					        		   columnBuilder.append("   "+zcolumn+" ='"+valueDate+"',");
			        		   columnBuilder.append("   "+zcolumn+" ='"+value+"',"); 
			        	   }else{
			        		   columnBuilder.append("   "+zcolumn+" =null,");
			        	   }
			           }else {  
				          	columnBuilder.append("  "+zcolumn+" ='"+value+"',"); 
			           }
				 }else 
	        		   columnBuilder.append("  "+zcolumn+" =null,");
	          
		} 
		columnBuilder=columnBuilder.delete(columnBuilder.length()-1, columnBuilder.length());//去除最后一个 
		return columnBuilder.toString();
	}
	
	//update 修改拼装sql
	@SuppressWarnings("rawtypes")
	private static String getUpdateValueMap(Map<String,String> columnMap){
		StringBuilder columnBuilder=new StringBuilder();
		Set set =columnMap.entrySet();
	    Iterator it=set.iterator();
        while(it.hasNext()){
        	Map.Entry entry = (Map.Entry)it.next();          
            String zcolumn=entry.getKey().toString();          
            String value=entry.getValue().toString(); 
            columnBuilder.append(" "+zcolumn+" ='"+value+"',");//要判断类型是否要加""
        }
        columnBuilder=columnBuilder.delete(columnBuilder.length()-1, columnBuilder.length());//去除最后一个,
       return columnBuilder.toString();
	}  

}
