package com.jzsec.modules.epl.entity;

import java.util.List;

import com.jzsec.common.persistence.DataEntity;
import com.jzsec.common.utils.StringUtil;
import com.jzsec.modules.eplCategory.entity.EplCategory;
import com.jzsec.modules.func.entity.Func;

/**
 * Created by caodaoxi on 16-8-19.
 */
public class Epl extends DataEntity<Epl> {

	private static final long serialVersionUID = 1L;

	private Integer eplId;//风控规则id

    private String eplName;//规则中文或英文名称

    private Integer parentId;//父类规则id
    
    private String parentName;//父类规则名称

    private String epl;//风控规则sql，sql中阀值需要取阀值配置表的变量替换

    private String eplDescribe;//规则描述

    private Integer status;//状态：1,运行上线状态.2,下线状态

    private String textState;//中文描述模板
    
    private String startTime;//EPL启动时间
    
    private String endTime;//EPL结束时间
    
    private String onlineTime;//EPL上线时间
    
    private String offlineTime;//EPL下线时间
    
    private Integer isAlarm;//是否报警:1报警,2不报警
    
    private Integer eplBelong;//规则所属类型:1事前,2事中,3事后
    
    private String scheduleTime;//事中规则调度时间

    private String thresholds;//阀值
    
    private String keyword;//搜索关键词
    
    private List<Threshold> thresholdList;//阀值
    
    private String thresholdListStr;//阀值
    
    private Integer alarmId;//报警类型ID
    
    private Integer alarmEplId;//报警类型ID[自增]
    
    private String alarmName;//报警类型名称
    
    private AlarmEpl alarmEpl;//报警规则
    
    private List<AlarmThreshold> alarmThresholdList;//报警规则阀值
    
    private String alarmThresholdStr;//报警规则阀值
    
    private Integer alarmGroupId;//报警规则接收组
    
    private Integer alarmType;//报警规则类型
    
    private String alarmSQL;//报警规则sql
    
    private String alarmTemplate;//报警规则模板
    
    private String alarmDescribe;//报警规则描述

	private List<Func> funcs; //查询功能点集合
	
    private String funcStr;//功能点,以逗号分隔的字符串
    
    private String funcStr2;//功能点,以逗号分隔的字符串[用于web页面多选框]
    
	private List<EplGroupKey> groupKeys; //查询hash键值集合
	
    private String eplGroupKeyStr;//指令字段id集合, 以逗号分隔的字符串
    
    private String eplGroupKeyStr2;//指令字段id集合, 以逗号分隔的字符串[用于web页面多选框]
    
    private List<String> userEplIds;//当前用户权限内规则id  
    
    private List<EplCategory> userEplParents;//当前用户权限内父类规则
    
    private String eplParents;//规则父类,以逗号分隔的字符串
    
    private String eplParents2;//规则父类,以逗号分隔的字符串[用于web页面多选框]
    
    private String eplTypes;//规则筛选,以逗号分隔的字符串
    
