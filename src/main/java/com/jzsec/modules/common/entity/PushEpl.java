package com.jzsec.modules.common.entity;

import com.jzsec.common.persistence.DataEntity;

/**
 * 推送至zookeeper节点上的信息
 * @author 劉 焱
 * @date 2016-8-1
 * @tags
 */
public class PushEpl extends DataEntity<PushEpl> {

	private static final long serialVersionUID = 1L;
	private String eplName;
	private String epl;
	private String thresholds;
	private int isAlarm;
	private String allSchema;
	public String getEplName() {
		return eplName;
	}
	public void setEplName(String eplName) {
		this.eplName = eplName;
	}
	public String getEpl() {
		return epl;
	}
	public void setEpl(String epl) {
		this.epl = epl;
	}
	public String getThresholds() {
		return thresholds;
	}
	public void setThresholds(String thresholds) {
		this.thresholds = thresholds;
	}

	public int getIsAlarm() {
		return isAlarm;
	}

	public void setIsAlarm(int isAlarm) {
		this.isAlarm = isAlarm;
	}

	public String getAllSchema() {
		return allSchema;
	}

	public void setAllSchema(String allSchema) {
		this.allSchema = allSchema;
	}
}
