package com.tmw.tracking.dao;

import com.tmw.tracking.validation.validator.PhoneFormatValidator;
import com.tmw.tracking.web.TrackingBaseUnitTest;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(TrackingBaseUnitTest.class)
public class PhoneFormatValidatorTest {

    @Test
    public void testPhoneFormatValidation(){
        final PhoneFormatValidator validator = new PhoneFormatValidator();
        Assert.assertFalse(validator.isValid("unknown", null));
        Assert.assertFalse(validator.isValid("123", null));
        Assert.assertTrue(validator.isValid("123-456-7890", null));
    }
}
