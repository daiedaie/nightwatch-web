package com.jzsec.modules.schema.dao;

import java.util.List;

import com.jzsec.common.persistence.CrudDao;
import com.jzsec.common.persistence.annotation.MyBatisDao;
import com.jzsec.modules.schema.entity.SchemaField;

/**
 * Created by caodaoxi on 16-7-30.
 */
@MyBatisDao
public interface SchemaFieldDao extends CrudDao<SchemaField> {
    public SchemaField querySchemaFieldByFieldName(SchemaField schemaField);
    public int deleteFields(Integer schemaId);
	public void batchInsert(List<SchemaField> addList);

}
