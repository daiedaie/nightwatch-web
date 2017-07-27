package com.jzsec.modules.epl.service;

import com.jzsec.common.config.LoadConfig;
import com.jzsec.common.persistence.Page;
import com.jzsec.common.service.CrudService;
import com.jzsec.common.utils.StringUtil;
import com.jzsec.common.utils.StringUtils;
import com.jzsec.modules.common.entity.PushAlarmEpl;
import com.jzsec.modules.common.entity.PushEpl;
import com.jzsec.modules.common.servie.CommonService;
import com.jzsec.modules.epl.dao.AlarmEplDao;
import com.jzsec.modules.epl.dao.AlarmEplRelationDao;
import com.jzsec.modules.epl.dao.AlarmThresholdDao;
import com.jzsec.modules.epl.dao.EplDao;
import com.jzsec.modules.epl.dao.EplGroupKeyDao;
import com.jzsec.modules.epl.dao.ThresholdDao;
import com.jzsec.modules.epl.entity.AlarmEpl;
import com.jzsec.modules.epl.entity.AlarmEplRelation;
import com.jzsec.modules.epl.entity.AlarmThreshold;
import com.jzsec.modules.epl.entity.Epl;
import com.jzsec.modules.epl.entity.EplGroupKey;
import com.jzsec.modules.epl.entity.Threshold;
import com.jzsec.modules.eplCategory.dao.EplCategoryDao;
import com.jzsec.modules.eplCategory.dao.EplCategoryRelationDao;
import com.jzsec.modules.eplCategory.entity.EplCategory;
import com.jzsec.modules.eplCategory.entity.EplCategoryRelation;
import com.jzsec.modules.func.dao.FuncDao;
import com.jzsec.modules.func.entity.EplFunc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 风控规则
 * @author 劉 焱
 * @date 2016-8-24
 * @tags
 */
@Service
@Transactional(readOnly = true)
public class EplService extends CrudService<EplDao, Epl> {
	@Autowired
	private CommonService commonService;
	@Autowired
	private EplCategoryDao eplCategoryDao;
	@Autowired
	private EplCategoryRelationDao categoryRelationDao;
	@Autowired
	private AlarmEplRelationDao alarmEplRelationDao;
	@Autowired
	private AlarmEplDao alarmEplDao;
	@Autowired
	private AlarmThresholdDao alarmThresholdDao;
	@Autowired
	private ThresholdDao thresholdDao;
	@Autowired
	private FuncDao funcDao;
	@Autowired
	private EplGroupKeyDao groupKeyDao;
	
	/**
	 * 规则列表查询
	 */
	@Override
	public Page<Epl> findPage(Page<Epl> page, Epl epl) {
		epl.setPage(page);
		List<Epl> list = dao.findList(epl);
		List<Epl> returnList = new ArrayList<Epl>();
		for(Epl epl1 : list){
			Epl record = epl1;
			String decrible = commonService.getEplSql(epl1.getThresholds(), epl1.getEplDescribe());
			record.setEplDescribe(decrible);
			returnList.add(record);
		}
		page.setList(returnList);
		return page;
	}

