package com.tmw.tracking.dao.impl;

import com.google.inject.Singleton;
import com.tmw.tracking.DomainUtils;
import com.tmw.tracking.Transaction;
import com.tmw.tracking.dao.UserDao;
import com.tmw.tracking.entity.Role;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.entity.enums.RoleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link UserDao} implementation
 *
 * @author dmikhalishin@provectus-it.com
 * @see UserDao
 */
@Singleton
public class UserDaoImpl implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    private EntityManager entityManager;

    @Inject
    public UserDaoImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User getById(final Long id) {
        TypedQuery<User> query = entityManager.createQuery("from User where id = :id and active = true", User.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getUsersByRoleType(final RoleType roleType) {
        if (roleType == null) return new ArrayList<User>();

        final TypedQuery<User> typedQuery = entityManager.createQuery("select distinct u from User u " +
                "left join u.roles r " +
                "where r.type = :roleType and u.active = :active order by u.uniqueId", User.class);
        typedQuery.setParameter("roleType", roleType);
        typedQuery.setParameter("active", true);

        return typedQuery.getResultList();
    }

    @Override
    public User getUserByEmail(final String email) {
        TypedQuery<User> query = entityManager.createQuery("from User where email = :email and active = 'Y'", User.class);
        query.setParameter("email", email);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public User getAnyUserByEmail(final String email) {
        TypedQuery<User> query = entityManager.createQuery("from User where email = :email ", User.class);
        query.setParameter("email", email);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see UserDao#create(User)
     */
    @Transaction
    @Override
    public User create(final User user) {
        //check for unique id
        User existing = getAnyUserByEmail(user.getEmail());
        if (existing != null) {
            throw new RuntimeException("User with the email "+ user.getEmail() + " already exists (it might be inactive)");
        }
        entityManager.persist(user);
        return user;
    }

    /**
     * {@inheritDoc}
     *
     * @see UserDao#create(User)
     */
    @Transaction
    @Override
    public User update(final User user) {
        return entityManager.merge(user);
    }

    /**
     * {@inheritDoc}
     *
     * @see UserDao#create(User)
     */
    @Transaction
    @Override
    public void delete(final User user) {
        user.setActive(false);
        entityManager.merge(user);
    }


    @Override
    public List<User> getAll() {
        final StringBuilder sql = new StringBuilder();
        sql.append("from User ").append(" order by id");
        return DomainUtils.getLimitResult(entityManager, User.class, sql.toString(), null, null);
    }

    @Override
    public User clearRoles(String email) {
        User existing = getAnyUserByEmail(email);
        if (existing == null) {
            return null;
        }
        existing.setRoles(new ArrayList<Role>());
        return update(existing);
    }

}
