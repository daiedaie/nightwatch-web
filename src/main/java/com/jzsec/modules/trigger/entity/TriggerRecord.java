package com.jzsec.modules.trigger.entity;

import java.util.List;

import com.jzsec.common.persistence.DataEntity;
import com.jzsec.common.utils.StringUtil;
import com.jzsec.common.utils.excel.annotation.ExcelField;
import com.jzsec.modules.epl.entity.Epl;

/**
 * 风控规则触发记录类
 * @author 劉 焱
 * @date 2016-7-25
 * @tags
 */
public class TriggerRecord  extends DataEntity<TriggerRecord>{
	
	private static final long serialVersionUID = 1L;
	@ExcelField(title="规则id", align=2, sort=20)
	private Integer eplId; //规则id
	@ExcelField(title="规则名称", align=2, sort=3)
	private String eplName; //规则名称
	@ExcelField(title="触发时的统计状态", align=2, sort=20)
	private String triggerState; //触发时的统计状态
	@ExcelField(title="规则触发时的时间", align=2, sort=20)
	private String triggerTime;  //规则触发时的时间
	@ExcelField(title="处理状态", align=2, sort=20)
	private Integer status;  //处理状态
	private Integer oldStatus;  //之前处理状态
	private String opinion; //处理意见
	private String dealTime; //处理时间
	@ExcelField(title="资金账号", align=2, sort=2)
	private String ymtCode;   //一码通账号

	private String textState; //统计状态文本模板

	private String eplDescribe; //规则描述

	private String thresholds;  //阀值
	private String orgid;  //机构id
	@ExcelField(title="机构名称", align=2, sort=1)
	private String orgname; //机构名称

	private String startTime;  //开始时间

	private String endTime;	//结束时间

	private String readNum;		// 已读
	private String unReadNum;	// 未读

	private boolean isSelf;		// 是否只查询自己的通知

	private String readFlag;	// 本人阅读状态
    
	private String time;//统计时间
	
	private int totalCount;//总数
	
	private int dealCount;//已处理数量
	
	private int undealCount;//未处理数量
	
	private int ignoreCount;//忽略数量
	
	private int verificationCount;//处理中的数量
	
	private String startDate;//查询开始时间
	
	private String endDate;//查询结束时间
	
	private String level;//事件级别
	
	private int triggerCnt;//触发次数
	
	private int statisticPageNo;//触发统计表当前页码
	
	private int statisticPageSize;//触发统计表页面大小
	
	private Long statisticYmtCode;//触发统计表点击事件条件字段
	private Integer statisticEplId;//触发统计表点击事件条件字段
	private String statisticLevel;//触发统计表点击事件条件字段
	private Integer statisticStatus;//触发统计表点击事件条件字段
	private String orderBy;//点击排序字段
	private String statusName;//状态中文名称
	
	private List<Epl> eplList;//该用户所能查看的EPL规则列表
    
    private String eplTypes;//规则筛选,以逗号分隔的字符串
    
    private String eplTypes2;//规则筛选,以逗号分隔的字符串[用于web页面多选框]
	
//	private String orderBy;//详情页排序
//	
//	public String getOrderBy() {
//		return orderBy;
//	}
//
//	public void setOrderBy(String orderBy) {
//		this.orderBy = orderBy;
//	}

	public TriggerRecord() {
		super();
	}

	public TriggerRecord(String id){
		super(id);
	}

	public String getReadNum() {
		return readNum;
	}

	public void setReadNum(String readNum) {
		this.readNum = readNum;
	}

	public String getUnReadNum() {
		return unReadNum;
	}

	public void setUnReadNum(String unReadNum) {
		this.unReadNum = unReadNum;
	}

	public boolean isSelf() {
		return isSelf;
	}

	public void setSelf(boolean self) {
		isSelf = self;
	}

