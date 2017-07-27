/**
 * 
 */
package com.jzsec.common.azkaban;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.jzsec.common.utils.SSLUtil;

/**
 * azkaban相关操作
 * @author 劉 焱
 * @date 2017-6-5
 */
public class Azkaban {
    private static String url;
    public static String filePath;
    public static String fileContent;
    private static String azkabanUser;  
    private static String azkabanPassword;  
    private static String projectName;  
    private static RestTemplate restTemplate;
    
    static {  
    	SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    	requestFactory.setConnectTimeout(2000);
    	requestFactory.setReadTimeout(2000);
    	restTemplate = new RestTemplate(requestFactory);
    	
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("azkaban.properties");  
        Properties p = new Properties();  
        try {  
                p.load(is);  
                url = p.getProperty("url");  
                filePath = p.getProperty("filePath"); 
                fileContent = p.getProperty("fileContent"); 
                azkabanUser = p.getProperty("azkabanUser");  
                azkabanPassword = p.getProperty("azkabanPassword");  
                projectName = p.getProperty("projectName");  
                SSLUtil.turnOffSslChecking();
        } catch (Exception e) {  
                e.printStackTrace();  
        }  
    }  
    
    /**
     * 登录测试 登录调度系统
     * 
     * @throws Exception
     */
    public JSONObject login() throws Exception {
        HttpHeaders hs = new HttpHeaders();
        hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        hs.add("X-Requested-With", "XMLHttpRequest");
        LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<String, String>();
        linkedMultiValueMap.add("action", "login");
        linkedMultiValueMap.add("username", azkabanUser);
        linkedMultiValueMap.add("password", azkabanPassword);

        HttpEntity<LinkedMultiValueMap<String, String>> httpEntity = new HttpEntity<LinkedMultiValueMap<String, String>>(linkedMultiValueMap, hs);
        String result = restTemplate.postForObject(url, httpEntity, String.class);
        return result == null ? null : JSONObject.fromObject(result);
    }
    
    /**
     * 获取登录认证后的sessionid
     * @return
     * @throws Exception
     */
    public String getSessionId() throws Exception{
    	JSONObject jsonObject = login();
		return jsonObject == null ? null : jsonObject.get("session.id").toString();
    }

    /**
     * 创建任务测试 创建一个project
     */
    public void createPro(String sessionId, String name, String description) throws Exception {
        HttpHeaders hs = new HttpHeaders();
        hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        hs.add("X-Requested-With", "XMLHttpRequest");
        LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<String, String>();
        linkedMultiValueMap.add("session.id", sessionId);
        linkedMultiValueMap.add("action", "create");
        linkedMultiValueMap.add("name", name);
        linkedMultiValueMap.add("description", description);

        HttpEntity<LinkedMultiValueMap<String, String>> httpEntity = new HttpEntity<LinkedMultiValueMap<String, String>>(linkedMultiValueMap, hs);
        String postForObject = restTemplate.postForObject(url + "/manager", httpEntity, String.class);
        System.out.println(postForObject);

    }

    /**
     * 删除任务测试 删除一个project
     */
    public void deletePro(String sessionId, String projectName) throws Exception {
        HttpHeaders hs = new HttpHeaders();
        hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        hs.add("X-Requested-With", "XMLHttpRequest");
        hs.add("Accept", "text/plain;charset=utf-8");

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", sessionId);
        map.put("project", projectName);

        ResponseEntity<String> exchange = restTemplate.exchange(
                url + "/manager?session.id={id}&delete=true&project={project}", HttpMethod.GET,
                new HttpEntity<String>(hs), String.class, map);

        System.out.println(exchange.getBody());
    }

    /**
     * 上传zip 上传依赖文件 zip包
     */
    public JSONObject uploadZip(String sessionId, String filePath) throws Exception {
        FileSystemResource resource = new FileSystemResource(new File(filePath));
        LinkedMultiValueMap<String, Object> linkedMultiValueMap = new LinkedMultiValueMap<String, Object>();
        linkedMultiValueMap.add("session.id", sessionId);
        linkedMultiValueMap.add("ajax", "upload");
        linkedMultiValueMap.add("project", projectName);
        linkedMultiValueMap.add("file", resource);
        String result = restTemplate.postForObject(url + "/manager", linkedMultiValueMap, String.class);
        return result == null ? null : JSONObject.fromObject(result);
    }

