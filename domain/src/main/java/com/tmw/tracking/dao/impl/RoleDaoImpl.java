package com.tmw.tracking.dao.impl;

import com.google.inject.Singleton;
import com.tmw.tracking.Transaction;
import com.tmw.tracking.dao.RoleDao;
import com.tmw.tracking.entity.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

@Singleton
public class RoleDaoImpl implements RoleDao {

    private final static Logger logger = LoggerFactory.getLogger(RoleDao.class);

    private EntityManager entityManager;

    @Inject
    public RoleDaoImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     *
     * @see RoleDao#getById(Long)
     */
    @Override
    public Role getById(final Long id) {
        final TypedQuery<Role> query = entityManager.createQuery("from Role where id = :id", Role.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Role getByRoleName(String roleName) {
        final TypedQuery<Role> query = entityManager.createQuery("from Role where roleName = :roleName", Role.class);
        query.setParameter("roleName", roleName);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transaction
    @Override
    public Role update(Role role) {
        if (role.getId() == null) {
            entityManager.persist(role);
        } else {
            entityManager.merge(role);
        }
        return role;
    }

    @Transaction
    @Override
    public void delete(Role role) {
        entityManager.remove(role);
    }

    /**
     * {@inheritDoc}
     *
     * @see RoleDao#getAll()
     */
    @Override
    public List<Role> getAll() {
        final TypedQuery<Role> query = entityManager.createQuery("from Role order by roleName", Role.class);
        return query.getResultList();
    }

}
