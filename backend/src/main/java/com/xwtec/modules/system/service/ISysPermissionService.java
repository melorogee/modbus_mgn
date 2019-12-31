package com.xwtec.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xwtec.common.exception.BaseException;
import com.xwtec.modules.system.entity.SysPermission;
import com.xwtec.modules.system.model.TreeModel;

import java.util.List;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author scott
 * @since 2018-12-21
 */
public interface ISysPermissionService extends IService<SysPermission> {
	
	public List<TreeModel> queryListByParentId(String parentId);
	
	/**真实删除*/
	public void deletePermission(String id) throws BaseException;
	/**逻辑删除*/
	public void deletePermissionLogical(String id) throws BaseException;
	
	public void addPermission(SysPermission sysPermission) throws BaseException;
	
	public void editPermission(SysPermission sysPermission) throws BaseException;
	
	public List<SysPermission> queryByUser(String username);

	/**
	 *  查询用户按钮权限
	 * @param username
	 * @return
	 */
	public List<String> queryByUserBtn(String username);
}
