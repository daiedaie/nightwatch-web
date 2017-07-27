package com.jzsec.modules.trigger.web;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jzsec.common.utils.DateUtils;
import com.jzsec.modules.func.entity.Func;
import com.jzsec.modules.func.entity.FuncField;
import com.jzsec.modules.func.service.FuncService;
import net.sf.json.JSONObject;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jzsec.common.mapper.JsonMapper;
import com.jzsec.common.persistence.Page;
import com.jzsec.common.utils.StringUtils;
import com.jzsec.common.utils.excel.ExportExcel;
import com.jzsec.common.web.BaseController;
import com.jzsec.modules.epl.entity.Epl;
import com.jzsec.modules.eplCategory.entity.EplCategory;
import com.jzsec.modules.eplCategory.service.EplCategoryService;
import com.jzsec.modules.schema.entity.Schema;
import com.jzsec.modules.schema.entity.SchemaField;
import com.jzsec.modules.trigger.entity.TriggerRecord;
import com.jzsec.modules.trigger.service.TriggerRecordService;

/**
 * 风控触发记录控制层
 * @author 劉 焱
 * @date 2016-7-25
 */
@Controller
@RequestMapping(value = "${adminPath}/trigger")
public class TriggerRecordController extends BaseController {
	@Autowired
	private TriggerRecordService recordService;
	@Autowired
	private EplCategoryService categoryService;
	@Autowired
	private FuncService funcService;
	
