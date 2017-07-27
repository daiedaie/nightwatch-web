package com.jzsec.modules.common.servie;


import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPException;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.jzsec.common.azkaban.Azkaban;
import com.jzsec.common.config.LoadConfig;
import com.jzsec.common.utils.StringUtil;
import com.jzsec.common.utils.ZipUtil;
import com.jzsec.modules.common.entity.PushAlarmEpl;
import com.jzsec.modules.common.entity.PushEpl;


/**
 * 公共方法实现层
 * @author 劉 焱
 * @date 2016-8-1
 * @tags
 */
@Service
public class CommonService{
	/**
	 * 向zookeeper节点中推送规则变更信息
	 * @throws Exception 
	 */
	public void pushZookeeper(String json, String path, String childPath) throws Exception {
		CuratorFramework curator = CuratorFrameworkFactory.newClient(LoadConfig.getZookeeperNode(), LoadConfig.getZookeeperSessionTimeout(), 
				LoadConfig.getZookeeperConnectionTimeout(), new RetryNTimes(LoadConfig.getZookeeperRetryTimes(), LoadConfig.getZookeeperSleepBetweenRetries()));
		curator.start();
        //检测父节点是否存在
        if(curator.checkExists().forPath(path) == null ){
            //父节点不存在：先创建父节点再创建子节点
            curator.create().forPath(path);
            curator.create().forPath(childPath, json.getBytes());
        }else{
            //父节点存在：创建子节点
            if(curator.checkExists().forPath(childPath) == null ){
                curator.create().forPath(childPath, json.getBytes());
            } else {
                curator.setData().forPath(childPath, json.getBytes());
            }
        }
	}
	/**
	 * 报警规则推送信息处理
	 * @throws Exception 
	 */
	public void alarmEplPushData(PushAlarmEpl pushAlarmEpl, String action, String path, String childPath) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
    	Map<String, Object> alarmMap = new HashMap<String, Object>();
    	alarmMap.put("alarmTemplate", pushAlarmEpl.getAlarmTemplate());
    	alarmMap.put("alarmType", pushAlarmEpl.getAlarmType());
    	alarmMap.put("email", pushAlarmEpl.getEmail());
    	alarmMap.put("id", pushAlarmEpl.getId());
    	alarmMap.put("phone", pushAlarmEpl.getPhone());

    	map.put("alarm", alarmMap);
    	map.put("type", "epl");
    	map.put("eplName", pushAlarmEpl.getEplName());
    	map.put("eplSql", getEplSql(pushAlarmEpl.getThresholds(), pushAlarmEpl.getEpl()));
    	map.put("id", pushAlarmEpl.getId());
    	map.put("action", action);
    	
