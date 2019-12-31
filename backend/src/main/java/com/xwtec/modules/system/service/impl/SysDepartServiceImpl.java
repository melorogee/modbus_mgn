package com.xwtec.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.xwtec.common.util.JsonUtils;
import com.xwtec.common.util.YouBianCodeUtil;
import com.xwtec.modules.system.controller.FindsDepartsChildrenUtil;
import com.xwtec.modules.system.entity.SysDepart;
import com.xwtec.modules.system.mapper.SysDepartMapper;
import com.xwtec.modules.system.model.SysDepartTreeModel;
import com.xwtec.modules.system.service.ISysDepartService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 部门表 服务实现类
 * <p>
 * 
 * @author Steve
 * @Since 2019-01-22
 */
@Service
public class SysDepartServiceImpl<T> extends ServiceImpl<SysDepartMapper, SysDepart> implements ISysDepartService {

	@Autowired
	private ISysDepartService sysDepartService;

	@Autowired
	private SysDepartMapper sysDepartMapper;

	// 该集合用来存储部门下的所有数据
	private List<SysDepart> globalList = new ArrayList<>();


	/**
	 * queryTreeList 对应 queryTreeList 查询所有的部门数据,以树结构形式响应给前端
	 */
	@Override
	public List<SysDepartTreeModel> queryTreeList() {
		LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
		query.eq(SysDepart::getDelFlag, 0);
		query.orderByAsc(SysDepart::getDepartOrder);
		List<SysDepart> list = sysDepartService.list(query);
		globalList = list;
		// 调用wrapTreeDataToTreeList方法生成树状数据
		List<SysDepartTreeModel> listResult = FindsDepartsChildrenUtil.wrapTreeDataToTreeList(list);
		return listResult;
	}

	/**
	 * saveDepartData 对应 add 保存用户在页面添加的新的部门对象数据
	 */
	@Override
	@Transactional
	public void saveDepartData(SysDepart sysDepart, String username) {
		if (sysDepart != null && username != null) {
			if (sysDepart.getParentId() == null) {
				sysDepart.setParentId("");
			}
			String s = UUID.randomUUID().toString().replace("-", "");
			sysDepart.setId(s);
			// 先判断该对象有无父级ID,有则意味着不是最高级,否则意味着是最高级
			// 获取父级ID
			String parentId = sysDepart.getParentId();
			String[] codeArray = generateOrgCode(parentId);
			sysDepart.setOrgCode(codeArray[0]);
			String orgType = codeArray[1];
			sysDepart.setOrgType(String.valueOf(orgType));
			sysDepart.setCreateTime(new Date());
			sysDepart.setDelFlag("0");
			sysDepartService.save(sysDepart);
		}

	}
	
