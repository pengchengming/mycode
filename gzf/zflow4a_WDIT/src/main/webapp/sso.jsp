<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ page  language="java" import="com.bizduo.zflow.util.Configure" %>

<html>
<head>
<meta http-equiv="cache-control" content="max-age=0" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
<meta http-equiv="pragma" content="no-cache" />
</head>
<body>
<%
String[] names = new String[3];
int ntlmtimes=0;
if(request.getSession().getAttribute("ntlmtimes")!=null)
	ntlmtimes=Integer.parseInt(request.getSession().getAttribute("ntlmtimes").toString());
request.getSession().setAttribute("ntlmtimes", ntlmtimes+1);

String auth = request.getHeader("Authorization");
System.out.println("auth:"+auth);
// out.println("1");
if ((auth == null)){
// 	out.println("2");
// 	out.println(ntlmtimes);
	if(ntlmtimes<=1&&("true".equalsIgnoreCase(Configure.getConfigure("ntlm").toString()))){
	%>
	<script type='text/javascript'>window.location="login"; </script>
	<% 
// 		out.println("2.1");
	response.setStatus(response.SC_UNAUTHORIZED);
	response.setHeader("WWW-Authenticate", "NTLM");
	response.flushBuffer();
	}
	else{
// 		out.println("3");
		response.sendRedirect("login");
		response.flushBuffer();
	}
	
}else if (auth.startsWith("NTLM ")){
// 	out.println("4");
	
	byte[] msg = new sun.misc.BASE64Decoder().decodeBuffer(auth.substring(5));
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
		response.sendError(response.SC_UNAUTHORIZED);
		response.flushBuffer();

		//zt@2013/4/18 解决Resin3域登录慢问题
		if (request instanceof HttpServletRequest){				
			HttpServletRequest req = (HttpServletRequest)request;
			req.logout();				
		}
		if (response instanceof HttpServletResponse){
			HttpServletResponse res = (HttpServletResponse)response;
			res.flushBuffer();//(true);
		}

		return;
	}

else{
	long t=new java.util.Date().getTime();
	%>
<div style="display:none">
<form id=tokenform action="ntlm?t=<%=t%>">
<input name="token" value="<%=auth %>" >
<input type="submit" />
</form>
<script type='text/javascript'>tokenform.submit(); </script>
</div>
	
	<% 
}
}else{
// 	out.println("5");

	response.sendRedirect("login");
	response.flushBuffer();
}

%>
</body>
</html>