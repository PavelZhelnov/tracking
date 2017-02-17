package com.tmw.tracking.dao;

import com.tmw.tracking.entity.AuthenticatedUser;
import com.tmw.tracking.entity.User;

public interface AuthenticatedUserDao {
    /**
     * Retrieves the {@link AuthenticatedUser} by {@link User}
     * @param user the {@code User}. Cannot be {@code null}.
     * @return {@code AuthenticatedUser} object
     */
    AuthenticatedUser getAuthenticatedUser(User user);
    /**
     * Retrieves the {@link AuthenticatedUser} by token
     * @param token the unique token. Cannot be {@code null}.
     * @return {@code AuthenticatedUser} object
     */
    AuthenticatedUser getAuthenticatedUserByToken(String token);
    /**
     * Create the {@link AuthenticatedUser}
     * @param authenticatedUser the {@code AuthenticatedUser} that should be created. Cannot be {@code null}.
     */
    void create(AuthenticatedUser authenticatedUser);
    /**
     * Update the {@link AuthenticatedUser}
     * @param authenticatedUser the {@code AuthenticatedUser} that should be updated. Cannot be {@code null}.
     */
    void update(AuthenticatedUser authenticatedUser);
    /**
     * Delete the {@link AuthenticatedUser}
     * @param authenticatedUser the {@code AuthenticatedUser} that should be deleted. Cannot be {@code null}.
     */
    void delete(AuthenticatedUser authenticatedUser);
}
