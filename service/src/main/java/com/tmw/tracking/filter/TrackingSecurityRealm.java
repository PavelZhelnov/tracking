package com.tmw.tracking.filter;

import com.tmw.tracking.entity.Role;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.service.AuthenticationService;
import com.tmw.tracking.service.impl.AuthenticationServiceImpl;
import com.tmw.tracking.service.PermissionService;
import com.tmw.tracking.utils.GuiceInstanceHolder;
import com.tmw.tracking.utils.HexGenerator;
import com.tmw.tracking.web.hibernate.EntityManagerProvider;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TrackingSecurityRealm extends AuthorizingRealm {

    protected AuthenticationService authenticationLogic;
    protected EntityManagerProvider entityManagerProvider;
    protected PermissionService permissionManager;

    public TrackingSecurityRealm() {
        super();
        setAuthenticationTokenClass(AuthenticationToken.class);
        inject();
    }


    protected void inject(){
        this.authenticationLogic =  GuiceInstanceHolder.getInjector().getInstance(AuthenticationServiceImpl.class);
        this.entityManagerProvider = GuiceInstanceHolder.getInjector().getInstance(EntityManagerProvider.class);
        permissionManager = GuiceInstanceHolder.getInjector().getInstance(PermissionService.class);
        setRolePermissionResolver(permissionManager);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token) throws AuthenticationException {
        entityManagerProvider.create();
        try {
            if(token instanceof UsernamePasswordToken) {
                final UsernamePasswordToken upToken = (UsernamePasswordToken) token;
                final String username = upToken.getUsername() != null ? upToken.getUsername().toUpperCase() : "";
                final String password = upToken.getPassword() != null ? String.valueOf(upToken.getPassword()) : null;
                final User user = authenticationLogic.createOrUpdateUser(username, password);
                return new TrackingAuthenticationInfo(user, HexGenerator.md5(password));
            } else if(token instanceof TrackingAuthenticationToken){
                return new TrackingAuthenticationInfo(authenticationLogic.validateUser((String)token.getCredentials()), token.getCredentials());
            }
            return null;
        }  finally {
            entityManagerProvider.close();
        }
    }

    /**
     * {@inheritDoc}
     * @see org.apache.shiro.realm.jdbc.JdbcRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
        User user = (User) principals.getPrimaryPrincipal();
        Set<String>roleSet = new HashSet<String>();
        Collection<Role> roles = user.getRoles();
        if(roles != null) {
            for(Role role : roles) {
                roleSet.add(role.getType().name());
            }
        }
        return new SimpleAuthorizationInfo(roleSet);
    }




}
