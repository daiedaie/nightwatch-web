package com.jzsec.modules.watch.entity;

import java.util.List;

import com.jzsec.common.persistence.DataEntity;
import com.jzsec.common.utils.StringUtil;
import com.jzsec.modules.epl.entity.Epl;

/**
 * 重点观察客户信息
 * @author 劉 焱
 * @date 2017-3-13
 */
public class Watch extends DataEntity<Watch> {
	private static final long serialVersionUID = 1L;
	private Long fundid;
	private Long custid;
	private String fundname;
	private Long ymtCode;
	private String orgid;
    private String orgname;
    private String isFollow;
    
    private Integer triggerCnt;
    private Integer triggerDates;
    private String triggerLastTime;
    private String startDate;
    private String endDate;
    
	private List<Epl> eplList;//该用户所能查看的EPL规则列表
    
	public Long getFundid() {
		return fundid;
	}
	public void setFundid(Long fundid) {
		this.fundid = fundid;
	}
	public Long getCustid() {
		return custid;
	}
	public void setCustid(Long custid) {
		this.custid = custid;
	}
	public String getFundname() {
		return fundname;
	}
	public void setFundname(String fundname) {
		this.fundname = fundname;
	}
	public Long getYmtCode() {
		return ymtCode;
	}
	public void setYmtCode(Long ymtCode) {
		this.ymtCode = ymtCode;
	}
	public String getOrgid() {
		return orgid;
	}
	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	public String getIsFollow() {
		return isFollow;
	}
	public void setIsFollow(String isFollow) {
		this.isFollow = isFollow;
	}
	public Integer getTriggerCnt() {
		return triggerCnt;
	}
	public void setTriggerCnt(Integer triggerCnt) {
		this.triggerCnt = triggerCnt;
	}
	public Integer getTriggerDates() {
		return triggerDates;
	}
	public void setTriggerDates(Integer triggerDates) {
		this.triggerDates = triggerDates;
	}
	public String getTriggerLastTime() {
		return StringUtil.subDateLength(triggerLastTime);
	}
	public void setTriggerLastTime(String triggerLastTime) {
		this.triggerLastTime = triggerLastTime;
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
	public List<Epl> getEplList() {
		return eplList;
	}
	public void setEplList(List<Epl> eplList) {
		this.eplList = eplList;
	}

}
