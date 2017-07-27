package com.jzsec.modules.cust.entity;

import com.jzsec.common.persistence.DataEntity;

/**
 * 客户信息
 * @author 劉 焱
 * @date 2016-10-31
 */
public class Cust extends DataEntity<Cust> {
	private static final long serialVersionUID = 1L;
	private Long fundid;
	private Long custid;
	private String fundname;
	private Long ymtCode;
	private String orgid;
    private String orgname;
    private Long opendate;
    private Long closedate;
    private String status;
    private Double assets;
    private String watchStatus;
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
	public Long getOpendate() {
		return opendate;
	}
	public void setOpendate(Long opendate) {
		this.opendate = opendate;
	}
	public Long getClosedate() {
		return closedate;
	}
	public void setClosedate(Long closedate) {
		this.closedate = closedate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Double getAssets() {
		return assets;
	}
	public void setAssets(Double assets) {
		this.assets = assets;
	}
	public String getWatchStatus() {
		return watchStatus;
	}
	public void setWatchStatus(String watchStatus) {
		this.watchStatus = watchStatus;
	}

}