	/**
	 * saveDepartData 的调用方法,生成部门编码和部门类型
	 * 
	 * @param parentId
	 * @return
	 */
	private String[] generateOrgCode(String parentId) {	
		//update-begin--Author:Steve  Date:20190201 for：组织机构添加数据代码调整
				LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
				LambdaQueryWrapper<SysDepart> query1 = new LambdaQueryWrapper<SysDepart>();
				String[] strArray = new String[2];
		        // 创建一个List集合,存储查询返回的所有SysDepart对象
		        List<SysDepart> departList = new ArrayList<>();
				// 定义新编码字符串
				String newOrgCode = "";
				// 定义旧编码字符串
				String oldOrgCode = "";
				// 定义部门类型
				String orgType = "";		
				// 如果是最高级,则查询出同级的org_code, 调用工具类生成编码并返回                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
				if (StringUtil.isNullOrEmpty(parentId)) {
					// 线判断数据库中的表是否为空,空则直接返回初始编码
					query1.eq(SysDepart::getParentId, "");
					query1.orderByDesc(SysDepart::getOrgCode);
					departList = sysDepartService.list(query1);
					if(departList == null || departList.size() == 0) {
						strArray[0] = YouBianCodeUtil.getNextYouBianCode(null);
						strArray[1] = "1";
						return strArray;
					}else {
					SysDepart depart = departList.get(0);
					oldOrgCode = depart.getOrgCode();
					orgType = depart.getOrgType();
					newOrgCode = YouBianCodeUtil.getNextYouBianCode(oldOrgCode);
					}
				} else { // 反之则查询出所有同级的部门,获取结果后有两种情况,有同级和没有同级
					// 封装查询同级的条件
					query.eq(SysDepart::getParentId, parentId);
					// 降序排序
					query.orderByDesc(SysDepart::getOrgCode);
					// 查询出同级部门的集合
					List<SysDepart> parentList = sysDepartService.list(query);
					// 查询出父级部门
					SysDepart depart = sysDepartService.getById(parentId);
					// 获取父级部门的Code
					String parentCode = depart.getOrgCode();
					// 根据父级部门类型算出当前部门的类型
					orgType = String.valueOf(Integer.valueOf(depart.getOrgType()) + 1);
					// 处理同级部门为null的情况
					if (parentList == null || parentList.size() == 0) {
						// 直接生成当前的部门编码并返回
						newOrgCode = YouBianCodeUtil.getSubYouBianCode(parentCode, null);
					} else { //处理有同级部门的情况
						// 获取同级部门的编码,利用工具类
						String subCode = parentList.get(0).getOrgCode();
						// 返回生成的当前部门编码
						newOrgCode = YouBianCodeUtil.getSubYouBianCode(parentCode, subCode);
					}
				}
				// 返回最终封装了部门编码和部门类型的数组
				strArray[0] = newOrgCode;
				strArray[1] = orgType;
				return strArray;
		//update-end--Author:Steve  Date:20190201 for：组织机构添加数据代码调整
	} 

	
	/**
	 * removeDepartDataById 对应 delete方法 根据ID删除相关部门数据
	 * 
	 */
	/*
	 * @Override
	 * 
	 * @Transactional public boolean removeDepartDataById(String id) {
	 * System.out.println("要删除的ID 为=============================>>>>>"+id); boolean
	 * flag = sysDepartService.removeById(id); return flag; }
	 */

	/**
	 * updateDepartDataById 对应 edit 根据部门主键来更新对应的部门数据
	 */
	@Override
	@Transactional
	public Boolean updateDepartDataById(SysDepart sysDepart, String username) {
		if (sysDepart != null && username != null) {
			sysDepart.setUpdateTime(new Date());
			sysDepart.setUpdateBy(username);
			sysDepartService.updateById(sysDepart);
			return true;
		} else {
			return false;
		}

	}


	/**
	 * <p>
	 * 根据关键字搜索相关的部门数据
	 * </p>
	 */
	@Override
	public List<SysDepartTreeModel> searhBy(String keyWord) {
		LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
		query.like(SysDepart::getDepartName, keyWord);
		List<SysDepart> departList = this.sysDepartService.list(query);
		List<SysDepartTreeModel> newList = new ArrayList<>();
		if(departList.size() > 0 || sysDepartService != null) {
			for(SysDepart depart : departList) {
				newList.add(new SysDepartTreeModel(depart));
			}
			return newList;
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> queryDepartChildList(String orgType) {
		List<Map<String, Object>> sysDepartList = sysDepartMapper.getDepartList(orgType);
		for (Map<String, Object> map : sysDepartList) {
			String id = (String) map.get("id");
			List<Map<String, Object>> childList = sysDepartMapper.getDepartChildList(id);
			map.put("children", childList);
		}
		return sysDepartList;
	}

	@Override
	public List<SysDepart> queryCurAndChildDepart(String deptId) {
		return sysDepartMapper.queryChildDepart(deptId);
	}

	@Override
	public List<SysDepart> queryAllDepart(String deptId) {
		// 查询本节点及上级节点
		List<SysDepart> sysDepartList = sysDepartMapper.queryParentDepart(deptId);
		// 查询本节点及子节点
		List<SysDepart> curDepartList = sysDepartMapper.queryChildDepart(deptId);
		List<SysDepart> list = new ArrayList<>();
		// 合并
		list.addAll(sysDepartList);
		list.addAll(curDepartList);
		Map<String,Object> map = Maps.newHashMap();
		for (SysDepart depart : list) {
			map.put(depart.getId(),depart);
		}
		// 清除list
		list.clear();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			// 将去重后的对象存放list
			list.add(JsonUtils.jsonToEntity(JsonUtils.objectToJson(entry.getValue()),SysDepart.class));
		}
		return list;
	}
}
