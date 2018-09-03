package com.bizduo.zflow.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import com.bizduo.zflow.domain.bizType.BizValue;
import com.bizduo.zflow.domain.sys.Role;
import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.service.bizType.IBizTypeService;
import com.bizduo.zflow.service.sys.IUserService;

public class CustomAuthenticationProvider extends
        AbstractUserDetailsAuthenticationProvider {

	private final static String isAdmin = "ROLE_ADMIN";
	@Autowired
	private IUserService userService;
	@Autowired
	private IBizTypeService  bizTypeService;
	
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        //如果想做点额外的检查,可以在这个方法里处理,校验不通时,直接抛异常即可
        System.out
                .println("CustomAuthenticationProvider.additionalAuthenticationChecks() is called!");
    }

    @Override
    protected UserDetails retrieveUser(String username,
            UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
  
		String password = (String) authentication.getCredentials();
        if(!StringUtils.hasText(password)){
            throw new BadCredentialsException("Password malformed, Please enter password again!");
        } 
        User user =  userService.findUserAccountsByUsername(username);
        if(!"1".equals(user.getUserType()))
        	user= userService.findUserAccountsByUsernameEqualsAndPasswordEquals(username, password);
        else
        	user= userService.findUserAccountsByLDAP(username, password);
        
        List<BizValue> listBizValue = bizTypeService.getBizValuesByCode(User.ISROLEGROUP);
		boolean isRoleGroup=false;//true 从缓存中取值
		if (listBizValue != null && listBizValue.size() > 0) {
			String value = listBizValue.get(0).getDisplayValue();
			if(value!=null&& value.equals("1") ){
				isRoleGroup=true;
			}
		} 
		 User u = user.clone();
		if(isRoleGroup){ //是否角色分拆
			Map<String,Collection<GrantedAuthority>> grantedAuthorityMap= userService.findMapGrantedAuthorityByUserId(user.getId());
			Map<String,Collection<GrantedAuthority>> phoneGrantedAuthorityMap= userService.findMapPhoneGrantedAuthorityByUserId(user.getId());
			
			u.setPermissionMap(grantedAuthorityMap);
	        u.setPhonePermissionMap(phoneGrantedAuthorityMap);
		}else {
		     	List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		        Collection<GrantedAuthority> list = userService.findGrantedAuthorityByUserId(user.getId());
		        authorities.addAll(list);

		        List<GrantedAuthority> phoneAuthorities = new ArrayList<GrantedAuthority>();
		        Collection<GrantedAuthority> phonelist = userService.findPhoneGrantedAuthorityByUserId(user.getId());
		        phoneAuthorities.addAll(phonelist);
		        
		        u.setPermissions(authorities);
		        u.setPhonePermissions(phoneAuthorities);
		}       
        return u;	
	    	

    }

}