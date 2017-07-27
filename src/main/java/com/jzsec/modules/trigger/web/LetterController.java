package com.jzsec.modules.trigger.web;

import com.jzsec.common.persistence.Page;
import com.jzsec.common.web.BaseController;
import com.jzsec.modules.trigger.entity.Letter;
import com.jzsec.modules.trigger.service.LetterService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 监管函信息
 * @author 劉 焱
 * @date 2016-10-31
 */
@Controller
@RequestMapping(value = "${adminPath}/rtc/letter")
public class LetterController extends BaseController {
	@Autowired
	private LetterService letterService;

	@RequiresPermissions("rtc:letter:list")
	@RequestMapping(value = {"list", ""})
	public String list(Letter letter, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Letter> page = letterService.findPage(new Page<Letter>(request, response), letter);
		model.addAttribute("page", page);
		return "modules/trigger/letter";
	}

	@RequiresPermissions("rtc:letter:view")
	@RequestMapping(value = "form")
	public String editSchema(Letter letter, HttpServletRequest request, HttpServletResponse response, Model model) {
		Letter letter1 = letterService.findUniqueByProperty("id", letter.getId());
		model.addAttribute("letter", letter1);
		
		return "modules/trigger/letterForm";
	}

}