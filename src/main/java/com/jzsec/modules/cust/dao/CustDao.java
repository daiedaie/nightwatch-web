package com.jzsec.modules.cust.dao;

import com.jzsec.common.persistence.CrudDao;
import com.jzsec.common.persistence.annotation.MyBatisDao;
import com.jzsec.modules.cust.entity.Cust;

/**
 * @author 劉 焱
 * @date 2016-10-19
 */
@MyBatisDao
public interface CustDao extends CrudDao<Cust> {
}
