package org.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.common.util.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgisContainerProvider;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Configuration
@PropertySource("application.properties")
@Transactional
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestConfig {

    @Autowired
    private Environment env;
    static DataSource datasource;

    @Container
    static final JdbcDatabaseContainer<?> container = new PostgisContainerProvider().newInstance().withDatabaseName("db")
            .withUsername("user").withPassword("password").withInitScript("sql/create.sql");

    static {
        container.start();

        HikariConfig config = new HikariConfig();
        JdbcDatabaseContainer<?> jdbcContainer = (JdbcDatabaseContainer<?>) container;
        config.setJdbcUrl(jdbcContainer.getJdbcUrl());
        config.setUsername(jdbcContainer.getUsername());
        config.setPassword(jdbcContainer.getPassword());
        config.setDriverClassName(jdbcContainer.getDriverClassName());
        datasource = new HikariDataSource(config);
    }
    @Bean
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager tm = new DataSourceTransactionManager(datasource);
        tm.setDefaultTimeout(env.getProperty("valid.request.timeout.sec", Integer.class, -1));
        return tm;
    }

    @Bean
    public SqlSessionFactory sqlAppSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(datasource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        factoryBean.setTypeAliasesPackage(env.getProperty("app.alias", "org.example.api.model"));
        factoryBean.setTypeHandlersPackage(
                env.getProperty("app.handler", "it.movyon.nvt.handler,it.autostradetech.npt.handler"));
        factoryBean.setMapperLocations(
                resolver.getResources(env.getProperty("app.mapper", "classpath*:MyBatis/*.xml")));
        String executorStr = env.getProperty("app.exe");
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCacheEnabled(false);
        if (StringUtils.isNotBlank(executorStr)) {
            configuration.setDefaultExecutorType(ExecutorType.valueOf(executorStr));
            factoryBean.setConfiguration(configuration);
        }
        factoryBean.setConfiguration(configuration);
        return factoryBean.getObject();
    }
}
