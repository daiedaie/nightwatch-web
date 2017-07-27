package com.jzsec.modules.trigger.dao;

import com.jzsec.common.persistence.CrudDao;
import com.jzsec.common.persistence.annotation.MyBatisDao;
import com.jzsec.modules.trigger.entity.Letter;

/**
 * @author 劉 焱
 * @date 2016-10-19
 */
@MyBatisDao
public interface LetterDao extends CrudDao<Letter> {
}
