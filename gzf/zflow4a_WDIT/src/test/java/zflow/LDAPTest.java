package zflow;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Hashtable;
import java.util.Enumeration;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls ;
import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchResult;

public class LDAPTest {
	static BufferedWriter output;

public static void main(String[] args) {
	LDAPTest ldap=new LDAPTest();
	
	 String s = new String();  
     String s1 = new String();  
       
     try {  
         File f = new File("c:/abc/ldap.sql");  
           
         if (f.exists()) {  
             System.out.println("文件存在");  
         } else {  
             System.out.println("文件不存在，正在创建...");  
             if (f.createNewFile()) {  
                 System.out.println("文件创建成功！");  
             } else {  
                 System.out.println("文件创建失败！");  
             }  
         }  
           
         output = new BufferedWriter(new FileWriter(f)); 
         ldap.init();
          
         output.close();  
     } catch (Exception e) {  
         e.printStackTrace();  
     }  

}
public void init(){
DirContext ctx =null;
Hashtable env =new Hashtable();
env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
env.put(Context.PROVIDER_URL, "ldap://172.16.10.22:389/");//连接LDAP的URL和端口


env.put(Context.SECURITY_AUTHENTICATION, "simple");//以simple方式发送
//env.put(Context.SECURITY_PRINCIPAL, "cn=xinyi,OU=Jahwa User,DC=jahwa,DC=com,DC=cn");//用户名
env.put(Context.SECURITY_PRINCIPAL, "yunpan@jahwa.com.cn");//用户名
env.put(Context.SECURITY_CREDENTIALS, "P@ssw0rd");//密码
String baseDN="OU=Jahwa User,DC=jahwa,DC=com,DC=cn";//查询区域
String filter="(&(objectClass=person))";//条件查询

try{
ctx =new InitialDirContext(env);//连接LDAP服务器
System.out.println("Success");
SearchControls constraints =new SearchControls();//执行查询操作
constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
NamingEnumeration en=ctx.search(baseDN, filter, constraints);
if(en==null){
System.out.println("There have no value");
}else{
while(en.hasMoreElements()){

Object obj=en.nextElement();
if(obj instanceof SearchResult){
SearchResult sr=(SearchResult) obj;
String cn=sr.getName();

//System.out.println("cccccc: "+cn);
//System.out.println("====: "+sr.getAttributes().get("displayName"));
//output.write(sr.getAttributes().get("sAMAccountName").get()+":"+sr.getAttributes().get("displayName").get()+"\r\n");
//output.write("insert into global_employee(id,username,realname,isleave,sex,openid) values(dbo.fn_GetNextID('employee_id'),'"+sr.getAttributes().get("sAMAccountName").get()+"','"+sr.getAttributes().get("displayName").get()+"',0,1,newid());\r\n"); 
//output.write("update id_table set next_val=next_val+1 where sequence_name='employee_id';\r\n"); 
//
//output.write("insert into global_user (id,accountNonExpired,accountNonLocked,credentialsNonExpired,enabled,fullname,password,passwordChanged,username,organization_id,usertype) ");
//output.write(" values(dbo.fn_GetNextID('user_id'),1,1,1,1,'"+sr.getAttributes().get("displayName").get()+"','3d4f2bf07dc1be38b20cd6e46949a1071f9d0e3d',0,'"+sr.getAttributes().get("sAMAccountName").get()+"',1000,1);\r\n");
//output.write(" insert into global_user_role (user_id,role_id) values(dbo.fn_GetNextID('user_id'),2)");
//output.write("update id_table set next_val=next_val+1 where sequence_name='user_id';\r\n"); 
if(sr.getAttributes().get("mail")!=null)
	output.write("update global_employee set email='"+sr.getAttributes().get("mail").get()+"' where username='"+sr.getAttributes().get("sAMAccountName").get()+"';\r\n"); 



}
}
}

}catch(javax.naming.AuthenticationException e){
System.out.println(e.getMessage());
}catch(Exception e){
System.out.println("erro："+e);
}
}

}
