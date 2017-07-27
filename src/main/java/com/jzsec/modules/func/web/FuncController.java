package com.jzsec.modules.func.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import com.jzsec.common.persistence.Page;
import com.jzsec.common.utils.DateUtil;
import com.jzsec.common.utils.StringUtil;
import com.jzsec.common.web.BaseController;
import com.jzsec.modules.func.entity.Func;
import com.jzsec.modules.func.entity.FuncField;
import com.jzsec.modules.func.service.FuncFieldService;
import com.jzsec.modules.func.service.FuncService;
import com.jzsec.modules.schema.entity.Schema;
import com.jzsec.modules.schema.service.SchemaService;

import org.apache.commons.lang.RandomStringUtils;
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

/**
 * 功能点 
 * @author 劉 焱
 * @date 2016-10-19
 */
@Controller
@RequestMapping(value = "${adminPath}/rtc/func")
public class FuncController extends BaseController {
	@Autowired
	private FuncService funcService;
	@Autowired
	private FuncFieldService funcFieldService;
	@Autowired
	private SchemaService schemaService;

	@RequiresPermissions("rtc:func:list")
	@RequestMapping(value = {"list", ""})
	public String list(Func func, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Func> page = funcService.findPage(new Page<Func>(request, response), func);
		List<Func> list = page == null ? new ArrayList<Func>() : page.getList();
		int length = list == null ? 0 : list.size();
		for(int i=0; i<length; i++){
			String sql = list.get(i)==null ? "" : list.get(i).getFuncsql();
			list.get(i).setFuncsql(StringUtil.isEmpty(sql) ? null : StringUtil.entitycharFromString(sql));
			String statisticSql = list.get(i)==null ? "" : list.get(i).getFuncStatisticSql();
			list.get(i).setFuncStatisticSql(StringUtil.isEmpty(statisticSql) ? null : StringUtil.entitycharFromString(statisticSql));
		}
		page.setList(list);
		model.addAttribute("page", page);
		return "modules/func/func";
	}

	@RequiresPermissions(value={"rtc:func:add","rtc:func:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String editSchema(Func func, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Schema> schemaList = schemaService.findList(new Schema());
		model.addAttribute("schemaList", schemaList);
		
		if(func.getId() != null) {
			Func func1 = funcService.findUniqueByProperty("id", func.getId());
			func1.setFuncsql(StringUtil.entitycharFromString(func1.getFuncsql()));
			func1.setFuncStatisticSql(StringUtil.entitycharFromString(func1.getFuncStatisticSql()));
			model.addAttribute("func", func1);
			
			return "modules/func/funcForm";
		}
		return "modules/func/funcForm";
	}

	@RequiresPermissions(value={"rtc:func:add","rtc:func:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Func func, Model model, RedirectAttributes redirectAttributes) {
		func.setFuncsql(StringUtil.entitycharToString(func.getFuncsql()));
		func.setFuncStatisticSql(StringUtil.entitycharToString(func.getFuncStatisticSql()));
		if(StringUtil.isEmpty(func.getId())){
			func.setFuncid(DateUtil.getIntTimeId());
		}
		funcService.save(func);
		return "redirect:" + adminPath + "/rtc/func/list";
	}

	@RequiresPermissions("rtc:func:del")
	@RequestMapping(value = "delete")
	public String deleteAll(Func func, RedirectAttributes redirectAttributes) {
		funcService.delete(func);
		return "redirect:" + adminPath + "/rtc/func/list";
	}
	
	@RequiresPermissions("rtc:func:field")
	@RequestMapping(value = {"field"})
	public String getFieldList(FuncField funcField, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<FuncField> list = funcFieldService.findList(funcField);
		model.addAttribute("list", list);
		return "modules/func/field";
	}
	
	@RequiresPermissions(value={"rtc:field:edit"})
	@RequestMapping(value = "saveField")
	@ResponseBody
	public FuncField saveFuncField(FuncField field, HttpServletRequest request, HttpServletResponse response, Model model) {
		boolean isNew = StringUtil.isEmpty(field.getId()) ? true : false;
		try {
			field.setFieldDescribe(URLDecoder.decode(field.getFieldDescribe(),  "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		funcFieldService.save(field);
    	
		if (isNew){
			field = funcFieldService.get(field);
		}
		
		return field;
	}
}