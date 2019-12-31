package com.xwtec.common.util;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统权限工具类:  <br/>
 * @ClassName: SysPowerUtils
 * @Auther: zhuchen
 * @Date: 2019/3/28 10:43
 * @Desctiption: TODO
 * @Version: 1.0
 */
public class SysPowerUtils {
    /**
     * 根据角色Code返回自定义权限:  <br/>
     * @auther: zhuchen
     * @param: map 入参
     * @date: 2019/3/28 11:10
     * @return:
     */
    public static String getRolePowerCode(String roleCode){

        return  getHomePowerMapByRoleCode(roleCode);
    }
    /**
     * 角色首页权限:  <br/>
     * @auther: zhuchen
     * @param: 
     * @date: 2019/3/28 11:12
     * @return: 
     */
    private static String getHomePowerMapByRoleCode(String roleCode){
        Map<String,String> map = new HashMap<>();
        map.put("admin","1");
        map.put("sysAdmin","1");
        map.put("000001","1");
        map.put("000002","2");
        map.put("000003","3");

        // ----------------------
        return  map.get(roleCode);
    }
}
