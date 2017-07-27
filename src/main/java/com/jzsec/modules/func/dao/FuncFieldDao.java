package com.jzsec.modules.func.dao;

import java.util.List;

import com.jzsec.common.persistence.CrudDao;
import com.jzsec.common.persistence.annotation.MyBatisDao;
import com.jzsec.modules.func.entity.FuncField;

/**
 * 功能点字段
 * @author 劉 焱
 * @date 2016-10-28
 */
@MyBatisDao
public interface FuncFieldDao extends CrudDao<FuncField> {
	public void batchInsert(List<FuncField> funcFields);
	public void batchDelete(List<String> delIds);
	public void deletefields(FuncField field);
}
