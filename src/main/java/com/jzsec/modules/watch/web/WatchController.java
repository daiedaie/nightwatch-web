package com.jzsec.modules.watch.web;

import java.util.List;

import com.jzsec.common.persistence.Page;
import com.jzsec.common.web.BaseController;
import com.jzsec.modules.epl.entity.Epl;
import com.jzsec.modules.eplCategory.service.EplCategoryService;
import com.jzsec.modules.watch.entity.Watch;
import com.jzsec.modules.watch.service.WatchService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 重点关注
 * @author 劉 焱
 * @date 2017-3-13
 */
@Controller
@RequestMapping(value = "${adminPath}/rtc/watch")
public class WatchController extends BaseController {
	@Autowired
	private WatchService watchService;
	@Autowired
	private EplCategoryService categoryService;
	
	@RequiresPermissions("rtc:watch:list")
	@RequestMapping(value = {"list", ""})
	public String list(Watch watch, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<Epl> eplCategories = categoryService.getEplTypeRoleList();
		watch.setEplList(eplCategories);
		Page<Watch> page = watchService.findPage(new Page<Watch>(request, response), watch);
		model.addAttribute("page", page);
		return "modules/trigger/watchList";
	}

	@RequiresPermissions("rtc:watch:changeWatch")
	@RequestMapping(value = "add1")
	public String addWatch1(Watch watch, RedirectAttributes redirectAttributes) {
		watchService.add(watch);
		return "redirect:" + adminPath + "/rtc/cust/list";
	}
	
	@RequiresPermissions("rtc:watch:changeWatch")
	@RequestMapping(value = "add2")
	public String addWatch2(Watch watch, RedirectAttributes redirectAttributes) {
		watchService.add(watch);
		return "redirect:" + adminPath + "/rtc/watch/list";
	}
	
	@RequiresPermissions("rtc:watch:changeWatch")
	@RequestMapping(value = "cancel1")
	public String cancelWatch1(Watch watch, RedirectAttributes redirectAttributes) {
		watchService.cancel(watch);
		
		return "redirect:" + adminPath + "/rtc/cust/list";
	}
	
	@RequiresPermissions("rtc:watch:changeWatch")
	@RequestMapping(value = "cancel2")
	public String cancelWatch2(Watch watch, RedirectAttributes redirectAttributes) {
		watchService.cancel(watch);
		
		return "redirect:" + adminPath + "/rtc/watch/list";
	}

}