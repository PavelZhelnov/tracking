package com.tmw.tracking;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author dmikhalishin@provectus-it.com
 */
@Target( { METHOD })
@Retention(RUNTIME)
public @interface Transaction {
}
