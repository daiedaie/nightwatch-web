package com.jzsec.common.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.springframework.web.socket.WebSocketSession;


/**
 * 存放所有的缓存变量
 * @author 劉 焱
 * @date 2016-7-29
 * @tags
 */
public class MyCache {
	/**
	 * 触发记录当天各状态数量变更
	 */
	public static Map<String, Object> triggerUndeal = new ConcurrentHashMap<String, Object>();
	/**
	 * 触发记录前三十天各状态数量变动
	 */
	public static Map<String, Object> triggerHistoryStatus = new ConcurrentHashMap<String, Object>();
	/**
     *  存放用户与WebSocketSession对应信息, key为userName value为WebSocketSession
     */
    public static Map<String,WebSocketSession> userWebSocketSession = new ConcurrentHashMap<String,WebSocketSession>(50);
	/**
	 * 存放用户登录的HttpSession容器 其key为userId;
	 */
	public static Map<String, HttpSession> userHttpSessionMap = new ConcurrentHashMap<String, HttpSession>(50);

//	public static String ZOOKEEPER_NODE = LoadConfig.getZookeeperNode();
//
//	public static String ZOOKEEPER_RISK_PATH = LoadConfig.getZookeeperRiskPath();
//
//	public static String ZOOKEEPER_RISK_CHILD_PATH = LoadConfig.getZookeeperRiskChildPath();
//
//	public static String ZOOKEEPER_ALARM_PATH = LoadConfig.getZookeeperAlarmPath();	
//
//	public static String ZOOKEEPER_ALARM_CHILD_PATH = LoadConfig.getZookeeperAlarmChildPath();	
//
//	public static String ELASTICSEARCH_NODE_URL = LoadConfig.getElasticsearchNodeUrl();
//
//	public static long timerWebsocketInterval  = LoadConfig.getTimerWebsocketInterval();
//
//	public static long refreshConfigInterval = LoadConfig.getRefreshConfigInterval();
}
