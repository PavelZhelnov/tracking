package com.tmw.tracking.validation.validator;

import com.tmw.tracking.validation.PhoneFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PhoneFormatValidator implements ConstraintValidator<PhoneFormat, String> {

    public static final Pattern regex = Pattern.compile("^\\(?([0-9]{3})\\)?[-. ]([0-9]{3})[-. ]([0-9]{4})$");
    /**
     * {@inheritDoc}
     * @see ConstraintValidator#initialize(java.lang.annotation.Annotation)
     */
    @Override
    public void initialize(final PhoneFormat s) {
        // do nothing
    }

    /**
     * {@inheritDoc}
     * @see ConstraintValidator#isValid(Object, javax.validation.ConstraintValidatorContext)
     */
    @Override
    public boolean isValid(final String s, final ConstraintValidatorContext constraintValidatorContext) {
        return PhoneFormatValidator.isValid(s);
    }

    /**
     * @param phoneStr - phone number
     * @return - is valid string with phone
     */
    public static boolean isValid(final String phoneStr) {
        return phoneStr== null ? false : regex.matcher(phoneStr).matches();
    }

}
