package com.jzsec.common.websocket;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.codehaus.jackson.map.ObjectMapper;

import com.jzsec.common.config.LoadConfig;
import com.jzsec.common.config.MyCache;
import com.jzsec.common.config.MyConst;
import com.jzsec.common.utils.DateUtils;
import com.jzsec.common.utils.NumberUtil;
import com.jzsec.common.utils.SpringContextHolder;
import com.jzsec.modules.epl.entity.Epl;
import com.jzsec.modules.eplCategory.service.EplCategoryService;
import com.jzsec.modules.trigger.entity.TriggerStatistics;
import com.jzsec.modules.trigger.service.TriggerRecordService;


/**
 * 定时任务
 * @author 劉 焱
 * @date 2016-7-29
 * @tags
 */
public class MyTimerTask {
	/**
	 * 定时扫描数据库告警信息数量
	 */
	public void checkAlarmRecordCounts() {

		new Timer().schedule(new TimerTask(){ 
        //调用schedule方法执行任务
            public void run() {
//            	long l1 = System.currentTimeMillis();
//            	System.out.println(l1);
            	Date date = new Date();
            	String nowDate = DateUtils.formatDate(date, MyConst.YYYY_MM_DD);
            	if(MyCache.triggerHistoryStatus.get("nowDate") == null || ! nowDate.equals((String)MyCache.triggerHistoryStatus.get("nowDate"))){
            		MyCache.triggerUndeal.put("total", 0);
            		MyCache.triggerUndeal.put("unprocessed", 0);
            		MyCache.triggerUndeal.put("processed", 0);
            		MyCache.triggerUndeal.put("processing", 0);
            		MyCache.triggerUndeal.put("ignored", 0);
            		MyCache.triggerUndeal.put("triggers", "");
            		MyCache.triggerUndeal.put("list", "");
            		MyCache.triggerUndeal.put("currentStatistics", "");
            		
            		MyCache.triggerHistoryStatus.put("flag", true);
            		MyCache.triggerHistoryStatus.put("nowDate", nowDate);
            		MyCache.triggerHistoryStatus.put("total", 0);
            		MyCache.triggerHistoryStatus.put("unprocessed", 0);
            		MyCache.triggerHistoryStatus.put("processed", 0);
            		MyCache.triggerHistoryStatus.put("processing", 0);
            		MyCache.triggerHistoryStatus.put("ignored", 0);
            	}else{
            		MyCache.triggerHistoryStatus.put("flag", false);
            	}
            	
        		TriggerRecordService recordService =  SpringContextHolder.getBean("triggerRecordService");
        		EplCategoryService categoryService =  SpringContextHolder.getBean("eplCategoryService");
        		
        		// 查询当天未读消息
        		Map<String, Object> map = new HashMap<String, Object>();
        		map.put("startDate", nowDate+" 00:00:00");
        		map.put("endDate", DateUtils.formatDate(date, MyConst.YYYY_MM_DD_HH_MM_SS));
//        		long l21 = System.currentTimeMillis();
//        		System.out.println(l21+"==================l21: "+(l21 - l1));
        		TriggerStatistics historyStatistics = recordService.findHistoryStatusCount(map);
            	
            	if(historyStatistics.getUnprocessed() != (Integer)MyCache.triggerHistoryStatus.get("unprocessed") || 
            			historyStatistics.getTotal() != (Integer)MyCache.triggerHistoryStatus.get("total") ||
            			historyStatistics.getProcessed() != (Integer)MyCache.triggerHistoryStatus.get("processed") || 
            			historyStatistics.getProcessing() != (Integer)MyCache.triggerHistoryStatus.get("processing") || 
            			historyStatistics.getIgnored() != (Integer)MyCache.triggerHistoryStatus.get("ignored") || 
            			(Boolean)MyCache.triggerHistoryStatus.get("flag")){
            		
            		MyCache.triggerHistoryStatus.put("total", historyStatistics.getTotal());
            		MyCache.triggerHistoryStatus.put("unprocessed", historyStatistics.getUnprocessed());
            		MyCache.triggerHistoryStatus.put("processed", historyStatistics.getProcessed());
            		MyCache.triggerHistoryStatus.put("processing", historyStatistics.getProcessing());
            		MyCache.triggerHistoryStatus.put("ignored", historyStatistics.getIgnored());
            		
            		for(String userId : MyCache.userWebSocketSession.keySet()){
            			List<Epl> eplList = categoryService.getEplTypeRoleList(userId);
            			map.put("eplList", eplList);
            			
            			//右侧小铃铛
            			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
            			list = recordService.findUndealTypes(map);
            			MyCache.triggerUndeal.put("list", list);
                		// 查询当天未读消息
                		TriggerStatistics triggerStatistics = recordService.findUndealAndTotalCount(map);
                		MyCache.triggerUndeal.put("total", triggerStatistics.getTotal());
                		MyCache.triggerUndeal.put("unprocessed", triggerStatistics.getUnprocessed());
                		MyCache.triggerUndeal.put("processed", triggerStatistics.getProcessed());
                		MyCache.triggerUndeal.put("processing", triggerStatistics.getProcessing());
                		MyCache.triggerUndeal.put("ignored", triggerStatistics.getIgnored());
                		//当日各类型事件数量及占比情况
                		MyCache.triggerUndeal.put("currentStatistics", getCurrentStatistics(triggerStatistics));
                		//获取日/时/分图形数据
                    	Map<String, Object> maps;
    					try {
    						maps = recordService.getStatisticsByPeriod(map);
    						MyCache.triggerUndeal.put("day", maps.get("day"));
    						MyCache.triggerUndeal.put("hour", maps.get("hour"));
    						MyCache.triggerUndeal.put("minute", maps.get("minute"));
    					} catch (ParseException e1) {
    						e1.printStackTrace();
    					}
    					//最新事件
            			List<Map<String, Object>> triggers = null;
    					try {
    						triggers = recordService.findLatest(map);
    					} catch (Exception e1) {
    						e1.printStackTrace();
    					}
            			MyCache.triggerUndeal.put("triggers", triggers);
            			
            			//转换为json格式便于传输
                		ObjectMapper mapper = new ObjectMapper();
                		String json = "";
                		try {
                			json = mapper.writeValueAsString(MyCache.triggerUndeal);
    					} catch (IOException e) {
    						e.printStackTrace();
    					}
                		MyCache.triggerUndeal.put("list", json);
            			
                		//数据发送
            			MyWebSocketSendMsg socketSendMsg = new MyWebSocketSendMsg();
                		socketSendMsg.sendTextMessageToUser(userId, (String) MyCache.triggerUndeal.get("list"));
            		}
            		
//            		// 查询当天未读消息
////            		long l22 = System.currentTimeMillis();
////            		System.out.println(l22+"==================l22: "+(l22 - l21));
//            		TriggerStatistics triggerStatistics = recordService.findUndealAndTotalCount(map);
//            		MyCache.triggerUndeal.put("total", triggerStatistics.getTotal());
//            		MyCache.triggerUndeal.put("unprocessed", triggerStatistics.getUnprocessed());
//            		MyCache.triggerUndeal.put("processed", triggerStatistics.getProcessed());
//            		MyCache.triggerUndeal.put("processing", triggerStatistics.getProcessing());
//            		MyCache.triggerUndeal.put("ignored", triggerStatistics.getIgnored());
//            		
////            		long l3 = System.currentTimeMillis();
////            		System.out.println(l3+"==================l3: "+(l3 - l22));
//            		//右侧小铃铛
//            		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
//            		list = recordService.findUndealTypes(map);
//            		MyCache.triggerUndeal.put("list", list);
//            		
////                	long l4 = System.currentTimeMillis();
////                	System.out.println(l4+"==================l4: "+(l4 - l3));
//            		
//            		//当日各类型事件数量及占比情况
//            		MyCache.triggerUndeal.put("currentStatistics", getCurrentStatistics(triggerStatistics));
////                	long l5 = System.currentTimeMillis();
////                	System.out.println(l5+"==================l5: "+(l5 - l4));
//            		
//            		//获取日/时/分图形数据
//                	Map<String, Object> maps;
//					try {
//						maps = recordService.getStatisticsByPeriod(map);
//						MyCache.triggerUndeal.put("day", maps.get("day"));
//						MyCache.triggerUndeal.put("hour", maps.get("hour"));
//						MyCache.triggerUndeal.put("minute", maps.get("minute"));
//					} catch (ParseException e1) {
//						e1.printStackTrace();
//					}
//            		
////                	long l6 = System.currentTimeMillis();
////                	System.out.println(l6+"==================l6: "+(l6 - l5));
//                	
//            		//最新事件
//        			List<Map<String, Object>> triggers = null;
//					try {
//						triggers = recordService.findLatest(map);
//					} catch (Exception e1) {
//						e1.printStackTrace();
//					}
//        			MyCache.triggerUndeal.put("triggers", triggers);
//            		
////                	long l7 = System.currentTimeMillis();
////                	System.out.println(l7+"==================l7: "+(l7 - l6));
//        			
//            		//转换为json格式便于传输
//            		ObjectMapper mapper = new ObjectMapper();
//            		String json = "";
//            		try {
//            			json = mapper.writeValueAsString(MyCache.triggerUndeal);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//            		MyCache.triggerUndeal.put("list", json);
////                	long l8 = System.currentTimeMillis();
////                	System.out.println(l8+"==================l8: "+(l8 - l7));
//            		System.out.println(MyCache.triggerUndeal);
//            		System.out.println((String) MyCache.triggerUndeal.get("list"));
//            		//发送给所有人
//            		MyWebSocketSendMsg socketSendMsg = new MyWebSocketSendMsg();
//            		socketSendMsg.sendMessageToUsers((String) MyCache.triggerUndeal.get("list"));
//                	long l9 = System.currentTimeMillis();
//                	System.out.println(l9+"==================总计:"+(l9 - l1));
            	}

            }

        } , 10000 , LoadConfig.getTimerWebsocketInterval());//过10秒后执行，之后每隔指定毫秒执行一次
	}
	
