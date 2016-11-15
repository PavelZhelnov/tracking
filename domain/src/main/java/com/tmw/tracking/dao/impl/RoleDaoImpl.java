package com.tmw.tracking.dao.impl;

import com.google.inject.Singleton;
import com.tmw.tracking.dao.RoleDao;
import com.tmw.tracking.entity.Role;
import com.tmw.tracking.entity.enums.RoleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link RoleDao} implementation
 *
 * @author dmikhalishin@provectus-it.com
 * @see RoleDao
 */
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

    /**
     * {@inheritDoc}
     *
     * @see RoleDao#getById(Long)
     */
    @Override
    public Role getByRoleType(final RoleType roleType) {
        final TypedQuery<Role> query = entityManager.createQuery("from Role where type = :type", Role.class);
        query.setParameter("type", roleType);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see RoleDao#getAll()
     */
    @Override
    public List<Role> getAll() {
        final TypedQuery<Role> query = entityManager.createQuery("from Role order by type", Role.class);
        return query.getResultList();
    }

    @Override
    public Map<RoleType, Role> getAllRoleTypes() {
        List<Role> roles = getAll();
        Map<RoleType, Role> map = new HashMap<RoleType, Role>(roles.size());
        for (Role role : roles) {
            map.put(role.getType(), role);
        }
        return map;
    }
}
