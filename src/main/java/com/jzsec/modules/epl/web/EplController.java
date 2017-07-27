package com.jzsec.modules.epl.web;

import com.jzsec.common.config.LoadConfig;
import com.jzsec.common.config.MyConst;
import com.jzsec.common.mapper.JsonMapper;
import com.jzsec.common.persistence.Page;
import com.jzsec.common.utils.DateUtil;
import com.jzsec.common.utils.StringUtil;
import com.jzsec.common.web.BaseController;
import com.jzsec.modules.common.entity.PushAlarmEpl;
import com.jzsec.modules.common.entity.PushEpl;
import com.jzsec.modules.common.servie.CommonService;
import com.jzsec.modules.epl.entity.AlarmEplRelation;
import com.jzsec.modules.epl.entity.AlarmGroup;
import com.jzsec.modules.epl.entity.AlarmThreshold;
import com.jzsec.modules.epl.entity.Epl;
import com.jzsec.modules.epl.entity.Threshold;
import com.jzsec.modules.epl.service.AlarmEplRelationService;
import com.jzsec.modules.epl.service.AlarmEplService;
import com.jzsec.modules.epl.service.AlarmGroupService;
import com.jzsec.modules.epl.service.AlarmThresholdService;
import com.jzsec.modules.epl.service.EplService;
import com.jzsec.modules.epl.service.ThresholdService;
import com.jzsec.modules.eplCategory.entity.EplCategory;
import com.jzsec.modules.eplCategory.service.EplCategoryService;
import com.jzsec.modules.func.entity.Func;
import com.jzsec.modules.func.service.FuncService;
import com.jzsec.modules.schema.entity.Schema;
import com.jzsec.modules.schema.entity.SchemaField;
import com.jzsec.modules.schema.service.SchemaFieldService;
import com.jzsec.modules.schema.service.SchemaService;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 风控规则及阀值配置
 * @author 劉 焱
 * @date 2016-8-24
 * @tags
 */
@Controller
@RequestMapping(value = "${adminPath}/rtc/epl")
public class EplController extends BaseController {
	@Autowired
	private EplService eplService;
	@Autowired
	private EplCategoryService categoryService;
	@Autowired
	private ThresholdService thresholdService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private AlarmEplService alarmEplService;
	@Autowired
	private AlarmEplRelationService alarmEplRelationService;
	@Autowired
	private AlarmGroupService alarmGroupService;
	@Autowired
	private AlarmThresholdService alarmThresholdService;
	@Autowired
	private FuncService funcService;
	@Autowired
	private SchemaService schemaService;
	@Autowired
	private SchemaFieldService schemaFieldService;

	@RequiresPermissions("rtc:epl:list")
	@RequestMapping(value = {"list", ""})
	public String list(Epl epl, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<EplCategory> parentTypes = categoryService.getEplParentTypeRoleList();
		epl.setUserEplParents(parentTypes);
		Page<Epl> page = eplService.findPage(new Page<Epl>(request, response), epl);
		List<Epl> list = page == null ? new ArrayList<Epl>() : page.getList();
		int length = list == null ? 0 : list.size();
		for(int i=0; i<length; i++){
			String sql = list.get(i)==null ? "" : list.get(i).getEpl();
			list.get(i).setEpl(StringUtil.isEmpty(sql) ? null : StringUtil.entitycharFromString(sql));
			String eplDescribe = list.get(i)==null ? "" : list.get(i).getEplDescribe();
			list.get(i).setEplDescribe(StringUtil.isEmpty(eplDescribe) ? null : StringUtil.entitycharFromString(eplDescribe));
			String textState = list.get(i)==null ? "" : list.get(i).getTextState();
			list.get(i).setTextState(StringUtil.isEmpty(textState) ? null : StringUtil.entitycharFromString(textState));
		}
		page.setList(list);
		
		model.addAttribute("page", page);
		model.addAttribute("parentTypes", parentTypes);
		
		return "modules/epl/epl";
	}

