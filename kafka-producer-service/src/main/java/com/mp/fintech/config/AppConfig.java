package com.mp.fintech.config;

import com.mp.fintech.entity.AuthorizationStatus;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public AuthorizationStatus authorizationStatus() {
        return new AuthorizationStatus();
    }
}
