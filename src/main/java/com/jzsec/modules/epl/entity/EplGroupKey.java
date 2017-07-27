package com.jzsec.modules.epl.entity;

import com.jzsec.common.persistence.DataEntity;

/**
 * EPL之间hash键值
 * @author 劉 焱
 * @date 2016-10-19
 */
public class EplGroupKey extends DataEntity<EplGroupKey> {
	private static final long serialVersionUID = 1L;
	private Integer eplId;
    private Integer schemaId;
    private Integer fieldId;
    private String fieldName;
    private String fieldDescribe;
    private String fieldtype;
	public Integer getEplId() {
		return eplId;
	}
	public void setEplId(Integer eplId) {
		this.eplId = eplId;
	}
	public Integer getSchemaId() {
		return schemaId;
	}
	public void setSchemaId(Integer schemaId) {
		this.schemaId = schemaId;
	}
	public Integer getFieldId() {
		return fieldId;
	}
	public void setFieldId(Integer fieldId) {
		this.fieldId = fieldId;
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
	public String getFieldtype() {
		return fieldtype;
	}
	public void setFieldtype(String fieldtype) {
		this.fieldtype = fieldtype;
	}
}
