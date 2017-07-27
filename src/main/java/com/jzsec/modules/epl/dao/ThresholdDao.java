package com.jzsec.modules.epl.dao;

import java.util.List;

import com.jzsec.common.persistence.CrudDao;
import com.jzsec.common.persistence.annotation.MyBatisDao;
import com.jzsec.modules.epl.entity.Threshold;

/**
 * 风控规则阀值
 * @author 劉 焱
 * @date 2016-8-24
 * @tags
 */
@MyBatisDao
public interface ThresholdDao extends CrudDao<Threshold> {
    public Threshold queryThresholdByThreshold(Threshold threshold);
    public int deleteThresholds(Integer eplId);
	public List<Threshold> findAllThresholds(Threshold threshold);
	public void batchInsert(List<Threshold> thresholds);
	public void batchDelete(List<String> delIds);

}
