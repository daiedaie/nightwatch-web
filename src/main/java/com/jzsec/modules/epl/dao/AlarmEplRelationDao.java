package com.jzsec.modules.epl.dao;

import com.jzsec.common.persistence.CrudDao;
import com.jzsec.common.persistence.annotation.MyBatisDao;
import com.jzsec.modules.epl.entity.AlarmEplRelation;

/**
 * 风控规则与报警规则关联
 * @author 劉 焱
 * @date 2016-8-31
 * @tags
 */
@MyBatisDao
public interface AlarmEplRelationDao extends CrudDao<AlarmEplRelation> {

	public void deleteByEplId(Integer eplId);

}
