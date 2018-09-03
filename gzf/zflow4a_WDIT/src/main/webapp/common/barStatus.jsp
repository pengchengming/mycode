<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	var barStatus={handleByCurrentUser:false,reportEmpty:false,canBack:false,canSubmit:false};
	<report:handleByCurrentUser report="${report}">
		barStatus.handleByCurrentUser=true;
	</report:handleByCurrentUser>
		
	<c:if test="${empty report.id}">
		barStatus.reportEmpty=true;
	</c:if>

	<report:canBack report="${report}">
		barStatus.canBack=true;
	</report:canBack>

	<report:canSubmit report="${report}">
		barStatus.canSubmit=true;
	</report:canSubmit>
		
		function barStatusChange(){
			if(barStatus.handleByCurrentUser){
				$('#a_save').show();
				if(barStatus.canBack)
					$('#a_backward').show();
				else
					$('#a_backward').hide();
				if(barStatus.canSubmit && checkRequired()["checkOk"])
					$('#a_forward').show();
				else
					$('#a_forward').hide();
			}
			else{
				$('#a_save').hide();
				$('#a_backward').hide();
				$('#a_forward').hide();
			}
		
			if(barStatus.reportEmpty){	
				$('#a_print').hide();
			}
			else{
				$('#a_print').show();
			}	
		}

		$(function(){barStatusChange()});
</script>