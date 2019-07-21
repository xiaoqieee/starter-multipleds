package com.hawcore.framework.multipleds.aop;

import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;

//@Component

public class MultipleDatasourcePointcut extends StaticMethodMatcherPointcut {

    @Autowired
    private Environment ev;

//    @Override
    public boolean matches(Method method, Class<?> aClass) {
        return aClass.getName().startsWith(ev.getProperty("multiple.datasource.base-service-package"));
    }

    public MultipleDatasourcePointcut() {
    }

}
