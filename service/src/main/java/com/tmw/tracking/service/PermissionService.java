package com.tmw.tracking.service;

import com.tmw.tracking.domain.PermissionType;
import com.tmw.tracking.entity.enums.RoleType;
import org.apache.shiro.authz.permission.RolePermissionResolver;

import java.util.Collection;

public interface PermissionService extends RolePermissionResolver {

    Collection<PermissionType> getPermissions(RoleType role);

}
