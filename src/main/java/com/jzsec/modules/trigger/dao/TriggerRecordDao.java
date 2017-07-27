package com.jzsec.modules.trigger.dao;

import java.util.List;
import java.util.Map;

import com.jzsec.common.persistence.CrudDao;
import com.jzsec.common.persistence.Page;
import com.jzsec.common.persistence.annotation.MyBatisDao;
import com.jzsec.modules.trigger.entity.TriggerRecord;
import com.jzsec.modules.trigger.entity.TriggerStatistics;

/**
 * 风控触发记录持久层
 * @author 劉 焱
 * @date 2016-7-25
 * @tags
 */
@MyBatisDao
public interface TriggerRecordDao  extends CrudDao<TriggerRecord>{
	public List<TriggerRecord> findTriggerList(TriggerRecord triggerRecord);
	public List<TriggerRecord> findTriggerEpl(TriggerRecord triggerRecord);
	public List<TriggerRecord> findTriggerOrg(TriggerRecord triggerRecord);
	
	public List<TriggerStatistics> getTriggerStatistics(Map<String, String> requestMap);
	public List<TriggerStatistics> getTriggerStatisticsByPeriod(Map<String, String> requestMap);

	public List<TriggerStatistics> getTriggerStatisticsNew(Map<String, String> requestMap);

	public List<TriggerStatistics> getTriggerStatisticsOld(Map<String, String> requestMap);

	public List<TriggerRecord> findLatest(Map<String, Object> map);
	/**
	 * 获取列表统计信息
	 * @param triggerRecord
	 * @return
	 */
	public List<TriggerRecord> findStatisticsList(TriggerRecord triggerRecord);
	/**
	 * 获取列表统计汇总信息
	 * @param triggerRecord
	 * @return
	 */
	public TriggerRecord findStatisticsTotal(TriggerRecord triggerRecord);
	/**
	 * 获取未处理各类型触发记录数量
	 * @return
	 */
	public List<Map<String, Object>> findUndealTypes(Map<String, Object> map);
	/**
	 * 获取当天各状态触发记录数量
	 * @param map
	 * @return
	 */
	public TriggerStatistics findUndealAndTotalCount(Map<String, Object> map);
	public List<TriggerStatistics> getStatisticsByPeriod(Map<String, Object> requestMap);
	public List<TriggerStatistics> getStatisticsAdd(Map<String, Object> map);
	public TriggerStatistics findHistoryStatusCount(Map<String, Object> map);
	/**
	 * 获取触发记录页面统计表数据
	 * @param triggerRecord
	 * @return
	 */
	public List<TriggerRecord> findStatisticRecordList(TriggerRecord triggerRecord);
	/**
	 * 获取触发记录页面统计表数据总数
	 * @param triggerRecord 
	 * @return
	 */
//	public long findStatisticRecordCount(TriggerRecord triggerRecord);
	/**
	 * 根据一账通账号批量处理
	 * @param triggerRecord
	 */
	public void updateByYmtCode(TriggerRecord triggerRecord);
}