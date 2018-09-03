<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script type="text/javascript" src="<c:url value="/javascripts/prototype/prototype.js" />" ></script>
<script type="text/javascript" src="<c:url value="/javascripts/knipex/common.js" />" ></script>
<script type="text/javascript">
	Event.observe(window, 'load', function() {
		Try.these(  
			function(){
			 	onWindowLoad();
			 	menuMouseOn();
			}
		)
	});	
	   
</script>