    	String json = mapper.writeValueAsString(map);
		pushZookeeper(json, path, childPath);
	}
	/**
	 * 风控规则推送信息处理
	 * @throws Exception 
	 */
	public void rtcEplPushData(PushEpl pushEpl, String action, String path, String childPath) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("type", "epl");
    	map.put("isAlarm", pushEpl.getIsAlarm());
    	map.put("eplName", pushEpl.getEplName());
    	map.put("eplSql", getEplSql(pushEpl.getThresholds(), pushEpl.getEpl()));
    	map.put("id", pushEpl.getId());
    	map.put("action", action);
		map.put("allSchema", pushEpl.getAllSchema());
    	
    	String json = mapper.writeValueAsString(map);
		pushZookeeper(json, path, childPath);
	}
	/**
	 * 推送信息处理[只包含id和action]
	 * @throws Exception 
	 */
	public void rtcPushData(Integer id, String action, String path, String childPath) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("id", id);
    	map.put("action", action);
    	
    	String json = mapper.writeValueAsString(map);
		pushZookeeper(json, path, childPath);
	}
	/**
	 * 遍历规则阀值并替换
	 * @param thresholds
	 * @param alarmEplSql
	 * @return
	 */
	public String getEplSql(String thresholds, String alarmEplSql){
    	if(thresholds != null && !StringUtils.isEmpty(thresholds)) {
    		String[] dts = thresholds.split(",");
    		Map<String, Object> paramMap = new HashMap<String, Object>();
    		for(String threshold : dts) {
    			String[] fields = threshold.split("=");
    			paramMap.put(fields[0], fields[1]);
    		}
    		alarmEplSql = replaceStrVar(paramMap, alarmEplSql);
    	}
    	return alarmEplSql;
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
    
    /**
     * 验证EPL语句是否合规
     * @param esperEpl
     * @return
     */
	public boolean isEplNorm(String esperEpl){
		if(StringUtil.isEmpty(esperEpl)){
			return false;
		}
		
		boolean flag = true;
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		try {
//			System.out.println(esperEpl);

			String esper = esperEpl.replaceAll("\\$\\{[a-zA-Z0-9]*\\}", "1");
			String[] eplSqls = esper.trim().split(";");
			for(int i = 0; i < eplSqls.length; i++) {

				if(!StringUtils.isBlank(eplSqls[i])) {
					admin.compileEPL(eplSqls[i].trim());
				}

			}
		} catch (EPException e) {
			flag = false;
			System.out.println(esperEpl);
			e.printStackTrace();
		}finally{
			admin.destroyAllStatements();
			epService.destroy();
		}
		
		return flag;
	}
	/**
	 * 验证esper语句能否正常加载
	 * @param scriptCode
	 * @return
	 */
	public boolean isEsperNorm(String esperEpl) {
		if(StringUtil.isEmpty(esperEpl)){
			return false;
		}
		
		boolean flag = true;
		EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
		EPAdministrator admin = epService.getEPAdministrator();
		try {
			admin.compileEPL(esperEpl);
		} catch (EPException e) {
			flag = false;
			System.out.println(esperEpl);
			e.printStackTrace();
		}finally{
			admin.destroyAllStatements();
			epService.destroy();
		}
		
		return flag;
	}
	
	/**
	 * 将事后规则打包并上传zip包
	 * @throws Exception 
	 */
	public String zipFile(Azkaban azkaban, Integer eplId, boolean isRemove) throws Exception{
		String zipName = "fk";
		String path = azkaban.filePath+File.separator+zipName;
		File pathTest = new File(path);
		if(! pathTest.exists()){
			pathTest.mkdirs();
		}
		
		File file = new File(path+File.separator+eplId+".job");
		String destPath = path+".zip";
		File dest = new File(destPath);
		
		if(isRemove){
			file.delete();
			dest.delete();
		} else {
			PrintStream ps = null;
			try {
				ps = new PrintStream(new FileOutputStream(file));
				String words = azkaban.fileContent + " " +eplId;
				ps.println(words);// 往文件里写入字符串
				ps.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				if(ps != null){
					ps.close();
				}
			}
		}
		
		File originFile = new File(path);
		if(originFile.isDirectory() && originFile.listFiles().length == 0){
			originFile = new File(azkaban.filePath);
		}
		ZipUtil.zip(originFile, dest);
		
        //上传zip包
        String sessionId = azkaban.getSessionId();
        JSONObject jsonObject = azkaban.uploadZip(sessionId, destPath);
        if(jsonObject == null || jsonObject.isEmpty()){
        	return "error:上传ZIP包返回空值!";
        } else if (jsonObject.containsKey("error")){
        	return "error:"+jsonObject.getString("error");
        }
        
		return sessionId;
	}
	
	/**
	 * 上传azkaban并启动调度
	 * @param eplId
	 * @return 
	 */
	public String uploadZipFile(Integer eplId, String cronExpression){
		Azkaban azkaban = new Azkaban();
		try {
            String sessionId = zipFile(azkaban, eplId, false);
            String[] messages = sessionId.split(":");
            if(messages.length>1){
            	return messages[1];
            }
            //若有调度则移除, 使用新的调度
            JSONObject jsonObject1 = azkaban.fetchFlowAPro(sessionId);
            azkaban.unscheduleFlowByFlow(sessionId, jsonObject1.getString("projectId"), eplId+"");
            JSONObject jsonObject2 = azkaban.scheduleByCronEXEaFlow(sessionId, eplId+"", cronExpression);
            if(jsonObject2 == null){
            	return "执行调度时返回空值!";
            } else if (jsonObject2.containsKey("error")){
            	return jsonObject2.getString("error");
            } else if ("error".equals(jsonObject2.getString("status"))){
            	return jsonObject2.getString("message");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "交互异常 - " + e.getMessage();
        }
		
		return null;
	}
	/**
	 * azkaban删除调度
	 * @param eplId
	 * @return
	 */
	public String removeSchedule(Integer eplId) {
		Azkaban azkaban = new Azkaban();
		try {
            String sessionId = zipFile(azkaban, eplId, true);
            String[] messages = sessionId.split(":");
            if(messages.length>1){
            	return messages[1];
            }
            
            JSONObject jsonObject1 = azkaban.fetchFlowAPro(sessionId);
			JSONObject jsonObject = azkaban.unscheduleFlowByFlow(sessionId, jsonObject1.getString("projectId"), eplId+"");
			if(jsonObject == null){
				return "azkaban返回空值!";
			} else if (jsonObject.containsKey("error")){
				return jsonObject.getString("error");
			} else if ("error".equals(jsonObject.getString("status"))){
            	return jsonObject.getString("message");
            } 
		} catch (Exception e) {
			e.printStackTrace();
			return "交互异常 - " + e.getMessage();
		}
		return null;
	}
}
