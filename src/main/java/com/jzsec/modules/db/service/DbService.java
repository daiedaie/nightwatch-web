package com.jzsec.modules.db.service;

import com.jzsec.common.service.CrudService;
import com.jzsec.modules.db.dao.DbDao;
import com.jzsec.modules.db.entity.Db;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 外部数据源
 * @author 劉 焱
 * @date 2016-8-29
 * @tags
 */
@Service
@Transactional(readOnly = true)
public class DbService extends CrudService<DbDao, Db> {

}