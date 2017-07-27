<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>风控规则配置</title>
	<!-- 装饰:默认 -->
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/bootstrap-multiselect/bootstrap-multiselect.css" />
	<script src="${ctxStatic}/bootstrap-multiselect/bootstrap-multiselect.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<style type="text/css">
    /*自定义宽度*/  
    .myOwnDdl{  
        display:inline-block;  
        width:100%;  
    }  
      
    /* 实现宽度自定义 */  
    .myOwnDdl .btn-group{  
        width:100%;  
    }  
    .myOwnDdl .multiselect {  
        width:100%;  
        text-align:right;  
        margin-top:-5px;  
    }  
    .myOwnDdl ul {  
        width:100%;  
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
    
    .btn-default:hover, .btn-default:focus, .btn-default:active, .btn-default.active, .open .dropdown-toggle.btn-default {
 <%--     background-color: #FFFFFF;
    border-color: #FFFFFF;
    color: #FFFFFF;
     border-top-color: rgb(186, 186, 186);
        border-right-color: rgb(186, 186, 186);
        border-bottom-color: rgb(186, 186, 186);
        border-left-color: rgb(186, 186, 186); --%>
        }
    </style>
</head>
<body>
	<form:form id="inputForm" modelAttribute="epl" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="eplParents"/>
		<form:hidden path="funcStr"/>
		<form:hidden path="eplGroupKeyStr"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		      <tr>
		         <td  class="width-10 active"><label class="pull-right"><font color="red">*</font>规则类别:</label></td>
		         <td  class="width-35" >
					<form:select path="eplBelong" class="form-control m-b required" onchange="changBelong(this);" disabled="true" >
						<form:option value="1" label="事前规则"/>
						<form:option value="2" label="事中规则"/>
						<form:option value="3" label="事后规则"/>
					</form:select>
			     </td>
		         <td  class="width-10 active"><label class="pull-right"><font color="red">*</font>规则编号:</label></td>
		         <td class="width-35" ><form:input path="eplId" htmlEscape="false" maxlength="50" class="form-control" disabled="true"/></td>
		      </tr>
		      <tr >
		         <td  class="width-10 active"><label class="pull-right">最近启用:</label></td>
		         <td  class="width-35" ><form:input path="onlineTime" htmlEscape="false" maxlength="50" class="form-control" disabled="true"/></td>
		         <td  class="width-10 active"><label class="pull-right">最近停用:</label></td>
		         <td  class="width-35" ><form:input path="offlineTime" htmlEscape="false" maxlength="50" class="form-control" disabled="true"/></td>
		      </tr>
		      <tr>
		         <td  class="width-10 active"><label class="pull-right"><font color="red">*</font>所属主题:</label></td>
		         <td  class="width-35" >
		         	<div  class="myOwnDdl">
						<form:select  path="eplParents2" class="form-control m-b required" multiple="multiple" disabled="true" >
							<form:options items="${parentTypes}" itemLabel="category" itemValue="id" htmlEscape="false"/>
						</form:select>
		         	</div>
		         </td>
		         <td  class="width-10 active"><label class="pull-right"><font color="red">*</font>规则名称:</label></td>
		         <td  class="width-35" ><form:input path="eplName" htmlEscape="false" maxlength="50" class="form-control required" disabled="true" /></td>
		      </tr>
		      <tr id="belongSelect">
		         <td  class="width-10 active"><label class="pull-right"><font color="red">*</font>功能点选择:</label></td>
		         <td  class="width-35" >
		         	<div  class="myOwnDdl">
						<form:select  path="funcStr2" class="form-control m-b required" multiple="multiple" disabled="true" >
							<form:options items="${funcList}" itemLabel="funcname" itemValue="funcid"  htmlEscape="false"/>
						</form:select>
		         	</div>
		         </td>
		         <td  class="width-10 active eplGroupKey"><label class="pull-right"><font color="red">*</font>hash规则:</label></td>
		         <td  class="width-35 eplGroupKey">
		         	<div  class="myOwnDdl">
						<form:select  path="eplGroupKeyStr2" class="form-control m-b required" multiple="multiple" disabled="true" >
						</form:select>
		         	</div>
		         </td>
		         <td  class="width-10 active scheduleTime" hidden="hidden"><label class="pull-right"><font color="red">*</font>调度时间:</label></td>
		         <td  class="width-35 scheduleTime" hidden="hidden"><form:input path="scheduleTime" htmlEscape="false" maxlength="20" class="form-control required" disabled="true" /></td>
		      </tr>
		      <tr>
		         <td  class="width-10 active"><label class="pull-right"><font color="red"></font>开启时间:</label></td>
		         <td  class="width-35" ><form:input type="text" path="startTime" class="Wdate form-control" 
		         		onFocus="WdatePicker({beginDate:'%y-%M-01 00:00:00',dateFmt:'HH:mm:ss',alwaysUseStartDate:true})" 
		          		style="width:204px;margin-right: 50px;height: 33px;border: 1px solid #e5e6e7;" disabled="true" /></td>
		         <td  class="width-10 active"><label class="pull-right"><font color="red"></font>结束时间:</label></td>
		         <td  class="width-35" ><form:input type="text" path="endTime" class="Wdate form-control" 
		         		onFocus="WdatePicker({endDate:'%y-%M-01 00:00:00',dateFmt:'HH:mm:ss',alwaysUseStartDate:true})" 
		          		style="width:204px;margin-right: 50px;height: 33px;border: 1px solid #e5e6e7;" disabled="true" /></td>
		      </tr>
		      <tr>
		         <td  class="width-10 active"><label class="pull-right"><font color="red">*</font>启用状态:</label></td>
		         <td  class="width-35" >
		         	<form:radiobuttons path="status" items="${fns:getDictList('epl_status')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required" disabled="true" />
		         </td>
		      </tr>
		      <tr>
		         <td  class="width-10 active"><label class="pull-right"><font color="red">*</font>SQL:</label></td>
				 <td class="width-35" colspan="3"><form:textarea path="epl" htmlEscape="false" rows="2" class="form-control required" disabled="true" /></td>
		      </tr>
		      <tr>
		         <td  class="width-10 active"><label class="pull-right"><font color="red">*</font>中文描述模板:</label></td>
				 <td class="width-35" colspan="3"><form:textarea path="textState" htmlEscape="false" rows="2" maxlength="200" class="form-control required" disabled="true" /></td>
		      </tr>
		      <tr>
		         <td  class="width-10 active"><label class="pull-right">规则描述:</label></td>
				 <td class="width-35" colspan="3"><form:textarea path="eplDescribe" htmlEscape="false" rows="2" maxlength="200" class="form-control" disabled="true" /></td>
		      </tr>
		       <tr>
		         <td  class="width-15 active"><label class="pull-right">阀值列表:</label></td>
				  <td class="width-35" colspan="3">
					  <table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
					  	<thead>
						  <tr>
							  <th hidden="true">编号</th>
							  <th>名称</th>
							  <th>阀值</th>
							  <th>描述</th>
						  </tr>
					  	</thead>
					    <tbody>
						  <c:forEach items="${epl.thresholdList}" var="threshold">
							  <tr>
								  <td value="${threshold.id}" hidden="true">
									  ${threshold.id}
								  </td>
								  <td value="${threshold.thresholdName}">
									  ${threshold.thresholdName}
								  </td>
								  <td value="${threshold.thresholdValue}">
									  ${threshold.thresholdValue}
								  </td>
								  <td value="${threshold.thresholdDescribe}">
									  ${threshold.thresholdDescribe}
								  </td>
							  </tr>
						  </c:forEach>
					    </tbody>
				      </table>
				  </td>
			</tr>
		</tbody>
		</table>
	</form:form>
	
	<script type="text/javascript">
	$(document).ready(function() {
		//父类规则选项
		var multiSelectOption0={ 
		        includeSelectAllOption: true,
		        selectAllValue:"select-all-value",
		        nonSelectedText:"--请选择--",
		        nSelectedText:"个主题",
		        allSelectedText:"全部",
		        selectAllText:"全部主题",
		        maxHeight:300,  
		        numberDisplayed:3,   
		        enableFiltering: true,  
		        selectAllJustVisible: true,
		        optionClass: function(element) {  
		            var value = $(element).parent().find($(element)).index();  
		            if (value%2 == 0) {  
		                return 'even';  
		            }  
		            else {  
		                return 'odd';  
		            }  
		        },  
	            onChange: function(option, checked, select) {

	            },
		}; 
		var eplParentsStr = "${epl.eplParents}";
		var eplParentsList = eplParentsStr == null || eplParentsStr == "" ? "" : eplParentsStr.split(",");
		$("#eplParents2").multiselect("destroy").multiselect(multiSelectOption0).multiselect("select", eplParentsList); 
		
		var multiSelectOption={ 
		        includeSelectAllOption: true,
		        selectAllValue:"select-all-value",
		        nonSelectedText:"--请选择--",
		        nSelectedText:"个功能点",
		        allSelectedText:"全部",
		        selectAllText:"全部功能点",
		        maxHeight:300,  
		        numberDisplayed:3,   
		        enableFiltering: true,  
		        selectAllJustVisible: true  
		}; 
		var funcStr = "${epl.funcStr}";
		var funcList = funcStr == null || funcStr == "" ? "" : funcStr.split(",");
		$("#funcStr2").multiselect("destroy").multiselect(multiSelectOption).multiselect("select", funcList); 
		
		
		//hash键值选项
		var schemaList = ${schemaList};
		schemaList = schemaList instanceof  String ? eval("("+schemaList+")") : schemaList;
		var schemaFieldList = ${schemaFieldList};
		schemaFieldList = schemaFieldList instanceof  String ? eval("("+schemaFieldList+")") : schemaFieldList;
		for(var i=0; i<schemaList.length; i++){
			var schemaId = schemaList[i].schemaId;
			$("#eplGroupKeyStr2").append("<optgroup label='"+schemaList[i].schemaName+"["+schemaList[i].schemaDescribe+"]' class='"+schemaId+"'>");
			for(var j=0; j<schemaFieldList.length; j++){
				if(schemaFieldList[j].schemaId == schemaId){
					$("#eplGroupKeyStr2 optgroup."+schemaId).append("<option value='"+schemaFieldList[j].id+"'>"+
							schemaFieldList[j].fieldName+"["+schemaFieldList[j].fieldDescribe+"]</option>");
				}
			}
		}
		
		var eplGroupKeyStr = "${epl.eplGroupKeyStr}";
		var groupKeyList = eplGroupKeyStr == null || eplGroupKeyStr == "" ? "" : eplGroupKeyStr.split(",");
		var multiSelectOption2={ 
		        includeSelectAllOption: false,
		        selectAllValue:"select-all-value",
		        nonSelectedText:"--请选择--",
		        nSelectedText:"个hash键值",
		        allSelectedText:"全部",
		        selectAllText:"全部键值",
		        maxHeight:300,  
		        numberDisplayed:3,   
		        enableFiltering: true,  
		        selectAllJustVisible: true,
		        enableClickableOptGroups: true,
		        enableCollapsibleOptGroups: true,
		}; 
		$("#eplGroupKeyStr2").multiselect("destroy").multiselect(multiSelectOption2).multiselect("select", groupKeyList); 
	});
	</script>
</body>
</html>