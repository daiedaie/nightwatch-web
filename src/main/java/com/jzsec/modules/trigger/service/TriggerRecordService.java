package com.jzsec.modules.trigger.service;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.jzsec.common.utils.ElasticsearchUtils;
import com.jzsec.modules.func.dao.FuncDao;
import com.jzsec.modules.func.entity.Func;
import com.jzsec.modules.func.entity.FuncField;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jzsec.common.config.MyConst;
import com.jzsec.common.persistence.Page;
import com.jzsec.common.service.CrudService;
import com.jzsec.common.utils.DateUtil;
import com.jzsec.common.utils.DateUtils;
import com.jzsec.common.utils.StringUtil;
import com.jzsec.modules.sys.utils.DictUtils;
import com.jzsec.modules.trigger.dao.TriggerRecordDao;
import com.jzsec.modules.trigger.entity.Detail;
import com.jzsec.modules.trigger.entity.TriggerRecord;
import com.jzsec.modules.trigger.entity.TriggerStatistics;

/**
 * 预警触发记录
 * @author 劉 焱
 * @date 2016-8-19
 * @tags
 */
@Service
@Transactional(readOnly = true)
public class TriggerRecordService extends CrudService<TriggerRecordDao, TriggerRecord> {
	@Autowired
	private FuncDao funcDao;
	
