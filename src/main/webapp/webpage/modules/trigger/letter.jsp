<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>监管函信息查询</title>
	<meta name="decorator" content="default"/>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content">
<div class="ibox">
<div class="ibox-title">
		<h5>监管查询</h5>
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
	<form:form id="searchForm" modelAttribute="letter" action="${ctx}/rtc/letter/list" method="post"  class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group" style="margin-bottom: 10px;">
			<span>客户名称：</span>
			<form:input path="custName" htmlEscape="false" maxlength="50"  class=" form-control input-sm" style="width:204px;margin-right: 50px;"/>
			<span>开始时间：</span>
			<form:input type="text" path="startDate" class="Wdate" onFocus="WdatePicker({beginDate:'%y-%M-01 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss',alwaysUseStartDate:true})"  
				style="width:204px;margin-right: 50px;height: 33px;border: 1px solid #e5e6e7;"/>
			<span>结束时间：</span>
			<form:input type="text"  path="endDate" class="Wdate" onFocus="WdatePicker({endDate:'%y-%M-01 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss',alwaysUseStartDate:true})" 
				style="width:204px;margin-right: 50px;height: 33px;border: 1px solid #e5e6e7;"/>
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
				<th><div style="width:auto;text-align:center;">姓名</div></th>
				<th><div style="width:auto;text-align:center;">股东号</div></th>
				<th><div style="width:auto;text-align:center;" >一码通</div></th>
				<th><div style="width:auto;text-align:center;" >所属机构</div></th>
				<th><div style="width:auto;text-align:center;" >函件类型</div></th>
				<th><div style="width:auto;text-align:center;" >交易所</div></th>
				<th><div style="width:auto;text-align:center;" >收函日期</div></th>
				<th><div style="width:auto;text-align:center;" >原因</div></th>
				<th><div style="width:auto;text-align:center;" >事件类型</div></th>
				<th><div style="width:auto;text-align:center;" >触发次数</div></th>
				<th><div style="width:auto;text-align:center;" >操作</div></th>
			</tr>
		</thead>
		<tbody><%request.setAttribute("strEnter", "\n");request.setAttribute("strTab", "\t");%>
		<c:choose>
			<c:when test="${not empty page.list}">
				<c:forEach items="${page.list}" var="letter">
					<tr>
						<td hidden="true">${letter.id}</td>
						<td>${letter.custName}</td>
						<td>${letter.stockholder}</td>
						<td>${letter.ymtCode} </td>
						<td>${letter.orgname} </td>
						<td>${fns:getDictLabel(letter.letterType, 'letter_type', '')} </td>
						<td>${fns:getDictLabel(letter.letterFrom, 'letter_from', '')} </td>
						<td>${letter.receiveTime} </td>
						<td>${letter.reason} </td>
						<td>${letter.eplName} </td>
						<td><a href="#" onclick="changePage(${letter.eplId == null ? 0 : letter.eplId},${letter.ymtCode == null ? 0 : letter.ymtCode});" >${letter.triggerCnt}</a></td>
						<td style="text-align: center;">
							<shiro:hasPermission name="rtc:letter:view">
								<a href="#" onclick="openDialogView('详情', '${ctx}/rtc/letter/form?id=${letter.id}','800px', '550px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i>详情</a>
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
<!-- 时间插件 -->
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">
	//将日期转换为时间字符串:yyyy-MM-dd HH:mm:ss
	var formatDateTime = function (date) {  
		var y = date.getFullYear();  
		var m = date.getMonth() + 1;  
		m = m < 10 ? ('0' + m) : m;  
		var d = date.getDate();  
		d = d < 10 ? ('0' + d) : d;  
		var h = date.getHours();  
		h=h < 10 ? ('0' + h) : h;  
		var minute = date.getMinutes();  
		minute = minute < 10 ? ('0' + minute) : minute;  
		var second=date.getSeconds();  
		second=second < 10 ? ('0' + second) : second;  
		return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second;  
	};  
	
	// 对Date的扩展，将 Date 转化为指定格式的String
	// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
	// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
	// 例子：
	// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
	// (new Date()).Format("yyyy-M-d h:m:s.S")   ==> 2006-7-2 8:9:4.18
	Date.prototype.Format = function (fmt) { //author: meizz
	  var o = {
	    "M+": this.getMonth() + 1, //月份
	    "d+": this.getDate(), //日
	    "h+": this.getHours(), //小时
	    "m+": this.getMinutes(), //分
	    "s+": this.getSeconds(), //秒
	    "q+": Math.floor((this.getMonth() + 3) / 3), //季度
	    "S": this.getMilliseconds() //毫秒
	  };
	  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	  for (var k in o)
	  if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	  return fmt;
	};
	
	function addDate(date,days){ 
		var d=new Date(date); 
		d.setDate(d.getDate()+days); 
		return new Date(d);
	}
	/**
	 * 点击数字跳转
	 * @param time
	 * @param eplId
	 * @param status
	 */
	function changePage(eplId,ymtCode){
	    var   startDate = $("#startDate").val(), 
	            endDate = $("#endDate").val(), 
	            eplId = eplId == 0 ? "" : eplId;
	            status = status;
	            //console.log(eplTypes);
		if(ymtCode != 0) top.openTab("${ctx}/trigger/record?startDate="+startDate+"&endDate="+endDate+"&eplId="+eplId+"&ymtCode="+ymtCode,"事件详情", false);
	};
	
	$(function(){
		if($("#startDate").val() == "") $("#startDate").val(formatDateTime(addDate(new Date(), -7))); 
		if($("#endDate").val() == "") $("#endDate").val(new Date().Format("yyyy-MM-dd hh:mm:ss")); 
	});
	
</script>
</body>
</html>