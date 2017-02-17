package com.tmw.tracking.service.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tmw.tracking.entity.enums.RoleType;
import com.tmw.tracking.service.PermissionService;
import com.tmw.tracking.domain.PermissionType;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

import java.util.*;

@Singleton
public class PermissionServiceImpl implements PermissionService, RolePermissionResolver {
    private static Map<RoleType, Set<PermissionType>> rolePermMap = new LinkedHashMap<RoleType, Set<PermissionType>>();
    private static Map<String, Collection<Permission>> permMap = new HashMap<String, Collection<Permission>>();

    static {
        rolePermMap.put(RoleType.SYSTEM_ADMIN, new HashSet<PermissionType>(Arrays.asList(
                PermissionType.SHOW_SEARCH_ORDER,
                PermissionType.SHOW_USERS,
                PermissionType.ACCESS_ALL_STORES,
                PermissionType.LOGIN_APP,
                PermissionType.LOGIN_WEB,
                PermissionType.JOB_STATUS
        )));

        rolePermMap.put(RoleType.USER, new HashSet<PermissionType>(Arrays.asList(
                PermissionType.SHOW_SEARCH_ORDER,
                PermissionType.ACCESS_ALL_STORES,
                PermissionType.LOGIN_APP,
                PermissionType.LOGIN_WEB
                )));

    }

    @Inject
    public PermissionServiceImpl() {
        init();
    }

    private void init() {
        for (Map.Entry<RoleType, Set<PermissionType>> entry : rolePermMap.entrySet()) {
            RoleType role = entry.getKey();
            Set<PermissionType> permissionTypes = entry.getValue();
            Collection<Permission> permissions = new HashSet<Permission>();
            for (PermissionType permissionType : permissionTypes) {
                permissions.add(new WildcardPermission(permissionType.name()));
            }
            permMap.put(role.name(), permissions);
        }
    }

    @Override
    public Collection<PermissionType> getPermissions(RoleType role) {
        return rolePermMap.get(role);
    }

    @Override
    public Collection<Permission> resolvePermissionsInRole(String roleString) {
        return permMap.get(roleString);
    }
}
