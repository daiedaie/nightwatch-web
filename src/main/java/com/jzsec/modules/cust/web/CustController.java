package com.jzsec.modules.cust.web;

import com.jzsec.common.persistence.Page;
import com.jzsec.common.web.BaseController;
import com.jzsec.modules.cust.entity.Cust;
import com.jzsec.modules.cust.service.CustService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 客户信息
 * @author 劉 焱
 * @date 2016-10-31
 */
@Controller
@RequestMapping(value = "${adminPath}/rtc/cust")
public class CustController extends BaseController {
	@Autowired
	private CustService custService;

	@RequiresPermissions("rtc:cust:list")
	@RequestMapping(value = {"list", ""})
	public String list(Cust cust, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Cust> page = custService.findPage(new Page<Cust>(request, response), cust);
		model.addAttribute("page", page);
		return "modules/cust/cust";
	}

	@RequiresPermissions("rtc:cust:view")
	@RequestMapping(value = "form")
	public String editSchema(Cust cust, HttpServletRequest request, HttpServletResponse response, Model model) {
		Cust cust1 = custService.findUniqueByProperty("id", cust.getId());
		model.addAttribute("cust", cust1);
		
		return "modules/cust/custForm";
	}

}