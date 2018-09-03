<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
 
<script type="text/javascript">
var userRole = "${sessionScope.ROLES}";
var ROLEALL = '${sessionScope.ROLEALL}';
</script>
 
<table class="table table-bordered table-hover text-center table-horizontal">
<tbody>
	<tr>
		<th class="text-right" width="200">单位名称</th>
        <td class="text-left"  id="applicant"></td>
	</tr>
	<tr>
		<th class="text-right" width="200">企业信用代码</th>
        <td class="text-left" id="unifiedSocialCreditCode" ></td>
	</tr>
	<tr>
		<th class="text-right" width="200">注册地址</th>
        <td class="text-left" id="registerAddress"  ></td>
	</tr>
	<tr>
		<th class="text-right" width="200">营业或办公地址</th>
        <td class="text-left" id="officeAddress" ></td>
	</tr>
	<tr>
		<th class="text-right" width="200">注册资本金(人民币/万元)</th>
        <td class="text-left" id="registerMoney" ></td>
	</tr>
	<tr id="licenseNumber_tr">
		<th class="text-right" width="200">证照编号</th>
        <td class="text-left" id="licenseNumber" ></td>
	</tr>
	<tr  id="registrationNumber_tr" >
		<th class="text-right" width="200">工商登记号</th>
        <td class="text-left" id="registrationNumber" ></td>
	</tr>
	<tr id="taxRegistersCardNumber_tr">
		<th class="text-right" width="200">税务登记号</th>
        <td class="text-left" id="taxRegistersCardNumber" ></td>
	</tr>
	<tr>
		<th class="text-right" width="200">上一年度纳税金额(人民币/万元)</th>
        <td class="text-left" id="oneYearIsTaxAmount" ></td>
	</tr>
	<tr>
		<th class="text-right" width="200">所属委办</th>
        <td class="text-left" id="companytype"></td>
	</tr>
	<tr>
		<th class="text-right" width="200">在职员工人数</th>
        <td class="text-left" id="staffNum"  ></td>
	</tr>
	<tr>
		<th class="text-right" width="200">单位承担租金的百分比</th>
        <td class="text-left" id="rent"  ></td>
	</tr>
	<tr>
		<th class="text-right" width="200">所选小区</th>
        <td class="text-left" id="pickDwelling"  ></td>
	</tr>
	<tr>
		<th class="text-right" width="200">企业性质</th>
        <td class="text-left" id="companyNature" ></td>
	</tr>
	<tr>
		<th class="text-right" width="200">申请公租房工作专职联系人</th>
        <td class="text-left" id="linkman" ></td>
	</tr>
	<tr>
		<th class="text-right" width="200">联系人(手机)</th>
        <td class="text-left" id="phone" ></td>
	</tr>
	<tr>
		<th class="text-right" width="200">联系人(电子邮箱)</th>
        <td class="text-left" id="email" ></td>
	</tr>
	<tr>
		<th class="text-right" width="200">联系人(电话)</th>
        <td class="text-left"  id="tel" ></td>
	</tr>
	<tr>
		<th class="text-right" width="200">申请单位(需求说明)</th>
        <td class="text-left" id="applicationCompany" ></td>
	</tr>
	<tr id="photoType1_tr">
		<th class="text-right" width="200">营业执照(三证合一)</th>
        <td class="text-left" id="photoType1_td"></td>
	</tr>
	<tr id="photoType2_tr">
		<th class="text-right" width="200">营业执照(未三证合一)</th>
        <td class="text-left" id="photoType2_td"></td>
	</tr>
	</tbody>
</table>
 
<table id="showstep">
<!-- 	<tr><td class="text-right"  width="150">资料预审结果：</td><td id="step1" class="text-left" style="color:green"></td><td class="text-right"  width="150">审核时间：</td><td id="time1" class="text-left" style="color:green"></td><td class="text-right"  width="150">审核人:</td><td id="user1" class="text-left" style="color:green"></td></tr> -->
<!-- 	<tr><td class="text-right">公租房资格审核结果：</td><td id="step2" class="text-left" style="color:green"></td><td class="text-right">审核时间：</td><td id="time2" class="text-left" style="color:green"></td><td class="text-right">审核人:</td><td id="user2" class="text-left" style="color:green"></td></tr> -->
<!-- 	<tr><td class="text-right">科/经/商委审核结果：</td><td id="step3" class="text-left" style="color:green"></td><td class="text-right">审核时间：</td><td id="time3" class="text-left" style="color:green"></td><td class="text-right">审核人:</td><td id="user3" class="text-left" style="color:green"></td><td class="text-right" width="150">审核依据:</td><td id="basis3" class="text-left"></td></tr> -->
</table>

<table id="showreturnremark">
</table>