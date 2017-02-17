package com.tmw.tracking.web.hibernate;

import org.junit.Before;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import static org.easymock.EasyMock.*;

public class EntityManagerFlowFilterTest {

    private EntityManagerProvider entityManagerProvider;

    @Before
    public void before(){
        entityManagerProvider = createMock(EntityManagerProvider.class);
    }

    @Test
    public void testInit() throws Exception {
        //entityManagerProvider.init();
        //expectLastCall().times(1);
        replay(entityManagerProvider);
        final EntityManagerFlowFilter filter = new EntityManagerFlowFilter(entityManagerProvider);
        filter.init(null);
        verify(entityManagerProvider);
    }

    @Test
    public void testDoFilter() throws Exception{
        entityManagerProvider.create();
        expectLastCall().times(1);
        entityManagerProvider.close();
        expectLastCall().times(1);
        entityManagerProvider.applyGlobalFilters();
        expectLastCall().times(1);
        final FilterChain filterChain = createMock(FilterChain.class);
        final ServletRequest request = createMock(ServletRequest.class);
        final ServletResponse response = createMock(ServletResponse.class);
        filterChain.doFilter(request, response);
        expectLastCall().times(1);

        replay(entityManagerProvider, filterChain, request, response);
        final EntityManagerFlowFilter filter = new EntityManagerFlowFilter(entityManagerProvider);
        filter.doFilter(request, response, filterChain);
        verify(entityManagerProvider);
    }
}
