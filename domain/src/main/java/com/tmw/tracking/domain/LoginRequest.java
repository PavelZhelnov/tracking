package com.tmw.tracking.domain;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author dmikhalishin@provectus-it.com
 */
@XmlRootElement
public class LoginRequest {

    private String userId;
    private String password;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
