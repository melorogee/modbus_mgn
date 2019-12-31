package com.xwtec.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xwtec.modules.system.entity.SysDepart;
import com.xwtec.modules.system.model.SysDepartTreeModel;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门表 服务实现类
 * <p>
 * 
 * @author：Steve
 * @Since：   2019-01-22
 */
public interface ISysDepartService extends IService<SysDepart>{


    /**
     * 查询所有部门信息,并分节点进行显示
     * @return
     */
    List<SysDepartTreeModel> queryTreeList();

    /**
     * 保存部门数据
     * @param sysDepart
     */
    void saveDepartData(SysDepart sysDepart,String username);

    /**
     * 更新depart数据
     * @param sysDepart
     * @return
     */
    Boolean updateDepartDataById(SysDepart sysDepart,String username);
    
    /**
     * 删除depart数据
     * @param id
     * @return
     */
	/* boolean removeDepartDataById(String id); */
    
    /**
     * 根据关键字搜索相关的部门数据
     * @param keyWord
     * @return
     */
    List<SysDepartTreeModel> searhBy(String keyWord);

    List<Map<String, Object>> queryDepartChildList(String orgType);

    /**
     * queryAllDepart: 当前节点及上下级节点的所有组织 <br/>
     * @auther: Jerry
     * @param: [deptId]
     * @date: 2019/6/4 16:13
     * @return: java.util.List<com.xwtec.modules.system.entity.SysDepart>
     */
    List<SysDepart> queryAllDepart(String deptId);

    /**
     * queryCurAndChildDepart: 当前节点及子节点下的组织 <br/>
     * @auther: Jerry
     * @param: [deptId]
     * @date: 2019/6/4 16:13
     * @return: java.util.List<com.xwtec.modules.system.entity.SysDepart>
     */
    List<SysDepart> queryCurAndChildDepart(String deptId);
}