	/**
	 * 当日各类型事件数量及占比情况
	 * @param triggerStatistics
	 * @return
	 */
	public static Map<String, Object> getCurrentStatistics(TriggerStatistics triggerStatistics) {
		Map<String, Object> currentStatistics = new HashMap<String, Object>();
		currentStatistics.put("unprocessed", triggerStatistics.getUnprocessed());
		currentStatistics.put("unprocessedPro", triggerStatistics.getTotal() == 0 ? "0%" : 
			NumberUtil.doubleCutToStrRound(Double.parseDouble(triggerStatistics.getUnprocessed()*100/triggerStatistics.getTotal() + ""), 2) + "%");
		currentStatistics.put("processed", triggerStatistics.getProcessed());
		currentStatistics.put("processedPro", triggerStatistics.getTotal() == 0 ? "0%" : 
			NumberUtil.doubleCutToStrRound(Double.parseDouble(triggerStatistics.getProcessed()*100/triggerStatistics.getTotal() + ""), 2) + "%");
		currentStatistics.put("processing", triggerStatistics.getProcessing());
		currentStatistics.put("processingPro", triggerStatistics.getTotal() == 0 ? "0%" : 
			NumberUtil.doubleCutToStrRound(Double.parseDouble(triggerStatistics.getProcessing()*100/triggerStatistics.getTotal() + ""), 2) + "%");
		currentStatistics.put("ignored", triggerStatistics.getIgnored());
		currentStatistics.put("ignoredPro", triggerStatistics.getTotal() == 0 ? "0%" : 
			NumberUtil.doubleCutToStrRound(Double.parseDouble(triggerStatistics.getIgnored()*100/triggerStatistics.getTotal() + ""), 2) + "%");
		return currentStatistics;
	}
	
	
	/**
	 * 定时刷新配置文件
	 */
	public void refreshConfiguration() {

		new Timer().schedule(new TimerTask(){ 
        //调用schedule方法执行任务
            public void run() {
            	LoadConfig.load();
            }

        } , 100000 , LoadConfig.getRefreshConfigInterval());//过10秒后执行，之后每隔指定毫秒执行一次
	}
}