	/**
	 * 规则编辑
	 * @param epl
	 * @return 
	 */
	@Transactional(readOnly=false)
	public String edit(Epl epl, Epl oldEpl){
		//主题类型是否改变
		String eplParents = epl.getEplParents()==null ? "" : epl.getEplParents();
		List<String> eplParentList = Arrays.asList(eplParents.split(","));
		String oldEplParents = oldEpl.getEplParents()==null ? "" : oldEpl.getEplParents();
		List<String> oldEplParentList = Arrays.asList(oldEplParents.split(","));
		boolean flag = false;
		for(String eplParentId : eplParentList){
			if("".equals(oldEplParents)){
				flag = true;
				break;
			}
			for(String oldEplParentId : oldEplParentList){
				if(!oldEplParentList.contains(eplParentId) || !eplParentList.contains(oldEplParentId)){
					flag = true;
					break;
				}
			}
		}
		
		//主题类型改变
		if(flag){
			EplCategoryRelation categoryRelation = new EplCategoryRelation();
			categoryRelation.setEplId(epl.getEplId());
			categoryRelation.setCategoryIdList(eplParentList);
			categoryRelationDao.delete(categoryRelation);
			categoryRelationDao.insertBatch(categoryRelation);
		}
		
		//修改EPL与功能点的关联
		if(! StringUtil.isEmpty(epl.getId())){
			funcDao.deleteByEplId(oldEpl.getEplId());
		}
		String funcidStr = epl.getFuncStr();
		String[] funcids = funcidStr.split(",");
		List<EplFunc> funcs = new ArrayList<EplFunc>();
		for(int i=0; i<funcids.length; i++){
			EplFunc func = new EplFunc();
			func.setFuncid(Integer.parseInt(funcids[i]));
			func.setEplId(epl.getEplId());
			funcs.add(func);
		}
		funcDao.batchInsert(funcs);
		
		//若为事中规则, 修改EPL与hash键值的关联
		if(epl.getEplBelong() == 2){
			if(! StringUtil.isEmpty(epl.getId())){
				groupKeyDao.deleteByEplId(epl.getEplId());
			}
			String groupKeyStr = epl.getEplGroupKeyStr();
			String[] fieldIds = groupKeyStr.split(",");
			List<EplGroupKey> groupKeys = new ArrayList<EplGroupKey>();
			for(int i=0; i<fieldIds.length; i++){
				EplGroupKey key = new EplGroupKey();
				key.setFieldId(Integer.parseInt(fieldIds[i]));
				key.setEplId(epl.getEplId());
				groupKeys.add(key);
			}
			groupKeyDao.batchInsert(groupKeys);
		}
		
		if(StringUtil.isEmpty(epl.getId()) && epl.getStatus() == 1){//新增且上线
			String addResult = eplAdd(epl);
			if(! StringUtil.isEmpty(addResult)){
				return addResult;
			}
		}else if(StringUtil.isEmpty(epl.getId()) && epl.getStatus() != 1){//新增且下线
			String addResult = eplAdd(epl);
			if(! StringUtil.isEmpty(addResult)){
				return addResult;
			}
		}else if(!StringUtil.isEmpty(epl.getId())){//修改
			eplUpdate(epl, oldEpl);
		}
		
		return "规则保存成功";
	}

	/**
	 * 变更EPL规则并处理阀值
	 */
	@Transactional(readOnly=false)
	private void eplUpdate(Epl newEpl, Epl oldEpl) {
		dao.update(newEpl);
		String newEplStr = newEpl.getEpl();
		String[] thresholdStr = newEplStr.replaceAll("\\$\\{", "\\}").split("\\}");
		
		
		//若该EPL有阀值新增, 则添加新阀值
		if(thresholdStr.length>1){
			List<String> names = new ArrayList<String>();
			for(int i=0; i<thresholdStr.length; i++){
				if(i%2==1){
					if(!names.contains(thresholdStr[i])){
						String thresholdName = thresholdStr[i];
						names.add(thresholdName);
					}
				}
			}
			
			//遍历原有的阀值并筛选出无用的阀值以及新增的阀值
			Threshold threshold = new Threshold();
			threshold.setEplId(newEpl.getEplId());
			List<Threshold> oldThresholds = thresholdDao.findAllThresholds(threshold);
			
			List<Threshold> delThresholds = new ArrayList<Threshold>();
			for(Threshold threshold2 : oldThresholds){
				Threshold threshold3 = new Threshold();
				threshold3.setId(threshold2.getId());
				threshold3.setThresholdName(threshold2.getThresholdName());
				delThresholds.add(threshold3);
			}
			
			List<String> insertNames = new ArrayList<String>();
			for(String name : names){
				insertNames.add(name); 
			}
			
			if(oldThresholds != null && oldThresholds.size() != 0){
				for(int i=0; i<oldThresholds.size(); i++){
					if(names.contains(oldThresholds.get(i).getThresholdName())){
						delThresholds.remove(oldThresholds.get(i));
						insertNames.remove(oldThresholds.get(i).getThresholdName());
					}
				}
			}
			
			//增加新阀值
			if(insertNames != null && insertNames.size() != 0){
				List<Threshold> newThresholds  = new ArrayList<Threshold>();
				for(int i=0; i<insertNames.size(); i++){
					Threshold threshold2 = new Threshold();
					threshold2.setEplId(newEpl.getEplId());
					threshold2.setThresholdName(insertNames.get(i));
					newThresholds.add(threshold2);
				}
				thresholdDao.batchInsert(newThresholds);
			}
			
			//删除无用的旧阀值
			if(delThresholds != null && delThresholds.size() != 0){
				List<String> delIds = new ArrayList<String>();
				for(int i=0; i<delThresholds.size(); i++){
					delIds.add(delThresholds.get(i).getId());
				}
				thresholdDao.batchDelete(delIds);
			}
		}else{
			//若修改后的EPL规则没有阀值, 则删除阀值表中该规则的阀值
			thresholdDao.deleteThresholds(newEpl.getEplId());
		}
	}

