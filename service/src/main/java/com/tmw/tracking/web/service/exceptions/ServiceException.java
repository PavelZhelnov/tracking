package com.tmw.tracking.web.service.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by pzhelnov on 11/11/2016.
 */
public class ServiceException extends WebApplicationException {
    private boolean important;

    public ServiceException(final String message) {
        this(message, true);
    }

    public ServiceException(final String message, final boolean important) {
        this(message, new Exception(message));

    }

    public ServiceException(final String message, Throwable throwable) {
        super(throwable, Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(message).type(MediaType.TEXT_PLAIN).build());
    }

    public boolean isImportant() {
        return important;
    }
}
