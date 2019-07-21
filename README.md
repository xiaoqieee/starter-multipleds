# starter-multipleds
多数据源集成框架


# 需要在业务系统中添加的配置：
multiple.datasource.service-list=db,db1,db2   #业务服务中引用的多个数据源代号（第一个为默认数据源）
multiple.datasource.base-service-package=com.hawcore.framework.multiplyds.service   #业务服务中service层的包名
multiple.datasource.base-mapper-package=com.hawcore.framework.multiplyds.mapper     #业务服务中mapper层的包名

# 以下为对各个数据源的配置
multiple.datasource.db.driver-class-name=com.mysql.cj.jdbc.Driver
multiple.datasource.db.url=jdbc:mysql://localhost:3306/test_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
multiple.datasource.db.username=root
multiple.datasource.db.password=123456

multiple.datasource.db1.driver-class-name=com.mysql.cj.jdbc.Driver
multiple.datasource.db1.url=jdbc:mysql://localhost:3306/test_db1?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
multiple.datasource.db1.username=root
multiple.datasource.db1.password=123456

multiple.datasource.db2.driver-class-name=com.mysql.cj.jdbc.Driver
multiple.datasource.db2.url=jdbc:mysql://localhost:3306/test_db2?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
multiple.datasource.db2.username=root
multiple.datasource.db2.password=123456

#注意：multiple.datasource.base-service-package和multiple.datasource.base-mapper-package分别配置业务服务的service层代码和mapper层代码的基础包名，
即在此包下分别创建各个数据源对应的包来放各个数据源对应Service或者Mapper的class类代码，包名需要与multiple.datasource.service-list中定义的一致。
