package com.xwtec.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xwtec.modules.system.entity.SysUserRole;
import com.xwtec.modules.system.mapper.SysUserRoleMapper;
import com.xwtec.modules.system.service.ISysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author scott
 * @since 2018-12-21
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;


    @Override
    public int getUserRoleByRoleId(SysUserRole userRole) {
        return sysUserRoleMapper.getUserRoleByRoleId(userRole);
    }
}
