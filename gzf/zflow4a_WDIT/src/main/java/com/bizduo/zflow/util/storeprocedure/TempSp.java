package com.bizduo.zflow.util.storeprocedure;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;



public class TempSp extends StoreProcedureCallWithParamThreadSafe<TempSpInParam, TempSpResult>{  
    //   
    private String spSql = "{? = call tempdb..mathtutor2 (?,?,?)}";  
      
    @Override  
    protected CallableStatement createCallableStatement(Connection con, TempSpInParam inParamObj) throws SQLException {  
        CallableStatement cs = (CallableStatement) con.prepareCall(spSql);  
        cs.registerOutParameter(1, java.sql.Types.INTEGER);  
        cs.setInt(2, inParamObj.getA());  
        cs.setInt(3, inParamObj.getB());  
        cs.registerOutParameter(4, java.sql.Types.INTEGER);  
        return cs;  
    }  
      
    @Override  
    protected TempSpResult creatResultObject() {  
        return new TempSpResult();  
    }  
      
    // 把所有结果集都放入t list中。这里没有加以区分，实际代码通常应该对resultSetNum 进行case switch，对不同的结果集分别处理。  
    @Override  
    protected void handleResultSetMapping(int resultSetNum, ResultSet rs, int rowNum, TempSpResult t) throws SQLException {  
        System.out.println(String.format("resultSetNum=%s",  resultSetNum));  
        t.getList().add(rs.getInt(1));  
    }  
  
    @Override  
    protected void handleOutParameter(CallableStatement cs, TempSpResult t) throws SQLException {  
        t.setReturnVal(cs.getInt(1));  
        t.setOutVal(cs.getInt(4));  
    }
 
}  