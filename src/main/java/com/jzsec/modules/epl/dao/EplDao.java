package com.jzsec.modules.epl.dao;

import com.jzsec.common.persistence.CrudDao;
import com.jzsec.common.persistence.annotation.MyBatisDao;
import com.jzsec.modules.common.entity.PushEpl;
import com.jzsec.modules.epl.entity.Epl;
import com.jzsec.modules.eplCategory.entity.EplCategory;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 风控规则
 * @author 劉 焱
 * @date 2016-8-24
 * @tags
 */
@MyBatisDao
public interface EplDao extends CrudDao<Epl> {
	public List<Epl> findEplCategory();
	public List<Epl> findParentTypeList();
	public PushEpl pushZookeeper(Integer eplId);
	public Epl findAlarmEpl(Integer eplId);
	public void updateStatus(Epl epl);
	/**
	 * 进获取EPL表中字段,与其他表无关联
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public Epl findUniqueEplByProperty(@Param(value="propertyName")String propertyName, @Param(value="value")Object value);
	/**
	 * 获取用户权限内能够查看的规则类型
	 * @param map
	 * @return
	 */
	public List<Epl> findEplTypeList(Map<String, Object> map);
	/**
	 * 获取规则类型及其关联父类型, 用于列表显示
	 * @param map
	 * @return
	 */
	public List<Epl> findEplTypeRelateParentList(Map<String, Object> map);;
}