$(document).ready(function(){
		 
	 $("#report").css("display", "none");
	 
	 $("#sign").on('click', sign);
	 $("#export").on('click', exportXlsx);
	 $("#viewCharts").on('click', showCharts);
	 
	 $("#tip").on('click', function(){
		 
	 });
 });

function sign(){
	$.ajax({
		type:"get",
		dataType:"json",
		url:"sign?keepSign=" + $("#keepSign").get(0).checked,
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
			//show dowload link
			$("#report").css("display", "block");
		}
	});
}


function showCharts() {
	//clear main div
	$("#main").empty();
	
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