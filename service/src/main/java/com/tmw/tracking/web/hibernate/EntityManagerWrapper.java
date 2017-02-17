package com.tmw.tracking.web.hibernate;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class EntityManagerWrapper implements EntityManager {

    private final EntityManagerProvider entityManagerProvider;

    @Inject
    public EntityManagerWrapper(EntityManagerProvider entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    /**
     * {@inheritDoc}
     * @see EntityManager#persist(Object)
     */
    @Override
    public void persist(final Object entity) {
        entityManagerProvider.getEntityManager().persist(entity);
    }

    /**
     * {@inheritDoc}
     * @see EntityManager#merge(Object)
     */
    @Override
    public <T> T merge(final T entity) {
        return entityManagerProvider.getEntityManager().merge(entity);
    }

    /**
     * {@inheritDoc}
     * @see EntityManager#remove(Object)
     */
    @Override
    public void remove(final Object entity) {
        entityManagerProvider.getEntityManager().remove(entity);
    }

    /**
     * {@inheritDoc}
     * @see EntityManager#find(Class, Object)
     */
    @Override
    public <T> T find(final Class<T> entityClass, final Object primaryKey) {
        return entityManagerProvider.getEntityManager().find(entityClass, primaryKey);
    }

    /**
     * {@inheritDoc}
     * @see EntityManager#find(Class, Object, java.util.Map)
     */
    @Override
    public <T> T find(final Class<T> entityClass, final Object primaryKey, final Map<String, Object> properties) {
        return entityManagerProvider.getEntityManager().find(entityClass, primaryKey, properties);
    }

    /**
     * {@inheritDoc}
     * @see EntityManager#find(Class, Object, javax.persistence.LockModeType)
     */
    @Override
    public <T> T find(final Class<T> entityClass, final Object primaryKey, final LockModeType lockMode) {
        return entityManagerProvider.getEntityManager().find(entityClass, primaryKey, lockMode);
    }

    /**
     * {@inheritDoc}
     * @see EntityManager#find(Class, Object, java.util.Map)
     */
    @Override
    public <T> T find(final Class<T> entityClass, final Object primaryKey, final LockModeType lockMode, final Map<String, Object> properties) {
        return entityManagerProvider.getEntityManager().find(entityClass, primaryKey, lockMode, properties);
    }

    /**
     * {@inheritDoc}
     * @see EntityManager#getReference(Class, Object)
     */
    @Override
    public <T> T getReference(final Class<T> entityClass, final Object primaryKey) {
        return entityManagerProvider.getEntityManager().getReference(entityClass, primaryKey);
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#flush()
     */
    @Override
    public void flush() {
        entityManagerProvider.getEntityManager().flush();
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#setFlushMode(javax.persistence.FlushModeType)
     */
    @Override
    public void setFlushMode(final FlushModeType flushMode) {
        entityManagerProvider.getEntityManager().setFlushMode(flushMode);
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#getFlushMode()
     */
    @Override
    public FlushModeType getFlushMode() {
        return entityManagerProvider.getEntityManager().getFlushMode();
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#lock(Object, javax.persistence.LockModeType)
     */
    @Override
    public void lock(final Object entity, final LockModeType lockMode) {
        entityManagerProvider.getEntityManager().lock(entity, lockMode);
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#lock(Object, javax.persistence.LockModeType, java.util.Map)
     */
    @Override
    public void lock(final Object entity, final LockModeType lockMode, final Map<String, Object> properties) {
        entityManagerProvider.getEntityManager().lock(entity, lockMode, properties);
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#refresh(Object)
     */
    @Override
    public void refresh(final Object entity) {
        entityManagerProvider.getEntityManager().refresh(entity);
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#refresh(Object, java.util.Map)
     */
    @Override
    public void refresh(final Object entity, final Map<String, Object> properties) {
        entityManagerProvider.getEntityManager().refresh(entity, properties);
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#refresh(Object, javax.persistence.LockModeType)
     */
    @Override
    public void refresh(final Object entity,final LockModeType lockMode) {
        entityManagerProvider.getEntityManager().refresh(entity, lockMode);
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#refresh(Object, javax.persistence.LockModeType, java.util.Map)
     */
    @Override
    public void refresh(final Object entity, final LockModeType lockMode, final Map<String, Object> properties) {
        entityManagerProvider.getEntityManager().refresh(entity, lockMode, properties);
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#clear()
     */
    @Override
    public void clear() {
        entityManagerProvider.getEntityManager().clear();
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#detach(Object)
     */
    @Override
    public void detach(final Object entity) {
        entityManagerProvider.getEntityManager().detach(entity);
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#contains(Object)
     */
    @Override
    public boolean contains(final Object entity) {
        return entityManagerProvider.getEntityManager().contains(entity);
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#getLockMode(Object)
     */
    @Override
    public LockModeType getLockMode(final Object entity) {
        return entityManagerProvider.getEntityManager().getLockMode(entity);
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#setProperty(String, Object)
     */
    @Override
    public void setProperty(final String propertyName, final Object value) {
        entityManagerProvider.getEntityManager().setProperty(propertyName, value);
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#getProperties()
     */
    @Override
    public Map<String, Object> getProperties() {
        return entityManagerProvider.getEntityManager().getProperties();
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#createQuery(String)
     */
    @Override
    public Query createQuery(final String qlString) {
        return entityManagerProvider.getEntityManager().createQuery(qlString);
    }
    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#createQuery(javax.persistence.criteria.CriteriaQuery)
     */
    @Override
    public <T> TypedQuery<T> createQuery(final CriteriaQuery<T> criteriaQuery) {
        return entityManagerProvider.getEntityManager().createQuery(criteriaQuery);
    }
    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#createQuery(String, Class)
     */
    @Override
    public <T> TypedQuery<T> createQuery(final String qlString, final Class<T> resultClass) {
        return entityManagerProvider.getEntityManager().createQuery(qlString, resultClass);
    }
    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#createNamedQuery(String)
     */
    @Override
    public Query createNamedQuery(final String name) {
        return entityManagerProvider.getEntityManager().createNamedQuery(name);
    }
    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#createQuery(String, Class)
     */
    @Override
    public <T> TypedQuery<T> createNamedQuery(final String name, final Class<T> resultClass) {
        return entityManagerProvider.getEntityManager().createNamedQuery(name, resultClass);
    }
    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#createNativeQuery(String)
     */
    @Override
    public Query createNativeQuery(final String sqlString) {
        return entityManagerProvider.getEntityManager().createNativeQuery(sqlString);
    }
    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#createNativeQuery(String, Class)
     */
    @Override
    public Query createNativeQuery(final String sqlString, final Class resultClass) {
        return entityManagerProvider.getEntityManager().createNativeQuery(sqlString, resultClass);
    }
    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#createNativeQuery(String, String)
     */
    @Override
    public Query createNativeQuery(final String sqlString, final String resultSetMapping) {
        return entityManagerProvider.getEntityManager().createNativeQuery(sqlString, resultSetMapping);
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#joinTransaction()
     */
    @Override
    public void joinTransaction() {
        entityManagerProvider.getEntityManager().joinTransaction();
    }
    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#unwrap(Class)
     */
    @Override
    public <T> T unwrap(final Class<T> cls) {
        return entityManagerProvider.getEntityManager().unwrap(cls);
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#getDelegate()
     */
    @Override
    public Object getDelegate() {
        return entityManagerProvider.getEntityManager().getDelegate();
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#close()
     */
    @Override
    public void close() {
        //entityManagerProvider.getEntityManager().close();
        // reporting usage of close methods:
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "EntityManager.close() method is used", new Exception());
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#isOpen()
     */
    @Override
    public boolean isOpen() {
        return entityManagerProvider.getEntityManager().isOpen();
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#getTransaction()
     */
    @Override
    public EntityTransaction getTransaction() {
        //Logger.getLogger(getClass().getName()).log(Level.WARNING, "EntityManager.getTransaction() method is used", new Exception());
        return entityManagerProvider.getEntityManager().getTransaction();
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#getEntityManagerFactory()
     */
    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerProvider.getEntityManager().getEntityManagerFactory();
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#getCriteriaBuilder()
     */
    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return entityManagerProvider.getEntityManager().getCriteriaBuilder();
    }

    /**
     * {@inheritDoc}
     * @see javax.persistence.EntityManager#getMetamodel()
     */
    @Override
    public Metamodel getMetamodel() {
        return entityManagerProvider.getEntityManager().getMetamodel();
    }

}
