<!DOCTYPE HTML>
<html>
<link href="css/style.css" rel="stylesheet" type="text/css">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>train-培训</title>
</head>

<body>
	<banner><img src="images/train_banner.jpg" width="100%"></banner>
    <content style="background-image: url(images/list_head_bg.jpg); background-repeat: repeat-x;">
		<table width="100%" cellspacing="0" class="list">
        	<tr>
                <th>课程名称</th>
            </tr>
        	<tr>
                <td>非财务经理的财务管理</td>
            </tr>
          <tr>
            <td>Finance management for<br/> Non-financial Executives</td>
          </tr>
          <tr>
            <td>公司财务分析与风险防范</td>
          </tr>
          <tr>
            <td>内部控制与流程管理</td>
          </tr>
          <tr>
            <td>全面预算管理与控制</td>
          </tr>
          <tr>
            <td>成本分析与控制（如何与Excel ERP 结合）</td>
          </tr>
          <tr>
            <td>EXCEL高效数据分析之道</td>
          </tr>
          <tr>
            <td>企业运营报表与标准化建设--运筹帷幄的基础</td>
          </tr>
          <tr>
            <td>创新之道——成功商业模式</td>
          </tr>
          <tr>
            <td>信息化条件下的数据的下采集及标准化</td>
          </tr>
          <tr>
            <td>从核算走向管理_财务人职业能力塑造</td>
          </tr>
          <tr>
            <td>流程管理理论与实践</td>
          </tr>
          <tr>
            <td>从技术到管理:新任主管特训营</td>
          </tr>
          <tr>
            <td>财务管理训练营</td>
          </tr>
        </table>
    </content>
    <bottom_div>
    <input type="button" class="club_button" value="开通课程" onclick="javascripts:signup();">
    <script type="text/javascript">
    	function signup(){
    		window.location="/zflow/wxflows/wxsignup?openid=<%=request.getParameter("openid")%>&activitycode=2014032003";
    	}
    </script>
    <a href="http://www.financeren.com/gongkaike.asp">培训课程详情</a>
    </bottom_div>
</body>
</html>
