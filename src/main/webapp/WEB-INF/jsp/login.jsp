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
	
	<link rel="stylesheet" href="../static/css/main.css" type="text/css" />
	
	<script type="text/javascript" src="../static/js/login.js"></script>

</head>
<body>
	<div class="container">
		<div class="page-header">
		    <h1>长久贷P2P投资管理工具
		        <small>登录, 签到, 报表导出及展示</small>
		    </h1>
		</div>
		<form class="form-horizontal" role="form">
		
			<div class="form-group">
				<label for="username" class="col-sm-2 control-label">用户名:</label>
				<div class="col-xs-4">
					<input type="text" class="form-control" name="username" id="username" placeholder="请输入用户名">
				</div>
			</div>
			
			<div class="form-group">
				<label for="password" class="col-sm-2 control-label">密码:</label>
				<div class="col-xs-4">
					<input type="password" class="form-control" name="password" id="password" placeholder="请输入密码">
				</div>
			</div>
			
			<div class="form-group">
				<label for="imgcode" class="col-sm-2 control-label">验证码:</label>
				<div class="col-xs-4">
					<input type="text" class="form-control" name="imgcode" id="imgcode" placeholder="请输入验证码" />
					<br>
					<img alt="这是验证码啊！！" id="imgUrl" src=""> 
					<input type="button" value="获取验证码" id="getImgCode" class="btn btn-info" />
					<input type="button"  value="登录"  id="login" class="btn btn-primary"/>
				</div>
			</div>
		</form>
		
		
	</div>	
</body>
</html>
