package com.xwtec.common.aspect.annotation;

import com.xwtec.common.aspect.ParameterType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoCatch {
    //处理参数
    ParameterType type() default ParameterType.NONE;

    //操作类型
    String operationType() default "";
}
