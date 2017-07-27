package com.jzsec.modules.eplCategory.dao;

import com.jzsec.common.persistence.CrudDao;
import com.jzsec.common.persistence.annotation.MyBatisDao;
import com.jzsec.modules.eplCategory.entity.EplCategoryRelation;

/**
 * 风控规则父类
 * @author 劉 焱
 * @date 2016-7-25
 * @tags
 */
@MyBatisDao
public interface EplCategoryRelationDao extends CrudDao<EplCategoryRelation> {

	void insertBatch(EplCategoryRelation categoryRelation);

}