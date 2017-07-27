package com.jzsec.common.websocket;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.jzsec.common.config.MyCache;
import com.jzsec.common.config.MyConst;
import com.jzsec.common.utils.DateUtils;
import com.jzsec.common.utils.SpringContextHolder;
import com.jzsec.modules.epl.entity.Epl;
import com.jzsec.modules.eplCategory.service.EplCategoryService;
import com.jzsec.modules.sys.entity.User;
import com.jzsec.modules.sys.utils.UserUtils;
import com.jzsec.modules.trigger.entity.TriggerStatistics;
import com.jzsec.modules.trigger.service.TriggerRecordService;

/**
 * websocke的收到消息的具体实现
 * @author wangyong01@szkingdom
 *
 */
@Component("webSokcetReceivedMsg") //配置文件生成相应Bean
public class MyWebSocketReceivedMsg {
	private static Logger logger=Logger.getLogger(MyWebSocketReceivedMsg.class);
	@Resource
	private  MyWebSocketSendMsg webSocketSendMsg;
	
	/**
	 * httpsession待完善, 不能使用
	 * @param session
	 * @param webSocketMessage
	 * @throws Exception
	 */
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws Exception {
		// 检查HttpSession是否过期
		User user = (User) session.getAttributes().get(MyConst.SESSION_USER);
		System.out.println(session.getAttributes().get(MyConst.SESSION_USER));
		String userId = user == null ? null : user.getId();
		if (!checkHttpSession(session, userId)) {
			webSocketSendMsg.closeSessionByUserId(userId);// 过期关闭
			return;
		} else {
			// TODO 延长httpSession存活时间 防止HttpSession过期
		}
		// 获取文本消息
		TextMessage textMessage = new TextMessage(webSocketMessage.getPayload()+ "");
		String content = textMessage.getPayload();
		logger.info("userId" + userId + " ;content" + content);
		// 处理信息
//		dealStringMsg(userName, content);
	}
	
	
	/**
	 * 握手成功后 初次链接成功执行的方法
	 * @param session
		 * @throws Exception
	 */
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		User user = (User) session.getAttributes().get(MyConst.SESSION_USER);
        String userId = user.getId();
        // 放入容器中
        MyCache.userWebSocketSession.put(userId, session);
        
        TriggerRecordService recordService =  SpringContextHolder.getBean("triggerRecordService");
		EplCategoryService categoryService =  SpringContextHolder.getBean("eplCategoryService");
		
		// 查询当天未读消息
		Map<String, Object> map = new HashMap<String, Object>();
		Date date = new Date();
    	String nowDate = DateUtils.formatDate(date, MyConst.YYYY_MM_DD);
		map.put("startDate", nowDate+" 00:00:00");
		map.put("endDate", DateUtils.formatDate(date, MyConst.YYYY_MM_DD_HH_MM_SS));
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
		MyCache.triggerUndeal.put("currentStatistics", MyTimerTask.getCurrentStatistics(triggerStatistics));
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
		webSocketSendMsg.sendTextMessageToUser(userId, json);
//        System.out.println(MyCache.triggerUndeal.get("list").getClass());
//        webSocketSendMsg.sendTextMessageToUser(userId, ! MyCache.triggerUndeal.get("list").getClass().equals(String.class) ? "" : (String) MyCache.triggerUndeal.get("list"));
    }
	
	/**
	 * 检查HttpSession是否过期，通过返回true，不通过返回false
	 * @param session
	 * @return 
	 */
	private boolean checkHttpSession(WebSocketSession session,String userId){
    	HttpSession httpSession = MyCache.userHttpSessionMap.get(userId);
    	if(httpSession==null){
    		return false;
    	}else{
    		return true;
    	}
	}

}
