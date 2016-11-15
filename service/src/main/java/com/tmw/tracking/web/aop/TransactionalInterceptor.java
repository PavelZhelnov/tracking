package com.tmw.tracking.web.aop;

import com.tmw.tracking.web.hibernate.EntityManagerProvider;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

/**
 * Transactional Interceptor
 * @author dmikhalishin@provectus-it.com
 * @see MethodInterceptor
 */
public class TransactionalInterceptor implements MethodInterceptor {

    public static final int TIMEOUT = Integer.valueOf(System.getProperty("tracking.transaction.timeout", "-1"));  //seconds
    protected final static Logger logger = LoggerFactory.getLogger(TransactionalInterceptor.class);

    private final EntityManagerProvider entityManagerProvider;

    public TransactionalInterceptor(final EntityManagerProvider entityManagerProvider){
        this.entityManagerProvider = entityManagerProvider;
    }

    /**
     * {@inheritDoc}
     * @see MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    @Override
    public Object invoke(final MethodInvocation mi) throws Throwable {
        final EntityManager entityManager = entityManagerProvider.getEntityManager();
        if(TIMEOUT>0)  entityManager.unwrap(Session.class).getTransaction().setTimeout(TIMEOUT);
        boolean transactionAlreadyStarted = entityManager.getTransaction().isActive();
        if(!transactionAlreadyStarted)
            entityManager.getTransaction().begin();
        try {
            final Object result = mi.proceed();
            if(!transactionAlreadyStarted)
                entityManager.getTransaction().commit();
            return result;
        } catch (Throwable e) {
            if(entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
                entityManager.close();
            }
            throw e;
        }
    }

}
