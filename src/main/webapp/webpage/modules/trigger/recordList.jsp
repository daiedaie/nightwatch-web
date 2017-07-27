<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>事件详情</title>
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/bootstrap-multiselect/bootstrap-multiselect.css" />
	<script src="${ctxStatic}/bootstrap-multiselect/bootstrap-multiselect.js"></script>
	<style type="text/css">
	/*自定义宽度*/  
    .myOwnDdl {  
        display:inline-block;  
    }  
      
    /* 实现宽度自定义 */  
    .myOwnDdl .btn-group{  
    }  
    .myOwnDdl .multiselect {  
	    width: 204px;
    	height: 40px;
    	border-left-width: 0px;
   	 	border-bottom-width: 0px;
        text-align:right;  
        margin-top:-5px;  
    }  
    .myOwnDdl ul {  
    }  
    .myOwnDdl .multiselect-selected-text {  
        position:absolute;  
        left:0;  
        right:25px;  
        text-align:left;  
        padding-left:20px;  
    }  
      
    /*控制隔行换色*/  
    .myOwnDll .multiselect-container li.odd {  
        background: #eeeeee;  
    } 
    
   .checkbox input[type="checkbox"]{
    	margin-top: 5px;
    }
	</style>
	<script type="text/javascript">
		function openStatisticForm(ymtCode, status, eplId, eplName){
			openDialog("一键处理", "${ctx}/trigger/record/statisticForm?ymtCode=" + ymtCode +"&status=" + status +"&eplId=" + eplId +"&eplName=" + encodeURI(encodeURI(eplName)),"700px", "400px");
		}
		
		function statisticPage(n,s){
			$("#statisticPageNo").val(n);
			$("#statisticPageSize").val(s);
			$("#statisticYmtCode").val(null);
			$("#statisticEplId").val(null);
			$("#statisticLevel").val(null);
			$("#statisticStatus").val(null);
			$("#searchForm").submit();
	    	return false;
	    }
		
		function contentPage(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			statisticAjax();
	    }
		
		function getSelectTable(row, ymtCode, eplId, level, status){
			$("#pageNo").val(0);
			$("#statisticYmtCode").val(ymtCode);
			$("#statisticEplId").val(eplId);
			$("#statisticLevel").val(level);
			$("#statisticStatus").val(status);
			statisticAjax();
			$(row).parent().find("tr").each(function(){
				$(this).removeAttr("style");
			});
			$(row).css("background-color", "#86EA29");
		}
		
		function statisticAjax(){
			var eplIdForServer = $("#statisticEplId").val();
			if(eplIdForServer == ""){
				var selectedOptions = $("#eplTypes2").find("option:selected");
				for(var index=0; index<selectedOptions.length; index++){
					if(index==0){
						eplIdForServer = $(selectedOptions[index]).val();
					}
				}
			}
			var ymtCodeForServer = $("#statisticYmtCode").val() == "" ? $("#ymtCode").val() : $("#statisticYmtCode").val();
			var levelForServer = $("#statisticLevel").val() == "" ? $("#level").val() : $("#statisticLevel").val();
			var statusForServer = $("#statisticStatus").val() == "" ? $("#status").val() : $("#statisticStatus").val();
			$.ajax({
				type: "POST",
				url: "${ctx}/trigger/record/contentTable?ymtCode="+ymtCodeForServer+"&eplId="+eplIdForServer+
					"&level="+$("#statisticLevel").val()+"&status="+$("#statisticStatus").val()+
					"&startDate="+$("#startDate").val()+"&endDate="+$("#endDate").val()+
					"&pageNo="+$("#pageNo").val()+"&pageSize="+$("#pageSize").val()+
					"&orderBy2="+$("#orderBy2").val(),
				success: function(msg){
					var data = msg instanceof  String ? eval("("+msg+")") : msg;
//					console.log(data);
					
					$("#contentTable tbody").empty();
					for(var i=0; i<data.length; i++){
						if(i==0){
							$("#page").empty().append(data[i].opinion);
						}
						var tr = "<tr><td>" + data[i].triggerTime + "</td>" +
						"<td>" + data[i].ymtCode + "</td>" +
						"<td><a  href='#' title='" + data[i].eplDescribe + "'>" +  data[i].eplName + "</a></td>" +
						"<td>" + data[i].triggerState + "</td>" +
						"<td>" + data[i].level + "</td>" +
						"<td>" + data[i].statusName + "</td>" +
						"<td>" + data[i].dealTime + "</td>" +
						"<td><a href='#' onclick='openDialogView(\"事件详情 - "+data[i].eplName+"\","+ "\"${ctx}/trigger/record/form?id="+data[i].id+"\",\"1000px\",\"600px\");' "+
						"class='btn btn-info btn-xs' ><i class='fa fa-search-plus'>详情</i></a>"+
						"<a href='#' onclick='openDialog(\"事件处理 - "+data[i].eplName+"\","+ "\"${ctx}/trigger/record/form?id="+data[i].id+"\",\"1000px\",\"600px\");' "+
						"class='btn btn-success btn-xs' ><i class='fa fa-edit'>处理</i></a></td></tr>";
						
						$("#contentTable tbody").append($(tr));
					}
					document.getElementById("mao").scrollIntoView();
				}
			});
		}
		
		function statisticReset(){
			$("#statisticPageNo").val(0);
			$("#statisticYmtCode").val(null);
			$("#statisticEplId").val(null);
			$("#statisticLevel").val(null);
			$("#statisticStatus").val(null);
			$("#orderBy2").val("tr.trigger_time DESC");
		}
		/**
		* 统计表排序呢回调函数
		*/
		function orderCallBack(){
			statisticReset();
			sortOrRefresh();
		}
		/**
		* 加载排序按钮
		*/
		function setSort(item){
			$(item+" .sort-column").each(function(){
					$(this).html($(this).text()+" <i class=\"fa fa-sort\"></i>");
			});
		}
		/**
		* 排序图标变动
		*/
		function sortColumn(item, orderBy){
			if ($(item).hasClass(orderBy[0])){
				orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="DESC"?"down":"up";
				$(item).find("i").remove();
				$(item).html($(item).html()+" <i class=\"fa fa-sort-"+orderBy[1]+"\"></i>");
			}
		}
		
		function statisticTableSort(){
			$("#statisticTable th.sort-column").each(function(){
				var orderBy = $("#orderBy").val().split(" ");
				sortColumn(this, orderBy);
			});
		}
		
		function contentTableSort(){
			$("#contentTable th.sort-column").each(function(){
				var orderBy2 = $("#orderBy2").val().split(" ");
				sortColumn(this, orderBy2);
			});
		}
		
		//将选择的值填充到eplType中
		function setEplType(){
			var selectedOptions = $("#eplTypes2").find("option:selected");
			var TypeStr = "";
			for(var index=0; index<selectedOptions.length; index++){
				if(index==0){
					TypeStr += $(selectedOptions[index]).val();
				}else{
					TypeStr += ","+$(selectedOptions[index]).val();
				}
			}
			$("#eplTypes").val(TypeStr);
		}

		//将选中项的上级勾选上
		function getGroupSelect(){
			var multiselectGroups = $("#eplTypes2").parent().find("ul li.multiselect-group");
			$("#eplTypes2 optgroup").each(function (index) {
				var optSelects = $(this).find("option:selected");
				if(optSelects != null && optSelects.length!=0){
					$(this).attr("selected",true);
					$(multiselectGroups[index]).addClass("active");
				}else{
					$(this).attr("selected",false);
					$(multiselectGroups[index]).removeClass("active");
				}
			  });
		}
		
		$(function(){
			setSort("#statisticTable");
			setSort("#contentTable");
			statisticTableSort();
			contentTableSort();

			$("#statisticTable th.sort-column").click(function(){
				var order = $(this).attr("class").split(" ");
				var sort = $("#orderBy").val().split(" ");
				for(var i=0; i<order.length; i++){
					if (order[i] == "sort-column"){order = order[i+1]; break;}
				}
				if (order == sort[0]){
					sort = (sort[1]&&sort[1].toUpperCase()=="DESC"?"ASC":"DESC");
					$("#orderBy").val(order+" DESC"!=order+" "+sort?"":order+" "+sort);
				}else{
					$("#orderBy").val(order+" ASC");
				}
				orderCallBack();
			});
			
			$("#contentTable th.sort-column").click(function(){
				var order = $(this).attr("class").split(" ");
				var sort = $("#orderBy2").val().split(" ");
				for(var i=0; i<order.length; i++){
					if (order[i] == "sort-column"){order = order[i+1]; break;}
				}
				if (order == sort[0]){
					sort = (sort[1]&&sort[1].toUpperCase()=="DESC"?"ASC":"DESC");
					$("#orderBy2").val(order+" DESC"!=order+" "+sort?"":order+" "+sort);
				}else{
					$("#orderBy2").val(order+" ASC");
				}
				
				statisticAjax();
				setSort("#contentTable");
				contentTableSort();
			});
			
			$("#statisticTable th.sort-column").click(function(){
				statisticReset();
			});
			
			//EPL选项
			var categoryList = ${categoryList};
			categoryList = categoryList instanceof  String ? eval("("+categoryList+")") : categoryList;
			var eplTypeList = ${eplTypeList};
			eplTypeList = eplTypeList instanceof  String ? eval("("+eplTypeList+")") : eplTypeList;
//			console.log(categoryList);
//			console.log(eplTypeList);
			for(var i=0; i<categoryList.length; i++){
				var categoryId = categoryList[i].id;
				$("#eplTypes2").append("<optgroup label='"+categoryList[i].category+"' class='"+categoryId+"'>");
//				console.log($("#eplTypes2 optgroup"));
//				console.log($("#eplTypes2 optgroup."+categoryId));
				for(var j=0; j<eplTypeList.length; j++){
					if(eplTypeList[j].parentId == categoryId){
						$("#eplTypes2 optgroup."+categoryId).append("<option value='"+ eplTypeList[j].eplId +"'>"+
								eplTypeList[j].eplName+"</option>");
					}
				}
			}
			
			var eplTypes = "${triggerRecord.eplTypes}";
			$("#eplTypes").val(eplTypes);
			console.log(eplTypes);
			var TypeList = eplTypes == null || eplTypes == "" ? "" : eplTypes.split(",");
//			console.log(TypeList);
			var multiSelectOption={ 
			        includeSelectAllOption: false,
			        selectAllValue:"select-all-value",
			        nonSelectedText:"",
			        nSelectedText:"个规则类型",
			        allSelectedText:"全部",
			        selectAllText:"全部规则类型",
			        maxHeight:300,  
			        numberDisplayed:1,   
			        enableFiltering: true,  
			        selectAllJustVisible: true,
			        enableClickableOptGroups: true,
			        enableCollapsibleOptGroups: true,
			        optionClass: function(element) {  
			            var value = $(element).parent().find($(element)).index();  
			            if (value%2 == 0) {  
			                return 'even';  
			            } else {  
			                return 'odd';  
			            }  
			        },  
		            onChange: function(option, checked, select) {
		            	getGroupSelect();
		            	setEplType();
		            },
			}; 
			$("#eplTypes2").multiselect("destroy").multiselect(multiSelectOption).multiselect("select", TypeList); 
			//将一级选择框折叠
			var lis = $("#eplTypes2").parent().find("ul li");
			for(var i=0; i<lis.length; i++){
				if(! $(lis[i]).hasClass("multiselect-item")){
					$(lis[i]).addClass("multiselect-collapsible-hidden").css("display","none");
				}
			}
			setEplType();
			getGroupSelect();
			
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
			$(".myOwnDdl button").removeClass("btn-default").addClass('btn-white');
		});
