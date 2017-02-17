package com.tmw.tracking.web.hibernate;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class EntityManagerFlowFilter implements Filter {

    private final static Logger logger = LoggerFactory.getLogger(EntityManagerFlowFilter.class);

    private final EntityManagerProvider entityManagerProvider;

    @Inject
    public EntityManagerFlowFilter(EntityManagerProvider entityManagerProvider) {
        this.entityManagerProvider = entityManagerProvider;
    }

    /**
     * {@inheritDoc}
     * @see Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
//        entityManagerProvider.init();
    }

    /**
     * {@inheritDoc}
     * @see Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        entityManagerProvider.create();
        try {
            // enable global filters
            entityManagerProvider.applyGlobalFilters();
            chain.doFilter(request, response);
            final String agent = request instanceof HttpServletRequestWrapper ? ((HttpServletRequestWrapper) request).getHeader("User-Agent") : null;
            if(response instanceof HttpServletResponse && ((HttpServletResponse) response).getStatus() != 200 && StringUtils.isNotBlank(agent)) {
                logger.warn("Response status is not 200. " + agent);
            }
        }finally {
            entityManagerProvider.close();
        }
    }

    /**
     * {@inheritDoc}
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        entityManagerProvider.destroy();
    }

}
