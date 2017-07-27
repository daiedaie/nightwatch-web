package com.jzsec.modules.epl.dao;

import java.util.List;

import com.jzsec.common.persistence.CrudDao;
import com.jzsec.common.persistence.annotation.MyBatisDao;
import com.jzsec.modules.epl.entity.EplGroupKey;

/**
 * @author 劉 焱
 * @date 2016-10-19
 */
@MyBatisDao
public interface EplGroupKeyDao extends CrudDao<EplGroupKey> {
	public void deleteByEplId(Integer eplId);
	public void batchInsert(List<EplGroupKey> funcs);
}
