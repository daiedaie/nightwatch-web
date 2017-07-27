package com.jzsec.modules.watch.service;

import com.jzsec.common.service.CrudService;
import com.jzsec.modules.watch.dao.WatchDao;
import com.jzsec.modules.watch.entity.Watch;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 重点观察客户信息处理
 * @author 劉 焱
 * @date 2017-3-13
 */
@Service
@Transactional(readOnly = true)
public class WatchService extends CrudService<WatchDao, Watch> {

	@Transactional(readOnly = false)
	public void add(Watch watch) {
		Watch watch1 = dao.findUniqueByProperty("ymt_code", watch.getYmtCode());
		watch.setIsFollow("1");
		if(watch1 != null){
			dao.update(watch);
		} else {
			dao.insert(watch);
		}
	}
	
	@Transactional(readOnly = false)
	public void cancel(Watch watch) {
		watch.setIsFollow("0");
		dao.update(watch);
	}

}
