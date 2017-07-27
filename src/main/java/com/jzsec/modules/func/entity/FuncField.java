package com.jzsec.modules.func.entity;

import com.jzsec.common.persistence.DataEntity;

/**
 * Created by caodaoxi on 16-10-21.
 */
public class FuncField extends DataEntity<FuncField> {

    private Integer funcid;
    private String fieldName;
    private String fieldDescribe;
    private Integer isStatistic;
    private Integer isCondition;

    public Integer getFuncid() {
        return funcid;
    }

    public void setFuncid(Integer funcid) {
        this.funcid = funcid;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldDescribe() {
        return fieldDescribe;
    }

    public void setFieldDescribe(String fieldDescribe) {
        this.fieldDescribe = fieldDescribe;
    }

	public Integer getIsStatistic() {
		return isStatistic;
	}

	public void setIsStatistic(Integer isStatistic) {
		this.isStatistic = isStatistic;
	}

	public Integer getIsCondition() {
		return isCondition;
	}

	public void setIsCondition(Integer isCondition) {
		this.isCondition = isCondition;
	}
    
}
