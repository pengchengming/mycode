<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
 
<script type="text/javascript">
var userRole = "${sessionScope.ROLES}";
var ROLEALL = '${sessionScope.ROLEALL}';
</script>

<h3 class="text-center padding-vertical-20">公租房单位申请意向登记单表</h3>
<div class="row">
	<div>
	<h4>申请人基本信息</h4>
	<table class="table table-bordered table-hover text-center table-horizontal">
		<tbody>
		<tr><th width="200">人员姓名</th><td id="userName"></td><th width="200">户籍地</th><td id="placeOfDomicile"></td><th width="200">婚姻状况</th><td id="maritalStatus"></td></tr>
		<tr>
			<th width="200">本市住房情况</th>
			<td id="housingConditionsInTheCity"></td>
			<th>申请电话</th>
			<td id="applicantPhone"></td>
		</tr>
		<tr><th width="200">身份证号码</th><td id="identityCardNumber"></td><th>居住证号码</th><td id="residencePermitNumber"></td><th>住房公积金账号</th><td id="housingAccumulationFundAccount"></td></tr>
		<tr>
			<th width="200">所选小区</th>
			<td id="pickDwelling"></td>
			<th>申请户型</th>
			<td id="applyForFamily"></td>
		</tr>
		<tr><th width="200">户籍地址</th><td id="permanentAddress" colspan="5"></td></tr>
		<tr><th width="200">联系地址</th><td id="address" colspan="5"></td></tr>
		<tr><th width="200">学历</th><td id="education"></td><th width="200">学位证书编码</th><td id="educationCardCode"></td><th width="200">毕业院校</th><td id="graduationSchool"></td></tr>
		<tr><th width="200">毕业时间</th><td id="graduationTime"></td><th width="200">专业</th><td id="specialty"></td></tr>
		<tr><th width="200">符合条件</th><td id="accordwith" colspan='5'></td></tr>
		</tbody>
	</table>

	</div>
	<div id="">
	<h4>共同申请人</h4>
	<table id="relative" class="table table-bordered table-hover text-center table-horizontal">
		<tbody>
			<tr><th>与申请人关系</th><th>姓名</th><th>身份证号码</th><th>身份证照片</th></tr>
		</tbody>
	</table>
	</div>
	<div id="housemessage">
	<h4>房产信息</h4>
	<div id="userhouing">
<!-- 	<table class="table table-bordered table-hover text-center table-horizontal"> -->
<!-- 		<tbody> -->
<!-- 			<tr><th width="200">房屋坐落地址</th><td id="houseaddress"></td><th width="200">使用面积</th><td id="area"></td></tr> -->
<!-- 			<tr><th width="200">产权承租人</th><td id="propertyOwner"></td><th width="200">该住房人口总数</th><td id="theHousingAllNumpeople"></td></tr> -->
<!-- 			<tr><th width="200">房产证或使用权凭证照片</th><td id="photo1" colspan="3"></td></tr> -->
<!-- 		</tbody> -->
<!-- 	</table> -->
	</div>
	</div>
	<div id="">
	<h4>上传人</h4>
	<table id="userphoto" class="table table-bordered table-hover text-center table-horizontal">
		<tbody>
			<tr><th width="200">材料内容</th><th>照片</th></tr>
		</tbody>
	</table>
	</div>
	</div>
	
<table id="approvaluser" style="padding: 10px;">
</table>