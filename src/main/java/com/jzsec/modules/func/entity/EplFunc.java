package com.jzsec.modules.func.entity;

import com.jzsec.common.persistence.DataEntity;

/**
 * epl与功能点映射实体类
 * @author 劉 焱
 * @date 2016-10-27
 */
public class EplFunc extends DataEntity<EplFunc> {
	private static final long serialVersionUID = 1L;
	
	private int funcid;
	
	private int eplId;

	public int getFuncid() {
		return funcid;
	}

	public void setFuncid(int funcid) {
		this.funcid = funcid;
	}

	public int getEplId() {
		return eplId;
	}

	public void setEplId(int eplId) {
		this.eplId = eplId;
	}
	
}
