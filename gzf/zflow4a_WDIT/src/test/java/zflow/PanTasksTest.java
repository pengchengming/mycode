package zflow;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.web.bind.annotation.RequestParam;

import com.aliyun.openservices.oss.model.ListObjectsRequest;
import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.aliyun.openservices.oss.model.ObjectListing;
import com.bizduo.zflow.domain.sys.Employee;
import com.bizduo.zflow.service.sys.IEmployeeService;
import com.bizduo.zflow.service.sys.IUserService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false) 
public class PanTasksTest extends AbstractJUnit4SpringContextTests {

	@Test
	public void test() {
		try {
			checkObjects();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Autowired
	private IUserService userService;
	@Autowired
	private IEmployeeService employeeService;	
	
		
    public void checkObjects() throws Exception { 
		String bucketName="jahwa";
		
		//保存当前文件 
		com.aliyun.openservices.oss.OSSClient ossClient;
		com.aliyun.openservices.oss.model.PutObjectResult  result;
		
        String accessid = "GpJqxQDmKAa7pouT";          // AccessID
        String accesskey = "8ccWp2nkm5qW8m15d7pIPMI23JLsRS";     // AccessKey
//        String bucketName ="jahwa";		
        ossClient = new com.aliyun.openservices.oss.OSSClient(accessid, accesskey); //当然这里可以封装下
        ListObjectsRequest r = new ListObjectsRequest(bucketName);
        
        HashMap hashmap = new HashMap();
        HashMap usedSizeHashmap = new HashMap();
        
        StringBuffer sbText  = new StringBuffer();
        sbText.append("{");
        sbText.append("\"code\":1, \"results\": [");
        
        //查出制定范围的人
        List<Employee> employeelist = employeeService.getByRealName("");// employeeService.getByOpenId(openId);
        
        for (Employee employee : employeelist) {
        	employee.setUsedDiskSize(0.0000);
        	hashmap.put(employee.getOpenId(), employee);
        	//usedSizeHashmap.put(employee.getOpenId(), new Long(0));
        }
        r.setPrefix("");
        ObjectListing objects  = ossClient.listObjects(r);
        String filename="";
        String useropenid="";
        
        
        do{  
            for (OSSObjectSummary  objectSummary : objects .getObjectSummaries()){
            	if(objectSummary.getKey().lastIndexOf("/")>0)
            	{
            	String openId = objectSummary.getKey().substring(0, objectSummary.getKey().lastIndexOf("/"));
            	Employee employee = (Employee)hashmap.get(openId);
            	if(employee==null) continue;

            	if(objectSummary.getKey().lastIndexOf("/")>0){
                	filename =objectSummary.getKey().substring(objectSummary.getKey().lastIndexOf("/")+1);
                	useropenid =objectSummary.getKey().substring(1,objectSummary.getKey().lastIndexOf("/"));                	
                }
                
            	//usedSizeHashmap.put(openId, ((Long)usedSizeHashmap.get(openId))+objectSummary.getSize());
            	employee.setUsedDiskSize(employee.getUsedDiskSize()+objectSummary.getSize());
            	hashmap.put(openId, employee);
            	}
            }                                 
//            objects = ossClient.listObjects(objects);  
            
        } while (objects.isTruncated());    
        
        Iterator iterator = hashmap.keySet().iterator();
        while(iterator.hasNext()) {
        	Employee employee =(Employee)hashmap.get(iterator.next());
        	employeeService.update(employee);
        }
        
        
	}
	
	
	

}
