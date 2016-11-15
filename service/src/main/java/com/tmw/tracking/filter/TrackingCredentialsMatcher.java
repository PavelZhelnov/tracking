package com.tmw.tracking.filter;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

public class TrackingCredentialsMatcher extends HashedCredentialsMatcher {

    /**
     * {@inheritDoc}
     * @see HashedCredentialsMatcher#doCredentialsMatch(org.apache.shiro.authc.AuthenticationToken, org.apache.shiro.authc.AuthenticationInfo)
     */
    @Override
    public boolean doCredentialsMatch(final AuthenticationToken token, final AuthenticationInfo info) {
        if(token instanceof TrackingAuthenticationToken && info != null){
            return token.getCredentials().equals(info.getCredentials());
        } else {
            return super.doCredentialsMatch(token, info);
        }
    }
}
