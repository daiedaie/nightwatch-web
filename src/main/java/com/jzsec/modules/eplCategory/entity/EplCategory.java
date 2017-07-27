package com.jzsec.modules.eplCategory.entity;

import java.util.List;

import com.jzsec.common.persistence.DataEntity;

/**
 * 报警规则配置类
 * @author 劉 焱
 * @date 2016-7-25
 * @tags
 */
public class EplCategory extends DataEntity<EplCategory> {

	private static final long serialVersionUID = 1L;

    private String category;//规则父类名称

    private String categoryDescribe;//类别描述

    private String keyword;//搜索关键字
    
    private List<String> categoryIds;//当前用户权限
    
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category == null ? null : category.trim();
	}

	public String getCategoryDescribe() {
		return categoryDescribe;
	}

	public void setCategoryDescribe(String categoryDescribe) {
		this.categoryDescribe = categoryDescribe == null ? null : categoryDescribe.trim();
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<String> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<String> categoryIds) {
		this.categoryIds = categoryIds;
	}

}