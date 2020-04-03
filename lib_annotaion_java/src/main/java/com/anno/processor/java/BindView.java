package com.anno.processor.java;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Administrator
 */
@Target(ElementType.FIELD) // 作用在类上
@Retention(RetentionPolicy.CLASS) // 存活时间是编译时
public @interface BindView {
    int value() default 0;
}
