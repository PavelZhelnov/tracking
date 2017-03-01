package com.tmw.tracking.web.aop;

import com.tmw.tracking.entity.User;
import com.tmw.tracking.service.impl.AuthenticationServiceImpl;
import com.tmw.tracking.utils.Utils;
import com.tmw.tracking.web.controller.AnonController;
import com.tmw.tracking.web.controller.MainController;
import com.tmw.tracking.web.controller.MonitoringController;
import com.tmw.tracking.web.controller.UserController;
import com.tmw.tracking.web.service.auth.AuthenticationResource;
import com.tmw.tracking.web.service.exceptions.ServiceException;
import com.tmw.tracking.web.service.util.error.ErrorCode;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.shiro.SecurityUtils;

import java.lang.reflect.Method;
import java.util.UUID;

public class ServiceMethodInterceptor implements MethodInterceptor {

    private static final Logger logger = Logger.getLogger(ServiceMethodInterceptor.class);

    /**
     * {@inheritDoc}
     *
     * @see MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    @Override
    public Object invoke(final MethodInvocation methodInvocation) throws Throwable {
        long start = System.currentTimeMillis();
        String target = methodInvocation.getThis().getClass().getSuperclass().getName() + "." + methodInvocation.getMethod().getName();
        MDC.put(Utils.MDC_API_METHOD, target);
        MDC.put(Utils.MDC_REQUEST_ID, String.valueOf(UUID.randomUUID()));
        try {
            User user = AuthenticationServiceImpl.getAuthenticatedUser();
            if (user != null) {
                MDC.put(Utils.MDC_USER, user.getEmail());
            }
        } catch (Throwable t) {}

        logger.info("Call api method: " + target);
        try {
            isAuthenticatedCheck(methodInvocation);
            return methodInvocation.proceed();
        } catch (ServiceException e) {
                logger.warn((String) e.getResponse().getEntity());
            throw e;
        } catch (Exception e) {
            String reason = StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : e.getClass().getSimpleName();
            logger.error(reason,e);
            //TODO check what's the type of exception
            throw new ServiceException(reason, ErrorCode.INTERNAL_SERVER_ERROR);
        } finally {
            MDC.put(Utils.MDC_DURATION, String.valueOf(System.currentTimeMillis() - start));
            logger.info("Processed api method: " + target);
            MDC.clear();
        }
    }

    private void isAuthenticatedCheck(final MethodInvocation methodInvocation) {
        Method method = methodInvocation.getMethod();
        Class<?> declaringClass = method.getDeclaringClass();
        String methodName = method.getName();
        String declaringClassName = declaringClass.getName();
        if (!(declaringClassName.equals(AuthenticationResource.class.getName())
                || declaringClassName.equals(MonitoringController.class.getName())
                || declaringClassName.equals(AnonController.class.getName())
                || declaringClassName.equals(UserController.class.getName())
                || (declaringClassName.equals(MainController.class.getName())
                && (methodName.equals("loginGet") || methodName.equals("loginPost"))))
                && !SecurityUtils.getSubject().isAuthenticated())
            throw new ServiceException("Token is invalid. User is not logged.", ErrorCode.AUTH_ERROR_TOKEN_IS_INVALID);
    }

}
