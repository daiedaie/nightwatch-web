package com.jzsec.common.config;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 加载配置文件
 * @author 劉 焱
 * @date 2016-8-9
 * @tags
 */
public class LoadConfig {
    static private String zookeeperNode;
    static private String zookeeperRiskPath;
    static private String zookeeperRiskChildPath;
    static private String zookeeperSchemaChildPath;
    static private String zookeeperScriptChildPath;
    static private String zookeeperAlarmPath;
    static private String zookeeperAlarmChildPath;
    static private int zookeeperSessionTimeout;
    static private int zookeeperConnectionTimeout;
    static private int zookeeperRetryTimes;
    static private int zookeeperSleepBetweenRetries;
    static private String elasticsearchNodeUrl;
    static private long timerWebsocketInterval;
    static private long refreshConfigInterval;
    
    public static void load(){
		Resource resource = new ClassPathResource("config.properties");
		try {
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
//			System.out.println("config文件加载中......");
			zookeeperNode = props.getProperty("zookeeper_node").toString().trim();
			zookeeperRiskPath = props.getProperty("zookeeper_risk_path").toString().trim();
			zookeeperRiskChildPath = props.getProperty("zookeeper_risk_child_path").toString().trim();
			zookeeperSchemaChildPath = props.getProperty("zookeeper_schema_child_path").toString().trim();
			zookeeperScriptChildPath = props.getProperty("zookeeper_script_child_path").toString().trim();
			zookeeperAlarmPath = props.getProperty("zookeeper_alarm_path").toString().trim();
			zookeeperAlarmChildPath = props.getProperty("zookeeper_alarm_child_path").toString().trim();
			zookeeperSessionTimeout = Integer.parseInt(props.getProperty("zookeeper_session_timeout").toString().trim());
			zookeeperConnectionTimeout = Integer.parseInt(props.getProperty("zookeeper_connection_timeout").toString().trim());
			zookeeperRetryTimes = Integer.parseInt(props.getProperty("zookeeper_retry_times").toString().trim());
			zookeeperSleepBetweenRetries = Integer.parseInt(props.getProperty("zookeeper_sleep_between_retries").toString().trim());
			elasticsearchNodeUrl = props.getProperty("elasticsearch_node_url").toString().trim();
			timerWebsocketInterval = Long.parseLong(props.getProperty("timer_websocket_interval").toString().trim());
			refreshConfigInterval = Long.parseLong(props.getProperty("refresh_config_interval").toString().trim());
//			System.out.println("config文件加载成功!");
		} catch (IOException e) {
			System.out.println("config文件加载出错!");
			e.printStackTrace();
		}
	}
	/** 
	 * Zookeeper节点地址 
	 */
    public static String getZookeeperNode() {
		return zookeeperNode;
	}
	/** 
	 * Zookeeper风控规则路径 
	 */
	public static String getZookeeperRiskPath() {
		return zookeeperRiskPath;
	}
	/** 
	 * Zookeeper风控规则子路径 
	 */
	public static String getZookeeperRiskChildPath() {
		return zookeeperRiskChildPath;
	}
	/** 
	 * Zookeeper报警规则子路径 
	 */
	public static String getZookeeperAlarmChildPath() {
		return zookeeperAlarmChildPath;
	}
	/** 
	 * Zookeeper报警规则路径 
	 */
	public static String getZookeeperAlarmPath() {
		return zookeeperAlarmPath;
	}
	/**
	 * websocket推送任务轮询数据库时间间隔
	 */
	public static long getTimerWebsocketInterval() {
		return timerWebsocketInterval;
	}
	/**
	 * zookeeper客户端session超时时间
	 */
	public static int getZookeeperSessionTimeout() {
		return zookeeperSessionTimeout;
	}
	/**
	 * zookeeper客户端获取连接超时时间
	 * @return
	 */
	public static int getZookeeperConnectionTimeout() {
		return zookeeperConnectionTimeout;
	}
	/**
	 * zookeeper客户端连接超时重试次数
	 * @return
	 */
	public static int getZookeeperRetryTimes() {
		return zookeeperRetryTimes;
	}
	/**
	 * zookeeper超时连接重试期间连接间隔时间
	 * @return
	 */
	public static int getZookeeperSleepBetweenRetries() {
		return zookeeperSleepBetweenRetries;
	}
	/** 
	 * elasticsearch节点
	 */
	public static String getElasticsearchNodeUrl() {
		return elasticsearchNodeUrl;
	}
	/**
	 * 定时刷新config配置文件时间间隔
	 */
	public static long getRefreshConfigInterval() {
		return refreshConfigInterval;
	}
	/** 
	 * Zookeeper数据源格式子路径 
	 */
	public static String getZookeeperSchemaChildPath() {
		return zookeeperSchemaChildPath;
	}
	/** 
	 * Zookeeper函数脚本子路径 
	 */
	public static String getZookeeperScriptChildPath() {
		return zookeeperScriptChildPath;
	}

}