package com.jzsec.modules.func.dao;

import java.util.List;

import com.jzsec.common.persistence.CrudDao;
import com.jzsec.common.persistence.annotation.MyBatisDao;
import com.jzsec.modules.func.entity.EplFunc;
import com.jzsec.modules.func.entity.Func;

/**
 * @author 劉 焱
 * @date 2016-10-19
 */
@MyBatisDao
public interface FuncDao extends CrudDao<Func> {
	public List<Func> findNameList();
    public List<Func> findFuncByEplId(int eplId);
    public Func findFuncByFuncid(int funcid);
	public void deleteByEplId(Integer eplId);
	public void batchInsert(List<EplFunc> funcs);
}