    /**
     * Fetch Flows of a Project 获取一个project的流ID
     */
    public JSONObject fetchFlowAPro(String sessionId) throws Exception {
        HttpHeaders hs = new HttpHeaders();
        hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        hs.add("X-Requested-With", "XMLHttpRequest");
        hs.add("Accept", "text/plain;charset=utf-8");

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", sessionId);
        map.put("project", projectName);

        ResponseEntity<String> exchange = restTemplate.exchange(
                url + "/manager?session.id={id}&ajax=fetchprojectflows&project={project}", HttpMethod.GET,
                new HttpEntity<String>(hs), String.class, map);
        return exchange == null ? null : JSONObject.fromObject(exchange.getBody());
    }

    /**
     * Fetch Jobs of a Flow 获取一个job的流结构 依赖关系
     */
    public JSONObject fetchFlow(String sessionId, String flow) throws Exception {
        HttpHeaders hs = new HttpHeaders();
        hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        hs.add("X-Requested-With", "XMLHttpRequest");
        hs.add("Accept", "text/plain;charset=utf-8");

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", sessionId);
        map.put("project", projectName);
        map.put("flow", flow);

        ResponseEntity<String> exchange = restTemplate.exchange(
                url + "/manager?session.id={id}&ajax=fetchflowgraph&project={project}&flow={flow}", HttpMethod.GET,
                new HttpEntity<String>(hs), String.class, map);
        return exchange == null ? null : JSONObject.fromObject(exchange.getBody());
    }

    /**
     * Fetch Executions of a Flow 获取执行的project 列表
     */
    public JSONObject fetchEXEFlow(String sessionId, String flow, String start, String length) throws Exception {
        HttpHeaders hs = new HttpHeaders();
        hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        hs.add("X-Requested-With", "XMLHttpRequest");
        hs.add("Accept", "text/plain;charset=utf-8");

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", sessionId);
        map.put("project", projectName);
        map.put("flow", flow);
        map.put("start", start);
        map.put("length", length);

        ResponseEntity<String> exchange = restTemplate.exchange(
                url + "/manager?session.id={id}&ajax=fetchFlowExecutions&project={project}&flow={flow}&start={start}&length={length}",
                HttpMethod.GET, new HttpEntity<String>(hs), String.class, map);
        return exchange == null ? null : JSONObject.fromObject(exchange.getBody());
    }

    /**
     * Fetch Running Executions of a Flow 获取正在执行的流id
     */
    public JSONObject fetchRunningEXEFlow(String sessionId, String flow) throws Exception {
        HttpHeaders hs = new HttpHeaders();
        hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        hs.add("X-Requested-With", "XMLHttpRequest");
        hs.add("Accept", "text/plain;charset=utf-8");

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", sessionId);
        map.put("project", projectName);
        map.put("flow", flow);
        ResponseEntity<String> exchange = restTemplate.exchange(
                url + "/executor?session.id={id}&ajax=getRunning&project={project}&flow={flow}", HttpMethod.GET,
                new HttpEntity<String>(hs), String.class, map);
        return exchange == null ? null : JSONObject.fromObject(exchange.getBody());
    }

    /**
     * Execute a Flow 执行一个流 还有很多其他参数 具体参考api
     */
    public JSONObject fetchEXEaFlow(String sessionId, String flow) throws Exception {
        HttpHeaders hs = new HttpHeaders();
        hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        hs.add("X-Requested-With", "XMLHttpRequest");
        hs.add("Accept", "text/plain;charset=utf-8");

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", sessionId);
        map.put("project", projectName);
        map.put("flow", flow);
        ResponseEntity<String> exchange = restTemplate.exchange(
                url + "/executor?session.id={id}&ajax=executeFlow&project={project}&flow={flow}", HttpMethod.GET,
                new HttpEntity<String>(hs), String.class, map);
        return exchange == null ? null : JSONObject.fromObject(exchange.getBody());
    }

