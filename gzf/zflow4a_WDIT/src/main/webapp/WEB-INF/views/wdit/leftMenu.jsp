<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 
<script type="text/javascript">
	$(document).ready(function(){
		var menuId= '${sessionScope.menuId}';
		$("#menu_"+menuId).addClass("open");
		$("#menu_ul_"+menuId).show();
	});
	function toUrlPage(url,miId){
		if(url.indexOf("?")>0)
			url+="&";
		else 
			url+="?";
		url+="menuId="+miId;
		window.location.href=<c:url value="/"/>+url;
	}
</script>
 
<!-- start: PAGESLIDE LEFT --> 
        <div id="pageslide-left" class="pageslide inner ">
            <div class="navbar-content">
                <!-- start: SIDEBAR -->
                <div class="main-navigation left-wrapper transition-left">
                    <div class="navigation-toggler hidden-sm hidden-xs">
                        <a href="#main-navbar" class="sb-toggle-left"></a>
                    </div>
                    <!-- start: MAIN NAVIGATION MENU -->
                    <ul class="main-navigation-menu">
						<c:forEach var="mi" items="${sessionScope.MENUITEMS}"> 
							<li  id="menu_${mi.id}">
								<a href="javascript:void(0)">
									<i class="iconfont icon-yonghu"></i> 
									<span class="title" > ${mi.name} </span>
									<i class="iconfont icon-arrow"></i> 
								</a>
							 	 <ul class="sub-menu"  id="menu_ul_${mi.id}">
									<c:forEach var="tmi" items="${mi.subMenuItemList}">
										<li><a href="javascript:toUrlPage('${tmi.url}',${mi.id});"  >${tmi.name}</a></li>
									</c:forEach> 
								</ul> 
							</li>
						</c:forEach>
<!--                         <li><a href="#"><i class="iconfont icon-jindu"></i> 审核进度</a></li> -->
                    </ul>
                    <!-- end: MAIN NAVIGATION MENU -->
                </div>
                <!-- end: SIDEBAR -->
            </div>
        </div>
        <!-- end: PAGESLIDE LEFT -->
         
  