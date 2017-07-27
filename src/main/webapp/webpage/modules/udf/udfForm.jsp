<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>风控规则配置</title>
	<!-- 装饰:默认 -->
	<meta name="decorator" content="default"/>
</head>
<body>
	<form:form id="inputForm" modelAttribute="udf" action="${ctx}/rtc/udf/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		      <tr>
		         <td  class="width-10 active"><label class="pull-right"><font color="red">*</font>函数全类名:</label></td>
		         <td class="width-35" ><form:input id="methodName" path="methodName" htmlEscape="false" maxlength="50" class="form-control required"/></td>
		         <td  class="width-10 active"><label class="pull-right"><font color="red">*</font>缓存生命周期:</label></td>
		         <td  class="width-35" ><form:input path="maxAgeSeconds" htmlEscape="false" maxlength="50" class="form-control required"/></td>
		      </tr>
		      <tr>
		         <td  class="width-10 active"><label class="pull-right"><font color="red">*</font>缓存清除策略:</label></td>
		         <td  class="width-35" >
		         	<form:select path="cacheReferenceType" class="form-control m-b required" maxlength="50">
		         		<form:option value="" label="请选择清除策略"/>
						<form:options items="${fns:getDictList('cache_reference_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
		         </td>
		         <td  class="width-10 active"><label class="pull-right"><font color="red">*</font>缓存清除间隔:</label></td>
		         <td  class="width-35" ><form:input path="purgeIntervalSeconds" htmlEscape="false" maxlength="50" class="form-control required"/></td>
		      </tr>
		      <tr>
		         <td  class="width-10 active"><label class="pull-right"><font color="red">*</font>函数描述:</label></td>
				 <td class="width-35" colspan="3"><form:textarea path="methodDescribe" htmlEscape="false" rows="2" maxlength="200" class="form-control"/></td>
		      </tr>
		</tbody>
		</table>
	</form:form>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		$(document).ready(function() {
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
		});
	</script>
</body>
</html>