	@RequiresPermissions(value={"rtc:epl:add","rtc:epl:edit","rtc:epl:alarm"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Epl epl, Model model, RedirectAttributes redirectAttributes) {
//		System.out.println(epl);
		if(StringUtil.isEmpty(epl.getId())){
			epl.setEplId(DateUtil.getIntTimeId());
		}
		epl.setEpl(StringUtil.entitycharToString(epl.getEpl()));
		epl.setEplDescribe(StringUtil.entitycharToString(epl.getEplDescribe()));
		epl.setTextState(StringUtil.entitycharToString(epl.getTextState()));
		epl.setUserEplParents(categoryService.getEplParentTypeRoleList());
		
		Epl epl1 = StringUtil.isEmpty(epl.getId()) ? new Epl() : eplService.findUniqueByProperty("id", epl.getId());
		if(epl.getEplBelong() == null){
			epl.setEplBelong(epl1.getEplBelong());
		}
		epl.setEplId(StringUtil.isEmpty(epl.getId()) ? DateUtil.getIntTimeId() : epl1.getEplId());
		
		if(epl.getStatus() ==1){
			epl.setOnlineTime(DateUtil.dateToString(new Date(), MyConst.YYYY_MM_DD));
		} else if(epl.getStatus() ==2){
			epl.setOfflineTime(DateUtil.dateToString(new Date(), MyConst.YYYY_MM_DD));
		}
		
		//事前
		if(epl.getEplBelong() ==1){
			
		//事中
		} else if(epl.getEplBelong() ==2){
			if(commonService.isEplNorm(epl.getEpl())){
				String message = eplService.edit(epl, epl1);
				addMessage(redirectAttributes, message);
				
				try {
					eplService.editEplToZookeeper(epl, epl1);
				} catch (Exception e) {
					try {
						eplService.editEplToZookeeper(epl, epl1);
					} catch (Exception e1) {
						addMessage(redirectAttributes, "风控规则数据库保存成功, 但推送至zookeeper节点异常!");
						e1.printStackTrace();
					}
				}
			}else{
				addMessage(redirectAttributes, "风控规则EPL验证失败!");
			}
		//事后
		} else if(epl.getEplBelong() ==3){
			String message = eplService.edit(epl, epl1);
			addMessage(redirectAttributes, message);
			String errorMessage = null;
			if(StringUtil.isEmpty(epl.getId()) && epl.getStatus() == 1){//新增且上线
				errorMessage = commonService.uploadZipFile(epl.getEplId(), epl.getScheduleTime());
			}else if(!StringUtil.isEmpty(epl.getId())){//修改
				if(epl1.getStatus() == epl.getStatus() && epl.getStatus() == 1){//修改前后都为上线状态
					errorMessage = commonService.uploadZipFile(epl.getEplId(), epl.getScheduleTime());
				}else if(epl1.getStatus() != epl.getStatus() && epl.getStatus() == 1){//由下线修改为上线状态
					errorMessage = commonService.uploadZipFile(epl.getEplId(), epl.getScheduleTime());
				}else if(epl1.getStatus() == 1 && epl.getStatus() != 1){//由上线修改为下线状态
					errorMessage = commonService.removeSchedule(epl.getEplId());
				}
			} 
			if(errorMessage != null){
				addMessage(redirectAttributes, "风控规则数据库保存成功, 但azkaban调度异常, 异常信息: "+errorMessage);
			}
		}

		return "redirect:" + adminPath + "/rtc/epl/list";
	}
	
	@RequiresPermissions("rtc:epl:del")
	@RequestMapping(value = "delete")
	public String delete(Epl epl, RedirectAttributes redirectAttributes) {
		epl.setStatus(3);
		eplService.deleteSoft(epl);
		
		//事前
		if(epl.getEplBelong() ==1){
			
		//事中
		} else if(epl.getEplBelong() ==2){
			PushEpl obj = eplService.pushZookeeper(epl.getEplId());
			try {
				commonService.rtcEplPushData(obj, "remove", LoadConfig.getZookeeperRiskPath(),  LoadConfig.getZookeeperRiskChildPath());
			} catch (Exception e) {
				try {
					commonService.rtcEplPushData(obj, "remove", LoadConfig.getZookeeperRiskPath(),  LoadConfig.getZookeeperRiskChildPath());
				} catch (Exception e1) {
					addMessage(redirectAttributes, "风控规则数据库软删除成功, 但推送至zookeeper节点异常!");
					e1.printStackTrace();
				}
			}
			
		//事后
		} else if(epl.getEplBelong() ==3){
			String errorMessage = commonService.removeSchedule(epl.getEplId());
			if(errorMessage != null){
				addMessage(redirectAttributes, "风控规则数据库软删除成功, 但azkaban移除调度异常, 异常信息: "+errorMessage);
			}
		}

		return "redirect:" + adminPath + "/rtc/epl/list";
	}

	@RequiresPermissions("rtc:epl:view")
	@RequestMapping(value = "form")
	public String editEpl(Epl epl, HttpServletRequest request, HttpServletResponse response, Model model) {
//		获取规则父类型
		List<EplCategory> parentTypes = categoryService.getEplParentTypeRoleList();
		model.addAttribute("parentTypes", parentTypes);
//		获取报警规则类型
//		List<Epl> alarmTypes = alarmEplService.findTypeList();
//		model.addAttribute("alarmTypes", alarmTypes);
		
//		获取所有功能点选项
		List<Func> funcList = funcService.findNameList();
		model.addAttribute("funcList", funcList);
		
//		获取指令组选项
		List<Schema> schemaList = schemaService.findList(new Schema());
		String schemaListStr =  JsonMapper.getInstance().toJson(schemaList);
		model.addAttribute("schemaList",  schemaListStr);
//		System.out.println(schemaListStr);
		
//		获取指令键值选项
		List<SchemaField> schemaFieldList = schemaFieldService.findList(new SchemaField());
		String schemaFieldListStr =  JsonMapper.getInstance().toJson(schemaFieldList);
		model.addAttribute("schemaFieldList",  schemaFieldListStr);
//		System.out.println(schemaFieldListStr);
		
		if(epl.getId() != null) {
			Epl epl1 = eplService.findUniqueByProperty("id", epl.getId());
			epl1.setEpl(StringUtil.entitycharFromString(epl1.getEpl()));
			epl1.setEplDescribe(StringUtil.entitycharFromString(epl1.getEplDescribe()));
			epl1.setTextState(StringUtil.entitycharFromString(epl1.getTextState()));
			if( epl1 != null){
				String funcStr = StringUtil.isEmpty(epl1.getFuncStr()) ? null : epl1.getFuncStr();
				epl1.setFuncStr(funcStr);
				String groupKeyStr = StringUtil.isEmpty(epl1.getEplGroupKeyStr()) ? null : epl1.getEplGroupKeyStr();
				epl1.setEplGroupKeyStr(groupKeyStr);
			}
			
			//是否报警
			if(epl1.getIsAlarm() == 1){
				AlarmEplRelation alarmEplRelation1 = alarmEplRelationService.findUniqueByProperty("epl_id", epl1.getEplId());
				epl1.setAlarmId(alarmEplRelation1 == null ? null :alarmEplRelation1.getAlarmEplId());
			}
			
			//获取阀值
			Threshold threshold = new Threshold();
			threshold.setEplId(epl1.getEplId());
			List<Threshold> thresholdList = thresholdService.findAllThresholds(threshold);
			epl1.setThresholdList(thresholdList);
			
			model.addAttribute("epl", epl1);
			return "modules/epl/eplForm";
		}
		
		return "modules/epl/eplForm";
	}
	
	@RequiresPermissions(value={"rtc:epl:view"},logical=Logical.OR)
	@RequestMapping(value = "form1")
	public String checkEpl(Epl epl, HttpServletRequest request, HttpServletResponse response, Model model) {
//		获取规则父类型
		List<EplCategory> parentTypes = categoryService.getEplParentTypeRoleList();
		model.addAttribute("parentTypes", parentTypes);
//		获取报警规则类型
//		List<Epl> alarmTypes = alarmEplService.findTypeList();
//		model.addAttribute("alarmTypes", alarmTypes);
		
//		获取所有功能点选项
		List<Func> funcList = funcService.findNameList();
		model.addAttribute("funcList", funcList);
		
//		获取指令组选项
		List<Schema> schemaList = schemaService.findList(new Schema());
		String schemaListStr =  JsonMapper.getInstance().toJson(schemaList);
		model.addAttribute("schemaList",  schemaListStr);
		
//		获取指令键值选项
		List<SchemaField> schemaFieldList = schemaFieldService.findList(new SchemaField());
		String schemaFieldListStr =  JsonMapper.getInstance().toJson(schemaFieldList);
		model.addAttribute("schemaFieldList",  schemaFieldListStr);
		
		Epl epl1 = eplService.findUniqueByProperty("id", epl.getId());
		epl1.setEpl(StringUtil.entitycharFromString(epl1.getEpl()));
		epl1.setEplDescribe(StringUtil.entitycharFromString(epl1.getEplDescribe()));
		epl1.setTextState(StringUtil.entitycharFromString(epl1.getTextState()));
		
		//设置已选择的功能点和hash键值
		if(epl1 != null){
			String funcStr = StringUtil.isEmpty(epl1.getFuncStr()) ? null : epl1.getFuncStr();
			epl1.setFuncStr(funcStr);
			String groupKeyStr = StringUtil.isEmpty(epl1.getEplGroupKeyStr()) ? null : epl1.getEplGroupKeyStr();
			epl1.setEplGroupKeyStr(groupKeyStr);
		}
		
		//设置报警信息
		if(epl1.getIsAlarm() == 1){
			AlarmEplRelation alarmEplRelation1 = alarmEplRelationService.findUniqueByProperty("epl_id", epl1.getEplId());
			epl1.setAlarmId(alarmEplRelation1 == null ? null :alarmEplRelation1.getAlarmEplId());
		}
		
		//设置阀值
		Threshold threshold = new Threshold();
		threshold.setEplId(epl1.getEplId());
		List<Threshold> thresholdList = thresholdService.findAllThresholds(threshold);
		epl1.setThresholdList(thresholdList);
		
		model.addAttribute("epl", epl1);
		return "modules/epl/eplForm1";
	}

	@RequiresPermissions(value={"rtc:epl:changeStatus","rtc:epl:add","rtc:epl:edit"},logical=Logical.OR)
	@RequestMapping(value = "changeStatus")
	public String updateStatus(Epl epl, RedirectAttributes redirectAttributes) {
		if(epl.getStatus() == 1){
			epl.setStatus(2);
			epl.setOfflineTime(DateUtil.dateToString(new Date(), MyConst.YYYY_MM_DD));
		}else{
			epl.setStatus(1);
			epl.setOnlineTime(DateUtil.dateToString(new Date(), MyConst.YYYY_MM_DD));
		}
		
		eplService.save(epl);
		
		//事前
		if(epl.getEplBelong() ==1){
			
		//事中
		} else if(epl.getEplBelong() ==2){
			try {
				eplService.changeEplStatusToZookeeepr(epl);
			} catch (Exception e) {
				try {
					eplService.changeEplStatusToZookeeepr(epl);
				} catch (Exception e1) {
					addMessage(redirectAttributes, "风控规则数据库状态变更成功, 但推送至zookeeper节点异常!");
					e1.printStackTrace();
				}
			}
			
		//事后
		} else if(epl.getEplBelong() ==3){
			if(epl.getStatus() == 1){
				Epl epl1 = eplService.findUniqueByProperty("id", epl.getId());
				String errorMessage = commonService.uploadZipFile(epl1.getEplId(), epl1.getScheduleTime());
				if(errorMessage != null){
					addMessage(redirectAttributes, "风控规则数据库保存成功, 但azkaban调度异常, 异常信息: "+errorMessage);
				}
			}else{
				String errorMessage = commonService.removeSchedule(epl.getEplId());
				if(errorMessage != null){
					addMessage(redirectAttributes, "风控规则数据库软删除成功, 但azkaban移除调度异常, 异常信息: "+errorMessage);
				}
			}
		}
		
		return "redirect:" + adminPath + "/rtc/epl/list";
	}
	
	@RequiresPermissions("rtc:epl:category")
	@RequestMapping(value = "categoryForm")
	public String editCategoryForm(Epl epl, HttpServletRequest request, HttpServletResponse response, Model model) {
//		获取规则父类型
		List<EplCategory> parentTypes = categoryService.getEplParentTypeRoleList();
		model.addAttribute("parentTypes", parentTypes);
		model.addAttribute("epl", epl);
		
		return "modules/epl/categoryForm";
	}
	
	@RequiresPermissions("rtc:epl:category")
	@RequestMapping(value = "categoryEdit")
	public String categoryEdit(Epl epl, HttpServletRequest request, HttpServletResponse response, Model model) {
		eplService.categoryEdit(epl);
		
		return "redirect:" + adminPath + "/rtc/epl/list";
	}

	@RequiresPermissions("rtc:epl:threshold")
	@RequestMapping(value = "threshold")
	public String getThresholds(Threshold threshold, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Threshold> list = thresholdService.findAllThresholds(threshold);
		model.addAttribute("list", list);
		
		return "modules/epl/threshold";
	}
	
//	@RequiresPermissions("epl:threshold:view")
//	@RequestMapping(value = "thresholdEdit")
	public String getThresholdsEdit(Threshold threshold, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Threshold> p = new Page<Threshold>(request, response);
		p.setCount(100);
		p.setPageSize(100);
		Page<Threshold> page = thresholdService.findPage(p, threshold);
		model.addAttribute("page", page);
		
		return "modules/epl/thresholdEdit";
	}
	
//	@RequiresPermissions(value={"epl:threshold:del"})
//	@RequestMapping(value = "deleteThreshold")
	public String deleteThreshold(Threshold threshold, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		thresholdService.delete(threshold);
    	PushEpl obj = eplService.pushZookeeper(threshold.getEplId());
    	commonService.rtcEplPushData(obj, "update", LoadConfig.getZookeeperRiskPath(),  LoadConfig.getZookeeperRiskChildPath());
    	
		return "redirect:" + adminPath + "/rtc/epl/threshold?eplId="+threshold.getEplId();
	}

//	@RequiresPermissions(value={"epl:threshold:add","epl:threshold:edit"},logical=Logical.OR)
	@RequestMapping(value = "saveThreshold")
	@ResponseBody
	public Object saveThreshold(Threshold threshold, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
			threshold.setThresholdDescribe(URLDecoder.decode(threshold.getThresholdDescribe(),   "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		thresholdService.save(threshold);
		
		Epl epl = eplService.findUniqueByProperty("epl_id", threshold.getEplId());
		//事前
		if(epl.getEplBelong() ==1){
			
		//事中
		} else if(epl.getEplBelong() ==2){
			PushEpl obj = eplService.pushZookeeper(threshold.getEplId());
			try {
				commonService.rtcEplPushData(obj, "update", LoadConfig.getZookeeperRiskPath(),  LoadConfig.getZookeeperRiskChildPath());
			} catch (Exception e) {
				try {
					commonService.rtcEplPushData(obj, "update", LoadConfig.getZookeeperRiskPath(),  LoadConfig.getZookeeperRiskChildPath());
				} catch (Exception e1) {
					e1.printStackTrace();
					return "error";
				}
			}
		//事后
		} else if(epl.getEplBelong() ==3){
			
		}
		
		return threshold;
	}

	@RequiresPermissions(value={"rtc:epl:alarm"})
	@RequestMapping(value = "alarmForm")
	public String openAlarmForm(Epl epl, HttpServletRequest request, HttpServletResponse response, Model model) {
		Epl epl1 = eplService.findAlarm(epl);
		epl1.setId(epl.getId());
		epl1.setIsAlarm(epl.getIsAlarm());
		epl1.setEplId(epl.getEplId());
		epl1.setStatus(epl.getStatus());
		epl1.setAlarmSQL(StringUtil.entitycharFromString(epl1.getAlarmSQL()));
		epl1.setAlarmDescribe(StringUtil.entitycharFromString(epl1.getAlarmDescribe()));
		epl1.setAlarmTemplate(StringUtil.entitycharFromString(epl1.getAlarmTemplate()));
		model.addAttribute("epl", epl1);
		
		List<AlarmGroup> alarmGroups = alarmGroupService.findList();
		model.addAttribute("alarmGroups", alarmGroups);
		return "modules/epl/alarmForm";
	}

	@RequiresPermissions(value={"rtc:epl:alarm"})
	@RequestMapping(value = "deleteAlarmThreshold")
	@ResponseBody
	public String deleteAlarmThreshold(AlarmThreshold alarmThreshold, RedirectAttributes redirectAttributes) {
		alarmThresholdService.deleteAlarmThreshold(alarmThreshold);
		
		if(alarmThreshold.getStatus() == 1){
			PushAlarmEpl obj = alarmEplService.pushZookeeper(alarmThreshold.getEplId());
			try {
				commonService.alarmEplPushData(obj, "remove", LoadConfig.getZookeeperAlarmPath(),  LoadConfig.getZookeeperAlarmChildPath());
			} catch (Exception e) {
				try {
					commonService.alarmEplPushData(obj, "remove", LoadConfig.getZookeeperAlarmPath(),  LoadConfig.getZookeeperAlarmChildPath());
				} catch (Exception e1) {
					addMessage(redirectAttributes, "报警规则数据库阀值删除成功, 但推送至zookeeper节点异常!");
					e1.printStackTrace();
				}
			}
		}		
		
		return "redirect:" + adminPath + "/rtc/epl/alarmForm?eplId="+alarmThreshold.getEplId();
	}

	@RequiresPermissions(value={"rtc:epl:alarm"})
	@RequestMapping(value = "saveAlarmThreshold")
	@ResponseBody
	public Object saveAlarmThreshold(AlarmThreshold alarmThreshold, HttpServletRequest request, HttpServletResponse response, Model model) {
		alarmThresholdService.saveAlarmThreshold(alarmThreshold);

		if(alarmThreshold.getStatus() == 1){
			PushAlarmEpl obj = alarmEplService.pushZookeeper(alarmThreshold.getEplId());
			try {
				commonService.alarmEplPushData(obj, "update", LoadConfig.getZookeeperAlarmPath(),  LoadConfig.getZookeeperAlarmChildPath());
			} catch (Exception e) {
				try {
					commonService.alarmEplPushData(obj, "update", LoadConfig.getZookeeperAlarmPath(),  LoadConfig.getZookeeperAlarmChildPath());
				} catch (Exception e1) {
					e1.printStackTrace();
					return "error";
				}
			}
		}	
		
		return alarmThreshold;
	}

	@RequiresPermissions(value={"rtc:epl:alarm"})
	@RequestMapping(value = "saveAlarm")
	public String saveAlarm(Epl epl, Model model, RedirectAttributes redirectAttributes) throws Exception {
		if(StringUtil.isEmpty(epl.getAlarmEplId())){
			epl.setAlarmEplId(DateUtil.getIntTimeId());
		}
		epl.setAlarmSQL(StringUtil.entitycharToString(epl.getAlarmSQL()));
		epl.setAlarmDescribe(StringUtil.entitycharToString(epl.getAlarmDescribe()));
		epl.setAlarmTemplate(StringUtil.entitycharToString(epl.getAlarmTemplate()));
		Epl oldEpl = eplService.findUniqueByProperty("id", epl.getId());
		
		if(commonService.isEplNorm(epl.getAlarmSQL())){
			eplService.saveAlarm(epl, oldEpl);
			
			try {
				eplService.saveAlarmToZookeeper(epl, oldEpl);
			} catch (Exception e) {
				try {
					eplService.saveAlarmToZookeeper(epl, oldEpl);
				} catch (Exception e1) {
					addMessage(redirectAttributes, "报警规则数据库保存成功, 但推送至zookeeper节点异常!");
					e1.printStackTrace();
				}
			}
		}else{
			addMessage(redirectAttributes, "报警规则EPL验证失败!");
		}
		
		return "redirect:" + adminPath + "/rtc/epl/list";
	}

	@RequiresPermissions(value={"rtc:epl:alarm"})
	@RequestMapping(value = "deleteAlarm")
	public String deleteAlarm(Epl epl, Model model, RedirectAttributes redirectAttributes) {
		AlarmEplRelation alarmEplRelation = alarmEplRelationService.findUniqueByProperty("epl_id", epl.getEplId());
		Integer alarmEplId = alarmEplRelation == null ? null : alarmEplRelation.getAlarmEplId();
		PushAlarmEpl obj = alarmEplService.pushZookeeper(alarmEplId);
		
		eplService.deleteAlarm(epl, alarmEplId);
		
		if(epl.getStatus() == 1){
			try {
				commonService.alarmEplPushData(obj, "remove", LoadConfig.getZookeeperAlarmPath(),  LoadConfig.getZookeeperAlarmChildPath());
			} catch (Exception e) {
				try {
					commonService.alarmEplPushData(obj, "remove", LoadConfig.getZookeeperAlarmPath(),  LoadConfig.getZookeeperAlarmChildPath());
				} catch (Exception e1) {
					addMessage(redirectAttributes, "报警规则数据库删除成功, 但推送至zookeeper节点异常!");
					e1.printStackTrace();
				}
			}
		}

		return "redirect:" + adminPath + "/rtc/epl/list";
	}
}