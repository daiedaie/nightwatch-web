package com.jzsec.modules.schema.dao;

import com.jzsec.common.persistence.CrudDao;
import com.jzsec.common.persistence.annotation.MyBatisDao;
import com.jzsec.modules.schema.entity.Schema;

/**
 * Created by caodaoxi on 16-7-30.
 */
@MyBatisDao
public interface SchemaDao extends CrudDao<Schema> {
}
