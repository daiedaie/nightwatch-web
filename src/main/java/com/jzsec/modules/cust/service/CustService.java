package com.jzsec.modules.cust.service;

import com.jzsec.common.service.CrudService;
import com.jzsec.modules.cust.dao.CustDao;
import com.jzsec.modules.cust.entity.Cust;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 客户信息
 * @author 劉 焱
 * @date 2016-10-31
 */
@Service
@Transactional(readOnly = true)
public class CustService extends CrudService<CustDao, Cust> {

}
