package com.xwtec.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xwtec.modules.system.entity.SysRolePermission;

/**
 * <p>
 * 角色权限表 服务类
 * </p>
 *
 * @author scott
 * @since 2018-12-21
 */
public interface ISysRolePermissionService extends IService<SysRolePermission> {
	
	/**
	 * 保存授权/先删后增
	 * @param roleId
	 * @param permissionIds
	 */
	public void saveRolePermission(String roleId,String permissionIds);

}
