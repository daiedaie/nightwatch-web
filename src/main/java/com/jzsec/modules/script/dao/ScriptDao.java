package com.jzsec.modules.script.dao;

import com.jzsec.common.persistence.CrudDao;
import com.jzsec.common.persistence.annotation.MyBatisDao;
import com.jzsec.modules.script.entity.Script;


/**
 * esper脚本
 * @author 劉 焱
 * @date 2016-12-13
 */
@MyBatisDao
public interface ScriptDao extends CrudDao<Script>{
}