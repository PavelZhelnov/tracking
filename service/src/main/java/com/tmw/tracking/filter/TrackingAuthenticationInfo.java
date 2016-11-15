package com.tmw.tracking.filter;

import com.tmw.tracking.entity.User;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

public class TrackingAuthenticationInfo implements AuthenticationInfo {

    private final User authenticatedUser;
    private final Object credentials;

    public TrackingAuthenticationInfo(final User authenticatedUser, final Object credentials){
        this.authenticatedUser = authenticatedUser;
        this.credentials = credentials;
    }

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }

    @Override
    public PrincipalCollection getPrincipals() {
        return new SimplePrincipalCollection(authenticatedUser, "tracking");
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }


}
