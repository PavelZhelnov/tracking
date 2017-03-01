package com.tmw.tracking.service;

import com.tmw.tracking.entity.Permission;
import com.tmw.tracking.entity.Role;
import org.apache.shiro.authz.permission.RolePermissionResolver;

import java.util.Collection;
import java.util.List;

public interface PermissionService extends RolePermissionResolver {

    Collection<Permission> getPermissions(Role role);

    List<Permission> getAllPermissions();

}
