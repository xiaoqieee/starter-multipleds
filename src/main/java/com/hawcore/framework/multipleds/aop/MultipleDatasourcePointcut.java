package com.hawcore.framework.multipleds.aop;

import com.hawcore.framework.multipleds.exception.MultipleDSException;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;


public class MultipleDatasourcePointcut extends StaticMethodMatcherPointcut {

    @Autowired
    private Environment ev;

    public boolean matches(Method method, Class<?> aClass) {
        String baseServicePackage = ev.getProperty("multiple.datasource.base-service-package");
        if (null == baseServicePackage || baseServicePackage.length() < 1) {
            throw new MultipleDSException("Required configuration [multiple.datasource.base-service-package]");
        }
        return aClass.getName().startsWith(ev.getProperty("multiple.datasource.base-service-package"));
    }

    public MultipleDatasourcePointcut() {
    }

}
