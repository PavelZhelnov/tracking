package com.tmw.tracking.guice;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.tmw.tracking.Transaction;
import com.tmw.tracking.dao.AuthenticatedUserDao;
import com.tmw.tracking.dao.ContainerTypeDao;
import com.tmw.tracking.dao.JobStatusInfoDao;
import com.tmw.tracking.dao.PermissionDao;
import com.tmw.tracking.dao.RoleDao;
import com.tmw.tracking.dao.TrackingSiteDao;
import com.tmw.tracking.dao.UserDao;
import com.tmw.tracking.dao.impl.AuthenticatedUserDaoImpl;
import com.tmw.tracking.dao.impl.ContainerTypeDaoImpl;
import com.tmw.tracking.dao.impl.DriverDao;
import com.tmw.tracking.dao.impl.DriverDaoImpl;
import com.tmw.tracking.dao.impl.JobStatusInfoDaoImpl;
import com.tmw.tracking.dao.impl.PermissionDaoImpl;
import com.tmw.tracking.dao.impl.RoleDaoImpl;
import com.tmw.tracking.dao.impl.TrackingSiteDaoImpl;
import com.tmw.tracking.dao.impl.TransactionDao;
import com.tmw.tracking.dao.impl.TransactionDaoImpl;
import com.tmw.tracking.dao.impl.UserDaoImpl;
import com.tmw.tracking.service.PermissionService;
import com.tmw.tracking.service.impl.PermissionServiceImpl;
import com.tmw.tracking.utils.Utils;
import com.tmw.tracking.web.aop.TransactionalInterceptor;
import com.tmw.tracking.web.hibernate.EntityManagerProvider;
import com.tmw.tracking.web.hibernate.EntityManagerWrapper;

import javax.persistence.EntityManager;

public class DaoModule extends AbstractModule {

    /**
     * {@inheritDoc}
     * @see com.google.inject.AbstractModule#configure()
     */
    @Override
    protected void configure() {
        final EntityManagerProvider entityManagerProvider = new EntityManagerProvider(Utils.PERSIST_MODULE_NAME);

        bind(EntityManagerProvider.class).toInstance(entityManagerProvider);
        final TransactionalInterceptor interceptor = new TransactionalInterceptor(entityManagerProvider);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transaction.class), interceptor);
        bind(EntityManager.class).to(EntityManagerWrapper.class);
        // dao
        bind(AuthenticatedUserDao.class).to(AuthenticatedUserDaoImpl.class);
        bind(JobStatusInfoDao.class).to(JobStatusInfoDaoImpl.class);
        bind(UserDao.class).to(UserDaoImpl.class);
        bind(PermissionService.class).to(PermissionServiceImpl.class);
        bind(RoleDao.class).to(RoleDaoImpl.class);
        bind(TrackingSiteDao.class).to(TrackingSiteDaoImpl.class);
        bind(DriverDao.class).to(DriverDaoImpl.class);
        bind(TransactionDao.class).to(TransactionDaoImpl.class);
        bind(PermissionDao.class).to(PermissionDaoImpl.class);
        bind(ContainerTypeDao.class).to(ContainerTypeDaoImpl.class);

    }
}
