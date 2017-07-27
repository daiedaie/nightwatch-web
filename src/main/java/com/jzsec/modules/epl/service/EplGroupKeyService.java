package com.jzsec.modules.epl.service;

import com.jzsec.common.service.CrudService;
import com.jzsec.modules.epl.dao.EplGroupKeyDao;
import com.jzsec.modules.epl.entity.EplGroupKey;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * EPLhash键值
 * @author 劉 焱
 * @date 2016-10-19
 */
@Service
@Transactional(readOnly = true)
public class EplGroupKeyService extends CrudService<EplGroupKeyDao, EplGroupKey> {


}
