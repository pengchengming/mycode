import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.bizduo.zflow.domain.sys.Organization;
import com.bizduo.zflow.domain.sys.Role;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.service.sys.IOrganizationService;
import com.bizduo.zflow.service.sys.IRoleService;
import com.bizduo.zflow.service.sys.IUserService;
import com.bizduo.zflow.status.OrganType;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false) 
public class UserTest {
	@Autowired
	private IUserService userService;
	@Autowired
	private IOrganizationService organizationService;
	@Autowired
	private IRoleService roleService;
	
	@Test
    public void createUsers() throws Exception{
		User user=new User();
		for (int i = 1; i <=500; i++) {
			String istr="0";
			if(i+"".length()==1){
				istr="00";
			}else if(i+"".length()==2){
				istr="0";
			}
			user.setUsername("test0"+istr+i);
			user.setPassword("111111");
			user.setTel("18616786080");
			user.setEmail("user0"+istr+i+"@jiajuist.com");
			 
		}
		 
		user.setOrganization(this.organizationService.findObjByKey(Organization.class, 2)); 
		Role role = roleService.findObjByKey(Role.class, 2);
		List<Role> roles = new ArrayList<Role>();	
		roles.add(role);
		user.setRoles(roles);
		try{
			userService.createUser(user, true); 
		}catch(Exception e){
			e.printStackTrace(); 
		} 
    } 
}