	@ModelAttribute
	public TriggerRecord get(@RequestParam(required=false) String id) {
		TriggerRecord entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = recordService.get(id);
		}
		if (entity == null){
			entity = new TriggerRecord();
		}
		return entity;
	}
	/**
	 * 登录进入主页面
	 * @return
	 */
	@RequestMapping("home")
	public String info(){
		return  "modules/trigger/home";
	}
	
	@RequiresPermissions("trigger:record:list")
	@RequestMapping(value = {"record/list", "record", ""})
	public String triggerRecordList(TriggerRecord triggerRecord, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		System.out.println(triggerRecord);
		List<Epl> eplCategories = categoryService.getEplTypeRoleList();
		model.addAttribute("eplCategories", eplCategories);
		triggerRecord.setEplList(eplCategories);
		
//		获取EPL父类选项
		List<EplCategory> categoryList = categoryService.getEplParentTypeRoleList();
		String categoryListStr =  JsonMapper.getInstance().toJson(categoryList);
		model.addAttribute("categoryList",  categoryListStr);
		
//		获取EPL选项
		List<Epl> eplTypeList = categoryService.getParentAndEplTypeRoleList();
		String eplTypeListStr =  JsonMapper.getInstance().toJson(eplTypeList);
		model.addAttribute("eplTypeList",  eplTypeListStr);

		if(triggerRecord.getEplId() != null){
			triggerRecord.setEplTypes(triggerRecord.getEplId()+"");
		}
		
		if(triggerRecord.getEplTypes() != null && !"".equals(triggerRecord.getEplTypes().trim())){
			List<Epl> eplTypeRequestList = new ArrayList<Epl>();
			List<String> eplTypeIdList  = Arrays.asList(triggerRecord.getEplTypes().split(","));
			System.out.println(eplTypeIdList);
			for(String eplTypeId : eplTypeIdList){
				if(!"".equals(eplTypeId.trim())){
					Epl epl = new Epl();
					epl.setEplId(Integer.parseInt(eplTypeId));
					eplTypeRequestList.add(epl);
				}
			}
			triggerRecord.setEplList(eplTypeRequestList);
		}
		
		List<TriggerRecord> eplList = recordService.findTriggerEpl(triggerRecord);
		Page<TriggerRecord> statisticPage = recordService.findStatisticRecord(new Page<TriggerRecord>(request, response), triggerRecord, eplList);
		String orderBy2 = request.getParameter("orderBy2");
		Page<TriggerRecord> page = recordService.findRecord(new Page<TriggerRecord>(request, response), triggerRecord, eplList, orderBy2);
		
		model.addAttribute("statisticPage", statisticPage);
		model.addAttribute("page", page);
		
		return "modules/trigger/recordList";
	}
	/**
	 * 一键处理触发统计表
	 * @param triggerRecord
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions(value={"trigger:record:add","trigger:record:edit"},logical=Logical.OR)
	@RequestMapping(value = "record/statisticForm")
	public String statisticForm(TriggerRecord triggerRecord, Model model, RedirectAttributes redirectAttributes) throws Exception {
		triggerRecord.setEplName(URLDecoder.decode(triggerRecord.getEplName(),"UTF-8"));
		model.addAttribute("triggerRecord", triggerRecord);
		return "modules/trigger/recordStatisticForm";
	}
	/**
	 * 异步显示触发详情表
	 * @param triggerRecord
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "record/contentTable")
	@ResponseBody
	public Object contentTable(TriggerRecord triggerRecord, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		triggerRecord.setEplList(categoryService.getEplTypeRoleList());
		List<TriggerRecord> eplList = recordService.findTriggerEpl(triggerRecord);
		String orderBy2 = request.getParameter("orderBy2");
		Page<TriggerRecord> page = recordService.findRecord(new Page<TriggerRecord>(request, response), triggerRecord, eplList, orderBy2);
		page.getList().get(0).setOpinion(page.toString());
		return page.getList();
	}
	/**
	 * 查看，增加，编辑报告表单页面
	 * @throws Exception 
	 */
	@RequiresPermissions(value={"trigger:record:view","trigger:record:add","trigger:record:edit"},logical=Logical.OR)
	@RequestMapping(value = "record/form")
	public String form(TriggerRecord triggerRecord, Model model) throws Exception {
		String contents = "";
		String detailTable = "";
		if (StringUtils.isNotBlank(triggerRecord.getId())){
			triggerRecord = recordService.get(triggerRecord);
			
			List<Func> funcs = funcService.findFuncByEplId(triggerRecord.getEplId());
			System.out.println(funcs);
//			String sql =  "SELECT rc_msg_para.fundid AS fundid, rc_msg_para.stkcode AS stkcode, rc_timestamp AS timestamp, rc_msg_para.orderprice AS orderprice, rc_msg_para.orderqty AS orderqty FROM rtc-2016-10-20/Entrust WHERE ymt_code='89809800005' and rc_timestamp>=1476929702544 and rc_timestamp<=1476929702544";
			if(funcs.size() > 0) {
				StringBuilder tabBuilder = new StringBuilder();
				StringBuilder contentBuilder = new StringBuilder();
				tabBuilder.append("<ul id=\"myTab\" class=\"nav nav-tabs\">");
				contentBuilder.append("<div id=\"myTabContent\" class=\"tab-content\">");
				for (int i = 0; i < funcs.size(); i++) {
					if(i==0){
						tabBuilder.append("<li class=\"active\"><a href=\"#func" + i + "\" data-toggle=\"tab\">" + funcs.get(i).getFuncname() + "</a></li>");
					}else{
						tabBuilder.append("<li><a href=\"#func" + i + "\" data-toggle=\"tab\">" + funcs.get(i).getFuncname() + "</a></li>");
					}
					
					//获取查询统计字段和详情字段
					List<FuncField> fieldList = funcs.get(i).getFields();
					List<FuncField> detailList = new ArrayList<FuncField>();
					List<FuncField> statisticList = new ArrayList<FuncField>();
					for(FuncField field : fieldList){
						if(field.getIsStatistic() ==1){
							statisticList.add(field);
						}else{
							detailList.add(field);
						}
					}
					
					//获取ES查询SQL
//					System.out.println(triggerRecord.getTriggerState());
					JSONObject triggerRecordJson = JSONObject.fromObject(triggerRecord.getTriggerState());
					triggerRecord = recordService.replaceStrVar(triggerRecord);
					triggerRecordJson.put("schemaId", funcs.get(i).getSchemaId());

					String statisticSql = recordService.replaceStrVarByJSON(triggerRecordJson, funcs.get(i).getFuncStatisticSql());
//					System.out.println(statisticList);
					System.out.println("statisticSql:  "+statisticSql);
					
					//从ES查询数据
					List<JSONObject> statistics = recordService.queryDetailPage(statisticList, statisticSql);
//					System.out.println(statistics);
					
					if(i==0){
						contentBuilder.append("<div class=\"tab-pane fade in active\" id=\"func" + i + "\">");
					}else{
						contentBuilder.append("<div class=\"tab-pane fade\" id=\"func" + i + "\">");
					}
					contentBuilder.append("<table id=\"contentTable" + i + "\" class=\"table table-striped table-bordered table-hover table-condensed dataTables-example dataTable\">\n");
					contentBuilder.append("<thead><tr>");
					
					//设置统计tab页表头字段
					for(int j=0; j<statisticList.size(); j++) {
						FuncField funcField = statisticList.get(j);
						if("fundid".equals(funcField.getFieldName())){
							contentBuilder.append("<th class='sort-column "+ funcField.getFieldName() +"' colNum='" + j + "' onclick='sortColumn(this);'>" + funcField.getFieldDescribe() + "</th>");
						}else{
							contentBuilder.append("<th colNum='" + j + "'>" + funcField.getFieldDescribe() + "</th>");
						}
					}
					
					contentBuilder.append("</tr></thead>");
					contentBuilder.append("<tbody>");
					for(int j = 0; j < statistics.size(); j++) {
						contentBuilder.append("<tr onclick='clickRow(this," + funcs.get(i).getFuncid() + ")'>");
						for(FuncField funcField :  statisticList) {
							if(statistics.get(j).containsKey(funcField.getFieldName())) {
								if("timestamp".equals(funcField.getFieldName())) {
									contentBuilder.append("<td value='"+funcField.getFieldName()+"'>" + DateUtils.getDateByTimestamp(statistics.get(j).getLong(funcField.getFieldName()), "yyyy-MM-dd HH:mm:ss") + "</td>");
								} else {
									contentBuilder.append("<td value='"+funcField.getFieldName()+"'>" + statistics.get(j).getString(funcField.getFieldName()) + "</td>");
								}
							}
							else {
								contentBuilder.append("<td value=''></td>");
							}

						}
						contentBuilder.append("</tr>");
					}
					contentBuilder.append("</tbody>");
					contentBuilder.append("</table>");
					contentBuilder.append("</div>");
					
					//获取详情页
					if(i==0){
						String detailSql = recordService.replaceStrVarByJSON(triggerRecordJson, funcs.get(i).getFuncsql());
//						System.out.println(detailList);
						System.out.println("detailSql:  "+detailSql);
						List<JSONObject> details = recordService.queryDetailPage(detailList, detailSql);
						
						//设置详情表标题和内容
						StringBuilder detailBuilder = new StringBuilder();
						detailBuilder.append("<thead><tr>");
						
						//设置详情表头字段
						for(int j=0; j<detailList.size(); j++) {
							FuncField funcField = detailList.get(j);
							if("fundid".equals(funcField.getFieldName()) || "timestamp".equals(funcField.getFieldName())){
								detailBuilder.append("<th class='sort-column "+ funcField.getFieldName() +"' colNum='" + j + "' onclick='sortColumn(this);'>" + funcField.getFieldDescribe() + "</th>");
							}else{
								detailBuilder.append("<th colNum='" + j + "'>" + funcField.getFieldDescribe() + "</th>");
							}
						}
						
						detailBuilder.append("</tr></thead>");
						detailBuilder.append("<tbody>");
						
						//设置详情内容
						for(int j = 0; j < details.size(); j++) {
							detailBuilder.append("<tr>");
							for(FuncField funcField :  detailList) {
								if(details.get(j).containsKey(funcField.getFieldName())) {
									if("timestamp".equals(funcField.getFieldName())) {
										detailBuilder.append("<td>" + DateUtils.getDateByTimestamp(details.get(j).getLong(funcField.getFieldName()), "yyyy-MM-dd HH:mm:ss") + "</td>");
									} else {
										detailBuilder.append("<td>" + details.get(j).getString(funcField.getFieldName()) + "</td>");
									}
								}
							}
							detailBuilder.append("</tr>");
						}
						detailBuilder.append("</tbody>");
						detailTable = detailBuilder.toString();
					}
				}
				
				tabBuilder.append("<a style='float:right!important;margin-right:-1px' id='detailExport' " +
						"class='layui-layer-btn' data-placement='left' title='全部导出' >" +
						"<i class='fa fa-file-excel-o'></i> 全部导出</a></ul>");
				contentBuilder.append("</div>");
				contents = tabBuilder.append(contentBuilder.toString()).toString();
			}

		}

		model.addAttribute("triggerRecord", triggerRecord);
//		model.addAttribute("details", recordService.queryDetailPage(0, 10, triggerRecord.getFundid(), startTime, endTime));
		model.addAttribute("funcs", contents);
		model.addAttribute("detail", detailTable);
		return "modules/trigger/recordForm";
	}

	@RequiresPermissions(value={"trigger:record:add","trigger:record:edit"},logical=Logical.OR)
	@RequestMapping(value = "record/statisticSave")
	public String statisticSave(TriggerRecord triggerRecord, Model model, RedirectAttributes redirectAttributes) throws Exception {
		recordService.statisticSave(triggerRecord);
		addMessage(redirectAttributes, "处理成功");
		return "redirect:" + adminPath + "/trigger/record/list";
	}
	
	@RequiresPermissions(value={"trigger:record:add","trigger:record:edit"},logical=Logical.OR)
	@RequestMapping(value = "record/save")
	public String save(TriggerRecord triggerRecord, Model model, RedirectAttributes redirectAttributes) throws Exception {
		if (!beanValidator(model, triggerRecord)){
			return form(triggerRecord, model);
		}
//		// 如果是修改，则状态为已发布，则不能再进行操作
//		if (StringUtils.isNotBlank(triggerRecord.getId())){
//			TriggerRecord e = recordService.get(triggerRecord.getId());
//			if (e.getStatus() != 4){
//				addMessage(redirectAttributes, "已处理，不能重复处理！");
//				return "redirect:" + adminPath + "/trigger/record";
//			}
//		}
		recordService.save(triggerRecord);
		addMessage(redirectAttributes, "处理'" + triggerRecord.getEplName() + "'成功");
		return "redirect:" + adminPath + "/trigger/record/list";
	}
	
	@RequiresPermissions("trigger:record:del")
	@RequestMapping(value = "record/delete")
	public String delete(TriggerRecord triggerRecord, RedirectAttributes redirectAttributes) {
		recordService.delete(triggerRecord);
		addMessage(redirectAttributes, "删除记录成功");
		return "redirect:" + adminPath + "/trigger/record/list";
	}
	
	@RequiresPermissions("trigger:record:del")
	@RequestMapping(value = "record/deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			recordService.delete(recordService.get(id));
		}
		addMessage(redirectAttributes, "删除记录成功");
		return "redirect:" + adminPath + "/trigger/record/list";
	}


	@RequiresPermissions("trigger:record:export")
	@RequestMapping(value = "record/export", method= RequestMethod.POST)
	public String export(TriggerRecord triggerRecord, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "triggerRecord-" + DateTime.now().toString("yyyy-MM-dd") + ".xlsx";
			if(triggerRecord.getEplTypes() != null && !"".equals(triggerRecord.getEplTypes().trim())){
				List<Epl> eplTypeRequestList = new ArrayList<Epl>();
				List<String> eplTypeIdList  = Arrays.asList(triggerRecord.getEplTypes().split(","));
				System.out.println(eplTypeIdList);
				for(String eplTypeId : eplTypeIdList){
					if(!"".equals(eplTypeId.trim())){
						Epl epl = new Epl();
						epl.setEplId(Integer.parseInt(eplTypeId));
						eplTypeRequestList.add(epl);
					}
				}
				triggerRecord.setEplList(eplTypeRequestList);
			} else {
				triggerRecord.setEplList(categoryService.getEplTypeRoleList());
			}
			List<TriggerRecord> eplList = recordService.findTriggerEpl(triggerRecord);
			Page<TriggerRecord> page = recordService.findRecord(new Page<TriggerRecord>(request, response, -1), triggerRecord, eplList, null);
			new ExportExcel("触发记录列表", TriggerRecord.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/trigger/record/list?repage";
	}

	@RequiresPermissions("trigger:statistics:list")
	@RequestMapping(value = {"statisticsList"})
	public String statisticsList(TriggerRecord triggerRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Epl> eplCategories = categoryService.getEplTypeRoleList();
		model.addAttribute("eplCategories", eplCategories);
		triggerRecord.setEplList(eplCategories);
		
//		获取EPL父类选项
		List<EplCategory> categoryList = categoryService.getEplParentTypeRoleList();
		String categoryListStr =  JsonMapper.getInstance().toJson(categoryList);
		model.addAttribute("categoryList",  categoryListStr);
		
//		获取EPL选项
		List<Epl> eplTypeList = categoryService.getParentAndEplTypeRoleList();
		String eplTypeListStr =  JsonMapper.getInstance().toJson(eplTypeList);
		model.addAttribute("eplTypeList",  eplTypeListStr);
		
		if(triggerRecord.getEplTypes() != null && !"".equals(triggerRecord.getEplTypes().trim())){
			List<Epl> eplTypeRequestList = new ArrayList<Epl>();
			List<String> eplTypeIdList  = Arrays.asList(triggerRecord.getEplTypes().split(","));
			for(String eplTypeId : eplTypeIdList){
				if(!"".equals(eplTypeId.trim())){
					Epl epl = new Epl();
					epl.setEplId(Integer.parseInt(eplTypeId));
					eplTypeRequestList.add(epl);
				}
			}
			triggerRecord.setEplList(eplTypeRequestList);
		}
		Page<TriggerRecord> page = recordService.findStatistics(new Page<TriggerRecord>(request, response), triggerRecord);
		model.addAttribute("page", page);
		System.out.println(page.getList());
		
		return "modules/trigger/statisticsList";
	}
	/**
	 * 将详情页中的触发详情导出
	 * @param triggerRecord
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
//	@RequiresPermissions("trigger:record:detailExport")
	@RequestMapping(value = "record/detailExport")
	public String detailExport(TriggerRecord triggerRecord, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try{
			recordService.detailExport(triggerRecord, request, response);
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出用户失败！失败信息："+e.getMessage());
		}
		return "redirect:" + adminPath + "/trigger/record/form";
	}
	/**
	 * 获取详情页中的detailTable表内容
	 * @param triggerRecord
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "record/detailTable")
	@ResponseBody
	public String detailTable(TriggerRecord triggerRecord, HttpServletRequest request) throws Exception {
		String funcId = request.getParameter("funcId");
		String condition = request.getParameter("condition");
//		System.out.println(funcId);
//		System.out.println(condition);
		String[] conditions = condition.split(",");
		triggerRecord = recordService.get(triggerRecord);
		Func func = funcService.findFuncByFuncid(Integer.parseInt(funcId));
		
		//获取查询统计字段和详情字段
		List<FuncField> fieldList = func.getFields();
		List<FuncField> detailList = new ArrayList<FuncField>();
		Set<String> conditionSet = new HashSet<String>();
		for(FuncField field : fieldList){
			if(field.getIsStatistic() !=1){
				detailList.add(field);
			}
			if(field.getIsCondition() == 1){
				conditionSet.add(field.getFieldName());
			}
		}
		
		//替换SQL中阀值
		JSONObject triggerRecordJson = JSONObject.fromObject(triggerRecord.getTriggerState());
		triggerRecord = recordService.replaceStrVar(triggerRecord);
		triggerRecordJson.put("schemaId", func.getSchemaId());
		String detailSql = recordService.replaceStrVarByJSON(triggerRecordJson, func.getFuncsql());
//		System.out.println(detailList);
//		System.out.println("detailSql:  "+detailSql);
		for(String cond : conditions){
			if(StringUtils.isEmpty(cond)) continue;
			String[] kv = cond.split(":");
			String condKey = kv[0].trim();
			if(conditionSet.contains(condKey)){
				detailSql += " AND " + condKey + " = " + kv[1].trim();
			}
		}
		System.out.println("detailSql:  "+detailSql);
		List<JSONObject> details = recordService.queryDetailPage(detailList, detailSql);
		
		//设置详情表标题和内容
		StringBuilder detailBuilder = new StringBuilder();
		detailBuilder.append("<thead><tr>");
		
		//设置详情表头字段
		for(int j=0; j<detailList.size(); j++) {
			FuncField funcField = detailList.get(j);
			if("timestamp".equals(funcField.getFieldName())){
				detailBuilder.append("<th class='sort-column "+ funcField.getFieldName() +"' colNum='" + j + "' onclick='sortColumn(this);'>" + funcField.getFieldDescribe() + "</th>");
			}else{
				detailBuilder.append("<th colNum='" + j + "'>" + funcField.getFieldDescribe() + "</th>");
			}
		}
		
		detailBuilder.append("</tr></thead>");
		detailBuilder.append("<tbody>");
		
		//设置详情内容
		for(int j = 0; j < details.size(); j++) {
			detailBuilder.append("<tr>");
			for(FuncField funcField :  detailList) {
				if(details.get(j).containsKey(funcField.getFieldName())) {
					if("timestamp".equals(funcField.getFieldName())) {
						detailBuilder.append("<td value='"+funcField.getFieldName()+"'>" + DateUtils.getDateByTimestamp(details.get(j).getLong(funcField.getFieldName()), "yyyy-MM-dd HH:mm:ss") + "</td>");
					} else {
						detailBuilder.append("<td value='"+funcField.getFieldName()+"'>" + details.get(j).getString(funcField.getFieldName()) + "</td>");
					}
				}
			}
			detailBuilder.append("</tr>");
		}
		detailBuilder.append("</tbody>");
		
		return detailBuilder.toString();
	}
	
}
