<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; UTF-8">
	
	<link rel="shortcut icon" href="../favicon.ico">
	<link rel="icon" href="../favicon.ico">

	<script src="//cdn.bootcss.com/jquery/1.10.0/jquery.js"></script>
	
	<!-- 引入 ECharts 文件 
	<script src="../js/echarts.js"></script>
	-->
	<script src="//cdn.bootcss.com/echarts/3.4.0/echarts.js"></script>
	
	<!-- 引入 shine 主题
	<script src="../js/shine.js"></script>
	 -->
	<script src="//cdn.bootcss.com/shine.js/0.2.7/shine.js"></script>
	
	
	<link href="//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
	<script src="//cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	
	<style type="text/css">
  
		.box{  
				margin-left:auto;  
				margin-right:auto; 
				margin-top:100PX; 
			}
	</style>
	
	<script type="text/javascript">
	 $(document).ready(function(){
		 $("#getImgCode").on('click', getImgCode);
		 $("#login").on('click', login);
		 $("#sign").on('click', sign);
		 $("#export").on('click', exportXlsx);
		 $("#viewCharts").on('click', showCharts);
	 });
	
	
	function getImgCode() {
		
		$.ajax({
			type:"get",
			dataType:"json",
			url:"code",
			success: function(data){
				console.log(data);
				$("#imgUrl").attr("src","..//"+data.imgCodePath);
			}
		});
	}

	function login() {
		var username = document.getElementById("username").value;
		var password = document.getElementById("password").value;
		var code = document.getElementById("imgCode").value;
		console.log(username, password, code);
		
		$.ajax({
			type:"get",
			dataType:"json",
			url:"login?username="+username+"&password="+password+"&imgcode="+code,
			success: function(data){
				console.log(data);
				alert(data.logined);
			}
		});
	}
	function sign(){
		$.ajax({
			type:"get",
			dataType:"json",
			url:"sign",
			success: function(data){
				console.log(data);
				alert(data.signedList);
			}
		});
	}
	
	function exportXlsx() {
		
		$.ajax({
			type:"get",
			dataType:"json",
			url:"export",
			success: function(data){
				console.log(data);
				$("#report").attr("href","..//"+data.reportPath);
			}
		});
	}
	
	
	function showCharts() {
		
		$.ajax({
			type:"get",
			dataType:"json",
			url:"chart",
			success: function(data){
				console.log(data);
				viewCharts(data);
			}
		});
	}
	
	function viewCharts(data){
		// 基于准备好的dom，初始化echarts实例
		var myChart = echarts.init(document.getElementById('main'),'shine');
		
		// 指定图表的配置项和数据
		var option = {
			title : {
				text : '回款计划详细'
			},
			tooltip : {},
			legend : {
				data : [ '本息', '本金', '利息' ]
			},
			xAxis : {
				data : data.datelst
			},
			yAxis : {},
			series : [ 
			{
				name : '本息',
				type : 'bar',
				data : data.totallst
			},
			{
				name : '本金',
				type : 'bar',
				data : data.capitallst
			},
			{
				name : '利息',
				type : 'bar',
				data : data.interestlst
			}
			]
		};

		// 使用刚指定的配置项和数据显示图表。
		myChart.setOption(option);
	}
	
	window.onload = function () {
		console.log("load...");
	}
	
	</script>

</head>
<body>
<div class="container">
<div class="page-header">
    <h1>长久贷P2P投资管理工具
        <small>登录, 签到, 报表导出及展示</small>
    </h1>
</div>
<!-- 
<p>基于Spring MVC的应用, 后台提供Restful API, 前端基于jquery + bootstrap。</p>
 -->
 <!-- 
<div class="panel panel-primary">
    <div class="panel-heading">
        <h3 class="panel-title">长久贷P2P投资管理工具</h3>
    </div>
    <div class="panel-body">
  -->
		<form class="form-horizontal" role="form">
		
			<div class="form-group">
				<label for="username" class="col-sm-2 control-label">用户名:</label>
				<div class="col-xs-4">
					<input type="text" class="form-control" id="username" placeholder="请输入用户名">
				</div>
			</div>
			
			<div class="form-group">
				<label for="password" class="col-sm-2 control-label">密码:</label>
				<div class="col-xs-4">
					<input type="password" class="form-control" id="password" placeholder="请输入密码">
				</div>
			</div>
			
			<div class="form-group">
				<label for="imgCode" class="col-sm-2 control-label">验证码:</label>
				<div class="col-xs-4">
					<input type="text" class="form-control" id="imgCode" placeholder="请输入验证码" />
					<br>
					<img alt="这是验证码啊！！" id="imgUrl" src=""> 
					<input type="button" value="获取验证码" id="getImgCode" class="btn btn-info" />
				</div>
			</div>
			
		</form>
	
	<input type="button"  value="登录"  id="login" class="btn btn-primary"/>
	<input type="button"  value="签到"  id="sign" class="btn btn-primary"/>
	
	
	
	<input type="button"  value="导出报表"  id="export"  class="btn btn-primary"/>
	<input type="button" value="展示报表" data-toggle="modal" data-target="#myModal"
	id="viewCharts" class="btn btn-warning"/><br>

	<a id="report" href="">点我下载报表</a>
	


<!-- 
    </div>
    <div class="panel-footer">
    	登录, 签到, 报表导出及展示
     <p><a href="https://github.com/bran4he/interest_summary">项目github</a></p>
     </div>
</div>
 -->
<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="myModalLabel">
					echarts 报表
				</h4>
			</div>
			<div class="modal-body">
				<div id="main" style="width:800px; height: 600px;"></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>

	
	
	
</div>	
</body>
</html>