    /**
     * Cancel a Flow Execution 中断一个执行流
     */
    public JSONObject cancelEXEaFlowTest(String sessionId, String execid) throws Exception {
        HttpHeaders hs = new HttpHeaders();
        hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        hs.add("X-Requested-With", "XMLHttpRequest");
        hs.add("Accept", "text/plain;charset=utf-8");

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", sessionId);
        map.put("execid", execid);
        ResponseEntity<String> exchange = restTemplate.exchange(
                url + "/executor?session.id={id}&ajax=cancelFlow&execid={execid}", HttpMethod.GET,
                new HttpEntity<String>(hs), String.class, map);
        return exchange == null ? null : JSONObject.fromObject(exchange.getBody());
    }
    
    /**
     * Flexible scheduling using Cron 通过cron表达式调度执行 创建调度任务
     */
    public JSONObject scheduleByCronEXEaFlow(String sessionId, String flow, String cronExpression) throws Exception {
        HttpHeaders hs = new HttpHeaders();
        hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        hs.add("X-Requested-With", "XMLHttpRequest");
        LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<String, String>();
        linkedMultiValueMap.add("session.id", sessionId);
        linkedMultiValueMap.add("ajax", "scheduleCronFlow");
        linkedMultiValueMap.add("projectName", projectName);
        linkedMultiValueMap.add("cronExpression", cronExpression);
        linkedMultiValueMap.add("flowName", flow);
        linkedMultiValueMap.add("flow", flow);
        
        HttpEntity<LinkedMultiValueMap<String, String>> httpEntity = new HttpEntity<LinkedMultiValueMap<String, String>>(linkedMultiValueMap, hs);
        String postForObject = restTemplate.postForObject(url + "/schedule", httpEntity, String.class);
        return postForObject == null ? null : JSONObject.fromObject(postForObject);
    }
    
    /**
     * Fetch a Schedule 获取一个调度器job的信息 根据project的id 和 flowId
     */
    public JSONObject fetchScheduleInfo(String sessionId, String projectId, String flow) throws Exception {
        HttpHeaders hs = new HttpHeaders();
        hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        hs.add("X-Requested-With", "XMLHttpRequest");
        hs.add("Accept", "text/plain;charset=utf-8");

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", sessionId);
        map.put("projectId", projectId);
        map.put("flowId", flow);
        ResponseEntity<String> exchange = restTemplate.exchange(
                url + "/schedule?session.id={id}&ajax=fetchSchedule&projectId={projectId}&flowId={flowId}",
                HttpMethod.GET, new HttpEntity<String>(hs), String.class, map);
        return exchange == null ? null : JSONObject.fromObject(exchange.getBody());
    }
    
    /**
     * Unschedule a Flow 取消一个流的调度
     */
    public JSONObject unscheduleFlowByScheduleId(String sessionId, String scheduleId) throws Exception {
        HttpHeaders hs = new HttpHeaders();
        hs.add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        hs.add("X-Requested-With", "XMLHttpRequest");
        LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<String, String>();
        linkedMultiValueMap.add("session.id", sessionId);
        linkedMultiValueMap.add("action", "removeSched");
        linkedMultiValueMap.add("scheduleId", scheduleId);

        HttpEntity<LinkedMultiValueMap<String, String>> httpEntity = new HttpEntity<LinkedMultiValueMap<String, String>>(linkedMultiValueMap, hs);
        String postForObject = restTemplate.postForObject(url + "/schedule", httpEntity, String.class);
        return postForObject == null ? null : JSONObject.fromObject(postForObject);
    }
    
    /**
     * Unschedule a Flow 取消一个流的调度
     */
    public JSONObject unscheduleFlowByFlow(String sessionId, String projectId, String flow) throws Exception {
    	JSONObject jsonObject = fetchScheduleInfo(sessionId, projectId, flow);
    	if(jsonObject != null && jsonObject.get("schedule") != null){
    		JSONObject schedule = JSONObject.fromObject(jsonObject.get("schedule"));
    		JSONObject result = unscheduleFlowByScheduleId(sessionId, schedule.getString("scheduleId"));
    		return result == null ? null : JSONObject.fromObject(result);
    	} else {
    		return jsonObject;
    	}
    }
}
