package com.jzsec.modules.eplCategory.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jzsec.common.service.CrudService;
import com.jzsec.common.utils.DateUtil;
import com.jzsec.common.utils.StringUtils;
import com.jzsec.modules.epl.entity.Epl;
import com.jzsec.modules.epl.service.EplService;
import com.jzsec.modules.eplCategory.dao.EplCategoryDao;
import com.jzsec.modules.eplCategory.entity.EplCategory;
import com.jzsec.modules.sys.dao.RoleDao;
import com.jzsec.modules.sys.entity.Role;
import com.jzsec.modules.sys.utils.UserUtils;

/**
 * 报警规则配置业务实现层
 * @author 劉 焱
 * @date 2016-7-25
 * @tags
 */
@Service
@Transactional(readOnly = true)
public class EplCategoryService extends CrudService<EplCategoryDao, EplCategory> {
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private EplService eplService;
	/**
	 * 查询风控规则父类类型
	 */
	public List<EplCategory> findTypeList() {
		return dao.findTypeList();
	}
	/**
	 * 获取当前用户权限内能够查看的规则父类型
	 * @return
	 */
	public List<EplCategory> getEplParentTypeRoleList() {
		return getEplParentTypeRoleList(UserUtils.getUser().getId());
	}
	/**
	 * 获取当前用户权限内能够查看的规则类型
	 * @return
	 */
	public List<Epl> getEplTypeRoleList() {
		return getEplTypeRoleList(UserUtils.getUser().getId());
	}
	/**
	 * 获取指定用户权限内能够查看的规则父类型
	 * @return
	 */
	public List<EplCategory> getEplParentTypeRoleList(String userId) {
		List<EplCategory> parentTypes = dao.findTypeList();
		List<String> parentIds = new ArrayList<String>();
		for(EplCategory category: parentTypes){
			parentIds.add(category.getId());
		}
		
		List<EplCategory> eplTypeList = new ArrayList<EplCategory>();
		String userName = UserUtils.get(userId) == null ? "" : UserUtils.get(userId).getName();
		if(! "admin".equals(userName)){
			List<String> eplTypeStrList = roleDao.findEplTypeByUserId(userId);
			for (String eplType : eplTypeStrList) {
				String[] eplParentIds = eplType.split(",");
				for(String eplParentId : eplParentIds){
					eplParentId = eplParentId.trim();
					if(!"".equals(eplParentId) && parentIds.contains(eplParentId)){
						parentIds.remove(eplParentId);
						for(EplCategory category: parentTypes){
							if(eplParentId.equals(category.getId())){
								eplTypeList.add(category);
							}
						}
					}
				}
			}
		} else {
			eplTypeList = parentTypes;
		}
		return eplTypeList;
	}
	/**
	 * 获取指定用户权限内能够查看的规则类型
	 * @return
	 */
	public List<Epl> getEplTypeRoleList(String userId) {
		List<EplCategory> parentTypes = getEplParentTypeRoleList(userId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parentTypes", parentTypes);
		List<Epl> eplList = eplService.findEplTypeList(map);
		
		return eplList;
	}
	/**
	 * 获取当前用户权限内能够查看的规则类型[包括父类规则]
	 * @return
	 */
	public List<Epl> getParentAndEplTypeRoleList() {
		return getParentAndEplTypeRoleList(UserUtils.getUser().getId());
	}
	/**
	 * 获取指定用户权限内能够查看的规则类型[包括父类规则]
	 * @param id
	 * @return
	 */
	private List<Epl> getParentAndEplTypeRoleList(String userId) {
		List<EplCategory> parentTypes = getEplParentTypeRoleList();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parentTypes", parentTypes);
		List<Epl> eplList = eplService.findEplTypeRelateParentList(map);
		
		return eplList;
	}
	/**
	 * 保存数据
	 * @param category
	 */
	@Override
	@Transactional(readOnly = false)
	public void save(EplCategory category) {
		if (category.getIsNewRecord()){
			category.preInsert();
			category.setId(DateUtil.getIntTimeId()+"");
			dao.insert(category);
			List<Role> roles = roleDao.findRoleIdsByUserId(UserUtils.getUser().getId());
			for(Role role : roles){
				Role role1 = new Role();
				role1.setId(role.getId());
				String eplType = role.getEplType();
				if(StringUtils.isBlank(eplType)){
					eplType = category.getId();
				} else {
					eplType += ","+category.getId();
				}
				role1.setEplType(eplType);
				roleDao.updateEplType(role1);
			}
		}else{
			category.preUpdate();
			dao.update(category);
		}		
	}
	
	/**
	 * 删除数据
	 * @param entity
	 */
	@Override
	@Transactional(readOnly = false)
	public void delete(EplCategory category) {
		List<Role> roles = roleDao.findRoleIdsByUserId(UserUtils.getUser().getId());
		for(Role role : roles){
			String eplType = role.getEplType();
			if(!StringUtils.isBlank(eplType)){
				List<String> eplTypes = new ArrayList<String>(Arrays.asList(eplType.split(",")));
				if(eplTypes.contains(category.getId())){
					eplTypes.remove(category.getId());
					eplType = "";
					for(String categoryId : eplTypes){
						if(StringUtils.isBlank(categoryId)){
							eplType = categoryId;
						} else {
							eplType += categoryId;
						}
					}
					Role role1 = new Role();
					role1.setId(role.getId());
					role1.setEplType(eplType);
					roleDao.updateEplType(role1);
				}
			}
		}
		dao.delete(category);
	}
}
