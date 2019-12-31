package com.xwtec.common.util;

import com.xwtec.modules.shiro.authc.util.JwtUtil;
import com.xwtec.modules.system.entity.SysUser;
import com.xwtec.modules.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ClassName: SysCacheUtil
 * @Auther: zhuchen
 * @Date: 2019/3/14 13:14
 * @Desctiption: 系统缓存工具类
 * @Version: 1.0
 */
@Component
@Slf4j
public class SysCacheUtil {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ISysUserService sysUserService;
    /**
     * 获取登陆用户userId:  <br/>
     * 根据当前登录人的Token从redis中获取当前登录人的userId
     * 1、当redis中可以取到userId则直接返回
     * 2、当redis中没有取得userId则根据token中的userName获取数据库中的userId
     * 3、当redis中Token为空时则返回空
     * @auther: zhuchen
     * @param: request
     * @date: 2019/3/14 13:39
     * @return: 
     */
    public String getLoginUserId(HttpServletRequest request){
        String accessToken = request.getHeader("X-Access-Token");
        log.info("   request中获取Token："+accessToken);
        if(StringUtils.isNotBlank(accessToken)){
            Object userId = redisUtil.get(accessToken+"LoginUserId");
            // 如果redis中没有当前登录用户的userId 则根据token中的userName获取数据库中是userId
            if(userId==null){
                log.info("   redis中获取userId为空  ======== ");
                String currentUserName = getLoginUserName(request);
                // 查询用户信息
                SysUser sysUser = sysUserService.getUserByName(currentUserName);
                log.info("   根据token中的userName获取数据库中的userId："+sysUser.getId());
                //当前登陆人userId再次存入session
                redisUtil.set(accessToken + "LoginUserId", sysUser.getId());
                //设置超时时间
                redisUtil.expire(accessToken + "LoginUserId", JwtUtil.EXPIRE_TIME/1000);
                return sysUser.getId();
            }
            log.info("   redis中获取userId："+userId.toString());
            return userId.toString();
        }
        log.info("   redis中获取Token为空  ======== ");
        return "";
    }
    /**
     * 获取登陆用户角色信息:  <br/>
     * 步骤同获取登陆用户userId一致
     * @auther: zhuchen
     * @param: 
     * @date: 2019/3/14 14:41
     * @return:   例：{roleId=f6817f48af4fb3af11b9e8bf182f618b, roleCode=admin, userId=e9ca23d68d884d4ebb19d07889727dae}
     */
    public Map<String,String> getLoginRoleInfo(HttpServletRequest request){
        String accessToken = request.getHeader("X-Access-Token");
        log.info("   request中获取Token："+accessToken);
        if(StringUtils.isNotBlank(accessToken)){
            Object loginRoleInfo = redisUtil.get(accessToken+"LoginRoleInfo");
            // 如果redis中没有当前登录用户的loginRoleInfo 则根据token中的userName获取数据库中的LoginRoleInfo
            if(loginRoleInfo==null){
                log.info("   redis中获取userRoleInfo为空  ======== ");
                String currentUserName = getLoginUserName(request);
                // 查询用户角色信息
                Map<String,String> userRole = sysUserService.getRoleInfoByUserName(currentUserName);
                log.info("   根据token中的userName获取数据库中的userRoleInfo："+userRole);
                //当前登陆人userId再次存入session
                redisUtil.set(accessToken + "LoginRoleInfo", userRole);
                //设置超时时间
                redisUtil.expire(accessToken + "LoginRoleInfo", JwtUtil.EXPIRE_TIME/1000);
                return userRole;
            }
            log.info("   redis中获取userRoleInfo："+ (Map<String,String>)loginRoleInfo);
            return (Map<String,String>)loginRoleInfo;
        }
        log.info("   redis中获取Token为空  ======== ");
        return null;
    }
    
    /**
     * 获取登录用户的登录账号userName:  <br/>
     * @auther: zhuchen
     * @param: request
     * @date: 2019/3/14 13:39
     * @return: 
     */
    public static String getLoginUserName(HttpServletRequest request){

        return JwtUtil.getUserNameByToken(request);
    }
}
