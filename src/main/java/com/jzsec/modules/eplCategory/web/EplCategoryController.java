package com.jzsec.modules.eplCategory.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jzsec.common.persistence.Page;
import com.jzsec.common.web.BaseController;
import com.jzsec.modules.eplCategory.entity.EplCategory;
import com.jzsec.modules.eplCategory.service.EplCategoryService;

/**
 * 风控规则父类
 * @author 劉 焱
 * @date 2016-7-25
 * @tags
 */
@Controller
@RequestMapping(value = "${adminPath}/rtc/category")
public class EplCategoryController extends BaseController {
	@Autowired
	private EplCategoryService categoryService;

	@RequiresPermissions("rtc:category:list")
	@RequestMapping(value = {"list", ""})
	public String list(EplCategory category, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<EplCategory> parentTypes = categoryService.getEplParentTypeRoleList();
		List<String> categoryIds = new ArrayList<String>();
		for(EplCategory category2 : parentTypes){
			categoryIds.add(category2.getId());
		}
		category.setCategoryIds(categoryIds);
		Page<EplCategory> page = categoryService.findPage(new Page<EplCategory>(request, response), category);
		model.addAttribute("page", page);
		return "modules/category/category";
	}

	@RequiresPermissions(value={"rtc:category:add","rtc:category:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String edit(EplCategory category, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(category.getId() != null) {
			category = categoryService.findUniqueByProperty("id", category.getId());
		}
		model.addAttribute("category", category);
		return "modules/category/categoryForm";
	}

	@RequiresPermissions(value={"rtc:category:add","rtc:category:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(EplCategory category, Model model, RedirectAttributes redirectAttributes) {
		categoryService.save(category);
		return "redirect:" + adminPath + "/rtc/category/list";
	}

	@RequiresPermissions("rtc:category:del")
	@RequestMapping(value = "delete")
	public String delete(EplCategory category, RedirectAttributes redirectAttributes) {
		categoryService.delete(category);
		return "redirect:" + adminPath + "/rtc/category/list";
	}
}
