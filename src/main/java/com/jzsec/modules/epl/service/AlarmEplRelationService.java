package com.jzsec.modules.epl.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jzsec.common.service.CrudService;
import com.jzsec.modules.epl.dao.AlarmEplRelationDao;
import com.jzsec.modules.epl.entity.AlarmEplRelation;

/**
 * 风控规则与报警规则关联
 * @author 劉 焱
 * @date 2016-8-31
 * @tags
 */
@Service
@Transactional(readOnly = true)
public class AlarmEplRelationService extends CrudService<AlarmEplRelationDao, AlarmEplRelation> {

}
