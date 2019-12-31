package com.xwtec.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xwtec.common.util.oConvertUtils;
import com.xwtec.modules.system.entity.SysRolePermission;
import com.xwtec.modules.system.mapper.SysRolePermissionMapper;
import com.xwtec.modules.system.service.ISysRolePermissionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 角色权限表 服务实现类
 * </p>
 *
 * @author scott
 * @since 2018-12-21
 */
@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {

    @Override
    public void saveRolePermission(String roleId, String permissionIds) {
        LambdaQueryWrapper<SysRolePermission> query = new QueryWrapper<SysRolePermission>().lambda().eq(SysRolePermission::getRoleId, roleId);
        this.remove(query);
        List<SysRolePermission> list = new ArrayList<SysRolePermission>();
        String arr[] = permissionIds.split(",");
        for (String p : arr) {
            if (oConvertUtils.isNotEmpty(p)) {
                SysRolePermission rolepms = new SysRolePermission(roleId, p);
                list.add(rolepms);
            }
        }
        this.saveBatch(list);
    }

}
