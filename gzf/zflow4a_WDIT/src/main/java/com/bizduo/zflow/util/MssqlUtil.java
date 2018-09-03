package com.bizduo.zflow.util;

import java.text.SimpleDateFormat;
import java.util.List;

import com.bizduo.zflow.domain.importdata.ImportColumn;


public class MssqlUtil {

	public static boolean checkFieldType(int type, String value){
		boolean state;
		SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		try{
			switch(type){
				case 34://image 34
				case 35://text 35
				case 36://uniqueidentifier 36
					state = false;
					break;
				case 40://date 40
					sdf2.parse(value);
					state = true;
					break;
				case 41://time 41
				case 42://datetime2 42
				case 43://datetimeoffset 43
					state = false;
					break;
				case 48://tinyint 48
				case 52://smallint 52
				case 56://int 56
					Integer.parseInt(value);
					state = true;
					break;
				case 58://smalldatetime 58
				case 59://real 59
					state = false;
					break;
				case 60://money 60
					Double.parseDouble(value);
					state = true;
					break;
				case 61://datetime 61
					sdf1.parse(value);
					state = true;
					break;
				case 62://float 62
					Float.parseFloat(value);
					state = true;
					break;
				case 98://sql_variant 98
				case 99://ntext 99
					state = false;
					break;
				case 104://bit 104
					Boolean.parseBoolean(value);
					state = true;
					break;
				case 106://decimal 106
				case 108://numeric 108
					Double.parseDouble(value);
					state = true;
					break;
				case 122://smallmoney 122
					Float.parseFloat(value);
					state = true;
					break;
				case 127://bigint 127
					Long.parseLong(value);
					state = true;
					break;
				case 128://hierarchyid 128 
				case 129://geometry 129 
				case 130://geography 130
				case 165://varbinary 165
					state = false;
					break;
				case 167://varchar 167
					state = true;
					break;
				case 173://binary 173
					state = false;
					break;
				case 175://char 175
					state = true;
					break;
				case 189://timestamp 189
					sdf0.parse(value);
					state = true;
					break;
				case 231://nvarchar 231
				case 239://nchar 239
					state = true;
					break;
				case 241://xml 241
				case 256://sysname 256
					state = false;
					break;
				default :
					state = false;
					break;
			}
		}catch(Exception e){
			state = false;
		}
		return state;
	}

	public static String getSql(String tablename, List<ImportColumn> cols, String[] value, String batchNo) {
		StringBuilder sql1 = new StringBuilder();
		StringBuilder sql2 = new StringBuilder();
		sql1.append("INSERT INTO ").append(tablename).append("(");
		sql2.append(" VALUES('");
		for(int i = 0; i < cols.size(); i++){
//			if(i == (cols.size() - 1)){
//				sql1.append(cols.get(i).getName()).append(")");
//				sql2.append(null == value[i] ? "" : value[i]).append("')");
//			}else{
				sql1.append(cols.get(i).getName()).append(",");
				sql2.append(null == value[i] ? "" : value[i]).append("','");
//			}
		}
		sql1.append("[BatchNo]").append(")");
		sql2.append(batchNo).append("')");
		return sql1.append(sql2).toString();
	}
	
	/**
     * 将EXCEL中A,B,C,D,E列映射成0,1,2,3
     * @param col
     * @return
     */
    public static int getExcelCol(String col){
       col = col.toUpperCase();     
       //从-1开始计算,字母重1开始运算。这种总数下来算数正好相同。
       int count = -1;
       char[] cs = col.toCharArray();
       for(int i = 0; i < cs.length; i++){
           count += (cs[i] - 64 ) * Math.pow(26, cs.length - 1 - i);
       }
       return count;
    }
}
