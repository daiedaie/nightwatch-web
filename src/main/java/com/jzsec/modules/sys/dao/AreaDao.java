/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jzsec.modules.sys.dao;

import com.jzsec.common.persistence.TreeDao;
import com.jzsec.common.persistence.annotation.MyBatisDao;
import com.jzsec.modules.sys.entity.Area;

/**
 * 区域DAO接口
 * @author jeeplus
 * @version 2014-05-16
 */
@MyBatisDao
public interface AreaDao extends TreeDao<Area> {
	
}
