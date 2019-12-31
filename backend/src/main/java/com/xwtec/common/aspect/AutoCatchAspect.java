package com.xwtec.common.aspect;

import com.xwtec.common.api.vo.Result;
import com.xwtec.common.aspect.annotation.AutoCatch;
import com.xwtec.common.exception.VerificationException;
import com.xwtec.common.util.LocalDateUtils;
import com.xwtec.common.util.SysCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class AutoCatchAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SysCacheUtil sysCacheUtil;

    @Pointcut("@annotation(com.xwtec.common.aspect.annotation.AutoCatch)")
    public void catchPointCut() {

    }

    @Around("catchPointCut()")
    public Result around(ProceedingJoinPoint point) throws Exception{
        //处理注解参数
        annParaHandler(point);
        //执行方法
        Result result;
        try {
            result = (Result) point.proceed();
        } catch (Throwable throwable) {
            log.error("操作失败：", throwable);
            if (throwable instanceof VerificationException)
                result = Result.getResultForError((VerificationException) throwable);
            else
                result = Result.getResultForError();
        }
        return result;
    }

    /**
     * 处理注解参数
     * @param point
     * @throws NoSuchMethodException
     */
    private void annParaHandler(ProceedingJoinPoint point) throws Exception {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Object target = point.getTarget();
        Method method = target.getClass().getMethod(signature.getName(), signature.getParameterTypes());
        AutoCatch annotation = method.getAnnotation(AutoCatch.class);
        ParameterType type = annotation.type();
        String operationType = annotation.operationType();
        if (type == ParameterType.USER) {
            setUserInfo(point,operationType);
        }
    }

    /**
     * 加入user信息
     * @param joinPoint
     */
    private void setUserInfo(JoinPoint joinPoint,String operationType) throws Exception {
        Object[] args = joinPoint.getArgs();
        String loginUserId = sysCacheUtil.getLoginUserId(request);
        String roleId = sysCacheUtil.getLoginRoleInfo(request).get("roleId");
        if (args[0] instanceof Map) {
            Map map = (Map) args[0];
            map.put("userId", loginUserId);
            map.put("updateUserId", loginUserId);
            map.put("roleId", roleId);
            map.put("createTime", LocalDateUtils.nowTimeFormatDefault());
            map.put("updateTime", LocalDateUtils.nowTimeFormatDefault());
        } else {
            //实体类分支
            Object obj = args[0];
            if ("1".equals(operationType)) {
                //新增
                Method setCreateBy = obj.getClass().getMethod("setCreateBy", String.class);
                Method setCreateTime = obj.getClass().getMethod("setCreateTime", String.class);
                setCreateBy.invoke(obj, loginUserId);
                setCreateTime.invoke(obj, LocalDateUtils.nowTimeFormatDefault());
            } else {
                //更新
                Method setUpdateBy = obj.getClass().getMethod("setUpdateBy", String.class);
                Method setUpdateTime = obj.getClass().getMethod("setUpdateTime", String.class);
                setUpdateBy.invoke(obj, loginUserId);
                setUpdateTime.invoke(obj, LocalDateUtils.nowTimeFormatDefault());
            }
        }


    }
}
