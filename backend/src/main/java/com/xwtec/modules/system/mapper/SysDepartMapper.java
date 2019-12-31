package com.xwtec.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xwtec.modules.system.entity.SysDepart;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门 Mapper 接口
 * <p>
 * 
 * @author： Steve
 * @Since：   2019-01-22
 */
public interface SysDepartMapper extends BaseMapper<SysDepart> {

    List<Map<String,Object>> getDepartList(@Param("orgType") String orgType);

    List<Map<String,Object>> getDepartChildList(@Param("parentId") String parentId);

    List<SysDepart> queryChildDepart(String deptId);
    List<SysDepart> queryParentDepart(String deptId);

    SysDepart selectByDeptName(String deptName);
}
