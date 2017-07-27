package com.jzsec.modules.trigger.service;

import com.jzsec.common.service.CrudService;
import com.jzsec.modules.trigger.dao.LetterDao;
import com.jzsec.modules.trigger.entity.Letter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 监管函信息
 * @author 劉 焱
 * @date 2016-10-31
 */
@Service
@Transactional(readOnly = true)
public class LetterService extends CrudService<LetterDao, Letter> {

}
