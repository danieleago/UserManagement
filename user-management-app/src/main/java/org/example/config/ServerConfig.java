package org.example.config;

import com.google.gson.GsonBuilder;
import io.micrometer.common.util.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.serde.OffsetDateTimeAdapter;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.time.OffsetDateTime;
import java.util.List;

@Configuration
@Import({BusinessConfig.class})
public class ServerConfig implements WebMvcConfigurer {

    @Autowired
    private Environment env;
    @Bean
    public DataSourceProperties todosDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource todosDataSource() {
        DataSourceProperties prop = todosDataSourceProperties();
        prop.setUrl(prop.getUrl() + "?ApplicationName=USR_MNGM");
        return prop.initializeDataSourceBuilder().build();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0,
                new GsonHttpMessageConverter(
                        (new GsonBuilder().registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
                                .serializeNulls().create())));
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource datasource) {
        DataSourceTransactionManager tm = new DataSourceTransactionManager(datasource);
        tm.setDefaultTimeout(env.getProperty("valid.request.timeout.sec", Integer.class, -1));
        return tm;
    }

    @Bean
    public SqlSessionFactory sqlAppSessionFactory(DataSource ds) throws Exception {

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(ds);
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
