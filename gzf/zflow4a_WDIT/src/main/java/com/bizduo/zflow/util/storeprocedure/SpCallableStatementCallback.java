package com.bizduo.zflow.util.storeprocedure;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.support.JdbcUtils;
  
public abstract class SpCallableStatementCallback<T> implements CallableStatementCallback<T> {  
   
    public final T doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {  
        cs.execute();  
        T t= this.creatResultObject();  
        int resultSetNum = 0;  
        while (cs.getMoreResults()) {  
            handleResultSet(resultSetNum++, cs, t);  
        }  
        this.handleOutParameter(cs, t);  
        return t;  
    }  
      
    private void handleResultSet(int resultSetNum, CallableStatement cs, T t) throws SQLException {  
        ResultSet rs = cs.getResultSet();  
        int rowNum = 0;  
        while(rs.next()){  
            this.handleResultSetMapping(resultSetNum, rs, rowNum++, t);  
        }  
        JdbcUtils.closeResultSet(rs);  
    }  
      
    protected abstract T creatResultObject();  
      
    // int resultSetNum，一个存储过程返回多个结果集的时候，这个Num来区分结果集的index。  
    protected abstract void handleResultSetMapping(int resultSetNum, ResultSet rs, int rowNum, T t) throws SQLException;  
      
    protected abstract void handleOutParameter(CallableStatement cs, T t) throws SQLException;  
}  