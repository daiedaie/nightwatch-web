<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>事件详情</title>
	<!-- 装饰:默认 -->
	<meta name="decorator" content="default"/>
</head>
<body>
	<form:form id="inputForm" modelAttribute="triggerRecord" method="post" class="form-horizontal" 
		action="${ctx}/trigger/record/statisticSave?ymtCode=${triggerRecord.ymtCode}&eplId=${triggerRecord.eplId}&oldStatus=${triggerRecord.status}">
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
			   <tr>
				  <td  class="width-8 active"><label class="pull-right"><font color="red">*</font>一码通账号:</label></td>
				  <td class="width-40" ><form:input disabled="true" path="ymtCode" htmlEscape="false" class="form-control"/></td>
			   </tr>
			  <tr>
				  <td  class="width-8 active"><label class="pull-right"><font color="red">*</font>事件类型:</label></td>
				  <td class="width-40" ><form:textarea disabled="true" path="eplName" htmlEscape="false" rows="2" maxlength="200" class="form-control"/></td>
			  </tr>
			  <tr>
				  <td  class="width-8 active"><label class="pull-right"><font color="red">*</font>处理状态:</label></td>
				  <td class="width-40"><form:radiobuttons path="status" items="${fns:getDictList('trigger_status')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/></td>
			  </tr>
			  <tr>
				  <td  class="width-8 active"><label class="pull-right"><font color="red">*</font>处理意见:</label></td>
				  <td class="width-40"  ><form:textarea path="opinion" htmlEscape="false" rows="2" maxlength="200" class="form-control"></form:textarea></td>
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
			//$("#name").focus();
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