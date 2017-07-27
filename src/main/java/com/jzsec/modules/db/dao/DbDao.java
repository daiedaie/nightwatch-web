package com.jzsec.modules.db.dao;

import com.jzsec.common.persistence.CrudDao;
import com.jzsec.common.persistence.annotation.MyBatisDao;
import com.jzsec.modules.db.entity.Db;

/**
 * 外部数据源
 * @author 劉 焱
 * @date 2016-8-29
 * @tags
 */
@MyBatisDao
public interface DbDao extends CrudDao<Db> {
}