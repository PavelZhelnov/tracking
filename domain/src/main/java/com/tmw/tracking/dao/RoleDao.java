package com.tmw.tracking.dao;

import com.tmw.tracking.entity.Role;
import com.tmw.tracking.entity.enums.RoleType;

import java.util.List;
import java.util.Map;

/**
 * {@link Role} DAO
 * @author dmikhalishin@provectus-it.com
 */
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
     * Retrieves the collection of {@link Role Role}
     * @return the collection of {@code Role}
     */
    Map<RoleType,Role> getAllRoleTypes();
    /**
     * Retrieves the collection of {@link Role Role}
     * @return the collection of {@code Role}
     */

    /**
     * Retrieves the {@link Role Role} by ID
     *
     * @param roleType the {@code Role} type. Cannot be {@code null}
     * @return the {@code Role}
     */
    Role getByRoleType(RoleType roleType);
}
