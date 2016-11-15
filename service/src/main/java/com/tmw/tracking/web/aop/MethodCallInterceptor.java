package com.tmw.tracking.web.aop;

import java.util.UUID;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tmw.tracking.utils.Utils;

/**
 * Created by ankultepin on 21.09.2015.
 */
public class MethodCallInterceptor implements MethodInterceptor {

    protected final static Logger logger = LoggerFactory.getLogger(MethodCallInterceptor.class);

    /**
     * {@inheritDoc}
     *
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    @Override
    public Object invoke(final MethodInvocation methodInvocation) throws Throwable {
        long start = System.currentTimeMillis();
        String target = methodInvocation.getThis().getClass().getSuperclass().getName() + "." + methodInvocation.getMethod().getName();
        String originalMethod = (String) MDC.get(Utils.MDC_METHOD);
        String originalRequestId = (String) MDC.get(Utils.MDC_REQUEST_ID);
        MDC.put(Utils.MDC_METHOD, target);

        if(StringUtils.isBlank(originalRequestId)) {
            MDC.put(Utils.MDC_REQUEST_ID, String.valueOf(UUID.randomUUID()));
        }
        logger.info("Call method: " + target);
        try {
            return methodInvocation.proceed();
        } catch (Throwable e) {
            logger.error("Error during execution " + this.getClass().getName() + " " + e.getMessage(), e);
            throw e;
        } finally {
            MDC.put(Utils.MDC_DURATION, String.valueOf(System.currentTimeMillis() - start));
            logger.info("Processed method: " + target);
            MDC.remove(Utils.MDC_DURATION);
            if(StringUtils.isNotBlank(originalMethod)) {
                MDC.put(Utils.MDC_METHOD, originalMethod);
            } else {
                MDC.remove(Utils.MDC_METHOD);
            }
            if(StringUtils.isBlank(originalRequestId)) {
                MDC.remove(Utils.MDC_REQUEST_ID);
            }
        }
    }

}
