package com.tmw.tracking.dao;

import com.tmw.tracking.entity.Role;
import java.util.List;

public interface RoleDao {

    /**
     * Retrieves the {@link Role Role} by ID
     * @param id the {@code Role} id. Cannot be {@code null}
     * @return the {@code Role}
     */
    Role getById(Long id);

    /**
     * Retrieves the collection of {@link Role Role}
     * @return the collection of {@code Role}
     */
    List<Role> getAll();

    /**
     * Retrieves the {@link Role Role} by ID
     *
     * @param roleType the {@code Role} type. Cannot be {@code null}
     * @return the {@code Role}
     */
    Role getByRoleName(String roleName);

    Role update(Role role);

    void delete(Role role);
}
