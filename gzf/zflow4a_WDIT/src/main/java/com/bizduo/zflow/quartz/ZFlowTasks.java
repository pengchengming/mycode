package com.bizduo.zflow.quartz;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bizduo.zflow.domain.sys.OperateLog;

public class ZFlowTasks {

	protected final Log log = LogFactory.getLog(TimingBusiness.class);  
	/*
	@Autowired
	private IUserService userService;
	@Autowired
	private IOperateLogService operateLogService;
	@Autowired
	private IEmployeeService employeeService;*/	
 
	 
    @SuppressWarnings("unused")
	public void checkZflowTask() throws Exception {
    	

    	Date begintime =new Date(); 

    	//aliyunOSSService.saveOSSFiles();

    	OperateLog operatelog = new OperateLog(
    			"9901"//String opcode
    			,"1"// String opresult
    			,"定时任务执行成功" //String opdesc
    			,null//String opkey1
    			,null//String opkey2
    			,null //String opkey3
    			,null //String opkey4
    			,""//String mac
    			,""// String ipaddress
    			,begintime//Date beginTime
    			,new Date()// Date endTime
    			,new Date()// Date createDate
    			,"ZflowTask"// String createByName
    			); 

    	
    }

 
	
	
	
	
}
