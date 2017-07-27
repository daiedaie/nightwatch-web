/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jzsec.modules.udf.service;

import com.jzsec.common.persistence.Page;
import com.jzsec.common.service.CrudService;
import com.jzsec.modules.trigger.dao.TriggerRecordDao;
import com.jzsec.modules.trigger.entity.TriggerRecord;
import com.jzsec.modules.udf.dao.UdfDao;
import com.jzsec.modules.udf.entity.Udf;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 预警触发记录
 * @author 劉 焱
 * @date 2016-8-19
 * @tags
 */
@Service
@Transactional(readOnly = true)
public class UdfService extends CrudService<UdfDao, Udf> {
	/**
	 * 分页查询
	 * @param page
	 * @param udf
	 * @return
	 */
	public Page<Udf> find(Page<Udf> page, Udf udf) {
		udf.setPage(page);
		List<Udf> list = dao.findList(udf);
		page.setList(list);
		return page;
	}
}