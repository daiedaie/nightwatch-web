<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>客户信息查询</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
 	$(document).ready(function() {
 		$("#contentTable tbody tr").each(function(){
 			//console.log("${page.list}");
 			var num = new Number($(this).find("td:eq(6)").text());
 			$(this).find("td:eq(6)").text(num.toFixed(2));
 			//$(this).find("td:eq(6)").text(300000003333.09);
 			//$(this).find("td:eq(6)").empty();
 			//console.log(num.toExponential(2));
			//console.log($(this).find("td:eq(6)").text());
			//if(num>=10) $(this).find("td:eq(6)").text(num.toExponential(2));
 		  });
	});
</script>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content">
<div class="ibox">
<div class="ibox-title">
		<h5>客户信息查询</h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<a class="close-link">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!-- 查询条件 -->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="cust" action="${ctx}/rtc/cust/list" method="post"  class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group" style="margin-bottom: 10px;">
			<span>资金账号：</span>
			<form:input path="fundid" htmlEscape="false" maxlength="20" class=" form-control input-sm" style="width:204px;margin-right: 50px;"
				onkeyup="top.numberValidation(this,true,20);" onafterpaste="top.numberValidation(this,true,20);"/>
			<span>客户号：</span>
			<form:input path="custid" htmlEscape="false" maxlength="20"  class=" form-control input-sm" style="width:204px;margin-right: 50px;"
				onkeyup="top.numberValidation(this,true,20);" onafterpaste="top.numberValidation(this,true,20);"/>
			<span>客户名称：</span>
			<form:input path="fundname" htmlEscape="false" maxlength="50"  class=" form-control input-sm" style="width:204px;margin-right: 50px;"/>
		</div>
		<div class="form-group" >
			<span>&nbsp;一&nbsp;码&nbsp;通：</span>
			<form:input path="ymtCode" htmlEscape="false" maxlength="20"  class=" form-control input-sm" style="width:204px;margin-right: 50px;"
				onkeyup="top.numberValidation(this,true,20);" onafterpaste="top.numberValidation(this,true,20);"/>
		</div>
	</form:form>
	<br/>
	</div>
	</div>
	
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>
	
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th hidden="true"><div style="width:auto;text-align:center;" >ID</div></th>
				<th><div style="width:auto;text-align:center;">资金账号</div></th>
				<th><div style="width:auto;text-align:center;">客户号</div></th>
				<th><div style="width:auto;text-align:center;" >名称</div></th>
				<th><div style="width:auto;text-align:center;" >一码通</div></th>
				<th><div style="width:auto;text-align:center;" >所属机构</div></th>
				<th><div style="width:auto;text-align:center;" >总资产</div></th>
				<th><div style="width:auto;text-align:center;" >开户时间</div></th>
				<th><div style="width:auto;text-align:center;" >销户时间</div></th>
				<th><div style="width:auto;text-align:center;" >状态</div></th>
				<th><div style="width:120px;text-align:center;" >操作</div></th>
			</tr>
		</thead>
		<tbody><%request.setAttribute("strEnter", "\n");request.setAttribute("strTab", "\t");%>
		<c:choose>
			<c:when test="${not empty page.list}">
				<c:forEach items="${page.list}" var="cust">
					<tr>
						<td hidden="true">${cust.id}</td>
						<td>${cust.fundid}</td>
						<td>${cust.custid}</td>
						<td>${cust.fundname} </td>
						<td>${cust.ymtCode} </td>
						<td>${cust.orgname} </td>
						<td>${cust.assets} </td>
						<td>${cust.opendate} </td>
						<td>${cust.closedate} </td>
						<td>${fns:getDictLabel(cust.status, 'cust_status', '')}</td>
						<td style="text-align: center;">
							<shiro:hasPermission name="rtc:cust:view">
								<a href="#" onclick="openDialogView('详情', '${ctx}/rtc/cust/form?id=${cust.id}','800px', '450px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i>详情</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="rtc:watch:changeWatch">
								<c:choose>
									<c:when test="${cust.watchStatus == 1}">
										<a href="${ctx}/rtc/watch/cancel1?ymtCode=${cust.ymtCode}" onclick="return confirmx('确定要取消关注吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i>取消关注</a>
									</c:when>
									<c:otherwise>
										<a href="${ctx}/rtc/watch/add1?ymtCode=${cust.ymtCode}" onclick="return confirmx('确定要关注该一码通吗？', this.href)" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>关注</a>
									</c:otherwise>
								</c:choose>
							</shiro:hasPermission>
						</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr class="main_info">
					<td colspan="100" class="center" align="center">没有相关数据</td>
				</tr>
			</c:otherwise>
		</c:choose>
		</tbody>
	</table>
	
	<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	<br/>
	<br/>
	</div>
	</div>
</div>

</body>
</html>