<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>字段列表</title>
	<!-- 装饰:默认 -->
	<meta name="decorator" content="default"/>
</head>
<body>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
	  	<thead>
		  <tr>
			  <th hidden="true">编号</th>
			  <th>名称</th>
			  <th>统计字段</th>
			  <th>查询字段</th>
			  <th>描述</th>
			  <th>操作</th>
		  </tr>
	  	</thead>
	    <tbody>
			<c:choose>
			   <c:when test="${not empty list}">
				   <c:forEach items="${list}" var="field">
						  <tr ondblclick="clickRow(this, ${field.funcid})">
							  <td value="${field.id}"  hidden="true">
								  ${field.id}
							  </td>
							  <td value="${field.fieldName}">
								  ${field.fieldName}
							  </td>
							  <td value="${field.isStatistic}">
								  ${fns:getDictLabel(field.isStatistic, 'func_field_is_statistic', '')}
							  </td>
							  <td value="${field.isCondition}">
							  	<div>
								   <label class="checkbox-inline"><input type="radio" name="isCondition ${field.id}" id="isCondition1 ${field.id}" value=1> 是</label>
								   <label class="checkbox-inline"><input type="radio" name="isCondition ${field.id}" id="isCondition2 ${field.id}"  value=2> 否</label>
								</div>
							  </td>
							  <td value="${field.fieldDescribe}" class="canEdit">
								  ${field.fieldDescribe}
							  </td>
		   					  <td style="text-align: center;width:110px;" class="canEdit">
								  <a href="#" onclick="clickRow(this, ${field.funcid})" ><i class="fa fa-edit"></i>修改</a>
							  </td>
						  </tr>
				   </c:forEach>
			   </c:when>
			   <c:otherwise>
				   <tr class="main_info">
					   <td colspan="100" class="center" align="center" id="replaceInfo">数据格式没有定义</td>
				   </tr>
			   </c:otherwise>
	   		</c:choose>
    	</tbody>
     </table>
	 <script type="text/javascript" >
	 	$(function(){
	 		setRadio();
	 	})
	 	
	 	function setRadio(){
	 		$("tbody tr").each(function(){
	 			if($($(this).find("td")[3]).attr("value") ==1){
//	 				console.log($("input[type=radio][name='isCondition "+$($(this).find("td")[0]).attr("value")+"'][value=1]"));
			 		$("input[type=radio][name='isCondition "+$($(this).find("td")[0]).attr("value")+"'][value=1]").attr("checked","checked");
	 			}else{
	 				$("input[type=radio][name='isCondition "+$($(this).find("td")[0]).attr("value")+"'][value=2]").attr("checked","checked");
	 			}
				if($($(this).find("td")[2]).attr("value") != 1){
					$("input[type=radio][name='isCondition "+$($(this).find("td")[0]).attr("value")+"'][value=1]").attr("disabled",true);
					$("input[type=radio][name='isCondition "+$($(this).find("td")[0]).attr("value")+"'][value=2]").attr("disabled",true);
				}
	 		});
	 	}
	 
		function saveRow(row, url, funcid) {
			var tds = $(row).parent().parent().find("td.canEdit input");
			var parent = $(row).parent().parent();
			var rowId = $(parent.find("td")[0]).attr("value");
			var fieldName = $(parent.find("td")[1]).attr("value");
			var isStatistic = $(parent.find("td")[2]).attr("value");
			var isStatisticText = $(parent.find("td")[2]).text();
			var isCondition = $(parent.find("input:radio:checked")).attr("value");
			var field = {
				id:rowId,	
				funcid:funcid,
				fieldName:fieldName,
				isStatistic:isStatistic,
				isCondition:isCondition,
				fieldDescribe:$($(row).parent().parent().find("td.canEdit input")).val(),
			};
//			console.log(field);
			confirmx('确定要保存该字段吗？', function(){
				$.ajax({
					type: "POST",
					data:field,
					url: url,
					success: function(msg){
						var data = msg;
						if(data instanceof  String){
							data=eval("("+msg+")");
						}
//						console.log($(row).parent().parent().find("td.canEdit"));
						$(parent.find("td")[3]).attr("value", data.isCondition);
						$($(row).parent().parent().find("td.canEdit")[0]).empty().attr("value", data.fieldDescribe).append(data.fieldDescribe);
						var optionTr = "<a href='#' onclick='clickRow(this, "+funcid+")'><i class='fa fa-edit'></i>修改</a>";
						$($(row).parent().parent().find("td.canEdit")[1]).empty().append($(optionTr));
						setRadio();
/**						
						var ts = "<td value='" + data.id + "' hidden='true'>" + data.id + "</td>" +
						"<td value='" + data.fieldName + "'>" + data.fieldName + "</td>" +
						"<td value='" + data.isStatistic + "'>" + isStatisticText + "</td>" +
						"<td value='" + data.fieldDescribe + "'>" + data.fieldDescribe + "</td>" +
						"<td style='text-align: center;width:110px;'>"+
						"<a href='#' onclick='clickRow(this, "+funcid+")'><i class='fa fa-edit'></i>修改</a></td>";
						
						if($("#replaceInfo").text() == "数据格式没有定义"){
//							console.log($("#replaceInfo").parent().parent());
							var body = $("#replaceInfo").parent().parent();
							body.empty();
							body.append("<tr ondblclick='clickRow(this,"+funcid+")'>"+ts+"</tr>");
						}else{
							parent.empty();
							parent.append($(ts));
						}**/
					}
				});
	
			});
		}
	
		function clickRow(row,funcid) {
			var tds = $(row).find("td");
			if(tds == null || tds.length<1){
				tds = $(row).parent().parent().find("td");
				row = $(row).parent().parent();
			}
/**			var inputs = $(row).find("td input .canEdit");
			var rowId = $(tds[0]).attr("value") ? $(tds[0]).attr("value") : '';
			var fieldName = $(tds[1]).attr("value");
			var isStatistic = $(tds[2]).attr("value");
			var isStatisticTrxt = isStatistic == 1 ? "是" : "否";
			console.log($(inputs[0]).val());
			console.log($($(row).find(".canEdit")[0]).attr("value"));
			var fieldDescribe = $(inputs[0]).val()===undefined ? $($(row).find(".canEdit")[0]).attr("value") : $(inputs[0]).val();
			
			var tr= "<td value='" + rowId + "' hidden='true'>" + rowId + "</td>" +
						"<td value='" + fieldName + "'>" + fieldName + "</td>" +
						"<td value='" + isStatistic + "'>" + isStatisticTrxt + "</td>" +
						"<td value='" + fieldDescribe + "'><input value='" + fieldDescribe + "'maxlength='50' class='form-control required'/></td>" +
						"<td style='text-align: center;width:110px;'>" +
						"<a href='#' onclick='saveRow(this, \"${ctx}/rtc/func/saveField?funcid="+funcid+"\")'><i class='fa fa-edit'></i>保存</a>" +
						"</td>";
//			$(row).empty();
//			$(row).append($(tr));
			**/
			
			var inputs = $(row).find("td.canEdit input");
			var fieldDescribe = $(inputs[0]).val()===undefined ? $($(row).find(".canEdit")[0]).attr("value") : $(inputs[0]).val();
			var fieldDescribeTr = "<input value='" + fieldDescribe + "'maxlength='50' class='form-control required'/>";
			$($(row).find(".canEdit")[0]).empty().attr("value", fieldDescribe).append($(fieldDescribeTr));
//			console.log($(row).find(".canEdit")[0]);
			var optionTr = "<a href='#' onclick='saveRow(this, \"${ctx}/rtc/func/saveField?funcid="+funcid+"\", "+funcid+")'><i class='fa fa-edit'></i>保存</a>";
			$($(row).find(".canEdit")[1]).empty().append($(optionTr));
		}
	</script>
	</body>
</html>