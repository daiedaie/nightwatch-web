package com.jzsec.modules.func.service;

import com.jzsec.common.service.CrudService;
import com.jzsec.modules.func.dao.FuncFieldDao;
import com.jzsec.modules.func.entity.FuncField;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 功能点字段
 * @author 劉 焱
 * @date 2016-10-28
 */
@Service
@Transactional(readOnly = true)
public class FuncFieldService extends CrudService<FuncFieldDao, FuncField> {

}
