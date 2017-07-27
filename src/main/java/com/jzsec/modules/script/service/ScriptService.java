package com.jzsec.modules.script.service;

import com.jzsec.common.config.LoadConfig;
import com.jzsec.common.service.CrudService;
import com.jzsec.modules.common.servie.CommonService;
import com.jzsec.modules.script.dao.ScriptDao;
import com.jzsec.modules.script.entity.Script;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * esper脚本配置
 * @author 劉 焱
 * @date 2016-12-13
 */
@Service
@Transactional(readOnly = true)
public class ScriptService extends CrudService<ScriptDao, Script> {
	@Autowired
	private CommonService commonService;
	
	/**
	 * 推送至zookeeper数据
	 * @param schema
	 * @throws Exception 
	 */
	public void PushZookeeper(Script script, boolean isRemove) throws Exception {
		if(isRemove){
	    	commonService.rtcPushData(script.getScriptId(), "remove", LoadConfig.getZookeeperRiskPath(),  LoadConfig.getZookeeperScriptChildPath());
		}else{
			if(script.getIsNewRecord()){
		    	commonService.rtcPushData(script.getScriptId(), "add", LoadConfig.getZookeeperRiskPath(),  LoadConfig.getZookeeperScriptChildPath());
			}else{
		    	commonService.rtcPushData(script.getScriptId(), "update", LoadConfig.getZookeeperRiskPath(),  LoadConfig.getZookeeperScriptChildPath());
			}
		}
	}
}