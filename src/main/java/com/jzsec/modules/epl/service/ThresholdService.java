package com.jzsec.modules.epl.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.jzsec.common.persistence.Page;
import com.jzsec.common.service.CrudService;
import com.jzsec.common.utils.StringUtil;
import com.jzsec.modules.epl.dao.EplDao;
import com.jzsec.modules.epl.dao.ThresholdDao;
import com.jzsec.modules.epl.entity.Epl;
import com.jzsec.modules.epl.entity.Threshold;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 接口Service
 * @author caodaoxi
 * @version 2016-05-28
 */
@Service
@Transactional(readOnly = true)
public class ThresholdService extends CrudService<ThresholdDao, Threshold> {
	@Resource
	private EplDao eplDao;
	
	@Transactional(readOnly = false)
	public void deleteThresholds(Integer eplId) {
		dao.deleteThresholds(eplId);
	}

	/**
	 * 通过eplid和thresholdName查询字段
	 * @param threshold
	 * @return
	 */
	public Threshold queryThresholdByThreshold(Threshold threshold) {
		return dao.queryThresholdByThreshold(threshold);
	}

	/**
	 * 查询EPL规则所有阀值, 若EPL新建还未设置阀值, 则列出其阀值名称
	 * @param threshold
	 * @return
	 */
	public List<Threshold> findAllThresholds(Threshold threshold) {
		//通过EPLID查询数据库所有阀值
		List<Threshold> thresholds = dao.findAllThresholds(threshold);

//		//若该EPL没有阀值, 则截取EPL规则中{}括号内的阀值名称
//		if(thresholds == null || thresholds.size() == 0){
//			Epl epl = eplDao.findUniqueByProperty("epl_id", threshold.getEplId());
//			if(epl != null && ! StringUtil.isEmpty(epl.getEpl())){
//				String eplStr = epl.getEpl();
//				String[] thresholdStr = eplStr.replaceAll("\\$\\{", "\\}").split("\\}");
//				
//				//若该EPL有阀值,则将阀值名称添加到阀值列表中
//				if(thresholdStr.length>1){
//					thresholds  = new ArrayList<Threshold>();
//					for(int i=0; i<thresholdStr.length; i++){
//						if(i%2==1){
//							Threshold threshold2 = new Threshold();
//							String thresholdName = thresholdStr[i];
//							threshold2.setThresholdName(thresholdName);
//							threshold2.setEplId(threshold.getEplId());
//							thresholds.add(threshold2);
//						}
//					}
//				}
//				
//			}
//			
//		}
		
		return thresholds;
	}
	
//	@Transactional(readOnly = false)
//	public int saveSchemaField(SchemaField entity) {
//		if (entity.getIsNewRecord()){
//			entity.preInsert();
//			return dao.insert(entity);
//		}else{
//			entity.preUpdate();
//			dao.update(entity);
//			return Integer.parseInt(entity.getId());
//		}
//	}
//
//
//	@Transactional(readOnly = false)
//	public int querySchemaFieldByFieldName(SchemaField entity) {
//		if (entity.getIsNewRecord()){
//			entity.preInsert();
//			return dao.insert(entity);
//		}else{
//			entity.preUpdate();
//			dao.update(entity);
//			return Integer.parseInt(entity.getId());
//		}
//	}
//
//	@Transactional(readOnly = false)
//	public int deleteFields(int schemaId) {
//		return dao.deleteFields(schemaId);
//	}
}
