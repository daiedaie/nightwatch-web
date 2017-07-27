<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>功能点配置</title>
	<meta name="decorator" content="default"/>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content">
<div class="ibox">
<div class="ibox-title">
		<h5>功能点配置</h5>
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
	<form:form id="searchForm" action="${ctx}/rtc/func/list" method="post"  class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>关键词：&nbsp;</span>
				<input id="keyword" name="keyword" type="text" maxlength="20" class="form-control input-sm"/>
		 </div>
	</form:form>
	<br/>
	</div>
	</div>
	
	
		<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="rtc:func:add">
				<table:addRow url="${ctx}/rtc/func/form" title="菜单" height="500px"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
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
				<th hidden="true"><div style="width:auto;text-align:center;" >编号</div></th>
				<th><div style="width:auto;text-align:center;">名称</div></th>
				<th><div style="width:auto;text-align:center;">类型</div></th>
				<th><div style="width:auto;text-align:center;" >SQL</div></th>
				<th><div style="width:120px;text-align:center;" >操作</div></th>
			</tr>
		</thead>
		<tbody><%request.setAttribute("strEnter", "\n");request.setAttribute("strTab", "\t");%>
		<c:choose>
			<c:when test="${not empty page.list}">
				<c:forEach items="${page.list}" var="func">
					<tr>
						<td hidden="true">${func.id}</td>
						<td>${func.funcname}</td>
						<td>${func.schemaDescribe}</td>
						<td>${func.funcsql} </td>
						<td style="text-align: center;width:209px;">
							<shiro:hasPermission name="rtc:func:edit">
								<a href="#" onclick="openDialog('编辑', '${ctx}/rtc/func/form?id=${func.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>编辑</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="rtc:func:field">
								<a href="#" onclick="openDialogView('字段列表', '${ctx}/rtc/func/field?id=${func.id}&funcid=${func.funcid}','800px', '550px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>字段</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="rtc:func:del">
								<a href="${ctx}/rtc/func/delete?id=${func.id}&funcid=${func.funcid}" onclick="return confirmx('要删除该功能点吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i>删除</a>
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