	/**
	 * 新增EPL规则并处理阀值
	 * @param epl
	 */
	@Transactional(readOnly=false)
	private String eplAdd(Epl epl) {
		epl.setIsAlarm(2);
		dao.insert(epl);
		String eplStr = epl.getEpl();
		String[] thresholdStr = eplStr.replaceAll("\\$\\{", "\\}").split("\\}");
		
		//若该EPL有阀值,则将阀值名称添加到阀值列表中
		if(thresholdStr.length>1){
			List<Threshold> thresholds  = new ArrayList<Threshold>();
			Set<String> keySet = new HashSet<String>();
			for(int i=0; i<thresholdStr.length; i++){
				if(i%2==1){
                    if(!keySet.contains(thresholdStr[i])) {
                        Threshold threshold = new Threshold();
                        String thresholdName = thresholdStr[i];
                        threshold.setEplId(epl.getEplId());
                        threshold.setThresholdName(thresholdName);
                        thresholds.add(threshold);
                        keySet.add(thresholdName);
                    }
				}
			}
            keySet = null;
			thresholdDao.batchInsert(thresholds);
		}
		return null;
	}
	
	/**
	 * 获取风控规则类型
	 * @return
	 */
	public List<Epl> findEplCategory() {
		List<Epl> eplList = dao.findEplCategory();
		return eplList;
	}
	/**
	 * 获取风控规则父类型
	 * @return
	 */
	public List<Epl> findParentTypeList() {
		List<Epl> eplList = dao.findParentTypeList();
		return eplList;
	}
	/**
	 * 获取zookeeper推送信息
	 * @param eplId
	 * @return
	 */
	public PushEpl pushZookeeper(Integer eplId) {
		return dao.pushZookeeper(eplId);
	}
	/**
	 * 获取风控规则报警信息
	 * @param epl
	 * @return
	 */
	public Epl findAlarm(Epl epl) {
		if(epl.getIsAlarm()==2){
			return epl;
		}
		epl = dao.findAlarmEpl(epl.getEplId());
		AlarmThreshold alarmThreshold = new AlarmThreshold();
		alarmThreshold.setEplId(epl.getAlarmEplId());
		List<AlarmThreshold> alarmThresholdList = alarmThresholdDao.findList(alarmThreshold);
		epl.setAlarmThresholdList(alarmThresholdList);
		
		return epl;
	}
	/**
	 * 保存报警规则设置
	 * @param epl
	 * @throws Exception 
	 */
	@Transactional(readOnly=false)
	public void saveAlarm(Epl epl, Epl oldEpl) throws Exception {
		//查询报警情况, 并修改相应的报警类型
		 if(oldEpl.getIsAlarm()==2){
//			 AlarmEpl alarmEpl1 = alarmEplDao.findUniqueByProperty("epl_id", epl.getAlarmEplId());
//			 if(alarmEpl1 != null){
//				 throw new Exception("该报警编号已存在!");
//			 }
			AlarmEpl alarmEpl = new AlarmEpl();
			alarmEpl.setEplId(epl.getAlarmEplId());
			alarmEpl.setEpl(epl.getAlarmSQL());
			alarmEpl.setEplName(epl.getAlarmName());
			alarmEpl.setEplDescribe(epl.getAlarmDescribe());
			alarmEpl.setAlarmGroupId(epl.getAlarmGroupId());
			alarmEpl.setAlarmType(epl.getAlarmType());
			alarmEpl.setStatus(epl.getStatus());
			alarmEpl.setAlarmTemplate(epl.getAlarmTemplate());
			alarmEplDao.insert(alarmEpl);
			AlarmEplRelation alarmEplRelation = new AlarmEplRelation();
			alarmEplRelation.setEplId(epl.getEplId());
			alarmEplRelation.setAlarmEplId(epl.getAlarmEplId());
			alarmEplRelationDao.insert(alarmEplRelation);
			
			Epl epl1 = new Epl();
			epl1.setId(epl.getId());
			epl1.setIsAlarm(1);
			dao.update(epl1);
		}else if(oldEpl.getIsAlarm()==1){
			AlarmEpl alarmEpl = new AlarmEpl();
			alarmEpl.setId(epl.getAlarmId().toString());
			alarmEpl.setEplId(epl.getAlarmEplId());
			alarmEpl.setEpl(epl.getAlarmSQL());
			alarmEpl.setEplName(epl.getAlarmName());
			alarmEpl.setEplDescribe(epl.getAlarmDescribe());
			alarmEpl.setAlarmGroupId(epl.getAlarmGroupId());
			alarmEpl.setAlarmType(epl.getAlarmType());
			alarmEpl.setStatus(epl.getStatus());
			alarmEpl.setAlarmTemplate(epl.getAlarmTemplate());
			alarmEplDao.update(alarmEpl);
		}

		 if(!StringUtil.isEmpty(epl.getAlarmThresholdStr())){
			 String[] alarmThresholdList = epl.getAlarmThresholdStr().split("\\|");
			 for(String alarmThresholdStr : alarmThresholdList) {
				 String[] thresholdStr = alarmThresholdStr.split(",");
				 AlarmThreshold alarmThreshold = new AlarmThreshold();
				 alarmThreshold.setEplId(Integer.parseInt(thresholdStr[1]));
				 alarmThreshold.setThresholdName(thresholdStr[2]);
				 alarmThreshold.setThresholdValue(Double.parseDouble(thresholdStr[3]));
				 alarmThreshold.setThresholdDescribe(thresholdStr[4]);
				 if(!StringUtils.isBlank(thresholdStr[0])){
					 alarmThreshold.setId(thresholdStr[0].trim());
					 alarmThresholdDao.update(alarmThreshold);
				 }else{
					 alarmThresholdDao.insert(alarmThreshold);
				 }
			 }	
		 }
		 
	}

