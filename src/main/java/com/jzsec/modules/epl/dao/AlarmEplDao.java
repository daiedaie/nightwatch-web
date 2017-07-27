package com.jzsec.modules.epl.dao;

import java.util.List;

import com.jzsec.common.persistence.CrudDao;
import com.jzsec.common.persistence.annotation.MyBatisDao;
import com.jzsec.modules.common.entity.PushAlarmEpl;
import com.jzsec.modules.epl.entity.AlarmEpl;
import com.jzsec.modules.epl.entity.Epl;

/**
 * 报警规则配置持久层
 * @author 劉 焱
 * @date 2016-7-25
 * @tags
 */
@MyBatisDao
public interface AlarmEplDao extends CrudDao<AlarmEpl> {

	List<Epl> findTypeList();

	void deleteByEplId(Integer alarmEplId);

	PushAlarmEpl pushZookeeper(Integer alarmEplId);

}