	public String getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(String readFlag) {
		this.readFlag = readFlag;
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time+":00";
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getDealCount() {
		return dealCount;
	}

	public void setDealCount(int dealCount) {
		this.dealCount = dealCount;
	}

	public int getUndealCount() {
		return undealCount;
	}

	public void setUndealCount(int undealCount) {
		this.undealCount = undealCount;
	}

	public int getIgnoreCount() {
		return ignoreCount;
	}

	public void setIgnoreCount(int ignoreCount) {
		this.ignoreCount = ignoreCount;
	}

	public int getVerificationCount() {
		return verificationCount;
	}

	public void setVerificationCount(int verificationCount) {
		this.verificationCount = verificationCount;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = StringUtil.stringHandle(startDate);
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = StringUtil.stringHandle(endDate);
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
        this.eplName = eplName == null ? null : eplName.trim();
    }

    public String getTriggerState() {
        return triggerState;
    }

    public void setTriggerState(String triggerState) {
        this.triggerState = triggerState == null ? null : triggerState.trim();
    }

    public String getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(String triggerTime) {
        this.triggerTime = StringUtil.subDateLength(triggerTime);
    }

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion == null ? null : opinion.trim();
	}

	public String getDealTime() {
		return dealTime;
	}

	public void setDealTime(String dealTime) {
		this.dealTime = StringUtil.subDateLength(dealTime);
	}

	public String getYmtCode() {
		return ymtCode;
	}

	public void setYmtCode(String ymtCode) {
		this.ymtCode = ymtCode;
	}

	public String getTextState() {
		return textState;
	}

	public void setTextState(String textState) {
		this.textState = textState == null ? null : textState.trim();
	}

	public String getEplDescribe() {
		return eplDescribe;
	}

	public void setEplDescribe(String eplDescribe) {
		this.eplDescribe = eplDescribe == null ? null : eplDescribe.trim();
	}

	public String getThresholds() {
		return thresholds;
	}

	public void setThresholds(String thresholds) {
		this.thresholds = thresholds == null ? null : thresholds.trim();
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid == null ? null : orgid.trim();
	}

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname == null ? null : orgname.trim();
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = StringUtil.subDateLength(startTime);
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = StringUtil.subDateLength(endTime);
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public int getTriggerCnt() {
		return triggerCnt;
	}

	public void setTriggerCnt(int triggerCnt) {
		this.triggerCnt = triggerCnt;
	}

	public Integer getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(Integer oldStatus) {
		this.oldStatus = oldStatus;
	}

	public int getStatisticPageNo() {
		return statisticPageNo;
	}

	public void setStatisticPageNo(int statisticPageNo) {
		this.statisticPageNo = statisticPageNo;
	}

	public int getStatisticPageSize() {
		return statisticPageSize;
	}

	public void setStatisticPageSize(int statisticPageSize) {
		this.statisticPageSize = statisticPageSize;
	}

	public Long getStatisticYmtCode() {
		return statisticYmtCode;
	}

	public void setStatisticYmtCode(Long statisticYmtCode) {
		this.statisticYmtCode = statisticYmtCode;
	}

	public Integer getStatisticEplId() {
		return statisticEplId;
	}

	public void setStatisticEplId(Integer statisticEplId) {
		this.statisticEplId = statisticEplId;
	}

	public String getStatisticLevel() {
		return statisticLevel;
	}

	public void setStatisticLevel(String statisticLevel) {
		this.statisticLevel = statisticLevel;
	}

	public Integer getStatisticStatus() {
		return statisticStatus;
	}

	public void setStatisticStatus(Integer statisticStatus) {
		this.statisticStatus = statisticStatus;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public List<Epl> getEplList() {
		return eplList;
	}

	public void setEplList(List<Epl> eplList) {
		this.eplList = eplList;
	}

	public String getEplTypes() {
		return eplTypes;
	}

	public void setEplTypes(String eplTypes) {
		this.eplTypes = eplTypes;
	}

	public String getEplTypes2() {
		return eplTypes2;
	}

	public void setEplTypes2(String eplTypes2) {
		this.eplTypes2 = eplTypes2;
	}

}