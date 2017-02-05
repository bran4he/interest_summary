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
	
	<script type="text/javascript" src="../static/js/sum.js"></script>

</head>
<body>
<div class="container">
	<div class="page-header">
	    <h1>长久贷P2P投资管理工具
	        <small>登录, 签到, 报表导出及展示</small>
	    </h1>
	</div>
	<form class="form-inline" role="form">
		<div class="checkbox">
		    <label>
		      <input type="checkbox" id="keepSign" name="keepSign">后台签到
		      <span id="tip" class="badge pull-right">?</span>
		    </label>
	 	</div>
		<input type="button"  value="签到"  id="sign" class="btn btn-primary"/>
	</form>
	
	<input type="button"  value="导出报表"  id="export"  class="btn btn-primary"/>
	<input type="button" value="展示报表" data-toggle="modal" data-target="#myModal"
	id="viewCharts" class="btn btn-warning"/><br>

	<a id="report" href="" >点我下载报表</a>


<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:850px;">
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
