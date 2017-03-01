package com.tmw.tracking.dao;

import com.tmw.tracking.entity.User;

import java.util.List;

public interface UserDao {

    User getById(Long id);

    User getUserByEmail(String email);

    User getAnyUserByEmail(String email);

    User create(User user);

    User update(User user);

    void delete(User user);

    List<User> getAll();

    User clearRoles(String email);

}
