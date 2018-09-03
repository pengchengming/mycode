package zflow;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bizduo.zflow.domain.sys.Organization;
import com.bizduo.zflow.domain.sys.Role;
import com.bizduo.zflow.domain.sys.User;


public class zcbdTest {

	  public static void main(String[] args)
	  { 
				User user=new User();
				for (int i = 1; i <=500; i++) {
					String istr="";
					if((i+"").length()==1){
						istr="00";
					}else if((i+"").length()==2){
						istr="0";
					}
					
					System.out.println(istr+i);
					/*
					user.setUsername("test0"+istr+i);
					user.setPassword("111111");
					user.setTel("18616786080");
					user.setEmail("user0"+istr+i+"@jiajuist.com");
					user.setRealname("测试"+istr+i);
					user.setOrganization(this.organizationService.findObjByKey(Organization.class, 2)); 
					 
					List<Role> roles = new ArrayList<Role>();	
					roles.add(role);
					user.setRoles(roles);*/
					try{
						 
					}catch(Exception e){
						e.printStackTrace(); 
					} 
				} 
		  
		  /*
	    String txt="${id}";
	    
	    String re1="(\\$)";	// Any Single Character 1
	    String re2="(\\{)";	// Any Single Character 2
	    String re3="(i)";	// Any Single Character 3
	    String re4="(d)";	// Any Single Character 4
	    String re5="(\\})";	// Any Single Character 5
		try {
			 Pattern p = Pattern.compile(re1+re2+re3+re4+re5,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			    Matcher m = p.matcher(txt);
			    if (m.find())
			    {
			        String c1=m.group(1);
			        String c2=m.group(2);
			        String c3=m.group(3);
			        String c4=m.group(4);
			        String c5=m.group(5);
			        System.out.print("("+c1.toString()+")"+"("+c2.toString()+")"+"("+c3.toString()+")"+"("+c4.toString()+")"+"("+c5.toString()+")"+"\n");
			    }
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	   
	  }
}
