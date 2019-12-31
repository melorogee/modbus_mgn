package com.xwtec.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xwtec.modules.system.entity.SysUserRole;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author scott
 * @since 2018-12-21
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    int getUserRoleByRoleId(SysUserRole userRole);
}
