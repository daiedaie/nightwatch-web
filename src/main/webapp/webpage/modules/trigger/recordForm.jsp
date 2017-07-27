<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>事件详情</title>
	<!-- 装饰:默认 -->
	<meta name="decorator" content="default"/>
</head>
<body>
	<form:form id="inputForm" modelAttribute="triggerRecord" action="${ctx}/trigger/record/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<input id="orderBy" name="orderBy" value="timestamp DESC" type="hidden" />
		<%--<table:sortColumn id="orderBy" name="orderBy" value="${triggerRecord.orderBy}" callback="changePage();"/>
		--%><sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody><%--
		      <tr hidden="true">
		         <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>事件类型：</label></td>
		         <td class="width-35" ><form:input path="eplName" htmlEscape="false" maxlength="50" disabled="true" class="form-control required"/></td>
				  <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>事件级别:</label></td>
				  <td class="width-35" ><form:radiobuttons path="level" items="${fns:getDictList('trigger_level')}" itemLabel="label" itemValue="value" htmlEscape="false" disabled="true"  class="i-checks required"/></td>
		      </tr>
		       <tr hidden="true">
		         <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>一码通账号：</label></td>
		         <td class="width-35" ><form:input path="ymtCode" htmlEscape="false" maxlength="50" disabled="true" class="form-control required"/></td>
		         <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>发生时间：</label></td>
		         <td class="width-35" ><form:input path="triggerTime" htmlEscape="false" maxlength="50" disabled="true" class="form-control required"/></td>
		      </tr>
			  <tr hidden="true">
				  <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>规则描述:</label></td>
				  <td class="width-35" colspan="3"><form:textarea disabled="true" path="eplDescribe" htmlEscape="false" rows="2" maxlength="200" class="form-control"/></td>
			  </tr>
			  --%>
			  <tr>
				  <td  class="width-10 active"><label class="pull-right"><font color="red">*</font>事件描述:</label></td>
				  <td class="width-40" ><form:textarea disabled="true" path="triggerState" htmlEscape="false" rows="2" maxlength="200" class="form-control"/></td>
			  </tr>
			  <tr>
				  <td  class="width-10 active">	<label class="pull-right">交易统计：</label></td>
				  <td class="width-40" >${funcs}</td>
			  </tr>
			  <tr>
				  <td  class="width-10 active">	<label class="pull-right">交易详情：</label></td>
				  <td class="width-40" >
				  	<div id="mao">
						  <table id="detailTable"  class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
						  	${detail}
						  </table>
				  	</div>
				  </td>
			  </tr>
			  <tr>
				  <td  class="width-10 active"><label class="pull-right"><font color="red">*</font>处理状态:</label></td>
				  <td class="width-40"><form:radiobuttons path="status" items="${fns:getDictList('trigger_status')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/></td>
			  </tr>
			  <tr>
				  <td  class="width-10 active"><label class="pull-right"><font color="red">*</font>处理意见:</label></td>
				  <td class="width-40" ><form:textarea path="opinion" htmlEscape="false" rows="2" maxlength="200" class="form-control"></form:textarea></td>
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
			
		//排序点击回调函数
		function changePage(){
			console.log($("#orderBy").val()+1);
	//		var orderBy = $("#orderBy").val() == "" ? "fundid DESC" : $("#orderBy").val();
	//		console.log($("#orderBy").val()+2);
			window.location.href="${ctx}/trigger/record/form?id="+$("#id").val()+"&orderBy="+orderBy; 
		}
		
		//导出全部详情
		$("#detailExport").click(function(){
			top.layer.confirm('确认要导出Excel吗?', {icon: 3, title:'系统提示'}, function(index){
			   	window.open("${ctx}/trigger/record/detailExport?id="+$("#id").val());
				top.layer.close(index);
			});
		});
		
		//点击显示相应详情表
		function clickRow(row, funcId){
			var tds = $(row).find("td");
			var condition = "";
			for(var i=0; i<tds.length; i++){
				var fieldName = $(tds[i]).attr("value");
				if(fieldName != null && fieldName != ""){
					var fieldValue = $(tds[i]).text();
					if(condition == ""){
						condition += fieldName + ":'" + fieldValue+"'";
					}else{
						condition += "," + fieldName + ":'" + fieldValue+"'";
					}
				}
			}
			
			$.ajax({
				type: "POST",
				url: "${ctx}/trigger/record/detailTable?id="+$("#id").val()+"&funcId="+funcId+"&condition="+condition,
				success: function(msg){
					$("#detailTable").empty().append($(msg));
					setSort("#detailTable ");
					$(row).parent().find("tr").each(function(){
						$(this).removeAttr("style");
					});
					$(row).css("background-color", "#86EA29");
					document.getElementById("mao").scrollIntoView();
//					iTabSort();
				}
			});
		}
		
		//加载排序按钮
		function setSort(item){
			$(item+".sort-column").each(function(){
					$(this).html($(this).html()+" <i class=\"fa fa-sort\"></i>");
			});
		}
		
		//切换排序图标
		function iTabSort(){
			var orderBy = $("#orderBy").val().split(" ");
//			console.log("============="+orderBy+"===========");
			$(".sort-column").each(function(){
				if ($(this).hasClass(orderBy[0])){
//					console.log(orderBy[1]+orderBy[0]);
					orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="DESC"?"down":"up";
					$(this).find("i").remove();
					$(this).html($(this).html()+" <i class=\"fa fa-sort-"+orderBy[1]+"\"></i>");
//					console.log($(this).find("i"));
				}
			});
		}
			
			function sortColumn(item){
				var order = $(item).attr("class").split(" ");
				var sort = $("#orderBy").val().split(" ");
//				console.log("order1 : "+order);
				for(var i=0; i<order.length; i++){
					if (order[i] == "sort-column"){
						order = order[i+1]; 
						break;
					}
				}
				
//				console.log("order2 : "+order);
//				console.log("sort : "+sort);
				if (order == sort[0]){
					sort = (sort[1]&&sort[1].toUpperCase()=="DESC"?"ASC":"DESC");
//					console.log("sort2 : "+sort);
//					console.log("orderSort1 : "+order+" DESC");
//					console.log("orderSort2 : "+order+" "+sort);
					var orderby1 = order+" DESC"!=order+" "+sort ? order+" ASC" : order+" "+sort;
//					console.log(orderby1);
					$("#orderBy").val(orderby1);
				}else{
					$("#orderBy").val(order+" ASC");
				}
//				console.log($("#orderBy").val());
				iTabSort();
				
				var tableId = $(item).parent().parent().parent().attr("id");
				var iCol = $(item).attr("colNum");
				var dataType = "int";
				if("timestamp" == order){
					dataType = "date";
				}
//				console.log(tableId+" : "+iCol+" : "+dataType);
				sortAble(tableId, iCol, dataType);
			}
			
		      function ieOrFireFox(ob){  
		          if (ob.textContent != null)  
		          return ob.textContent;  
		          var s = ob.innerText;  
		          return s.substring(0, s.length);  
		      }  
		        
		      //排序 tableId: 表的id,iCol:第几列 ；dataType：iCol对应的列显示数据的数据类型  
		      function sortAble(tableId, iCol, dataType) {  
		          var table = document.getElementById(tableId);  
		          var tbody = table.tBodies[0];  
		          var colRows = tbody.rows;  
		          var aTrs = new Array;  
		               //将将得到的列放入数组，备用  
		          for (var i=0; i < colRows.length; i++) {  
		              aTrs[i] = colRows[i];  
		          }  
		                         
		          //判断上一次排列的列和现在需要排列的是否同一个。  
		          if (table.sortCol == iCol) {  
		              aTrs.reverse();
		          } else {  
		              //如果不是同一列，使用数组的sort方法，传进排序函数  
		              aTrs.sort(compareEle(iCol, dataType));  
		          }  
		                
		          var oFragment = document.createDocumentFragment();  
		                        
		          for (var i=0; i < aTrs.length; i++) {  
		              oFragment.appendChild(aTrs[i]);  
		          }                  
		          tbody.appendChild(oFragment);  
		          //记录最后一次排序的列索引  
		          table.sortCol = iCol;  
		      }  
		      //将列的类型转化成相应的可以排列的数据类型  
		      function convert(sValue, dataType) {  
		          switch(dataType) {  
		          case "int":  
		              return parseInt(sValue);  
		          case "float":  
		              return parseFloat(sValue);  
		          case "date":  
		              return stringToDate(sValue);  
		          default:  
		              return sValue.toString();  
		          }  
		      }  
		                    
		      //排序函数，iCol表示列索引，dataType表示该列的数据类型  
		      function compareEle(iCol, dataType) {  
		          return  function (oTR1, oTR2) {  
		              var vValue1 = convert(ieOrFireFox(oTR1.cells[iCol]), dataType);  
		              var vValue2 = convert(ieOrFireFox(oTR2.cells[iCol]), dataType);  
		              if (vValue1 < vValue2) {  
		                  return -1;  
		              } else if (vValue1 > vValue2) {  
		                  return 1;  
		              } else {  
		                  return 0;  
		              }  
		       	 };  
		      }
		      
		      /*'yyyy-MM-dd HH:mm:ss'格式的字符串转日期*/
		      function stringToDate(str){
		          var tempStrs = str.split(" ");
		          var dateStrs = tempStrs[0].split("-");
		          var year = parseInt(dateStrs[0], 10);
		          var month = parseInt(dateStrs[1], 10) - 1;
		          var day = parseInt(dateStrs[2], 10);
		          var timeStrs = tempStrs[1].split(":");
		          var hour = parseInt(timeStrs [0], 10);
		          var minute = parseInt(timeStrs[1], 10);
		          var second = parseInt(timeStrs[2], 10);
		          var date = new Date(year, month, day, hour, minute, second);
		          return date;
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

			setSort("");
			iTabSort();
//			$(".sort-column").click(sortColumn(this));
		});
	</script>
</body>
</html>