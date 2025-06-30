package com.github.mathbook3948.zzibot.config;

import com.github.mathbook3948.zzibot.handler.StringArrayTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class WebConfig {

    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> configuration.getTypeHandlerRegistry()
                .register(List.class, JdbcType.ARRAY, new StringArrayTypeHandler());
    }

}
