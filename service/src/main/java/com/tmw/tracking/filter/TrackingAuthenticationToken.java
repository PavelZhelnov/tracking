package com.tmw.tracking.filter;

import org.apache.shiro.authc.AuthenticationToken;

public class TrackingAuthenticationToken implements AuthenticationToken {

    private final String token;

    public TrackingAuthenticationToken(final String token){
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
