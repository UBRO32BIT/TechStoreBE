package org.example.prm392_groupprojectbe.config.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.stereotype.Component;

@Component
public class CustomRetryListener implements RetryListener {

    private static final Logger log = LoggerFactory.getLogger(CustomRetryListener.class);

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        log.warn("Retry attempt {} failed: {}", context.getRetryCount(), throwable.getMessage());
    }
}
