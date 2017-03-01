package com.tmw.tracking.service.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tmw.tracking.dao.PermissionDao;
import com.tmw.tracking.dao.RoleDao;
import com.tmw.tracking.entity.Role;
import com.tmw.tracking.service.PermissionService;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Singleton
public class PermissionServiceImpl implements PermissionService, RolePermissionResolver {

    private static Map<String, Collection<Permission>> permMap = new HashMap<String, Collection<Permission>>();
    private PermissionDao permissionDao;
    private RoleDao roleDao;
    /*static {
        rolePermMap.put(RoleType.SYSTEM_ADMIN, new HashSet<PermissionType>(Arrays.asList(
                PermissionType.SHOW_USERS,
                PermissionType.SHOW_ROLES,
                PermissionType.LOGIN_APP,
                PermissionType.JOB_STATUS
        )));

        rolePermMap.put(RoleType.USER, new HashSet<PermissionType>(Arrays.asList(
                PermissionType.LOGIN_APP
                )));

    }*/

    @Inject
    public PermissionServiceImpl(final PermissionDao permissionDao,
                                 final RoleDao roleDao) {
        this.permissionDao = permissionDao;
        this.roleDao = roleDao;
        init();
    }

    private void init() {

        for (Role role : roleDao.getAll()) {
            Collection<Permission> permissions = new HashSet<Permission>();
            for (com.tmw.tracking.entity.Permission permission: role.getPermissionList()) {
                permissions.add(new WildcardPermission(permission.getName().name()));
            }
            permMap.put(role.getRoleName(), permissions);
        }
    }

    @Override
    public Collection<com.tmw.tracking.entity.Permission> getPermissions(Role role) {
        return role.getPermissionList();
    }

    @Override
    public Collection<Permission> resolvePermissionsInRole(String roleString) {
        return permMap.get(roleString);
    }

    @Override
    public List<com.tmw.tracking.entity.Permission> getAllPermissions() {
        return permissionDao.getAllPermissions();
    }
}
