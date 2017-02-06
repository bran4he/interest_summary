# interest_summary
a demo to show interest capturing data from website and show charts with echarts
- - - 

1. use **redis** as data storage
2. design some kind of **structure** type in redis, don't use js to handle the raw data
3. use **echarts** to show the final results
4. get the **bonous** type & number, showing in the page for reference
5. add permission control
6. for config. file, save something security data use complicate method, eg. BASE64 and then exchange some character
7. TBC


- - -
update currently

## 设计
> 爬取投资回款信息后写入excel报表，报表写入/WEB-INF/download/admin/date_changjiudai_username.xlsx
- 由action控制下载文件，通过session获取用户名从而获得正确的目录
- 报表名称前缀是时间，逻辑是一天只需要导出一份最新的报表，其他时间可以复用同一份，提升性能
- TODO: spring bacth/job 每周/月扫描一次目录删除失效报表

## 主页
![Alt text](index_page.png)



## 弹出窗口显示统计报表
![Alt text](echarts_page.png)
