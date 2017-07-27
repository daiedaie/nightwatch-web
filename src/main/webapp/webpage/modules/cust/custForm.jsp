<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>客户信息详情</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<form:form id="inputForm" modelAttribute="cust" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>资金账号:</label></td>
		         <td  class="width-35" ><form:input path="fundid" htmlEscape="false" maxlength="50" class="form-control required" disabled="true"/></td>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>状态:</label></td>
		         <td class="width-35" >
		         	<form:select path="status" class="form-control m-b required" maxlength="50" disabled="true">
						<form:options items="${fns:getDictList('cust_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
		         </td>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>客户号:</label></td>
		         <td class="width-35" ><form:input path="custid" htmlEscape="false" maxlength="50" class="form-control required" disabled="true"/></td>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>客户名称:</label></td>
		         <td  class="width-35" ><form:input path="fundname" htmlEscape="false" maxlength="50" class="form-control required" disabled="true"/></td>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>一码通:</label></td>
		         <td class="width-35" ><form:input path="ymtCode" htmlEscape="false" maxlength="50" class="form-control required" disabled="true"/></td>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>所属机构:</label></td>
		         <td  class="width-35" ><form:input path="orgname" htmlEscape="false" maxlength="50" class="form-control required" disabled="true"/></td>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>开户时间:</label></td>
		         <td class="width-35" ><form:input path="opendate" htmlEscape="false" maxlength="50" class="form-control required" disabled="true"/></td>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>总资产:</label></td>
		         <td  class="width-35" ><form:input path="assets" htmlEscape="false" maxlength="50" class="form-control required" disabled="true"/></td>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>销户时间:</label></td>
		         <td  class="width-35" ><form:input path="closedate" htmlEscape="false" maxlength="50" class="form-control required" disabled="true"/></td>
		      </tr>
		      <tr>
		      </tr>
		</tbody>
		</table>
	</form:form>
</body>
</html>