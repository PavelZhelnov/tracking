package com.tmw.tracking.web.service.util.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.tmw.tracking.utils.Utils;
import com.tmw.tracking.web.service.exceptions.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.StringWriter;

/**
 * Created by pzhelnov on 2/16/2017.
 */
public class ResponseFilter implements ContainerResponseFilter {
    private final static Logger logger = LoggerFactory.getLogger(ResponseFilter.class);
    /**
     * {@inheritDoc}
     * @see ContainerResponseFilter#filter(com.sun.jersey.spi.container.ContainerRequest, com.sun.jersey.spi.container.ContainerResponse)
     */
    @Override
    public ContainerResponse filter(final ContainerRequest containerRequest, final ContainerResponse containerResponse) {
        if(containerResponse != null && (containerResponse.getStatus() == Response.Status.OK.getStatusCode() || containerResponse.getStatus() == Response.Status.NO_CONTENT.getStatusCode()))
            updateResponse(containerResponse);
        else if(containerResponse != null && containerResponse.getStatus() != 200)
            processError(containerResponse);
        return containerResponse;
    }

    /**
     * Convert service result to {@link ServiceResponse}
     * @param containerResponse the {@code ContainerResponse} object. Cannot be {@code null}.
     */
    private void updateResponse(final ContainerResponse containerResponse){
        if(containerResponse == null || (containerResponse.getStatus() != Response.Status.OK.getStatusCode() && containerResponse.getStatus() != Response.Status.NO_CONTENT.getStatusCode()))return;
        final ObjectMapper objectMapper = Utils.initObjectMapper();
        final StringWriter stringWriter = new StringWriter();
        try {
            objectMapper.writeValue(stringWriter, new ServiceResponse(com.tmw.tracking.web.service.util.response.Status.DONE, containerResponse.getEntity()));
            containerResponse.setEntity(stringWriter.toString());
            containerResponse.setStatusType(Response.Status.OK);
        }catch (Exception e){
            logger.error(Utils.errorToString(e));
            throw  new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Convert {@link ServiceException} to {@link ServiceResponse}
     * @param containerResponse the {@code ContainerResponse} object. Cannot be {@code null}.
     */
    private void processError(final ContainerResponse containerResponse){
        if(containerResponse == null || containerResponse.getMappedThrowable() == null
                || !(containerResponse.getMappedThrowable() instanceof ServiceException))return;
        final ObjectMapper objectMapper = Utils.initObjectMapper();
        final StringWriter stringWriter = new StringWriter();
        try {
            final ServiceException error = (ServiceException)containerResponse.getMappedThrowable();
            objectMapper.writeValue(stringWriter, new ServiceResponse(com.tmw.tracking.web.service.util.response.Status.ERROR, new com.tmw.tracking.web.service.util.response.Error((String)error.getResponse().getEntity(), error.getCode())));
            containerResponse.setEntity(stringWriter.toString());
            containerResponse.setStatusType(Response.Status.OK);
        }catch (Exception e){
            logger.error(Utils.errorToString(e));
            throw  new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }


}