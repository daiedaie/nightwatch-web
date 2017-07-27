package com.jzsec.modules.epl.dao;

import com.jzsec.common.persistence.CrudDao;
import com.jzsec.common.persistence.annotation.MyBatisDao;
import com.jzsec.modules.epl.entity.AlarmThreshold;

/**
 * 风控报警规则阀值
 * @author 劉 焱
 * @date 2016-9-5
 * @tags
 */
@MyBatisDao
public interface AlarmThresholdDao extends CrudDao<AlarmThreshold> {

	void deleteThresholds(Integer eplId);

}
