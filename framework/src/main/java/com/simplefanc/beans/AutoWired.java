package com.simplefanc.beans;

import java.lang.annotation.*;

/**
 * 被它注解的属性需要添加对应的依赖
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoWired {
}
