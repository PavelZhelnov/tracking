package com.tmw.tracking.web;

import com.tmw.tracking.service.impl.AuthenticationServiceImpl;
import com.tmw.tracking.web.hibernate.EntityManagerProvider;
import com.tmw.tracking.filter.TrackingSecurityRealm;

/**
 * @author dmikhalishin@provectus-it.com
 */

public class TestTrackingSecurityRealm extends TrackingSecurityRealm {

    @Override
    protected void inject(){
        this.authenticationLogic =  GuiceInstanceHolderTest.getInjector().getInstance(AuthenticationServiceImpl.class);
        this.entityManagerProvider = GuiceInstanceHolderTest.getInjector().getInstance(EntityManagerProvider.class);
    }
}
