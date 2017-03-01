package com.tmw.tracking.entity.support;

import com.tmw.tracking.entity.Permission;
import com.tmw.tracking.entity.Role;

import java.util.List;
import java.util.Set;

/**
 * Created by pzhelnov on 2/28/2017.
 */
public class RoleInfo {
    private Role role;
    private Set<Permission> permissions;
    private List<Permission> allPermissions;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissionSet) {
        this.permissions = permissionSet;
    }

    public List<Permission> getAllPermissions() {
        return allPermissions;
    }

    public void setAllPermissions(List<Permission> allPermissions) {
        this.allPermissions = allPermissions;
    }
}
