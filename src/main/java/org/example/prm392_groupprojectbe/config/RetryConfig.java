package org.example.prm392_groupprojectbe.config;

import org.example.prm392_groupprojectbe.config.listener.CustomRetryListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.support.RetryTemplate;

@EnableRetry
@Configuration
public class RetryConfig {

    @Bean
    public RetryTemplate retryTemplate(CustomRetryListener retryListener) {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.registerListener(retryListener);
        return retryTemplate;
    }
}
