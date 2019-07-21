package com.hawcore.framework.multipleds.config;

import com.hawcore.framework.multipleds.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbContextHolder {

    private static Logger logger = LoggerFactory.getLogger(DbContextHolder.class);

    private static final ThreadLocal contextHolder = new ThreadLocal();

    private static final ThreadLocal<Stack<String>> dsContextHolder = new InheritableThreadLocal<Stack<String>>() {
        @Override
        protected Stack<String> initialValue() {
            return new Stack();
        }
    };

    /**
     * 设置数据源
     *
     * @param serviceName
     */
    public static void setDbType(String serviceName) {
        contextHolder.set(serviceName);
        dsContextHolder.get().push(serviceName);
        logger.debug("Switch datasource to: " + serviceName);
    }

    /**
     * 取得当前数据源
     *
     * @return
     */
    public static String getDbType() {
        return (String) contextHolder.get();
    }

    /**
     * 方法执行完，需要回退至上一个数据源
     */
    public static void reback() {
        String serviceName = dsContextHolder.get().pop();
        logger.debug("Release datasource: " + serviceName);
        contextHolder.set(dsContextHolder.get().getTop());
        logger.debug("Switch datasource back to: " + dsContextHolder.get().getTop());
    }

    /**
     * 清除上下文数据
     */
    public static void clearDbType() {
        contextHolder.remove();
    }
}
