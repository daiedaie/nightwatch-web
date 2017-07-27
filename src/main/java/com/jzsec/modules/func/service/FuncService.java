package com.jzsec.modules.func.service;

import java.util.ArrayList;
import java.util.List;

import com.jzsec.common.service.CrudService;
import com.jzsec.common.utils.StringUtil;
import com.jzsec.modules.func.dao.FuncDao;
import com.jzsec.modules.func.dao.FuncFieldDao;
import com.jzsec.modules.func.entity.Func;
import com.jzsec.modules.func.entity.FuncField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 功能点
 * @author 劉 焱
 * @date 2016-10-19
 */
@Service
@Transactional(readOnly = true)
public class FuncService extends CrudService<FuncDao, Func> {
	@Autowired
	private FuncFieldDao fieldDao;

	/**
	 * 获取所有的功能点列表,用于选择
	 * @return
	 */
	public List<Func> findNameList() {
		return dao.findNameList();
	}

	/**
	 * 根据规则id获取对应的功能点
	 * @param funcid
	 * @return
	 */
    public List<Func> findFuncByEplId(int eplId) {
        return dao.findFuncByEplId(eplId);
    }
    
	/**
	 * 根据funcid获取对应的功能点
	 * @param funcid
	 * @return
	 */
    public Func findFuncByFuncid(int funcid) {
        return dao.findFuncByFuncid(funcid);
    }
    
    /**
     * 删除
     */
    @Override
    @Transactional(readOnly=false)
    public void delete(Func func) {
    	dao.delete(func);
    	FuncField field = new FuncField();
    	field.setFuncid(func.getFuncid());
    	fieldDao.deletefields(field);
    }
    
    /**
     * 保存
     */
    @Override
    @Transactional(readOnly=false)
    public void save(Func func) {
    	if(StringUtil.isEmpty(func.getId())){
    		add(func);
    	}else{
    		update(func);
    	}
    }

	/**
	 * 新增功能点并处理字段
	 * @param func
	 */
    @Transactional(readOnly=false)
	private void add(Func func) {
		dao.insert(func);
		
		List<FuncField> funcFields = new ArrayList<FuncField>();
		getAllFields(func, funcFields);
		
		fieldDao.batchInsert(funcFields);
	}

	/**
	 * 根据func SQL和统计SQL获取所有的字段
	 * @param func
	 * @param funcFields
	 */
	private void getAllFields(Func func, List<FuncField> funcFields) {
		//详情SQL
		String sql =  func.getFuncsql() == null ? "" :func.getFuncsql();
		List<String> fields  = getSQLFieldList(new ArrayList<String>(), sql);
		if(fields.size() > 0){
			for(int i=0; i<fields.size(); i++){
				FuncField field = new FuncField();
				field.setFuncid(func.getFuncid());
				field.setIsStatistic(0);
				field.setIsCondition(2);
				field.setFieldName(fields.get(i));
				funcFields.add(field);
			}
		}
		
		//统计SQL
		String statisticSql =  func.getFuncStatisticSql() == null ? "" :func.getFuncStatisticSql();
		List<String> statisticFields  = getSQLFieldList(new ArrayList<String>(), statisticSql);
		if(statisticFields.size() > 0){
			for(int i=0; i<statisticFields.size(); i++){
				FuncField field = new FuncField();
				field.setFuncid(func.getFuncid());
				field.setIsStatistic(1);
				field.setIsCondition(2);
				field.setFieldName(statisticFields.get(i));
				funcFields.add(field);
			}
		}
	}

	/**
	 * 获取SQL内的字段名称, 并放入List集合中
	 * @param set
	 * @param sql
	 * @return
	 */
	private List<String> getSQLFieldList(List<String> list, String sql) {
		String[] asFields = sql.split("FROM")[0].split("SELECT");
		String asFields1 = asFields[0].trim();
		if("".equals(asFields1) && asFields.length>1){
			asFields1 = asFields[1].trim();
		}
		String[] asField1 = asFields1.split(",");
		
		int length = asField1.length;
		if(length > 1 || (length == 1 && !"".equals(asField1[0].trim()))){
			for(int i=0; i<length; i++){
				if(! "".equals(asField1[i].trim())){
					String[] fieldStr = asField1[i].split("AS");
					list.add(fieldStr.length==1 ? fieldStr[0].trim() : fieldStr[1].trim());
				}
			}
		}
		return list;
	}
	
	/**
	 * 变更功能点并处理字段
	 * @param func
	 */
    @Transactional(readOnly=false)
	private void update(Func func) {
		dao.update(func);
		
		//获取页面传过来的所有字段
		List<FuncField> funcFields = new ArrayList<FuncField>();
		getAllFields(func, funcFields);
		
		if(funcFields.size() > 0){
			//遍历原有的字段并筛选出无用的字段以及新增的字段
			FuncField field = new FuncField();
			field.setFuncid(func.getFuncid());
			List<FuncField> oldFields = fieldDao.findList(field);
			
			//将旧字段放入待删除列表中
			List<FuncField> delFields = new ArrayList<FuncField>();
			delFields.addAll(oldFields);
			
			//将新字段放入带插入列表中
			List<FuncField> insertFields = new ArrayList<FuncField>();
			insertFields.addAll(funcFields);
			
			//移除待删除列表和待插入列表的相同字段
			if(oldFields != null && oldFields.size() != 0){
				for(int i=0; i<oldFields.size(); i++){
					for(int j=0; j<funcFields.size(); j++){
						if(funcFields.get(j).getFieldName().equals(oldFields.get(i).getFieldName()) 
								&& funcFields.get(j).getIsStatistic() == oldFields.get(i).getIsStatistic()){
							delFields.remove(oldFields.get(i));
							insertFields.remove(funcFields.get(j));
						}
					}
				}
			}
			
			//增加新字段
			if(insertFields != null && insertFields.size() != 0){
				List<FuncField> newFields  = new ArrayList<FuncField>();
				for(int i=0; i<insertFields.size(); i++){
					FuncField field2 = new FuncField();
					field2.setFuncid(func.getFuncid());
					field2.setIsStatistic(insertFields.get(i).getIsStatistic());
					field2.setIsCondition(insertFields.get(i).getIsCondition());
					field2.setFieldName(insertFields.get(i).getFieldName());
					newFields.add(field2);
				}
				fieldDao.batchInsert(newFields);
			}
			
			//删除无用的旧字段
			if(delFields != null && delFields.size() != 0){
				List<String> delIds = new ArrayList<String>();
				for(int i=0; i<delFields.size(); i++){
					delIds.add(delFields.get(i).getId());
				}
				fieldDao.batchDelete(delIds);
			}
		}
	}

}
