/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jzsec.modules.sys.service;

import com.jzsec.common.service.CrudService;
import com.jzsec.modules.sys.dao.OrgDao;
import com.jzsec.modules.sys.entity.Org;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 区域Service
 * @author jeeplus
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class OrgService extends CrudService<OrgDao, Org> {

	@Autowired
	private OrgDao orgDao;
	public List<Org> findAll(){
		return orgDao.findList(null);
	}

}
