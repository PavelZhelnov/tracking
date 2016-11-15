package com.tmw.tracking.filter;

import org.apache.log4j.MDC;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by pzhelnov on 3/9/2016.
 */
public class TrackingWebHeadersFilter implements Filter {

    private final static Logger logger = LoggerFactory.getLogger(TrackingWebHeadersFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doBeforeRequestProcessing(request);
        chain.doFilter(request, response);
        doAfterRequestProcessing();
    }

    private void doBeforeRequestProcessing(ServletRequest request) {
    }

    private void doAfterRequestProcessing() {
    }

    private void cleanParameter(String name) {
        MDC.remove(name);
    }

    private void addParameter(String name, ServletRequest request) {
        Enumeration<String> element = ((ShiroHttpServletRequest) request).getHeaders(name.toLowerCase());
        if (element != null && element.hasMoreElements()) {
            MDC.put(name, element.nextElement());
        }
    }

    @Override
    public void destroy() {

    }
}
