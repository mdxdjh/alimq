package com.ellis.commons.alimq.common;

import java.lang.annotation.*;

/**
 * @author ellis.luo
 * @date 16/11/4 下午7:08
 * @description MQ策略注解配置
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MQAnnotation
{
    String topic() default ""; // MQ Topic
}
