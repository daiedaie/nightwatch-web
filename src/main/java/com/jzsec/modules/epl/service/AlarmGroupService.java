package com.jzsec.modules.epl.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jzsec.common.service.CrudService;
import com.jzsec.modules.epl.dao.AlarmGroupDao;
import com.jzsec.modules.epl.entity.AlarmGroup;

/**
 * 报警接收组
 * @author 劉 焱
 * @date 2016-9-5
 * @tags
 */
@Service
@Transactional(readOnly = true)
public class AlarmGroupService extends CrudService<AlarmGroupDao, AlarmGroup> {

	public List<AlarmGroup> findList(){
		return dao.findList();
	}
}
