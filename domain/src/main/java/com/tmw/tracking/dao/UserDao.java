package com.tmw.tracking.dao;

import com.tmw.tracking.entity.User;
import com.tmw.tracking.entity.enums.RoleType;

import java.util.List;

/**
 * {@link User} DAO
 * @author dmikhalishin@provectus-it.com
 */
public interface UserDao {

    User getById(Long id);

    List<User> getUsersByRoleType(RoleType roleType);

    User getUserByEmail(String email);

    User getAnyUserByEmail(String email);

    User create(User user);

    User update(User user);

    void delete(User user);

    List<User> getAll();

    User clearRoles(String email);

}
