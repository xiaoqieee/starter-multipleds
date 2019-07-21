package com.hawcore.framework.multipleds.aop;

import com.hawcore.framework.multipleds.config.DbContextHolder;
import com.hawcore.framework.multipleds.exception.MultipleDSException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class MultipleDatasourceMethodAdvice implements MethodInterceptor {

    private Logger logger = LoggerFactory.getLogger(MultipleDatasourceMethodAdvice.class);

    @Autowired
    private Environment ev;

    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        String className = methodInvocation.getMethod().getDeclaringClass().getName();
        String serviceName = getServiceName(className);
        logger.debug("Set datasource service name [{}]", serviceName);
        DbContextHolder.setDbType(serviceName);
        try {
            Object result = methodInvocation.proceed();
            return result;
        } finally {
            logger.debug("Back to datasource service name [{}]", serviceName);
            DbContextHolder.reback();
        }
    }

    private String getServiceName(String className) {
        String basePackage = ev.getProperty("multiple.datasource.base-service-package");
        if (null == basePackage || basePackage.length() < 1) {
            throw new MultipleDSException("Required configuration [multiple.datasource.base-service-package]");
        }
        String suffix = className.replace(basePackage + ".", "");
        if (!suffix.contains(".")) {
            return null;
        }
        if (suffix.length() < 1 || !suffix.contains(".")) {
            throw new MultipleDSException("Invalid configuration [multiple.datasource.base-service-package] or Invalid service package path");
        }

        String serviceName = suffix.split("\\.")[0];
        if (null == serviceName || serviceName.length() < 1) {
            throw new MultipleDSException("Invalid service package path, it's package path must prefix [{" + basePackage + "}]");
        }
        return serviceName;
    }
}
