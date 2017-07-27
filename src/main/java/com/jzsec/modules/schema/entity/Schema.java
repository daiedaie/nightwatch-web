package com.jzsec.modules.schema.entity;

import com.jzsec.common.persistence.DataEntity;

import java.util.List;

/**
 * Created by caodaoxi on 16-5-29.
 */
public class Schema extends DataEntity<Schema> {
    private Integer schemaId;
    private String schemaName;
    private String schemaDescribe;
    private List<SchemaField> schemaFieldList;
    private String schemaFieldListStr;
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<SchemaField> getSchemaFieldList() {
        return schemaFieldList;
    }

    public void setSchemaFieldList(List<SchemaField> schemaFieldList) {
        this.schemaFieldList = schemaFieldList;
    }

    public String getSchemaFieldListStr() {
        return schemaFieldListStr;
    }

    public void setSchemaFieldListStr(String schemaFieldListStr) {
        this.schemaFieldListStr = schemaFieldListStr;
    }

    public Integer getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(Integer schemaId) {
        this.schemaId = schemaId;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getSchemaDescribe() {
        return schemaDescribe;
    }

    public void setSchemaDescribe(String schemaDescribe) {
        this.schemaDescribe = schemaDescribe;
    }
}
