package com.jzsec.modules.watch.dao;

import com.jzsec.common.persistence.CrudDao;
import com.jzsec.common.persistence.annotation.MyBatisDao;
import com.jzsec.modules.watch.entity.Watch;

/**
 * 
 * @author 劉 焱
 * @date 2017-3-13
 */
@MyBatisDao
public interface WatchDao extends CrudDao<Watch> {

}
