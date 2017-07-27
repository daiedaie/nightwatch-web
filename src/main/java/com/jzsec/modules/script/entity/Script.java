package com.jzsec.modules.script.entity;

import com.jzsec.common.persistence.DataEntity;

/**
 * esper脚本
 * @author 劉 焱
 * @date 2016-12-13
 */
public class Script extends DataEntity<Script> {
    private Integer scriptId;
    private String scriptName;
    private String scriptCode;
    private String scriptDescribe;
    private String keyword;

    public Integer getScriptId() {
		return scriptId;
	}

	public void setScriptId(Integer scriptId) {
		this.scriptId = scriptId;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public String getScriptCode() {
		return scriptCode;
	}

	public void setScriptCode(String scriptCode) {
		this.scriptCode = scriptCode;
	}

	public String getScriptDescribe() {
		return scriptDescribe;
	}

	public void setScriptDescribe(String scriptDescribe) {
		this.scriptDescribe = scriptDescribe;
	}

	public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
