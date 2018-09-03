package com.bizduo.zflow.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.bizduo.zflow.domain.sys.User;
import com.bizduo.zflow.security.SignedUsernamePasswordAuthenticationToken;
import com.bizduo.zflow.service.sys.IUserService;

/**
 * Servlet Filter implementation class PPFeedBack
 */
public class PPFeedBack extends AbstractAuthenticationProcessingFilter {
	@Autowired 
	private IUserService userService;

//	private String usernameHeader = "j_username";
//	private String passwordHeader = "j_password";
//	private String signatureHeader = "j_signature";
	protected PPFeedBack() {
		super("/j_spring_security_filter");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException,
			IOException, ServletException {
//		String username = request.getHeader(usernameHeader);
//		String password = request.getHeader(passwordHeader);
//		String signature = request.getHeader(signatureHeader);
		User user = userService.findByUserId(Integer.parseInt(request.getParameter("userId")));
		SignedUsernamePasswordAuthenticationToken authRequest = new SignedUsernamePasswordAuthenticationToken(
				user.getUsername(), user.getPassword(), null);
		return this.getAuthenticationManager().authenticate(authRequest);
	}  
}