	public TriggerRecord get(String id) {
		TriggerRecord entity = dao.get(id);
		try {
			entity = replaceStrVar(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entity;
	}
	

	/**
	 * 获取触发记录页面统计表数据
	 * @param eplList 
	 * @param triggerRecord 
	 * @param page 
	 * @param page
	 * @param triggerRecord
	 * @param eplList 
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public Page<TriggerRecord> findStatisticRecord(Page<TriggerRecord> page, TriggerRecord triggerRecord, List<TriggerRecord> eplList) throws JsonParseException, JsonMappingException, IOException {
		page.setPageNo(triggerRecord.getStatisticPageNo());
		page.setPageSize(triggerRecord.getStatisticPageSize());
		page.setFuncName("statisticPage");
		triggerRecord.setPage(page);
		List<TriggerRecord> list = dao.findStatisticRecordList(triggerRecord);
//		long count = dao.findStatisticRecordCount(triggerRecord);
		List<TriggerRecord> list1 = new ArrayList<TriggerRecord>();
		if(list == null || list.size() != 0){
			for(TriggerRecord record : list){
				TriggerRecord record1 = record;
				for(TriggerRecord record2 : eplList){
					if(record.getEplId().equals(record2.getEplId())){
						record1.setEplName(record2.getEplName());
						record1.setTextState(record2.getTextState());
						record1.setEplDescribe(getEplSql(record2.getThresholds(), record2.getEplDescribe()));
						record1.setThresholds(record2.getThresholds());
					}
				}
				list1.add(record1);
			}
		}
		
		page.setList(list1);
		return page;
	}


	/**
	 * 触发记录列表
	 * @param page
	 * @param triggerRecord
	 * @param orderBy2 
	 * @return
	 * @throws Exception 
	 */
	public Page<TriggerRecord> findRecord(Page<TriggerRecord> page, TriggerRecord triggerRecord, List<TriggerRecord> eplList, String orderBy2) throws Exception {
		page.setFuncName("contentPage");
		page.setOrderBy(StringUtils.isBlank(orderBy2) ? "tr.trigger_time DESC" : orderBy2);
		triggerRecord.setPage(page);
		
		List<TriggerRecord> list = dao.findTriggerList(triggerRecord);
		List<TriggerRecord> list1 = new ArrayList<TriggerRecord>();
		if(list == null || list.size() != 0){
			for(TriggerRecord record : list){
				TriggerRecord record1 = record;
				for(TriggerRecord record2 : eplList){
					if(record.getEplId().equals(record2.getEplId())){
						record1.setEplName(record2.getEplName());
						record1.setTextState(record2.getTextState());
						record1.setEplDescribe(record2.getEplDescribe());
						record1.setThresholds(record2.getThresholds());
						record1.setStatusName(DictUtils.getDictLabel(record.getStatus()+"","trigger_status",""));
						record1.setLevel(DictUtils.getDictLabel(record.getLevel()+"","trigger_level",""));
					}
				}
				list1.add(record1);
			}
		}
		page.setList(replaceStrVar(list1));
		return page;
	}

	/**
	 * 更新阅读状态
	 */
	@Transactional(readOnly = false)
	public void updateReadFlag(TriggerRecord record) {
		record.setReadFlag("1");
		dao.update(record);
	}

	/**
	 * 触发记录统计
	 * @throws ParseException 
	 */
	@Transactional(readOnly = false)
	public List<TriggerStatistics> getTriggerStatistics(String period) throws ParseException {
		Map<String, String> requestMap = getRequestMap(period);
		return dao.getTriggerStatistics(requestMap);
	}

	/**
	 * 阀值替换
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public List<TriggerRecord> replaceStrVar(List<TriggerRecord> list) throws JsonParseException, JsonMappingException, IOException{
		List<TriggerRecord> returnList = new ArrayList<TriggerRecord>();
		ObjectMapper mapper = new ObjectMapper();
		for(TriggerRecord triggerRecord : list){
			Map<String, Object> map = mapper.readValue(triggerRecord.getTriggerState(), Map.class);
			TriggerRecord record = triggerRecord;
			String triggerState = triggerRecord.getTextState();

			for (Object s : map.keySet()) {
				if("start_time".equals(s.toString()) || "end_time".equals(s.toString())){
					String time = DateUtils.getDateByTimestamp(Long.parseLong(map.get(s.toString()).toString()), "yyyy-MM-dd HH:mm:ss");
					triggerState = triggerState.replaceAll("\\$\\{".concat(s.toString()).concat("\\}"), time);
				}else{
					String value = map.get(s.toString()) == null ? "" : map.get(s.toString()).toString();
					triggerState = triggerState.replaceAll("\\$\\{".concat(s.toString()).concat("\\}"), value);
				}
			}
			String decrible = getEplSql(triggerRecord.getThresholds(), triggerRecord.getEplDescribe());
			record.setEplDescribe(decrible);
			record.setTriggerState(triggerState);
			returnList.add(record);
		}

			return returnList;
	}
	/**
	 * 单条阀值替换
	 * @param triggerRecord
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public TriggerRecord replaceStrVar(TriggerRecord triggerRecord) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		TriggerRecord record = triggerRecord;
		Map<String, Object> map = mapper.readValue(triggerRecord.getTriggerState(), Map.class);
		String triggerState = triggerRecord.getTextState();

		for (Object s : map.keySet()) {
			if("start_time".equals(s.toString()) || "end_time".equals(s.toString())){
				String time = DateUtils.getDateByTimestamp(Long.parseLong(map.get(s.toString()).toString()), "yyyy-MM-dd HH:mm:ss");
				triggerState = triggerState.replaceAll("\\$\\{".concat(s.toString()).concat("\\}"), time);
			}else{
				String value = map.get(s.toString()) == null ? "" : map.get(s.toString()).toString();
				triggerState = triggerState.replaceAll("\\$\\{".concat(s.toString()).concat("\\}"), value);
			}
		}
		String decrible = getEplSql(triggerRecord.getThresholds(), triggerRecord.getEplDescribe());
		record.setEplDescribe(decrible);
		record.setTriggerState(triggerState);

		return record;
	}
	/**
	 * 规则阀值替换
	 * @param thresholds
	 * @param eplSql
	 * @return
	 */
	public String getEplSql(String thresholds, String eplSql){
		if(thresholds != null && !StringUtils.isEmpty(thresholds)) {
			String[] dts = thresholds.split(",");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			for(String threshold : dts) {
				String[] fields = threshold.split("=");
				paramMap.put(fields[0], fields[1]);
			}
			eplSql = replaceStrVar(paramMap, eplSql);
		}
		return eplSql;
	}
	/**
	 * 替换规则中的阀值
	 * @param map
	 * @param template
	 * @return
	 */
	public String replaceStrVar(Map<String, Object> map, String template){
		for (Object s : map.keySet()) {
			String value = map.get(s.toString()) == null ? "" : map.get(s.toString()).toString();
			template = template.replaceAll("\\$\\{".concat(s.toString()).concat("\\}"), value);
		}
		return template;
	}

	public String replaceStrVarByJSON(JSONObject obj, String template){
		if(obj.containsKey("start_time") && obj.containsKey("end_time")) {
			List<String> dateList = DateUtil.getDateList("rtc-", obj.getLong("start_time"), obj.getLong("end_time"));

			if(dateList != null && dateList.size() > 0) {
				StringBuilder indexStr = new StringBuilder();
				for(int j = 0; j < dateList.size(); j++) {
					indexStr.append(dateList.get(j));
					if(j < dateList.size() - 1) indexStr.append(",");
				}
				obj.put("db", indexStr.toString());
			}

		}

		for (Object s : obj.keySet()) {
			String value = obj.get(s.toString()) == null ? "" : obj.get(s.toString()).toString();
			template = template.replaceAll("\\$\\{".concat(s.toString()).concat("\\}"), value);
		}
		return template;
	}

	/**
	 * 触发记录获取详情页
	 * @return
	 */
	public List<JSONObject> queryDetailPage(List<FuncField> funcFields, String funcsql) {
		List<JSONObject> details = ElasticsearchUtils.search(funcFields, funcsql);
		return details;
	}

//	/**
//	 * 触发记录获取详情页
//	 * @param pageNumber
//	 * @param pageSize
//	 * @param fundid
//	 * @param startDate
//	 * @param endDate
//	 * @return
//	 */
//	public List<Detail> queryDetailPage(Integer pageNumber, Integer pageSize, long fundid, long startDate, long endDate) {
//		String[] fieldList = new String[]{"@fields.timestamp", "@fields.rc_msg_para.fundid", "@fields.stationaddr", "@fields.rc_msg_para.orderprice", "@fields.rc_msg_para.market", "@fields.rc_msg_para.orderqty", "@fields.rc_msg_para.stkcode"};
//		List<Detail> details = null;
////		details = getDetail(fundid, fieldList, startDate, endDate, pageNumber, pageSize);
//		return details;
//	}
//
//	/**
//	 * 获取详情
//	 * @param fieldList
//	 * @param start
//	 * @param end
//	 * @param from
//	 * @param size
//	 * @return
//	 */
//	public static List<JSONObject> getDetail(String indexType, long ymtCode, String[] fieldList, long start, long end, int from, int size) {
//		Client client = getClient();
//		ArrayList<JSONObject> details = null;
//		try {
//			QueryBuilder termQuery = termQuery( "@fields.ymt_code", ymtCode);
//			QueryBuilder rangeQuery = rangeQuery("@fields.timestamp").from(start).to(end);
//			QueryBuilder boolQuery = boolQuery().must(termQuery).must(rangeQuery);
//			SearchResponse response = client.prepareSearch("rtc*").setTypes(indexType)
//					.addFields(fieldList)
//					.setQuery(boolQuery).setFrom(from).setSize(size).execute().actionGet();
//			SearchHits hits = response.getHits();
//			if(hits.getTotalHits() == 0) return details;
//			details = new ArrayList<JSONObject>();
//			for (SearchHit hit : hits.getHits()) {
//				Detail detail = new Detail();
//				detail.setFundid(Long.parseLong(hit.field("@fields.rc_msg_para.fundid").getValue().toString()));
//				detail.setMarket(hit.field("@fields.rc_msg_para.market").getValue().toString());
//				detail.setOrderprice(Double.parseDouble(hit.field("@fields.rc_msg_para.orderprice").getValue().toString()));
//				detail.setOrderqty(Double.parseDouble(hit.field("@fields.rc_msg_para.orderqty").getValue().toString()));
//				detail.setStationaddr(hit.field("@fields.stationaddr").getValue().toString());
//				detail.setStkcode(hit.field("@fields.rc_msg_para.stkcode").getValue().toString());
//				detail.setDt(new Date(Long.parseLong(hit.field("@fields.timestamp").getValue().toString())));
////				details.add(detail);
//			}
//			return details;
//		} finally {
//			client.close();
//		}
//	}
//
//	public static Client getClient() {
//		Client client = null;
//		Settings settings = null;
//		try {
////			settings = ImmutableSettings.settingsBuilder()
////					.put("cluster.name", "rtc").build();
//			settings = Settings.settingsBuilder()
//					.put("cluster.name", "rtc").build();
//			client = TransportClient.builder().settings(settings).build()
//					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.1.171.103"), 9300))
//					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.1.171.104"), 9300))
//					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.1.171.105"), 9300));
////			client = new TransportClient(settings)
////					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.1.171.103"), 9300))
////					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.1.171.104"), 9300))
////					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.1.171.105"), 9300));
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
//		return client;
//	}

	/**
	 * 根据时段统计触发记录
	 * @throws ParseException 
	 */
	@Transactional(readOnly = false)
	public List<TriggerStatistics> getTriggerStatisticsByPeriod(String period) throws ParseException {
		Map<String, String> requestMap = getRequestMap(period);
		return dao.getTriggerStatisticsByPeriod(requestMap);
	}

	/**
	 * 获取SQL查询参数
	 * @param period
	 * @return
	 * @throws ParseException
	 */
	private Map<String, String> getRequestMap(String period) throws ParseException {
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("period", period);
		String startDate = "";
		if("minute".equals(period)){
			startDate = DateUtils.getIntervalTime(new Date(), MyConst.YYYY_MM_DD_HH_MM, -149*60);
		}else if("hour".equals(period)){
			startDate = DateUtils.getIntervalTime(new Date(), MyConst.YYYY_MM_DD_HH, -23*60*60);
		}else{
			startDate = DateUtils.getIntervalTime(new Date(), MyConst.YYYY_MM_DD, -29*24*60*60);
		}
		requestMap.put("startDate", startDate);
		return requestMap;
	}

	/**
	 * 根据时段统计触发记录并封装数据
	 * @param period
	 * @return
	 * @throws ParseException 
	 */
	public Map<String, Object> getTriggerStatisticsByPeriod2(String period, String endDateStr) throws ParseException {
		if(StringUtil.isEmpty(period)){
			period = "day";
		}
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("period", period);
		String startDate = "";
		Date endDate = StringUtil.isEmpty(endDateStr) ? new Date() : DateUtil.stringToDate(endDateStr, MyConst.YYYY_MM_DD_HH_MM_SS);
		if("minute".equals(period)){
			startDate = DateUtils.getIntervalTime(endDate, MyConst.YYYY_MM_DD_HH_MM, -29*60);
		}else if("hour".equals(period)){
			startDate = DateUtils.getIntervalTime(endDate, MyConst.YYYY_MM_DD_HH, -23*60*60);
		}else{
			startDate = DateUtils.getIntervalTime(endDate, MyConst.YYYY_MM_DD, -29*24*60*60);
		}
		requestMap.put("startDate", startDate);
		requestMap.put("endDate", DateUtil.dateToString(endDate, MyConst.YYYY_MM_DD_HH_MM_SS));
		
		List<TriggerStatistics> triggerStatistics = dao.getTriggerStatisticsByPeriod(requestMap);
		Map<String, Object> data = new HashMap<String, Object>();
		//数据当前增长情况
		TriggerStatistics triggerStatistics2 = getTriggerIncrease(requestMap);
		Map<String, Object> statisticsMap = new HashMap<String, Object>();
		statisticsMap.put("totalAdd", triggerStatistics2.getTotalAdd());
		statisticsMap.put("totalPre", triggerStatistics2.getTotalPre());
		statisticsMap.put("unprocessedAdd", triggerStatistics2.getUnprocessedAdd());
		statisticsMap.put("unprocessedPre", triggerStatistics2.getUnprocessedPre());
		statisticsMap.put("processingAdd", triggerStatistics2.getProcessingAdd());
		statisticsMap.put("processingPre", triggerStatistics2.getProcessingPre());
		statisticsMap.put("processedAdd", triggerStatistics2.getProcessedAdd());
		statisticsMap.put("processedPre", triggerStatistics2.getProcessedPre());
		statisticsMap.put("ignoredAdd", triggerStatistics2.getIgnoredAdd());
		statisticsMap.put("ignoredPre", triggerStatistics2.getIgnoredPre());
		data.put("triggerStatistics", statisticsMap);
		
		List<String> map = new ArrayList<String>();
		//获得开始时间结束时间之间的日期数组
		//以天为单位统计[默认]
		if ("day".equals(period)) {
			map = DateUtil.getEveryDayOfMonthStr(startDate, new Date(), MyConst.YYYY_MM_DD, MyConst.YYYY_MM_DD);
		//以小时为单位统计
		} else if ("hour".equals(period)) {
			map = DateUtil.getEveryHourStr(new Date(), 1, 24, MyConst.YYYY_MM_DD_HH);
		} else if ("minute".equals(period)) {
			map = DateUtil.getEveryMinuteStr(new Date(), 1, 30, MyConst.YYYY_MM_DD_HH_MM);
		}
		
		return dataHandle(period, map, triggerStatistics, data);
	}

	/**
	 * 触发记录当前时间段增长情况
	 * @param requestMap
	 * @return
	 */
	private TriggerStatistics getTriggerIncrease(Map<String, String> requestMap) {
		List<TriggerStatistics> list1 = dao.getTriggerStatisticsNew(requestMap);
		List<TriggerStatistics> list2 = dao.getTriggerStatisticsOld(requestMap);
		TriggerStatistics triggerStatistics1 = new TriggerStatistics();
		TriggerStatistics triggerStatistics2 = new TriggerStatistics();
		if(list1 != null && list1.size() != 0){
			triggerStatistics1 = list1.get(0);
		}
		if(list2 != null && list2.size() != 0){
			triggerStatistics2 = list2.get(0);
		}
		
		TriggerStatistics triggerStatistics = new TriggerStatistics();
		triggerStatistics.setProcessedAdd(triggerStatistics1.getProcessed() - triggerStatistics2.getProcessed());
		triggerStatistics.setProcessingAdd(triggerStatistics1.getProcessing() - triggerStatistics2.getProcessing());
		triggerStatistics.setIgnoredAdd(triggerStatistics1.getIgnored() - triggerStatistics2.getIgnored());
		triggerStatistics.setUnprocessedAdd(triggerStatistics1.getUnprocessed() - triggerStatistics2.getUnprocessed());
		triggerStatistics.setTotalAdd(triggerStatistics1.getTotal() - triggerStatistics2.getTotal());
		
		if(triggerStatistics1.getProcessed() == 0 && triggerStatistics2.getProcessed() == 0){
			triggerStatistics.setProcessedPre(0);
		}else if(triggerStatistics2.getProcessed() == 0){
			triggerStatistics.setProcessedPre(100);
		}else{
			triggerStatistics.setProcessedPre((triggerStatistics1.getProcessed() - triggerStatistics2.getProcessed())*100 / triggerStatistics2.getProcessed());
		}
		
		if(triggerStatistics1.getProcessing() == 0 && triggerStatistics2.getProcessing() == 0){
			triggerStatistics.setProcessingPre(0);
		}else if(triggerStatistics2.getProcessing() == 0){
			triggerStatistics.setProcessingPre(100);
		}else{
			triggerStatistics.setProcessingPre((triggerStatistics1.getProcessing() - triggerStatistics2.getProcessing())*100 / triggerStatistics2.getProcessing());
		}
		
		if(triggerStatistics1.getIgnored() == 0 && triggerStatistics2.getIgnored() == 0){
			triggerStatistics.setIgnoredPre(0);
		}else if(triggerStatistics2.getIgnored() == 0){
			triggerStatistics.setIgnoredPre(100);
		}else{
			triggerStatistics.setIgnoredPre((triggerStatistics1.getIgnored() - triggerStatistics2.getIgnored())*100 / triggerStatistics2.getIgnored());
		}
		
		if(triggerStatistics1.getUnprocessed() == 0 && triggerStatistics2.getUnprocessed() == 0){
			triggerStatistics.setUnprocessedPre(0);
		}else if(triggerStatistics2.getUnprocessed() == 0){
			triggerStatistics.setUnprocessedPre(100);
		}else{
			triggerStatistics.setUnprocessedPre((triggerStatistics1.getUnprocessed() - triggerStatistics2.getUnprocessed())*100 / triggerStatistics2.getUnprocessed());
		}
		
		if(triggerStatistics1.getTotal() == 0 && triggerStatistics2.getTotal() == 0){
			triggerStatistics.setTotalPre(0);
		}else if(triggerStatistics2.getTotal() == 0){
			triggerStatistics.setTotalPre(100);
		}else{
			triggerStatistics.setTotalPre((triggerStatistics1.getTotal() - triggerStatistics2.getTotal())*100 / triggerStatistics2.getTotal());
		}
		
		return triggerStatistics;
	}

	
	/**
	 * 数据封装
	 * @param map
	 * @param data
	 * @return
	 */
	private Map<String, Object> dataHandle(String period, List<String> map, List<TriggerStatistics> triggerStatistics, Map<String, Object> data) {
		List<String> xAxisData = new ArrayList<String>();
		xAxisData.addAll(map);
		
		//设置日期横轴数据
		List<String> xAxisData2 = new ArrayList<String>();
		if ("day".equals(period)) {
			for(String date : xAxisData){
				xAxisData2.add(Integer.parseInt(date.substring(5, 7))+"月"+Integer.parseInt(date.substring(8, 10))+"日");
			}
		} else if ("hour".equals(period)) {
			for(String date : xAxisData){
				xAxisData2.add(date.substring(11));
			}
		} else if ("minute".equals(period)) {
			for(String date : xAxisData){
				xAxisData2.add(date.substring(11));
			}
		}
		data.put("xAxisData", xAxisData2);
		
		//设置数据类别[中间标题]
		List<String> legendData1 = new ArrayList<String>();
		legendData1.add("总数");
		legendData1.add("未处理数");
		data.put("legendData1", legendData1);

		//横轴长度
		int xSize = xAxisData.size();
		//总数
		int[] total = new int[xSize];
		//未处理数
		int[] unprocesseds = new int[xSize];
		//已处理数
//		int[] processeds = new int[xSize];
//		//忽略数
//		int[] ignoreds = new int[xSize];
//		//处理中数
//		int[] processings = new int[xSize];

//		System.out.println(triggerStatistics);
		for(TriggerStatistics statistic : triggerStatistics){
			String date = statistic.getTriggerDate();
			int index = xAxisData.indexOf(date);
			total[index] = statistic.getTotal();
			unprocesseds[index] = statistic.getUnprocessed();
		}
		
		//统计
		data.put("unprocesseds", unprocesseds);
		data.put("total", total);

		return data;
	}
	
	/**
	 * 获取最新十条未处理记录
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> findLatest(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		List<TriggerRecord> returnList = replaceStrVar(dao.findLatest(map));
		for(TriggerRecord record : returnList){
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("triggerTime", record.getTriggerTime());
			map1.put("eplDescribe", record.getEplDescribe());
			map1.put("eplName", record.getEplName());
			map1.put("ymtCode", record.getYmtCode());
//			map1.put("orgname", record.getOrgname());
			map1.put("triggerState", record.getTriggerState());
			map1.put("status", record.getStatus());
			map1.put("dealTime", record.getDealTime());
			
			list.add(map1);
		}
		return list;
	}
	/**
	 * 触发记录统计页面分页查询
	 * @param page
	 * @param triggerRecord
	 * @return
	 */
	public Page<TriggerRecord> findStatistics(Page<TriggerRecord> page, TriggerRecord triggerRecord) {
		triggerRecord.setPage(page);
		List<TriggerRecord> list = dao.findStatisticsList(triggerRecord);
		triggerRecord.setPage(null);
		TriggerRecord total = dao.findStatisticsTotal(triggerRecord);
		if(total == null){
			total = new TriggerRecord();
		}
		TriggerRecord record = new TriggerRecord();
		record.setEplName("total");
		record.setDealCount(total.getDealCount());
		record.setIgnoreCount(total.getIgnoreCount());
		record.setUndealCount(total.getUndealCount());
		record.setVerificationCount(total.getVerificationCount());
		record.setTotalCount(total.getTotalCount());
		list.add(record);
		
		page.setList(list);
		return page;
	}
	/**
	 * 获取未处理各类型触发记录数量
	 * @return
	 */
	public List<Map<String, Object>> findUndealTypes(Map<String, Object> map) {
		return  dao.findUndealTypes(map);
	}
	/**
	 * 获取各状态触发记录数量
	 * @param map
	 * @return
	 */
	public TriggerStatistics findUndealAndTotalCount(Map<String, Object> map) {
		return dao.findUndealAndTotalCount(map);
	}
	/**
	 * 获取日/时/分图形数据
	 * @return
	 * @throws ParseException 
	 */
	public Map<String, Object> getStatisticsByPeriod(Map<String, Object> map) throws ParseException {
		String endDate = (String) map.get("endDate");
		//数据当前增长情况
		List<TriggerStatistics> statisticsAdd = dao.getStatisticsAdd(map);
		TriggerStatistics dayStatisticsNew = new TriggerStatistics();
		TriggerStatistics hourStatisticsNew = new TriggerStatistics();
		TriggerStatistics minuteStatisticsNew = new TriggerStatistics();
		TriggerStatistics dayStatisticsOld = new TriggerStatistics();
		TriggerStatistics hourStatisticsOld = new TriggerStatistics();
		TriggerStatistics minuteStatisticsOld = new TriggerStatistics();
		
		if(statisticsAdd != null && statisticsAdd.size() != 0){
			for(TriggerStatistics triggerStatistics : statisticsAdd){
				if("day".equals(triggerStatistics.getPeriod()) && triggerStatistics.getIsNew() == 1){
					dayStatisticsNew = triggerStatistics;
				}else if("hour".equals(triggerStatistics.getPeriod()) && triggerStatistics.getIsNew() == 1){
					hourStatisticsNew = triggerStatistics;
				}else if("minute".equals(triggerStatistics.getPeriod()) && triggerStatistics.getIsNew() == 1){
					minuteStatisticsNew = triggerStatistics;
				}else if("day".equals(triggerStatistics.getPeriod()) && triggerStatistics.getIsNew() == 0){
					dayStatisticsOld = triggerStatistics;
				}else if("hour".equals(triggerStatistics.getPeriod()) && triggerStatistics.getIsNew() == 0){
					hourStatisticsOld = triggerStatistics;
				}else if("minute".equals(triggerStatistics.getPeriod()) && triggerStatistics.getIsNew() == 0){
					minuteStatisticsOld = triggerStatistics;
				}
			}
		}
		
		Map<String, Object> dayStatisticsMap = getTriggerIncrease(dayStatisticsNew, dayStatisticsOld);
		Map<String, Object> hourStatisticsMap = getTriggerIncrease(hourStatisticsNew, hourStatisticsOld);
		Map<String, Object> minuteStatisticsMap = getTriggerIncrease(minuteStatisticsNew, minuteStatisticsOld);
		
		//获得开始时间结束时间之间的日期数组
		List<String> dayMap = DateUtil.getEveryDayStr(DateUtil.stringToDate(endDate, MyConst.YYYY_MM_DD_HH_MM_SS), 1, 30, MyConst.YYYY_MM_DD);
		List<String> hourMap = DateUtil.getEveryHourStr(DateUtil.stringToDate(endDate, MyConst.YYYY_MM_DD_HH_MM_SS), 1, 24, MyConst.YYYY_MM_DD_HH);
		List<String> minuteMap = DateUtil.getEveryMinuteStr(DateUtil.stringToDate(endDate, MyConst.YYYY_MM_DD_HH_MM_SS), 1, 30, MyConst.YYYY_MM_DD_HH_MM);
		
		//设置日期横轴数据
		List<String> dayXAxisData = new ArrayList<String>();
		List<String> dayXAxisData2 = new ArrayList<String>();
		dayXAxisData.addAll(dayMap);
		for(String date : dayXAxisData){
			dayXAxisData2.add(Integer.parseInt(date.substring(5, 7))+"月"+Integer.parseInt(date.substring(8, 10))+"日");
		}
		List<String> hourXAxisData = new ArrayList<String>();
		List<String> hourXAxisData2 = new ArrayList<String>();
		hourXAxisData.addAll(hourMap);
		for(String date : hourXAxisData){
			hourXAxisData2.add(date.substring(11));
		}
		List<String> minuteXAxisData = new ArrayList<String>();
		List<String> minuteXAxisData2 = new ArrayList<String>();
		minuteXAxisData.addAll(minuteMap);
		for(String date : minuteXAxisData){
			minuteXAxisData2.add(date.substring(11));
		}
		
		//设置数据类别[中间标题]
		List<String> legendData1 = new ArrayList<String>();
		legendData1.add("总数");
		legendData1.add("未处理数");
		
		//封装图形数据
		Map<String, Object> requestMap = new HashMap<String, Object>();
		String minuteStartDate = DateUtils.getIntervalTime(endDate, MyConst.YYYY_MM_DD_HH_MM, -29*60);
		String hourStartDate = DateUtils.getIntervalTime(endDate, MyConst.YYYY_MM_DD_HH, -23*60*60);
		String dayStartDate = DateUtils.getIntervalTime(endDate, MyConst.YYYY_MM_DD, -29*24*60*60);
		requestMap.put("endDate", endDate);
		requestMap.put("dayStartDate", dayStartDate);
		requestMap.put("hourStartDate", hourStartDate);
		requestMap.put("minuteStartDate", minuteStartDate);
		requestMap.put("eplList", map.get("eplList"));
		List<TriggerStatistics> statistics = dao.getStatisticsByPeriod(requestMap);
		List<TriggerStatistics> dayStatistics = new ArrayList<TriggerStatistics>();
		List<TriggerStatistics> hourStatistics = new ArrayList<TriggerStatistics>();
		List<TriggerStatistics> minuteStatistics = new ArrayList<TriggerStatistics>();
		if(statistics != null && statistics.size() != 0){
			for(TriggerStatistics triggerStatistics : statistics){
				if("day".equals(triggerStatistics.getPeriod())){
					dayStatistics.add(triggerStatistics);
				}else if("hour".equals(triggerStatistics.getPeriod())){
					hourStatistics.add(triggerStatistics);
				}else if("minute".equals(triggerStatistics.getPeriod())){
					minuteStatistics.add(triggerStatistics);
				}
			}
		}
		Map<String, Object> dayData = new HashMap<String, Object>();
		dayData.put("legendData1", legendData1);
		dayData.put("xAxisData", dayXAxisData2);
		dayData.put("triggerStatistics", dayStatisticsMap);
		dayData = dataHandle(dayXAxisData, dayStatistics, dayData);
		Map<String, Object> hourData = new HashMap<String, Object>();
		hourData.put("legendData1", legendData1);
		hourData.put("xAxisData", hourXAxisData2);
		hourData.put("triggerStatistics", hourStatisticsMap);
		hourData = dataHandle(hourXAxisData, hourStatistics, hourData);
		Map<String, Object> minuteData = new HashMap<String, Object>();
		minuteData.put("legendData1", legendData1);
		minuteData.put("xAxisData", minuteXAxisData2);
		minuteData.put("triggerStatistics", minuteStatisticsMap);
		minuteData = dataHandle(minuteXAxisData, minuteStatistics, minuteData);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("day", dayData);
		returnMap.put("hour", hourData);
		returnMap.put("minute", minuteData);
		return returnMap;
	}
	/**
	 * 触发记录当前时间段增长情况
	 * @return
	 */
	private Map<String, Object> getTriggerIncrease(TriggerStatistics statisticsNew, TriggerStatistics statisticsOld) {
		Map<String, Object> statisticsMap = new HashMap<String, Object>();
//		statisticsMap.put("processedAdd", statisticsNew.getProcessed() - statisticsOld.getProcessed());
//		statisticsMap.put("processingAdd", statisticsNew.getProcessing() - statisticsOld.getProcessing());
//		statisticsMap.put("ignoredAdd", statisticsNew.getIgnored() - statisticsOld.getIgnored());
//		statisticsMap.put("unprocessedAdd", statisticsNew.getUnprocessed() - statisticsOld.getUnprocessed());
//		statisticsMap.put("totalAdd", statisticsNew.getTotal() - statisticsOld.getTotal());
		statisticsMap.put("processedAdd", statisticsNew.getProcessed());
		statisticsMap.put("processingAdd", statisticsNew.getProcessing());
		statisticsMap.put("ignoredAdd", statisticsNew.getIgnored());
		statisticsMap.put("unprocessedAdd", statisticsNew.getUnprocessed());
		statisticsMap.put("totalAdd", statisticsNew.getTotal());

		if(statisticsNew.getProcessed() == 0 && statisticsOld.getProcessed() == 0){
			statisticsMap.put("processedPre", 0);
		}else if(statisticsOld.getProcessed() == 0){
			statisticsMap.put("processedPre", 100);
		}else{
			statisticsMap.put("processedPre", (statisticsNew.getProcessed() - statisticsOld.getProcessed())*100 / statisticsOld.getProcessed());
		}
		
		if(statisticsNew.getProcessing() == 0 && statisticsOld.getProcessing() == 0){
			statisticsMap.put("processingPre", 0);
		}else if(statisticsOld.getProcessing() == 0){
			statisticsMap.put("processingPre", 100);
		}else{
			statisticsMap.put("processingPre", (statisticsNew.getProcessing() - statisticsOld.getProcessing())*100 / statisticsOld.getProcessing());
		}
		
		if(statisticsNew.getIgnored() == 0 && statisticsOld.getIgnored() == 0){
			statisticsMap.put("ignoredPre", 0);
		}else if(statisticsOld.getIgnored() == 0){
			statisticsMap.put("ignoredPre", 100);
		}else{
			statisticsMap.put("ignoredPre", (statisticsNew.getIgnored() - statisticsOld.getIgnored())*100 / statisticsOld.getIgnored());
		}
		
		if(statisticsNew.getUnprocessed() == 0 && statisticsOld.getUnprocessed() == 0){
			statisticsMap.put("unprocessedPre", 0);
		}else if(statisticsOld.getUnprocessed() == 0){
			statisticsMap.put("unprocessedPre", 100);
		}else{
			statisticsMap.put("unprocessedPre", (statisticsNew.getUnprocessed() - statisticsOld.getUnprocessed())*100 / statisticsOld.getUnprocessed());
		}
		
		if(statisticsNew.getTotal() == 0 && statisticsOld.getTotal() == 0){
			statisticsMap.put("totalPre", 0);
		}else if(statisticsOld.getTotal() == 0){
			statisticsMap.put("totalPre", 100);
		}else{
			statisticsMap.put("totalPre", (statisticsNew.getTotal() - statisticsOld.getTotal())*100 / statisticsOld.getTotal());
		}
		
		return statisticsMap;
	}
	/**
	 * 数据封装
	 * @param data
	 * @return
	 */
	private Map<String, Object> dataHandle(List<String> xAxisData, List<TriggerStatistics> triggerStatistics, Map<String, Object> data) {
		//横轴长度
		int xSize = xAxisData.size();
		//总数
		int[] total = new int[xSize];
		//未处理数
		int[] unprocesseds = new int[xSize];
		//已处理数
//		int[] processeds = new int[xSize];
//		//忽略数
//		int[] ignoreds = new int[xSize];
//		//处理中数
//		int[] processings = new int[xSize];

//		System.out.println(triggerStatistics);
		for(TriggerStatistics statistic : triggerStatistics){
			String date = statistic.getTriggerDate();
			int index = xAxisData.indexOf(date);
			total[index] = statistic.getTotal();
			unprocesseds[index] = statistic.getUnprocessed();
		}
		
		//统计
		data.put("unprocesseds", unprocesseds);
		data.put("total", total);

		return data;
	}

	/**
	 * 查询当前30触发记录各状态数量变动情况
	 * @param map
	 * @return
	 */
	public TriggerStatistics findHistoryStatusCount(Map<String, Object> map) {
		return dao.findHistoryStatusCount(map);
	}

	/**
	 * 导出详情页各功能点详情
	 * @param triggerRecord
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	public void detailExport(TriggerRecord triggerRecord, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    //生成一个excel文件  
        WritableWorkbook wwb = null;      
        String fileName = "triggerRecordDetail-" + DateTime.now().toString("yyyyMMddHHmmss") + ".xlsx";
       //首先要使用Workbook类的工厂方法创建一个可写入的工作薄(Workbook)对象      
       wwb = Workbook.createWorkbook(new File(fileName));  
       
       triggerRecord = dao.get(triggerRecord);
       JSONObject triggerRecordJson = JSONObject.fromObject(triggerRecord.getTriggerState());
       triggerRecord = replaceStrVar(triggerRecord);
       List<Func> funcs = funcDao.findFuncByEplId(triggerRecord.getEplId());
       String querySql = null;
       
       if(funcs != null && funcs.size() != 0){
    	   for(int i=0; i<funcs.size(); i++){
				triggerRecordJson.put("schemaId", funcs.get(i).getSchemaId());
				querySql = replaceStrVarByJSON(triggerRecordJson, funcs.get(i).getFuncsql());
				List<FuncField> fields = funcs.get(i).getFields();
				List<JSONObject> details = queryDetailPage(fields, querySql);
				if(wwb!=null){
	                   //创建一个可写入的工作表      
	                   //Workbook的createSheet方法有两个参数，第一个是工作表的名称，第二个是工作表在工作薄中的位置      
	                   WritableSheet ws = wwb.createSheet(funcs.get(i).getFuncname()+"列表", i);  
	                   //添加标题
	                   for(int j=0; j<fields.size(); j++) {
							FuncField funcField = funcs.get(i).getFields().get(j);
							
							WritableFont wfc = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);   
							WritableCellFormat wcfFC = new WritableCellFormat(wfc);   
							wcfFC.setBackground(Colour.GRAY_25);  
							Label label = new Label(j, 0, funcField.getFieldDescribe(), wcfFC);  
							ws.setColumnView(j, 20); //设置列宽  
							ws.addCell(label);  //添加标题  
						}
	                   //下面开始添加单元格      
	                   String str="";  
						for(int j = 0; j < details.size(); j++) {
							for(int k=0; k<fields.size(); k++) {
								FuncField funcField = fields.get(k);
								if(details.get(j).containsKey(funcField.getFieldName())) {
									if("timestamp".equals(funcField.getFieldName())) {
										str = DateUtils.getDateByTimestamp(details.get(j).getLong(funcField.getFieldName()), "yyyy-MM-dd HH:mm:ss");
									} else {
										str = details.get(j).getString(funcField.getFieldName());
									}
									//这里需要注意的是: 在Excel中，第一个参数表示列，第二个表示行      
		                            Label labelC = new Label(k, j+1, str);   
		                            //将生成的单元格添加到工作表中      
		                            ws.addCell(labelC);    
								} 
							}
						}
				}
				
    	   }
    	   
       }
       //Excel操作完毕之后，关闭所有的操作资源   
        try {      
            //从内存中写入文件中      
            wwb.write();      
            //关闭资源，释放内存      
            wwb.close();      
        } catch (Exception e) {    
             e.printStackTrace();  
        }
         
        //把生成的文件下载  
       File file = new File(fileName);
       if(!file.exists()) throw new Exception("文件不存在!");  
       FileInputStream fileInputStream = new FileInputStream(file);  
       BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);  
       OutputStream outputStream = response.getOutputStream();  
       BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);  
       response.setContentType("application/x-download");  
       response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));  
       int bytesRead = 0;
       byte[] buffer = new byte[8192];  
       while ((bytesRead = bufferedInputStream.read(buffer, 0, 8192)) != -1) {  
           bufferedOutputStream.write(buffer, 0, bytesRead);  
       }  
       bufferedOutputStream.flush();  
       fileInputStream.close();  
       bufferedInputStream.close();  
       outputStream.close();  
       bufferedOutputStream.close();  		
	}

	/**
	 * 获取EPL列表
	 * @param triggerRecord
	 * @return
	 */
	public List<TriggerRecord> findTriggerEpl(TriggerRecord triggerRecord) {
		return dao.findTriggerEpl(triggerRecord);
	}

	/**
	 * 批量处理触发事件
	 * @param triggerRecord
	 */
	@Transactional(readOnly = false)
	public void statisticSave(TriggerRecord triggerRecord) {
		dao.updateByYmtCode(triggerRecord);
	}
}