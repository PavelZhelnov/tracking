package com.tmw.tracking.web.service.util.response;

import javax.xml.bind.annotation.XmlRootElement;
import java.lang.*;

/**
 * Created by pzhelnov on 2/16/2017.
 */
@XmlRootElement
public class ServiceResponse {

    private Status status;
    private Error error;
    private Object data;

    public ServiceResponse(){}

    public ServiceResponse(final Status status, final Object data){
        this.status = status;
        this.error = null;
        this.data = data;
    }

    public ServiceResponse(final Status status, final Error error){
        this.status = status;
        this.error = error;
        this.data = null;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}