package com.bizduo.zflow.util.storeprocedure;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)  
public class TestSample //extends GenericTestCase 
{  
  
    @Autowired  
    protected JdbcTemplate jdbcTemplate;  
  
    @Test  
    public void test1() {  
        TempSp tempSp = new TempSp();  
        tempSp.setJdbcTemplate(jdbcTemplate);  
        System.out.println(tempSp.call(new TempSpInParam(2, 3)));  
    }  
      
}  