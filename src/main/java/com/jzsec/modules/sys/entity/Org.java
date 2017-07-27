/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jzsec.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.jzsec.common.config.Global;
import com.jzsec.common.persistence.DataEntity;
import com.jzsec.common.utils.Collections3;
import com.jzsec.common.utils.excel.annotation.ExcelField;
import com.jzsec.common.utils.excel.fieldtype.RoleListType;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 用户Entity
 * @author jeeplus
 * @version 2013-12-05
 */
public class Org extends DataEntity<Org> {

	private static final long serialVersionUID = 1L;
	private int orgid;
	private String orgname;

	public int getOrgid() {
		return orgid;
	}

	public void setOrgid(int orgid) {
		this.orgid = orgid;
	}

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
}