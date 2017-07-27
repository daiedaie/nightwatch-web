<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>事件统计</title>
	<!-- 装饰:默认 -->
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
    	width: 228px;
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
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content">
<div class="ibox">
<div class="ibox-title">
		<h5>事件统计 </h5>
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
		<form:form id="searchForm" modelAttribute="triggerRecord"  method="post" class="form-inline">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<form:hidden path="eplTypes" value=""/>
			<div class="myOwnDdl form-group">
				<span>开始时间：</span>
				<input type="text" id="startDate" name="startDate" class="Wdate" onFocus="WdatePicker({beginDate:'%y-%M-01 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss',alwaysUseStartDate:true})"  style="width:204px;margin-right: 50px;height: 33px;border: 1px solid #e5e6e7;"/>
				<span>结束时间：</span>
				<input type="text" id="endDate" name="endDate" class="Wdate" onFocus="WdatePicker({endDate:'%y-%M-01 00:00:00',dateFmt:'yyyy-MM-dd HH:mm:ss',alwaysUseStartDate:true})"  style="width:204px;margin-right: 50px;height: 33px;border: 1px solid #e5e6e7;"/>
				<span>事件类型：</span>
				<form:select  path="eplTypes2" class="form-control m-b" multiple="multiple" style="width:204px;margin-right: 79px;">
				</form:select>
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
				<th>事件类型</th>
				<th>已处理</th>
				<th>忽略</th>
				<th>处理中</th>
				<th>未处理</th>
				<th>总数</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="triggerRecord">
			<tr>
				<td>
					<c:if test="${triggerRecord.eplId == null}">
						汇总: 
					</c:if>
					<c:if test="${triggerRecord.eplId != null}">
						${fns:abbr(triggerRecord.eplName,50)}
					</c:if>
				</td>
				<td>
					<a href="#" onclick="changePage(${triggerRecord.eplId == null ? 0 : triggerRecord.eplId},1);" >${triggerRecord.dealCount}</a>
				</td>
				<td>
					<a href="#" onclick="changePage(${triggerRecord.eplId == null ? 0 : triggerRecord.eplId},2);" >${triggerRecord.ignoreCount}</a>
				</td>
				<td>
					<a href="#" onclick="changePage(${triggerRecord.eplId == null ? 0 : triggerRecord.eplId},3);" >${triggerRecord.verificationCount}</a>
				</td>
				<td>
					<a href="#" onclick="changePage(${triggerRecord.eplId == null ? 0 : triggerRecord.eplId},4);" >${triggerRecord.undealCount}</a>
				</td>
				<td>
					<a href="#" onclick="changePage(${triggerRecord.eplId == null ? 0 : triggerRecord.eplId},'');" >${triggerRecord.totalCount}</a>
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
//	console.log($("#eplTypes").val());
}

//将选中项的上级勾选上
function getGroupSelect(){
	var multiselectGroups = $("#eplTypes2").parent().find("ul li.multiselect-group");
	$("#eplTypes2 optgroup").each(function (index) {
		var optSelects = $(this).find("option:selected");
		if(optSelects != null && optSelects.length!=0){
			console.log(optSelects);
			$(this).attr("selected",true);
//			console.log($(multiselectGroups[index]));
			$(multiselectGroups[index]).addClass("active");
//			console.log($(multiselectGroups[index]));
//			console.log($(multiselectGroups[index]).find("input"));
//			$(multiselectGroups[index]).find("input").attr("checked",true);
		}else{
			$(this).attr("selected",false);
			$(multiselectGroups[index]).removeClass("active");
//			$(multiselectGroups[index]).find("input").attr("checked",false);
		}
	  });
}

$(document).ready(function() {
	//EPL选项
	var categoryList = ${categoryList};
	categoryList = categoryList instanceof  String ? eval("("+categoryList+")") : categoryList;
	var eplTypeList = ${eplTypeList};
	eplTypeList = eplTypeList instanceof  String ? eval("("+eplTypeList+")") : eplTypeList;
//	console.log(categoryList);
//	console.log(eplTypeList);
	for(var i=0; i<categoryList.length; i++){
		var categoryId = categoryList[i].id;
		$("#eplTypes2").append("<optgroup label='"+categoryList[i].category+"' class='"+categoryId+"'>");
//		console.log($("#eplTypes2 optgroup"));
//		console.log($("#eplTypes2 optgroup."+categoryId));
		for(var j=0; j<eplTypeList.length; j++){
			if(eplTypeList[j].parentId == categoryId){
				$("#eplTypes2 optgroup."+categoryId).append("<option value='"+ eplTypeList[j].eplId +"'>"+
						eplTypeList[j].eplName+"</option>");
			}
		}
	}
	
	var eplTypes = "${triggerRecord.eplTypes}";
	$("#eplTypes").val(eplTypes);
	//console.log(eplTypes);
	var TypeList = eplTypes == null || eplTypes == "" ? "" : eplTypes.split(",");
//	console.log(TypeList);
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

/**
 * 点击数字跳转
 * @param time
 * @param eplId
 * @param status
 */
function changePage(eplId,status){
    var   startDate = $("#startDate").val(), 
            endDate = $("#endDate").val(), 
            eplId = eplId == 0 ? "" : eplId;
            eplTypes = $("#eplTypes").val(),
            status = status;
            //console.log(eplTypes);
	top.openTab("${ctx}/trigger/record?startDate="+startDate+"&endDate="+endDate+"&eplId="+eplId+
			"&eplTypes="+eplTypes+"&status="+status,"事件详情", false);
};
</script>
</body>
</html>