package com.jzsec.modules.eplCategory.entity;

import java.util.List;

import com.jzsec.common.persistence.DataEntity;

/**
 * 报警规则配置类
 * @author 劉 焱
 * @date 2016-7-25
 * @tags
 */
public class EplCategoryRelation extends DataEntity<EplCategoryRelation> {

	private static final long serialVersionUID = 1L;

    private Integer categoryId;//规则父类名称

    private Integer eplId;//规则ID
    
    private List<String> categoryIdList;

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getEplId() {
		return eplId;
	}

	public void setEplId(Integer eplId) {
		this.eplId = eplId;
	}

	public List<String> getCategoryIdList() {
		return categoryIdList;
	}

	public void setCategoryIdList(List<String> categoryIdList) {
		this.categoryIdList = categoryIdList;
	}

}