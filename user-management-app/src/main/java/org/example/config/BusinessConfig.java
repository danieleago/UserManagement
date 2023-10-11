package org.example.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.example.mapper.UserManagementMapper;
import org.example.service.UserManagementService;
import org.example.service.impl.UserManagementServiceImpl;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BusinessConfig {

    @Bean
    public MapperFactoryBean<UserManagementMapper> userManagerMapper(SqlSessionFactory factory) {
        MapperFactoryBean<UserManagementMapper> factoryBean = new MapperFactoryBean<>(UserManagementMapper.class);
        factoryBean.setSqlSessionFactory(factory)
        ;
        return factoryBean;
    }

    @Bean
    public UserManagementService getUserManagementService(){
        return new UserManagementServiceImpl();
    }
}