    private String eplTypes2;//规则筛选,以逗号分隔的字符串[用于web页面多选框]
    
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName == null ? null : parentName.trim();
	}

	public String getEpl() {
        return epl;
    }

    public void setEpl(String epl) {
        this.epl = StringUtil.stringHandle(epl);
    }

    public String getEplDescribe() {
        return eplDescribe;
    }

    public void setEplDescribe(String eplDescribe) {
        this.eplDescribe = eplDescribe == null ? null : eplDescribe.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTextState() {
        return textState;
    }

    public void setTextState(String textState) {
        this.textState = textState == null ? null : textState.trim();
    }

    public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime == null ? null : startTime.trim();
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime == null ? null : endTime.trim();
	}

	public String getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(String onlineTime) {
		this.onlineTime = onlineTime == null ? null : onlineTime.trim();
	}

	public String getOfflineTime() {
		return offlineTime;
	}

	public void setOfflineTime(String offlineTime) {
		this.offlineTime = offlineTime == null ? null : offlineTime.trim();
	}

	public Integer getIsAlarm() {
		return isAlarm;
	}

	public void setIsAlarm(Integer isAlarm) {
		this.isAlarm = isAlarm;
	}

	public Integer getEplBelong() {
		return eplBelong;
	}

	public void setEplBelong(Integer eplBelong) {
		this.eplBelong = eplBelong;
	}

	public String getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public String getThresholds() {
        return thresholds;
    }

    public void setThresholds(String thresholds) {
        this.thresholds = thresholds == null ? null : thresholds.trim();
    }

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword == null ? null : keyword.trim();
	}

	public List<Threshold> getThresholdList() {
		return thresholdList;
	}

	public String getThresholdListStr() {
		return thresholdListStr;
	}

	public void setThresholdListStr(String thresholdListStr) {
		this.thresholdListStr = thresholdListStr;
	}

	public void setThresholdList(List<Threshold> thresholdList) {
		this.thresholdList = thresholdList;
	}

	public Integer getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(Integer alarmId) {
		this.alarmId = alarmId;
	}

	public Integer getAlarmEplId() {
		return alarmEplId;
	}

	public void setAlarmEplId(Integer alarmEplId) {
		this.alarmEplId = alarmEplId;
	}

	public String getAlarmName() {
		return alarmName;
	}

	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName == null ? null : alarmName.trim();
	}

	public AlarmEpl getAlarmEpl() {
		return alarmEpl;
	}

	public void setAlarmEpl(AlarmEpl alarmEpl) {
		this.alarmEpl = alarmEpl;
	}

	public List<AlarmThreshold> getAlarmThresholdList() {
		return alarmThresholdList;
	}

	public void setAlarmThresholdList(List<AlarmThreshold> alarmThresholdList) {
		this.alarmThresholdList = alarmThresholdList;
	}

	public String getAlarmThresholdStr() {
		return alarmThresholdStr;
	}

	public void setAlarmThresholdStr(String alarmThresholdStr) {
		this.alarmThresholdStr = StringUtil.stringHandle(alarmThresholdStr);
	}

	public Integer getAlarmGroupId() {
		return alarmGroupId;
	}

	public void setAlarmGroupId(Integer alarmGroupId) {
		this.alarmGroupId = alarmGroupId;
	}

	public Integer getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(Integer alarmType) {
		this.alarmType = alarmType;
	}

	public String getAlarmSQL() {
		return alarmSQL;
	}

	public void setAlarmSQL(String alarmSQL) {
		this.alarmSQL = StringUtil.stringHandle(alarmSQL);
	}

	public String getAlarmTemplate() {
		return alarmTemplate;
	}

	public void setAlarmTemplate(String alarmTemplate) {
		this.alarmTemplate = StringUtil.stringHandle(alarmTemplate);
	}

	public String getAlarmDescribe() {
		return alarmDescribe;
	}

	public void setAlarmDescribe(String alarmDescribe) {
		this.alarmDescribe = StringUtil.stringHandle(alarmDescribe);
	}

	public String getFuncStr() {
		return funcStr;
	}

	public void setFuncStr(String funcStr) {
		this.funcStr = StringUtil.stringHandle(funcStr);
	}

	public String getFuncStr2() {
		return funcStr2;
	}

	public void setFuncStr2(String funcStr2) {
		this.funcStr2 = funcStr2;
	}

	public List<Func> getFuncs() {
		return funcs;
	}

	public void setFuncs(List<Func> funcs) {
		this.funcs = funcs;
	}

	public List<EplGroupKey> getGroupKeys() {
		return groupKeys;
	}

	public void setGroupKeys(List<EplGroupKey> groupKeys) {
		this.groupKeys = groupKeys;
	}

	public String getEplGroupKeyStr() {
		return eplGroupKeyStr;
	}

	public void setEplGroupKeyStr(String eplGroupKeyStr) {
		this.eplGroupKeyStr = eplGroupKeyStr;
	}

	public String getEplGroupKeyStr2() {
		return eplGroupKeyStr2;
	}

	public void setEplGroupKeyStr2(String eplGroupKeyStr2) {
		this.eplGroupKeyStr2 = eplGroupKeyStr2;
	}

	public List<String> getUserEplIds() {
		return userEplIds;
	}

	public void setUserEplIds(List<String> userEplIds) {
		this.userEplIds = userEplIds;
	}

	public List<EplCategory> getUserEplParents() {
		return userEplParents;
	}

	public void setUserEplParents(List<EplCategory> userEplParents) {
		this.userEplParents = userEplParents;
	}

	public String getEplParents() {
		return eplParents;
	}

	public void setEplParents(String eplParents) {
		this.eplParents = eplParents;
	}

	public String getEplParents2() {
		return eplParents2;
	}

	public void setEplParents2(String eplParents2) {
		this.eplParents2 = eplParents2;
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
