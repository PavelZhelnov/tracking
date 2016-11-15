package com.tmw.tracking.web.aop;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by ankultepin on 21.09.2015.
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface MethodCall {
}
