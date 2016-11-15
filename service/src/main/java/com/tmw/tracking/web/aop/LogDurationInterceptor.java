package com.tmw.tracking.web.aop;

import java.util.UUID;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tmw.tracking.utils.Utils;

/**
 * Created by vitusya on 10/22/14.
 */
public class LogDurationInterceptor implements MethodInterceptor {

    protected final static Logger logger = LoggerFactory.getLogger(LogDurationInterceptor.class);

    /**
     * {@inheritDoc}
     *
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    @Override
    public Object invoke(final MethodInvocation methodInvocation) throws Throwable {
        long start = System.currentTimeMillis();
        String target = methodInvocation.getThis().getClass().getSuperclass().getName() + "." + methodInvocation.getMethod().getName();
        MDC.put(Utils.MDC_CALL_ID, String.valueOf(UUID.randomUUID()));
        logger.info("Call soap method: " + target);
        try {
            return methodInvocation.proceed();
        } finally {
            MDC.put(Utils.MDC_DURATION, String.valueOf(System.currentTimeMillis() - start));
            logger.info("Processed soap method: " + target);
            MDC.remove(Utils.MDC_DURATION);
            MDC.remove(Utils.MDC_CALL_ID);
        }
    }
}