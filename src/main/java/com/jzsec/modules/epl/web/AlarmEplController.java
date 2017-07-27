package com.jzsec.modules.epl.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jzsec.common.web.BaseController;
import com.jzsec.modules.epl.entity.AlarmEpl;
import com.jzsec.modules.epl.service.AlarmEplService;

/**
 * 报警规则配置控制层
 * @author 劉 焱
 * @date 2016-7-25
 * @tags
 */
@Controller
@RequestMapping(value = "${adminPath}/alarmEpl")
public class AlarmEplController extends BaseController {
}
