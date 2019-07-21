package com.hawcore.framework.multipleds.aop;

import com.hawcore.framework.multipleds.config.DbContextHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

//@Component
public class MultipleDatasourceMethodAdvice implements MethodInterceptor {

    @Autowired
    private Environment ev;

//    @Override
//    public void before(Method method, Object[] objects, Object o) throws Throwable {
//        String className = o.getClass().getName();
//        System.out.println(className);
//        System.out.println("className");
//    }

//    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        String className = methodInvocation.getMethod().getDeclaringClass().getName();
//        System.out.println(className);
//        System.out.println("调用RoundService之前成功");
        DbContextHolder.setDbType(getServiceName(className));
        try {
            Object result = methodInvocation.proceed();
            return result;
        } finally {
            DbContextHolder.reback();
//            System.out.println("调用RoundService之后成功");
        }
    }

    private String getServiceName(String className) {
        String basePackage = ev.getProperty("multiple.datasource.base-service-package");
        String suffix = className.replace(basePackage + ".", "");
        return suffix.split("\\.")[0];
    }
}
