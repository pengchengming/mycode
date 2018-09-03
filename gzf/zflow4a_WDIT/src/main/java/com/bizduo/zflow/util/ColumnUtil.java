package com.bizduo.zflow.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bizduo.zflow.domain.table.ZColumn;

public class ColumnUtil {

	public static String colTypeDefinition(ZColumn column){
//		ColType colType,Long column.getLength(),Boolean column.getUnsigned(),
//		Boolean column.getZerofill(),Long column.getDecimals(),Boolean column.getBinary(),Boolean column.getAscii(),Boolean unicode
		String ret;
		switch(column.getColType()){
		case TINYINT:
			ret="TINYINT ("+column.getLength()+") "
			+(column.getUnsigned()?"column.getUnsigned()":"")+" " 
			+(column.getZerofill()?"column.getZerofill()":"");
			return ret;
		case SMALLINT: // | SMALLINT[(column.getLength())] [column.getUnsigned()] [column.getZerofill()]
			ret="SMALLINT ("+column.getLength()+") "
			+(column.getUnsigned()?"column.getUnsigned()":"")+" " 
			+(column.getZerofill()?"column.getZerofill()":"");
			return ret;
		case MEDIUMINT:  // | MEDIUMINT[(column.getLength())] [column.getUnsigned()] [column.getZerofill()]
			ret="MEDIUMINT ("+column.getLength()+") "
			+(column.getUnsigned()?"column.getUnsigned()":"")+" " 
			+(column.getZerofill()?"column.getZerofill()":"");
			return ret;
		case INT:  // | INT[(column.getLength())] [column.getUnsigned()] [column.getZerofill()]
			String database="SQL";
			if(database.trim().equals("SQL")) 
				ret="INT ";
			else 
				ret="INT ("+column.getLength()+") ";
			ret+=(column.getUnsigned()?"column.getUnsigned()":"")+" " 
			+(column.getZerofill()?"column.getZerofill()":"");
			return ret;
		case INTEGER:  //   | INTEGER[(column.getLength())] [column.getUnsigned()] [column.getZerofill()]
			ret="INTEGER ("+column.getLength()+") "
			+(column.getUnsigned()?"column.getUnsigned()":"")+" " 
			+(column.getZerofill()?"column.getZerofill()":"");
			return ret;
		case BIGINT:  //  | BIGINT[(column.getLength())] [column.getUnsigned()] [column.getZerofill()]
//			ret="BIGINT ("+column.getLength()+") "
//			+(column.getUnsigned()?"column.getUnsigned()":"")+" " 
//			+(column.getZerofill()?"column.getZerofill()":"");
			ret=" BIGINT  " ;
			return ret;
		case REAL:  // | REAL[(column.getLength(),column.getDecimals())] [column.getUnsigned()] [column.getZerofill()]
			ret="REAL ("+column.getLength()+") "
			+(column.getUnsigned()?"column.getUnsigned()":"")+" " 
			+(column.getZerofill()?"column.getZerofill()":"");
			return ret;
		case DOUBLE:  // | DOUBLE[(column.getLength(),column.getDecimals())] [column.getUnsigned()] [column.getZerofill()]
			ret="DOUBLE ("+column.getLength()+",2) "
			+(column.getUnsigned()?"column.getUnsigned()":"")+" " 
			+(column.getZerofill()?"column.getZerofill()":"");
			return ret;
		case FLOAT:  // | FLOAT[(column.getLength(),column.getDecimals())] [column.getUnsigned()] [column.getZerofill()]
			ret="FLOAT ("+column.getLength()+","+column.getDecimals()+") "
			+(column.getUnsigned()?"column.getUnsigned()":"")+" " 
			+(column.getZerofill()?"column.getZerofill()":"");
			return ret;
		case DECIMAL:   // | DECIMAL(column.getLength(),column.getDecimals()) [column.getUnsigned()] [column.getZerofill()]
			ret="DECIMAL ("+column.getLength()+","+column.getDecimals()+") "
			+(column.getUnsigned()?"column.getUnsigned()":"")+" " 
			+(column.getZerofill()?"column.getZerofill()":"");
			return ret;
		case NUMERIC:   // | NUMERIC(column.getLength(),column.getDecimals()) [column.getUnsigned()] [column.getZerofill()]
			ret="NUMERIC ("+column.getLength()+","+column.getDecimals()+") "
			+(column.getUnsigned()?"column.getUnsigned()":"")+" " 
			+(column.getZerofill()?"column.getZerofill()":"");
			return ret;
		case DATE:   // | DATE
			ret="DATE ";
			return ret;
		case TIME:   //  | TIME
			ret="TIME ";
			return ret;
		case TIMESTAMP:   // | TIMESTAMP
			ret="TIMESTAMP ";
			return ret;
		case DATETIME:   //| DATETIME
			ret="DATETIME ";
			return ret;
		case CHAR:   //   | CHAR(column.getLength()) [column.getBinary() | column.getAscii() | UNICODE]
			ret="CHAR ("+column.getLength()+") "
			+(column.getBinary()?"column.getBinary()":"")+" " 
			+(column.getAscii()?"column.getAscii()":"")+" "
			+(column.getUnicode()?"UNICODE":"");
			return ret;
		case VARCHAR:    // | VARCHAR(column.getLength()) [column.getBinary()]
			ret="VARCHAR ("+column.getLength()+") "
			+(column.getBinary()?"column.getBinary()":"");
			return ret;
		case TINYBLOB:   //| TINYBLOB
			ret="TINYBLOB ";
			return ret;
		case BLOB:   //| BLOB
			ret="BLOB ";
			return ret;
		case MEDIUMBLOB: //MEDIUMBLOB
			ret="MEDIUMBLOB ";
			return ret;
		case LONGBLOB:   //| LONGBLOB
			ret="LONGBLOB ";
			return ret;
		case TINYTEXT: //  | TINYTEXT [column.getBinary()]
			ret="TINYTEXT "
			+(column.getBinary()?"column.getBinary()":"");
			return ret;
		case MEDIUMTEXT: // | MEDIUMTEXT [column.getBinary()]
			ret="MEDIUMTEXT   "
			+(column.getBinary()?"column.getBinary()":"");
			return ret;
		case TEXT: //| TEXT [column.getBinary()]
			ret="TEXT  "
			+(column.getBinary()?"column.getBinary()":"");
			return ret;
		case LONGTEXT: //   | LONGTEXT [column.getBinary()]
//			ret="LONGTEXT "
//			+(column.getBinary()?"column.getBinary()":"");
			ret="text ";
			return ret;
		default: 
			return "";
		}

	}
	
	public String getCreateDateStr(Date createDate){
		 SimpleDateFormat dataFm=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		 return  dataFm.format(createDate);
	}
}
