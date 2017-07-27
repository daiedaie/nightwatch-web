<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>监管信息详情</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<form:form id="inputForm" modelAttribute="letter" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>客户姓名:</label></td>
		         <td  class="width-35" ><form:input path="custName" htmlEscape="false" maxlength="50" class="form-control required" disabled="true"/></td>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>所属机构:</label></td>
		         <td  class="width-35" ><form:input path="orgname" htmlEscape="false" maxlength="50" class="form-control required" disabled="true"/></td>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>股东号:</label></td>
		         <td class="width-35" ><form:input path="stockholder" htmlEscape="false" maxlength="50" class="form-control required" disabled="true"/></td>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>一码通:</label></td>
		         <td class="width-35" ><form:input path="ymtCode" htmlEscape="false" maxlength="50" class="form-control required" disabled="true"/></td>
			  </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>函件类型:</label></td>
		         <td class="width-35" >
		         	<form:select path="letterType" class="form-control m-b required" maxlength="50" disabled="true">
						<form:options items="${fns:getDictList('letter_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
		         </td>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>交易所:</label></td>
		         <td class="width-35" >
		         	<form:select path="letterFrom" class="form-control m-b required" maxlength="50" disabled="true">
						<form:options items="${fns:getDictList('letter_from')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>		   
				</td>   
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>收函日期:</label></td>
		         <td class="width-35" ><form:input path="receiveTime" htmlEscape="false" maxlength="50" class="form-control required" disabled="true"/></td>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red"></font>原因:</label></td>
				 <td class="width-35" colspan="3"><form:textarea path="reason" htmlEscape="false" rows="2" maxlength="500" class="form-control"></form:textarea></td>
		      </tr>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red"></font>说明:</label></td>
				 <td class="width-35" colspan="3"><form:textarea path="remark" htmlEscape="false" rows="2" maxlength="500" class="form-control"></form:textarea></td>
		      </tr>
		      <tr>
		      </tr>
		</tbody>
		</table>
	</form:form>
</body>
</html>