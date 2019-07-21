package com.hawcore.framework.multipleds.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@ImportResource("classpath:multipeds/spring-context.xml")
@EnableTransactionManagement
@MapperScan("${multiple.datasource.base-mapper-package}")
public class MultipleDSAutoConfiguration {
    @Autowired
    private Environment ev;

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setLocalPage(true);
        return paginationInterceptor;
    }

//    @Bean(name = "db1")
//    @ConfigurationProperties(prefix = "spring.datasource.druid.db1")
//    public DataSource db1() {
//        return DruidDataSourceBuilder.create().build();
//    }

//    @Bean(name = "db2")
//    @ConfigurationProperties(prefix = "spring.datasource.druid.db2")
//    public DataSource db2() {
//        return DruidDataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "db3")
//    @ConfigurationProperties(prefix = "spring.datasource.druid.db3")
//    public DataSource db3() {
//        return DruidDataSourceBuilder.create().build();
//    }

    /**
     * 动态数据源配置
     *
     * @return
     */
    @Bean
    @Primary
    @ConditionalOnProperty("multiple.datasource.service-list")
    public DataSource multipleDataSource() throws Exception {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = buildTargetDataSources();
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(targetDataSources.get(getFirstServiceName()));
        return dynamicDataSource;
    }

    public Map<Object, Object> buildTargetDataSources() throws Exception {
        String serviceListStr = ev.getProperty("multiple.datasource.service-list");
        if (null == serviceListStr || serviceListStr.length() < 1) {
            throw new RuntimeException("使用多数据源必须配置:multiple.datasource.service-list");
        }
        String[] serviceNameArr = serviceListStr.split(",");
        Map<Object, Object> targetDataSources = new HashMap();

        for (String serviceName : serviceNameArr) {
            targetDataSources.put(serviceName, buildDataSource(serviceName));
        }

        return targetDataSources;
    }

    private DataSource buildDataSource(String serviceName) throws Exception {
        Properties properties = new Properties();
        properties.setProperty("url", ev.getProperty("multiple.datasource." + serviceName + ".url"));
        properties.setProperty("driverClassName", ev.getProperty("multiple.datasource." + serviceName + ".driver-class-name"));
        properties.setProperty("username", ev.getProperty("multiple.datasource." + serviceName + ".username"));
        properties.setProperty("password", ev.getProperty("multiple.datasource." + serviceName + ".password"));
        DataSource db = DruidDataSourceFactory.createDataSource(properties);
        return db;
    }

    private String getFirstServiceName() {
        String serviceListStr = ev.getProperty("multiple.datasource.service-list");
        return serviceListStr.split(",")[0];
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(multipleDataSource());

        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        sqlSessionFactory.setConfiguration(configuration);
        //PerformanceInterceptor(),OptimisticLockerInterceptor()
        //添加分页功能
        sqlSessionFactory.setPlugins(new Interceptor[]{
                paginationInterceptor()
        });
//        sqlSessionFactory.setGlobalConfig(globalConfiguration());
        return sqlSessionFactory.getObject();
    }

 /*   @Bean
    public GlobalConfiguration globalConfiguration() {
        GlobalConfiguration conf = new GlobalConfiguration(new LogicSqlInjector());
        conf.setLogicDeleteValue("-1");
        conf.setLogicNotDeleteValue("1");
        conf.setIdType(0);
        conf.setMetaObjectHandler(new MyMetaObjectHandler());
        conf.setDbColumnUnderline(true);
        conf.setRefresh(true);
        return conf;
    }*/
}
