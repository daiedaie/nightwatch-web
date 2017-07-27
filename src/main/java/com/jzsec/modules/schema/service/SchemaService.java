package com.jzsec.modules.schema.service;

import java.util.ArrayList;
import java.util.List;

import com.jzsec.common.config.LoadConfig;
import com.jzsec.common.service.CrudService;
import com.jzsec.common.utils.StringUtils;
import com.jzsec.modules.common.servie.CommonService;
import com.jzsec.modules.schema.dao.SchemaDao;
import com.jzsec.modules.schema.dao.SchemaFieldDao;
import com.jzsec.modules.schema.entity.Schema;
import com.jzsec.modules.schema.entity.SchemaField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 接口Service
 * @author caodaoxi
 * @version 2016-05-28
 */
@Service
@Transactional(readOnly = true)
public class SchemaService extends CrudService<SchemaDao, Schema> {
	@Autowired
	private SchemaFieldDao schemaFieldDao;
	@Autowired
	private CommonService commonService;
	
	/**
	 * 编辑Schema及其field
	 * @param schema
	 */
	@Transactional(readOnly = false)
	public void saveSchema(Schema schema) {
		if (schema.getIsNewRecord()){
			schema.preInsert();
			dao.insert(schema);
		}else{
			schema.preUpdate();
			dao.update(schema);
			schema.setSchemaId(dao.findUniqueByProperty("id", schema.getId()).getSchemaId());
			schemaFieldDao.deleteFields(schema.getSchemaId());
		}
		
		List<SchemaField> addList = new ArrayList<SchemaField>();
		if(!StringUtils.isBlank(schema.getSchemaFieldListStr())) {
			String[] schemaFieldListStr = schema.getSchemaFieldListStr().split("\\|");
			for(String schemaFieldStr : schemaFieldListStr) {
				String[] fieldList = schemaFieldStr.split(",");
				SchemaField schemaField = new SchemaField();
				String id = StringUtils.isBlank(fieldList[0]) ? null : fieldList[0].trim();
				schemaField.setId(id);
				schemaField.setSchemaId(Integer.parseInt(fieldList[1]));
				schemaField.setFieldName(fieldList[2]);
				schemaField.setFieldType(fieldList[3]);
				schemaField.setFieldDescribe(fieldList[4]);
				addList.add(schemaField);
			}
			schemaFieldDao.batchInsert(addList);
		}		
	}
	/**
	 * 删除Schema及其关联Field
	 * @param schema
	 */
	@Transactional(readOnly = false)
	public void deleteSchema(Schema schema) {
		dao.delete(schema);
		schemaFieldDao.deleteFields(schema.getSchemaId());
	}
	/**
	 * 推送至zookeeper数据
	 * @param schema
	 * @throws Exception 
	 */
	public void PushZookeeper(Schema schema, boolean isRemove) throws Exception {
		if(isRemove){
	    	commonService.rtcPushData(schema.getSchemaId(), "remove", LoadConfig.getZookeeperRiskPath(),  LoadConfig.getZookeeperSchemaChildPath());
		}else{
			if(schema.getIsNewRecord()){
		    	commonService.rtcPushData(schema.getSchemaId(), "add", LoadConfig.getZookeeperRiskPath(),  LoadConfig.getZookeeperSchemaChildPath());
			}else{
		    	commonService.rtcPushData(schema.getSchemaId(), "update", LoadConfig.getZookeeperRiskPath(),  LoadConfig.getZookeeperSchemaChildPath());
			}
		}
	}

}
