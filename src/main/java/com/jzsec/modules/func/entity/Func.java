package com.jzsec.modules.func.entity;

import com.jzsec.common.persistence.DataEntity;
import com.jzsec.common.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能点
 * @author 劉 焱
 * @date 2016-10-19
 */
public class Func extends DataEntity<Func> {
	private static final long serialVersionUID = 1L;
	private Integer funcid;
    private String funcname;
    private Integer schemaId;
    private String schemaName;
    private String schemaDescribe;
    private String funcsql;
    private String funcStatisticSql;
    private String keyword;
	private List<FuncField> fields;
	private String fieldStr;

	public List<FuncField> getFields() {
		return fields;
	}

	public void setFields(List<FuncField> fields) {
		this.fields = fields;
	}

	public String getFieldStr() {
		return fieldStr;
	}

	public void setFieldStr(String fieldStr) {
		this.fieldStr = fieldStr;
		String[] fs = fieldStr.split(",");
		if(fs.length > 0) {
			fields = new ArrayList<FuncField>();
			FuncField funcField = null;
			for (String f : fs) {
//				System.out.println(f);
				funcField = new FuncField();
				String[] kv = f.split("@");
				funcField.setFieldName(kv[0]);
				funcField.setIsStatistic(Integer.parseInt(kv[1]));
				funcField.setIsCondition(Integer.parseInt(kv[2]));
				funcField.setFieldDescribe(kv.length ==4 ? kv[3] : kv[0]);
				fields.add(funcField);
			}
		}
	}

	public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

	public Integer getFuncid() {
		return funcid;
	}

	public void setFuncid(Integer funcid) {
		this.funcid = funcid;
	}

	public String getFuncname() {
		return funcname;
	}

	public void setFuncname(String funcname) {
		this.funcname = funcname;
	}

	public Integer getSchemaId() {
		return schemaId;
	}

	public void setSchemaId(Integer schemaId) {
		this.schemaId = schemaId;
	}

	public String getFuncsql() {
		return funcsql;
	}

	public String getSchemaDescribe() {
		return schemaDescribe;
	}

	public void setSchemaDescribe(String schemaDescribe) {
		this.schemaDescribe = schemaDescribe;
	}

	public void setFuncsql(String funcsql) {
		this.funcsql = StringUtil.stringHandle(funcsql);
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getFuncStatisticSql() {
		return funcStatisticSql;
	}

	public void setFuncStatisticSql(String funcStatisticSql) {
		this.funcStatisticSql = funcStatisticSql;
	}

}
