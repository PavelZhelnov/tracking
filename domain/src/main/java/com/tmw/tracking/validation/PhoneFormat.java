package com.tmw.tracking.validation;

import com.tmw.tracking.validation.validator.PhoneFormatValidator;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( { FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = PhoneFormatValidator.class)
public @interface PhoneFormat {

    String message() default "Phone number must have XXX-XXX-XXXX structure";
    java.lang.Class<?>[] groups() default {};
    java.lang.Class<? extends javax.validation.Payload>[] payload() default {};
}