	/**
	 * 取消报警
	 * @param epl
	 */
	@Transactional(readOnly=false)
	public void deleteAlarm(Epl epl, Integer alarmEplId) {
		alarmEplRelationDao.deleteByEplId(epl.getEplId());
		alarmEplDao.deleteByEplId(alarmEplId);
		alarmThresholdDao.deleteThresholds(alarmEplId);
		
		epl.setIsAlarm(2);
		dao.update(epl);
	}
	
	/**
	 * 规则删除
	 */
	@Transactional(readOnly=false)
	public void deleteSoft(Epl epl) {
		dao.updateStatus(epl);
	}

	/**
	 * 编辑EPL后将其推送到zookeeper节点
	 * @param epl
	 * @throws Exception 
	 */
	public void editEplToZookeeper(Epl epl, Epl epl1) throws Exception{
		PushEpl obj = pushZookeeper(epl.getEplId());
		if(StringUtil.isEmpty(epl.getId()) && epl.getStatus() == 1){//新增且上线
	    	commonService.rtcEplPushData(obj, "add", LoadConfig.getZookeeperRiskPath(),  LoadConfig.getZookeeperRiskChildPath());
		}else if(!StringUtil.isEmpty(epl.getId())){//修改
			if(epl1.getStatus() == epl.getStatus() && epl.getStatus() == 1){//修改前后都为上线状态
		    	commonService.rtcEplPushData(obj, "update", LoadConfig.getZookeeperRiskPath(),  LoadConfig.getZookeeperRiskChildPath());
			}else if(epl1.getStatus() != epl.getStatus() && epl.getStatus() == 1){//由下线修改为上线状态
		    	commonService.rtcEplPushData(obj, "add", LoadConfig.getZookeeperRiskPath(),  LoadConfig.getZookeeperRiskChildPath());
			}else if(epl1.getStatus() == 1 && epl.getStatus() != 1){//由上线修改为下线状态
		    	commonService.rtcEplPushData(obj, "remove", LoadConfig.getZookeeperRiskPath(),  LoadConfig.getZookeeperRiskChildPath());
			}
		}
	}
	
