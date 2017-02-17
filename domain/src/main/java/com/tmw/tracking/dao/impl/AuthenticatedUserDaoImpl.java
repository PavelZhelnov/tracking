package com.tmw.tracking.dao.impl;

import com.google.inject.Singleton;
import com.tmw.tracking.Transaction;
import com.tmw.tracking.dao.AuthenticatedUserDao;
import com.tmw.tracking.entity.AuthenticatedUser;
import com.tmw.tracking.entity.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.Date;

@Singleton
public class AuthenticatedUserDaoImpl implements AuthenticatedUserDao{
    private EntityManager entityManager;
    @Inject
    public AuthenticatedUserDaoImpl(final EntityManager entityManager){
        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     * @see AuthenticatedUserDao#getAuthenticatedUser(User)
     */
    @Override
    public AuthenticatedUser getAuthenticatedUser(final User user){
        final Query query = entityManager.createQuery("from AuthenticatedUser where user.id = :userId and expired > :expired ");
        query.setParameter("userId", user.getId());
        query.setParameter("expired", new Date());
        try {
            return (AuthenticatedUser)query.getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * @see AuthenticatedUserDao#getAuthenticatedUser(User)
     */
    @Override
    public AuthenticatedUser getAuthenticatedUserByToken(final String token) {
        final Query query = entityManager.createQuery("from AuthenticatedUser where token = :token");
        query.setParameter("token", token);
        try {
            return (AuthenticatedUser)query.getSingleResult();
        } catch (NoResultException e){
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * @see AuthenticatedUserDao#create(AuthenticatedUser)
     */
    @Transaction
    @Override
    public void create(final AuthenticatedUser authenticatedUser) {
        entityManager.persist(authenticatedUser);
    }

    /**
     * {@inheritDoc}
     * @see AuthenticatedUserDao#update(AuthenticatedUser)
     */
    @Transaction
    @Override
    public void update(final AuthenticatedUser authenticatedUser){
        entityManager.merge(authenticatedUser);
    }

    /**
     * {@inheritDoc}
     * @see AuthenticatedUserDao#delete(AuthenticatedUser)
     */
    @Transaction
    @Override
    public void delete(final AuthenticatedUser authenticatedUser) {
        entityManager.remove(authenticatedUser);
    }
}
