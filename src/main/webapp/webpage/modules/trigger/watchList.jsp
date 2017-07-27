<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>重点关注客户</title>
	<!-- 装饰:默认 -->
	<meta name="decorator" content="default"/>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content">
<div class="ibox">
<div class="ibox-title">
		<h5>重点关注客户 </h5>
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
	
		<!-- 查询条件 -->
	<div class="row">
	<div class="col-sm-12">
		<form:form id="searchForm" modelAttribute="watch"  method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<div  style="margin-bottom: 10px;">
				<span>开始时间：</span>
				<form:input type="text" path="startDate" class="Wdate" onFocus="WdatePicker({beginDate:'%y-%M-01 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss',alwaysUseStartDate:true})"  style="width:204px;margin-right: 50px;height: 33px;border: 1px solid #e5e6e7;"/>
				<span>结束时间：</span>
				<form:input type="text"  path="endDate" class="Wdate" onFocus="WdatePicker({endDate:'%y-%M-01 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss',alwaysUseStartDate:true})"  style="width:204px;margin-right: 50px;height: 33px;border: 1px solid #e5e6e7;"/>
				<span> 一 码 通 ：</span>
				<form:input path="ymtCode" htmlEscape="false" maxlength="20"  class=" form-control input-sm" style="width:204px;margin-right: 50px;"
					onkeyup="top.numberValidation(this,true,20);" onafterpaste="top.numberValidation(this,true,20);"/>
			</div>
			<div>
				<span>是否关注：</span>
				<form:radiobuttons path="isFollow" items="${fns:getDictList('customer_is_watch')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks"  style="width:204px;margin-right: 50px;height: 33px;border: 1px solid #e5e6e7;"/>
			</div>
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

	<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
		<thead>
			<tr>
				<th class="sort-column ymtCode">一码通</th>
				<th class="sort-column triggerCnt">触发次数</th>
				<th class="sort-column triggerDates">触发天数</th>
				<th class="sort-column triggerLastTime">最新触发时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="watch">
			<tr>
				<td>${watch.ymtCode}</td>
				<td><a href="#" onclick="changePage(${watch.ymtCode == null ? 0 : watch.ymtCode});" >${watch.triggerCnt}</a></td>
				<td>${watch.triggerDates}</td>
				<td>${watch.triggerLastTime}</td>
				<td style="text-align: center;">
					<shiro:hasPermission name="rtc:watch:changeWatch">
						<c:choose>
							<c:when test="${watch.isFollow == 1}">
								<a href="${ctx}/rtc/watch/cancel2?ymtCode=${watch.ymtCode}" onclick="return confirmx('确定要取消关注吗？', this.href)" class="btn btn-danger btn-xs" ><i class="fa fa-trash"></i>取消关注</a>
							</c:when>
							<c:otherwise>
								<a href="${ctx}/rtc/watch/add2?ymtCode=${watch.ymtCode}" onclick="return confirmx('确定要关注该一码通吗？', this.href)" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>关注</a>
							</c:otherwise>
						</c:choose>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	<br/>
	<br/>
	</div>
	</div>
</div>
<!-- 时间插件 -->
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">
/**
 * 点击数字跳转
 * @param time
 * @param eplId
 * @param status
 */
function changePage(ymtCode){
    var   startDate = $("#startDate").val(), 
            endDate = $("#endDate").val(), 
            ymtCode = $("#ymtCode").val() == "" ? ymtCode : $("#ymtCode").val();
	top.openTab("${ctx}/trigger/record?startDate="+startDate+"&endDate="+endDate+"&ymtCode="+ymtCode,"事件详情", false);
}
</script>
</body>
</html>