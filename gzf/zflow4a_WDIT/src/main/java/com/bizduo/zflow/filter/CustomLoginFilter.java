package com.bizduo.zflow.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bizduo.zflow.security.CustomAuthenticationToken;
//import org.springframework.security.authentication.AuthenticationServiceException;

public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    @SuppressWarnings("static-access")
	public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
    	

		String[] names = new String[3];
		byte[] msg=null;
		String remoteHost="";
		String domain="";
		String username="";
		
		
		
		String auth = request.getParameter("token");//request.getHeader("Authorization");
		if (auth == null){
			response.setStatus(response.SC_UNAUTHORIZED);
			response.addHeader("WWW-Authenticate", "NTLM");
//			response.
//			out.println("<script type='text/javascript'> window.location.href='/login/Login.jsp?from='+window.location.href.replace('?gopage','&gopage'); </script>");
			return null;
		}else{
//			out.println("auth:"+auth+"<BR>");
		}
		
		if (auth.startsWith("NTLM ")){

			try {
				msg = new sun.misc.BASE64Decoder().decodeBuffer(auth.substring(5));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(">>>>sso>>msg="+new String(msg));
			int off = 0, length, offset;
			if (msg[8] == 1){
				byte z = 0;
				byte[] msg1 = {(byte)'N', (byte)'T', (byte)'L', (byte)'M', (byte)'S', (byte)'S', (byte)'P', 
							z,(byte)2, z, z, z, z, z, z, z,(byte)40, z, z, z, 
							(byte)1, (byte)130, z, z,z, (byte)2, (byte)2,
							(byte)2, z, z, z, z, z, z, z, z, z, z, z, z
						};
				
				response.setHeader("WWW-Authenticate", "NTLM " + 
				new sun.misc.BASE64Encoder().encodeBuffer(msg1));
//				response.sendError(response.SC_UNAUTHORIZED);
//				response.flushBuffer();

//	 			//zt@2013/4/18 解决Resin3域登录慢问题
//	 			if (request instanceof HttpServletRequest){				
//	 				HttpRequest req = (HttpRequest)request;
//	 				req.killKeepalive();				
//	 			}
//	 			if (response instanceof HttpResponse){
//	 				HttpResponse res = (HttpResponse)response;
//	 				res.disableCaching(true);
//	 			}
 
			}else if (msg[8] == 3){
				off = 30;
				length = msg[off+17]*256 + msg[off+16];
				offset = msg[off+19]*256 + msg[off+18];
				remoteHost = new String(msg, offset, length);
				names[2] = remoteHost;
				length = msg[off+1]*256 + msg[off];
				offset = msg[off+3]*256 + msg[off+2];
				domain = new String(msg, offset, length);
				names[0] = domain;
				length = msg[off+9]*256 + msg[off+8];
				offset = msg[off+11]*256 + msg[off+10];
				username = new String(msg, offset, length);
				names[1] = username;
//				out.println("Username:"+username+"<BR>");
//				out.println("RemoteHost:"+remoteHost+"<BR>");
//				out.println("Domain:"+domain+"<BR>");
				//if(1==1) return;
				if("jahwa.com.cn".equals(names[0])) {
					names[0] = "jahwa";
				}
			}
		}	

//		String username = username;//obtainUsername(request).toUpperCase().trim();
		//username="Admin";
		String password = "";//obtainPassword(request);
		//获取用户输入的下一句答案
		String answer = "";//obtainAnswer(request);
		//获取问题Id(即: hashTable的key)
		Integer questionId = 1;//obtainQuestionId(request);

		//这里将原来的UsernamePasswordAuthenticationToken换成我们自定义的CustomAuthenticationToken
		CustomAuthenticationToken authRequest = new CustomAuthenticationToken(
				username, password, questionId, answer);

		//这里就将token传到后续验证环节了
		setDetails(request, authRequest);
		
		return this.getAuthenticationManager().authenticate(authRequest);
	    	
    	
    	
    	

        // if (!request.getMethod().equals("POST")) {
        // throw new AuthenticationServiceException(
        // "Authentication method not supported: "
        // + request.getMethod());
        // }

//        String username = obtainUsername(request).toUpperCase().trim();
//        String password = obtainPassword(request);
//
//        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
//                username, password);
//
//        setDetails(request, authRequest);
//        return this.getAuthenticationManager().authenticate(authRequest);
    }

}