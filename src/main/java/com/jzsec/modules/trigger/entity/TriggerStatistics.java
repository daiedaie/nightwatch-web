/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jzsec.modules.trigger.entity;

import com.jzsec.common.persistence.DataEntity;
import com.jzsec.common.utils.StringUtil;

/**
 * 触发记录统计
 * @author 劉 焱
 * @date 2016-9-14
 * @tags
 */
public class TriggerStatistics extends DataEntity<TriggerStatistics> {

	private static final long serialVersionUID = 1L;

	private String triggerDate;

	private int processed;

	private int ignored;

	private int unprocessed;

	private int processing;

	private int total;

	private int processedAdd;
	private int ignoredAdd;
	private int unprocessedAdd;
	private int processingAdd;
	private int totalAdd;
	private double processedPre;
	private double ignoredPre;
	private double unprocessedPre;
	private double processingPre;
	private double totalPre;
	private String period;//日/时/分
	private int isNew;//是否最新数据:1.是,0.否
	public String getTriggerDate() {
		return triggerDate;
	}

	public void setTriggerDate(String triggerDate) {
		this.triggerDate = triggerDate;
	}

	public int getProcessed() {
		return processed;
	}

	public void setProcessed(int processed) {
		this.processed = processed;
	}

	public int getIgnored() {
		return ignored;
	}

	public void setIgnored(int ignored) {
		this.ignored = ignored;
	}

	public int getUnprocessed() {
		return unprocessed;
	}

	public void setUnprocessed(int unprocessed) {
		this.unprocessed = unprocessed;
	}

	public int getProcessing() {
		return processing;
	}

	public void setProcessing(int processing) {
		this.processing = processing;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getProcessedAdd() {
		return processedAdd;
	}

	public void setProcessedAdd(int processedAdd) {
		this.processedAdd = processedAdd;
	}

	public int getIgnoredAdd() {
		return ignoredAdd;
	}

	public void setIgnoredAdd(int ignoredAdd) {
		this.ignoredAdd = ignoredAdd;
	}

	public int getUnprocessedAdd() {
		return unprocessedAdd;
	}

	public void setUnprocessedAdd(int unprocessedAdd) {
		this.unprocessedAdd = unprocessedAdd;
	}

	public int getProcessingAdd() {
		return processingAdd;
	}

	public void setProcessingAdd(int processingAdd) {
		this.processingAdd = processingAdd;
	}

	public int getTotalAdd() {
		return totalAdd;
	}

	public void setTotalAdd(int totalAdd) {
		this.totalAdd = totalAdd;
	}

	public double getProcessedPre() {
		return processedPre;
	}

	public void setProcessedPre(double processedPre) {
		this.processedPre = processedPre;
	}

	public double getIgnoredPre() {
		return ignoredPre;
	}

	public void setIgnoredPre(double ignoredPre) {
		this.ignoredPre = ignoredPre;
	}

	public double getUnprocessedPre() {
		return unprocessedPre;
	}

	public void setUnprocessedPre(double unprocessedPre) {
		this.unprocessedPre = unprocessedPre;
	}

	public double getProcessingPre() {
		return processingPre;
	}

	public void setProcessingPre(double processingPre) {
		this.processingPre = processingPre;
	}

	public double getTotalPre() {
		return totalPre;
	}

	public void setTotalPre(double totalPre) {
		this.totalPre =  totalPre;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = StringUtil.stringHandle(period);
	}

	public int getIsNew() {
		return isNew;
	}

	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}
	
}