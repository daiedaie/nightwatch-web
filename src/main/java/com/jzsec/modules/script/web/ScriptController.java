package com.jzsec.modules.script.web;

import java.util.ArrayList;
import java.util.List;

import com.jzsec.common.persistence.Page;
import com.jzsec.common.utils.DateUtil;
import com.jzsec.common.utils.StringUtil;
import com.jzsec.common.utils.StringUtils;
import com.jzsec.common.web.BaseController;
import com.jzsec.modules.common.servie.CommonService;
import com.jzsec.modules.script.entity.Script;
import com.jzsec.modules.script.service.ScriptService;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * esper脚本配置
 * @author 劉 焱
 * @date 2016-12-13
 */
@Controller
@RequestMapping(value = "${adminPath}/rtc/script")
public class ScriptController extends BaseController {
	@Autowired
	private ScriptService scriptService;
	@Autowired
	private CommonService commonService;
		
	@RequiresPermissions("rtc:script:list")
	@RequestMapping(value = {"list", ""})
	public String list(Script script, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Script> page = scriptService.findPage(new Page<Script>(request, response), script);
		List<Script> list = page == null ? new ArrayList<Script>() : page.getList();
		int length = list == null ? 0 : list.size();
		for(int i=0; i<length; i++){
			String code = list.get(i)==null ? "" : list.get(i).getScriptCode();
			list.get(i).setScriptCode(StringUtil.isEmpty(code) ? null : StringUtil.entitycharFromString(code));
			String describe = list.get(i)==null ? "" : list.get(i).getScriptDescribe();
			list.get(i).setScriptDescribe(StringUtil.isEmpty(describe) ? null : StringUtil.entitycharFromString(describe));
		}
		page.setList(list);
		
		model.addAttribute("page", page);
		return "modules/script/script";
	}

	@RequiresPermissions(value={"rtc:script:add","rtc:script:edit"},logical= Logical.OR)
	@RequestMapping(value = "save")
	public String save(Script script, Model model, RedirectAttributes redirectAttributes) {
		script.setScriptCode(StringUtil.entitycharToString(script.getScriptCode()));
		script.setScriptDescribe(StringUtil.entitycharToString(script.getScriptDescribe()));
		if(StringUtil.isEmpty(script.getId())){
			script.setScriptId(DateUtil.getIntTimeId());
		}
		
		if(commonService.isEsperNorm(script.getScriptCode())){
			scriptService.save(script);
			
			try {
				scriptService.PushZookeeper(script, false);
			} catch (Exception e) {
				try {
					scriptService.PushZookeeper(script, false);
				} catch (Exception e1) {
					addMessage(redirectAttributes, "信息数据库保存成功, 但推送至zookeeper节点异常!");
					e1.printStackTrace();
				}
			}
		}else{
			addMessage(redirectAttributes, "函数脚本验证失败!");
		}
		
		return "redirect:" + adminPath + "/rtc/script/list";
	}

	/**
	 * 查看，增加，编辑表单页面
	 */
	@RequiresPermissions(value={"rtc:script::view","rtc:script:add","rtc:script:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Script script, Model model) {
		if (StringUtils.isNotBlank(script.getId())){
			script = scriptService.get(script);
			script.setScriptCode(StringUtil.entitycharFromString(script.getScriptCode()));
			script.setScriptDescribe(StringUtil.entitycharFromString(script.getScriptDescribe()));
		}
		
		model.addAttribute("script", script);
		return "modules/script/scriptForm";
	}

	@RequiresPermissions("rtc:script:del")
	@RequestMapping(value = "delete")
	public String delete(Script script, RedirectAttributes redirectAttributes) {
		scriptService.delete(script);
		
		try {
			scriptService.PushZookeeper(script, true);
		} catch (Exception e) {
			try {
				scriptService.PushZookeeper(script, true);
			} catch (Exception e1) {
				addMessage(redirectAttributes, "信息数据库删除成功, 但推送至zookeeper节点异常!");
				e1.printStackTrace();
			}
		}
		
		return "redirect:" + adminPath + "/rtc/script/?repage";
	}
 
}
