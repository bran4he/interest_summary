<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; UTF-8">
	<link rel="shortcut icon" href="../favicon.ico">
	<link rel="icon" href="../favicon.ico">

	<script src="//cdn.bootcss.com/jquery/1.10.0/jquery.js"></script>
	
	<!-- 引入 ECharts 文件 -->
	<script src="//cdn.bootcss.com/echarts/3.4.0/echarts.js"></script>
	<!-- 引入 shine 主题 -->
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
		 $("#sign").on('click', sign);
		 $("#export").on('click', exportXlsx);
		 $("#viewCharts").on('click', showCharts);
	 });
	
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
	
	<input type="button"  value="签到"  id="sign" class="btn btn-primary"/>
	
	<input type="button"  value="导出报表"  id="export"  class="btn btn-primary"/>
	<input type="button" value="展示报表" data-toggle="modal" data-target="#myModal"
	id="viewCharts" class="btn btn-warning"/><br>

	<a id="report" href="">点我下载报表</a>


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
