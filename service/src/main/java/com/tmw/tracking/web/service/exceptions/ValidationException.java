package com.tmw.tracking.web.service.exceptions;

import com.tmw.tracking.web.service.util.error.ErrorCode;

/**
 * Created by pzhelnov on 2/16/2017.
 */
public class ValidationException extends ServiceException {


    public ValidationException(String message) {
        super(message, ErrorCode.PARAMETER_IS_INVALID);
    }
}
