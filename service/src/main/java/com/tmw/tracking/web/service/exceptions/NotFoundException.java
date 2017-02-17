package com.tmw.tracking.web.service.exceptions;

import com.tmw.tracking.web.service.util.error.ErrorCode;

/**
 * Created by pzhelnov on 2/16/2017.
 */
public class NotFoundException extends ServiceException {

    public NotFoundException(String message) {
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }
}
