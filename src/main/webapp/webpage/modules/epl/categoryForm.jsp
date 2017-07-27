<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>主题设置</title>
	<!-- 装饰:默认 -->
	<meta name="decorator" content="default"/>
	<link rel="stylesheet" href="${ctxStatic}/bootstrap-multiselect/bootstrap-multiselect.css" />
	<script src="${ctxStatic}/bootstrap-multiselect/bootstrap-multiselect.js"></script>
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
	<form:form id="inputForm" modelAttribute="epl" action="${ctx}/rtc/epl/categoryEdit" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="eplId"/>
		<form:hidden path="eplParents"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		      <tr>
		         <td  class="width-10 active"><label class="pull-right"><font color="red">*</font>规则编号:</label></td>
		         <td class="width-35" ><form:input path="eplId" htmlEscape="false" maxlength="50" class="form-control" disabled="true"/></td>
		         <td  class="width-10 active"><label class="pull-right"><font color="red">*</font>主题类型:</label></td>
		         <td  class="width-35" >
		         	<div  class="myOwnDdl">
						<form:select  path="eplParents2" class="form-control m-b required" multiple="multiple">
							<form:options items="${parentTypes}" itemLabel="category" itemValue="id" htmlEscape="false"/>
						</form:select>
		         	</div>
		         </td>
		      </tr>
		</tbody>
		</table>
	</form:form>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  var parentSelected = "";
			  $("#eplParents2 option:selected").each(function () {
				  if(parentSelected == ""){
					  parentSelected += $(this).val();
				  }else{
					  parentSelected += "," + $(this).val();
				  }
			  });
			  $("#eplParents").val(parentSelected);
			  
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}
		
		$(document).ready(function() {
			//父类规则选项
			var multiSelectOption={ 
			        includeSelectAllOption: true,
			        selectAllValue:"select-all-value",
			        nonSelectedText:"--请选择--",
			        nSelectedText:"个主题",
			        allSelectedText:"全部",
			        selectAllText:"全部主题",
			        maxHeight:300,  
			        numberDisplayed:2,   
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
			$("#eplParents2").multiselect("destroy").multiselect(multiSelectOption).multiselect("select", eplParentsList); 
			
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
</body>
</html>