	/**
	 * 改变EPL状态后将其推送到zookeeper节点
	 * @throws Exception 
	 */
	public void changeEplStatusToZookeeepr(Epl epl) throws Exception{
		PushEpl obj = pushZookeeper(epl.getEplId());
		if(epl.getStatus() == 1){//由下线修改为上线状态
	    	commonService.rtcEplPushData(obj, "add", LoadConfig.getZookeeperRiskPath(),  LoadConfig.getZookeeperRiskChildPath());
		}else {//由上线修改为下线状态
	    	commonService.rtcEplPushData(obj, "remove", LoadConfig.getZookeeperRiskPath(),  LoadConfig.getZookeeperRiskChildPath());
		}
		
		if(epl.getIsAlarm() == 1){
			AlarmEplRelation alarmEplRelation = alarmEplRelationDao.findUniqueByProperty("epl_id", epl.getEplId());
			Integer alarmEplId = alarmEplRelation == null ? null : alarmEplRelation.getAlarmEplId();
			PushAlarmEpl obj2 = alarmEplDao.pushZookeeper(alarmEplId);
			if(epl.getStatus() == 1){//由下线修改为上线状态
		    	commonService.alarmEplPushData(obj2, "add", LoadConfig.getZookeeperAlarmPath(),  LoadConfig.getZookeeperAlarmChildPath());
			}else {//由上线修改为下线状态
		    	commonService.alarmEplPushData(obj2, "remove", LoadConfig.getZookeeperAlarmPath(),  LoadConfig.getZookeeperAlarmChildPath());
			}
		}
	}

	/**
	 * 报警规则变更过后推送至zookeeper节点
	 * @param epl
	 * @param oldEpl
	 * @throws Exception 
	 */
	public void saveAlarmToZookeeper(Epl epl, Epl oldEpl) throws Exception {
		if(epl.getStatus() == 1){
			 PushAlarmEpl obj = alarmEplDao.pushZookeeper(epl.getAlarmEplId());
			 if(oldEpl.getIsAlarm()==2){
				 commonService.alarmEplPushData(obj, "add", LoadConfig.getZookeeperAlarmPath(),  LoadConfig.getZookeeperAlarmChildPath());
			 }else{
				 commonService.alarmEplPushData(obj, "update", LoadConfig.getZookeeperAlarmPath(),  LoadConfig.getZookeeperAlarmChildPath());
			 }
		 }
	}

	/**
	 * 获取用户权限内能够查看的规则类型
	 * @param map
	 * @return
	 */
	public List<Epl> findEplTypeList(Map<String, Object> map) {
		return dao.findEplTypeList(map);
	}

	/**
	 * 获取规则类型及其关联父类型, 用于列表显示
	 * @param map
	 * @return
	 */
	public List<Epl> findEplTypeRelateParentList(Map<String, Object> map) {
		return dao.findEplTypeRelateParentList(map);
	}

	/**
	 * 修改规则话题
	 * @param epl
	 */
	@Transactional(readOnly=false)
	public void categoryEdit(Epl epl) {
		String eplParents = epl.getEplParents()==null ? "" : epl.getEplParents();
		List<String> eplParentList = new ArrayList<String>(Arrays.asList(eplParents.split(",")));
		EplCategoryRelation categoryRelation = new EplCategoryRelation();
		categoryRelation.setEplId(epl.getEplId());
		categoryRelation.setCategoryIdList(eplParentList);
		categoryRelationDao.delete(categoryRelation);
		categoryRelationDao.insertBatch(categoryRelation);		
	}
}