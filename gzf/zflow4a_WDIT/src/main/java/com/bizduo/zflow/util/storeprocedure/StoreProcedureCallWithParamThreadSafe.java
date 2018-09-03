package com.bizduo.zflow.util.storeprocedure;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
  
public abstract class StoreProcedureCallWithParamThreadSafe<P, T> extends SpCallableStatementCallback<T> {  
      
    private JdbcTemplate jdbcTemplate;  
      
    public StoreProcedureCallWithParamThreadSafe() {  
    }  
      
    public StoreProcedureCallWithParamThreadSafe(JdbcTemplate jdbcTemplate) {  
        this.jdbcTemplate = jdbcTemplate;  
    }  
      
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {  
        this.jdbcTemplate = jdbcTemplate;  
    }  
      
    protected abstract CallableStatement createCallableStatement(Connection con, P inParamObj) throws SQLException ;  
  
    protected CallableStatementCreator createCallableStatement(final P inParamObj) {  
        return new CallableStatementCreator() {  
            public CallableStatement createCallableStatement(Connection con) throws SQLException {  
                return StoreProcedureCallWithParamThreadSafe.this.createCallableStatement(con, inParamObj);  
            }  
        };  
    }  
      
    public T call(P inputParameterObject){  
        return jdbcTemplate.execute(this.createCallableStatement(inputParameterObject), this);  
    }  
      
}  
