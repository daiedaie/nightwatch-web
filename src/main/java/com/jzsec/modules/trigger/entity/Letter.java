package com.jzsec.modules.trigger.entity;

import com.jzsec.common.persistence.DataEntity;

/**
 * 监管函信息
 * @author 劉 焱
 * @date 2016-10-31
 */
public class Letter extends DataEntity<Letter> {
	private static final long serialVersionUID = 1L;
	private String custName;
	private String stockholder;
    private String branchCode;
    private String letterType;
    private String letterFrom;
    private String receiveTime;
    private String uniqueRowid;
    private String reason;
    private String remark;
    private String letterDis;
    private String ymtCode;
    private String orgname;
    private String triggerCnt;
    private Integer eplId;
    private String eplName;
    private String startDate;
    private String endDate;
    
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getStockholder() {
		return stockholder;
	}
	public void setStockholder(String stockholder) {
		this.stockholder = stockholder;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getLetterType() {
		return letterType;
	}
	public void setLetterType(String letterType) {
		this.letterType = letterType;
	}
	public String getLetterFrom() {
		return letterFrom;
	}
	public void setLetterFrom(String letterFrom) {
		this.letterFrom = letterFrom;
	}
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getUniqueRowid() {
		return uniqueRowid;
	}
	public void setUniqueRowid(String uniqueRowid) {
		this.uniqueRowid = uniqueRowid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getLetterDis() {
		return letterDis;
	}
	public void setLetterDis(String letterDis) {
		this.letterDis = letterDis;
	}
	public String getYmtCode() {
		return ymtCode;
	}
	public void setYmtCode(String ymtCode) {
		this.ymtCode = ymtCode;
	}
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	public String getTriggerCnt() {
		return triggerCnt;
	}
	public void setTriggerCnt(String triggerCnt) {
		this.triggerCnt = triggerCnt;
	}
	public Integer getEplId() {
		return eplId;
	}
	public void setEplId(Integer eplId) {
		this.eplId = eplId;
	}
	public String getEplName() {
		return eplName;
	}
	public void setEplName(String eplName) {
		this.eplName = eplName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
