package com.tmw.tracking.dao;

import com.tmw.tracking.domain.PermissionType;
import com.tmw.tracking.entity.Permission;

import java.util.List;

/**
 * Created by pzhelnov on 2/21/2017.
 */
public interface PermissionDao {

    Permission getByPermissionType(PermissionType permissionType);

    List<Permission> getAllPermissions();
}