</script>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content">
<div class="ibox">
<div class="ibox-title">
		<h5>事件详情 </h5>
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
	<sys:message content="${message}" hideType="1"/>
	
		<!-- 查询条件 -->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="triggerRecord" action="${ctx}/trigger/record/list" method="post" class="form-inline">
		<form:input path="statisticPageNo" type="hidden" value="${statisticPage.pageNo}"/>
		<form:input path="statisticPageSize" type="hidden" value="${statisticPage.pageSize}"/>
		<form:input path="statisticYmtCode" type="hidden"/>
		<form:input path="statisticEplId" type="hidden"/>
		<form:input path="statisticLevel" type="hidden"/>
		<form:input path="statisticStatus" type="hidden"/>
		<form:hidden path="eplTypes" />
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${triggerRecord.orderBy}"/>
		<input id="orderBy2" name="orderBy2" value="tr.trigger_time DESC" type="hidden" />
		
		<div class="form-group">
			<div style="margin-bottom: 10px;" class="form-inline">
				<span>事件类型：</span>
				<div class=" myOwnDdl form-control m-b form-inline" style="width: 204px;margin-right: 50px;padding-top: 4px;margin-bottom: 15px;padding-left: -10;padding-left: 0px;border-left-width: 1px;height: 41px;">
					<form:select  path="eplTypes2" class="" multiple="multiple" >
					</form:select>
				</div>
				<span>事件级别：</span>
				<form:select path="level"  class="form-control m-b" style="width:204px;margin-right: 50px;">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('trigger_level')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span>处理状态：</span>
				<form:select path="status"  class="form-control m-b" style="width:204px;margin-right: 50px;">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('trigger_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			<div>
				<span>开始时间：</span>
				<form:input type="text" path="startDate" class="Wdate" onFocus="WdatePicker({beginDate:'%y-%M-01 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss',alwaysUseStartDate:true})"  
					style="width:204px;margin-right: 50px;height: 33px;border: 1px solid #e5e6e7;"/>
				<span>结束时间：</span>
				<form:input type="text"  path="endDate" class="Wdate" onFocus="WdatePicker({endDate:'%y-%M-01 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss',alwaysUseStartDate:true})" 
					style="width:204px;margin-right: 50px;height: 33px;border: 1px solid #e5e6e7;"/>
				<span> 一 码 通 ：</span>
				<form:input path="ymtCode" htmlEscape="false" maxlength="20"  class=" form-control input-sm" style="width:204px;margin-right: 50px;"
					onkeyup="top.numberValidation(this,true,20);" onafterpaste="top.numberValidation(this,true,20);"/>
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
		<c:if test="${!requestScope.triggerRecord.self}">
			<shiro:hasPermission name="trigger:record:export">
	       		<table:exportExcel url="${ctx}/trigger/record/export"></table:exportExcel><!-- 导出按钮 -->
	       </shiro:hasPermission>
	      
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		 </c:if>
		</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="statisticReset();search();" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="statisticReset();reset();" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>

	<table id="statisticTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
		<thead>
			<tr>
				<th class="sort-column ymt_code">一码通账号</th>
				<th class="sort-column epl_id">事件类型</th>
				<th class="sort-column level">事件级别</th>
				<th class="sort-column status">处理状态</th>
				<th>触发次数</th>
				<th>最新触发时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${statisticPage.list}" var="record">
			<tr onclick='getSelectTable(this,"${record.ymtCode}", "${record.eplId}", "${record.level}", "${record.status}");'>
				<td value="${record.ymtCode}">${record.ymtCode}</td>
				<td value="${record.eplId}">
					<a  href="#" title="${record.eplDescribe}">${record.eplName}</a>
				</td>
				<td value="${record.level}">${fns:getDictLabel(record.level, 'trigger_level', '')}</td>
				<td value="${record.status}">${fns:getDictLabel(record.status, 'trigger_status', '')}</td>
				<td>${record.triggerCnt}</td>
				<td>${record.triggerTime}</td>
				<td>
					<shiro:hasPermission name="trigger:record:edit">
    					<a href="#" onclick='openStatisticForm("${record.ymtCode}", "${record.status}", "${record.eplId}", "${record.eplName}");' class="btn btn-success btn-xs" ><i class="fa fa-edit">一键处理</i></a>
    				</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<!-- 分页代码 -->
	<table:page page="${statisticPage}"></table:page>
	<br/>
	<br/>
	<hr/>
	<div id="mao">
	<table id="contentTable" class="table table-striped table-bordered  table-hover table-condensed  dataTables-example dataTable no-footer">
		<thead>
			<tr>
				<th class="sort-column tr.trigger_time">发生时间</th>
				<th>一码通账号</th>
				<th>事件类型</th>
				<th>事件描述</th>
				<th>事件级别</th>
				<th>处理状态</th>
				<th class="sort-column tr.deal_time">更新时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="record">
			<tr>
				<td>${record.triggerTime}</td>
				<td>${record.ymtCode}</td>
				<td>
					<a  href="#" title="${record.eplDescribe}">${record.eplName}</a>
				</td>
				<td>${record.triggerState}</td>
				<td>${record.level}</td>
				<td>${record.statusName}</td>
				<td>${record.dealTime}</td>
				<td>
					<shiro:hasPermission name="trigger:record:view">
						<a href="#" onclick="openDialogView('事件详情 - ${record.eplName}', '${ctx}/trigger/record/form?id=${record.id}','1000px', '600px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus">详情</i></a>
					</shiro:hasPermission>
					<shiro:hasPermission name="trigger:record:edit">
    					<a href="#" onclick="openDialog('事件处理 - ${record.eplName}', '${ctx}/trigger/record/form?id=${record.id}','1000px', '600px')" class="btn btn-success btn-xs" ><i class="fa fa-edit">处理</i></a>
    				</shiro:hasPermission>
    				<%--<shiro:hasPermission name="trigger:record:del">
						<a href="${ctx}/trigger/record/delete?id=${record.id}" onclick="return confirmx('确认要删除该记录吗？', this.href)"   class="btn btn-danger btn-xs btn-circle"><i class="fa fa-trash"></i></a>
					</shiro:hasPermission>
				--%></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<!-- 分页代码 -->
	<div id="page">
		<table:page page="${page}"></table:page>
	</div>
	</div>
	</div>
</div>
</div>

<!-- 时间插件 -->
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</body>
</html>