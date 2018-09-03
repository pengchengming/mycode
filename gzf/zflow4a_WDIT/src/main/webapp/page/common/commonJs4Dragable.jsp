<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%--@ include file="/WEB-INF/page/common/taglibs.jsp" --%>
<%-- <script type="text/javascript" src="<c:url value="/javascript/ext/3.2.1/adapter/jquery/ext-jquery-adapter.js"/>"></script> --%>
<%-- <script type="text/javascript" src="<c:url value="/javascript/ext/3.2.1/ext-all.js"/>"></script> --%>
<%-- <script type="text/javascript" src="<c:url value="/javascript/jquery/ui/jquery.ui.core.js" />"></script> --%>
<%-- <script type="text/javascript" src="<c:url value="/javascript/jquery/ui/jquery.ui.position.js" />"></script> --%>
<%-- <script type="text/javascript" src="<c:url value="/javascript/jquery/ui/jquery.ui.widget.js" />"></script> --%>
<%-- <script type="text/javascript" src="<c:url value="/javascript/jquery/ui/jquery.ui.mouse.js" />"></script> --%>
<%-- <script type="text/javascript" src="<c:url value="/javascript/jquery/ui/jquery.ui.draggable.js" />"></script> --%>
<%-- 
<script type="text/javascript" src="<c:url value="/javascript/jquery/jquery-ui-1.8.18.custom.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/javascript/jquery/ui/jquery.ui.autocomplete.js" />"></script>
<script type="text/javascript" src="<c:url value="/javascript/jquery/jquery-1.7.1.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/javascript/jquery/jquery-ex.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/jquery/jquery.json-2.2.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/jquery/ui/jquery.ui.tabs.js" />"></script>

<script type="text/javascript" src="<c:url value="/javascript/i18n.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/ui-1.0.0-min.js"/>"></script>

<script type="text/javascript" src="<c:url value="/javascript/zTree/jquery.ztree.core-3.0.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/zTree/jquery.ztree.exedit-3.0.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/zTree/jquery.ztree.excheck-3.0.min.js"/>"></script>
--%>

<script type="text/javascript" src="<c:url value="/javascript/jquery/jquery-1.7.1.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/javascript/jquery/jquery-ex.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/jquery/jquery.json-2.2.js"/>"></script>

<script type="text/javascript" src="<c:url value="/javascript/jquery/jquery-ui-1.8.18.custom.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/javascript/jquery/ui/jquery.ui.autocomplete.js" />"></script>
<script type="text/javascript" src="<c:url value="/javascript/jquery/ui/jquery.ui.tabs.js" />"></script>

<script type="text/javascript" src="<c:url value="/javascript/i18n.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/ui.js"/>"></script>

<script type="text/javascript" src="<c:url value="/javascript/zTree/jquery.ztree.core-3.0.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/zTree/jquery.ztree.exedit-3.0.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/zTree/jquery.ztree.excheck-3.0.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/hoverIntent.js"/>"></script>
<script type="text/javascript" src="<c:url value="/javascript/superfish.js"/>"></script>

<script type="text/javascript">
function headpage(id){
	$("#" + id).addClass("current");
}


 	function showOrhed(id){
		if($("#" + id+" .second-li").is(":visible")){
			$("#" + id+" .first-li-title").addClass("first-li-title-closed").removeClass("first-li-title");
		}
		if($("#" + id+" .second-li").is(":hidden")){
			$("#" + id+" .first-li-title-closed").addClass("first-li-title").removeClass("first-li-title-closed");
		}
 		if($("#" + id+" .second-li").is(":visible")){
 			$("#" + id+" .second-li").hide();
 		}else if($("#" + id+" .second-li").is(":hidden")){
 			$("#" + id+" .second-li").show();
 		}
 	}

	function openWin(id,config) {
		var ele = ($("#" + id));
		var jqele = $(ele);  
		config=config?config:{};
		if(config && config.width){
			$(".ui-window-frame",ele).width(config.width);
		}
		jqele.show();
		jqele.draggable({containment:"html",handle:'.ui-window-title'});
		var w = jqele.width();
		var h = jqele.height();
		var compareTop = 0;
		var compareLeft = 0;
		if($(window).height()>h){
			compareTop = Math.round(($(window).height()- h)/2); 
		}
		else{
			compareTop = 0;
		}
		if($(window).width()>w){
			compareLeft = Math.round(($(window).width()- w)/2); 
		}
		else{
			compareLeft = 0;
		}

		$("#" + id).css("top",$('html').scrollTop()+ compareTop+"px");
		$("#" + id).css("left",$('html').scrollLeft()+compareLeft+"px");
		
		ui.showCover(45);
	};

	
	function closeWin(id) {
		$("#" + id).hide();
		ui.hideCover();
	}
	
	function showCurrent(){
		if($(".left-menu-current")==null){
			$(this).addClass("left-menu-current");
		}else{
			$(".left-menu-current").removeClass("left-menu-current");
			$(this).addClass("left-menu-current");
		}
	}
</script>
